package com.zuqiuyu.spo2h;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zuqiuyu.spo2h.UIActions.BTDevicesAdapter;
import com.zuqiuyu.spo2h.bluetooth.BluetoothChatService;
import com.zuqiuyu.spo2h.bluetooth.BluetoothUtils;
import com.zuqiuyu.spo2h.utils.CONST;
import com.zuqiuyu.spo2h.utils.utils;
import com.zuqiuyu.spo2h.wave.Wave;
import com.zuqiuyu.spo2h.wave.WaveParse;

import java.util.ArrayList;


public class MainActivity extends Activity implements OnClickListener,OnItemClickListener{
	
	//UI
	public static View             vwDivider0;
	public static View             IsMainShow;
	public static RelativeLayout   rlSpO2;
	public static LinearLayout     llBCR;
	public static LinearLayout     llNTC;
	public static LinearLayout     llBT;
	public static LinearLayout     llBTSelect;
	public static LinearLayout     llCFG;
	public static LinearLayout     llCfgDedail;
	public static LinearLayout     mainll;
	public static LinearLayout     llsf;
	public static RelativeLayout     juzhong;
	public static ImageButton      ibBluetooth;
	public static ImageButton      ibRecord;
	public static ImageButton      ibConfig;
	public static ListView         lvBtDevices;
	public static Button           btnResearch,start,finish;
	public static ProgressBar      pbSearch;

	
	//bluetooth service
	public  static Context          mContext;
	public static  BluetoothUtils   mBluetoothUtils;
	private BTDevicesAdapter        mBTDevicesAdapter;
	private ProgressDialog          mBtConnectProgressDialog;
	

	public static TextView tvSpO2;
	public static TextView tvPR;
	
	//draw wave
	public static SurfaceView sfvSpO2Wave;
	public static Wave mSpO2WaveDraw;

	public static int count = 0;
	public static ArrayList<Integer> countList= new ArrayList<Integer>();

	public static boolean BTFLAG = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		//初始化蓝牙
		mBluetoothUtils = new BluetoothUtils(this,mMainHandler);
		//判断蓝牙是否打开
		mBluetoothUtils.Init();
		//监听广播
		mBluetoothUtils.RegisterRecevier();
		//初始化主界面
		UI_WidgetInit();

		WaveParse mSpO2WaveParas = new WaveParse();
		mSpO2WaveParas.bufferCounter = 1;
		mSpO2WaveParas.xStep = 4;
		mSpO2WaveDraw = new Wave(sfvSpO2Wave,mSpO2WaveParas);


		
	}
//再按一次退出程序
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			//判断是否主界面
			if(vwDivider0.getVisibility() == View.GONE)
			{
				UIActions.ShowMainScreen(); 
			}
			else
			{
				utils.ExitWithToat(MainActivity.this,mainll);
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MainActivity.mBluetoothUtils.UnRegisterRecevier();
		UIActions.isMainScreenShow = true;
		super.onDestroy();
	}


