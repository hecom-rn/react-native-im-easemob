/*!
 *  \~chinese
 *  @header EMCallConference.h
 *  @abstract 多人会议
 *  @author Hyphenate
 *  @version 3.00
 *
 *  \~english
 *  @header EMCallConference.h
 *  @abstract COnference
 *  @author Hyphenate
 *  @version 3.00
 */

#import <Foundation/Foundation.h>

/*!
 *  \~chinese
 *  多人会议对象
 *
 *  \~english
 *  Conference class
 */
@interface EMCallConference : NSObject

/*!
 *  \~chinese
 *  会话标识符, 本地生成
 *
 *  \~english
 *  Unique call id, locally generated
 */
@property (nonatomic, strong, readonly) NSString *callId;

/*!
 *  \~chinese
 *  会议标识符,服务器生成
 *
 *  \~english
 *  Unique conference id, server generation
 */
@property (nonatomic, strong, readonly) NSString *confId;

/*!
 *  \~chinese
 *  通话本地的username
 *
 *  \~english
 *  Local username
 */
@property (nonatomic, strong, readonly) NSString *localName;

@end
