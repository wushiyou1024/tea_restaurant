# tea_restaurant

#### 介绍
茶餐厅外卖点单系统

#### 软件架构
项目后端使用springboot进行开发，其中使用redis进行缓存。使用springcache进行redis缓存时候的管理。使用shardingjdbc完成mysql主从分离的设置。使用swagger生成开发文档。
前端商家平台使用vue2.x脚手架开发，使用elementui开发前端ui。使用axios获取api，使用vue.router进行路由
前端用户界面使用uniapp开发,使用vuex做组件数据存储。项目使用nginx进行反向代理，使用阿里云短信服务 完成前台用户的登录。
使用了websocket完成小程序用户下单之后商家收到订单提醒 并播放语言提醒。
#### 安装教程

1. 需要准备至少两台服务器，安装mysql、nginx、redis服务
2. mysql需要先配置好主从分离，然后在application.xml 修改你的相关ip
3. 前端需要安装node.js。详见vue官网。



