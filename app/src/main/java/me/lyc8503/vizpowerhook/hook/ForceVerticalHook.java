package me.lyc8503.vizpowerhook.hook;

import android.app.AndroidAppHelper;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class ForceVerticalHook extends XC_MethodHook {

    private static final String TAG = "ForceVerticalHook";

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);

        ContentResolver resolver = AndroidAppHelper.currentApplication().getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://me.lyc8503.vizpowerhook/config");
        Cursor cursor = resolver.query(uri, null, null, null, null);
        // XposedBridge.log(TAG + " " + Arrays.toString(cursor.getColumnNames()));
        cursor.moveToFirst();
        boolean vertical = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("forceVertical")));
        cursor.close();

        if (vertical) {
            param.setResult(false);
//            XposedBridge.log(TAG + " 已经强制改为竖屏模式.");
        }
    }
}
