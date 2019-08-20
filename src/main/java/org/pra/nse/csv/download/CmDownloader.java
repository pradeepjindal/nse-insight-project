package org.pra.nse.csv.download;

import org.pra.nse.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CmDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmDownloader.class);

    private FileUtils fileUtils = new FileUtils();

    public void download() {
        String dataDir = System.getProperty("user.home") + File.separator + "pra-nse-cm";
        List<String> filesToBeDownloaded = constructFileNames();
        List<String> filesDownloadUrl = constructFileDownloadUrl(filesToBeDownloaded);

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

    private List<String> constructFileDownloadUrl(List<String> filesToBeDownloaded) {
        //String baseUrl = "https://www.nseindia.com/content/historical/DERIVATIVES/2019";
        //String baseUrl = "https://www.nseindia.com/content/historical/EQUITIES/2019/AUG/cm16AUG2019bhav.csv.zip";
        String baseUrl = "https://www.nseindia.com/content/historical/EQUITIES/2019";
        List<String> filesUrl;

        filesUrl = filesToBeDownloaded.stream().map(fileName -> {
            //LOGGER.info(fileName);
            return baseUrl + "/" + fileName.substring(4, 7) + "/" + fileName;
        }).collect(Collectors.toList());
        //String newUrl = baseUrl + "/" + localDate.getMonth().name().substring(0,3) + "/fo" + formatter.format(localDate).toUpperCase() + "bhav.csv.zip";
        filesUrl.forEach(url -> LOGGER.info(url));
        return filesUrl;
    }

    private List<String> constructFileNames() {
        List<String> fileNamesToBeDownloaded = new ArrayList<>();
        String foBhavCopy = "https://www.nseindia.com/content/historical/DERIVATIVES/2019/AUG/fo14AUG2019bhav.csv.zip";
        LocalDate localDate = LocalDate.of(2019,8,1);
        //LOGGER.info(localDate);
        //localDate.getMonth().name().substring(0,3);
        //LOGGER.info(localDate.getMonth().name().substring(0,3));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMyyyy");
        //formatter.format(localDate)
        //LOGGER.info(formatter.format(localDate));

        LOGGER.info(System.getProperty("user.name"));
        LOGGER.info(System.getProperty("user.home"));
        LOGGER.info(System.getProperty("user.dir"));

        LocalDate todayDate = LocalDate.now();
        while(localDate.compareTo(todayDate) < 1) {
            //LOGGER.info(localDate);
            //LOGGER.info(localDate.getDayOfWeek());
            if("SATURDAY".equals(localDate.getDayOfWeek().name()) || "SUNDAY".equals(localDate.getDayOfWeek().name())) {
                //LOGGER.info(localDate.getDayOfWeek());
            } else {
                //LOGGER.info(localDate.getDayOfWeek());
                String newFileName = "cm" + formatter.format(localDate).toUpperCase() + "bhav.csv.zip";
                //LOGGER.info(newFileName);
                fileNamesToBeDownloaded.add(newFileName);
            }
            localDate = localDate.plusDays(1);
        }

        //fileNamesToBeDownloaded.forEach(fileName -> LOGGER.info(fileName));
        LOGGER.info("Total File Count: " + fileNamesToBeDownloaded.size());

        List<String> existingFiles = fetchExistingFileNames();
        fileNamesToBeDownloaded.removeAll(existingFiles);
        LOGGER.info("No of files already exist: " + existingFiles.size());

        //LOGGER.info("-----Final File Name List to be downloaded");
        //fileNamesToBeDownloaded.forEach(fileName -> LOGGER.info(fileName));
        LOGGER.info("No of Files to be downloaded: " + fileNamesToBeDownloaded.size());
        return fileNamesToBeDownloaded;
    }

    public List<String> fetchExistingFileNames() {
        String dataDir = System.getProperty("user.home") + File.separator + "pra-nse-cm";
        File folder = new File(dataDir);
        File[] listOfFiles = folder.listFiles();

        if(null == folder.listFiles()) {
            fileUtils.createDataDir(dataDir);
            listOfFiles = folder.listFiles();
        }
        //if(listOfFiles == null) createDataDir(dataDir);

        List<String> existingFiles = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                //LOGGER.info("File " + listOfFiles[i].getName());
                existingFiles.add(listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                LOGGER.info("Directory " + listOfFiles[i].getName());
            }
        }
        return existingFiles;
    }

}