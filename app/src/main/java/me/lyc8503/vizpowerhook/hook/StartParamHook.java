package me.lyc8503.vizpowerhook.hook;

import android.app.Activity;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class StartParamHook extends XC_MethodHook {

    public static String TAG = "StartParamHook";

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);

        String newName = "miaow~";
        String newListenType = "-1";

        XposedBridge.log(TAG + " 获取的参数: " + param.args[0] + ", " + param.getResult());

        // 判断获取的参数并修改返回
//        if (param.args[0].equals("NickName")) {
//            XposedBridge.log(TAG + " Hook到名字并改为 " + newName);
//            param.setResult(newName);
//        }
//
//        if(param.args[0].equals("listenType")){
//            XposedBridge.log(TAG + " Hook到ListenType并改为 " + newListenType);
//            param.setResult(newListenType);
//        }

    }
}
