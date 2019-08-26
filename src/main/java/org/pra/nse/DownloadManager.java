package org.pra.nse;

import org.pra.nse.csv.downloader.CmDownloader;
import org.pra.nse.csv.downloader.FoDownloader;
import org.pra.nse.csv.downloader.MtoDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DownloadManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadManager.class);

    private final CmDownloader cmDownloader;
    private final FoDownloader foDownloader;
    private final MtoDownloader matDownloader;

    public DownloadManager(CmDownloader cmDownloader,
                           FoDownloader foDownloader,
                           MtoDownloader matDownloader) {
        this.cmDownloader = cmDownloader;
        this.foDownloader = foDownloader;
        this.matDownloader = matDownloader;
    }

    public void download(LocalDate downloadFromDate) {
        cmDownloader.download(downloadFromDate);
        foDownloader.download(downloadFromDate);
        matDownloader.download(downloadFromDate);
    }
}
