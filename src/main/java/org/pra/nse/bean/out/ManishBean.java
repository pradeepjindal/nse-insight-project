package org.pra.nse.bean.out;

import java.time.LocalDate;
import java.util.Date;

public class ManishBean {
    private String symbol;
    private Date expiryDate;
    private LocalDate expiryLocalDate;
    private Date tdyDate;
    //

    private double tdyClose;
    private long tdyTraded;
    private long tdyDelivery;
    private double deliveryToTradeRatio;
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

    public Date getTdyDate() {
        return tdyDate;
    }

    public void setTdyDate(Date tdyDate) {
        this.tdyDate = tdyDate;
    }

    public LocalDate getExpiryLocalDate() {
        return expiryLocalDate;
    }

    public void setExpiryLocalDate(LocalDate expiryLocalDate) {
        this.expiryLocalDate = expiryLocalDate;
    }

    public double getTdyClose() {
        return tdyClose;
    }

    public void setTdyClose(double tdyClose) {
        this.tdyClose = tdyClose;
    }

    public long getTdyTraded() {
        return tdyTraded;
    }

    public void setTdyTraded(long tdyTraded) {
        this.tdyTraded = tdyTraded;
    }

    public long getTdyDelivery() {
        return tdyDelivery;
    }

    public void setTdyDelivery(long tdyDelivery) {
        this.tdyDelivery = tdyDelivery;
    }

    public double getDeliveryToTradeRatio() {
        return deliveryToTradeRatio;
    }

    public void setDeliveryToTradeRatio(double deliveryToTradeRatio) {
        this.deliveryToTradeRatio = deliveryToTradeRatio;
    }

    public long getTdyOI() {
        return tdyOI;
    }

    public void setTdyOI(long tdyOI) {
        this.tdyOI = tdyOI;
    }
}
