package org.pra.nse.csv.downloader;

import org.pra.nse.AppConstants;
import org.pra.nse.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class CmDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmDownloader.class);

    private FileUtils fileUtils = new FileUtils();

    public void download() {
        String dataDir = AppConstants.BASE_DATA_DIR + File.separator + AppConstants.CM_DIR_NAME;
        List<String> filesToBeDownloaded = fileUtils.constructFileNames(
                LocalDate.of(2019,8,1),
                AppConstants.CM_FILE_NAME_DATE_FORMAT,
                AppConstants.CM_FILE_PREFIX, AppConstants.CM_FILE_SUFFIX);
        filesToBeDownloaded.removeAll(fileUtils.fetchFileNames(dataDir, null, null));
        List<String> filesDownloadUrl = fileUtils.constructFileDownloadUrlWithYearAndMonth(
                AppConstants.CM_BASE_URL, filesToBeDownloaded);

        filesDownloadUrl.stream().forEach( fileUrl -> {
        //filesDownloadUrl.forEach( fileUrl -> {
            String outputDirAndFileName = dataDir + File.separator + fileUrl.substring(62,85);
            LOGGER.info("URL: " + fileUrl);
            LOGGER.info("OUT: " + outputDirAndFileName);
            try (BufferedInputStream inputStream = new BufferedInputStream(new URL(fileUrl).openStream());
                 FileOutputStream fileOS = new FileOutputStream(outputDirAndFileName)) {
                byte data[] = new byte[1024];
                int byteContent;
                while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                    fileOS.write(data, 0, byteContent);
                }
                fileUtils.unzip(outputDirAndFileName);
            } catch (IOException e) {
                LOGGER.info(e.getMessage());
            }
        });
    }

}
