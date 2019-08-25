package org.pra.nse;

import org.pra.nse.bean.FoBean;
import org.pra.nse.bean.PraBean;
import org.pra.nse.csv.merge.CmMerge;
import org.pra.nse.csv.merge.FoMerge;
import org.pra.nse.csv.merge.MatMerge;
import org.pra.nse.csv.reader.FoCsvReader;
import org.pra.nse.csv.writer.ManishCsvWriter;
import org.pra.nse.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class ManishProcessor implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManishProcessor.class);

    private final DownloadManager downloadManager;
    private final FoCsvReader csvReader;
    private final CmMerge cmMerge;
    private final FoMerge foMerge;
    private final MatMerge matMerge;
    private final FileUtils fileUtils;
    private final ManishCsvWriter manishCsvWriter;

    public ManishProcessor(DownloadManager downloadManager,
                           FoCsvReader csvReader,
                           CmMerge cmMerge, FoMerge foMerge, MatMerge matMerge,
                           ManishCsvWriter manishCsvWriter,
                           FileUtils fileUtils) {
        this.downloadManager = downloadManager;
        this.csvReader = csvReader;
        this.cmMerge = cmMerge;
        this.foMerge = foMerge;
        this.matMerge = matMerge;
        this.manishCsvWriter = manishCsvWriter;
        this.fileUtils = fileUtils;
    }

    public void process() {
        downloadManager.download();
        //---------------------------------------------------------
        Map<FoBean, FoBean> foBeanMap;
        String foLatestFileName = fileUtils.getLatestFileNameForFo(1);
        LOGGER.info("latestFileName FO: " + foLatestFileName);
        String foPreviousFileName = fileUtils.getLatestFileNameForFo(2);
        LOGGER.info("previousFileName FO: " + foPreviousFileName);

        // FO
        foBeanMap = csvReader.read(null, foLatestFileName);
        LOGGER.info("{}",foBeanMap.size());

        csvReader.read(foBeanMap, foPreviousFileName);
        LOGGER.info("{}",foBeanMap.size());

        List<PraBean> praBeans = foMerge.merge(foBeanMap);
        //praBeans.forEach(bean -> LOGGER.info(bean));
        LOGGER.info("{}",praBeans.size());

        // MAT
        matMerge.merge(praBeans);
        // CM
        cmMerge.merge(praBeans);

        //-------------------------------------------------------
        String manishOutputPathAndFileName = AppConstants.BASE_DATA_DIR
                + File.separator + AppConstants.DATA_DIR_NAME
                + File.separator + AppConstants.MANISH_FILE_NAME + AppConstants.CSV_FILE_SUFFIX;
        manishCsvWriter.write(praBeans, manishOutputPathAndFileName);
        //-------------------------------------------------------
        int fromIndex = foLatestFileName.lastIndexOf(".") -9;
        int toIndex = foLatestFileName.lastIndexOf(".");
        String fileDate = "-" + foLatestFileName.substring(fromIndex, toIndex);
        String outputPathAndFileName = AppConstants.BASE_DATA_DIR
                + File.separator + AppConstants.DATA_DIR_NAME
                + File.separator + AppConstants.MANISH_FILE_NAME + fileDate  + AppConstants.CSV_FILE_SUFFIX;
        manishCsvWriter.write(praBeans, outputPathAndFileName);
        //-------------------------------------------------------

        if(fileUtils.isFileExist(outputPathAndFileName)) {
            LOGGER.info("---------------------------------------------------------------------------------------------------------------");
            LOGGER.info("SUCCESS! praData.csv File has been placed at: " + outputPathAndFileName);
            LOGGER.info("---------------------------------------------------------------------------------------------------------------");
        } else {
            LOGGER.info("ERROR! something got wrong, could not create data file.");
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        LOGGER.info("Manish Processor | ============================== | Kicking");
		try {
            process();
		} catch(Exception e) {
			e.printStackTrace();
		}
        LOGGER.info("Manish Processor | ============================== | Completed");
    }
}
