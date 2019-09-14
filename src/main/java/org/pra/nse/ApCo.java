package org.pra.nse;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ApCo {
    public static final LocalDate DOWNLOAD_FROM_DATE = LocalDate.of(2019,9,10);

    public static final String BASE_DATA_DIR = System.getProperty("user.home");

    //https://www.nseindia.com/content/historical/EQUITIES/2019/SEP/cm10SEP2019bhav.csv.zip
    public static final String CM_BASE_URL = "https://www.nseindia.com/content/historical/EQUITIES/2019";
    public static final String FO_BASE_URL = "https://www.nseindia.com/content/historical/DERIVATIVES/2019";
    public static final String MT_BASE_URL = "https://www.nseindia.com/archives/equities/mto";
    //https://www.nseindia.com/content/nsccl/fao_participant_vol_13092019.csv
    public static final String BP_BASE_URL = "https://www.nseindia.com/content/nsccl";

    public static final String CM_DIR_NAME = "pra-nse-cm";
    public static final String FO_DIR_NAME = "pra-nse-fo";
    public static final String MT_DIR_NAME = "pra-nse-mt";
    public static final String BP_DIR_NAME = "pra-nse-bp";

    public static final String CM_FILE_NAME_DATE_FORMAT = "ddMMMyyyy";
    public static final String FO_FILE_NAME_DATE_FORMAT = "ddMMMyyyy";
    public static final String MT_FILE_NAME_DATE_FORMAT = "ddMMyyyy";
    public static final String BP_FILE_NAME_DATE_FORMAT = "ddMMyyyy";
    public static final String PRA_FILE_NAME_DATE_FORMAT = "yyyy-MM-dd";

    public static final String PRA_DATA_DIR_NAME = "pra-nse-computed-data";
    public static final String PRA_DATA_DATE_FORMAT = "dd-MMM-yyyy";

    public static final String CM_NSE_FILE_PREFIX = "cm";
    public static final String FO_NSE_FILE_PREFIX = "fo";
    public static final String MT_NSE_FILE_PREFIX = "MTO_";
    public static final String BP_NSE_FILE_PREFIX = "fao_participant_vol_";

    public static final String CM_DATA_FILE_PREFIX = "cm-";
    public static final String FO_DATA_FILE_PREFIX = "fo-";
    public static final String MT_DATA_FILE_PREFIX = "mt-";
    public static final String BP_DATA_FILE_PREFIX = "bp-";

    public static final String CM_FILE_SUFFIX = "bhav.csv.zip";
    public static final String FO_FILE_SUFFIX = "bhav.csv.zip";
    public static final String MT_FILE_EXT = ".DAT";
    public static final String BP_FILE_EXT = ".csv";
    public static final String PRA_DATA_FILE_EXT = ".csv";

    public static final String MANISH_FILE_NAME = "manishData";
    public static final String PRADEEP_FILE_NAME = "pradeepData";


    public static final DateTimeFormatter CM_DTF = DateTimeFormatter.ofPattern(ApCo.CM_FILE_NAME_DATE_FORMAT);
    public static final String CM_FILES_PATH = ApCo.BASE_DATA_DIR + File.separator + ApCo.CM_DIR_NAME;

    public static final DateTimeFormatter FO_DTF = DateTimeFormatter.ofPattern(ApCo.FO_FILE_NAME_DATE_FORMAT);
    public static final String FO_FILES_PATH = ApCo.BASE_DATA_DIR + File.separator + ApCo.FO_DIR_NAME;

    public static final DateTimeFormatter MT_DTF = DateTimeFormatter.ofPattern(ApCo.MT_FILE_NAME_DATE_FORMAT);
    public static final String MT_FILES_PATH = ApCo.BASE_DATA_DIR + File.separator + ApCo.MT_DIR_NAME;

    public static final DateTimeFormatter PRA_DTF = DateTimeFormatter.ofPattern(ApCo.PRA_FILE_NAME_DATE_FORMAT);
    public static final String PRA_FILES_PATH = ApCo.BASE_DATA_DIR + File.separator + ApCo.PRA_DATA_DIR_NAME;
}
