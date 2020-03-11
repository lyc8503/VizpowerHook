package me.lyc8503.vizpowerhook;

import android.content.Context;

import java.nio.ByteBuffer;
import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.lyc8503.vizpowerhook.hook.AutoRollcallHook;
import me.lyc8503.vizpowerhook.hook.ClassListActivityHook;
import me.lyc8503.vizpowerhook.hook.DontKickMeHook;
import me.lyc8503.vizpowerhook.hook.ForceVerticalHook;
import me.lyc8503.vizpowerhook.hook.HideRealSystemInfoHook;
import me.lyc8503.vizpowerhook.hook.HttpLoginHook;
import me.lyc8503.vizpowerhook.hook.KeepFocusedHook;
import me.lyc8503.vizpowerhook.hook.KeepSpeakHook;
import me.lyc8503.vizpowerhook.hook.LoginPDUHook;
import me.lyc8503.vizpowerhook.hook.PubChatAndSendPicAndAskQuestionAndOpenMicHook;
import me.lyc8503.vizpowerhook.hook.ReturnRandomMacHook;
import me.lyc8503.vizpowerhook.hook.StartParamHook;
import me.lyc8503.vizpowerhook.hook.SetTeacherOrAssistantMobileModeHook;
import me.lyc8503.vizpowerhook.hook.WantEditDocHook;

public class HookTest implements IXposedHookLoadPackage {

    private static final String TAG = "VizpowerHookTest";

