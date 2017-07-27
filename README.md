# MVVMHabit
##
目前，android流行的MVC、MVP模式的开发框架很多，然而一款基于MVVM模式开发框架却很少。**MVVMHabit则是一款以谷歌的databinding为基础，整合Okhttp+RxJava+Retrofit+Glide等流行库，加上各种原生控件自定义的BindingAdapter，让事件与数据源完美绑定的一款容易上瘾的实用性快速开发框架**。告别findViewById()，告别setText()，也告别setOnClickListener()...
![](./img/mvvm_fc.jpg) 

## 框架特点
- **快速开发**

	只需要写项目的业务逻辑，不用再去关心网络请求、权限申请、view的生命周期等问题，撸起袖子就是干。

- **维护方便**

	MVVM开发模式，低耦合，逻辑分明。model层负责将请求的数据交给ViewModel；ViewModel层负责将请求到的数据做业务逻辑处理，最后交给View层去展示，与View一一对应；View层只负责界面绘制刷新，不处理业务逻辑，非常适合分配独立模块开发。

- **流行框架**

	[retrofit](https://github.com/square/retrofit)+[okhttp](https://github.com/square/okhttp)+[rxJava](https://github.com/ReactiveX/RxJava)负责网络请求；[gson](https://github.com/google/gson)负责解析json数据；[glide](https://github.com/bumptech/glide)负责加载图片；[rxlifecycle](https://github.com/trello/RxLifecycle)负责管理view的生命周期；与网络请求共存亡；[rxbinding](https://github.com/JakeWharton/RxBinding)结合databinding扩展UI事件；[rxpermissions](https://github.com/tbruyelle/RxPermissions)负责Android 6.0权限申请；[material-dialogs](https://github.com/afollestad/material-dialogs)一个漂亮的、流畅的、可定制的material design风格的对话框。

- **数据绑定**

	满足google目前控件支持的databinding双向绑定，并扩展原控件一些不支持的数据绑定。例如将图片的url路径绑定到ImageView控件中，在BindingAdapter方法里面则使用Glide加载图片；View的OnClick事件在BindingAdapter中方法使用RxView防重复点击，再把事件回调到ViewModel层，实现xml与ViewModel之间数据和事件的绑定(框架里面部分扩展控件和回调命令使用的是@kelin原创的)。

- **基类封装**

	专门针对MVVM模式打造的BaseActivity、BaseFragment、BaseViewModel，在View层中不再需要定义ViewDataBinding和ViewModel，直接在BaseActivity、BaseFragment上限定泛型即可使用。普通界面只需要编写Fragment，然后使用ContainerActivity盛装(代理)，这样就不需要每个界面都在AndroidManifest中注册一遍。

- **全局操作**

	1. 全局的Activity堆栈式管理，在程序任何地方可以打开、结束指定的Activity，一键退出应用程序。
	2. LoggingInterceptor全局拦截网络请求，打印Request和Response，格式化json、xml数据显示，方便与后台调试接口。
	3. 全局Cookie，支持SharedPreferences和内存两种管理模式。
	4. 全局的错误监听，根据不同的状态码或异常设置相应的message。
	5. 全局的异常捕获，程序发生异常时不会崩溃，可跳入异常界面重启应用。




## 1、准备工作
>网上的很多有关MVVM的资料，在此就不再阐述是什么MVVM了，不清除的朋友可以先去了解一下。
### 1.1、启用databinding
在主工程app的build.gradle的android {}中加入：

	dataBinding {
		enabled true
	}

### 1.2、依赖Library
从jcenter中央仓库远程依赖：

	暂不支持


或

下载例子程序，在主项目中依赖例子程序中的**mvvmhabit**：

	compile project(':mvvmhabit')

### 1.3、配置config.gradle
如果是下载的例子程序，那么还需要将例子程序中的config.gradle放入你的主项目根目录中，然后在根目录build.gradle的第一行加入：

	apply from: "config.gradle"

最后面加入：


	task clean(type: Delete) {
	    delete rootProject.buildDir
	}


**注意：**config.gradle中的 

android = [] 是你的开发相关版本配置，可自行修改

support = [] 是你的support相关配置，可自行修改

dependencies = [] 是依赖第三方库的配置，可以加新库，但不要去修改原有第三方库的版本号，不然可能会编译不过
### 1.4、配置AndroidManifest
添加权限：

	<uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

配置Application：

继承**mvvmhabit**中的BaseApplication，在你的自己AppApplication中配置
	
	//是否开启日志打印
	KLog.init(true);
	//配置全局异常崩溃操作
	CaocConfig.Builder.create()
        .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
        .enabled(true) //是否启动全局异常捕获
        .showErrorDetails(true) //是否显示错误详细信息
        .showRestartButton(true) //是否显示重启按钮
        .trackActivities(true) //是否跟踪Activity
        .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
        .errorDrawable(R.mipmap.ic_launcher) //错误图标
        .restartActivity(LoginActivity.class) //重新启动后的activity
	  //.errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
	  //.eventListener(new YourCustomEventListener()) //崩溃后的错误监听
        .apply();


## 2、快速上手

### 2.1、第一个Activity
以大家都熟悉的LoginActivty为例：三个文件**LoginActivty.java**、**LoginViewModel.java**、**activity_login.xml**

在activity_login.xml中导入LoginViewModel。

	<layout>

    <data>
        <variable
			type="com.goldze.mvvmhabit.ui.vm.LoginViewModel"
            name="viewModel"
            />
    </data>

		.....

	</layout>


> variable - type：类的全路径 <br>variable - name：变量名

LoginActivity继承BaseActivity
	
	public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {
		...
	}

> 保存activity_login.xml后databing会生成一个ActivityLoginBinding类。

BaseActivity有两个泛型参数，一个是ViewDataBinding，另一个是BaseViewModel，ActivityLoginBinding则是继承的ViewDataBinding作为第一个泛型参数，LoginViewModel继承BaseViewModel作为第二个泛型参数。

重写三个BaseActivity的三个方法

	@Override
    public int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LoginViewModel initViewModel() {
        //View持有ViewModel的引用 (这里暂时没有用Dagger2解耦)
        return new LoginViewModel(this);
    }

initContentView() 返回界面layout的id
<br>
initVariableId() 返回变量的id，对应activity_login中variable - name：变量名，就像一个控件的id,可以使用R.id.xxx，这里的BR跟R文件一样，由系统生成，使用BR.xxx找到这个ViewModel的id。
<br>
initViewModel() 返回ViewModel对象

LoginViewModel继承BaseViewModel

	public LoginViewModel(Context context) {
        super(context);
    }
在构造方法中调用super(context) 将上下文交给父类，即可使用父类的showDialog()、startActivity()等方法。在这个LoginViewModel中就可以尽情的写你的逻辑了！
### 2.1、数据绑定