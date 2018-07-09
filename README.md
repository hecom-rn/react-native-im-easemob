# react-native-easemob

[![Build Status](https://travis-ci.org/RNCommon/react-native-easemob.svg?branch=master)](https://travis-ci.org/RNCommon/react-native-easemob)
[![React Native](https://img.shields.io/badge/react%20native-0.52.2-brightgreen.svg)](https://github.com/facebook/react-native)
[![License](https://img.shields.io/aur/license/yaourt.svg)](https://github.com/RNCommon/react-native-easemob/blob/master/LICENSE)

环信IM的原生接口React-Native封装库。

## 安装

使用Yarn安装:

```
yarn add react-native-easemob
```

使用npm安装:

```
npm install --save react-native-easemob
```

## example工程设置

下载后，在example目录中执行npm install或者yarn，而不要在根目录下安装node_modules。

## iOS环境设置

添加Podfile，类似如下格式：

```
source 'https://github.com/CocoaPods/Specs.git'
platform :ios, '8.0'

target "MainProject" do
    pod 'Hyphenate', '= 3.4.2'
end
```

在主工程中，右键添加ios目录中的所有源文件。

向Build → Link Binary With Libraries中添加依赖库：

* MobileCoreServices.framework
* CFNetwork.framework
* libsqlite3.tbd
* libstdc++.6.0.9.tbd
* libz.tbd
* libiconv.tbd
* libresolv.tbd
* libxml2.tbd

向Build Settings → Linking → Other Linker Flags中添加-ObjC -lc++。

在Build Settings中的General中，在Embedded Binaries中添加Hyphenate.framework。

## Android环境设置

在settings.gradle文件中添加：
```
include ':react-native-hecom-easemob'
project(':react-native-hecom-easemob').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-hecom-easemob/android')
```
在module级的build.gradle中添加：
```
dependencies {
    implementation project(':react-native-hecom-easemob')
}
```
在ReactNativeHost中添加：
```
import com.hecom.easemob.EasemobPackage;

@Override
protected List<ReactPackage> getPackages() {
    return Arrays.<ReactPackage>asList(
        new EasemobPackage()
    );
}
```