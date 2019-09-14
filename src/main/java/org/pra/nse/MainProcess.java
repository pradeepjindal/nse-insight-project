package org.pra.nse;

import org.pra.nse.csv.download.DownloadManager;
import org.pra.nse.processor.ManishProcessor;
import org.pra.nse.processor.PradeepProcessor;
import org.pra.nse.processor.PradeepProcessorB;
import org.pra.nse.util.PraNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MainProcess implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainProcess.class);

    private final PraNameUtils praNameUtils;
    private final DownloadManager downloadManager;
    private final PradeepProcessor pradeepProcessor;
    private final PradeepProcessorB pradeepProcessorB;
    private final ManishProcessor manishProcessor;

    public MainProcess(PraNameUtils praNameUtils,
                       DownloadManager downloadManager,
                       PradeepProcessor pradeepProcessor,
                       PradeepProcessorB pradeepProcessorB,
                       ManishProcessor manishProcessor) {
        this.praNameUtils = praNameUtils;
        this.downloadManager = downloadManager;
        this.pradeepProcessor = pradeepProcessor;
        this.pradeepProcessorB = pradeepProcessorB;
        this.manishProcessor = manishProcessor;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("Main Process | ============================== | commencing");
        try {
            downloadManager.download(ApCo.DOWNLOAD_FROM_DATE);
            praNameUtils.validate();
            //---------------------------------------------------------
            pradeepProcessor.process(LocalDate.now());
            //pradeepProcessorB.process(LocalDate.now());
            manishProcessor.process(LocalDate.now());
        } catch(Exception e) {
            LOGGER.error("ERROR: {}",e);
        }
        LOGGER.info("Main Process | ============================== | finishing");
    }
}
