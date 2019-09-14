package org.pra.nse.csv.read;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.pra.nse.bean.in.FoBean;
import org.pra.nse.util.PraNameUtils;
import org.pra.nse.util.NseFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class FoCsvReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FoCsvReader.class);
    private final NseFileUtils nseFileUtils;
    private final PraNameUtils praNameUtils;

    FoCsvReader(NseFileUtils nseFileUtils, PraNameUtils praNameUtils) {
        this.nseFileUtils = nseFileUtils;
        this.praNameUtils = praNameUtils;
    }

    public Map<FoBean, FoBean> read(Map<FoBean, FoBean> foBeanMap, String fileName) throws FileNotFoundException {
        Map<FoBean, FoBean> localFoBeanMap = new HashMap<>();
        LOGGER.info("FO file exists: [{}]", fileName);

        FoBean foBean;
        //int missing = 0;
        AtomicInteger missingEntry = new AtomicInteger();
        List<FoBean> foBeanList = new ArrayList<>();

        foBeanList = readCsv(new File(fileName));

        foBeanList.stream().forEach(bean->{
            if( null == foBeanMap) {
                localFoBeanMap.put(bean, bean);
            } else {
                if(foBeanMap.containsKey(bean)) {
                    FoBean foBean1 = foBeanMap.get(bean);
                    foBeanMap.put(foBean1, bean);
                } else {
                    //LOGGER.info("bean not found: " + foBean);
                    missingEntry.incrementAndGet();
                }
            }
        });
//            if(foBeanMap == null) {
//                LOGGER.info("Total Beans in Map: " + localFoBeanMap.size());
//                LOGGER.info("Total Data Rows : " + (beanReader.getRowNumber()-1));
//                LOGGER.info("Total Map Rows : " + (localFoBeanMap.size()));
//                LOGGER.info("Does all rows from csv accounted for ? : " + (beanReader.getRowNumber()-1 ==  localFoBeanMap.size() ? "Yes" : "No"));
//            } else {
//                LOGGER.info("Total Beans in Map: " + foBeanMap.size());
//                LOGGER.info("Total Data Rows : " + (beanReader.getRowNumber()-1));
//                LOGGER.info("Total Map Rows : " + (foBeanMap.size()));
//                LOGGER.info("Does all rows from csv accounted for ? : " + (beanReader.getRowNumber()-1 ==  foBeanMap.size() ? "Yes" : "No"));
//            }
        return foBeanMap == null ? localFoBeanMap : foBeanMap;
    }

    private List<FoBean> readCsv(File csvFile) {
        List<FoBean> beans = null;
        try {
            //csvFile = ResourceUtils.getFile("classpath:screens.csv");
            CsvMapper mapper = new CsvMapper();
            mapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
            CsvSchema schema = mapper.schemaFor(FoBean.class).withHeader();
            MappingIterator<FoBean> it = mapper.readerFor(FoBean.class).with(schema).readValues(csvFile);
            //return it.readAll();
            beans = new ArrayList<>();
            while (it.hasNextValue()) {
                beans.add(it.nextValue());
            }
            LOGGER.info("CSV, Total Rows Count: [{}]", beans.size());
        } catch (Exception e) {
            LOGGER.error("Error occurred while loading object list from file: {}", e);
            //return Collections.emptyList();
        }
        //System.out.println("TOTAL beans read=" + beans.size());
        //beans.forEach( row -> LOGGER.info("{}", row));
        return beans;
    }
}
