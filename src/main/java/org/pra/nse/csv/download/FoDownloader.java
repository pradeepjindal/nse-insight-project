package org.pra.nse.csv.download;

import org.pra.nse.ApCo;
import org.pra.nse.util.DownloadUtils;
import org.pra.nse.util.NseFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

@Component
public class FoDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FoDownloader.class);

    private final NseFileUtils nseFileUtils;
    private final DownloadUtils downloadFile;

    public FoDownloader(NseFileUtils nseFileUtils, DownloadUtils downloadFile) {
        this.nseFileUtils = nseFileUtils;
        this.downloadFile = downloadFile;
    }

    public void download(LocalDate downloadFromDate) {
        String dataDir = ApCo.BASE_DATA_DIR + File.separator + ApCo.FO_DIR_NAME;
        List<String> filesToBeDownloaded = nseFileUtils.constructFileNames(
                downloadFromDate,
                ApCo.FO_FILE_NAME_DATE_FORMAT,
                ApCo.FO_NSE_FILE_PREFIX,
                ApCo.FO_FILE_SUFFIX);
        filesToBeDownloaded.removeAll(nseFileUtils.fetchFileNames(dataDir, null, null));
        List<String> filesDownloadUrl = nseFileUtils.constructFileDownloadUrlWithYearAndMonth(
                ApCo.FO_BASE_URL, filesToBeDownloaded);

        filesDownloadUrl.stream().forEach( fileUrl -> {
            downloadFile.downloadFile(fileUrl, dataDir,
                    () -> (dataDir + File.separator + fileUrl.substring(65, 88)),
                    zipFilePathAndName -> {
                        try {
                            nseFileUtils.unzipNew(zipFilePathAndName, ApCo.FO_DATA_FILE_PREFIX);
                        } catch (IOException e) {
                            LOGGER.warn("Error while downloading file: {}", e);
                        }
                    });
        });
    }

}
