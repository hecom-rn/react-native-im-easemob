require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = package['name']
  s.version      = package['version']
  s.summary      = package['description']
  s.authors      = { "Xiaosong Gao" => "gaoxiaosong06@gmail.com" }
  s.homepage     = package['homepage']
  s.license      = package['license']
  s.platform     = :ios, "8.0"
  s.source       = { :git => "https://github.com/RNCommon/react-native-im-easemob.git" }
  s.source_files = 'ios/**/*.{h,m}'
  s.dependency 'React'
  s.dependency 'MJExtension'
  s.default_subspec = 'Default'

  s.subspec 'Default' do |ss|
    ss.dependency 'Hyphenate'
  end

  s.subspec 'Deploy' do |ss|
    ss.dependency 'HyphenateDevice'
  end
end