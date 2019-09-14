package org.pra.nse.csv.merge;

import org.pra.nse.ApCo;
import org.pra.nse.bean.in.FoBean;
import org.pra.nse.bean.out.PraBean;
import org.pra.nse.csv.read.FoCsvReader;
import org.pra.nse.util.DateUtils;
import org.pra.nse.util.PraNameUtils;
import org.pra.nse.util.NseFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class FoMerger {
    private static final Logger LOGGER = LoggerFactory.getLogger(FoMerger.class);

    private final NseFileUtils nseFileUtils;
    private final PraNameUtils praNameUtils;
    private final FoCsvReader csvReader;

    public FoMerger(NseFileUtils nseFileUtils, PraNameUtils praNameUtils, FoCsvReader csvReader) {
        this.nseFileUtils = nseFileUtils;
        this.praNameUtils = praNameUtils;
        this.csvReader = csvReader;
    }

    public TreeSet<LocalDate> merge(List<PraBean> praBeans) throws FileNotFoundException {
        LOGGER.info("FO-Merge");
        Map<FoBean, FoBean> foBeanMap;
        //String foLatestFileName = fileUtils.getLatestFileNameForFo(1);
        String foLatestFileName = praNameUtils.getLatestFileNameFor(ApCo.FO_FILES_PATH, ApCo.FO_DATA_FILE_PREFIX, ApCo.PRA_DATA_FILE_EXT,1);
        LOGGER.info("latestFileName FO: " + foLatestFileName);
        //String foPreviousFileName = fileUtils.getLatestFileNameForFo(2);
        String foPreviousFileName = praNameUtils.getLatestFileNameFor(ApCo.FO_FILES_PATH, ApCo.FO_DATA_FILE_PREFIX, ApCo.PRA_DATA_FILE_EXT,2);
        LOGGER.info("previousFileName FO: " + foPreviousFileName);

        // FO
        foBeanMap = csvReader.read(null, foLatestFileName);
        //LOGGER.info("{}", foBeanMap.size());

        csvReader.read(foBeanMap, foPreviousFileName);
        //LOGGER.info("{}", foBeanMap.size());

        return merge(praBeans, foBeanMap);
        //praBeans.forEach(bean -> LOGGER.info(bean));
        //LOGGER.info("{}", praBeans.size());
    }

    public TreeSet<LocalDate> merge(List<PraBean> praBeans, Map<FoBean, FoBean> foBeanMap) {
        TreeSet<LocalDate> foMonthlyExpiryDates = new TreeSet<>();
        foBeanMap.entrySet().forEach( entry -> {

            FoBean todayBean = entry.getKey();
            FoBean previousBean = entry.getValue();
            PraBean praBean = new PraBean();
            //
            if("FUTSTK".equals(todayBean.getInstrument())) {
                foMonthlyExpiryDates.add(DateUtils.toLocalDate(todayBean.getExpiry_Dt()));
            }

            praBean.setInstrument(todayBean.getInstrument());
            praBean.setSymbol(todayBean.getSymbol());
            praBean.setExpiryLocalDate(DateUtils.toLocalDate(todayBean.getExpiry_Dt()));
            praBean.setExpiryDate(todayBean.getExpiry_Dt());
            praBean.setStrikePrice(todayBean.getStrike_Pr());
            praBean.setOptionType(todayBean.getOption_Typ());
            //
            praBean.setContracts(todayBean.getContracts());
            //
            praBean.setFoTdyClose(todayBean.getClose());
            praBean.setTdyOI(todayBean.getOpen_Int());
            //
            praBean.setTdyLocalDate(DateUtils.toLocalDate(todayBean.getTimestamp()));
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
                    double pct = previousBean.getClose()/100d;
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
        LOGGER.info("praBeans: {}", praBeans.size());
        return foMonthlyExpiryDates;
    }

}
