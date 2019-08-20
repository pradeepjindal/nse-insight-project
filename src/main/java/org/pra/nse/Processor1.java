package org.pra.nse;

import org.pra.nse.bean.FoBean;
import org.pra.nse.bean.PraBean;
import org.pra.nse.csv.merge.FoMerge;
import org.pra.nse.csv.reader.FoCsvReader;
import org.pra.nse.csv.CsvWriter;
import org.pra.nse.file.FileUtils;
import org.pra.nse.csv.download.CmDownloader;
import org.pra.nse.csv.download.FoDownloader;
import org.pra.nse.csv.download.MatDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Processor1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor1.class);

    public void process() {
        FoDownloader downloadNseFnoEod = new FoDownloader();
        downloadNseFnoEod.download();

        MatDownloader downloadNseDeliveryMat = new MatDownloader();
        downloadNseDeliveryMat.download();

        CmDownloader downloadNseCmEod = new CmDownloader();
        downloadNseCmEod.download();

        //---------------------------------------------------------
        Map<FoBean, FoBean> foBeanMap;
        FileUtils fileUtils = new FileUtils();
        String foLatestFileName = fileUtils.getLatestFileNameForFo(1);
        LOGGER.info("latestFileName FO: " + foLatestFileName);
        String foPreviousFileName = fileUtils.getLatestFileNameForFo(2);
        LOGGER.info("previousFileName FO: " + foPreviousFileName);

        //
        String matLatestFileName = fileUtils.getLatestFileNameForMat(1);
        LOGGER.info("latestFileName MAT: " + matLatestFileName);
        String matPreviousFileName = fileUtils.getLatestFileNameForMat(2);
        LOGGER.info("previousFileName MAT: " + matPreviousFileName);
        //

        FoCsvReader csvReader = new FoCsvReader();
        foBeanMap = csvReader.read(null, foLatestFileName);
        LOGGER.info("{}",foBeanMap.size());

        csvReader.read(foBeanMap, foPreviousFileName);
        LOGGER.info("{}",foBeanMap.size());

        FoMerge foMerge = new FoMerge();
        List<PraBean> praBeans = foMerge.merge(foBeanMap);
        //praBeans.forEach(bean -> LOGGER.info(bean));
        LOGGER.info("{}",praBeans.size());

        String outputPathAndFileName = System.getProperty("user.home") + File.separator + "pra-nse-computed-data" + File.separator + "praData.csv";
        CsvWriter csvWriter = new CsvWriter();
        csvWriter.write(praBeans, outputPathAndFileName);

        //
        String praDataDirAndFile = System.getProperty("user.home") + File.separator + "pra-nse-computed-data" + File.separator + "praData.csv";
        File praDataFile = new File(praDataDirAndFile);
        LOGGER.info("");
        if(praDataFile.exists()) {
            LOGGER.info("---------------------------------------------------------------------------------------------------------------");
            LOGGER.info("SUCCESS! praData.csv File has been placed at: " + praDataDirAndFile);
            LOGGER.info("---------------------------------------------------------------------------------------------------------------");
        } else {
            LOGGER.info("ERROR! something got wrong, could not create data file.");
        }
    }
}