//初始化主界面
	private void UI_WidgetInit() {
		// TODO Auto-generated method stub
		vwDivider0  =                  findViewById(R.id.vwDevider0);
		IsMainShow  = vwDivider0;
		rlSpO2      = (RelativeLayout) findViewById(R.id.rlSpO2);
	    llBCR       = (LinearLayout)   findViewById(R.id.llBCR);
	    llNTC       = (LinearLayout)   findViewById(R.id.llNTC);
	    llBT        = (LinearLayout)   findViewById(R.id.llBT);
	    llBTSelect  = (LinearLayout)   findViewById(R.id.llBTSelect);
	    llCFG       = (LinearLayout)   findViewById(R.id.llCFG);
	    llCfgDedail = (LinearLayout)   findViewById(R.id.llConfigDedail);
		juzhong     = (RelativeLayout) findViewById(R.id.juzhong);
	    ibBluetooth = (ImageButton)    findViewById(R.id.ibBluetooth);
	    ibRecord    = (ImageButton)    findViewById(R.id.ibRecord);
	    ibConfig    = (ImageButton)    findViewById(R.id.ibConfig);
	    lvBtDevices = (ListView)       findViewById(R.id.lvBtDevices);
	    btnResearch = (Button)         findViewById(R.id.btnResearch);
	    pbSearch    = (ProgressBar)    findViewById(R.id.pbSearch);
	    tvSpO2      = (TextView)       findViewById(R.id.tvSpO2);
	    tvPR        = (TextView)       findViewById(R.id.tvPR);
	    sfvSpO2Wave = (SurfaceView)    findViewById(R.id.sfvSpO2Wave);
		llsf = (LinearLayout)findViewById(R.id.llsf);
		start = (Button)findViewById(R.id.start);
		finish = (Button)findViewById(R.id.finish);
	    
	    mContext    =  MainActivity.this;
	    
	    rlSpO2.setOnClickListener(this);
	    llBCR.setOnClickListener(this);
	    ibBluetooth.setOnClickListener(this);
	    ibRecord.setOnClickListener(this);
	    ibConfig.setOnClickListener(this);
	    btnResearch.setOnClickListener(this);
		start.setOnClickListener(this);
		finish.setOnClickListener(this);
	    //设置蓝牙Adapter
	    mBTDevicesAdapter =  new UIActions().new BTDevicesAdapter(this);
	    lvBtDevices.setAdapter(mBTDevicesAdapter);
	    lvBtDevices.setOnItemClickListener(this);


	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		UIActions.OnClick(view,mBluetoothUtils);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		
		BluetoothDevice device = (BluetoothDevice) mBTDevicesAdapter.getItem(position);
		if(device != null)
		{
			mBluetoothUtils.BluetoothServiceInit(MainActivity.this, mMainHandler, device);
		}
	}

	//接收到handler主界面所做出的反应
	@SuppressLint("HandlerLeak")
	private Handler mMainHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) 
			{
				//血氧参数
			    case CONST.MESSAGE_SPO2_PARAM:
			    	UIActions.RefreshSpO2(msg.arg1);//血氧饱和度
			    	UIActions.RefreshPR(msg.arg2);//脉率
			    	break;
				//开始搜索蓝牙设备
				case CONST.MESSAGE_BLUETOOTH_START_SCAN:
					mBTDevicesAdapter.clear();
					mBTDevicesAdapter.notifyDataSetChanged();
					UIActions.ResearchBtnHide();
					break;
				//停止搜索蓝牙设备
				case CONST.MESSAGE_BLUETOOTH_STOP_SCAN:
					UIActions.ResearchBtnShow();
					break;
				//实时更新搜索到的蓝牙设备
				case CONST.MESSAGE_BLUETOOTH_A_DEVICE:
					mBTDevicesAdapter.addDevice((BluetoothDevice)msg.obj);
					mBTDevicesAdapter.notifyDataSetChanged();
					break;
				//蓝牙连接状态改变
				case CONST.MESSAGE_BLUETOOTH_STATE_CHANGE:
					{
						switch(msg.arg1)
						{
							case BluetoothChatService.STATE_CONNECTING:
								mBtConnectProgressDialog = ProgressDialog.show(MainActivity.this, null, getResources().getString(R.string.wait_for_connect));
								UIActions.ShowMainScreen();
								break;
							case BluetoothChatService.STATE_CONNECTED:
								if(mBtConnectProgressDialog != null)
								{
									mBtConnectProgressDialog.dismiss();
									
								}
								break;
							default:
								break;
						}
						break;
					}
				//成功连接到蓝牙设备
				case CONST.MESSAGE_BLUETOOTH_DEVICE_NAME:
					BTFLAG = true;
					String strName = msg.getData().getString(CONST.DEVICE_NAME);
					Toast.makeText(MainActivity.this, getResources().getString(com.zuqiuyu.spo2h.R.string.connect_successfully)+" "+strName, Toast.LENGTH_SHORT).show();
					break;
				//蓝牙连接失败
				case CONST.MESSAGE_BLUETOOTH_CONNECT_FAIL:
					BTFLAG = false;
					if(mBtConnectProgressDialog != null)
					{
						mBtConnectProgressDialog.dismiss();
					}
					break;
				//TOAST
				case CONST.MESSAGE_BLUETOOTH_TOAST:
					String strToast = msg.getData().getString(CONST.TOAST);
					Toast.makeText(MainActivity.this, strToast, Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}
	};

}
