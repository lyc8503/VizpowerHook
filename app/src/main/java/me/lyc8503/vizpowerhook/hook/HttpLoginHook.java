package me.lyc8503.vizpowerhook.hook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class HttpLoginHook extends XC_MethodHook {

    public static String TAG = "HttpLoginHook";

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);

        String newListenType = "-1";

        List<Map<String, String>> allClasses = (List<Map<String, String>>) XposedHelpers.getObjectField(param.thisObject, "m_MeetingDataList");
//        List<Map<String, String>> allClassesNew = new ArrayList<>();
        for (Map<String, String> oneClass : allClasses) {
            XposedBridge.log(TAG + " " + oneClass);
            for (Map.Entry entry : oneClass.entrySet()) {
                if (entry.getKey().equals("listenType")) {
                    entry.setValue(newListenType);
                    XposedBridge.log(TAG + " Hook到listenType并改为" + newListenType);
                }
            }
        }
    }
}
