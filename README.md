# react-native-im-easemob

环信IM的原生接口React-Native封装库。

## 安装

使用Yarn安装:

```
yarn add react-native-im-easemob
```

使用npm安装:

```
npm install --save react-native-im-easemob
```

## example工程设置

下载后，在example目录中执行npm install或者yarn，而不要在根目录下安装node_modules。

## iOS环境设置

添加Podfile，类似如下格式：

```
source 'https://github.com/CocoaPods/Specs.git'
platform :ios, '8.0'

target "MainProject" do
    pod 'Hyphenate', '= 3.5.1'
end
```

在主工程中，右键添加ios目录中的所有源文件。

向Build → Link Binary With Libraries中添加依赖库：

* MobileCoreServices.framework
* CFNetwork.framework
* libsqlite3.tbd
* libc++.tbd
* libz.tbd
* libiconv.tbd
* libresolv.tbd
* libxml2.tbd

向Build Settings → Linking → Other Linker Flags中添加`-ObjC -lc++`。

在Build Settings中的General中，在Embedded Binaries中添加Hyphenate.framework。

打包需要使用`lipo`来处理`Hyphenate.framework`，从中剔除i386和x86_64的模拟器框架。请参照环信文档进行操作。

## Android环境设置

在settings.gradle文件中添加：
```
include ':react-native-im-easemob'
project(':react-native-im-easemob').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-im-easemob/android')
```
在module级的build.gradle中添加：
```
dependencies {
    implementation project(':react-native-im-easemob')
}
```
在ReactNativeHost中添加：
```
import com.im.easemob.EasemobPackage;

@Override
protected List<ReactPackage> getPackages() {
    return Arrays.<ReactPackage>asList(
        new EasemobPackage()
    );
}
```