package infoex.cn.opxbluetoothpro;

import android.app.Application;
import android.content.Context;

/**
 * Author:Doraemon_xqw
 * Time:18.3.16
 * FileName:OPXApplication
 * Project:OpxblueToothPro
 * Package:infoex.cn.opxbluetoothpro
 * Company:YawooAI
 */
public class OPXApplication extends Application {
    static OPXApplication applicationContext;
    public OPXApplication(){
        applicationContext =this;
    }
    public static   OPXApplication getInstance() {
        if (applicationContext==null){
            applicationContext = new OPXApplication();
        }
        return applicationContext;
    }
}
