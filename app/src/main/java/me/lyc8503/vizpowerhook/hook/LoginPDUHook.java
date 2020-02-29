package me.lyc8503.vizpowerhook.hook;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class LoginPDUHook extends XC_MethodHook {

    public static String TAG = "LoginPDUHook";

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);

        XposedBridge.log(TAG + " Hook到joinMeetingPDU的创建.");

        for (Field field : param.args[0].getClass().getDeclaredFields()) {

            if (field.toGenericString().contains("final")) continue;

            XposedBridge.log("joinMeetingPDU: " + field.getName() + ", " + XposedHelpers.getObjectField(param.args[0], field.getName()));

            // 暂时不在此处修改, 尝试从getStartParam()处修改.
//                                if (field.getName().contains("szNickName")) {
////                                    Toast.makeText(vizContext, TAG + "VizpowerHook: 名字修改成功", Toast.LENGTH_LONG).show();
//                                    XposedBridge.log("找到scNickName: " + XposedHelpers.getObjectField(param.args[0], field.getName()));
//                                    XposedHelpers.setObjectField(param.args[0], field.getName(), "miaow~");
//                                    XposedBridge.log("修改过后的scNickName: " + XposedHelpers.getObjectField(param.args[0], field.getName()));
//                                }
//
//                                if(field.getName().contains("wUserRole")){
//                                    XposedBridge.log("找到wUserRole: " + XposedHelpers.getObjectField(param.args[0], field.getName()));
//                                    XposedHelpers.setShortField(param.args[0], field.getName(), Short.valueOf("8"));
//                                    XposedBridge.log("修改过后的wUserRole: " + XposedHelpers.getObjectField(param.args[0], field.getName()));
//                                }
        }

    }
}
