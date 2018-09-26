//
//  RNViewController.m
//  EaseMob-Example
//
//  Created by Xiaosong Gao on 2018/6/14.
//  Copyright © 2018年. All rights reserved.
//

#import "RNViewController.h"
#import <React/RCTRootView.h>
#import "RNBridgeModuleManager.h"

@interface RNViewController ()

@property (nonatomic, copy) NSString *moduleName;
@property (nonatomic, strong) NSDictionary *props;
@property (nonatomic, strong) RCTRootView *preparedView;

@end

@implementation RNViewController

- (instancetype)initWithJS:(NSString *)moduleName props:(NSDictionary *)props {
    self = [super init];
    if (self) {
        self.moduleName = moduleName;
        self.props = props;
        self.preparedView = [RNBridgeModuleManager generateView:self.moduleName props:self.props];
        self.preparedView.backgroundColor = [UIColor whiteColor];
    }
    return self;
}

- (void)loadView {
    self.view = self.preparedView;
}

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:NO];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:NO];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

@end
