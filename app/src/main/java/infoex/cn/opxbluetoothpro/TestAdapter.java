package infoex.cn.opxbluetoothpro;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Author:Doraemon_xqw
 * Time:18.3.15
 * FileName:TestAdapter
 * Project:OpxblueToothPro
 * Package:infoex.cn.opxbluetoothpro
 * Company:YawooAI
 */
public class TestAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<TestInTime> mlist;

    public TestAdapter(Context mContext, ArrayList<TestInTime> mlist) {
        this.mContext = mContext;
        this.mlist = mlist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = null;
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        view = View.inflate(mContext, R.layout.test_item, linearLayout);
        holder = new ItemVH(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TestInTime testInTime = mlist.get(position);
        ItemVH itemVH = (ItemVH) holder;
        if (testInTime.isControl){
            itemVH.mTvDate.setText(testInTime.getControlS());
            itemVH.mTvFDCS.setVisibility(View.INVISIBLE);
            itemVH.mTvWC.setVisibility(View.INVISIBLE);
            itemVH.mTvIntime.setVisibility(View.INVISIBLE);
            itemVH.mTvZSL.setVisibility(View.INVISIBLE);
            itemVH.mTvDur.setVisibility(View.INVISIBLE);
            itemVH.mTvYLQD.setVisibility(View.INVISIBLE);
            itemVH.mTvFbl.setVisibility(View.INVISIBLE);
        }else {
            itemVH.mTvDate.setVisibility(View.VISIBLE);
            itemVH.mTvFDCS.setVisibility(View.VISIBLE);
            itemVH.mTvWC.setVisibility(View.VISIBLE);
            itemVH.mTvIntime.setVisibility(View.VISIBLE);
            itemVH.mTvZSL.setVisibility(View.VISIBLE);
            itemVH.mTvDur.setVisibility(View.VISIBLE);
            itemVH.mTvYLQD.setVisibility(View.VISIBLE);
            itemVH.mTvFbl.setVisibility(View.VISIBLE);
            itemVH.mTvDate.setText(testInTime.getDate());
            itemVH.mTvZSL.setText( testInTime.getZSL()+"mm");
            itemVH.mTvFDCS.setText(testInTime.getFDCS()+"次");
            itemVH.mTvWC.setText(  testInTime.getWC()+"%");
            int minDur = testInTime.getDur()/60;
            int secDur = testInTime.getDur()%60;
            int minTime = testInTime.getTime()/60;
            int secTime = testInTime.getTime()%60;
            String secDurstr = secDur>9?secDur+"":"0"+secDur;
            String secTimestr = secTime>9?secTime+"":"0"+secTime;
            String minDurstr =  minDur>9?minDur+"":"0"+minDur;
            String minTimestr =  minTime>9?minTime+"":"0"+minTime;
            itemVH.mTvIntime.setText(  minTimestr+"分"+secTimestr+"秒");
            itemVH.mTvDur.setText( minDurstr+"分"+secDurstr+"秒");
            itemVH.mTvYLQD.setText(testInTime.getYlqd()+"mm/min");
            itemVH.mTvFbl.setText( testInTime.getFbl()+"mm");
        }

    }

    @Override
    public int getItemCount() {
        return mlist==null?0:mlist.size();
    }

    public  void initData(ArrayList<TestInTime> list){
        if (mlist!=null){
            mlist.clear();
        }
        mlist.addAll(list);
        notifyDataSetChanged();
    }
    public  void addData(ArrayList<TestInTime> list){
        mlist.addAll(list);
        notifyDataSetChanged();
    }

    public void addItme(TestInTime device){
        mlist.add(device);
        if (mlist.size()>1){
            mlist.remove(0);
        }

        notifyDataSetChanged();
    }


    public void addControl(String startContol){
        mlist.add(new TestInTime("",0,0,0,0,true,startContol,0,0,0));
        if (mlist.size()>1){
            mlist.remove(0);
        }

        notifyDataSetChanged();
//        String date, double ZSL, int FDCS, double WC, int time, boolean isControl, String controlS
    }
    public void clear(){
        mlist.clear();
        notifyDataSetChanged();
//        String date, double ZSL, int FDCS, double WC, int time, boolean isControl, String controlS
    }

    class ItemVH extends RecyclerView.ViewHolder{
        TextView mTvIntime,mTvZSL,mTvFDCS,mTvWC,mTvDate,mTvYLQD,mTvFbl,mTvDur;
        public ItemVH(View itemView) {
            super(itemView);
            mTvDate = (TextView) itemView.findViewById(R.id.test_time);
            mTvIntime = (TextView) itemView.findViewById(R.id.dangqianyongshi);
            mTvZSL = (TextView) itemView.findViewById(R.id.zhushuiliang);
            mTvWC = (TextView) itemView.findViewById(R.id.wuliangwucha);
            mTvFDCS = (TextView) itemView.findViewById(R.id.fandoucishu);
            mTvYLQD = (TextView) itemView.findViewById(R.id.ylqd);
            mTvFbl = (TextView) itemView.findViewById(R.id.fbl);
            mTvDur = (TextView) itemView.findViewById(R.id.dur);
        }
    }

    class ItemControl extends RecyclerView.ViewHolder{
        TextView mTvControl;

        public ItemControl(View itemView) {
            super(itemView);
            mTvControl = (TextView) itemView.findViewById(R.id.control_item);
        }
    }


}
