package org.pra.nse;

import org.pra.nse.bean.FoBean;
import org.pra.nse.bean.PraBean;
import org.pra.nse.csv.merge.CmMerge;
import org.pra.nse.csv.merge.FoMerge;
import org.pra.nse.csv.merge.MatMerge;
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


public class ManishProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManishProcessor.class);

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
        String outputPathAndFileName = System.getProperty("user.home")
                + File.separator + AppConstants.CALC_DIR_NAME
                + File.separator + AppConstants.MANISH_FILE_NAME;
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
