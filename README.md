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
> 网上的很多有关MVVM的资料，在此就不再阐述什么是MVVM了，不清除的朋友可以先去了解一下。
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
> 以大家都熟悉的登录操作为例：三个文件**LoginActivty.java**、**LoginViewModel.java**、**activity_login.xml**

##### 2.1.1、关联ViewModel
在activity_login.xml中关联LoginViewModel。

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

##### 2.1.2、继承Base

LoginActivity继承BaseActivity
	
	public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {
		....
	}

> 保存activity_login.xml后databing会生成一个ActivityLoginBinding类。

BaseActivity有两个泛型参数，一个是ViewDataBinding，另一个是BaseViewModel，ActivityLoginBinding则是继承的ViewDataBinding作为第一个泛型参数，LoginViewModel继承BaseViewModel作为第二个泛型参数。

重写BaseActivity的三个方法

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

initContentView() 返回界面layout的id<br>
initVariableId() 返回变量的id，对应activity_login中variable - name：变量名，就像一个控件的id，可以使用R.id.xxx，这里的BR跟R文件一样，由系统生成，使用BR.xxx找到这个ViewModel的id。<br>
initViewModel() 返回ViewModel对象

LoginViewModel继承BaseViewModel

	public LoginViewModel(Context context) {
        super(context);
    }
在构造方法中调用super(context) 将上下文交给父类，即可使用父类的showDialog()、startActivity()等方法。在这个LoginViewModel中就可以尽情的写你的逻辑了！
### 2.2、数据绑定
> 拥有databinding框架自带的双向绑定，也有扩展
##### 2.2.1、传统绑定
绑定用户名：

在LoginViewModel中定义

	//用户名的绑定
	public ObservableField<String> userName = new ObservableField<>("");
在用户名EditText标签中绑定

	android:text="@={viewModel.userName}"

这样一来，输入框中输入了什么，userName.get()的内容就是什么，userName.set("")设置什么，输入框中就显示什么。
**注意：**@符号后面需要加=号才能达到双向绑定效果；userName需要是public的，不然viewModel无法找到它。

点击事件绑定：

在LoginViewModel中定义
	
	//登录按钮的点击事件
	 public View.OnClickListener loginOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            
        }
    };
在登录按钮标签中绑定

	android:onClick="@{viewModel.loginOnClick}"

这样一来，用户的点击事件直接被回调到ViewModel层了，更好的维护了业务逻辑

这就是强大的databing框架双向绑定的特性，不用再给控件定义id，setText()，setOnClickListener()。

**但是，光有这些，完全满足不了我们复杂业务的需求啊！MVVMHabit闪亮登场：它有一套自定义的绑定规则，可以满足大部分的场景需求，请继续往下看。**

##### 2.2.2、自定义绑定
还拿点击事件说吧，不用传统的绑定方式，使用自定义的点击事件绑定。

在LoginViewModel中定义

	//登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            
        }
    });

在activity_login中定义命名空间

	xmlns:binding="http://schemas.android.com/apk/res-auto"

在登录按钮标签中绑定

	binding:onClickCommand="@{viewModel.loginOnClickCommand}"

这和原本传统的绑定不是一样吗？不，这其实是有差别的。使用这种形式的绑定，在原本事件绑定的基础之上，带有防重复点击的功能，1秒内多次点击也只会执行一次操作。如果不需要防重复点击，可以加入这条属性

	binding:isThrottleFirst="@{Boolean.TRUE}"

那这功能是在哪里做的呢？答案在下面的代码中。

	//防重复点击间隔(秒)
    public static final int CLICK_INTERVAL = 1;

    /**
     * requireAll 是意思是是否需要绑定全部参数, false为否
     * View的onClick事件绑定
     * onClickCommand 绑定的命令,
     * isThrottleFirst 是否开启防止过快点击
     */
    @BindingAdapter(value = {"onClickCommand", "isThrottleFirst"}, requireAll = false)
    public static void onClickCommand(View view, final BindingCommand clickCommand, final boolean isThrottleFirst) {
        if (isThrottleFirst) {
            RxView.clicks(view)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            if (clickCommand != null) {
                                clickCommand.execute();
                            }
                        }
                    });
        } else {
            RxView.clicks(view)
                    .throttleFirst(CLICK_INTERVAL, TimeUnit.SECONDS)//1秒钟内只允许点击1次
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            if (clickCommand != null) {
                                clickCommand.execute();
                            }
                        }
                    });
        }
    }

