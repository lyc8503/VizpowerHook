package me.lyc8503.vizpowerhook.hook;

import java.util.Random;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class ReturnRandomMacHook extends XC_MethodHook {

    private String randomMac() {
        Random random = new Random();
        String[] mac = {
                // 前三位为分配给小米手机的厂商编码
                String.format("%02x", 0x7c),
                String.format("%02x", 0xd6),
                String.format("%02x", 0x61),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff))
        };

        StringBuilder builder = new StringBuilder();
        for (String str : mac) {
            builder.append(str).append(":");
        }

        String finalMac = builder.toString().substring(0, builder.length() - 1);
        XposedBridge.log("随机 Mac: " + finalMac);
        return finalMac;
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);

        String fakeMac = randomMac();
        param.setResult(fakeMac);

    }
}
