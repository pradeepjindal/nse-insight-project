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
    private final MtDownloader mtDownloader;
    private final BpDownloader bpDownloader;

    public DownloadManager(CmDownloader cmDownloader,
                           FoDownloader foDownloader,
                           MtDownloader mtDownloader,
                           BpDownloader bpDownloader) {
        this.cmDownloader = cmDownloader;
        this.foDownloader = foDownloader;
        this.mtDownloader = mtDownloader;
        this.bpDownloader = bpDownloader;
    }

    public void download(LocalDate downloadFromDate) {
        cmDownloader.download(downloadFromDate);
        foDownloader.download(downloadFromDate);
        mtDownloader.download(downloadFromDate);
        bpDownloader.download(downloadFromDate);
    }
}
