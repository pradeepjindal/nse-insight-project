package org.pra.csv.merge;

import org.pra.bean.CmBean;
import org.pra.bean.PraBean;
import org.pra.csv.reader.CmCsvReader;
import org.pra.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class CmMerge {
    private static Logger LOGGER = LoggerFactory.getLogger(CmMerge.class);

    private FileUtils fileUtils = new FileUtils();

    public List<PraBean> merge(List<PraBean> praBeans) {
        String fromFile = null;
        CmCsvReader matCsvReader = new CmCsvReader();
        fromFile = fileUtils.getLatestFileNameForCm(1);
        Map<String, CmBean> latestBeanMap = matCsvReader.read(fromFile);
        fromFile = fileUtils.getLatestFileNameForCm(2);
        Map<String, CmBean> previousBeanMap = matCsvReader.read(fromFile);
        praBeans.forEach(praBean -> {
            String symbol = praBean.getSymbol();
            if(latestBeanMap.containsKey(symbol) && previousBeanMap.containsKey(symbol)) {
                praBean.setCmTodayClose(latestBeanMap.get(symbol).getClose());
                praBean.setCmPreviousClose(previousBeanMap.get(symbol).getClose());
                try{
                    if(latestBeanMap.get(symbol).getClose() != 0 && previousBeanMap.get(symbol).getClose() != 0) {
                        double pct = previousBeanMap.get(symbol).getClose()/100;
                        double diff = latestBeanMap.get(symbol).getClose() - previousBeanMap.get(symbol).getClose();
                        double pctChange = Math.round(diff / pct);
                        praBean.setCmPrcntChgInClose(pctChange);
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
        return praBeans;
    }
}
