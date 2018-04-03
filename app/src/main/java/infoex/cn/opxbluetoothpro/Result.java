package infoex.cn.opxbluetoothpro;

/**
 * Author:Doraemon_xqw
 * Time:18.3.15
 * FileName:Result
 * Project:OpxblueToothPro
 * Package:infoex.cn.opxbluetoothpro
 * Company:YawooAI
 */
public class Result {
    int ylqd;
    double fbl;
    int time;
    double zsl;
    int fdcs;
    double wc;
    String date;
    String latlng;

    public Result(int ylqd, double fbl, int time, double zsl, int fdcs, double wc, String date, String latlng) {
        this.ylqd = ylqd;
        this.fbl = fbl;
        this.time = time;
        this.zsl = zsl;
        this.fdcs = fdcs;
        this.wc = wc;
        this.date = date;
        this.latlng = latlng;
    }

    public Result() {
    }



    public int getYlqd() {
        return ylqd;
    }

    public void setYlqd(int ylqd) {
        this.ylqd = ylqd;
    }

    public double getFbl() {
        return fbl;
    }

    public void setFbl(double fbl) {
        this.fbl = fbl;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getZsl() {
        return zsl;
    }

    public void setZsl(double zsl) {
        this.zsl = zsl;
    }

    public int getFdcs() {
        return fdcs;
    }

    public void setFdcs(int fdcs) {
        this.fdcs = fdcs;
    }

    public double getWc() {
        return wc;
    }

    public void setWc(double wc) {
        this.wc = wc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    @Override
    public String toString() {
        return "Result{" +
                "ylqd=" + ylqd +
                ", fbl=" + fbl +
                ", time=" + time +
                ", zsl=" + zsl +
                ", fdcs=" + fdcs +
                ", wc=" + wc +
                ", date='" + date + '\'' +
                ", latlng='" + latlng + '\'' +
                '}';
    }
}
