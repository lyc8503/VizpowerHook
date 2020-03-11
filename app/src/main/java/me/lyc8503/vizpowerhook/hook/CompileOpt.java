package me.lyc8503.vizpowerhook.hook;

public class CompileOpt {
    // 由于 Java 没有类似 C++ 的 #ifdef 条件编译机制，所以只能这么搞了（
    // commit 之前记得把这里全都调成 false
    public static final boolean showBuildTime = false;
    public static final boolean changeName = false;
}