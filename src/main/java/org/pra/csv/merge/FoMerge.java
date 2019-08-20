package org.pra.csv.merge;

import org.pra.bean.FoBean;
import org.pra.bean.PraBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FoMerge {
    private static Logger LOGGER = LoggerFactory.getLogger(FoMerge.class);

    public List<PraBean> merge(Map<FoBean, FoBean> foBeanMap) {
        List<PraBean> praBeans = new ArrayList<>();
        praBeans = foBeanMap.entrySet().stream().map( entry -> {
            FoBean todayBean = entry.getKey();
            FoBean previousBean = entry.getValue();
            PraBean praBean = new PraBean();
            //
            praBean.setInstrument(todayBean.getInstrument());
            praBean.setSymbol(todayBean.getSymbol());
            //praBean.setExpiryDate(todayBean.getExpiry_Dt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            praBean.setExpiryDate(todayBean.getExpiry_Dt());
            praBean.setStrikePrice(todayBean.getStrike_Pr());
            praBean.setOptionType(todayBean.getOption_Typ());
            //
            praBean.setTodayClose(todayBean.getClose());
            praBean.setTodayOI(todayBean.getOpen_Int());
            //
            //praBean.setTodayTradeDate(todayBean.getTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            praBean.setTodayDate(todayBean.getTimestamp());

            //
            praBean.setPreviousClose(previousBean.getClose());
            praBean.setPreviousOI(previousBean.getOpen_Int());
            //praBean.setPreviousTradeDate(previousBean.getTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            praBean.setPreviousDate(previousBean.getTimestamp());
            //
            // calc fields
            try{
                if(todayBean.getClose() != 0 && previousBean.getClose() != 0) {
                    double pct = previousBean.getClose()/100;
                    double diff = todayBean.getClose() - previousBean.getClose();
                    double pctChange = Math.round(diff / pct);
                    praBean.setPrcntChgInClose(pctChange);
                }
            } catch (ArithmeticException ae) {
                ae.printStackTrace();
            }
            try{
                if(todayBean.getChg_In_Oi() != 0 && previousBean.getOpen_Int() != 0) {
                    double pct = previousBean.getOpen_Int()/100;
                    double diff = todayBean.getOpen_Int() - previousBean.getOpen_Int();
                    double pctChange = Math.round(diff / pct);
                    praBean.setPrcntChgInOI(pctChange);
                }
            } catch (ArithmeticException ae) {
                ae.printStackTrace();
            }
            return praBean;
        }).collect(Collectors.toList());
        return praBeans;
    }
}
