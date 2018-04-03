package infoex.cn.opxbluetoothpro;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.List;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BTService extends Service {
    private final static String TAG = BTService.class.getSimpleName();
    private static final String MY_SERVICEUUID = "0000ffec-0000-1000-8000-00805f9b34fb";
    private static final String MY_CHARUUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private static final String POINT_BLUETOOTH = "PPSOR-DEV-IF";
    public static final int SEARCH_END = 0x00;

    public static final int SEARCH_SUCCESS = 1;
    public static final int SEARCH_FAIL = 2;
    public static final int WRITE_DATE_BACK = 3;
    public static final int SEARCH_IS_NULL = 4;
    public static final int CONNECT_FAIL = 5;

    //    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner scanner;
    private BluetoothGatt bluetoothGatt;

    private static BTService instance;
    private Handler mHandler;
    private int SCAN_SECOND =10000;
    private Handler mCallHandler;


    public BTService() {
        if (instance==null){
            instance = this;
        }
    }

    public void setmCallHandler(Handler mCallHandler) {
        this.mCallHandler = mCallHandler;
    }

    public void setBluetoothAdapter(BluetoothAdapter mBluetoothAdapter) {
        this.mBluetoothAdapter = mBluetoothAdapter;
    }

    public void initBLE(){

        mHandler = new Handler();
        if (Build.VERSION.SDK_INT >21){
            mScanCallback= new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType,result);
                    BluetoothDevice device =result.getDevice();
                    if (device != null){
                        Log.e("onsearch","SDK_INT > 21"+device.getName());
                        //过滤掉其他设备
                        if (device.getName() != null){
                            if (null !=device.getName() && device.getName().equals(POINT_BLUETOOTH)){
                                scanBLE(false);
                                bluetoothGatt = device.connectGatt(getApplication(), true, mGattCallback);
                            }
                        }
                    }
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    super.onBatchScanResults(results);

                    Log.e(TAG,"onBatchScanResults");
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                    Log.e(TAG,"onScanFailed");
                }
            };
        }else {
            mLeScanCallback  = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice bluetoothDevice, int i,byte[] bytes) {
                    if (bluetoothDevice != null){
                        //过滤掉其他设备
                        if (bluetoothDevice.getName()!= null){
                            Log.e("onsearch","onLeScan"+bluetoothDevice.getName());

//                    bleAdapter.addItme(bluetoothDevice);
                            if (null!=bluetoothDevice.getName() && bluetoothDevice.getName().equals(POINT_BLUETOOTH)){
//                        if (mProgress!=null & mProgress.isShowing()){
//                            mProgress.dismiss();
//                        }
                                bluetoothGatt = bluetoothDevice.connectGatt(getApplication(), true, mGattCallback);
                                scanBLE(false);
                            }
                        }
                    }
                }

            };
        }
    }

    public static BTService getInstance(){
        if (instance==null){
            instance = new BTService();
            return instance;
        }else {
            return instance;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initBLE();
    }



    public void onDisconnect(){

        if (bluetoothGatt==null){
//            mCallHandler.sendEmptyMessage(SEARCH_IS_NULL);
            return;
        }
        bluetoothGatt.disconnect();
        bluetoothGatt.close();
        bluetoothGatt = null;
    }
    public void onConnect(){
        if (bluetoothGatt==null){
           // mCallHandler.sendEmptyMessage(SEARCH_IS_NULL);
            return;
        }

        BluetoothGattService service = bluetoothGatt.getService(UUID.fromString(MY_SERVICEUUID));
        BluetoothGattCharacteristic characteristic =null;
        if (service!=null){
            characteristic = service.getCharacteristic(UUID.fromString(MY_CHARUUID));
            bluetoothGatt.setCharacteristicNotification(characteristic,true);
            characteristic.setValue(new byte[]{0x7D,0x01,0x01,0x01});
            Log.e("写入",  bytesToHexString(new byte[]{0x7D,0x01,0x01,0x01,0x01}));
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            bluetoothGatt.writeCharacteristic(characteristic);
        }
    }

    public void starTest(){
        if (bluetoothGatt==null){
            mCallHandler.sendEmptyMessage(SEARCH_IS_NULL);
            return;
        }
        BluetoothGattService service = bluetoothGatt.getService(UUID.fromString(MY_SERVICEUUID));
        BluetoothGattCharacteristic characteristic =null;
        if (service!=null){
            characteristic = service.getCharacteristic(UUID.fromString(MY_CHARUUID));
            bluetoothGatt.setCharacteristicNotification(characteristic,true);
            characteristic.setValue(new byte[]{0x7D,0x01,0x10,0x10});
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            bluetoothGatt.writeCharacteristic(characteristic);
        }
    }

    public void stopTest(){
        if (bluetoothGatt==null){
            mCallHandler.sendEmptyMessage(SEARCH_IS_NULL);
            return;
        }
        BluetoothGattService service = bluetoothGatt.getService(UUID.fromString(MY_SERVICEUUID));
        BluetoothGattCharacteristic characteristic =null;
        if (service!=null){
            characteristic = service.getCharacteristic(UUID.fromString(MY_CHARUUID));
            bluetoothGatt.setCharacteristicNotification(characteristic,true);
            characteristic.setValue(new byte[]{0x7D,0x01,0x11,0x11});
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            bluetoothGatt.writeCharacteristic(characteristic);
        }
    }

    public void setNull(){
        if (bluetoothGatt==null){
            mCallHandler.sendEmptyMessage(SEARCH_IS_NULL);
            return;
        }
        BluetoothGattService service = bluetoothGatt.getService(UUID.fromString(MY_SERVICEUUID));
        BluetoothGattCharacteristic characteristic =null;
        if (service!=null){
            characteristic = service.getCharacteristic(UUID.fromString(MY_CHARUUID));
            bluetoothGatt.setCharacteristicNotification(characteristic,true);
            characteristic.setValue(new byte[]{0x7D,0x01,0x12,0x12});
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            bluetoothGatt.writeCharacteristic(characteristic);
        }
    }

    public void writeTo(byte[] val){
        if (bluetoothGatt==null ){
            mCallHandler.sendEmptyMessage(SEARCH_IS_NULL);
            return;
        }
        BluetoothGattService service = bluetoothGatt.getService(UUID.fromString(MY_SERVICEUUID));
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(MY_CHARUUID));
        bluetoothGatt.setCharacteristicNotification(characteristic,true);
        characteristic.setValue(val);
        Log.e("APP回应",  bytesToHexString(val));
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        bluetoothGatt.writeCharacteristic(characteristic);

    }
    public void scanBLE(boolean enable){
        if (Build.VERSION.SDK_INT < 21){
            Log.e("onsearch","SDK_INT < 21");
            if (enable && mLeScanCallback!=null){
//                Log.e("onsearch","SDK_INT < 21 true");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        mScaning = false;
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                        if (mProgress!=null&& mProgress.isShowing()){
//                            mProgress.dismiss();
//                            // makedialog("搜索结束，未发现设备！","确定");
//                        }
                        mCallHandler.sendEmptyMessage(SEARCH_END);
                    }
                },SCAN_SECOND);
//                mScaning = true;
//                Log.e("onsearch","SDK_INT < 21 startLeScan");
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else if ( mLeScanCallback!=null){
//                mScaning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        } else {
            if (enable && mScanCallback!=null){

//                Log.e("onsearch","SDK_INT > 21");
                scanner = mBluetoothAdapter.getBluetoothLeScanner();
                scanner.startScan(mScanCallback);
//                Log.e("onsearch","SDK_INT > 21 start");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scanner.stopScan(mScanCallback);
//                        if (mProgress!=null&& mProgress.isShowing()){
//                            mProgress.dismiss();
//                            // makedialog("搜索结束，未发现设备！","确定");
//                        }
                        mCallHandler.sendEmptyMessage(SEARCH_END);
                    }
                },SCAN_SECOND);
            }else if (mScanCallback!=null){
                scanner = mBluetoothAdapter.getBluetoothLeScanner();
                scanner.stopScan(mScanCallback);
            }
        }
    }

    //sacn扫描回调 5.0以上用

    private ScanCallback mScanCallback;

    //4.3以上
    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        //连接状态改变的回调
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // 连接成功后启动服务发现
                Log.e("AAAAAAAA", "启动服务发现:" + gatt.discoverServices());
            }
        }

        //发现服务的回调
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
//                Log.e(TAG, "成功发现服务");
                BluetoothGattService service = gatt.getService(UUID.fromString(MY_SERVICEUUID));
                Log.e(TAG, "成功发现服务"+service.getUuid().toString());
