# EDrop

## 项目背景

	    现如今，人们生活水平不断提高，产生的垃圾也越来越多，而进行垃圾分类管理能将这些垃圾转化为新能源，同时能让这些垃圾得到有效的处理，这样能减少对土壤的危害性，同时还能防止出现污染空气的现象。
	
	     在生活中的垃圾无处不在，可将这些垃圾划分为不等的类别，其中主要是将这些垃圾分为可回收垃圾、有害垃圾、厨余垃圾、其它垃圾等，这样能将这些垃圾得到有效的处理，也可以针对于不同的垃圾采用不同的处理方式。通过对垃圾进行分类管理，能最大限度的实现垃圾资源回收利用，同时减少垃圾处理的数量，可以通过垃圾费分类再进行人工处理转化为新能源。
	
	    同时在生活中很多垃圾都不是能进行自行分解的，从而产生很多的有毒物质，这样会给土壤进行严重的污染，从而导致农产物的产量逐渐下降，也一定的导致动物出现死亡的现象，进行分类处理能有效的减少危害性。

## 项目简介

       基于上述背景，de_dust2项目实训小组协作开发一款APP-易扔，帮助用户进行垃圾分类，同时帮助用户进行代扔处理，减轻用户生活负担，缓解用户压力，真正做到更好、更快、更高效的生活。

## 团队成员

- 张浩宇：
- 康晓慧：
  - 主要负责安卓端的开发与维护（大部分）
  - 运用各种样式进行前端界面的搭建
  - 重构环信聊天界面，使其更人性化
  - 添加各种动画效果，增加其美观性
  - 完成社区页面，完成Markdown的解析工作
  - 社区文章可点赞，可评论，可分享
- 张晨旭：
  - 主要负责安卓服务器端的开发与维护（全部）
  - 数据库的设计与维护（全部）
  - 后台管理系统服务器端的开发与维护（部分：登录、数据可视化的数据维护）
  - 后台管理系统数据可视化显示
  - 社区评论敏感词过滤（自己实现，基于 AC 自动机实现）
  - 数据仿真（模拟用户登录、注册及服务范围统计）
  - 数据库自动备份
  - 请求日志记录自动备份
  - 极光推送主动像安卓端推送消息
  - JUnit 进行单元测试
- 刘潭：
- 王明杰：
- 李诗凡：

## 安卓前端主要技术

1. QQ 用户进行登陆--完善信息
2. 使用MobTech技术进行手机验证码登陆
3. 轮播图fresco进行垃圾分类宣传
4. UniversalToast进行Toast的封装
5. LoadingDialog，Glide进行动画的加载
6. TextInputLayout进行输入框的动画封装
7. 使用悬浮按钮快速切换各种功能
8. SmartRefreshLayout进行页面的刷新
9. EventBus，Gson，OkHttp进行数据的传递
10. 侧边栏存储用户的基本信息以及软件的各种设置
11. 三级联动进行页面的优化处理
12. 极光推送，推送各种信息
13. 环信，进行人员的通信（文字聊天，视频聊天，音频聊天）

## 后台管理系统主要技术

1. HTML + JS + CSS 搭建后台管理系统前端界面
2. Ajax 异步请求数据.
3. ECharts 实现数据可视化显示

## 服务器端主要技术

1. 服务器端框架：基于 Spring + SpringMVC + MyBatis
2. 数据库：MySQL
3. C3P0 数据库连接池
4. LOG4J 记录日志并自动备份
5. 自动备份近一周数据库（每隔 1 小时备份一次）
6. 基于 JUnit 4 进行单元测试
7. 基于 AC 自动机实现敏感词过滤算法
8. 集成极光推送自动向安卓端推送消息

## 主要功能

#### 代扔功能

预约、聊天、接单、工作、完成

#### 垃圾分类学习功能

拍照识别垃圾信息、文字实时搜索垃圾、垃圾分类消息、垃圾分类答题、浏览环保类文章

## 具体流程

### 安卓客户端

1. 注册（注：以下三种注册方式注册成功均需到个人中心进行信息完善）
   - 用户名密码注册
   - 手机验证码注册
   - QQ一键登录注册
2. 登录（注：第一次登录完成后，无需再次登录，可自动登录）
   - 用户名密码登录
   - 手机验证码登录
   - QQ一键登录
   - __忘记密码可通过手机验证码进行找回
3. 拍照识别
   - 首先拍下要识别的垃圾
   - 上传进行识别
   - 显示垃圾信息
4. 文字搜索识别
   - 文字手动搜索垃圾
   - 显示垃圾信息
5. 预约代扔
   - 填写用户信息
   - 等待接单
6. 环信聊天	
   - 添加好友
     - 悬浮按钮搜索代扔信息
     - 添加用户
     - 联系人列表显示
     - 进行聊天
       - 视频聊天
       - 文字聊天
       - 语音聊天
   - 删除好友
     - 联系人列表
     - 长按进行删除
   - 同意好友邀请
   - 拒绝好友邀请
7. 服务详情
   - 当前预约单
   - 历史预约单
8. 极光推送
   - 预约时进行消息推送
   - 成功后进行消息推送
   - 添加好友进行消息推送
   - 添加成功进行消息推送
   - 添加失败进行消息推送
9. 垃圾分类大作战
   - 进行垃圾分类答题
   - 答题完成后得分
   - 评判垃圾信息分类是否合格
10. 社区信息推送
    - 定时进行垃圾信息投送
    - 可点赞
    - 可评论

### 后台管理系统

略

### 项目展示PPT
![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片1.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片2.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片3.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片4.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片5.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片6.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片7.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片8.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片9.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片10.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片11.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片12.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片13.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片14.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片15.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片16.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片17.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片21.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片22.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片18.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片19.JPG)

![](https://gitee.com/LYmystery/PicGo/raw/master/image/幻灯片20.JPG)
