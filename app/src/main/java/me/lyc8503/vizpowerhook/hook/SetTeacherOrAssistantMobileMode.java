package me.lyc8503.vizpowerhook.hook;

import de.robv.android.xposed.XposedBridge;

import de.robv.android.xposed.XC_MethodHook;

public class SetTeacherOrAssistantMobileMode extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        param.setResult(true);

        // Context vizContext = ((Activity) param.thisObject).getApplicationContext();
        // Toast.makeText(vizContext, "VizpowerHook: 已经启动教师模式！", Toast.LENGTH_LONG).show();
        XposedBridge.log("已经启动教师模式！");
    }
}