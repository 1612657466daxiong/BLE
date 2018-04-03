package infoex.cn.opxbluetoothpro;

/**
 * Author:Doraemon_xqw
 * Time:18.3.15
 * FileName:I
 * Project:OpxblueToothPro
 * Package:infoex.cn.opxbluetoothpro
 * Company:YawooAI
 */
public interface I {
    interface DB_DEMO {
        String DB_NAME = "OPXpad_demo.db";                                       // sqlite数据库 名字段
        int DATABASE_VERSION=1;                                                  // 数据库版本
    }

    interface PUSH_DATA{
        String BASE_URL="http://223.85.203.239:8900/OPX/";
        String PUSH_RESULT="RainTestData!addRainTestData";
        String YLQD = "rainStrong";
        String FBL = "resolution";
        String LATLNG ="lnglat";
        String DATE = "time";
        String TIME ="duration";
        String ZSL ="injection";
        String WC ="deviation";
        String FDCS ="fightNum";
    }
}
