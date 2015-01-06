package com.ruipengkj.dgxtos.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import org.apache.http.conn.util.InetAddressUtils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * TODO 网络工具类
 */
public class NetTools {

	/**
	 * 检查当前手机网络
	 * 
	 * @param context
	 * @return false没有网络
	 */
	public static boolean checkNet(Context context) {
		// 判断连接方式
		ConnectivityManager manager =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobileNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean wifiConnected, mobileConnected;
		if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
			wifiConnected = true;
		} else {
			wifiConnected = false;
		}
		if (mobileNetworkInfo != null && mobileNetworkInfo.isConnected()) {
			mobileConnected = true;
		} else {
			mobileConnected = false;
		}
		if (wifiConnected == false && mobileConnected == false) {
			// 如果都没有连接返回false，提示用户当前没有网络
			return false;
		}
		if (mobileConnected == true) {
			// 判断到当前是mobile连接，设置apn
			getLocalIPAddress();
		}
		return true;
	}

	private static void getLocalIPAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
						System.out.println("getHostAddress" + inetAddress.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
