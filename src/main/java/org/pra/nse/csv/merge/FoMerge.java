package org.pra.nse.csv.merge;

import org.pra.nse.bean.FoBean;
import org.pra.nse.bean.PraBean;
import org.pra.nse.csv.reader.FoCsvReader;
import org.pra.nse.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Service
public class FoMerge {
    private static final Logger LOGGER = LoggerFactory.getLogger(FoMerge.class);

    private final FileUtils fileUtils;
    private final FoCsvReader foCsvReader;

    public FoMerge(FileUtils fileUtils, FoCsvReader foCsvReader) {
        this.fileUtils = fileUtils;
        this.foCsvReader = foCsvReader;
    }

    public void merge(List<PraBean> praBeans) {
        Map<FoBean, FoBean> foBeanMap;
        String foLatestFileName = fileUtils.getLatestFileNameForFo(1);
        LOGGER.info("latestFileName FO: " + foLatestFileName);
        String foPreviousFileName = fileUtils.getLatestFileNameForFo(2);
        LOGGER.info("previousFileName FO: " + foPreviousFileName);

        // FO
        foBeanMap = foCsvReader.read(null, foLatestFileName);
        LOGGER.info("{}", foBeanMap.size());

        foCsvReader.read(foBeanMap, foPreviousFileName);
        LOGGER.info("{}", foBeanMap.size());

        merge(praBeans, foBeanMap);
        //praBeans.forEach(bean -> LOGGER.info(bean));
        LOGGER.info("{}", praBeans.size());
    }

    public void merge(List<PraBean> praBeans, Map<FoBean, FoBean> foBeanMap) {
        foBeanMap.entrySet().forEach( entry -> {
            FoBean todayBean = entry.getKey();
            FoBean previousBean = entry.getValue();
            PraBean praBean = new PraBean();
            //
            praBean.setInstrument(todayBean.getInstrument());
            praBean.setSymbol(todayBean.getSymbol());
            praBean.setExpiryLocalDate(todayBean.getExpiry_Dt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            praBean.setExpiryDate(todayBean.getExpiry_Dt());
            praBean.setStrikePrice(todayBean.getStrike_Pr());
            praBean.setOptionType(todayBean.getOption_Typ());
            //
            praBean.setContracts(todayBean.getContracts());
            //
            praBean.setFoTdyClose(todayBean.getClose());
            praBean.setTdyOI(todayBean.getOpen_Int());
            //
            praBean.setTdyLocalDate(todayBean.getTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            praBean.setTdyDate(todayBean.getTimestamp());
            //
            praBean.setFoPrevsClose(previousBean.getClose());
            praBean.setPrevsOI(previousBean.getOpen_Int());
            praBean.setPrevsLocalDate(previousBean.getTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            praBean.setPrevsDate(previousBean.getTimestamp());
            //
            // calc fields
            try{
                if(todayBean.getClose() != 0 && previousBean.getClose() != 0) {
                    double pct = previousBean.getClose()/100;
                    double diff = todayBean.getClose() - previousBean.getClose();
                    double pctChange = Math.round(diff / pct);
                    praBean.setFoPrcntChgInClose(pctChange);
                }
            } catch (ArithmeticException ae) {
                ae.printStackTrace();
            }
            try{
                if(todayBean.getChg_In_Oi() != 0 && previousBean.getOpen_Int() != 0) {
                    double pct = previousBean.getOpen_Int()/100d;
                    double diff = todayBean.getOpen_Int() - previousBean.getOpen_Int();
                    double pctChange = Math.round(diff / pct);
                    praBean.setPrcntChgInOI(pctChange);
                }
            } catch (ArithmeticException ae) {
                ae.printStackTrace();
            }
            praBeans.add(praBean);
        });
        LOGGER.info("{}", praBeans.size());
    }
}
