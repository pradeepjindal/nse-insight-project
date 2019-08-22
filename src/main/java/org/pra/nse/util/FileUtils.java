package org.pra.nse.util;

import org.pra.nse.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private DateTimeFormatter cmFormatter = DateTimeFormatter.ofPattern(AppConstants.CM_FILE_NAME_DATE_FORMAT);
    private String cmDir = AppConstants.BASE_DATA_DIR + File.separator + AppConstants.CM_DIR_NAME;

    private DateTimeFormatter foFormatter = DateTimeFormatter.ofPattern(AppConstants.FO_FILE_NAME_DATE_FORMAT);
    private String foDir = AppConstants.BASE_DATA_DIR + File.separator + AppConstants.FO_DIR_NAME;

    private DateTimeFormatter matFormatter = DateTimeFormatter.ofPattern(AppConstants.MTO_FILE_NAME_DATE_FORMAT);
    private String matDir = AppConstants.BASE_DATA_DIR + File.separator + AppConstants.MTO_DIR_NAME;


    public String getLatestFileNameForCm(int occurrence) {
        return getLatestFileNameForCm(LocalDate.now(), occurrence);
    }
    private String getLatestFileNameForCm(LocalDate localDate, int occurrence) {
        LocalDate date = localDate;
        File file;
        String filePathWithFileName = null;
        for(int i=0; i<occurrence; i++) {
            do {
                String fileName = "cm" + cmFormatter.format(date).toUpperCase() + ".csv";
                LOGGER.info("getLatestFileNameForCm | fileName: {}", fileName);
                filePathWithFileName = cmDir + File.separator + fileName;
                LOGGER.info("getLatestFileNameForCm | filePathWithFileName: {}", filePathWithFileName);
                file = new File(filePathWithFileName);
                date = date.minusDays(1);
            } while(!file.exists());
        }
        return filePathWithFileName;
    }

    public String getLatestFileNameForFo(int occurrence) {
        return getLatestFileNameForFo(LocalDate.now(), occurrence);
    }
    private String getLatestFileNameForFo(LocalDate localDate, int occurrence) {
        LocalDate date = localDate;
        File file;
        String filePathWithFileName = null;
        for(int i=0; i<occurrence; i++) {
            do {
                String fileName = AppConstants.FO_FILE_PREFIX + foFormatter.format(date).toUpperCase() + ".csv";
                LOGGER.info("getLatestFileNameForFo | fileName: {}", fileName);
                filePathWithFileName = foDir + File.separator + fileName;
                LOGGER.info("getLatestFileNameForFo | filePathWithFileName: {}", filePathWithFileName);
                file = new File(filePathWithFileName);
                date = date.minusDays(1);
            } while(!file.exists());
        }
        return filePathWithFileName;
    }

    public String getLatestFileNameForMat(int occurrence) {
        return getLatestFileNameForMat(LocalDate.now(), occurrence);
    }
    private String getLatestFileNameForMat(LocalDate localDate, int occurrence) {
        LocalDate date = localDate;
        File file;
        String filePathWithFileName = null;
        for(int i=0; i<occurrence; i++) {
            do {
                String fileName = AppConstants.MTO_FILE_PREFIX + matFormatter.format(date) + AppConstants.MTO_FILE_SUFFIX;
                LOGGER.info("getLatestFileNameForMat | fileName: {}", fileName);
                filePathWithFileName = matDir + File.separator + fileName;
                LOGGER.info("getLatestFileNameForMat | filePathWithFileName: {}", filePathWithFileName);
                file = new File(filePathWithFileName);
                date = date.minusDays(1);
            } while(!file.exists());
        }
        return filePathWithFileName;
    }

    public void createFolder(String outputPathAndFileName) {
        String dataDir = AppConstants.BASE_DATA_DIR + File.separator + AppConstants.DATA_DIR_NAME;
        File folder = new File(dataDir);
        File[] listOfFiles = folder.listFiles();

        if(null == folder.listFiles()) {
            createDataDir(dataDir);
        }
    }

    public void createDataDir(String dataDir) {
        File newFolder = new File(dataDir);
        boolean created =  newFolder.mkdir();
        System.out.println("creating folder: " + dataDir);
        if(created)
            System.out.println("Folder was created!");
        else
            System.out.println("Unable to create folder");
    }

    public boolean isFileExist(String filePathAndName) {
        return new File(filePathAndName).exists();
    }

    public void unzip(String outputDirAndFileName) throws IOException {
        //String fileZip = "src/main/resources/unzipTest/compressed.zip";
        //File destDir = new File("src/main/resources/unzipTest");
        File destDir = new File(
                outputDirAndFileName.substring(0, outputDirAndFileName.lastIndexOf("\\")).replace("bhav","")
        );
        byte[] buffer = new byte[1024];
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(outputDirAndFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ZipEntry zipEntry;
        zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile;
            newFile = newFile(destDir, zipEntry);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(newFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName().replace("bhav",""));
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }

    public String extractDate(String filePathAndName) {

        return null;
    }

    public List<String> constructFileNames(LocalDate fromDate, String fileDateFormat,
                                            String filePrefix, String fileSuffix) {
        List<String> fileNameList = new ArrayList<>();
        String foBhavCopy = "https://www.nseindia.com/content/historical/DERIVATIVES/2019/AUG/fo14AUG2019bhav.csv.zip";
        LocalDate localDate = fromDate;
        //LOGGER.info(localDate);
        //localDate.getMonth().name().substring(0,3);
        //LOGGER.info(localDate.getMonth().name().substring(0,3));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fileDateFormat);
        //formatter.format(localDate)
        //LOGGER.info(formatter.format(localDate));

        LocalDate todayDate = LocalDate.now();
        while(localDate.compareTo(todayDate) < 1) {
            //LOGGER.info(localDate);
            //LOGGER.info(localDate.getDayOfWeek());
            if("SATURDAY".equals(localDate.getDayOfWeek().name()) || "SUNDAY".equals(localDate.getDayOfWeek().name())) {
                //LOGGER.info(localDate.getDayOfWeek());
            } else {
                //LOGGER.info(localDate.getDayOfWeek());
                String newFileName = filePrefix + formatter.format(localDate).toUpperCase() + fileSuffix;
                //LOGGER.info(newFileName);
                fileNameList.add(newFileName);
            }
            localDate = localDate.plusDays(1);
        }
        //fileNamesToBeDownloaded.forEach(fileName -> LOGGER.info(fileName));
        LOGGER.info("Total File Count: " + fileNameList.size());
        return fileNameList;
    }

    public List<String> fetchFileNames(String dirPathAndName, String filePrefix, String fileSuffix) {
        //dirPathAndName = AppConstants.BASE_DATA_DIR + File.separator + AppConstants.CM_DIR_NAME;
        File folder = new File(dirPathAndName);
        File[] listOfFiles = folder.listFiles();
        if(null == folder.listFiles()) {
            createDataDir(dirPathAndName);
            listOfFiles = folder.listFiles();
        }
        List<String> existingFiles = new ArrayList<>();
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                //LOGGER.info("File " + listOfFiles[i].getName());
                existingFiles.add(listOfFile.getName());
            } else if (listOfFile.isDirectory()) {
                LOGGER.info("Directory " + listOfFile.getName());
            }
        }
        return existingFiles;
    }

    public List<String> constructFileDownloadUrlWithYearAndMonth(String baseUrl, List<String> filesToBeDownloaded) {
        //String baseUrl = "https://www.nseindia.com/content/historical/EQUITIES/2019/AUG/cm16AUG2019bhav.csv.zip";
        List<String> filesUrl;
        filesUrl = filesToBeDownloaded.stream().map(fileName -> {
            //LOGGER.info(fileName);
            return baseUrl + "/" + fileName.substring(4, 7) + "/" + fileName;
        }).collect(Collectors.toList());
        //String newUrl = baseUrl + "/" + localDate.getMonth().name().substring(0,3) + "/fo" + formatter.format(localDate).toUpperCase() + "bhav.csv.zip";
        filesUrl.forEach(url -> LOGGER.info(url));
        return filesUrl;
    }
    public List<String> constructFileDownloadUrl(String baseUrl, List<String> filesToBeDownloaded) {
        List<String> filesUrl;
        filesUrl = filesToBeDownloaded.stream().map(fileName -> {
            //LOGGER.info(fileName);
            return baseUrl + "/" + fileName;
        }).collect(Collectors.toList());
        //String newUrl = baseUrl + "/" + localDate.getMonth().name().substring(0,3) + "/fo" + formatter.format(localDate).toUpperCase() + "bhav.csv.zip";
        filesUrl.forEach(url -> LOGGER.info(url));
        return filesUrl;
    }

    public void downloadFile(String fromUrl, String toDir, Supplier<String> fileName, Consumer<String> filePathAndName) {
        //String outputDirAndFileName = toDir + File.separator + fromUrl.substring(62,85);
        String outputDirAndFileName = fileName.get();
        LOGGER.info("URL: " + fromUrl);
        LOGGER.info("OUT: " + outputDirAndFileName);
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(fromUrl).openStream());
             FileOutputStream fileOS = new FileOutputStream(outputDirAndFileName)) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
            //unzip(outputDirAndFileName);
            filePathAndName.accept(outputDirAndFileName);
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

}
