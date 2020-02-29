package me.lyc8503.vizpowerhook.hook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class SetRoleHook extends XC_MethodHook {

    public static String TAG = "SetRoleHook";
    short newRole = 8;

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        XposedBridge.log(TAG + " 原来的Role: " + param.args[0] + " 新Role: " + newRole);
        param.args[0] = newRole;
    }
}
