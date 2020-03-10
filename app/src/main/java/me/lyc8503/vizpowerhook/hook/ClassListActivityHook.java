package me.lyc8503.vizpowerhook.hook;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class ClassListActivityHook extends XC_MethodHook {

    public static final String TAG = "ClassListHook";

    @Override
    protected void beforeHookedMethod(MethodHookParam param) {
        XposedBridge.log(TAG + " 成功HookVizpower目标方法: " + param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) {
        Context vizContext = ((Activity) param.thisObject).getApplicationContext();
        Toast.makeText(vizContext, "VizpowerHook: You Have Hooked WXB!" + (CompileOpt.showBuildTime ? "0013" : ""), Toast.LENGTH_LONG).show();
        XposedBridge.log(TAG + " Hook Test已经完成!");
    }
}
