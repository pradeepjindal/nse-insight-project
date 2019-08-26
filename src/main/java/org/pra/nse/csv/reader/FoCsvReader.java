package org.pra.nse.csv.reader;

import org.pra.nse.AppConstants;
import org.pra.nse.bean.FoBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.constraint.*;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
public class FoCsvReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FoCsvReader.class);

    public Map<FoBean, FoBean> read(Map<FoBean, FoBean> foBeanMap, String fileName) {
        ICsvBeanReader beanReader = null;
        Map<FoBean, FoBean> localFoBeanMap = new HashMap<>();
        LOGGER.info("-----CSV Reader");
        try {
            beanReader = new CsvBeanReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);

            // the header elements are used to map the values to the bean (names must match)
            final String[] header = beanReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();

            FoBean foBean;
            //int missing = 0;
            Map.Entry<String, Integer> missingEntry = new AbstractMap.SimpleEntry<>("missing", 0);
            //
            long startWatch = System.currentTimeMillis();
            LOGGER.info("Beginning CSV Read: " + startWatch);
            List<FoBean> foBeanList = new ArrayList<>();
            while( (foBean = beanReader.read(FoBean.class, header, processors)) != null ) {
                //LOGGER.info(String.format("lineNo=%s, rowNo=%s, customer=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), foBean));
                foBeanList.add(foBean);
            }
            foBeanList.stream().forEach(bean->{
                if( null == foBeanMap) {
                    localFoBeanMap.put(bean, bean);
                } else {
                    if(foBeanMap.containsKey(bean)) {
                        FoBean foBean1 = foBeanMap.get(bean);
                        foBeanMap.put(foBean1, bean);
                    } else {
                        //LOGGER.info("bean not found: " + foBean);
                        missingEntry.setValue(missingEntry.getValue()+1);
                    }
                }
            });
            LOGGER.info("Completed CSV Read: " + (System.currentTimeMillis() - startWatch));
            //
            LOGGER.info("missing: " + missingEntry.getValue());
            if(foBeanMap == null) {
                LOGGER.info("Total Beans in Map: " + localFoBeanMap.size());
                LOGGER.info("Total Data Rows : " + (beanReader.getRowNumber()-1));
                LOGGER.info("Total Map Rows : " + (localFoBeanMap.size()));
                LOGGER.info("Does all rows from csv accounted for ? : " + (beanReader.getRowNumber()-1 ==  localFoBeanMap.size() ? "Yes" : "No"));
            } else {
                LOGGER.info("Total Beans in Map: " + foBeanMap.size());
                LOGGER.info("Total Data Rows : " + (beanReader.getRowNumber()-1));
                LOGGER.info("Total Map Rows : " + (foBeanMap.size()));
                LOGGER.info("Does all rows from csv accounted for ? : " + (beanReader.getRowNumber()-1 ==  foBeanMap.size() ? "Yes" : "No"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if( beanReader != null ) {
                try {
                    beanReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return foBeanMap == null ? localFoBeanMap : foBeanMap;
    }

    /**
     * Sets up the processors used for the examples. There are 10 CSV columns, so 10 processors are defined. Empty
     * columns are read as null (hence the NotNull() for mandatory columns).
     *
     * @return the cell processors
     */
    private static CellProcessor[] getProcessors() {
        return new CellProcessor[] {
                new NotNull(), // instrument
                new NotNull(), // symbol
                new ParseDate(AppConstants.PRA_DATA_DATE_FORMAT), // ExpiryDate
                new DMinMax(0L, DMinMax.MAX_DOUBLE), // strike
                new NotNull(), // option type
                new DMinMax(0L, DMinMax.MAX_DOUBLE), // open
                new DMinMax(0L, DMinMax.MAX_DOUBLE), // high
                new DMinMax(0L, DMinMax.MAX_DOUBLE), // low
                new DMinMax(0L, DMinMax.MAX_DOUBLE), // close
                new DMinMax(0L, DMinMax.MAX_DOUBLE), // settle
                new LMinMax(LMinMax.MIN_LONG, LMinMax.MAX_LONG), // contracts
                new DMinMax(0L, DMinMax.MAX_DOUBLE), // val_inlakh
                new LMinMax(LMinMax.MIN_LONG, LMinMax.MAX_LONG), // oi
                new LMinMax(LMinMax.MIN_LONG, LMinMax.MAX_LONG), // change in oi
                new ParseDate(AppConstants.PRA_DATA_DATE_FORMAT), // timestamp
                null
        };
    }
}
