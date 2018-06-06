/*!
 *  \~chinese
 *  @header EMCallStream.h
 *  @abstract 数据流
 *  @author Hyphenate
 *  @version 3.00
 *
 *  \~english
 *  @header EMCallStream.h
 *  @abstract Stream
 *  @author Hyphenate
 *  @version 3.00
 */

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

/*!
 *  \~chinese
 *  数据流类型
 *
 *  \~english
 *  Stream type
 */
typedef enum {
    EMStreamTypeNormal = 0,    /*! \~chinese 默认，视频或者语音 \~english Default, video or audio */
    EMStreamTypeDesktop,        /*! \~chinese 分享桌面 \~english Share desktop */
} EMStreamType;

/*!
 *  \~chinese
 *  数据流
 *
 *  \~english
 *  Stream
 */
@interface EMCallStream : NSObject

/*!
 *  \~chinese
 *  数据流标识符
 *
 *  \~english
 *  Unique stream id
 */
@property (nonatomic, strong, readonly) NSString *streamId;

/*!
 *  \~chinese
 *  数据流名称
 *
 *  \~english
 *  Stream name
 */
@property (nonatomic, strong, readonly) NSString *streamName;

/*!
 *  \~chinese
 *  上传者的成员名称
 *
 *  \~english
 *  Member name of the publisher
 */
@property (nonatomic, strong, readonly) NSString *memberName;

/*!
 *  \~chinese
 *  上传者环信ID
 *
 *  \~english
 *  Hyphenate ID of the publisher
 */
@property (nonatomic, strong, readonly) NSString *userName;

/*!
 *  \~chinese
 *  音频是否可用
 *
 *  \~english
 *  Whether the audio is available
 */
@property (nonatomic, readonly) BOOL enableVoice;

/*!
 *  \~chinese
 *  视频是否可用
 *
 *  \~english
 *  Whether the video is available
 */
@property (nonatomic, readonly) BOOL enableVideo;

/*!
 *  \~chinese
 *  扩展信息
 *
 *  \~english
 *  Extended Information
 */
@property (nonatomic, strong, readonly) NSString *ext;

/*!
 *  \~chinese
 *  数据流类型
 *
 *  \~english
 *  Stream type
 */
@property (nonatomic) EMStreamType type;

@end


/*!
 *  \~chinese
 *  上传数据流时的属性对象
 *
 *  \~english
 *  The config object when the stream is published
 */
@class EMCallLocalView;
@interface EMStreamParam : NSObject

/*!
 *  \~chinese
 *  数据流名称
 *
 *  \~english
 *  Stream name
 */
@property (nonatomic, copy) NSString *streamName;

/*!
 *  \~chinese
 *  视频是否可用，默认NO
 *
 *  \~english
 *  Whether the video is available, the default NO
 */
@property (nonatomic) BOOL enableVideo;

/*!
 *  \~chinese
 *  是否静音，默认NO
 *
 *  \~english
 *  Whether the mute, the default NO
 */
@property (nonatomic) BOOL isMute;

/*!
 *  \~chinese
 *  扩展信息
 *
 *  \~english
 *  Extended Information
 */
@property (nonatomic, copy) NSString *ext;

/*!
 *  \~chinese
 *  显示本地视频的页面
 *
 *  \~english
 *  The view of displays the local video
 */
@property (nonatomic, strong) EMCallLocalView *localView;

/*!
 *  \~chinese
 *  是否自定义视频数据，默认NO
 *
 *  \~english
 *  Whether to customize the video data, the default NO
 */
@property (nonatomic) BOOL enableCustomizeVideoData;

/*!
 *  \~chinese
 *  数据流类型
 *
 *  \~english
 *  Stream type
 */
@property (nonatomic) EMStreamType type;

/*!
 *  \~chinese
 *  分享桌面时要显示的页面
 *
 *  \~english
 *  The view to display when sharing the desktop
 */
@property (nonatomic, strong) UIView *desktopView;

/*!
 *  \~chinese
 *  初始化方法
 *
 *  @param aStreamName    数据流名称
 *
 *  @result 数据流实例
 *
 *  \~english
 *  Initialization method
 *
 *  @param aStreamName    Stream name
 *
 *  @result Stream instance
 */
- (instancetype)initWithStreamName:(NSString *)aStreamName;

@end
