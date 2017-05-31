package demo.sendcrashdemo;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * 版本 帮助类
 * 
 * @author ma
 */
public class CrashUtils {
	// 默认为北京时间对应的东八区
	private static final TimeZone GMT = TimeZone.getTimeZone("GMT+8");
	// SD卡的最小剩余容量大小1MB
	private final static long DEFAULT_LIMIT_SIZE = 1;


/////////////////////////////////////////获取手机信息/////////////////////////////////////////////
	/**
	 * 获取应用版本号
	 * 
	 * @param mContext
	 * @return
	 */
	public static int getAppVersionCode(Context mContext) {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
			int version = info.versionCode;
			return version;
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * 获取手机IMEI号
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getPhoneIMEI(Context mContext) {
		TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return imei;
	}

	/**
	 * 获取手机型号
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getPhoneModel(Context mContext) {
		// 取得 android 版本
		String manufacturer = "";
		String model = "";
		try {

			Class<android.os.Build> build_class = android.os.Build.class;
			// 取得牌子
			Field manu_field = build_class.getField("MANUFACTURER");
			manufacturer = (String) manu_field.get(new android.os.Build());
			// 取得型號
			Field field2 = build_class.getField("MODEL");
			model = (String) field2.get(new android.os.Build());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return manufacturer + " " + model;
	}

	/**
	 * 获取本地IP
	 * 
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return null;
	}

	/**
	 * 获取android当前可用内存大小 
	 */
	public static String getAvailMemory(Context mContext) {// 获取android当前可用内存大小   

		ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);  
		MemoryInfo mi = new MemoryInfo();  
		am.getMemoryInfo(mi);  
		//mi.availMem; 当前系统的可用内存   

		return Formatter.formatFileSize(mContext, mi.availMem);// 将获取的内存大小规格化   
	}  

	/**
	 * 获得系统总内存
	 */
	public static String getTotalMemory(Context mContext) {
		String str1 = "/proc/meminfo";// 系统内存信息文件 
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小 

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte 
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return Formatter.formatFileSize(mContext, initial_memory);// Byte转换为KB或者MB，内存大小规格化 
	}

	/////////////////////////////////////////时间格式化/////////////////////////////////////////////

	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			throw new IllegalArgumentException("date is null");
		}
		if (pattern == null) {
			throw new IllegalArgumentException("pattern is null");
		}
		SimpleDateFormat formatter = formatFor(pattern);
		return formatter.format(date);
	}
	private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS = new ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>() {

		@Override
		protected SoftReference<Map<String, SimpleDateFormat>> initialValue() {
			return new SoftReference<Map<String, SimpleDateFormat>>(
					new HashMap<String, SimpleDateFormat>());
		}
	};
	private static SimpleDateFormat formatFor(String pattern) {
		SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
		Map<String, SimpleDateFormat> formats = ref.get();
		if (formats == null) {
			formats = new HashMap<String, SimpleDateFormat>();
			THREADLOCAL_FORMATS.set(new SoftReference<Map<String, SimpleDateFormat>>(formats));
		}

		SimpleDateFormat format = formats.get(pattern);
		if (format == null) {
			format = new SimpleDateFormat(pattern, Locale.CHINA);
			format.setTimeZone(GMT);
			formats.put(pattern, format);
		}
		return format;
	}
	/////////////////////////////////////////将文本信息写入文件/////////////////////////////////////////////
	public static void writeToFile(String dir, String fileName, String content, String encoder) {
		File file = new File(dir, fileName);
		File parentFile = file.getParentFile();
		Log.e("ssssssssss","xieru");
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			osw = new OutputStreamWriter(new FileOutputStream(file, true), encoder);
			bw = new BufferedWriter(osw);
			bw.append(content);
			bw.append("\r\n");
			bw.flush();
		} catch (IOException e) {
		} finally {
			closeSilently(bw);
			closeSilently(osw);
		}
	}
	/**
	 * 关闭流操作
	 */
	public static void closeSilently(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
			}
		}
	}
	/////////////////////////////////////////判断SD卡是否可用/////////////////////////////////////////////
	public static boolean isSDCardAvaiable(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (getSDFreeSize() > DEFAULT_LIMIT_SIZE) {
				return true;
			} else {
				// ToastUtil.showToast(context, "SD卡容量不足，请检查");
				return false;
			}
		} else {
			// ToastUtil.showToast(context, "SD卡不存在或者不可用");
			return false;
		}
	}
	/**
	 * 获取SDCard的剩余大小
	 *
	 * @return 多少MB
	 */
	@SuppressWarnings("deprecation")
	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}
}
