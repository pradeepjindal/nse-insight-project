package org.pra.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private DateTimeFormatter cmFormatter = DateTimeFormatter.ofPattern("ddMMMyyyy");
    private String cmDir = System.getProperty("user.home") + File.separator + "pra-nse-cm";

    private DateTimeFormatter foFormatter = DateTimeFormatter.ofPattern("ddMMMyyyy");
    private String foDir = System.getProperty("user.home") + File.separator + "pra-nse-fno";

    private DateTimeFormatter matFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
    private String matDir = System.getProperty("user.home") + File.separator + "pra-nse-mat";


    public String getLatestFileNameForCm(int occurrence) {
        return getLatestFileNameForCm(LocalDate.now(), occurrence);
    }
    public String getLatestFileNameForCm(LocalDate localDate, int occurrence) {
        LocalDate date = localDate;
        File file = null;
        String filePathWithFileName = null;

        for(int i=0; i<occurrence; i++) {
            do {
                String fileName = "cm" + cmFormatter.format(date).toUpperCase() + "bhav.csv";
                //System.out.println("Latest File Name: " + fileName);
                filePathWithFileName = cmDir + File.separator + fileName;
                file = new File(filePathWithFileName);
                date = date.minusDays(1);
            } while(!file.exists());
        }
        return filePathWithFileName;
    }

    public String getLatestFileNameForFo(int occurrence) {
        return getLatestFileNameForFo(LocalDate.now(), occurrence);
    }
    public String getLatestFileNameForFo(LocalDate localDate, int occurrence) {
        LocalDate date = localDate;
        File file = null;
        String filePathWithFileName = null;

        for(int i=0; i<occurrence; i++) {
            do {
                String fileName = "fo" + foFormatter.format(date).toUpperCase() + "bhav.csv";
                //System.out.println("Latest File Name: " + fileName);
                filePathWithFileName = foDir + File.separator + fileName;
                file = new File(filePathWithFileName);
                date = date.minusDays(1);
            } while(!file.exists());
        }
        return filePathWithFileName;
    }

    public String getLatestFileNameForMat(int occurrence) {
        return getLatestFileNameForMat(LocalDate.now(), occurrence);
    }
    public String getLatestFileNameForMat(LocalDate localDate, int occurrence) {
        LocalDate date = localDate;
        File file = null;
        String filePathWithFileName = null;

        for(int i=0; i<occurrence; i++) {
            do {
                String fileName = "MTO_" + matFormatter.format(date) + ".DAT";
                //System.out.println("Latest File Name: " + fileName);
                filePathWithFileName = matDir + File.separator + fileName;
                file = new File(filePathWithFileName);
                date = date.minusDays(1);
            } while(!file.exists());
        }
        return filePathWithFileName;
    }

    public void createFolder(String outputPathAndFileName) {
        String dataDir = System.getProperty("user.home") + File.separator + "pra-nse-computed-data";
        File folder = new File(dataDir);
        File[] listOfFiles = folder.listFiles();

        if(null == folder.listFiles()) {
            createDataDir(dataDir);
            listOfFiles = folder.listFiles();
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
        ZipEntry zipEntry = null;
        zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = null;
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
}