onClickCommand方法是自定义的，使用@BindingAdapter注解来标明这是一个绑定方法。在方法中使用了RxView来增强view的clicks事件，.throttleFirst()限制订阅者在指定的时间内重复执行，最后通过BindingCommand将事件回调出去，就好比有一种拦截器，在点击时先做一下判断，然后再把事件沿着他原有的方向传递。

是不是觉得有点意思，好戏还在后头呢！
##### 2.2.3、自定义ImageView图片加载
绑定图片路径：

在ViewModel中定义

	public String imgUrl = "http://img0.imgtn.bdimg.com/it/u=2183314203,562241301&fm=26&gp=0.jpg";

在ImageView标签中

	binding:url="@{viewModel.imgUrl}"

url是图片路径，这样绑定后，这个ImageView就会去显示这张图片，不限网络图片还是本地图片。

如果需要给一个默认加载中的图片，可以加这一句

	binding:placeholderRes="@{R.mipmap.ic_launcher_round}"

> R文件需要在data标签中导入使用，如：`<import type="com.goldze.mvvmhabit.R" />`

BindingAdapter中的实现

	@BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext()).load(url).placeholder(placeholderRes)
                    .into(imageView);
        }
    }

很简单就自定义了一个ImageView图片加载的绑定，学会这种方式，可自定义扩展。
> 如果你对这些感兴趣，可以下载源码，在binding包中可以看到各类控件的绑定实现方式

##### 2.2.4、RecyclerView绑定
很常用的RecyclerView的绑定方式。

在ViewModel中定义：
	
	//给RecyclerView添加items
	public final ObservableList<NetWorkItemViewModel> observableList = new ObservableArrayList<>();
	//给RecyclerView添加ItemView
	public final ItemViewSelector<NetWorkItemViewModel> itemView = new ItemViewSelector<NetWorkItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, NetWorkItemViewModel item) {
			//设置item中ViewModel的id和item的layout
            itemView.set(BR.viewModel, R.layout.item_network);
        }

        @Override
        public int viewTypeCount() {
	 		//将RecyclerView划分成几部分，如果是一个list,就返回1，如果带有head和list，就返回2
            return 1;
        }
    };
ObservableList<>和ItemViewSelector<>的泛型是Item布局所对应的ViewModel

在xml中绑定

	<android.support.v7.widget.RecyclerView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                binding:itemView="@{viewModel.itemView}"
                binding:items="@{viewModel.observableList}"
                binding:layoutManager="@{LayoutManagers.linear()}"
                binding:lineManager="@{LineManagers.horizontal()}" />

layoutManager控制是线性的还是网格的，lineManager是控制水平的还是垂直的
> layoutManager和lineManager需要导入
> `<import type="me.tatarka.bindingcollectionadapter.LayoutManagers" />`
> `<import type="me.goldze.mvvmhabit.binding.viewadapter.recyclerview.LineManagers" />`

这样绑定后，在ViewModel中调用ObservableList的add()方法，添加一个Item的ViewModel，界面上就会实时绘制出一个Item。在Item对应的ViewModel中，同样可以以绑定的形式完成逻辑
> 可以在请求到数据后，循环添加`observableList.add(new NetWorkItemViewModel(context, entity));`详细可以参考例子程序中NetWorkViewModel类

## 3、网络请求
> 网络请求一直都是一个项目的核心，现在的项目基本都离不开网络，一个好用网络请求框架可以让开发事半功倍。
### 3.1、Retrofit+Okhttp+RxJava
> 现今，这三个组合基本是网络请求的标配，如果你对这三个框架不了解，建议先去查阅相关资料。

square出品的框架，用起来确实非常方便。在**MVVMHabit**中引入了

	compile "com.squareup.okhttp3:okhttp:3.8.1"
    compile "com.squareup.retrofit2:retrofit:2.3.0"
    compile "com.squareup.retrofit2:converter-gson:2.3.0"
    compile "com.squareup.retrofit2:adapter-rxjava:2.3.0"
所以只要在你构建的Retrofit中加入
	
	Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
 				.build();

或者直接使用例子程序中封装好的RetrofitClient
### 3.2、网络拦截器
**LoggingInterceptor：**全局拦截请求信息，格式化打印Request、Response，
	
	LoggingInterceptor mLoggingInterceptor = new LoggingInterceptor
		.Builder()//构建者模式
    	.loggable(true) //是否开启日志打印
        .setLevel(Level.BODY) //打印的等级
        .log(Platform.INFO) // 打印类型
        .request("Request") // request的Tag
        .response("Response")// Response的Tag
        .addHeader("version", BuildConfig.VERSION_NAME)//打印版本
        .build()


