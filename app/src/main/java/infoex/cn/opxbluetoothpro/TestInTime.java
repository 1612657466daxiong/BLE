package infoex.cn.opxbluetoothpro;

/**
 * Author:Doraemon_xqw
 * Time:18.3.15
 * FileName:TestInTime
 * Project:OpxblueToothPro
 * Package:infoex.cn.opxbluetoothpro
 * Company:YawooAI
 */
public class TestInTime {
    String date;
    double ZSL;
    int FDCS;
    double WC;
    int Time;
    boolean isControl;
    String controlS;
    int Ylqd;
    double fbl;
    int dur;

    public TestInTime(String date, double ZSL, int FDCS, double WC, int time, boolean isControl, String controlS, int ylqd, double fbl, int dur) {
        this.date = date;
        this.ZSL = ZSL;
        this.FDCS = FDCS;
        this.WC = WC;
        Time = time;
        this.isControl = isControl;
        this.controlS = controlS;
        Ylqd = ylqd;
        this.fbl = fbl;
        this.dur = dur;
    }

    public int getYlqd() {
        return Ylqd;
    }

    public void setYlqd(int ylqd) {
        Ylqd = ylqd;
    }

    public double getFbl() {
        return fbl;
    }

    public void setFbl(double fbl) {
        this.fbl = fbl;
    }

    public int getDur() {
        return dur;
    }

    public void setDur(int dur) {
        this.dur = dur;
    }

    public String getControlS() {
        return controlS;
    }

    public void setControlS(String controlS) {
        this.controlS = controlS;
    }

    @Override
    public String toString() {
        return "TestInTime{" +
                "date='" + date + '\'' +
                ", ZSL=" + ZSL +
                ", FDCS=" + FDCS +
                ", WC=" + WC +
                ", Time=" + Time +
                ", isControl=" + isControl +
                ", controlS='" + controlS + '\'' +
                ", Ylqd=" + Ylqd +
                ", fbl=" + fbl +
                ", dur=" + dur +
                '}';
    }

    public TestInTime(String date, double ZSL, int FDCS, double WC, int time, boolean isControl) {
        this.date = date;
        this.ZSL = ZSL;
        this.FDCS = FDCS;
        this.WC = WC;
        Time = time;
        this.isControl = isControl;
    }

    public boolean isControl() {
        return isControl;
    }

    public void setControl(boolean control) {
        isControl = control;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TestInTime(String date, double ZSL, int FDCS, double WC, int time) {
        this.date = date;
        this.ZSL = ZSL;
        this.FDCS = FDCS;
        this.WC = WC;
        Time = time;
    }

    public TestInTime() {
    }

    public double getZSL() {
        return ZSL;
    }

    public void setZSL(double ZSL) {
        this.ZSL = ZSL;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }

    public double getWC() {
        return WC;
    }

    public void setWC(double WC) {
        this.WC = WC;
    }

    public int getFDCS() {
        return FDCS;
    }

    public void setFDCS(int FDCS) {
        this.FDCS = FDCS;
    }

}
