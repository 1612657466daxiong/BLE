package infoex.cn.opxbluetoothpro;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_ENABLE_BT = 0xee;
    private static final int GPS_REQUEST_CODE = 0x11;

    private Context mContext;
    private ImageView ivSet;
    private BluetoothAdapter mBluetoothAdapter;

    BTService btService;
    private ProgressDialog mProgress;
    private Myhandler mCallhandler;
    private LinearLayout mLinTest,mLiLayoutResult;
    private RecyclerView mTestRcy;
    private TestAdapter mAdapter;
    private LinearLayoutManager mLL;
    private LinearLayout mLTest, mLConnect;

    private ArrayList<TestInTime> mList;
    private TextView mTvRainfallDensity, mTvResovingPower, mTvHostBatteryLevel, mTvHostVoltage,
            mTvCommunicationState, mTvNumber, mTvTipBatteryLevel, mTvTipVoltage;
    private TextView mTvYLQD,mTvMFBL,mTvMDUR,mTvMZSL,mTvMFDCS,mTvMWC;

    private ArrayList<BaseBean> arrayListYLQD, arrayListFBL;
    ArrayAdapter<BaseBean> adpter1, adapter2;
    private BaseBean currentYLQD, currentFBL;
    Dialog dialog;
    private RadioButton mbtnClear;
    private ResultDao resultDao;
    private LocationUtils instance;
    private Location location;
    private String latitude,longtitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mContext = this;
        mCallhandler = new Myhandler();
        resultDao = new ResultDao(mContext);
