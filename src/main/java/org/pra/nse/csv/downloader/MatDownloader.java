package org.pra.nse.csv.downloader;

import org.pra.nse.AppConstants;
import org.pra.nse.util.DownloadUtils;
import org.pra.nse.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class MatDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatDownloader.class);

    private final FileUtils fileUtils;
    private final DownloadUtils downloadFile;

    public MatDownloader(FileUtils fileUtils, DownloadUtils downloadFile) {
        this.fileUtils = fileUtils;
        this.downloadFile = downloadFile;
    }

    public void download() {
        String dataDir = AppConstants.BASE_DATA_DIR + File.separator + AppConstants.MTO_DIR_NAME;
        List<String> filesToBeDownloaded = fileUtils.constructFileNames(
                AppConstants.DOWNLOAD_FROM_DATE,
                AppConstants.MTO_FILE_NAME_DATE_FORMAT,
                AppConstants.MTO_FILE_PREFIX,
                AppConstants.MTO_FILE_SUFFIX);
        filesToBeDownloaded.removeAll(fileUtils.fetchFileNames(dataDir, null, null));
        List<String> filesDownloadUrl = fileUtils.constructFileDownloadUrl(
                AppConstants.MTO_BASE_URL, filesToBeDownloaded);

        filesDownloadUrl.parallelStream().forEach( fileUrl -> {
            downloadFile.downloadFile(fileUrl, dataDir,
                    () -> (dataDir + File.separator + fileUrl.substring(47,63)),
                    this::transformToCsv
            );
        });
    }

    private void transformToCsv(String downloadedDirAndFileName) {
        int firstIndex = downloadedDirAndFileName.lastIndexOf("_");
        String matCsvFileName = downloadedDirAndFileName.substring(firstIndex-3,firstIndex+9) + ".csv";
        String toFile = AppConstants.BASE_DATA_DIR + File.separator + AppConstants.MTO_DIR_NAME + File.separator + matCsvFileName;
        Map.Entry<String, Integer> entry = new AbstractMap.SimpleEntry<>("key", 0);
        File csvOutputFile = new File(toFile);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            try (Stream<String> stream = Files.lines(Paths.get(downloadedDirAndFileName))) {
                stream.filter(line->{
                    if(entry.getValue() < 3) {
                        entry.setValue(entry.getValue()+1);
                        return false;
                    } else {
                        return true;
                    }
                }).map(row -> {
                    if(entry.getValue() == 3) {
                        entry.setValue(entry.getValue()+1);
                        return "RecType,SrNo,Symbol,SecurityType,TradedQty,DeliverableQty,DeliveryToTradeRatio";
                    } else {
                        return row;
                    }
                }).forEach(pw::println);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}