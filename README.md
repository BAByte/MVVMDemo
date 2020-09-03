- [Sample MVVM App 介绍](#sample-mvvm-app---)
- [架构图](#---)
  * [功能列表](#----)
- [屏幕截图](#----)
    + [主页](#--)
    + [收藏](#--)
    + [水果](#--)
- [测试用例](#----)
    + [UI相关](#ui--)
    + [业务层到数据存储区的数据走向](#--------------)
- [项目包目录结构](#-------)
    + [包目录说明](#-----)
- [依赖库](#---)
- [ViewModle示例](#viewmodle--)
- [ViewModel使用示例](#viewmodel----)
- [Gradle编译配置](#gradle----)
    + [root/build.gradle](#root-buildgradle)
    + [app/build.gradle](#app-buildgradle)



# Sample MVVM App 介绍

基于Android最新的JetPack组件创建一个公共的MVVM项目示例，为团队统一框架开发提供支持。

该项目根据应用架构指南：https://developer.android.google.cn/jetpack/docs/guide#build-ui 进行设计。


# 架构图
在MVVM基础上增加一个业务层，与ViewModel平级。

![image](https://github.com/BAByte/pic/raw/master/2453453453rwfwdf.png)


## 功能列表

+ 查看各种水果介绍

+ 收藏喜欢的水果到收藏页面

+ 后台定时收集程序日志并上报



# 屏幕截图

### 主页

![image](https://github.com/BAByte/pic/blob/master/home.png?raw=true)

### 收藏

![image](https://github.com/BAByte/pic/blob/master/like.png?raw=true)

### 水果

![image](https://github.com/BAByte/pic/blob/master/detail.png?raw=true)


# 测试用例

### UI相关

+ MainActivityTest ： 该用例测试软件的展示功能，收藏功能是否正常

### 业务层到数据存储区的数据走向

+ FruitsRepositoryTest
+ LikeFruitsRepositoryTest
+ LogInfoRepositoryTest

# 项目包目录结构

~~~text
.
├── App.kt
├── extensions
│   └── MutableList.kt
├── log //业务单元
├── repository // 数据存储区
│   ├── data
│   ├── database
│   └── impl
│       └── FruitsRepository.kt
│		├── IFruitsRepository.kt
├── ui //UI层
│   ├── BaseActivity.kt
│   ├── BindingAdapter.kt
│   ├── MainActivity.kt
│   ├── fragment
│   │   ├── BaseFragment.kt
│   │   ├── FruitPicDetailFragment.kt
│   ├── recycler
│   │   ├── DividerItemDecoration.java
│   │   └── adapter
│   │       ├── FruitListAdapter.kt
│   │       └── LikeFruitsAdapter.kt
│   ├── viewmodels
│   │   └── LoggerInfoViewModel.kt
│   └── views
│       └── LoadingDialog.kt
└── utils
    └── ProcessUtils.java
    
~~~

### 包目录说明

+ 根目录：放App类，主服务类
+ extensions ：存放扩展方法的文件，命名规范：扩展哪个类的方法就以该类名称命名
+ log：存放的是日志收集器的业务实现，属于业务层
+ repository： 数据存储区存放向ViewModel提供数据的Repository接口，
+ + data：存放data class
  + database ：存放本地数据库相关内容
  + impl：Repository 具体实现
  + IFruitsRepository.kt Repository接口

+ ui : view层和viewModel层。
+ + Activity、BindingAdapter存放在该层根目录
  + fragment : 存放fragment类
  + recycler： 存放recycler的adapter，分割线样式相关类
+ views： 存放自定义View
+ utils：存放全局工具类

# 依赖库

- 基础

   -核心系统功能，Kotlin扩展以及对Multidex和自动化测试的支持的组件。

  - [AppCompat-](https://developer.android.com/topic/libraries/support-library/packages#v7-appcompat)在旧版Android上优雅降级。
  - [Android KTX-](https://developer.android.com/kotlin/ktx)编写更简洁，惯用的Kotlin代码。
  - [Test](https://developer.android.com/training/testing/) -用于单元和运行时UI测试的Android测试框架。

- 体系结构

  - [Data Binding](https://developer.android.com/topic/libraries/data-binding/)  -以声明方式将可观察的数据绑定到UI元素。
  - [Lifecycles](https://developer.android.com/topic/libraries/architecture/lifecycle) -创建一个自动响应生命周期事件的UI。
  - [LiveData-](https://developer.android.com/topic/libraries/architecture/livedata)构建数据对象，这些数据对象在基础数据库更改时通知视图。
  - [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/)  -处理应用Fragment和Activity导航所需的一切。
  - [Room](https://developer.android.com/topic/libraries/architecture/room) -使用应用内对象和编译时检查来访问应用的SQLite数据库。
  - [ViewModel-](https://developer.android.com/topic/libraries/architecture/viewmodel)存储与用户界面相关的数据，这些数据在应用程序旋转时不会被破坏。轻松安排异步任务以实现最佳执行。
  - [WorkManager-](https://developer.android.com/topic/libraries/architecture/workmanager)管理您的Android后台作业。

- 第三方

  - [Glide](https://bumptech.github.io/glide/)以加载图像
  - [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) 通过简化的代码管理后台线程并减少了对回调的需求
  - [Logger](https://github.com/orhanobut/logger) 打印漂亮的日志
  - [Gson](https://github.com/google/gson) Json转换的支持

**下面给出两种获取ViewModel的示例，由于篇幅问题，其他库的使用示例请看代码**

# ViewModle示例
~~~kotlin


class LikeFruitsViewModel(
    private val likeFruitsRepository: LikeFruitsRepository
) : ViewModel() {
    //模拟该数据是向数据库获取，数据库为唯一数据源
    var fruits: LiveData<List<Fruit>> = likeFruitsRepository.load()

    suspend fun deleteFromDataBase(fruit: Fruit) {
        fruits.value?.let {
            likeFruitsRepository.deleteFromDataBase(fruit)
        }
    }

    fun getFruit(id: String): Fruit? {
        return likeFruitsRepository.getFruit(id)
    }

    suspend fun deleteFromDataBase(position: Int) {
        fruits.value?.let {
            likeFruitsRepository.deleteFromDataBase(it[position])
        }
    }

    suspend fun saveToDataBase(fruits: List<Fruit>): List<Long> {
        return likeFruitsRepository.saveToDataBase(fruits)
    }
}
~~~
# ViewModel使用示例
~~~kotlin
class LikeFruitFragment : BaseFragment<FragmentLikeFruitsBinding>() {
  
		//委托生成ViewModel，获取的viewModel不是共享的
 		private val likeFruitsViewModel: LikeFruitsViewModel by viewModels ()
    
   	override fun onBinding(
        	inflater: LayoutInflater,
        	container: ViewGroup?,
        	savedInstanceState: Bundle?
    ): FragmentLikeFruitsBinding = FragmentLikeFruitsBinding.inflate(
        	inflater, container, false
    )
  
  	override fun setupUI(binding: FragmentLikeFruitsBinding) {
      	  //使用ViewModelProvider生成ViewModel，获取共享的ViewModel
        	likeFruitsViewModel = ViewModelProvider(
          	  requireActivity(), ViewModelProvider.NewInstanceFactory()
        	).get(LikeFruitsViewModel::class.java)
  	}
    
   	 //订阅数据更新
     private fun subscribeUI() {
        likeFruitsViewModel.fruits.observe(this) {
            Logger.d("LikeFruitsActivity observe = $it")
        }
    	}
}
~~~
# Gradle编译配置

### root/build.gradle

~~~java
 ext {
        kotlinVersion = '1.3.50'
        navigationVersion = '2.0.0'
    }
    
 dependencies {
        classpath 'com.android.tools.build:gradle:3.5.1'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        classpath "androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion"       
    }
~~~


### app/build.gradle

~~~java

//jetpack，kotlin需要添加如下插件
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'
apply plugin: 'kotlin-kapt'
apply plugin: 'androidx.navigation.safeargs'

android {
   ...
   //启用dataBinding
    dataBinding {
        enabled = true
    }

    defaultConfig {
      ...
      //单元测试
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"

        //设置room输出描述数据库结构文件的目录
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = ["room.schemaLocation":
                                     "$projectDir/schemas".toString()]
            }
        }
    }
   ...
    // work-runtime-ktx 2.1.0 and above now requires Java 8
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    // work-runtime-ktx 2.1.0 and above now requires Java 8
    kotlinOptions {
        jvmTarget = "1.8"
    }
    
    //Kotlin-Android-Extensions
    androidExtensions {
        experimental = true
    }
}

dependencies {

    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$rootProject.kotlinVersion"
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation "androidx.fragment:fragment-ktx:1.2.0-rc03"
    implementation 'androidx.core:core-ktx:1.1.0'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'androidx.recyclerview:recyclerview:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'

    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0-rc03"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0-rc03"
    implementation "androidx.navigation:navigation-fragment-ktx:2.1.0"
    implementation "androidx.navigation:navigation-ui-ktx:2.1.0"

    kapt "androidx.room:room-compiler:2.2.2"
    implementation "androidx.room:room-runtime:2.2.2"
    implementation "androidx.room:room-ktx:2.2.2"

    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.2"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2"

    implementation "androidx.work:work-runtime-ktx:2.2.0"

    implementation 'com.google.android.material:material:1.0.0'
    implementation 'com.orhanobut:logger:2.2.0'
    implementation "com.google.code.gson:gson:2.8.5"
    implementation "com.github.bumptech.glide:glide:4.10.0"

    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
    androidTestImplementation 'com.android.support.test.espresso:espresso-contrib:3.0.2'
    androidTestImplementation 'com.android.support.test:rules:1.0.2'
    androidTestImplementation "androidx.work:work-testing:2.2.0"
}
~~~









