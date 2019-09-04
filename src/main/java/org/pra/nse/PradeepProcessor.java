package org.pra.nse;

import org.pra.nse.bean.PraBean;
import org.pra.nse.csv.download.DownloadManager;
import org.pra.nse.csv.merge.CmMerger;
import org.pra.nse.csv.merge.FoMerger;
import org.pra.nse.csv.merge.MtMerger;
import org.pra.nse.csv.read.FoCsvReader;
import org.pra.nse.csv.writer.PradeepCsvWriter;
import org.pra.nse.util.FileNameUtils;
import org.pra.nse.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


@Component
public class PradeepProcessor implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(PradeepProcessor.class);

    private final DownloadManager downloadManager;
    private final FoCsvReader csvReader;
    private final CmMerger cmMerger;
    private final FoMerger foMerger;
    private final MtMerger mtMerger;
    private final FileUtils fileUtils;
    private final FileNameUtils fileNameUtils;
    private final PradeepCsvWriter csvWriter;

    public PradeepProcessor(DownloadManager downloadManager,
                            FoCsvReader csvReader,
                            CmMerger cmMerger, FoMerger foMerger, MtMerger mtMerger,
                            PradeepCsvWriter csvWriter,
                            FileUtils fileUtils,
                            FileNameUtils fileNameUtils) {
        this.downloadManager = downloadManager;
        this.csvReader = csvReader;
        this.cmMerger = cmMerger;
        this.foMerger = foMerger;
        this.mtMerger = mtMerger;
        this.csvWriter = csvWriter;
        this.fileUtils = fileUtils;
        this.fileNameUtils = fileNameUtils;
    }

    public void process(LocalDate downloadFromDate, LocalDate processForDate) throws IOException {
        downloadManager.download(downloadFromDate);
        fileNameUtils.validate();
        //---------------------------------------------------------
        List<PraBean> praBeans = new ArrayList<>();
        // FO
        TreeSet<LocalDate> foMonthlyExpiryDates = foMerger.merge(praBeans);
        // MAT
        mtMerger.merge(praBeans);
        // CM
        cmMerger.merge(praBeans);

        //-------------------------------------------------------
        String manishOutputPathAndFileName =
                ApCo.BASE_DATA_DIR
                + File.separator + ApCo.PRA_DATA_DIR_NAME
                + File.separator + ApCo.PRADEEP_FILE_NAME + ApCo.PRA_DATA_FILE_EXT;
        csvWriter.write(praBeans, manishOutputPathAndFileName, foMonthlyExpiryDates);
        //-------------------------------------------------------
        //String foLatestFileName = fileUtils.getLatestFileNameForFo(1);
        String foLatestFileName = fileNameUtils.getLatestFileNameFor(ApCo.FO_FILES_PATH, ApCo.FO_DATA_FILE_PREFIX, ApCo.PRA_DATA_FILE_EXT, 1);
        int fromIndex = foLatestFileName.lastIndexOf(".") -10;
        int toIndex = foLatestFileName.lastIndexOf(".");
        String fileDate = "-" + foLatestFileName.substring(fromIndex, toIndex);
        String outputPathAndFileName = ApCo.BASE_DATA_DIR
                + File.separator + ApCo.PRA_DATA_DIR_NAME
                + File.separator + ApCo.PRADEEP_FILE_NAME + fileDate  + ApCo.PRA_DATA_FILE_EXT;
        csvWriter.write(praBeans, outputPathAndFileName, foMonthlyExpiryDates);
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
        LOGGER.info("Pradeep Processor | ============================== | Kicking");
		try {
            process(ApCo.DOWNLOAD_FROM_DATE, LocalDate.now());
		} catch(Exception e) {
			e.printStackTrace();
		}
        LOGGER.info("Pradeep Processor | ============================== | Finished");
    }
}
