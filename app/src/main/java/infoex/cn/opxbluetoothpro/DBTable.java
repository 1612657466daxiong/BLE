package infoex.cn.opxbluetoothpro;



/**
 * Created by ibm on 2017/4/6.
 */
public interface DBTable {
    interface RESULT_TABLE_CREATE {
        String OPX_RESULT="CREATE TABLE "+
                ResultDao.RESULT_TABLE_NAME +"(" +
                ResultDao.RESULT_COLUMN_YLQD +" INTEGER," +
                ResultDao.RESULT_COLUMN_FBL + " DOUBLE, " +
                ResultDao.RESULT_COLUMN_STIME +" INTEGER,"+
                ResultDao.RESULT_COLUMN_ZSL +" DOUBLE," +
                ResultDao.RESULT_COLUMN_FDCS +" INTEGER, " +
                ResultDao.RESULT_COLUMN_WC +" DOUBLE, " +
                ResultDao.RESULT_COLUMN_LATLNG+ " TEXT, "+
                ResultDao.RESULT_COLUMN_DATE +" TEXT PRIMARY KEY);";
    }


}
