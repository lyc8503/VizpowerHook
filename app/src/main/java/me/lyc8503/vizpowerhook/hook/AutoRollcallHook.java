package me.lyc8503.vizpowerhook.hook;

import android.app.AndroidAppHelper;
import android.app.Application;
import android.app.Notification;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import me.lyc8503.vizpowerhook.HookTest;
import me.lyc8503.vizpowerhook.util.NotificationUtils;

public class AutoRollcallHook extends XC_MethodHook {

    private static final String TAG = "AutoRollcallHook";

    @Override
    protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);

        ContentResolver resolver = AndroidAppHelper.currentApplication().getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://me.lyc8503.vizpowerhook/config");
        Cursor cursor = resolver.query(uri, null, null, null, null);
        XposedBridge.log(TAG + " " + Arrays.toString(cursor.getColumnNames()));
        cursor.moveToFirst();
        boolean rollcall = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("autoRollcall")));
        cursor.close();

        XposedBridge.log("检测到签到! ");

        if (rollcall) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(new Random().nextInt(2000) + 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Method method = XposedHelpers.findMethodExact("vizpower.imeeting.viewcontroller.RollcallConfirmViewController", HookTest.classLoader, "vizpower.imeeting.viewcontroller.RollcallConfirmViewController");

                    try {
                        method.invoke(param.thisObject);
                        XposedBridge.log("签到成功完成!");
                        NotificationUtils.showNotification(AndroidAppHelper.currentApplication().getApplicationContext(), "自动签到成功!", "");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        NotificationUtils.showNotification(AndroidAppHelper.currentApplication().getApplicationContext(), "自动签到失败!", "请手动打开无限宝进行签到!并将此BUG连同Xposed框架日志反馈到Github项目的Isuues.");
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        NotificationUtils.showNotification(AndroidAppHelper.currentApplication().getApplicationContext(), "自动签到成功!", "请手动打开无限宝进行签到!并将此BUG连同Xposed框架日志反馈到Github项目的Isuues.");
                    }
                }
            });

            t.start();
        }
    }
}
