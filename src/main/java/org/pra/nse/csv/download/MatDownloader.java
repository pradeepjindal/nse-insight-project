package org.pra.nse.csv.download;

import org.pra.nse.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MatDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatDownloader.class);

    public void download() {
        String dataDir = System.getProperty("user.home") + File.separator + "pra-nse-mat";
        List<String> filesToBeDownloaded = constructFileNames();
        List<String> filesDownloadUrl = constructFileDownloadUrl(filesToBeDownloaded);

        filesDownloadUrl.stream().forEach( fileUrl -> {
        //filesDownloadUrl.forEach( fileUrl -> {
            String outputDirAndFileName = dataDir + File.separator + fileUrl.substring(47,63);
            LOGGER.info("URL: " + fileUrl);
            LOGGER.info("OUT: " + outputDirAndFileName);
            try (BufferedInputStream inputStream = new BufferedInputStream(new URL(fileUrl).openStream());
                 FileOutputStream fileOS = new FileOutputStream(outputDirAndFileName)) {
                byte data[] = new byte[1024];
                int byteContent;
                while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                    fileOS.write(data, 0, byteContent);
                }
                transformToCsv(outputDirAndFileName);
            } catch (IOException e) {
                LOGGER.info(e.getMessage());
            }
        });
    }

    private List<String> constructFileDownloadUrl(List<String> filesToBeDownloaded) {
        String baseUrl = "https://www.nseindia.com/archives/equities/mto";
        List<String> filesUrl;

        filesUrl = filesToBeDownloaded.stream().map(fileName -> {
            //LOGGER.info(fileName);
            return baseUrl + "/" + fileName;
        }).collect(Collectors.toList());
        //String newUrl = baseUrl + "/" + localDate.getMonth().name().substring(0,3) + "/fo" + formatter.format(localDate).toUpperCase() + "bhav.csv.zip";
        filesUrl.forEach(url -> LOGGER.info(url));
        return filesUrl;
    }

    private List<String> constructFileNames() {
        List<String> fileNamesToBeDownloaded = new ArrayList<>();
        LocalDate localDate = LocalDate.of(2019,8,1);
        //LOGGER.info(localDate);
        //localDate.getMonth().name().substring(0,3);
        //LOGGER.info(localDate.getMonth().name().substring(0,3));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
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
                LOGGER.info("It is weekend: {}", localDate.getDayOfWeek());
            } else {
                LOGGER.info("It is weekday: {}", localDate.getDayOfWeek());
                String newFileName = "MTO_" + formatter.format(localDate) + ".DAT";
                //LOGGER.info(newFileName);
                fileNamesToBeDownloaded.add(newFileName);
            }
            localDate = localDate.plusDays(1);
        }

        //fileNamesToBeDownloaded.forEach(fileName -> LOGGER.info(fileName));
        LOGGER.info("Total File Count (MAT): " + fileNamesToBeDownloaded.size());

        List<String> existingFiles = fetchExistingFileNames();
        fileNamesToBeDownloaded.removeAll(existingFiles);
        LOGGER.info("No of files already exist (MAT): " + existingFiles.size());

        //LOGGER.info("-----Final File Name List to be downloaded");
        //fileNamesToBeDownloaded.forEach(fileName -> LOGGER.info(fileName));
        LOGGER.info("No of Files to be downloaded (MAT): " + fileNamesToBeDownloaded.size());
        return fileNamesToBeDownloaded;
    }

    private List<String> fetchExistingFileNames() {
        String dataDir = System.getProperty("user.home") + File.separator + "pra-nse-mat";
        File folder = new File(dataDir);
        File[] listOfFiles = folder.listFiles();

        FileUtils fileUtils = new FileUtils();
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

    private void transformToCsv(String downloadedDirAndFileName) {
        String matCsvFileName = downloadedDirAndFileName.substring(downloadedDirAndFileName.lastIndexOf("_")-3, 42) + ".csv";
        String toFile = System.getProperty("user.home") + File.separator + "pra-nse-mat" + File.separator + matCsvFileName;
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
                }).forEach(tuple -> pw.println(tuple));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}