package org.pra.nse;

public class AppConstants {
    public static String BASE_DATA_DIR = System.getProperty("user.home");

    public static String CM_BASE_URL = "https://www.nseindia.com/content/historical/EQUITIES/2019";
    public static String FO_BASE_URL = "https://www.nseindia.com/content/historical/DERIVATIVES/2019";
    public static String MTO_BASE_URL = "https://www.nseindia.com/archives/equities/mto";

    public static String CM_DIR_NAME = "pra-nse-cm";
    public static String FO_DIR_NAME = "pra-nse-fo";
    public static String MTO_DIR_NAME = "pra-nse-mto";

    public static String CM_FILE_NAME_DATE_FORMAT = "ddMMMyyyy";
    public static String FO_FILE_NAME_DATE_FORMAT = "ddMMMyyyy";
    public static String MTO_FILE_NAME_DATE_FORMAT = "ddMMyyyy";

    public static String DATA_DIR_NAME = "pra-nse-computed-data";
    public static String DATA_DATE_FORMAT = "dd-MMM-yyyy";

    public static String CM_FILE_PREFIX = "cm";
    public static String FO_FILE_PREFIX = "fo";
    public static String MTO_FILE_PREFIX = "MTO_";

    public static String CM_FILE_SUFFIX = "bhav.csv.zip";
    public static String FO_FILE_SUFFIX = "bhav.csv.zip";
    public static String MTO_FILE_SUFFIX = ".DAT";

    public static String MANISH_FILE_NAME = "manishData.csv";
}
