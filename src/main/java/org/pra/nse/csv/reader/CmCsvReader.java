package org.pra.nse.csv.reader;

import org.pra.nse.bean.CmBean;
import org.pra.nse.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.constraint.DMinMax;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CmCsvReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmCsvReader.class);

    public Map<String, CmBean> read(String fromFile) {
        FileUtils fileUtils = new FileUtils();
        //String fromFile = fileUtils.getLatestFileNameForMat(1);
        int firstIndex = fromFile.lastIndexOf("cm");
        String cmCsvFileName = fromFile.substring(firstIndex, firstIndex+11) + "bhav.csv";
        String toFile = System.getProperty("user.home") + File.separator + "pra-nse-cm" + File.separator + cmCsvFileName;

        if(fileUtils.isFileExist(toFile)) {
            LOGGER.info("Mat file created with csv format: Successfully [{}]", toFile);
        } else {
            LOGGER.error("Mat file failed to be created in csv format: Failed [{}]", toFile);
        }
        //
        Map<String, CmBean> beanMap = readCsv(toFile);
        LOGGER.info("Total CM Beans in Map: {}", beanMap.size());
        return beanMap;
    }

    private Map<String, CmBean> readCsv(String fileName) {
        ICsvBeanReader beanReader = null;
        try {
            beanReader = new CsvBeanReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final CellProcessor[] processors = getProcessors();

        CmBean cmBean;
        String[] header;
        Map<String, CmBean> cmBeanMap = new HashMap<>();
        try {
            header = beanReader.getHeader(true);
            while( (cmBean = beanReader.read(CmBean.class, header, processors)) != null ) {
                //LOGGER.info(String.format("lineNo=%s, rowNo=%s, customer=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), matBean));
                if("EQ".equals(cmBean.getSeries())) {
                    if(cmBeanMap.containsKey(cmBean.getSymbol())) {
                        LOGGER.warn("Symbol already present in map: old value = [{}], new value = [{}]",
                                cmBeanMap.get(cmBean.getSymbol()), cmBean);
                    }
                    cmBeanMap.put(cmBean.getSymbol(), cmBean);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cmBeanMap;
    }

    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(), //symbol
                new NotNull(), //series
                new DMinMax(0L, DMinMax.MAX_DOUBLE), // open
                new DMinMax(0L, DMinMax.MAX_DOUBLE), // high
                new DMinMax(0L, DMinMax.MAX_DOUBLE), // low
                new DMinMax(0L, DMinMax.MAX_DOUBLE), // close
                new DMinMax(0L, DMinMax.MAX_DOUBLE), // last
                new DMinMax(0L, DMinMax.MAX_DOUBLE), // prevClose

                new LMinMax(0L, LMinMax.MAX_LONG), // tot trd qty
                new DMinMax(0L, DMinMax.MAX_DOUBLE), // tot trd val
                new ParseDate("dd-MMM-yyyy"), // timestamp
                new LMinMax(0L, LMinMax.MAX_LONG), // totalTrades
                new NotNull(), // isin
                null
        };
        return processors;
    }

}
