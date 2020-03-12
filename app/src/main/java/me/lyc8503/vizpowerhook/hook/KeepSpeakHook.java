package me.lyc8503.vizpowerhook.hook;

import android.app.AndroidAppHelper;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import de.robv.android.xposed.XposedBridge;

import de.robv.android.xposed.XC_MethodHook;

public class KeepSpeakHook extends XC_MethodHook{
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        /*ContentResolver resolver = AndroidAppHelper.currentApplication().getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://me.lyc8503.vizpowerhook/config");
        Cursor cursor = resolver.query(uri, null, null, null, null);
        cursor.moveToFirst();
        boolean keepSpeak = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("keepSpeak")));
        cursor.close();*/
        if (true) {
            param.setResult(null);
            XposedBridge.log("拦截到一次关闭麦克风操作！");
        }
    }
}