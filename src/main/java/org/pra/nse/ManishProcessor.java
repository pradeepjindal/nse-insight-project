package org.pra.nse;

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
import java.util.ArrayList;
import java.util.List;

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
        downloadManager.download(AppConstants.DOWNLOAD_FROM_DATE);
        //---------------------------------------------------------
        List<PraBean> praBeans = new ArrayList<>();
        // FO
        foMerge.merge(praBeans);
        // MAT
        matMerge.merge(praBeans);
        // CM
        cmMerge.merge(praBeans);

        //-------------------------------------------------------
        String manishOutputPathAndFileName = AppConstants.BASE_DATA_DIR
                + File.separator + AppConstants.PRA_DATA_DIR_NAME
                + File.separator + AppConstants.MANISH_FILE_NAME + AppConstants.PRA_DATA_FILE_EXT;
        manishCsvWriter.write(praBeans, manishOutputPathAndFileName);
        //-------------------------------------------------------
        String foLatestFileName = fileUtils.getLatestFileNameForFo(1);
        int fromIndex = foLatestFileName.lastIndexOf(".") -9;
        int toIndex = foLatestFileName.lastIndexOf(".");
        String fileDate = "-" + foLatestFileName.substring(fromIndex, toIndex);
        String outputPathAndFileName = AppConstants.BASE_DATA_DIR
                + File.separator + AppConstants.PRA_DATA_DIR_NAME
                + File.separator + AppConstants.MANISH_FILE_NAME + fileDate  + AppConstants.PRA_DATA_FILE_EXT;
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
