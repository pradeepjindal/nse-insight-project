package org.pra.nse.csv.writer;

import org.pra.nse.ApCo;
import org.pra.nse.bean.PraBean;
import org.pra.nse.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.FmtDate;
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
import java.util.TreeSet;

@Component
public class ManishCsvWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManishCsvWriter.class);

    private final FileUtils fileUtils;

    public ManishCsvWriter(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    public void write(List<PraBean> praBeans, String outputFilePathAndName, TreeSet<LocalDate> foExpiryDates) throws IOException {
        fileUtils.createFolder(outputFilePathAndName);
        //final ICsvBeanWriter beanWriter;
        try (ICsvBeanWriter beanWriter = new CsvBeanWriter(new FileWriter(outputFilePathAndName), CsvPreference.STANDARD_PREFERENCE)) {
            // the header elements are used to map the bean values to each column (names must match)
            final String[] header = new String[] {
                    "symbol", "expiryDate"
                    , "cmTdyClose", "cmTdyTraded"
                    , "tdyDelivery","prcntChgInDelivery"
                    , "tdyOI"
            };
            final CellProcessor[] processors = getProcessors();
            // write the header
            beanWriter.writeHeader(header);
            praBeans.forEach( praBean -> {
                // pra TODO
                if("FUTSTK".equals(praBean.getInstrument()) && praBean.getExpiryLocalDate().equals(foExpiryDates.first())) {
                    try {
                        beanWriter.write(praBean, header, processors);
                    } catch (IOException e) {
                        LOGGER.warn("some error: {}", e.getMessage());
                    }
                }
            });
        }
    }

    private static CellProcessor[] getProcessors() {
        return new CellProcessor[] {
                new NotNull(), // symbol
                new FmtDate(ApCo.PRA_DATA_DATE_FORMAT), // expiryDate
                new NotNull(), // cmTodayClose
                new LMinMax(0L, LMinMax.MAX_LONG), // volume
                new LMinMax(0L, LMinMax.MAX_LONG), // todayDelivery
                new NotNull(), // prcntChgInDelivery
                new LMinMax(LMinMax.MIN_LONG, LMinMax.MAX_LONG) // todayOpenInterest
        };
    }
}
