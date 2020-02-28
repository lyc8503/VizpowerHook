package me.lyc8503.vizpowerhook;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

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


                    XposedHelpers.findAndHookMethod("vizpower.weblogin.ClassListActivity", classLoader, "init", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            XposedBridge.log(TAG + " 成功HookVizpower目标方法: " + param);
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {

                            Toast.makeText(vizContext, "VizpowerHook: 已经成功Hook到无限宝!", Toast.LENGTH_LONG).show();
                            XposedBridge.log(TAG + " Hook Test已经完成!");
                        }
                    });
                }
            });
        }
    }
}
