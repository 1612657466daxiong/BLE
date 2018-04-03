package infoex.cn.opxbluetoothpro;

/**
 * Author:Doraemon_xqw
 * Time:18.3.19
 * FileName:Status
 * Project:OpxblueToothPro
 * Package:infoex.cn.opxbluetoothpro
 * Company:YawooAI
 */
public class Status {

    /**
     * code : 14
     * msg : 上传数据成功！
     */

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Status{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
