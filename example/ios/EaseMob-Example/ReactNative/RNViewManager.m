//
//  RNViewManager.m
//  EaseMob-Example
//
//  Created by Xiaosong Gao on 2018/6/14.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import "RNViewManager.h"

static NSMutableArray<RNViewController *> *vcsArray;

@implementation RNViewManager

+ (void)pushToJS:(NSString *)fileName
           props:(NSDictionary *)props
            navi:(UINavigationController *)navi {
    RNViewController *vc = [self generateViewController:fileName props:props];
    vc.hidesBottomBarWhenPushed = YES;
    [navi pushViewController:vc animated:YES];
}

+ (RNViewController *)generateViewController:(NSString *)fileName
                                       props:(NSDictionary *)props {
    if (vcsArray == nil) {
        vcsArray = [NSMutableArray array];
    }
    RNViewController *vc = [[RNViewController alloc] initWithJS:fileName props:props];
    [vcsArray addObject:vc];
    return vc;
}

+ (RNViewController *)currentViewController {
    if (vcsArray.count <= 0) {
        return nil;
    }
    return [vcsArray lastObject];
}

+ (void)clear {
    [vcsArray removeAllObjects];
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(pop:(NSString *)params) {
    RNViewController *vc = [RNViewManager currentViewController];
    if (vc) {
        [vcsArray removeLastObject];
        dispatch_async(dispatch_get_main_queue(), ^{
            UIViewController *lastVC = [vc.navigationController.viewControllers objectAtIndex:(vc.navigationController.viewControllers.count - 2)];
            if ([lastVC respondsToSelector:NSSelectorFromString(@"onBackAction:")]) {
                [lastVC performSelectorOnMainThread:NSSelectorFromString(@"onBackAction:") withObject:params waitUntilDone:YES];
            }
            [vc.navigationController popViewControllerAnimated:YES];
        });
    }
}

@end
