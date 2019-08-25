package org.pra.nse.csv.downloader;

import org.pra.nse.AppConstants;
import org.pra.nse.util.DownloadUtils;
import org.pra.nse.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class CmDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmDownloader.class);

    private final FileUtils fileUtils;
    private final DownloadUtils downloadFile;

    public CmDownloader(FileUtils fileUtils, DownloadUtils downloadFile) {
        this.fileUtils = fileUtils;
        this.downloadFile = downloadFile;
    }

    public void download() {
        String dataDir = AppConstants.BASE_DATA_DIR + File.separator + AppConstants.CM_DIR_NAME;
        List<String> filesToBeDownloaded = fileUtils.constructFileNames(
                AppConstants.DOWNLOAD_FROM_DATE,
                AppConstants.CM_FILE_NAME_DATE_FORMAT,
                AppConstants.CM_FILE_PREFIX,
                AppConstants.CM_FILE_SUFFIX);
        filesToBeDownloaded.removeAll(fileUtils.fetchFileNames(dataDir, null, null));
        List<String> filesDownloadUrl = fileUtils.constructFileDownloadUrlWithYearAndMonth(
                AppConstants.CM_BASE_URL, filesToBeDownloaded);

        filesDownloadUrl.parallelStream().forEach( fileUrl -> {
            downloadFile.downloadFile(fileUrl, dataDir,
                    () -> (dataDir + File.separator + fileUrl.substring(62, 85)),
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