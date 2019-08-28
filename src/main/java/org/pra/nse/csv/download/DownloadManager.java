package org.pra.nse.csv.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DownloadManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadManager.class);

    private final CmDownloader cmDownloader;
    private final FoDownloader foDownloader;
    private final MtDownloader matDownloader;

    public DownloadManager(CmDownloader cmDownloader,
                           FoDownloader foDownloader,
                           MtDownloader matDownloader) {
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
