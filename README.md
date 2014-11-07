Biu
=======

分工
-------
* **客户端**: 张焯琦 曾展鹏
* **服务端**: 李雨足 潘锦博

通信协定
-------
服务端和客户端通信中，请求一律采用**HTTP**协议的**POST**方法

通信的内容(Body of Request and Response)一律采用**JSON**格式进行封装

极长慎点
[精通HTTP协议](http://datatracker.ietf.org/doc/rfc2616)

项目依赖
-------

###[JPush极光推送](https://www.jpush.cn)
通过推送实现好友添加、聊天等功能

###服务器
个人比较熟悉[京东云擎](http://jae.jd.com),免费可用

另一个免费的是[新浪云SAE](http://sae.sina.com/cn)，也是免费不过要实名申请

###Python [Django框架](https://www.djangoproject.com)
###[安卓SDK](http://developer.android.com)
###[百度定位SDK](http://developer.baidu.com/map/index.php?title=android-locsdk)


主要功能及其技术实现
-------

### 用户注册 登陆 添加好友等
建库建表 ORM

**ps** 我们采用一个服务对应一个url

```python
urlpatterns = patterns(",
    (r'^register$', 'biu.user.register'),
    (r'^login$', 'biu.user.login'),
    # and so on
)
```

### 好友寻找
客户端会定时通知服务端其位置信息(经纬度)，服务端保留所有用户位置信息

当客户端发起好友寻找请求时，服务端就可以返回符合条件的用户

### 聊天
采用JPush提供的推送服务实现聊天，轻松简便
[](https://raw.github.com/sysu2012zzp/Biu/master/pushtask_architecture.png)

### 软件锁
I don't know what is this

### 安全聊天

