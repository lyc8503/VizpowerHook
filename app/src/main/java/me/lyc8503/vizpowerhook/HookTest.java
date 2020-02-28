package me.lyc8503.vizpowerhook;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

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


                    // 先获取Context便于弹出Toast
                    XposedHelpers.findAndHookMethod(ContextThemeWrapper.class, "attachBaseContext", Context.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            vizContext = (Context) param.args[0];
                        }
                    });


                    // 在ClassActivity中弹出提示提醒用户( 已做了对HD的适配 )
                    XC_MethodHook successHintHook = new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            XposedBridge.log(TAG + " 成功HookVizpower目标方法: " + param);
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            Toast.makeText(vizContext, "VizpowerHook: 已经Hook到无限宝! 模块启动成功.", Toast.LENGTH_LONG).show();
                            XposedBridge.log(TAG + " Hook Test已经完成!");
                        }
                    };

                    XposedHelpers.findAndHookMethod("vizpower.weblogin.ClassListActivity", classLoader, "init", successHintHook);
                    XposedHelpers.findAndHookMethod("vizpower.weblogin.ClassListActivityHD", classLoader, "init", successHintHook);


                    // Hook两个登陆有关的方法并打印出所有参数便于调试.
                    XposedHelpers.findAndHookMethod("vizpower.mtmgr.Room", classLoader, "advLogin", String.class, "vizpower.mtmgr.PDU.JoinMeetingPDU", "vizpower.mtmgr.IRoom.RetInfo", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            XposedBridge.log(TAG + " advLogin: " + Arrays.toString(param.args));
//                            XposedBridge.log(TAG + " joinMeetingPDU: " + );
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {

                        }
                    });

                    XposedHelpers.findAndHookMethod("vizpower.mtmgr.Room", classLoader, "loginToLoginServer", String.class, int.class, "vizpower.mtmgr.PDU.JoinMeetingPDU", "vizpower.mtmgr.IRoom.RetInfo", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            XposedBridge.log(TAG + " loginToLoginServer: " + Arrays.toString(param.args));
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {

                        }
                    });


                    // Hook登陆PDU更改信息
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.MeetingMgr", classLoader, "fillLoginInfo", "vizpower.mtmgr.PDU.JoinMeetingPDU", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);

                            XposedBridge.log(TAG + " Hook到joinMeetingPDU的创建.");

                            for (Field field : param.args[0].getClass().getDeclaredFields()) {

                                if (field.getName().contains("szNickName")) {
//                                    Toast.makeText(vizContext, TAG + "VizpowerHook: 名字修改成功", Toast.LENGTH_LONG).show();
                                    XposedBridge.log("找到scNickName: " + XposedHelpers.getObjectField(param.args[0], field.getName()));
                                    XposedHelpers.setObjectField(param.args[0], field.getName(), "miaow~");
                                    XposedBridge.log("修改过后的scNickName: " + XposedHelpers.getObjectField(param.args[0], field.getName()));
                                }

//                                if(field.getName().contains("wUserRole")){
//                                    XposedBridge.log("找到wUserRole: " + XposedHelpers.getObjectField(param.args[2], field.getName()));
//                                    XposedHelpers.setObjectField(param.args[2], field.getName(), 8);
//                                    XposedBridge.log("修改过后的wUserRole: " + XposedHelpers.getObjectField(param.args[2], field.getName()));
//                                }
                            }

                        }
                    });
                }
            });
        }
    }
}
