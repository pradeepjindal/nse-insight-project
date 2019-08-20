package org.pra.nse.csv.reader;

import org.pra.nse.bean.MatBean;
import org.pra.nse.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.constraint.DMinMax;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class MatCsvReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatCsvReader.class);

    public Map<String, MatBean> read(String fromFile) {
        FileUtils fileUtils = new FileUtils();
        //String fromFile = fileUtils.getLatestFileNameForMat(1);
        String matCsvFileName = fromFile.substring(fromFile.lastIndexOf("_")-3, 42) + ".csv";
        String toFile = System.getProperty("user.home") + File.separator + "pra-nse-mat" + File.separator + matCsvFileName;
        transformToCsv(fromFile, toFile);
        if(fileUtils.isFileExist(toFile)) {
            LOGGER.info("Mat file created with csv format: Successfully [{}]", toFile);
        } else {
            LOGGER.error("Mat file failed to be created in csv format: Failed [{}]", toFile);
        }
        //
        Map<String, MatBean> beanMap = readCsv(toFile);
        LOGGER.info("Total Mat Beans in Map: {}", beanMap.size());
        return beanMap;
    }

    private void transformToCsv(String fromFile, String toFile) {
        Map.Entry<String, Integer> entry = new AbstractMap.SimpleEntry<>("key", 0);
        File csvOutputFile = new File(toFile);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            try (Stream<String> stream = Files.lines(Paths.get(fromFile))) {
                stream.filter(line->{
                    if(entry.getValue() < 3) {
                        entry.setValue(entry.getValue()+1);
                        return false;
                    } else {
                        return true;
                    }
                }).map(row -> {
                    if(entry.getValue() == 3) {
                        entry.setValue(entry.getValue()+1);
                        return "RecType,SrNo,Symbol,SecurityType,TradedQty,DeliverableQty,DeliveryToTradeRatio";
                    } else {
                        return row;
                    }
                }).forEach(tuple -> pw.println(tuple));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Map<String, MatBean> readCsv(String fileName) {
        ICsvBeanReader beanReader = null;
        try {
            beanReader = new CsvBeanReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final CellProcessor[] processors = getProcessors();

        MatBean matBean;
        String[] header;
        Map<String, MatBean> matBeanMap = new HashMap<>();
        try {
            header = beanReader.getHeader(true);
            while( (matBean = beanReader.read(MatBean.class, header, processors)) != null ) {
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
            e.printStackTrace();
        }
        return matBeanMap;
    }

    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {
                new LMinMax(0L, LMinMax.MAX_LONG), // recType
                new LMinMax(0L, LMinMax.MAX_LONG), // srNo
                new NotNull(), // symbol
                new NotNull(), // securityType
                new LMinMax(0L, LMinMax.MAX_LONG), // tradedQty
                new LMinMax(0L, LMinMax.MAX_LONG), // deliverableQty
                new DMinMax(0L, DMinMax.MAX_DOUBLE) // deliveryToTradeRatio
        };
        return processors;
    }

}
