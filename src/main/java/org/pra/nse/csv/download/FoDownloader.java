package org.pra.nse.csv.download;

import org.pra.nse.ApCo;
import org.pra.nse.util.DownloadUtils;
import org.pra.nse.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
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

    public void download(LocalDate downloadFromDate) {
        String dataDir = ApCo.BASE_DATA_DIR + File.separator + ApCo.FO_DIR_NAME;
        List<String> filesToBeDownloaded = fileUtils.constructFileNames(
                downloadFromDate,
                ApCo.FO_FILE_NAME_DATE_FORMAT,
                ApCo.FO_NSE_FILE_PREFIX,
                ApCo.FO_FILE_SUFFIX);
        filesToBeDownloaded.removeAll(fileUtils.fetchFileNames(dataDir, null, null));
        List<String> filesDownloadUrl = fileUtils.constructFileDownloadUrlWithYearAndMonth(
                ApCo.FO_BASE_URL, filesToBeDownloaded);

        filesDownloadUrl.parallelStream().forEach( fileUrl -> {
            downloadFile.downloadFile(fileUrl, dataDir,
                    () -> (dataDir + File.separator + fileUrl.substring(65, 88)),
                    zipFilePathAndName -> {
                        try {
                            //fileUtils.unzip(zipFilePathAndName);
                            fileUtils.unzipNew(zipFilePathAndName, ApCo.FO_DATA_FILE_PREFIX);
                        } catch (IOException e) {
                            LOGGER.warn("Error while downloading file: {}", e);
                        }
                    });
        });
    }

}
