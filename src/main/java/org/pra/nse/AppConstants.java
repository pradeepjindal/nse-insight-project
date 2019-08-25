package org.pra.nse;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AppConstants {
    public static final LocalDate DOWNLOAD_FROM_DATE = LocalDate.of(2019,8,1);

    public static final String BASE_DATA_DIR = System.getProperty("user.home");

    public static final String CM_BASE_URL = "https://www.nseindia.com/content/historical/EQUITIES/2019";
    public static final String FO_BASE_URL = "https://www.nseindia.com/content/historical/DERIVATIVES/2019";
    public static final String MTO_BASE_URL = "https://www.nseindia.com/archives/equities/mto";

    public static final String CM_DIR_NAME = "pra-nse-cm";
    public static final String FO_DIR_NAME = "pra-nse-fo";
    public static final String MTO_DIR_NAME = "pra-nse-mto";

    public static final String CM_FILE_NAME_DATE_FORMAT = "ddMMMyyyy";
    public static final String FO_FILE_NAME_DATE_FORMAT = "ddMMMyyyy";
    public static final String MTO_FILE_NAME_DATE_FORMAT = "ddMMyyyy";

    public static final String DATA_DIR_NAME = "pra-nse-computed-data";
    public static final String DATA_DATE_FORMAT = "dd-MMM-yyyy";

    public static final String CM_FILE_PREFIX = "cm";
    public static final String FO_FILE_PREFIX = "fo";
    public static final String MTO_FILE_PREFIX = "MTO_";

    public static final String CM_FILE_SUFFIX = "bhav.csv.zip";
    public static final String FO_FILE_SUFFIX = "bhav.csv.zip";
    public static final String MTO_FILE_SUFFIX = ".DAT";
    public static final String CSV_FILE_SUFFIX = ".csv";

    public static final String MANISH_FILE_NAME = "manishData";


    public static final DateTimeFormatter cmFormatter = DateTimeFormatter.ofPattern(AppConstants.CM_FILE_NAME_DATE_FORMAT);
    public static final String cmDir = AppConstants.BASE_DATA_DIR + File.separator + AppConstants.CM_DIR_NAME;

    public static final DateTimeFormatter foFormatter = DateTimeFormatter.ofPattern(AppConstants.FO_FILE_NAME_DATE_FORMAT);
    public static final String foDir = AppConstants.BASE_DATA_DIR + File.separator + AppConstants.FO_DIR_NAME;

    public static final DateTimeFormatter matFormatter = DateTimeFormatter.ofPattern(AppConstants.MTO_FILE_NAME_DATE_FORMAT);
    public static final String matDir = AppConstants.BASE_DATA_DIR + File.separator + AppConstants.MTO_DIR_NAME;
}
