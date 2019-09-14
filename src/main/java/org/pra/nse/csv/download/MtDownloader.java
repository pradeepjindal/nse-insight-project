package org.pra.nse.csv.download;

import org.pra.nse.ApCo;
import org.pra.nse.util.DateUtils;
import org.pra.nse.util.DownloadUtils;
import org.pra.nse.util.NseFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Component
public class MtDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(MtDownloader.class);

    private final NseFileUtils nseFileUtils;
    private final DownloadUtils downloadFile;

    public MtDownloader(NseFileUtils nseFileUtils, DownloadUtils downloadFile) {
        this.nseFileUtils = nseFileUtils;
        this.downloadFile = downloadFile;
    }

    public void download(LocalDate downloadFromDate) {
        String dataDir = ApCo.BASE_DATA_DIR + File.separator + ApCo.MT_DIR_NAME;
        List<String> filesToBeDownloaded = nseFileUtils.constructFileNames(
                downloadFromDate,
                ApCo.MT_FILE_NAME_DATE_FORMAT,
                ApCo.MT_NSE_FILE_PREFIX,
                ApCo.MT_FILE_EXT);
        filesToBeDownloaded.removeAll(nseFileUtils.fetchFileNames(dataDir, null, null));
        List<String> filesDownloadUrl = nseFileUtils.constructFileDownloadUrl(
                ApCo.MT_BASE_URL, filesToBeDownloaded);

        filesDownloadUrl.stream().forEach( fileUrl -> {
            downloadFile.downloadFile(fileUrl, dataDir,
                    () -> (dataDir + File.separator + fileUrl.substring(ApCo.MT_BASE_URL.length()+1,63)),
                    downloadedFilePathAndName -> {
                        transformToCsvNew(downloadedFilePathAndName);
                    }
            );
        });
    }

    private void transformToCsvNew(String downloadedDirAndFileName) {
        int firstIndex = downloadedDirAndFileName.lastIndexOf("_");
        String csvFileName = ApCo.MT_DATA_FILE_PREFIX
                            + DateUtils.transformDate(downloadedDirAndFileName.substring(firstIndex+1, firstIndex+9))
                            + ApCo.PRA_DATA_FILE_EXT;
        String toFile = ApCo.BASE_DATA_DIR + File.separator + ApCo.MT_DIR_NAME + File.separator + csvFileName;
        AtomicInteger atomicInteger = new AtomicInteger();
        File csvOutputFile = new File(toFile);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            try (Stream<String> stream = Files.lines(Paths.get(downloadedDirAndFileName))) {
                stream.filter(line-> atomicInteger.incrementAndGet() > 3)
                        .map(row -> {
                    if(atomicInteger.get() == 4) {
                        return "RecType,SrNo,Symbol,SecurityType,TradedQty,DeliverableQty,DeliveryToTradeRatio";
                    } else {
                        return row;
                    }
                }).forEach(pw::println);
            } catch (IOException e) {
                LOGGER.warn("some error in MAT entry: {}", e);
            }
        } catch (FileNotFoundException e) {
            LOGGER.warn("some error: {}", e);
        }
    }

}
