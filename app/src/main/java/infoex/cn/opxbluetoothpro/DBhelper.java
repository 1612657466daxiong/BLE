package infoex.cn.opxbluetoothpro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by ibm on 2017/3/28.
 */
public class DBhelper extends SQLiteOpenHelper {
    private static DBhelper instant;

    public DBhelper(Context context) {
        super(context, getCreateName(), null , getCreateVersion());
    }
    private static int getCreateVersion(){
        return I.DB_DEMO.DATABASE_VERSION;
    }
    private static String getCreateName() {
        return I.DB_DEMO.DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBTable.RESULT_TABLE_CREATE.OPX_RESULT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public static DBhelper getInstant(Context context){
        if (instant==null){
            instant=new DBhelper(context.getApplicationContext());
        }
        return instant;
    }

    public  void closeDB(){
        if (instant!=null){
            SQLiteDatabase db = instant.getWritableDatabase();
            db.close();
            instant=null;
        }
    }
}
