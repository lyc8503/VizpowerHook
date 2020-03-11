package me.lyc8503.vizpowerhook;

import android.content.Context;
import android.view.ContextThemeWrapper;

import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.lyc8503.vizpowerhook.hook.BoolFunctionFalseHook;
import me.lyc8503.vizpowerhook.hook.BoolFunctionTrueHook;
import me.lyc8503.vizpowerhook.hook.ChatMgrHook;
import me.lyc8503.vizpowerhook.hook.ClassListActivityHook;
import me.lyc8503.vizpowerhook.hook.HttpLoginHook;
import me.lyc8503.vizpowerhook.hook.LoginPDUHook;
import me.lyc8503.vizpowerhook.hook.SetRoleHook;
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

                    // Hook Http登陆返回值
                    XposedHelpers.findAndHookMethod("vizpower.weblogin.VPWebLoginMgr", classLoader, "createMeetingListAdapter", new HttpLoginHook());

                    // 强制打开公聊
                    XposedHelpers.findAndHookMethod("vizpower.chat.ChatMgr", classLoader, "canSendChatPub", new BoolFunctionTrueHook());

                    // 开启发图模式。为防止夜神可能会被识别为 TV 而禁止，强制竖屏。已测试成功。
                    XposedHelpers.findAndHookMethod("vizpower.chat.ChatMgr", classLoader, "canSendPic", new BoolFunctionTrueHook());
                    XposedHelpers.findAndHookMethod("vizpower.common.VPUtils", classLoader, "isTVDevice", new BoolFunctionFalseHook());

                    // 开启提问功能，已测试成功。
                    XposedHelpers.findAndHookMethod("vizpower.chat.AskQuestionMgr", classLoader, "canSubmitQuestion", new BoolFunctionTrueHook());
                    XposedHelpers.findAndHookMethod("vizpower.chat.AskQuestionMgr", classLoader, "isAllowSubmitQuestion", new BoolFunctionTrueHook());


                    // 先写一个开麦克风功能, 代码风格和GUI都要改了但是我懒啊x
                    // 这个好像一口气打开了很多权限的样子随他去了 :P
                    // Anguei: 查了一下引用，发现只有在 AudioMgr.java 里面，有 if 形式的 hasTheChangeablePriv(int) 调用
                    //         在 ChatMgr.java 和 VPDocView.java 里面，有变量赋值形式的 hasTheChangeablePriv(int, int) 调用
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.PrivilegeMgr", classLoader, "hasTheChangeablePriv", int.class, new BoolFunctionTrueHook());

                    // 开启添加、标记文档的权限
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.iMeetingApp", classLoader, "isTeacherPhoneMode", new BoolFunctionTrueHook());
                    XposedHelpers.findAndHookMethod("vizpower.docview.DocManager", classLoader, "canAddDoc", new BoolFunctionTrueHook());
                    XposedHelpers.findAndHookMethod("vizpower.docview.DocManager", classLoader, "canNoteDoc", new BoolFunctionTrueHook());

                    // 开启弹幕
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.iMeetingApp", classLoader, "isCanDanMu", new BoolFunctionTrueHook());

                    // 开启发送礼物的功能
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.iMeetingApp", classLoader, "canShowGift", new BoolFunctionTrueHook());
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.iMeetingApp", classLoader, "canSendGift", new BoolFunctionTrueHook());

                    // 尝试修改为助教
                    // XposedHelpers.findAndHookMethod("vizpower.imeeting.iMeetingApp", classLoader, "isAssistantMode", new BoolFunctionTrueHook());
                    // XposedHelpers.findAndHookMethod("vizpower.immeting.UserMgr", classLoader, "isAssister", new BoolFunctionTrueHook());
                    // XposedHelpers.findAndHookMethod("vizpower.imeeting.MeetingMgr", classLoader, "setMyRole", short.class, new SetRoleHook());
                }
            });
        }
    }
}
