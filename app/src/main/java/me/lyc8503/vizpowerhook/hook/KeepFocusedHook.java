package me.lyc8503.vizpowerhook.hook;

import android.app.AndroidAppHelper;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class KeepFocusedHook extends XC_MethodHook {

    private static final String TAG = "KeepFocusedHook";

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);

        ContentResolver resolver = AndroidAppHelper.currentApplication().getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://me.lyc8503.vizpowerhook/config");
        Cursor cursor = resolver.query(uri, null, null, null, null);
        XposedBridge.log(TAG + " " + Arrays.toString(cursor.getColumnNames()));
        cursor.moveToFirst();
        boolean rollcall = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("autoRollcall")));
        cursor.close();

        if (rollcall) {
            XposedBridge.log(TAG + "强制修改为认真.");
            param.setResult(true);
        }
    }
}
