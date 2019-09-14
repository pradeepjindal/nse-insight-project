package org.pra.nse.processor;

import org.pra.nse.ApCo;
import org.pra.nse.ProCo;
import org.pra.nse.bean.out.PraBean;
import org.pra.nse.csv.download.DownloadManager;
import org.pra.nse.csv.merge.CmMerger;
import org.pra.nse.csv.merge.FoMerger;
import org.pra.nse.csv.merge.MtMerger;
import org.pra.nse.csv.read.FoCsvReader;
import org.pra.nse.csv.writer.PradeepCsvWriter;
import org.pra.nse.util.PraNameUtils;
import org.pra.nse.util.NseFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


@Component
public class PradeepProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(PradeepProcessor.class);

    private final DownloadManager downloadManager;
    private final FoCsvReader csvReader;
    private final CmMerger cmMerger;
    private final FoMerger foMerger;
    private final MtMerger mtMerger;
    private final NseFileUtils nseFileUtils;
    private final PraNameUtils praNameUtils;
    private final PradeepCsvWriter csvWriter;

    public PradeepProcessor(DownloadManager downloadManager,
                            FoCsvReader csvReader,
                            CmMerger cmMerger, FoMerger foMerger, MtMerger mtMerger,
                            PradeepCsvWriter csvWriter,
                            NseFileUtils nseFileUtils,
                            PraNameUtils praNameUtils) {
        this.downloadManager = downloadManager;
        this.csvReader = csvReader;
        this.cmMerger = cmMerger;
        this.foMerger = foMerger;
        this.mtMerger = mtMerger;
        this.csvWriter = csvWriter;
        this.nseFileUtils = nseFileUtils;
        this.praNameUtils = praNameUtils;
    }

    public void process(LocalDate processForDate) throws IOException {

        List<PraBean> praBeans = new ArrayList<>();
        // FO
        TreeSet<LocalDate> foMonthlyExpiryDates = foMerger.merge(praBeans);
        // MAT
        mtMerger.merge(praBeans);
        // CM
        cmMerger.merge(praBeans);

        //-------------------------------------------------------
        String outputPathAndFileNameForFixFile = ProCo.outputPathAndFileNameForFixFile(ApCo.PRADEEP_FILE_NAME);
        csvWriter.write(praBeans, outputPathAndFileNameForFixFile, foMonthlyExpiryDates);
        //-------------------------------------------------------
        //String foLatestFileName = fileUtils.getLatestFileNameForFo(1);
        String foLatestFileName = praNameUtils.getLatestFileNameFor(ApCo.FO_FILES_PATH, ApCo.FO_DATA_FILE_PREFIX, ApCo.PRA_DATA_FILE_EXT, 1);
        String outputPathAndFileNameForDynamicFile = ProCo.outputPathAndFileNameForDynamicFile(ApCo.PRADEEP_FILE_NAME, foLatestFileName);
                csvWriter.write(praBeans, outputPathAndFileNameForDynamicFile, foMonthlyExpiryDates);
        //-------------------------------------------------------

        if(nseFileUtils.isFileExist(outputPathAndFileNameForDynamicFile)) {
            LOGGER.info("---------------------------------------------------------------------------------------------------------------");
            LOGGER.info("SUCCESS! praData.csv File has been placed at: " + outputPathAndFileNameForDynamicFile);
            LOGGER.info("---------------------------------------------------------------------------------------------------------------");
        } else {
            LOGGER.info("ERROR! something got wrong, could not create data file.");
        }
    }

    //@Override
    public void run(ApplicationArguments args) {
        LOGGER.info("Pradeep Processor | ============================== | Kicking");
		try {
            process (LocalDate.now());
		} catch(Exception e) {
			e.printStackTrace();
		}
        LOGGER.info("Pradeep Processor | ============================== | Finished");
    }
}