//
                onConnect();
                mCallHandler.sendEmptyMessage(SEARCH_SUCCESS);
            }else{

                mCallHandler.sendEmptyMessage(SEARCH_FAIL);
                Log.e(TAG, "服务发现失败，错误码为:" + status);
            }
        }

        //写操作的回调
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
//                Log.e(TAG, "写入成功返回" +bytesToHexString(characteristic.getValue()));
            }else {
                Log.e(TAG, "写入失败" +characteristic.getValue());
                mCallHandler.sendEmptyMessage(CONNECT_FAIL);
            }
        }

        //读操作的回调
        public void onCharacteristicRead(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS ) {
                Log.e(TAG, "读取成功" +characteristic.getValue());

                byte[] value = characteristic.getValue();
                String string = bytesToHexString(value);
                Message message = new Message();
                message.what = WRITE_DATE_BACK;
                message.obj = value;
                mCallHandler.sendMessage(message);
//                Log.e(TAG,"返回数据"+string);
            }
        }

        //数据返回的回调（此处接收BLE设备返回数据）
        public void onCharacteristicChanged(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic) {
            if(characteristic.getValue()!=null){

                byte[] value = characteristic.getValue();
                String string = bytesToHexString(value);
                Message message = new Message();
                message.what = WRITE_DATE_BACK;
                message.obj = value;
                mCallHandler.sendMessage(message);
               Log.e(TAG,"返回数据"+string);
            }
        }
    };


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


}
