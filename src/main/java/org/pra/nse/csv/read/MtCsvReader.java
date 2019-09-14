package org.pra.nse.csv.read;

import org.pra.nse.ApCo;
import org.pra.nse.bean.in.MtBean;
import org.pra.nse.util.PraNameUtils;
import org.pra.nse.util.NseFileUtils;
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

    private final NseFileUtils nseFileUtils;
    private final PraNameUtils praNameUtils;

    MtCsvReader(NseFileUtils nseFileUtils, PraNameUtils praNameUtils) {
        this.nseFileUtils = nseFileUtils;
        this.praNameUtils = praNameUtils;
    }

    public Map<String, MtBean> read(String fromFile) {
        //String fromFile = fileUtils.getLatestFileNameForMat(1);
//        int firstIndex = fromFile.lastIndexOf(ApCo.MT_DATA_FILE_PREFIX);
//        String mtCsvFileName = fromFile.substring(firstIndex, firstIndex+13) + ".csv";
//        String toFile = ApCo.BASE_DATA_DIR + File.separator + ApCo.MT_DIR_NAME + File.separator + mtCsvFileName;
        String toFile = PathHelper.fileNameWithFullPath(ApCo.MT_DIR_NAME, ApCo.MT_DATA_FILE_PREFIX, fromFile);
        if(nseFileUtils.isFileExist(toFile)) {
            LOGGER.info("Mat file exist [{}]", toFile);
        } else {
            LOGGER.error("Mat file does not exist [{}]", toFile);
        }
        //
        Map<String, MtBean> beanMap = readCsv(toFile);
        LOGGER.info("Total MT Beans in Map: {}", beanMap.size());
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
        Map<String, MtBean> mtBeanMap = new HashMap<>();
        try {
            header = beanReader.getHeader(true);
            while( (matBean = beanReader.read(MtBean.class, header, processors)) != null ) {
                //LOGGER.info(String.format("lineNo=%s, rowNo=%s, customer=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), matBean));
                if("EQ".equals(matBean.getSecurityType())) {
                    if(mtBeanMap.containsKey(matBean.getSymbol())) {
                        LOGGER.warn("Symbol already present in map: old value = [{}], new value = [{}]",
                                mtBeanMap.get(matBean.getSymbol()), matBean);
                    }
                    mtBeanMap.put(matBean.getSymbol(), matBean);
                }
            }
        } catch (IOException e) {
            LOGGER.warn("some error: {}", e);
        }
        return mtBeanMap;
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
