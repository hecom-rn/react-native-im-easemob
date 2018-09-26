//
//  RNViewController.h
//  EaseMob-Example
//
//  Created by Xiaosong Gao on 2018/6/14.
//  Copyright © 2018年. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RNViewController: UIViewController

- (instancetype)initWithJS:(NSString *)moduleName props:(NSDictionary *)props;

@end
