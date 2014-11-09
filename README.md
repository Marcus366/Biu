Biu
=======

分工
-------
* **客户端**: 张焯琦 曾展鹏
* **服务端**: 李雨足 潘锦博

任务顺序
-------

*先完成需要两端通信的任务*

1. 找适合的云服务器(不一定是下面介绍的，能用就行)，架设好平台,创建所有空功能。根据下面的通信细节，所有功能不实现细节，直接返回基本数据。

2. 优先实现注册功能, 登陆功能

3. Biu式加好友

4. 聊天

…

*所有功能的实现细节在下面有详细描述*

通信协定
-------
服务端和客户端通信中

请求一律采用**HTTP**协议的**POST**方法

返回的内容(Body of Response)一律采用**JSON**格式进行封装

极长慎点
[精通HTTP协议](http://datatracker.ietf.org/doc/rfc2616)

项目依赖
-------

###1.[JPush极光推送](https://www.jpush.cn)
通过推送实现好友添加、聊天等功能

* [服务端python SDK下载](https://github.com/jpush/jpush-api-python-client)
* [客户端Android SDK集成指南](http://doc.jpush.cn/pages/viewpage.action?pageId=557214)

###2.服务器(请服务端众记得先搞定这个)
个人比较熟悉[京东云擎](http://jae.jd.com),免费可用

另一个免费的是[新浪云SAE](http://sae.sina.com/cn)，也是免费不过要实名申请

###3.Python [Django框架](https://www.djangoproject.com)

###4.[安卓SDK](http://developer.android.com)

###5.[百度定位SDK](http://developer.baidu.com/map/index.php?title=android-locsdk)


主要功能及其技术实现
-------
因为分客户端和服务端进行分工，所以该栏目比较强调通信的内容和细节，便于以后两方接入
时接口不一致导致工作量增大，仅仅客户端的功能(软件锁、安全聊天)容后再议

**注意: 请服务端针对每一个服务添加测试样例————即自己喂数据给自己进行测试**

### 用户注册 登陆 添加好友等
建库建表 使用Django的ORM框架搞定对象映射

ps: 为了方便起见，我们采用一个服务对应一个url

```python
urlpatterns = patterns(",
    (r'^register$', 'biu.user.register'),
    (r'^login$', 'biu.user.login'),
    (r'^logout$', 'biu.user.logout'),
    (r'^heartbeat$', 'biu.user.heartbeat'),
    # and so on
)
```

通信细节：
* register:

request: username, nickname, password(with MD5), etc 用户还需要什么属性可以再讨论一下

response: code(0 => ok, 1 => username already exist, 2 … etc)

* login:

request: username, passowrd(with MD5)

response: code(0 => ok, 1 => username not exist, 2 => password not correct)

* logout:

request: username

response: code(0 => ok, 1 => fail)


### 好友寻找
客户端会定时通知服务端其位置信息(经纬度)，服务端保留所有用户位置信息

当客户端发起好友寻找请求时，服务端就可以返回符合条件的用户

通信细节：
* heartbeat: (every 1min or 30s will send this request)

request: username, longitude, latitude

response: empty response is ok

* search:

request: username

response: a json list with following format

```json
{
  "count": 3,
  "users": [
    {"nickname": "Judy", "etc attributes"},
    {"nickname": "Gay", "etc attributes"},
    {"nickname": "Fuck", "etc attributes"}
  ]
}
```

### 聊天
采用JPush提供的推送服务实现聊天，轻松简便
![pushtalk](https://raw.githubusercontent.com/sysu2012zzp/Biu/master/pushtalk_architecture.png)

图片解析：当用户A对用户B发出一句话时，用户A向服务器发出请求，服务器向极光推送发出推送请求，由极光负责把信息推送至Ｂ

Why JPush: 极光推送能够保存离线消息，能够做到离线聊天。

### 软件锁
I don't know what is this

### 安全聊天

