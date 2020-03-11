# VizpowerHook
~~可以Hook到无限宝...然后就打开了新世界的大门?(雾)~~

## 现有功能

以下标有始终开启的功能无需到程序界面设置, 直接安装插件并激活即可正常使用.

- 强制竖屏, 不必忍受那难用的横屏模式
- 可以改名, 把自己的名字改成任意字符串.
- (始终开启)可以强制打开公聊权限. 上课时发言可以在公聊和私聊之间任意切换, 同时绕过禁言机制. 即使被助教或主讲禁言, 也可以正常说话.
- (始终开启)绕过屏蔽词. 无论发什么都不会变成星号.
- (始终开启)强制开启提问功能. 无视权限.
- (始终开启)开启发送图片的功能. 可以在公聊或私聊当中发送任意图片.
- (始终开启)可以开启麦克风. 你可以开启自己麦克风, 在课堂上放自己最喜欢的歌曲 ~~Hop~~.
- (始终开启)**测试中**: 显示在线人数.(只是修改了相关变量但在安卓端好像没有什么用?)
- **测试中**: 自动签到.
- **测试中**: 即使小窗运行或在后台运行, 始终保持认真状态.
- (始终开启)TODO: 隐藏机型和 Mac 地址
- (始终开启)大佬专用: 输出程序登陆/进入课堂时的一些关键变量给你继续深入攻破它提供帮助x.

## 安装方法
就是一个简单的 Xposed 插件, 安装后在 Xposed 中激活重启手机即可. 具体设置见应用主界面.
注意: 因为无限宝已经使用360加固, 所以本模块**不支持太极阴框架**, 其他需要重新打包应用的操作也都不支持.(重新打包应用会导致闪退)

#### 萌新再往下看
如果你不知道什么是 Xposed, 可以自行 Google Xposed框架安装教程.

没有Root的手机如果需要虚拟框架推荐使用 **Virtual Xposed(测试通过)**, 也可以使用 vmos 虚拟机.
如果要使用电脑上课, PC上的安卓虚拟机推荐支持VT的**夜神模拟器**, (注意要手动安装Xposed), 已测试通过.

IOS **不支持**而且在将来也不会支持此插件. 请使用安卓手机或电脑运行此插件.

## 关于我
高二了没有太多时间维护...尽量维护框架所有功能的正常运行, 短期内应该不会开发新功能.

## 给大佬们
原理很简单就是用 Xposed API Hook 相关的方法和修改变量.
欢迎大家上交 PR 或者 提交 Issues(Feature request 的话...你可能也要等其它大佬帮你实现了x).

如果是 PR 的话一般我都会尽快 merge 到 dev 分支, 出一个测试版.(只要没有bug, 能编译通过)
至于什么时候 merge 到 master 分支就...(咕咕咕).

开发过程是需要参考无限宝的源码的, 源码我已经反编译好了, 在这里: [https://github.com/lyc8503/VizpowerSourceCode](https://github.com/lyc8503/VizpowerSourceCode).

At Last, Happy coding!