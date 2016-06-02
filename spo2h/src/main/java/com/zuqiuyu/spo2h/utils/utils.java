package com.zuqiuyu.spo2h.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.zuqiuyu.spo2h.MainActivity;
import com.zuqiuyu.spo2h.R;

//再按一次退出程序
public class utils
{
	static long exitTime = 0;
    public static void ExitWithToat(Context context,View view)
    {
    	if ((System.currentTimeMillis() - exitTime) > 2000)
    	{
		    Toast.makeText(context, context.getResources().getString(R.string.exit_tip), Toast.LENGTH_SHORT).show();
			//Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
		    exitTime = System.currentTimeMillis();
		}
    	else
    	{
    		MainActivity.mBluetoothUtils.StopBluetoothChatService();
    		((Activity) context).finish();
		}
    }
}