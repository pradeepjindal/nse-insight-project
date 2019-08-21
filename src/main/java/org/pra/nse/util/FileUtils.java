package org.pra.nse.util;

import org.pra.nse.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                String fileName = "cm" + cmFormatter.format(date).toUpperCase() + "bhav.csv";
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
                String fileName = AppConstants.FO_FILE_PREFIX + foFormatter.format(date).toUpperCase() + "bhav.csv";
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
        File destDir = new File(outputDirAndFileName.substring(0, outputDirAndFileName.lastIndexOf("\\")));
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
        File destFile = new File(destinationDir, zipEntry.getName());
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

}
