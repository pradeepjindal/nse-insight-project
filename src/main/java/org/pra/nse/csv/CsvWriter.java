package org.pra.nse.csv;

import org.pra.nse.AppConstants;
import org.pra.nse.bean.PraBean;
import org.pra.nse.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.constraint.DMinMax;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class CsvWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvWriter.class);

    public void write(List<PraBean> praBeans, String outputPathAndFileName) {
        FileUtils fileUtils = new FileUtils();
        fileUtils.createFolder(outputPathAndFileName);

        //final ICsvBeanWriter beanWriter;
        try (ICsvBeanWriter beanWriter = new CsvBeanWriter(new FileWriter(outputPathAndFileName), CsvPreference.STANDARD_PREFERENCE)) {

            // the header elements are used to map the bean values to each column (names must match)
            final String[] header = new String[] { "instrument", "symbol", "expiryDate", "strikePrice", "optionType"
                    , "contracts"
                    , "cmTdyClose", "cmPrcntChgInClose"
                    , "foTdyClose", "foPrcntChgInClose"
                    , "tdyOI", "prcntChgInOI"
                    , "tdyDelivery","prcntChgInDelivery"
                    , "tdyDate"
                    //, "foPrevsClose", "prevsOI", "prevsDelivery", "cmPrevsClose"
                    , "prevsDate"
            };
            final CellProcessor[] processors = getProcessors();
            // write the header
            beanWriter.writeHeader(header);
            // write the beans
//            for( final PraBean praBean : praBeans ) {
//                beanWriter.write(praBean, header, processors);
//            }
            praBeans.stream()
                    .filter(bean -> {
                        if("FUTSTK".equals(bean.getInstrument()) && bean.getExpiryDate().getMonth() == new Date().getMonth()) {
                            return true;
                        } else {
                            return false;
                        }
                    })
                    .map(praBean -> {
                        try {
                            beanWriter.write(praBean, header, processors);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return praBean;
                    })
                    .count();
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
//            if( beanWriter != null ) {
//                try {
//                    beanWriter.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(), // instrument
                new NotNull(), // symbol
                new FmtDate(AppConstants.DATA_DATE_FORMAT), // expiryDate
                //new DMinMax(DMinMax.MIN_DOUBLE, DMinMax.MAX_DOUBLE), // strikePrice
                new NotNull(),
                new NotNull(), // optionType

                new LMinMax(LMinMax.MIN_LONG, LMinMax.MAX_LONG), // Contracts

                new NotNull(), // cmTodayClose
                new NotNull(),

                new DMinMax(DMinMax.MIN_DOUBLE, DMinMax.MAX_DOUBLE), // TodayClose
                //new DMinMax(DMinMax.MIN_DOUBLE, DMinMax.MAX_DOUBLE), // percentChangeInOpenInterest
                new NotNull(),
                new LMinMax(LMinMax.MIN_LONG, LMinMax.MAX_LONG), // TodayOpenInterest
                //new DMinMax(DMinMax.MIN_DOUBLE, DMinMax.MAX_DOUBLE), // percentChangeInClose
                new NotNull(),

                new LMinMax(0L, LMinMax.MAX_LONG), // todayDelivery
                new NotNull(), // prcntChgInDelivery

                new FmtDate(AppConstants.DATA_DATE_FORMAT), // TodayTradeDate

//                new DMinMax(0D, DMinMax.MAX_DOUBLE), // PreviousClose
//                new LMinMax(LMinMax.MIN_LONG, LMinMax.MAX_LONG), // PreviousOpenInterest
//                new LMinMax(0L, LMinMax.MAX_LONG), // previousDelivery
//                new NotNull(),
                new FmtDate(AppConstants.DATA_DATE_FORMAT) // PreviousTradeDate
        };
        return processors;
    }
}
