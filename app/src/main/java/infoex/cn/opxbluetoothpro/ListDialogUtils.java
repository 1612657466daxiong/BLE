package infoex.cn.opxbluetoothpro;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;




/**
 * Author:Doraemon_xqw
 * Time:17.11.12
 * FileName:ListDialogUtils
 * Project:WAIproject
 * Package:infoex.cn.wai.utils
 * Company:YawooAI
 */
public class ListDialogUtils {
    private AlertDialog alertDialog;
    Context mContext;
    private Context dialogContext;
    private LayoutInflater dialogInflater;
    private ArrayAdapter<BaseBean> adapter;

    public ListDialogUtils(Context mContext) {
        this.mContext = mContext;
    }


    public  ListDialogUtils show(String title, ArrayList<BaseBean> beanArrayList){
        dialogContext = new ContextThemeWrapper(mContext,android.R.style.Theme_Light);
        dialogInflater = (LayoutInflater) dialogContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapter = new ArrayAdapter<BaseBean>(mContext,R.layout.item_list_dialog,beanArrayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null){
                    convertView = dialogInflater.inflate(
                            R.layout.item_list_dialog, parent, false);
                }
                TextView textView = (TextView) convertView.findViewById(R.id.tv_dialog_list);
                String name = this.getItem(position).getName();
                textView.setText(name);
                return convertView;
            }
        };
        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onItemClickListener.onItemClick(adapter.getItem(which));
            }
        };
         alertDialog = new AlertDialog.Builder(mContext).setTitle(title)
                .setSingleChoiceItems(adapter, 0, clickListener).create();
        alertDialog.show();
        return this;
    }

    public interface OnItemClickListener{
        void onItemClick(BaseBean baseBean);
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void dissmiss(){
        if (alertDialog!=null){
            alertDialog.dismiss();
        }
    }



}
