package org.pra.bean;

import java.util.Date;
import java.util.Objects;

public class FoBean {
    private String Instrument;
    private String Symbol;
    private Date Expiry_Dt;
    private double Strike_Pr;
    private String Option_Typ;
    private double open;
    private double High;
    private double Low;
    private double Close;
    private double Settle_Pr;
    private long Contracts;
    private double Val_InLakh;
    private long Open_Int;
    private long Chg_In_Oi;
    private Date Timestamp;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoBean foBean = (FoBean) o;
        return Double.compare(foBean.Strike_Pr, Strike_Pr) == 0 &&
                Instrument.equals(foBean.Instrument) &&
                Symbol.equals(foBean.Symbol) &&
                Expiry_Dt.equals(foBean.Expiry_Dt) &&
                Option_Typ.equals(foBean.Option_Typ);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Instrument, Symbol, Expiry_Dt, Strike_Pr, Option_Typ);
    }

    @Override
    public String toString() {
        return "FoBean{" +
                "Instrument='" + Instrument + '\'' +
                ", Symbol='" + Symbol + '\'' +
                ", Expiry_Dt=" + Expiry_Dt +
                ", Strike_Pr=" + Strike_Pr +
                ", Option_Typ='" + Option_Typ + '\'' +
                ", open=" + open +
                ", High=" + High +
                ", Low=" + Low +
                ", Close=" + Close +
                ", Settle_Pr=" + Settle_Pr +
                ", Contracts=" + Contracts +
                ", Val_InLakh=" + Val_InLakh +
                ", Open_Int=" + Open_Int +
                ", Chg_In_Oi=" + Chg_In_Oi +
                ", Timestamp=" + Timestamp +
                '}';
    }

    public String getInstrument() {
        return Instrument;
    }

    public void setInstrument(String instrument) {
        Instrument = instrument;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public Date getExpiry_Dt() {
        return Expiry_Dt;
    }

    public void setExpiry_Dt(Date expiry_Dt) {
        Expiry_Dt = expiry_Dt;
    }

    public double getStrike_Pr() {
        return Strike_Pr;
    }

    public void setStrike_Pr(double strike_Pr) {
        Strike_Pr = strike_Pr;
    }

    public String getOption_Typ() {
        return Option_Typ;
    }

    public void setOption_Typ(String option_Typ) {
        Option_Typ = option_Typ;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return High;
    }

    public void setHigh(double high) {
        High = high;
    }

    public double getLow() {
        return Low;
    }

    public void setLow(double low) {
        Low = low;
    }

    public double getClose() {
        return Close;
    }

    public void setClose(double close) {
        Close = close;
    }

    public double getSettle_Pr() {
        return Settle_Pr;
    }

    public void setSettle_Pr(double settle_Pr) {
        Settle_Pr = settle_Pr;
    }

    public long getContracts() {
        return Contracts;
    }

    public void setContracts(long contracts) {
        Contracts = contracts;
    }

    public double getVal_InLakh() {
        return Val_InLakh;
    }

    public void setVal_InLakh(double val_InLakh) {
        Val_InLakh = val_InLakh;
    }

    public long getOpen_Int() {
        return Open_Int;
    }

    public void setOpen_Int(long open_Int) {
        Open_Int = open_Int;
    }

    public long getChg_In_Oi() {
        return Chg_In_Oi;
    }

    public void setChg_In_Oi(long chg_In_Oi) {
        Chg_In_Oi = chg_In_Oi;
    }

    public Date getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Date timestamp) {
        Timestamp = timestamp;
    }
}
