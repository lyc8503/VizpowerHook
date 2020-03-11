package me.lyc8503.vizpowerhook.hook;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook;

import android.app.AndroidAppHelper;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class SetTeacherOrAssistantMobileModeHook extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        ContentResolver resolver = AndroidAppHelper.currentApplication().getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://me.lyc8503.vizpowerhook/config");
        Cursor cursor = resolver.query(uri, null, null, null, null);
        cursor.moveToFirst();
        boolean teacherMode = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("teacherMode")));
        if (teacherMode) {
            super.beforeHookedMethod(param);
            param.setResult(true);
            XposedBridge.log("已经启动教师模式！");
        }
    }
}