//        byte[] i = new byte[]{0x00,(byte)0xA3};
//        byte hight = 0x00;
//        int low =  i[1];
//        int time = ;
//        Log.e("ceshishuju",time+"");
        instance = LocationUtils.getInstance(MainActivity.this);
        initData();
        initView();
        initListener();
        checkBLE();
        pushData();
        checkLoc();


    }



    private void checkLoc() {
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};

        if (Build.VERSION.SDK_INT >= 23) {
            //如果超过6.0才需要动态权限，否则不需要动态权限
            //如果同时申请多个权限，可以for循环遍历
            int check = ContextCompat.checkSelfPermission(this,permissions[0]);
            int check2 = ContextCompat.checkSelfPermission(this,permissions[1]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED && check2 == PackageManager.PERMISSION_GRANTED) {
                //写入你需要权限才能使用的方法
               if (!checkGPSIsOpen()){
                   openGPSSettings();
               }
            } else {
                //手动去请求用户打开权限(可以在数组中添加多个权限) 1 为请求码 一般设置为final静态变量
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            //写入你需要权限才能使用的方法
//            if (!checkGPSIsOpen()){
//                openGPSSettings();
//            }
        }

    }

    private boolean checkGPSIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    /**
     * 跳转GPS设置
     */
    private void openGPSSettings() {
        if (checkGPSIsOpen()) {
            //initLocation(); //自己写的定位方法
        } else {
            //没有打开则弹出对话框
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("当前应用缺少必要权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限。")
                    // 拒绝, 退出应用
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    .setPositiveButton("设置",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转GPS设置界面
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(intent, GPS_REQUEST_CODE);
                                }
                            })
                    .setCancelable(false)
                    .show();

        }
    }

    private void pushData() {
        ArrayList<Result> allResult = resultDao.getAllResult();
        if (allResult.size() > 0) {
            for (int i = 0; i < allResult.size(); i++) {
                Log.e("pushresult",allResult.get(i).toString());
                pushResult(allResult.get(i));
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //回调，判断用户到底点击是还是否。
        //如果同时申请多个权限，可以for循环遍历
        if (requestCode == 1 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //写入你需要权限才能使用的方法
            if (!checkGPSIsOpen()){
                openGPSSettings();
            }
        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this,"需要获得位置权限",Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void pushResult(final Result result) {

            Net.pushResult(this, result, new OkHttpUtils.OnCompleteListener<Status>() {
                @Override
                public void onSuccess(Status str) {
                    if (str!=null ){
                        int code = str.getCode();
                        if (code>0){
                            Log.e("push code",code+"");
                            resultDao.delete(result.getDate());
                        }
                    }
                }

                @Override
                public void onError(String error) {

                }
            });

    }

    private void initData() {
        arrayListYLQD = new ArrayList<>();
        BaseBean ylqd1 = new BaseBean((byte) 0x01, "1mm/min");
        BaseBean ylqd2 = new BaseBean((byte) 0x02, "2mm/min");
        BaseBean ylqd3 = new BaseBean((byte) 0x03, "3mm/min");
        BaseBean ylqd4 = new BaseBean((byte) 0x04, "4mm/min");
        arrayListYLQD.add(ylqd1);
        arrayListYLQD.add(ylqd2);
        arrayListYLQD.add(ylqd3);
        arrayListYLQD.add(ylqd4);

        arrayListFBL = new ArrayList<>();
        BaseBean fbl1 = new BaseBean((byte) 0x01, "0.1mm");
        BaseBean fbl2 = new BaseBean((byte) 0x02, "0.2mm");
        BaseBean fbl3 = new BaseBean((byte) 0x05, "0.5mm");
        BaseBean fbl4 = new BaseBean((byte) 0x0A, "1.0mm");
        arrayListFBL.add(fbl1);
        arrayListFBL.add(fbl2);
        arrayListFBL.add(fbl3);
        arrayListFBL.add(fbl4);
        adpter1 = new ArrayAdapter<BaseBean>(mContext, android.R.layout.simple_list_item_1, arrayListYLQD);
        adapter2 = new ArrayAdapter<BaseBean>(mContext, android.R.layout.simple_list_item_1, arrayListFBL);
    }

    private void initListener() {
        ivSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View v = inflater.inflate(R.layout.spinner_dialog, null);
                Spinner sylqd = (Spinner) v.findViewById(R.id.ylqd_sp);
                Spinner sfbl = (Spinner) v.findViewById(R.id.fbl_sp);
                Button commit = (Button) v.findViewById(R.id.btn_chose_commit);
                Button quit = (Button) v.findViewById(R.id.btn_chose_quit);

                dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
                dialog.getWindow().setContentView(v);
                sylqd.setAdapter(adpter1);
                sylqd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        currentYLQD = arrayListYLQD.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                sfbl.setAdapter(adapter2);
                sfbl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        currentFBL = arrayListFBL.get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currentFBL == null || currentYLQD == null) {
                            Toast.makeText(MainActivity.this, "参数不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            byte ylqd = currentYLQD.getId();
                            byte fbl = currentFBL.getId();
                            byte[] bytes = new byte[]{0x7D, 0x03, 0x20, ylqd, fbl, (byte) (0x20 + ylqd + fbl)};

                            btService.writeTo(bytes);
                            dialog.dismiss();
                        }
                    }
                });
                quit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }


    private void checkBLE() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(MainActivity.this, "不支持蓝牙程序", Toast.LENGTH_SHORT).show();
            finish();
        }
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            //不支持
            Toast.makeText(this, "不支持蓝牙程序", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else if (!mBluetoothAdapter.isEnabled()) {//判断是否已经打开
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        btService = BTService.getInstance();
        btService.setBluetoothAdapter(mBluetoothAdapter);
        btService.initBLE();
        btService.setmCallHandler(mCallhandler);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
        OkHttpUtils.release();
        instance.removeLocationUpdatesListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (instance==null){
            instance = LocationUtils.getInstance(this);
        }
        location = instance.showLocation();

    }

    private void initView() {
        mLiLayoutResult = (LinearLayout) findViewById(R.id.layout_result);
        mbtnClear = (RadioButton) findViewById(R.id.clear);
        mbtnClear.setVisibility(View.GONE);
        mTvYLQD = (TextView) findViewById(R.id.ylqd_mian);
        mTvMFBL = (TextView) findViewById(R.id.fbl_mian);
        mTvMDUR = (TextView) findViewById(R.id.dangqianyongshi_mian);
        mTvMFDCS = (TextView) findViewById(R.id.fandoucishu_mian);
        mTvMWC = (TextView) findViewById(R.id.wuliangwucha_mian);
        mTvMZSL = (TextView) findViewById(R.id.zhushuiliang_mian);
        mLTest = (LinearLayout) findViewById(R.id.layout_test_data);
        mLConnect = (LinearLayout) findViewById(R.id.layout_connect);
        mTestRcy = (RecyclerView) findViewById(R.id.recy_list);
        ivSet = (ImageView) findViewById(R.id.iv_de_set);

        mLinTest = (LinearLayout) findViewById(R.id.layout_test);
        mLinTest.setVisibility(View.GONE);
        mLTest.setVisibility(View.INVISIBLE);
        mTvRainfallDensity = (TextView) findViewById(R.id.person_name_rainfall_density);
        mTvResovingPower = (TextView) findViewById(R.id.person_name_resolving_power);
        mTvHostBatteryLevel = (TextView) findViewById(R.id.person_host_battery_level);
        mTvHostVoltage = (TextView) findViewById(R.id.person_name_host_voltage);
        mTvCommunicationState = (TextView) findViewById(R.id.person_name_communication_state);
        mTvNumber = (TextView) findViewById(R.id.person_number);
        mTvTipBatteryLevel = (TextView) findViewById(R.id.person_tipping_battery_level);
        mTvTipVoltage = (TextView) findViewById(R.id.person_name_tipping_voltage);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("搜索中...");
        mList = new ArrayList<>();
        mAdapter = new TestAdapter(this, mList);
        mLL = new LinearLayoutManager(this);
        mLL.setOrientation(LinearLayoutManager.VERTICAL);
        mTestRcy.setAdapter(mAdapter);
        mTestRcy.setLayoutManager(mLL);
    }

    private void DealByteValue(byte[] val) {
        if (val.length < 4) {
            return;
        }
        ControlBLE controlBLE = new ControlBLE(val);
        if (controlBLE.getHead() == 0x7D) {
            switch (controlBLE.getCommand()) {
                case 0x01://连接成功
                    Log.e("返回连接结果", bytesToHexString(val) + "");
                    if (mProgress!=null&& mProgress.isShowing()){
                        mProgress.dismiss();
                        // makedialog("搜索结束，未发现设备！","确定");
                        // makedialog("匹配成功请进行连接！","确定",mContext);
                    }
                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    break;
                case 0x0A://返回设备信息
                    Log.e("返回设备信息", bytesToHexString(val) + "");
                        boolean b = checkOutDeivce(controlBLE);
                        writeBackDevice(b);
                    break;
                case 0x10://开始命令
                    Log.e("返回开始命令", bytesToHexString(val) + "");
                    checkStar(controlBLE);
                    break;
                case 0x11://停止命令
                    Log.e("返回停止命令", bytesToHexString(val) + "");
                    checkStop(controlBLE);
                    break;
                case 0x12://放空命令
                    Log.e("返回放空命令", bytesToHexString(val) + "");
                    checkSetNull(controlBLE);
                    break;
                case 0x20://设置参数命令
                    Log.e("返回参数设置", bytesToHexString(val) + "");
                    checkSet(controlBLE);
                    break;
                case 0x30://设备主动上传测试状态命令
                    Log.e("返回测试信息", bytesToHexString(val) + "");
                    checkTestData(controlBLE);
                    break;
                case 0x40://设备主动上传测试结果命令
                    Log.e("返回测试结果", bytesToHexString(val) + "");
                    checkReslut(controlBLE);
                    break;
            }
        }
    }

    private void checkReslut(ControlBLE controlBLE) {
        if (controlBLE.getCommand() == 0x40) {
            byte info = 0x00;
            for (int i = 0; i < controlBLE.getInfo().length; i++) {
                info += controlBLE.getInfo()[i];
            }
            if ((byte) (controlBLE.getCommand() + info) == controlBLE.getCheck()) {
                byte[] info1 = controlBLE.getInfo();
                int ylqd = info1[0];
                double fbl;
                switch (info1[1]) {
                    case 0x01:
                        fbl = 0.1;
                        break;
                    case 0x02:
                        fbl = 0.2;
                        break;
                    case 0x05:
                        fbl = 0.5;
                        break;
                    case 0x0A:
                        fbl = 1.0;
                        break;
                    default:
                        fbl = 0.1;
                }
                byte hight = info1[2];
                byte low = info1[3];
                int time = (((hight<<8)&0x0000ff00)|(low&0x000000ff));
                double zsl = (((double)(info1[4]&0x000000ff))/10);
                int fdcs = info1[5]&0x000000ff;
                boolean zfwc = (info1[6]&0x80)==0?true:false;
                double wc =((double)(((info1[6]<<8)&0x00007f00)|((info1[7])&0x000000ff)))/10;
//                double wc =  (((double)((info1[7])&0x000000ff)) / 10);
                double wucha = zfwc ? wc : 0 - wc;
                String string = "此次测试结果：\n" +
                            "  当前雨量强度为" + ylqd + "mm/min \n" +
                           "  当前雨量筒分辨率为" + fbl + "mm \n" +
                        "  测试所用时长为" + time + "s \n" +
                        "  实际注水量为" + zsl + "mm \n" +
                        "  翻斗次数为" + fdcs + " \n" +
                        "  雨量误差为" + wucha + "%";
                int second = time%60;
                int min = time/60;
                if (mLiLayoutResult.getVisibility() ==View.GONE){
                    mLiLayoutResult.setVisibility(View.VISIBLE);
                    mTvYLQD.setText(ylqd+"mm/min");
                    mTvMFBL.setText(fbl + "mm");
                    mTvMDUR.setText(min+"分"+second + "秒");
                    mTvMZSL.setText(zsl + "mm");
                    mTvMFDCS.setText(fdcs +"次");
                    mTvMWC.setText(wucha + "%");
                }
                Calendar c = Calendar.getInstance();
                long timeInMillis = c.getTimeInMillis();
                Result result = new Result(ylqd, fbl, time, zsl, fdcs, wc, timeInMillis + "", "");
                Log.e("测试结果保存",result.toString());
                getLocaction(result);

                btService.writeTo(new byte[]{0x7D, 0x02, 0x40, 0x00, 0x40});
            } else {

                btService.writeTo(new byte[]{0x7D, 0x02, 0x40, 0x01, 0x41});
            }
        } else {

            btService.writeTo(new byte[]{0x7D, 0x02, 0x40, 0x01, 0x41});
        }
    }

    private void getLocaction(Result result) {
        location =instance.showLocation();
        Log.e( "获取经纬度", location!=null?location.getLatitude()+"":"location null" );
        if (location != null) {
            String address =  location.getLongitude()+","+location.getLatitude();
            Log.e( "获取经纬度", address );
            result.setLatlng(address);
            boolean b = resultDao.saveResult(result);
            Log.e("保存测试结果",b?"成功":"失败");
        }else {
            String address =  "";
            Log.e( "获取经纬度", address+"null" );
            result.setLatlng(address);
            boolean b = resultDao.saveResult(result);
            Log.e("保存测试结果 空经纬度",b?"成功":"失败");
        }

    }


    private void checkTestData(ControlBLE controlBLE) {
        if (controlBLE.getCommand() == 0x30  ){
            byte info=0x00;
            for (int i =0;i<controlBLE.getInfo().length;i++){
                info+=controlBLE.getInfo()[i];
            }
            if ((byte)(controlBLE.getCommand()+ info) == controlBLE.getCheck()){
                byte[] info1 = controlBLE.getInfo();
                double zsl = (((double)(info1[0]&0x000000ff))/10);
                int fdcs = info1[1]&0x000000ff;
                boolean zfwc = (info1[2]&0x80)==0?true:false;
                double wc =((double)(((info1[2]<<8)&0x00007f00)|((info1[3])&0x000000ff)))/10;
                double wucha = zfwc?wc:0-wc;
                byte hight = info1[4];
                byte low = info1[5];
                int time = (((hight<<8)&0x0000ff00)|(low&0x000000ff));
                int ylqd = info1[6];
                double fbl;
                switch (info1[7]){
                    case 0x01:
                        fbl = 0.1;
                        break;
                    case 0x02:
                        fbl = 0.2;
                        break;
                    case 0x05:
                        fbl = 0.5;
                        break;
                    case 0x0A:
                        fbl = 1.0;
                        break;
                    default:
                        fbl = 0.1;
                }
                byte hightdur = info1[8];
                byte lowdur = info1[9];
                int dur = (((hightdur<<8)&0x0000ff00)|(lowdur&0x000000ff));
                Calendar c = Calendar.getInstance();
                String date = c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DATE)+" "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                mAdapter.addItme(new TestInTime(date,zsl,fdcs,wucha,time,false,"",ylqd,fbl,dur));

                btService.writeTo(new byte[]{0x7D,0x02,0x30,0x00,0x30});
            }else {
               // Toast.makeText(MainActivity.this, "放空命令失败", Toast.LENGTH_SHORT).show();
                btService.writeTo(new byte[]{0x7D,0x02,0x30,0x01,0x31});
            }
        }else {
            //Toast.makeText(MainActivity.this, "放空命令失败", Toast.LENGTH_SHORT).show();
            btService.writeTo(new byte[]{0x7D,0x02,0x30,0x01,0x31});
        }
    }

    private void checkSetNull(ControlBLE controlBLE) {
        if (controlBLE.getCommand() == 0x12 && controlBLE.getInfo()[0] ==0x00 ){
            byte info=0x00;
            for (int i =0;i<controlBLE.getInfo().length;i++){
                info+=controlBLE.getInfo()[i];
            }
            if ((byte)(controlBLE.getCommand()+ info) == controlBLE.getCheck()){
                Toast.makeText(MainActivity.this, "放空", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this, "放空命令失败", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(MainActivity.this, "放空命令失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkStop(ControlBLE controlBLE) {
        if (controlBLE.getCommand() == 0x11 && controlBLE.getInfo()[0] ==0x00 ){
            byte info=0x00;
            for (int i =0;i<controlBLE.getInfo().length;i++){
                info+=controlBLE.getInfo()[i];
            }
            if ((byte)(controlBLE.getCommand()+ info) == controlBLE.getCheck()){

            }else {
                Toast.makeText(MainActivity.this, "停止命令失败", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(MainActivity.this, "停止命令失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkStar(ControlBLE controlBLE) {
        if (controlBLE.getCommand() == 0x10 && controlBLE.getInfo()[0] ==0x00 ){
            byte info=0x00;
            for (int i =0;i<controlBLE.getInfo().length;i++){
                info+=controlBLE.getInfo()[i];
            }
            if ((byte)(controlBLE.getCommand()+ info) == controlBLE.getCheck()){
                byte[] info1 = controlBLE.getInfo();
                int ylqd = info1[0];
                double fbl;
                switch (info1[1]){
                    case 0x01:
                        fbl = 0.1;
                        break;
                    case 0x02:
                        fbl = 0.2;
                        break;
                    case 0x05:
                        fbl = 0.5;
                        break;
                    case 0x0A:
                        fbl = 1.0;
                        break;
                    default:
                        fbl = 0.1;
                }
                byte hight = info1[2];
                byte low = info1[3];
                int time = (hight<<8)+ low;
                String control = "\n当前雨量强度："+ylqd+"mm/min, 当前雨量筒分辨率："+fbl+"mm\n"+"测试时长："+time+"s";
                if (mAdapter!=null){
                    mAdapter.addControl("开始"+control);
                }
            }else {
                Toast.makeText(MainActivity.this, "开始命令失败", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(MainActivity.this, "开始命令失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkSet(ControlBLE controlBLE) {
        if (controlBLE.getCommand() == 0x20 && controlBLE.getInfo()[0] ==0x00 ){
            byte info=0x00;
            for (int i =0;i<controlBLE.getInfo().length;i++){
                info+=controlBLE.getInfo()[i];
            }
            if ((byte)(controlBLE.getCommand()+ info) == controlBLE.getCheck()){
                Log.e("设置成功","-->");
                Toast.makeText(MainActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this, "设置失败,请重新设置", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(MainActivity.this, "设置失败,请重新设置", Toast.LENGTH_SHORT).show();
        }
    }


    private void writeBackDevice(boolean b) {
        byte[] back;
        if (b){
            back = new byte[]{0x7D,0x01,0x0A,0x00,0x0A};
        }else {
            back = new byte[]{0x7D,0x01,0x0A,0x01,0x0A};
        }
           btService.writeTo(back);
    }


    private boolean checkOutDeivce(ControlBLE controlBLE) {
        byte[] val = controlBLE.getInfo();

        int device1 =(int) val[0]&0x000000ff;
        double device2 = ((double) (val[1]&0x000000ff))/10;
        boolean device3 = (val[2])==0?false:true;
        int device4 = (int)val[3]&0x000000ff;
        double device5 = ((double)(val[4]&0x000000ff))/10;
        int device6 = (int)val[5]&0x000000ff;
        double device7;
        switch (val[6]){
            case 0x01:
                device7 = 0.1;
                break;
            case 0x02:
                device7 = 0.2;
                break;
            case 0x05:
                device7 = 0.5;
                break;
            case 0x0A:
                device7 = 1.0;
                break;
            default:
                device7 = 0.1;
        }
        int device8 = val[7]&0x000000ff;
        if (mLConnect.getVisibility() == View.VISIBLE){
        mTvTipBatteryLevel.setText(device1+"%");
        mTvTipVoltage.setText(device2+"V");
        mTvCommunicationState.setText(device3?"成功":"失败");
        mTvHostBatteryLevel.setText(device4+"%");
        mTvHostVoltage.setText(device5+"V");
        mTvRainfallDensity.setText(device6+"mm/min");
        mTvResovingPower.setText(device7+"mm");
        mTvNumber.setText(device8+"");
        }
        BaseBean currentYLQD = new BaseBean();
        currentYLQD.setId(val[5]);
        currentYLQD.setName(device6+" mm/min");
        BaseBean currentFBL = new BaseBean();
        currentFBL.setId(val[6]);
        currentFBL.setName(device7+" mm");
        if (controlBLE.getLengthB()!=val.length+1){
            return false;
        }else {
           if (val[0]>100 || val[0] <0 ||val[2]>1 || val[3] >100 || val[3]<0 ||ControlBLE.CheckControl(controlBLE)){
               return false;
           }else {
               return true;
           }
        }
    }


    public void on_testbegin(View view) {
        if (mLinTest.getVisibility() == View.GONE){
            mLinTest.setVisibility(View.VISIBLE);
        }else {
            mLinTest.setVisibility(View.GONE);
        }
        if (mLTest.getVisibility() == View.INVISIBLE){
            mLTest.setVisibility(View.VISIBLE);
            mLConnect.setVisibility(View.GONE);
        }else {
            mLTest.setVisibility(View.INVISIBLE);
            mLConnect.setVisibility(View.VISIBLE);
        }
        if (mbtnClear.getVisibility()==View.VISIBLE){
            mbtnClear.setVisibility(View.GONE);
        }else {
            mbtnClear.setVisibility(View.VISIBLE);
        }
    }

    public void on_search(View view) {
        if (mProgress!=null && !mProgress.isShowing()){
            mProgress.show();
        }
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(MainActivity.this, "不支持蓝牙程序", Toast.LENGTH_SHORT).show();
            finish();
        }
//        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            //不支持
            Toast.makeText(this, "不支持蓝牙程序", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else if (!mBluetoothAdapter.isEnabled()) {//判断是否已经打开
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }else if (mBluetoothAdapter.isEnabled()){
            btService.scanBLE(true);
        }
    }

    public void on_disconnect(View view) {

        btService.onDisconnect();
        if (mLConnect.getVisibility() == View.VISIBLE){
            mTvTipBatteryLevel.setText("---");
            mTvTipVoltage.setText("---");
            mTvCommunicationState.setText("---");
            mTvHostBatteryLevel.setText("---");
            mTvHostVoltage.setText("---");
            mTvRainfallDensity.setText("---");
            mTvResovingPower.setText("---");
            mTvNumber.setText("---");
        }
    }
    public void on_star(View view) {
        btService.starTest();

        mTvYLQD.setText("");
        mTvMWC.setText("");
        mTvMDUR.setText("");
        mTvMFDCS.setText("");
        mTvMFBL.setText("");
        mTvMZSL.setText("");
        if ( mLiLayoutResult.getVisibility() == View.VISIBLE){
            mLiLayoutResult.setVisibility(View.GONE);
        }

    }

    public void on_setnull(View view) {
        btService.setNull();
    }

    public void on_stop(View view) {
        btService.stopTest();
    }



    private void makedialog(final String textString, final String btntext, final Context mContext) {

                final View layout = View.inflate(mContext, R.layout.back_press, null);
                TextView text = (TextView) layout.findViewById(R.id.tv_dialog);
                text.setText(textString);
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final AlertDialog alertDialog = builder.create();
                Button mbtnpositive = (Button) layout.findViewById(R.id.btn_positive);
                alertDialog.setView(layout);
                mbtnpositive.setText(btntext);
                mbtnpositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
    }


    public static String bytesToHexString(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toHexString(bytes[i] & 0xFF);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            result += hexString.toUpperCase();
        }
        return result;
    }

    public void on_clear(View view) {
        if (mAdapter!=null){
            mAdapter.clear();
            mTvYLQD.setText("");
            mTvMWC.setText("");
            mTvMDUR.setText("");
            mTvMFDCS.setText("");
            mTvMFBL.setText("");
            mTvMZSL.setText("");
            if ( mLiLayoutResult.getVisibility() == View.VISIBLE){
                mLiLayoutResult.setVisibility(View.GONE);
            }
        }
    }


    class Myhandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case BTService.SEARCH_END:
                    if (mProgress!=null&& mProgress.isShowing()){
                            mProgress.dismiss();
                            // makedialog("搜索结束，未发现设备！","确定");
                        }
                    break;
                case BTService.SEARCH_IS_NULL:
                        makedialog("请先进行搜索匹配！","确定",mContext);
                    break;
                case BTService.WRITE_DATE_BACK:
                    byte[] obj = (byte[]) msg.obj;
                    if(obj[0] ==0x7D)
                    DealByteValue(obj);
                    break;
                case BTService.SEARCH_SUCCESS:
//                    if (mProgress!=null&& mProgress.isShowing()){
//                        mProgress.dismiss();
//                        // makedialog("搜索结束，未发现设备！","确定");
//                       // makedialog("匹配成功请进行连接！","确定",mContext);
//                    }

                    break;
                case BTService.SEARCH_FAIL:
                    if (mProgress!=null&& mProgress.isShowing()){
                        mProgress.dismiss();
                        // makedialog("搜索结束，未发现设备！","确定");
                    }
                    makedialog("匹配失败，请重新进行搜索！","确定",mContext);
                    break;
                case BTService.CONNECT_FAIL:
                    if (mProgress!=null&& mProgress.isShowing()){
                        mProgress.dismiss();
                        // makedialog("搜索结束，未发现设备！","确定");
                    }
                    makedialog("匹配失败，请重新进行搜索！","确定",mContext);
                    break;
            }
        }
    }


}
