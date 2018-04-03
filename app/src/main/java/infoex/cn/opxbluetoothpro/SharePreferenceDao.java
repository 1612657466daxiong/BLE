package infoex.cn.opxbluetoothpro;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/10/24.
 */
public class SharePreferenceDao {
    private static final String SHARE_NEME="saveTestInfo";
    private static SharePreferenceDao instance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor meditor;
    private static final String SHARE_KEY_YLQD ="share_key_ylqd";
    private static final String SHARE_KEY_FBL="share_key_fbl";
    private static final String SHARE_KEY_TIME="share_key_time";

    public SharePreferenceDao(Context context){
        mSharedPreferences =context.getSharedPreferences(SHARE_NEME, Context.MODE_APPEND);
        meditor=mSharedPreferences.edit();
    }

    public static SharePreferenceDao getInstance(Context context){
        if (instance==null){
            instance= new SharePreferenceDao(context);
        }
        return instance;
    }

    public void saveYLQD(int ylqd){
        meditor.putInt(SHARE_KEY_YLQD,ylqd);
        meditor.commit();
    }
    public void removeYLQD(){
        meditor.remove(SHARE_KEY_YLQD);
        meditor.commit();
    }
    public int getYQDL(){
        return mSharedPreferences.getInt(SHARE_KEY_YLQD,0);
    }

    public void saveFBL(float fbl){
        meditor.putFloat(SHARE_KEY_FBL,fbl);
        meditor.commit();
    }

    public void removeFBL(){
        meditor.remove(SHARE_KEY_FBL);
        meditor.commit();
    }
    public float getFBL(){
        return mSharedPreferences.getFloat(SHARE_KEY_FBL,0);
    }

    public void saveTime(int time){
        meditor.putInt(SHARE_KEY_TIME,time);
        meditor.commit();
    }
    public void removeTime(){
        meditor.remove(SHARE_KEY_TIME);
        meditor.commit();
    }
    public int getTime(){
        return mSharedPreferences.getInt(SHARE_KEY_TIME,0);
    }
}
