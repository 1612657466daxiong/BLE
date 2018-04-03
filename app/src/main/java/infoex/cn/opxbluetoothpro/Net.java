package infoex.cn.opxbluetoothpro;

import android.content.Context;
import android.util.Log;

/**
 * Author:Doraemon_xqw
 * Time:18.3.16
 * FileName:Net
 * Project:OpxblueToothPro
 * Package:infoex.cn.opxbluetoothpro
 * Company:YawooAI
 */
public class Net {
    public static void pushResult(Context context, Result result, OkHttpUtils.OnCompleteListener<Status> listener){
        OkHttpUtils<Status> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.url(I.PUSH_DATA.BASE_URL+I.PUSH_DATA.PUSH_RESULT)
                .addFormParam(I.PUSH_DATA.DATE,result.getDate())
                .addFormParam(I.PUSH_DATA.LATLNG,result.getLatlng()==null?"0,0":result.getLatlng())
                .addFormParam(I.PUSH_DATA.YLQD,result.getYlqd()+"")
                .addFormParam(I.PUSH_DATA.FBL,result.getFbl()+"")
                .addFormParam(I.PUSH_DATA.TIME,result.getTime()+"")
                .addFormParam(I.PUSH_DATA.ZSL,result.getZsl()+"")
                .addFormParam(I.PUSH_DATA.FDCS,result.getFdcs()+"")
                .addFormParam(I.PUSH_DATA.WC,result.getWc()+"")
                .targetClass(Status.class)
                .post()
                .execute(listener);
    }
}
