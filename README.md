# AndFix_Demo
记录AndFix使用的方法和流程，demo中对AndFix的接口做了一个封装，用来管理补丁文件

#概述
**什么是热修复？**</br>
**Answer：**当一个App发布之后，突然发现了一个严重bug需要进行紧急修复，这时候公司各方就会忙得焦头烂额：重新打包App、测试、向各个应用市场和渠道换包、提示用户升级、用户下载、覆盖安装。有时候仅仅是为了修改了一行代码，也要付出巨大的成本进行换包和重新发布。
这时候就提出一个问题：有没有办法以补丁的方式动态修复紧急Bug，不再需要重新发布App，不再需要用户重新下载，覆盖安装？
虽然Android系统并没有提供这个技术，但是很幸运的告诉大家，答案是：可以

**目前主流热修复框架**</br>
**Answer：**[Dexposed、AndFix、ClassLoader 热补丁方案比较](http://blog.zhaiyifan.cn/2015/11/20/HotPatchCompare/?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io)

**为什么选择AndFix**</br>
**Answer:** Andfix支持Art模式，应用Patch不需要重启。。。详情请参照 answer2中博文中的“比较”模块。

#使用流程
![流程图](https://github.com/mime-mob/AndFix_Demo/blob/master/AndFixDemo/img/%E5%9B%BE%E7%89%871.png)
</br>
这是AndFix官方的流程图，大致可以理解为这样一个流程：
检测到线上的版本有bug，创建一个分支去修改bug，修改完成并且测试通过后生成release版本apk，利用工具和线上版本进行对比生成差分文件即.patch文件，然后将.patch文件上传到线上服务器，最后将修复bug的分支代码合并到主分支。

#使用配置(AS)
- 引入依赖
```
dependencies {
    compile 'com.alipay.euler:andfix:0.4.0@aar'
}
```
- 混淆配置
```
-keep class * extends java.lang.annotation.Annotation
-keepclasseswithmembernames class * {
    native <methods>;
}
```

#Apkpatch工具
与普通的第三方工具的不同的是，andfix除了要在代码中引入library dependency外，还需要一个辅助工具apkpatch.jar来生成差分文件。现在我们就来看看它是如何生成差分文件的。
</br>首先，看看apkpatch如何使用的：</br>
命令行进入apkpatch.jar所在目录后执行命令：</br>
`apkpatch -f new.apk -t old.apk -o outputDir -k app.jks -p kpassword -a alias -e epassword`
</br>命令执行完成后会在输出目录下面生成下面的文件：
![patch](https://github.com/mime-mob/AndFix_Demo/blob/master/AndFixDemo/img/%E5%9B%BE%E7%89%872.png)
</br>如果存在多个.Patch文件的话，可以用下面这个命令将其合并为一个：</br>
`apkpatch -m <apatch_path...> -o outputDir -k app.jks -p kpassword -a alias -e epassword`
