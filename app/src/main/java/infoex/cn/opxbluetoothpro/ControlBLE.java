package infoex.cn.opxbluetoothpro;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author:Doraemon_xqw
 * Time:18.3.13
 * FileName:ControlBLE
 * Project:OpxblueToothPro
 * Package:infoex.cn.opxbluetoothpro
 * Company:YawooAI
 */
public class ControlBLE {
    private byte head;
    private int lengthB;
    private byte command;
    private byte[] info;
    private byte check;

    public ControlBLE(byte[] val) {
        head = val[0];
        lengthB = val[1];
        command =val[2];
        info  = new byte[lengthB -1];
        for (int i = 0; i< lengthB -1; i++){
            info[i] = val[i+3];
        }
        Log.e("control",bytesToHexString(info));
        check = val[val.length-1];

    }

    public byte getHead() {
        return head;
    }

    public void setHead(byte head) {
        this.head = head;
    }

    public int getLengthB() {
        return lengthB;
    }

    public void setLengthB(byte lengthB) {
        this.lengthB = lengthB;
    }

    public byte getCommand() {
        return command;
    }

    public void setCommand(byte command) {
        this.command = command;
    }

    public byte[] getInfo() {
        return info;
    }

    public void setInfo(byte[] info) {
        this.info = info;
    }

    public byte getCheck() {
        return check;
    }

    public void setCheck(byte check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "ControlBLE{" +
                "head=" + head +
                ", lengthB=" + lengthB +
                ", command=" + command +
                ", info=" + Arrays.toString(info) +
                ", check=" + check +
                '}';
    }

    public static boolean CheckControl(ControlBLE controlBLE){
        byte ch = 0;
        byte ack =0;
        byte command = controlBLE.getCommand();
        for (int i = 0;i<controlBLE.getInfo().length;i++){
            ch+=controlBLE.getInfo()[i];
        }
        ch += command;
        if (ch == controlBLE.getCheck()){
            return true;
        }else {
            return false;
        }
    }

    public static String bytesToHexString(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toHexString(bytes[i] & 0xFF);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            result += hexString.toUpperCase();
        }
        return result;
    }
}
