package org.pra.nse.csv.writer;

import org.pra.nse.AppConstants;
import org.pra.nse.bean.PraBean;
import org.pra.nse.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
import java.time.LocalDate;
import java.util.List;

@Service
public class ManishCsvWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManishCsvWriter.class);

    private final FileUtils fileUtils;

    public ManishCsvWriter(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    public void write(List<PraBean> praBeans, String outputFilePathAndName) {
        fileUtils.createFolder(outputFilePathAndName);
        //final ICsvBeanWriter beanWriter;
        try (ICsvBeanWriter beanWriter = new CsvBeanWriter(new FileWriter(outputFilePathAndName), CsvPreference.STANDARD_PREFERENCE)) {
            // the header elements are used to map the bean values to each column (names must match)
            final String[] header = new String[] { "instrument", "symbol", "expiryDate", "strikePrice", "optionType"
                    , "contracts"
                    , "cmTdyClose", "cmPrcntChgInClose"
                    , "foTdyClose", "foPrcntChgInClose"
                    , "tdyOI", "prcntChgInOI"
                    , "tdyDelivery","prcntChgInDelivery"
                    , "tdyDate"
                    , "cmPrevsClose", "foPrevsClose", "prevsOI", "prevsDelivery"
                    , "prevsDate"
            };
            final CellProcessor[] processors = getProcessors();
            // write the header
            beanWriter.writeHeader(header);
            // write the beans
//            for( final PraBean praBean : praBeans ) {
//                beanWriter.write(praBean, header, processors);
//            }
            praBeans.forEach( praBean -> {
                if("FUTSTK".equals(praBean.getInstrument()) && praBean.getExpiryLocalDate().getMonth() == LocalDate.now().getMonth()) {
                    try {
                        beanWriter.write(praBean, header, processors);
                    } catch (IOException e) {
                        LOGGER.warn("some error: {}", e);
                    }
                }
            });
        }catch (IOException e) {
            LOGGER.warn("some error: {}", e);
        }
    }

    private static CellProcessor[] getProcessors() {
        return new CellProcessor[] {
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

                new NotNull(),
                new DMinMax(0D, DMinMax.MAX_DOUBLE), // PreviousClose
                new LMinMax(LMinMax.MIN_LONG, LMinMax.MAX_LONG), // PreviousOpenInterest
                new LMinMax(0L, LMinMax.MAX_LONG), // previousDelivery
                new FmtDate(AppConstants.DATA_DATE_FORMAT) // PreviousTradeDate
        };
    }
}
