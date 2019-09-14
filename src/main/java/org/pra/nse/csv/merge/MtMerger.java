package org.pra.nse.csv.merge;

import org.pra.nse.ApCo;
import org.pra.nse.bean.in.MtBean;
import org.pra.nse.bean.out.PraBean;
import org.pra.nse.csv.read.MtCsvReader;
import org.pra.nse.util.PraNameUtils;
import org.pra.nse.util.NseFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MtMerger {
    private static final Logger LOGGER = LoggerFactory.getLogger(MtMerger.class);

    private final NseFileUtils nseFileUtils;
    private final PraNameUtils praNameUtils;
    private final MtCsvReader csvReader;

    public MtMerger(NseFileUtils nseFileUtils, PraNameUtils praNameUtils, MtCsvReader csvReader) {
        this.nseFileUtils = nseFileUtils;
        this.praNameUtils = praNameUtils;
        this.csvReader = csvReader;
    }

    public void merge(List<PraBean> praBeans) {
        LOGGER.info("MT-Merge");
        String fromFile;
        //fromFile = fileUtils.getLatestFileNameForMat(1);
        fromFile = praNameUtils.getLatestFileNameFor(ApCo.MT_FILES_PATH, ApCo.MT_DATA_FILE_PREFIX, ApCo.PRA_DATA_FILE_EXT,1);
        Map<String, MtBean> mtLatestBeanMap = csvReader.read(fromFile);
        //fromFile = fileUtils.getLatestFileNameForMat(2);
        fromFile = praNameUtils.getLatestFileNameFor(ApCo.MT_FILES_PATH, ApCo.MT_DATA_FILE_PREFIX, ApCo.PRA_DATA_FILE_EXT,2);
        Map<String, MtBean> matPreviousBeanMap = csvReader.read(fromFile);
        praBeans.forEach(praBean -> {
            if(mtLatestBeanMap.containsKey(praBean.getSymbol()) && matPreviousBeanMap.containsKey(praBean.getSymbol())) {
                praBean.setTdyDelivery(mtLatestBeanMap.get(praBean.getSymbol()).getDeliverableQty());
                praBean.setPrevsDelivery(matPreviousBeanMap.get(praBean.getSymbol()).getDeliverableQty());
                try{
                    if(mtLatestBeanMap.get(praBean.getSymbol()).getDeliverableQty() != 0
                            && matPreviousBeanMap.get(praBean.getSymbol()).getDeliverableQty() != 0) {
                        double pct = matPreviousBeanMap.get(praBean.getSymbol()).getDeliverableQty()/100d;
                        double diff = mtLatestBeanMap.get(praBean.getSymbol()).getDeliverableQty() - matPreviousBeanMap.get(praBean.getSymbol()).getDeliverableQty();
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
    }

//    public void merge(List<PraBean> praBeans, Map<String, MtBean> latestBeanMap, Map<String, MtBean> previousBeanMap) {
//        praBeans.forEach(praBean -> {
//            if(latestBeanMap.containsKey(praBean.getSymbol()) && previousBeanMap.containsKey(praBean.getSymbol())) {
//                praBean.setTdyDelivery(latestBeanMap.get(praBean.getSymbol()).getDeliverableQty());
//                praBean.setPrevsDelivery(previousBeanMap.get(praBean.getSymbol()).getDeliverableQty());
//                try{
//                    if(previousBeanMap.get(praBean.getSymbol()).getDeliverableQty() != 0
//                            && previousBeanMap.get(praBean.getSymbol()).getDeliverableQty() != 0) {
//                        double pct = previousBeanMap.get(praBean.getSymbol()).getDeliverableQty()/100d;
//                        double diff = previousBeanMap.get(praBean.getSymbol()).getDeliverableQty() - previousBeanMap.get(praBean.getSymbol()).getDeliverableQty();
//                        double pctChange = Math.round(diff / pct);
//                        praBean.setPrcntChgInDelivery(pctChange);
//                    }
//                } catch (ArithmeticException ae) {
//                    ae.printStackTrace();
//                }
//            } else {
//                if(!praBean.getSymbol().contains("NIFTY")) {
//                    LOGGER.warn("symbol not found in delivery map: {}", praBean.getSymbol());
//                }
//            }
//        });
//    }
}
