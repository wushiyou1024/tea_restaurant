# tea_restaurant

#### 介绍
茶餐厅外卖点单系统后端工程
前端项目地址(含微信小程序和商家管理后台)：https://github.com/wsy12123/tea_restaurant_front.git
#### 软件架构
项目后端使用springboot进行开发，其中使用redis进行缓存。使用springcache进行redis缓存时候的管理。使用shardingjdbc完成mysql主从分离的设置。使用swagger生成开发文档。
前端商家平台使用vue2.x脚手架开发，使用elementui开发前端ui。使用axios获取api，使用vue.router进行路由
前端用户界面使用uniapp开发,使用vuex做组件数据存储。项目使用nginx进行反向代理，使用阿里云短信服务 完成前台用户的登录。
使用了websocket完成小程序用户下单之后商家收到订单提醒 并播放语言提醒。
#### 安装教程

1. 需要准备至少两台服务器，安装mysql、nginx、redis服务
2. mysql需要先配置好主从分离，然后在application.xml 修改你的相关ip
3. 前端需要安装node.js。详见vue官网。

#### 项目部分截图-用户小程序部分

![image](https://github.com/wsy12123/img/blob/master/b%20(6).jpg)

![image](https://github.com/wsy12123/img/blob/master/b%20(5).jpg)

![image](https://github.com/wsy12123/img/blob/master/b%20(9).jpg)

![image](https://github.com/wsy12123/img/blob/master/b%20(10).jpg)

![image](https://github.com/wsy12123/img/blob/master/b%20(8).jpg)

![image](https://github.com/wsy12123/img/blob/master/b%20(7).jpg)

![image](https://github.com/wsy12123/img/blob/master/b%20(4).jpg)

![image](https://github.com/wsy12123/img/blob/master/b%20(3).jpg)

![image](https://github.com/wsy12123/img/blob/master/b%20(2).jpg)

![image](https://github.com/wsy12123/img/blob/master/b%20(1).jpg)

