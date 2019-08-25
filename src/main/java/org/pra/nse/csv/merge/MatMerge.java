package org.pra.nse.csv.merge;

import org.pra.nse.bean.MatBean;
import org.pra.nse.bean.PraBean;
import org.pra.nse.csv.reader.MatCsvReader;
import org.pra.nse.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MatMerge {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatMerge.class);

    private final FileUtils fileUtils;

    public MatMerge(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    public void merge(List<PraBean> praBeans) {
        String fromFile;
        MatCsvReader matCsvReader = new MatCsvReader();
        fromFile = fileUtils.getLatestFileNameForMat(1);
        Map<String, MatBean> matLatestBeanMap = matCsvReader.read(fromFile);
        fromFile = fileUtils.getLatestFileNameForMat(2);
        Map<String, MatBean> matPreviousBeanMap = matCsvReader.read(fromFile);
        praBeans.forEach(praBean -> {
            if(matLatestBeanMap.containsKey(praBean.getSymbol()) && matPreviousBeanMap.containsKey(praBean.getSymbol())) {
                praBean.setTdyDelivery(matLatestBeanMap.get(praBean.getSymbol()).getDeliverableQty());
                praBean.setPrevsDelivery(matPreviousBeanMap.get(praBean.getSymbol()).getDeliverableQty());
                try{
                    if(matLatestBeanMap.get(praBean.getSymbol()).getDeliverableQty() != 0
                            && matPreviousBeanMap.get(praBean.getSymbol()).getDeliverableQty() != 0) {
                        double pct = matPreviousBeanMap.get(praBean.getSymbol()).getDeliverableQty()/100d;
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
    }
}
