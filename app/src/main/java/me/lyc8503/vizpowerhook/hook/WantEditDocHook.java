package me.lyc8503.vizpowerhook.hook;

import de.robv.android.xposed.XposedBridge;

import de.robv.android.xposed.XC_MethodHook;

public class WantEditDocHook extends  XC_MethodHook{
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        param.setResult(true);
        XposedBridge.log("已经开启文档编辑权限！");
    }
}
