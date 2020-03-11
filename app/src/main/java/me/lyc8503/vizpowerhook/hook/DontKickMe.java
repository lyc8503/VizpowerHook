package me.lyc8503.vizpowerhook.hook;

import de.robv.android.xposed.XposedBridge;

import de.robv.android.xposed.XC_MethodHook;

public class DontKickMe extends XC_MethodHook{
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        param.setResult(null);
        XposedBridge.log("成功防踢！");
    }
}