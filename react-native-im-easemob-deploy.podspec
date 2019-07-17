require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = 'react-native-im-easemob-deploy'
  s.version      = package['version']
  s.summary      = package['description']
  s.authors      = { "Xiaosong Gao" => "gaoxiaosong06@gmail.com" }
  s.homepage     = package['homepage']
  s.license      = package['license']
  s.platform     = :ios, "8.0"
  s.source       = { :git => "https://github.com/hecom-rn/react-native-im-easemob.git" }
  s.source_files = 'ios/**/*.{h,m}'
  s.dependency 'React'
  s.dependency 'MJExtension'
  s.dependency 'HyphenateDeploy'
end