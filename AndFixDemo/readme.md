#AndFix热修复框架App使用框架

##作用：简化App植入AndFix框架的操作

##使用方法
1：导入Model（尚未支持gradle编译）

2：在Application.onCreate()方法中
	VersionManager.getInstance(context).initLoad();

3：在任何需要下载patch的地方
	VersionManager.getInstance(context).downloadPatch(patchUri);
	
4：获取当前已经加载的最新patch版本号
	VersionManager.getInstance(context).getLastPatchVersion();
	
**PS：getLastPatchVersion()这个方法每次从文件系统中获取最新patch版本号，不能依赖之前的下载记录，因为下载过的patch可能会被删除。**