package org.pra.nse2;

import org.pra.bean.FoBean;
import org.pra.bean.MatBean;
import org.pra.bean.PraBean;
import org.pra.csv.*;
import org.pra.csv.merge.FoMerge;
import org.pra.csv.reader.FoCsvReader;
import org.pra.csv.reader.MatCsvReader;
import org.pra.file.FileUtils;
import org.pra.nse.CmDownloader;
import org.pra.nse.FoDownloader;
import org.pra.nse.MatDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;


public class Processor2 {
    private static Logger LOGGER = LoggerFactory.getLogger(Processor2.class);

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
        String fromFile = null;
        MatCsvReader matCsvReader = new MatCsvReader();
        fromFile = fileUtils.getLatestFileNameForMat(1);
        Map<String, MatBean> matLatestBeanMap = matCsvReader.read(fromFile);
        fromFile = fileUtils.getLatestFileNameForMat(2);
        Map<String, MatBean> matPreviousBeanMap = matCsvReader.read(fromFile);
        praBeans.forEach(praBean -> {
            if(matLatestBeanMap.containsKey(praBean.getSymbol()) && matPreviousBeanMap.containsKey(praBean.getSymbol())) {
                praBean.setTodayDelivery(matLatestBeanMap.get(praBean.getSymbol()).getDeliverableQty());
                praBean.setPreviousDelivery(matPreviousBeanMap.get(praBean.getSymbol()).getDeliverableQty());
                try{
                    if(matLatestBeanMap.get(praBean.getSymbol()).getDeliverableQty() != 0
                            && matPreviousBeanMap.get(praBean.getSymbol()).getDeliverableQty() != 0) {
                        double pct = matPreviousBeanMap.get(praBean.getSymbol()).getDeliverableQty()/100;
                        double diff = matLatestBeanMap.get(praBean.getSymbol()).getDeliverableQty() - matPreviousBeanMap.get(praBean.getSymbol()).getDeliverableQty();
                        double pctChange = Math.round(diff / pct);
                        praBean.setPrcntChgInDelivery(pctChange);
                    }
                } catch (ArithmeticException ae) {
                    ae.printStackTrace();
                }
            } else {
                if(!praBean.getSymbol().contains("NIFTY")) {
                    LOGGER.warn("symbol not found in delivery map: {}", praBean.getSymbol());
                }
            }
        });
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