
XMUAssistant - 厦大助手（暂定名）
====

#1. 特色功能
1. 自动查询电费
2. 自动查询学生卡余额
3. 自动获取教务处新闻信息并推送
4. 展示当前时间段的课表（未完成）

#2. 需要修复的bug
1. 每次重新打开软件都需要重新登录
2. 输入用户名学号之后没有对服务器返回信息进行校验，若是输入错误，app错误退出
3. 执行获取新闻的时间方法时间太长，在模拟器上运行崩溃
4. 主界面只能在特定的地方进行左右滑动操作
5. 课表功能的实现
6. （致命bug）默认的权限设置问题，第一次打开软件之后就会fc,fix soon.
#3. 需要改进的地方
1. 侧滑栏界面外观功能等等（建议使用系统自带的Navigation Drawer重写）
2. 所有方法获取的信息使用minisql或者配置文件存储，读取速度更快
3. 做到下滑刷新当前页面信息
4. 做到定时定点查询学生卡电费余额，并且能够正确推送信息
5. 主页上的card可点击范围更大
6. 侧滑栏可以加入用户名和头像
7. 登录界面泛空，可以更好的设计
8. app启动时候跳帧过于严重，界面代码需要优化
9. 绑定宿舍的页面使用Activity，这样返回键可以正常使用
10. 宿舍电费和学生卡信息展示使用分开的card

#4. 计划添加的内容
1. （待更新）
