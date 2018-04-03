package infoex.cn.opxbluetoothpro;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by ibm on 2017/3/28.
 */
public class ResultDao {
    public static final String RESULT_TABLE_NAME ="t_opxpad_result";//表名
    public static final String RESULT_COLUMN_YLQD = "m_result_ylqd";//雨量强度
    public static final String RESULT_COLUMN_FBL ="m_result_fbl";//分辨率
    public static final String RESULT_COLUMN_STIME ="m_result_stime";//时长
    public static final String RESULT_COLUMN_ZSL ="m_result_zsl";//注水量
    public static final String RESULT_COLUMN_FDCS = "m_result_fdcs";//翻斗次数
    public static final String RESULT_COLUMN_WC = "m_result_wc";//误差
    public static final String RESULT_COLUMN_DATE = "m_result_date";//时间
    public static final String RESULT_COLUMN_LATLNG="m_result_latlng";//经纬度


    public ResultDao(Context context){
        DBManager.getInstant().onInit(context);
    }

    public boolean saveResult(Result user){
        return DBManager.getInstant().saveUser(user);
    }
    public Result getUser(int userid){
        return DBManager.getInstant().getUser(userid);
    }
//    public boolean updateUser(Result user){
//        return DBManager.getInstant().updateUser(user);
//    }
//
//    public boolean existUser(Result user){
//        return DBManager.getInstant().existResult(user);
//    }
    public boolean delete(String  date){
        return  DBManager.getInstant().deleteUser(date);
    }

    public ArrayList<Result> getAllResult(){
        return  DBManager.getInstant().getAllResult();
    }
}
