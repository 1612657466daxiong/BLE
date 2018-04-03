package infoex.cn.opxbluetoothpro;

/**
 * Author:Doraemon_xwq
 * Time:17.10.18
 * FileName:BaseBean
 * Project:WAI
 * Package:infoex.cn.wai.base
 * Company:YawooAI
 */
public class BaseBean {
    private byte id;
    private String name;

    public BaseBean(byte id, String name) {
        this.id = id;
        this.name = name;
    }

    public BaseBean() {
    }

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
