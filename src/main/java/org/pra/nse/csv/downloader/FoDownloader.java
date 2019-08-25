package org.pra.nse.csv.downloader;

import org.pra.nse.AppConstants;
import org.pra.nse.util.DownloadUtils;
import org.pra.nse.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class FoDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FoDownloader.class);

    private final FileUtils fileUtils;
    private final DownloadUtils downloadFile;

    public FoDownloader(FileUtils fileUtils, DownloadUtils downloadFile) {
        this.fileUtils = fileUtils;
        this.downloadFile = downloadFile;
    }

    public void download() {
        String dataDir = AppConstants.BASE_DATA_DIR + File.separator + AppConstants.FO_DIR_NAME;
        List<String> filesToBeDownloaded = fileUtils.constructFileNames(
                AppConstants.DOWNLOAD_FROM_DATE,
                AppConstants.FO_FILE_NAME_DATE_FORMAT,
                AppConstants.FO_FILE_PREFIX,
                AppConstants.FO_FILE_SUFFIX);
        filesToBeDownloaded.removeAll(fileUtils.fetchFileNames(dataDir, null, null));
        List<String> filesDownloadUrl = fileUtils.constructFileDownloadUrlWithYearAndMonth(
                AppConstants.FO_BASE_URL, filesToBeDownloaded);

        filesDownloadUrl.parallelStream().forEach( fileUrl -> {
            downloadFile.downloadFile(fileUrl, dataDir,
                    () -> (dataDir + File.separator + fileUrl.substring(65, 88)),
                    filePathAndName -> {
                        try {
                            fileUtils.unzip(filePathAndName);
                        } catch (IOException e) {
                            LOGGER.warn("Error while downloading file: {}", e);
                        }
                    });
        });
    }

}
