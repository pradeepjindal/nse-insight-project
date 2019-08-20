package org.pra.nse2;

import org.pra.bean.FoBean;
import org.pra.bean.PraBean;
import org.pra.csv.*;
import org.pra.csv.merge.CmMerge;
import org.pra.csv.merge.FoMerge;
import org.pra.csv.merge.MatMerge;
import org.pra.csv.reader.FoCsvReader;
import org.pra.file.FileUtils;
import org.pra.nse.CmDownloader;
import org.pra.nse.FoDownloader;
import org.pra.nse.MatDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;


public class Processor3 {
    private static Logger LOGGER = LoggerFactory.getLogger(Processor3.class);

    public void process() {
        FoDownloader downloadNseFnoEod = new FoDownloader();
        downloadNseFnoEod.download();

        MatDownloader downloadNseDeliveryMat = new MatDownloader();
        downloadNseDeliveryMat.download();

        CmDownloader downloadNseCmEod = new CmDownloader();
        downloadNseCmEod.download();

        //---------------------------------------------------------
        Map<FoBean, FoBean> foBeanMap = null;
        FileUtils fileUtils = new FileUtils();
        String foLatestFileName = fileUtils.getLatestFileNameForFo(1);
        LOGGER.info("latestFileName FO: " + foLatestFileName);
        String foPreviousFileName = fileUtils.getLatestFileNameForFo(2);
        LOGGER.info("previousFileName FO: " + foPreviousFileName);

        // FO
        FoCsvReader csvReader = new FoCsvReader();

        foBeanMap = csvReader.read(null, foLatestFileName);
        LOGGER.info("{}",foBeanMap.size());

        csvReader.read(foBeanMap, foPreviousFileName);
        LOGGER.info("{}",foBeanMap.size());

        FoMerge foMerge = new FoMerge();
        List<PraBean> praBeans = foMerge.merge(foBeanMap);
        //praBeans.forEach(bean -> LOGGER.info(bean));
        LOGGER.info("{}",praBeans.size());

        // MAT
        MatMerge matMerge = new MatMerge();
        matMerge.merge(praBeans);
        // CM
        CmMerge cmMerge = new CmMerge();
        cmMerge.merge(praBeans);
        //
        String outputPathAndFileName = System.getProperty("user.home") + File.separator + "pra-nse-computed-data" + File.separator + "praData.csv";
        CsvWriter csvWriter = new CsvWriter();
        csvWriter.write(praBeans, outputPathAndFileName);

        //
        if(fileUtils.isFileExist(outputPathAndFileName)) {
            LOGGER.info("---------------------------------------------------------------------------------------------------------------");
            LOGGER.info("SUCCESS! praData.csv File has been placed at: " + outputPathAndFileName);
            LOGGER.info("---------------------------------------------------------------------------------------------------------------");
        } else {
            LOGGER.info("ERROR! something got wrong, could not create data file.");
        }
    }
}