    public static ClassLoader classLoader;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
//        XposedBridge.log("Package loaded: " + loadPackageParam.packageName);
        if (loadPackageParam.packageName.equals("vizpower.imeeting")) {
            XposedBridge.log(TAG + " 检测到无限宝启动并成功 Hook.");

            XposedBridge.log(TAG + " 尝试获取 360 加固 Classloader...");
            XposedHelpers.findAndHookMethod("com.stub.StubApp", loadPackageParam.classLoader, "attachBaseContext", Context.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log(TAG + " 成功找到 360 加固入口...");
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    // 获取到的参数 args[0] 就是 360 的 Context 对象，通过这个对象来获取 classloader
                    Context context = (Context) param.args[0];
                    // 获取 360 的 classloader，之后 hook 加固后的就使用这个 classloader
                    classLoader = context.getClassLoader();
                    XposedBridge.log(TAG + " Hook到的Classloader: " + classLoader);
                    // 下面就是强classloader修改成360的classloader就可以成功的hook了

                    // 在 ClassActivity 中弹出提示提醒用户( 已做了对HD的适配 )
                    XC_MethodHook successHintHook = new ClassListActivityHook();
                    XposedHelpers.findAndHookMethod("vizpower.weblogin.ClassListActivity", classLoader, "init", successHintHook);
                    XposedHelpers.findAndHookMethod("vizpower.weblogin.ClassListActivityHD", classLoader, "init", successHintHook);


                    // Hook 两个登陆有关的方法并打印出所有参数便于调试.
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


                    // Hook 登陆 PDU 获取 / 更改 信息
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.MeetingMgr", classLoader, "fillLoginInfo", "vizpower.mtmgr.PDU.JoinMeetingPDU", new LoginPDUHook());


                    // 直接 Hook 程序启动部分
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.MeetingMgr", classLoader, "getStartParam", String.class, String.class, new StartParamHook());

                    // Hook Http 登陆返回值
                    XposedHelpers.findAndHookMethod("vizpower.weblogin.VPWebLoginMgr", classLoader, "createMeetingListAdapter", new HttpLoginHook());

                    // 强制打开公聊
                    XposedHelpers.findAndHookMethod("vizpower.chat.ChatMgr", classLoader, "canSendChatPub", new PubChatAndSendPicAndAskQuestionAndOpenMicHook());

                    // 开启发图模式。为防止夜神可能会被识别为 TV 而禁止，强制竖屏。已测试成功。
                    XposedHelpers.findAndHookMethod("vizpower.chat.ChatMgr", classLoader, "canSendPic", new PubChatAndSendPicAndAskQuestionAndOpenMicHook());
                    XposedHelpers.findAndHookMethod("vizpower.common.VPUtils", classLoader, "isTVDevice", new ForceVerticalHook());
                    XposedHelpers.findAndHookMethod("vizpower.common.VPUtils", classLoader, "isPadDevice", new ForceVerticalHook());

                    // 开启提问功能，已测试成功。
                    XposedHelpers.findAndHookMethod("vizpower.chat.AskQuestionMgr", classLoader, "canSubmitQuestion", new PubChatAndSendPicAndAskQuestionAndOpenMicHook());
                    XposedHelpers.findAndHookMethod("vizpower.chat.AskQuestionMgr", classLoader, "isAllowSubmitQuestion", new PubChatAndSendPicAndAskQuestionAndOpenMicHook());

                    // 自动签到 + 保持认真
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.RollCallMgr", classLoader, "onRollcall", ByteBuffer.class, new AutoRollcallHook());
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.iMeetingApp", classLoader, "isAppActive", new KeepFocusedHook());

                    // 隐藏 Mac 地址 和 机型
                    XposedHelpers.findAndHookMethod("vizpower.common.VPUtils", classLoader, "getMacAddrByNetworkInterface", new ReturnRandomMacHook());
                    XposedHelpers.findAndHookMethod("vizpower.common.VPUtils", classLoader, "getLocalMacAddressByWifiManager", Context.class, new ReturnRandomMacHook());
                    XposedHelpers.findAndHookMethod("vizpower.common.VPUtils", classLoader, "getVersion", new HideRealSystemInfoHook());

                    // 先写一个开麦克风功能, 代码风格和GUI都要改了但是我懒啊x
                    // 这个好像一口气打开了很多权限的样子随他去了 :P
                    // Anguei: 查了一下引用，发现只有在 AudioMgr.java 里面，有 if 形式的 hasTheChangeablePriv(int) 调用
                    //         在 ChatMgr.java 和 VPDocView.java 里面，有变量赋值形式的 hasTheChangeablePriv(int, int) 调用
                    // 暂时就开着吧反正可以开麦x
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.PrivilegeMgr", classLoader, "hasTheChangeablePriv", int.class, new PubChatAndSendPicAndAskQuestionAndOpenMicHook());
                    XposedHelpers.findAndHookMethod("vizpower.mtmgr.Room", classLoader, "denySpeak", int.class, new KeepSpeakHook());

                    // 开启教师模式、添加和标记文档的权限，
                    // TODO: 开启教师模式后，会有不定时重连的 feature，需要解决
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.iMeetingApp", classLoader, "isTeacherPhoneMode", new SetTeacherOrAssistantMobileModeHook());
                    XposedHelpers.findAndHookMethod("vizpower.docview.DocManager", classLoader, "canAddDoc", new WantEditDocHook());
                    XposedHelpers.findAndHookMethod("vizpower.docview.DocManager", classLoader, "canNoteDoc", new WantEditDocHook());

                    // 各种防踢
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.iMeetingApp", classLoader, "kickout", int.class, new DontKickMeHook());
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.MainActivity", classLoader, "onExit", new DontKickMeHook());
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.MainActivityHD", classLoader, "onExit", new DontKickMeHook());
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.MainActivityTeacher", classLoader, "onExit", new DontKickMeHook());
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.MainActivityTeacherHD", classLoader, "onExit", new DontKickMeHook());
                    XposedHelpers.findAndHookMethod("vizpower.imeeting.AssistantActivity", classLoader, "onExit", new DontKickMeHook());


                    // 开启发送礼物的功能

                    // 礼物加载不出来...
//                    XposedHelpers.findAndHookMethod("vizpower.imeeting.iMeetingApp", classLoader, "canShowGift", new BoolFunctionTrueHook());
//                    XposedHelpers.findAndHookMethod("vizpower.imeeting.iMeetingApp", classLoader, "canSendGift", new BoolFunctionTrueHook());

                    // 尝试修改为助教
                    // XposedHelpers.findAndHookMethod("vizpower.imeeting.iMeetingApp", classLoader, "isAssistantMode", new BoolFunctionTrueHook());
                    // XposedHelpers.findAndHookMethod("vizpower.imeeting.UserMgr", classLoader, "isAssister", new BoolFunctionTrueHook());
                    // XposedHelpers.findAndHookMethod("vizpower.imeeting.MeetingMgr", classLoader, "setMyRole", short.class, new SetRoleHook());
                }
            });
        }
    }
}
