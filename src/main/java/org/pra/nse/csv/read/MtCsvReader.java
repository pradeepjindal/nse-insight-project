package org.pra.nse.csv.read;

import org.pra.nse.ApCo;
import org.pra.nse.bean.MtBean;
import org.pra.nse.util.FileNameUtils;
import org.pra.nse.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.constraint.DMinMax;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.util.*;

@Component
public class MtCsvReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(MtCsvReader.class);

    private final FileUtils fileUtils;
    private final FileNameUtils fileNameUtils;

    MtCsvReader(FileUtils fileUtils, FileNameUtils fileNameUtils) {
        this.fileUtils = fileUtils;
        this.fileNameUtils = fileNameUtils;
    }

    public Map<String, MtBean> read(String fromFile) {
        //String fromFile = fileUtils.getLatestFileNameForMat(1);
        int firstIndex = fromFile.lastIndexOf(ApCo.MT_DATA_FILE_PREFIX);
        String mtCsvFileName = fromFile.substring(firstIndex, firstIndex+13) + ".csv";
        String toFile = ApCo.BASE_DATA_DIR + File.separator + ApCo.MT_DIR_NAME + File.separator + mtCsvFileName;
        if(fileUtils.isFileExist(toFile)) {
            LOGGER.info("Mat file created with csv format: Successfully [{}]", toFile);
        } else {
            LOGGER.error("Mat file failed to be created in csv format: Failed [{}]", toFile);
        }
        //
        Map<String, MtBean> beanMap = readCsv(toFile);
        LOGGER.info("Total Mat Beans in Map: {}", beanMap.size());
        return beanMap;
    }

    private Map<String, MtBean> readCsv(String fileName) {
        ICsvBeanReader beanReader = null;
        try {
            beanReader = new CsvBeanReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final CellProcessor[] processors = getProcessors();

        MtBean matBean;
        String[] header;
        Map<String, MtBean> matBeanMap = new HashMap<>();
        try {
            header = beanReader.getHeader(true);
            while( (matBean = beanReader.read(MtBean.class, header, processors)) != null ) {
                //LOGGER.info(String.format("lineNo=%s, rowNo=%s, customer=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), matBean));
                if("EQ".equals(matBean.getSecurityType())) {
                    if(matBeanMap.containsKey(matBean.getSymbol())) {
                        LOGGER.warn("Symbol already present in map: old value = [{}], new value = [{}]",
                                matBeanMap.get(matBean.getSymbol()), matBean);
                    }
                    matBeanMap.put(matBean.getSymbol(), matBean);
                }
            }
        } catch (IOException e) {
            LOGGER.warn("some error: e", e);
        }
        return matBeanMap;
    }

    private static CellProcessor[] getProcessors() {
        return new CellProcessor[] {
                new LMinMax(0L, LMinMax.MAX_LONG), // recType
                new LMinMax(0L, LMinMax.MAX_LONG), // srNo
                new NotNull(), // symbol
                new NotNull(), // securityType
                new LMinMax(0L, LMinMax.MAX_LONG), // tradedQty
                new LMinMax(0L, LMinMax.MAX_LONG), // deliverableQty
                new DMinMax(0L, DMinMax.MAX_DOUBLE) // deliveryToTradeRatio
        };
    }

}