package me.lyc8503.vizpowerhook.hook;

import de.robv.android.xposed.XC_MethodHook;

public class PubChatAndSendPicAndAskQuestionAndOpenMicHook extends XC_MethodHook {

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        param.setResult(true);
    }
}
