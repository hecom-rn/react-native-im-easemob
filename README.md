# react-native-easemob

[![Build Status](https://travis-ci.org/HecomBJ/react-native-easemob.svg?branch=master)](https://travis-ci.org/HecomBJ/react-native-easemob)
[![React Native](https://img.shields.io/badge/react%20native-0.52.2-brightgreen.svg)](https://github.com/facebook/react-native)
[![License](https://img.shields.io/aur/license/yaourt.svg)](https://github.com/HecomBJ/react-native-easemob/blob/master/LICENSE)

环信IM的原生接口React-Native封装库。

## 配置Git-LFS

下载开发时，需要先配置环境。

在OS X上使用Homebrew安装Git-LFS:

```
brew install git-lfs
```

如果仓库先进行clone操作，然后安装的Git-LFS，则需要进行LFS的设置:

```
cd [PROJECT_DIR_PATH]
git lfs install
git lfs pull origin
```

## 安装

使用Yarn安装:

```
yarn add react-native-easemob
```

使用npm安装:

```
npm install --save react-native-easemob
```

## iOS环境设置

在主工程中，右键添加node_modules/react-native-easemob/ios/RNEaseMob.xcodeproj。

向Build → Link Binary With Libraries中添加依赖库：

* MobileCoreServices.framework
* CFNetwork.framework
* libsqlite3.tbd
* libstdc++.6.0.9.tbd
* libz.tbd
* libiconv.tbd
* libresolv.tbd
* libxml2.tbd

向Build Settings → Linking → Other Linker Flags中添加-ObjC。