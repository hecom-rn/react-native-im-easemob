# react-native-im-easemob

[![npm version](https://img.shields.io/npm/v/react-native-im-easemob.svg?style=flat)](https://www.npmjs.com/package/react-native-im-easemob)
[![Build Status](https://travis-ci.org/RNCommon/react-native-im-easemob.svg?branch=master)](https://travis-ci.org/RNCommon/react-native-im-easemob)

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

```perl
source 'https://github.com/CocoaPods/Specs.git'
platform :ios, '8.0'

def common_target
    pod 'MJExtension', :git => 'https://github.com/RNCommon/MJExtension.git', :commit => 'cd3de71c4955935a762a46e59d90160991f5fa92'
    // React target and other common target...
end

target "MainTarget" do
    common_target
    pod 'react-native-im-easemob', :podspec => '../node_modules/react-native-im-easemob/react-native-im-easemob.podspec'
    pod 'Hyphenate', '= 3.5.1'
end

target "DeployTarget" do
    common_target
    pod 'react-native-im-easemob/Deploy', :podspec => '../node_modules/react-native-im-easemob/react-native-im-easemob-deploy.podspec'
    pod 'HyphenateDeploy', '= 3.5.1'
end
```

其中MainTarget是真机和模拟器通用的部署Target，主要用于调试。DeployTarget是只支持armv7和arm64的Target，主要用于打包发布。

这是因为环信SDK的原因，`Hyphenate`打包需要使用`lipo`来处理`Hyphenate.framework`，从中剔除i386和x86_64的模拟器框架。具体原因请参照环信文档。

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

Android离线推送接收消息，需要配置appId和appKey或者appId和AappSecret
(不同的手机需要配置的不一样，具体参考环信文档：http://docs-im.easemob.com/im/android/push/thirdpartypush)
在主工程中的AndroidManifest.xml中配置appId和appKey或者appId和AappSecret
```
 <!-- start  离线推送配置appId和appSecret(小米，oppo,meizu)-->
        <meta-data
            android:name="IM_EASEMOB_XIAOMI_PUSH_APP_ID"
            android:value="替换成你自己申请的小米APP_ID" />
        <meta-data
            android:name="IM_EASEMOB_XIAOMI_PUSH_APP_KEY"
            android:value="替换成你自己申请的小米APP_KEY" />

        <meta-data
            android:name="IM_EASEMOB_OPPO_PUSH_APP_KEY"
            android:value="替换成你自己申请的OPPO的APP_KEY" />
        <meta-data
            android:name="IM_EASEMOB_OPPO_PUSH_APP_SECRET"
            android:value="替换成你自己申请的OPPO的APP_SECRET" />

        <meta-data
            android:name="IM_EASEMOB_MEIZU_PUSH_APP_ID"
            android:value="替换成你自己申请的魅族的APP_ID" />
        <meta-data
            android:name="IM_EASEMOB_MEIZU_PUSH_APP_KEY"
            android:value="替换成你自己申请的魅族的APP_KEY" />
        <!--end      离线推送配置appId和appSecret(小米，oppo,meizu)-->
```