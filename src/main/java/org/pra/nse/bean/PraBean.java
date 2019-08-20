package org.pra.nse.bean;

import java.util.Date;
import java.util.Objects;

public class PraBean {
    private String instrument;
    private String symbol;
    private Date expiryDate;
    private double strikePrice;
    private String optionType;
    //
    private long contracts;
    //
    private double cmTodayClose;
    private double todayClose;
    private long todayOI;
    private long todayDelivery;
    private Date todayDate;
    //
    private double cmPreviousClose;
    private double previousClose;
    private long previousOI;
    private long previousDelivery;
    private Date previousDate;
    //
    private double cmPrcntChgInClose;
    private double prcntChgInClose;
    private double prcntChgInOI;
    private double prcntChgInDelivery;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PraBean praBean = (PraBean) o;
        return Double.compare(praBean.strikePrice, strikePrice) == 0 &&
                instrument.equals(praBean.instrument) &&
                symbol.equals(praBean.symbol) &&
                expiryDate.equals(praBean.expiryDate) &&
                optionType.equals(praBean.optionType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrument, symbol, expiryDate, strikePrice, optionType);
    }

    @Override
    public String toString() {
        return "PraBean{" +
                "instrument='" + instrument + '\'' +
                ", symbol='" + symbol + '\'' +
                ", expiryDate=" + expiryDate +
                ", strikePrice=" + strikePrice +
                ", optionType='" + optionType + '\'' +
                ", contracts=" + contracts +
                ", cmTodayClose=" + cmTodayClose +
                ", todayClose=" + todayClose +
                ", todayOI=" + todayOI +
                ", todayDelivery=" + todayDelivery +
                ", todayDate=" + todayDate +
                ", cmPreviousClose=" + cmPreviousClose +
                ", previousClose=" + previousClose +
                ", previousOI=" + previousOI +
                ", previousDelivery=" + previousDelivery +
                ", previousDate=" + previousDate +
                ", cmPrcntChgInClose=" + cmPrcntChgInClose +
                ", prcntChgInClose=" + prcntChgInClose +
                ", prcntChgInOI=" + prcntChgInOI +
                ", prcntChgInDelivery=" + prcntChgInDelivery +
                '}';
    }

    public String[] toArray() {
        String[] array = new String[5];
        array[0] = instrument;
        array[1] = symbol;
        array[2] = expiryDate.toString();
        array[3] = String.valueOf(strikePrice);
        array[4] = optionType;
        return array;
    }

    public String toCsvString() {
        return instrument
                + "," + symbol
                + "," + expiryDate.toString()
                + "," + String.valueOf(strikePrice)
                + "," + optionType;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public long getContracts() {
        return contracts;
    }

    public void setContracts(long contracts) {
        this.contracts = contracts;
    }

    public double getCmTodayClose() {
        return cmTodayClose;
    }

    public void setCmTodayClose(double cmTodayClose) {
        this.cmTodayClose = cmTodayClose;
    }

    public double getTodayClose() {
        return todayClose;
    }

    public void setTodayClose(double todayClose) {
        this.todayClose = todayClose;
    }

    public long getTodayOI() {
        return todayOI;
    }

    public void setTodayOI(long todayOI) {
        this.todayOI = todayOI;
    }

    public long getTodayDelivery() {
        return todayDelivery;
    }

    public void setTodayDelivery(long todayDelivery) {
        this.todayDelivery = todayDelivery;
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(Date todayDate) {
        this.todayDate = todayDate;
    }

    public double getCmPreviousClose() {
        return cmPreviousClose;
    }

    public void setCmPreviousClose(double cmPreviousClose) {
        this.cmPreviousClose = cmPreviousClose;
    }

    public double getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
    }

    public long getPreviousOI() {
        return previousOI;
    }

    public void setPreviousOI(long previousOI) {
        this.previousOI = previousOI;
    }

    public long getPreviousDelivery() {
        return previousDelivery;
    }

    public void setPreviousDelivery(long previousDelivery) {
        this.previousDelivery = previousDelivery;
    }

    public Date getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(Date previousDate) {
        this.previousDate = previousDate;
    }

    public double getCmPrcntChgInClose() {
        return cmPrcntChgInClose;
    }

    public void setCmPrcntChgInClose(double cmPrcntChgInClose) {
        this.cmPrcntChgInClose = cmPrcntChgInClose;
    }

    public double getPrcntChgInClose() {
        return prcntChgInClose;
    }

    public void setPrcntChgInClose(double prcntChgInClose) {
        this.prcntChgInClose = prcntChgInClose;
    }

    public double getPrcntChgInOI() {
        return prcntChgInOI;
    }

    public void setPrcntChgInOI(double prcntChgInOI) {
        this.prcntChgInOI = prcntChgInOI;
    }

    public double getPrcntChgInDelivery() {
        return prcntChgInDelivery;
    }

    public void setPrcntChgInDelivery(double prcntChgInDelivery) {
        this.prcntChgInDelivery = prcntChgInDelivery;
    }
}
