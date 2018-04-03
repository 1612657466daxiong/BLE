package infoex.cn.opxbluetoothpro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by ibm on 2017/3/28.
 */
public class DBManager {
    private static DBManager dbmgr=new DBManager();
    private DBhelper dbhelper;
    public void onInit(Context context){
        dbhelper=new DBhelper(context);
    }
    public static synchronized DBManager getInstant(){
        return dbmgr;
    }
    public synchronized void closeDB(){
        if (dbhelper!=null){
            dbhelper.closeDB();
        }
    }
    public synchronized boolean saveUser(Result user){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ResultDao.RESULT_COLUMN_YLQD,user.getYlqd());
        values.put(ResultDao.RESULT_COLUMN_FBL,user.getFbl());
        values.put(ResultDao.RESULT_COLUMN_STIME,user.getTime());
        values.put(ResultDao.RESULT_COLUMN_ZSL,user.getZsl());
        values.put(ResultDao.RESULT_COLUMN_FDCS,user.getFdcs());
        values.put(ResultDao.RESULT_COLUMN_WC,user.getWc());
        values.put(ResultDao.RESULT_COLUMN_DATE,user.getDate());

        if (db.isOpen()){

            Log.i("main","boolean"+(db.replace(ResultDao.RESULT_TABLE_NAME,null,values)!=-1));
            return db.replace(ResultDao.RESULT_TABLE_NAME,null,values)!=-1;
        }
        return false;
    }
    public synchronized Result getUser(int   userid){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql = "select * from "+ ResultDao.RESULT_TABLE_NAME +" where "+
                ResultDao.RESULT_COLUMN_YLQD +" = ?";
        Result user = null;
        Cursor cursor = db.rawQuery(sql,new String[]{userid+""});
        if (cursor.moveToNext()){
            user = new Result();
            user.setYlqd(cursor.getInt(cursor.getColumnIndex(ResultDao.RESULT_COLUMN_YLQD)));
            user.setFbl(cursor.getDouble(cursor.getColumnIndex(ResultDao.RESULT_COLUMN_FBL)));
            user.setTime(cursor.getInt(cursor.getColumnIndex(ResultDao.RESULT_COLUMN_STIME)));
            user.setZsl(cursor.getDouble(cursor.getColumnIndex(ResultDao.RESULT_COLUMN_ZSL)));
            user.setFdcs(cursor.getInt(cursor.getColumnIndex(ResultDao.RESULT_COLUMN_FDCS)));
            user.setWc(cursor.getDouble(cursor.getColumnIndex(ResultDao.RESULT_COLUMN_WC)));
            user.setDate(cursor.getString(cursor.getColumnIndex(ResultDao.RESULT_COLUMN_DATE)));

        }
        return user;
    }
//    public synchronized boolean existResult(Result user){
//        SQLiteDatabase db = dbhelper.getReadableDatabase();
//        String sql = "select * from "+ ResultDao.RESULT_TABLE_NAME +" where "+
//                ResultDao.RESULT_COLUMN_DATE +" = ?";
//        Cursor cursor = db.rawQuery(sql,new String[]{user.getDate()});
//        if (cursor.getCount()>0){
//            return updateUser(user);
//        }else {
//            return saveUser(user);
//        }
//    }
//    public synchronized boolean updateUser(Result user){
//        int reult = -1;
//        SQLiteDatabase db =dbhelper.getWritableDatabase();
//        String sql = ResultDao.RESULT_COLUMN_FBL +"=?";
//        ContentValues cv =new ContentValues();
//        cv.put(ResultDao.RESULT_COLUMN_FBL,user.getUserName());
//        cv.put(ResultDao.RESULT_COLUMN_STIME,user.getName());
//        cv.put(ResultDao.RESULT_COLUMN_ZSL,user.getEmail());
//        cv.put(ResultDao.RESULT_COLUMN_FDCS,user.getAddress());
//        cv.put(ResultDao.RESULT_COLUMN_WC,user.getTel());
//        cv.put(ResultDao.RESULT_COLUMN_DATE,user.getRoleId());
//
//        if (db.isOpen()){
//            reult=db.update(ResultDao.RESULT_TABLE_NAME,cv,sql,new String[]{user.getId()+""});
//        }
//        return reult!=-1;
//    }
    public synchronized boolean deleteUser(String date){
        int reult = -1;
        SQLiteDatabase db =dbhelper.getWritableDatabase();
        String sql = ResultDao.RESULT_COLUMN_DATE +"=?";
        if (db.isOpen()){
         reult = db.delete(ResultDao.RESULT_TABLE_NAME,sql,new String[]{date+""});
        }
        return reult>0;
    }

    public synchronized ArrayList<Result> getAllResult(){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql = "select * from "+ResultDao.RESULT_TABLE_NAME+" order by "+ResultDao.RESULT_COLUMN_STIME+" asc ";
        ArrayList<Result> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            Result result = new Result();
            result.setYlqd(cursor.getInt(cursor.getColumnIndex(ResultDao.RESULT_COLUMN_YLQD)));
            result.setFbl(cursor.getDouble(cursor.getColumnIndex(ResultDao.RESULT_COLUMN_FBL)));
            result.setTime(cursor.getInt(cursor.getColumnIndex(ResultDao.RESULT_COLUMN_STIME)));
            result.setZsl(cursor.getDouble(cursor.getColumnIndex(ResultDao.RESULT_COLUMN_ZSL)));
            result.setFdcs(cursor.getInt(cursor.getColumnIndex(ResultDao.RESULT_COLUMN_FDCS)));
            result.setWc(cursor.getDouble(cursor.getColumnIndex(ResultDao.RESULT_COLUMN_WC)));
            result.setDate(cursor.getString(cursor.getColumnIndex(ResultDao.RESULT_COLUMN_DATE)));
            list.add(result);
        }
        return list;
    }

}
