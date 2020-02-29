package me.lyc8503.vizpowerhook;

import android.content.Context;
import android.view.ContextThemeWrapper;

import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.lyc8503.vizpowerhook.hook.ClassListActivityHook;
import me.lyc8503.vizpowerhook.hook.LoginPDUHook;
import me.lyc8503.vizpowerhook.hook.StartParamHook;

public class HookTest implements IXposedHookLoadPackage {

    private static final String TAG = "VizpowerHookTest";
    private Context vizContext;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
//        XposedBridge.log("Package loaded: " + loadPackageParam.packageName);
        if (loadPackageParam.packageName.equals("vizpower.imeeting")) {
            XposedBridge.log(TAG + " 检测到无限宝启动并成功Hook.");

            XposedBridge.log(TAG + " 尝试获取360加固Classloader...");
            XposedHelpers.findAndHookMethod("com.stub.StubApp", loadPackageParam.classLoader, "attachBaseContext", Context.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log(TAG + " 成功找到360加固入口...");
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    // 获取到的参数args[0]就是360的Context对象，通过这个对象来获取classloader
                    Context context = (Context) param.args[0];
                    // 获取360的classloader，之后hook加固后的就使用这个classloader
                    ClassLoader classLoader = context.getClassLoader();
                    XposedBridge.log(TAG + " Hook到的Classloader: " + classLoader);
                    // 下面就是强classloader修改成360的classloader就可以成功的hook了


                    // 在ClassActivity中弹出提示提醒用户( 已做了对HD的适配 )
                    XC_MethodHook successHintHook = new ClassListActivityHook();
                    XposedHelpers.findAndHookMethod("vizpower.weblogin.ClassListActivity", classLoader, "init", successHintHook);
                    XposedHelpers.findAndHookMethod("vizpower.weblogin.ClassListActivityHD", classLoader, "init", successHintHook);


                    // Hook两个登陆有关的方法并打印出所有参数便于调试.
                    XposedHelpers.findAndHookMethod("vizpower.mtmgr.Room", classLoader, "advLogin", String.class, "vizpower.mtmgr.PDU.JoinMeetingPDU", "vizpower.mtmgr.IRoom.RetInfo", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            XposedBridge.log(TAG + " advLogin: " + Arrays.toString(param.args));
                        }
                    });
                    XposedHelpers.findAndHookMethod("vizpower.mtmgr.Room", classLoader, "loginToLoginServer", String.class, int.class, "vizpower.mtmgr.PDU.JoinMeetingPDU", "vizpower.mtmgr.IRoom.RetInfo", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            XposedBridge.log(TAG + " loginToLoginServer: " + Arrays.toString(param.args));
                        }
                    });


                    // Hook登陆PDU 获取 / 更改 信息
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.MeetingMgr", classLoader, "fillLoginInfo", "vizpower.mtmgr.PDU.JoinMeetingPDU", new LoginPDUHook());


                    // 直接Hook程序启动部分
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.MeetingMgr", classLoader, "getStartParam", String.class, String.class, new StartParamHook());
                }
            });
        }
    }
}
