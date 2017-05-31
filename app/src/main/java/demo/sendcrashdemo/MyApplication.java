package demo.sendcrashdemo;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ma on 2017/5/24.
 */

public class MyApplication extends Application implements
        CrashHandler.UncaughtExceptionHanlderListener {
    private List<Activity> activityList = new LinkedList<Activity>();
    private Thread thread;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance(getApplicationContext()).setHanlderListener(this);
        CrashHandler.getInstance(getApplicationContext()).setCrashLogDir(getCrashLogDir());
    }

    protected String getCrashLogDir() {
        // TODO Auto-generated method stub
        String sb = getExternalFilesDir(Environment.DIRECTORY_ALARMS) + "/log";
        return sb;
    }

    @Override
    public void handlerUncaughtException(StringBuffer sb) {
        Toast.makeText(getApplicationContext(),"cuowu",Toast.LENGTH_SHORT).show();
        SenderRunable senderRunnable = new SenderRunable("接收邮件账号",
                "密码", null, null);
        senderRunnable.setMail("--error--" + getCurrentTime(),
                sb.toString(), "发送邮件的账号", null);
        Log.e("error", "" + sb.toString());
        thread = new Thread(senderRunnable);
        thread.start();
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static String getCurrentTime() {
        String format = "yyyy-MM-dd  HH:mm:ss";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }
}
