package org.pra.nse.bean;

import java.time.LocalDate;
import java.util.Date;

public class ManishBean {
    private String symbol;
    private Date expiryDate;
    private LocalDate expiryLocalDate;
    //
    private Date tdyDate;
    private LocalDate tdyLocalDate;
    private double cmTdyClose;
    private long tdyVolume;
    private long tdyDelivery;
    private double prcntChgInDelivery;
    private long tdyOI;


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

    public LocalDate getExpiryLocalDate() {
        return expiryLocalDate;
    }

    public void setExpiryLocalDate(LocalDate expiryLocalDate) {
        this.expiryLocalDate = expiryLocalDate;
    }

    public Date getTdyDate() {
        return tdyDate;
    }

    public void setTdyDate(Date tdyDate) {
        this.tdyDate = tdyDate;
    }

    public LocalDate getTdyLocalDate() {
        return tdyLocalDate;
    }

    public void setTdyLocalDate(LocalDate tdyLocalDate) {
        this.tdyLocalDate = tdyLocalDate;
    }

    public double getCmTdyClose() {
        return cmTdyClose;
    }

    public void setCmTdyClose(double cmTdyClose) {
        this.cmTdyClose = cmTdyClose;
    }

    public long getTdyVolume() {
        return tdyVolume;
    }

    public void setTdyVolume(long tdyVolume) {
        this.tdyVolume = tdyVolume;
    }

    public long getTdyDelivery() {
        return tdyDelivery;
    }

    public void setTdyDelivery(long tdyDelivery) {
        this.tdyDelivery = tdyDelivery;
    }

    public double getPrcntChgInDelivery() {
        return prcntChgInDelivery;
    }

    public void setPrcntChgInDelivery(double prcntChgInDelivery) {
        this.prcntChgInDelivery = prcntChgInDelivery;
    }

    public long getTdyOI() {
        return tdyOI;
    }

    public void setTdyOI(long tdyOI) {
        this.tdyOI = tdyOI;
    }
}
