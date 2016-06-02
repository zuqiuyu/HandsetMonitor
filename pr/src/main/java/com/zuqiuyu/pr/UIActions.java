package com.zuqiuyu.pr;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.zuqiuyu.pr.bluetooth.BluetoothUtils;
import com.zuqiuyu.pr.data.DataParse;
import com.zuqiuyu.pr.utils.CONST;

import java.util.ArrayList;


public class UIActions 
{
	static public boolean isMainScreenShow = true;

	//事件的响应
	public static void OnClick(View view, BluetoothUtils mBluetoothUtils)
	{
		
		switch (view.getId()) {
			case R.id.rlSpO2:
						RLSpO2Show();	
					    break;
			case R.id.llBCR:
						RLConfigShow();
					    break;
			case R.id.ibBluetooth:
				        BluetoothSelectShow(mBluetoothUtils);
				        RLConfigShow();
				        break;
			case R.id.ibConfig:
		                CfgDetailShow();
		                RLConfigShow();
				        break;
			case R.id.ibRecord:
				        RecordShow();
				        RLConfigShow();
				        break;
			case R.id.btnResearch:
				        mBluetoothUtils.StartScanDevices();
				        break;
			case R.id.start:
						start();
						break;
			case R.id.finish:
						finish();
						break;

			default:
				        break;
		}
	}
	private static void start(){
		if (MainActivity.BTFLAG == true&&DataParse.STATE == CONST.NORMAL_STATE) {
			MainActivity.countList.add(MainActivity.count++);
			Log.e("tag", MainActivity.countList.get(MainActivity.count - 1) + "");
			Log.e("tag", "状态"+DataParse.STATE + "");
			CONST.FLAG = true;
			//Log.e("tag", "start");
		}
	}
	private static void finish(){
		CONST.FLAG = false;
		MainActivity.tvSpO2.setText("- -");
		MainActivity.tvPR.setText("- -");
		Log.e("tag", "finish");
	}
	//测试记录
	private static void RecordShow() {
		// TODO Auto-generated method stub
		if(!isMainScreenShow)
		{
			Intent intent = new Intent(MainActivity.mContext,RecordActivity.class);
			MainActivity.mContext.startActivity(intent);
		}
	}
	//设置
	private static void CfgDetailShow() {
		// TODO Auto-generated method stub
		if(!isMainScreenShow)
		{
			Intent intent = new Intent(MainActivity.mContext,SettingsActivity.class);  
            MainActivity.mContext.startActivity(intent);  
		}
	}
	//蓝牙搜索
	private static void BluetoothSelectShow(final BluetoothUtils mBluetoothUtils) {
		// TODO Auto-generated method stub
		
		if(!isMainScreenShow)
		{
			Animation animation = AnimationUtils.loadAnimation(MainActivity.mContext,R.anim.slide_left);
			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					mBluetoothUtils.StartScanDevices();
				}
			});
			
			
			MainActivity.llBTSelect.startAnimation(animation);
			MainActivity.llBTSelect.setVisibility(View.VISIBLE);
			MainActivity.llCfgDedail.setVisibility(View.GONE);
			MainActivity.llBT.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 2.5f));
		    MainActivity.llCFG.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f));
			
		}
	}

	public static void ShowMainScreen() {
		// TODO Auto-generated method stub
	    DividerShow();
		MainActivity.llsf.setVisibility(View.VISIBLE);
	    MainActivity.rlSpO2.setVisibility(View.VISIBLE);
	    MainActivity.llNTC.setVisibility(View.VISIBLE);
	    MainActivity.llBCR.setVisibility(View.VISIBLE);
	    MainActivity.llBT.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f));
	    MainActivity.llCFG.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f));
	    MainActivity.llBTSelect.setVisibility(View.GONE);
	    MainActivity.llCfgDedail.setVisibility(View.GONE);
		//可能改错了
		MainActivity.mSpO2WaveDraw.setVisibility(true);
	    
	    isMainScreenShow = true;
	}
	//设置、蓝牙、记录的特写
	private static void RLConfigShow() {
		// TODO Auto-generated method stub
		if(isMainScreenShow) 
		{
			DividerHide();
			MainActivity.llsf.setVisibility(View.GONE);
			MainActivity.rlSpO2.setVisibility(View.GONE);
			MainActivity.mSpO2WaveDraw.setVisibility(false);

			MainActivity.ibBluetooth.startAnimation(AnimationUtils.loadAnimation(MainActivity.mContext,R.anim.bt_in_translate_top));
			MainActivity.ibConfig.startAnimation(AnimationUtils.loadAnimation(MainActivity.mContext,R.anim.cfg_in_translate_top));
			MainActivity.ibRecord.startAnimation(AnimationUtils.loadAnimation(MainActivity.mContext,R.anim.rcd_in_translate_top));
			isMainScreenShow = false;
		}
	}


	//显示血氧
	private static void RLSpO2Show() {
		// TODO Auto-generated method stub
		if(!isMainScreenShow) return;
		
		DividerHide();
		//可能改错了
		MainActivity.llNTC.setVisibility(View.GONE);
	}


