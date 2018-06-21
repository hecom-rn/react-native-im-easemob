Pod::Spec.new do |s|
  s.name         = "RNEaseMob"
  s.version      = "0.0.1"
  s.summary      = "EaseMob IM iOS Interfaces"
  s.homepage     = "https://github.com/RNCommon/react-native-easemob"
  s.license      = "GPL-3.0-only"
  s.author             = { "Xiaosong Gao" => "gaoxiaosong06@gmail.com" }
  s.platform     = :ios, "8.0"
  s.source       = { :git => "https://github.com/RNCommon/react-native-easemob.git", :tag => s.version }
  s.source_files  = "ios", "ios/easemob", "ios/util"
  s.exclude_files = ""
  # s.public_header_files = "Classes/**/*.h"
  # s.resource  = "icon.png"
  # s.resources = "Resources/*.png"
  # s.preserve_paths = "FilesToSave", "MoreFilesToSave"
  s.frameworks = "MobileCoreServices", "CFNetwork"
  s.libraries = "sqlite3", "stdc++.6.0.9", "z", "iconv", "resolv", "xml2"
  # s.requires_arc = true
  # s.xcconfig = { "HEADER_SEARCH_PATHS" => "$(SDKROOT)/usr/include/libxml2" }
end
