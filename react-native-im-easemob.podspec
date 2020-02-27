require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = 'react-native-im-easemob'
  s.version      = package['version']
  s.summary      = package['description']
  s.authors      = { "Xiaosong Gao" => "gaoxiaosong06@gmail.com" }
  s.homepage     = package['homepage']
  s.license      = package['license']
  s.platform     = :ios, "8.0"
  s.source       = { :git => "https://github.com/hecom-rn/react-native-im-easemob.git" }
  
  s.subspec "Dev" do |ss|
    ss.source_files = 'ios/**/*.{h,m}'
    ss.dependency 'React'
    ss.dependency 'MJExtension'
    ss.dependency 'Hyphenate'
  end

  s.subspec "Deploy" do |ss|
    ss.source_files = 'ios/**/*.{h,m}'
    ss.dependency 'React'
    ss.dependency 'MJExtension'
    ss.dependency 'HySDKDeploy'
  end
  
end