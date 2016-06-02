package com.zuqiuyu.spo2h.data;

import android.os.Handler;
import android.util.Log;

import com.zuqiuyu.spo2h.MainActivity;
import com.zuqiuyu.spo2h.utils.CONST;

//在手机上解包PM6750发送的数据 核心算法
public class DataParse implements Runnable 
{
    private final String         TAG                 = getClass().getName(); 
	private final static int     BUFFER_SIZE         = 1024;//1KB
	private final int[]   PACKAGE_HEAD        = new int[]{0x55,0xaa};//固定的包头参数
	private final int     PKG_NIBP            = 0x03;//血压数据
	private final int     PKG_SPO2            = 0x04;//血氧数据
	private final int     PKG_SW_VER          = 0xfc;//软件版本号
	private final int     PKG_HW_VER          = 0xfd;//硬件版本号
	private final int     PKG_SPO2_WAVE       = 0xfe;//血氧波形数据


	private int skipCounter = 0;//计数器
	private Handler mHandler;
	
	
	private byte[] recvData = new byte[BUFFER_SIZE];//收到的数据
	private int    emptyIndex = 0;//空索引
	private int    parseIndex = 0;//解包的索引
	public static byte STATE;
	
	//构造函数，传递handler
	public DataParse(Handler handler) 
	{
		// TODO Auto-generated constructor stub
		this.mHandler = handler;
	}
	long time = System.currentTimeMillis();//自1970年1月1日0时起到现在的毫秒数
	//
	public void Add(byte[] buf, int bufSize)
	{
		boolean pkgStart = false;
		int pkgIndex = 0; //包序列
		int pkgLength = 0;//包的总长度
		byte[] pkgData = null;//包数据

		
		if(bufSize+emptyIndex <= BUFFER_SIZE)
		{
			System.arraycopy(buf, 0, recvData, emptyIndex, bufSize);
			emptyIndex = (emptyIndex+bufSize) % BUFFER_SIZE;
		}
		else if( (bufSize+emptyIndex > BUFFER_SIZE) && (bufSize+emptyIndex < 2*BUFFER_SIZE))
		{
			System.arraycopy(buf, 0, recvData, emptyIndex, BUFFER_SIZE-emptyIndex);
			int temp = emptyIndex;
			emptyIndex = 0;
			System.arraycopy(buf, BUFFER_SIZE-temp, recvData, emptyIndex, bufSize-(BUFFER_SIZE-temp));
			emptyIndex = bufSize-(BUFFER_SIZE-temp);
		}
		else {
			Log.i(TAG, "Receive too much data.");
			return;
		}
		if(bufSize < 5) return;
		
		int i = parseIndex;
		while (i != emptyIndex) {
	        
			if ((recvData[i]&0xff) == PACKAGE_HEAD[0]) {
	            int j = (i + 1)%BUFFER_SIZE;
	            if (j != emptyIndex && (recvData[j]&0xff) == PACKAGE_HEAD[1]) {
	            	int k = (j+1)%BUFFER_SIZE;
	            	if(k != emptyIndex)
	            	{
	            		pkgLength = recvData[k]&0xff;
	            		pkgData = new byte[pkgLength+2];
	            		pkgStart = true;
		                pkgIndex = 0;
		                parseIndex = i;
	            	}
	            }
	        }
	        if (pkgStart && pkgLength > 0) {
	            pkgData[pkgIndex] = recvData[i];
	            pkgIndex++;

	            if ((pkgLength != 0) && (pkgIndex == pkgLength + 2)) {
	                if(CheckSum(pkgData)){
	                    ParsePackage(pkgData);
	                }
	                pkgStart = false;
	                parseIndex = (i + 1) % BUFFER_SIZE;
	            }
	        }
	        i = (i + 1) % BUFFER_SIZE;
	    }
		
	  
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
	
	//解包
	private void ParsePackage(byte[] pkgData) {
		// TODO Auto-generated method stub
		int pkgType = pkgData[3]&0xff;//pkgData[3]即A1，表示测试类型的区分
		//&0xff 把byte转换成int
		switch (pkgType) {
			//波形
			case PKG_SPO2_WAVE:
				if (CONST.FLAG) {
					skipCounter++;
					if (skipCounter == 1) {
						MainActivity.mSpO2WaveDraw.add(pkgData[4] & 0xff);//A2
						skipCounter = 0;
					}
				}
				break;
			//参数（血氧（SPO2）脉率（PR））
		    case PKG_SPO2://0x55 0xAA 0x06   0x04 0x01     //0x00 0x00//     0xF4
				STATE = pkgData[4];
				if (MainActivity.BTFLAG == true&&STATE==CONST.NORMAL_STATE&&CONST.FLAG) {
					mHandler.obtainMessage(CONST.MESSAGE_SPO2_PARAM, 0xff & pkgData[5], 0xff & pkgData[6]).sendToTarget();
					//A3和A4
				}


			    break;
		    case PKG_SW_VER:
			    //Log.i(TAG, "pkg_SW");
			    break;
		    case PKG_HW_VER:
			   // Log.i(TAG, "pkg_HW");
			    break;
		default:
			break;
		}
		
	}

	private boolean CheckSum(byte[] packageData) {
		// TODO Auto-generated method stub
		int sum = 0;
		for(int i = 2; i < packageData.length-1; i++)
		{
			sum+=(packageData[i]&0xff);
		}
		
		if(((~sum)&0xff) == (packageData[packageData.length-1]&0xff))
		{
			return true;
		}
		
		return false;
	}

	
}