//显示分隔符
	private static void DividerShow() {
		// TODO Auto-generated method stub
		MainActivity.vwDivider0.setVisibility(View.VISIBLE);
	}
//隐藏分隔符
	private static void DividerHide() {
		// TODO Auto-generated method stub
		MainActivity.vwDivider0.setVisibility(View.GONE);
	}
//重新设计蓝牙（Adapter）适配器
	public class BTDevicesAdapter extends BaseAdapter {

		private ArrayList<BluetoothDevice> mBluetootDevices;
        private LayoutInflater mInflator;
        private Context mContext;

        public BTDevicesAdapter(Context context) {
            super();
            this.mContext = context;
            mBluetootDevices = new ArrayList<BluetoothDevice>();
            mInflator = ((Activity) mContext).getLayoutInflater();
        }
        
        public void addDevice(BluetoothDevice device)
        {
            if(!mBluetootDevices.contains(device))	
            {
            	mBluetootDevices.add(device);
            }
        }
        
        public void clear() {
        	mBluetootDevices.clear();
        }
        
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mBluetootDevices.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mBluetootDevices.get(position);
		}
		
		@Override
		public long getItemId(int i) {
			// TODO Auto-generated method stub
			return i;
		}
		
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			view = mInflator.inflate(R.layout.bt_device_item, null);
			TextView tvName     = (TextView) view.findViewById(R.id.device_name);
			TextView tvAddr     = (TextView) view.findViewById(R.id.device_address);
			TextView tvIsPaired = (TextView) view.findViewById(R.id.device_isPaired);
			ImageView ivBtImage = (ImageView) view.findViewById(R.id.device_image);
			
			String strName = mBluetootDevices.get(position).getName();
			String strAddr = mBluetootDevices.get(position).getAddress();
			Boolean isPaired = (mBluetootDevices.get(position).getBondState() == BluetoothDevice.BOND_BONDED) ? true:false;
			
			//设置搜索到的特定的蓝牙设备图标（“BerryMed”）
			if(strName != null && strName.equals(mContext.getResources().getString(R.string.berry_bluetooth_name)))
			{
				ivBtImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.berry_bt_image));
			}
			else
			{
				ivBtImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.unknown_bt_image));
			}
			//设定搜索到的蓝牙设备名字
			if (strName != null && strName.length() > 0)
			{
				tvName.setText(strName);
			}
			else
			{
			    tvName.setText(R.string.unknown_device);
			}
			
			tvAddr.setText(strAddr);
			
			if(isPaired)
			{
				tvIsPaired.setText(R.string.paired);
			}
			
			return view;
		}
	}

	public static void ResearchBtnHide() {
		// TODO Auto-generated method stub
		MainActivity.btnResearch.setVisibility(View.GONE);
		MainActivity.pbSearch.setVisibility(View.VISIBLE);
	}

	public static void ResearchBtnShow() {
		// TODO Auto-generated method stub
		MainActivity.pbSearch.setVisibility(View.GONE);
		MainActivity.btnResearch.setVisibility(View.VISIBLE);
	}
	//血氧饱和度
	public static void RefreshSpO2(int arg1) {
		// TODO Auto-generated method stub
		//正常数值
		if(arg1 != CONST.SPO2_INVALID_VALUE)
    	{
    		MainActivity.tvSpO2.setText(arg1+"");
    	}
		//非正常数值
    	else {
    		MainActivity.tvSpO2.setText(R.string.tvSpO2Invalid);
		}
	}
	//脉率
	public static void RefreshPR(int arg2) {
		// TODO Auto-generated method stub
		//正常数值
		if(arg2 != CONST.PR_INVALID_VALUE)
    	{
			MainActivity.tvPR.setText(arg2+"");
    	}
		//非正常数值
    	else {
    		MainActivity.tvPR.setText(R.string.tvPRInvalid);
		}
	}

		
	}



