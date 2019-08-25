package org.pra.nse;

import org.pra.nse.csv.downloader.CmDownloader;
import org.pra.nse.csv.downloader.FoDownloader;
import org.pra.nse.csv.downloader.MatDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DownloadManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadManager.class);

    private final CmDownloader cmDownloader;
    private final FoDownloader foDownloader;
    private final MatDownloader matDownloader;

    public DownloadManager(CmDownloader cmDownloader, FoDownloader foDownloader, MatDownloader matDownloader) {
        this.cmDownloader = cmDownloader;
        this.foDownloader = foDownloader;
        this.matDownloader = matDownloader;
    }

    public void download() {
        cmDownloader.download();
        foDownloader.download();
        matDownloader.download();
    }
}
