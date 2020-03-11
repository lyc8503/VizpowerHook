package me.lyc8503.vizpowerhook.hook;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class HideRealSystemInfoHook extends XC_MethodHook {

    private static final String TAG = "HideRealSystemInfoHook";

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        String[] ret = (String[]) param.getResult();
        XposedBridge.log(TAG + " 修改系统信息: 原信息" + Arrays.toString(ret));
        ret[0] = null;
        ret[1] = "9";
        ret[2] = "Mi";
        ret[3] = "";
        XposedBridge.log(TAG + " 修改系统信息: 修改过后信息" + Arrays.toString(ret));
        param.setResult(ret);
    }
}
