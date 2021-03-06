# 时间安排

| 进度                                     | 完成状态   |
| ---------------------------------------- | ---------- |
| **书本一：《图解HTTP》**                 |            |
| 阅读《图解HTTP》 1-3章节、笔记整理       | done-done  |
| 阅读《图解HTTP》 4-6章节、笔记整理       | done-todo  |
| 阅读《图解HTTP》 7-9章节、笔记整理       | done-todo  |
| 阅读《图解HTTP》 10-11章节、笔记整理     | done-todo  |
| **书本二：《图解TCP/IP》**               |            |
| 阅读《图解TCP/IP》第一章节、笔记整理     | done-done  |
| 阅读《图解TCP/IP》第二章节、笔记整理     | done-done  |
| 阅读《图解TCP/IP》第三章节、笔记整理     | done-done  |
| 阅读《图解TCP/IP》第四、五章节、笔记整理 | done-done  |
| 阅读《图解TCP/IP》第六、七章节、笔记整理 | done-done  |
| 阅读《图解TCP/IP》第八、九章节、笔记整理 | done-doing |
| 面试热点整理                             |            |
|                                          |            |
|                                          |            |

tcp、ip远比想象的速度慢，知识点比较多

# 书本一、图解HTTP

## 第一章

### 1. 三项WWW构建技术

	- SGML: Standard Generalized Markup Language 标准通用标记语言（HTML mark language）
	- HTTP: HyperText Transfer Protocol 超文本传输协议
	- URL: Uniform Resource Locator 统一资源定位符

### 2. 四个分层对应的协议

- **应用层：HTTP/DNS（域名解析）/FTP/Telnet/SMTP**

- **传输层: TCP/UDP=>可靠性保证机制**

  - 传输到此层会添加TCP首部

- **网络层(包，最小数据单位): IP(IP4/IP6)/ARP/ICMP**

  - 传输到此层会添加IP首部
  - **根据ARP可以将IP地址反查出MAC地址**

- **数据链路层: 以太网，无线LAN(local area network)**

  - 传输到此层会添加以太网首部

  - mac地址是在物理层，是网卡地址

### 3. 确保可靠性的TCP协议

​	为方便传输，将大块数据分割成以报文段（segment）为单位的数据包进行管理，**可靠服务**指的是确保数据准确可靠传达给对方。

​	为了保证准确传输到目标出，TCP协议采用了三次握手策略。使用握手标志 SNY(synchronized) 和 ACK(acknowledmet)

​	![tcp可靠性传输-三次握手](http-image\tcp可靠性传输-三次握手.png)

### 4. URI和URL

	- URI: uniform resource identify 统一资源标识符
 - URL: uniform resource locator 统一资源定位符

URI 格式：

​	http://user:pw@www.localhost:8080/index.html?username=wuyuefeng#ch1

	- **http:// 协议方案名**
	- user:pw登陆信息
	- www.localhost 服务地址
	- 8080 服务端口号
	- **index.html 带层次文件路径**
	- **username=wuyuefeng 查询字符串**
	- **ch1 片段标识符**

## 第二章

### 1. 请求与相应

- 请求

  GET / HTTP/1.1

  HOST: hackr.jp

  - GET 请求方法
  - HTTP/1.1 协议版本
  - Host：... 请求首部字段

- 响应

  HTTP/1.1 200 OK

  Date: ...

  Content-length:..

  \<html>

  - 200 响应码
  - OK 原因短语
  - header带的信息：相应首部字段
  - \<html> 相应实体

### 2. HTTP无状态

- 无状态，每次发送都不记录状态
- 可以通过cookie或者session或者set-header来记录下一次链接，管理链接状态
- 可以通过websock建立永久链接，并行请求，管线化

### 3. 请求方法

​	GET/POST/OPTION/HEAD/TRACE/DELET/PUT/CONNECT

- GET 获取资源
- POST 传输实体主体
- PUT 传输文件
- HEAD 获得报文首部，不会返回主题部分
- DELETE 删除文件，相应状态码204表示 not content（文件已经从服务器删除）
- OPTION 询问支持的方法， ALLOW:GET POST HEAD

- TRACE: 追踪路径，Max-Forwards 首部字段规定了其通过代理服务器转发次数，每次代理服务转发一次则减一
  - 容易引发XST攻击（ cross-site tracing 跨站追踪）

- CONNECT: 要求使用隧道协议链接代理

## 第三章

### 1. HTTP报文

- 请求报文
  - 请求行：请求方法，协议方案及版本号
  - 请求首部字段
  - 通用首部字段
  - 实体首部字段
- 响应报文
  - 状态行：响应结果状态码，原因短语，协议方案及版本号
  - 响应首部字段
  - 通用首部字段
  - 实体首部字段

- 报文主体

### 2. 提升编码传输速率

- 压缩传输的内容编码
  - gzip
  - compress
  - deflate
  - identity

- 分割发送分块的传输编码
- 发送多种数据的多部份对象集合
  - multipart/form-data：在web表单文件上传使用
  - multipart/byteanges：状态码206，只响应指定报文包含的多个范围的内容（获取部分请求范围，如图片的一半）

### 3. 协商返回最合适的内容

​	根据语言的不同，返回对应的编码网页信息

- Accept
- Accept-Charset
- Accept-Encoding
- Accept-Language
- Content-Language

## 第四章

## 第五章



# 书本二、图解TCP/IP

## 第一章：OSI分层结构

- OSI分层

  open system interconnnection

  - 应用层：HTTP/TELNET/SMTP(simple mail transfer protocol)/SNMP(simple network manager protocol)/ping/ftp/SSH(system shell)/POP

    - 应用层：

    - 展示层：HTML

    - 会话层：SSL/TLS

  - 传输层: TCP/UDP/UDP-Lite

  - 网络层: ARP/RARP/IPV4/IPV6/ICMP/IPsec

  - 数据链路层: 以太网、无线WAN、PPP

- 计算机网络两种规模：局域网（LAN）和广域网(WAN)

- 根据接收端数量分类：单播、多播、广播、任意播

  - 广播：ip4中的主机地址（主机标识符）全部为1（ABCD类地址）

- 网络硬件对应的层及作用：
  - 网卡：NIC，计算机的联网硬件设备，带有mac地址，可以让数据链路层通过路由表找到它
  - 中继器：物理延长的方法，将缩小的信号继而放大
  - 网桥：数据链路层延长网络的设备
    - 硬件设备：同轴电缆，双绞线电缆，光纤电缆
    - 以太网：使用了以上三种混合方式传输
    - 无线：通过无线电磁波进行传输
    - ATM：使用双绞线和光纤电缆，通过交换机方式实现异步传输协议，大大加快了链路层的传输速度
    - FDDI：光纤分布式数据接口，双绞线电缆和光纤电缆的呼呼
  - 路由器：通过网络层转发分组数据的设备
  - 网关：实现网络协议的转换
- 分层的意义：
  - 链路层知识通过物理方式将整个网络联通
  - 真正实现点点对点传输的是通过网络层，这里的网络层可以通过VPN、PPPoE等方式控制校验和流量等控制
  - 传输层tcp则是为了实现可靠性传输
  - 应用层实际发送的数据，并通过SSL/TLS进行了一些加密操作

- 各个分层单位说明
  - 包：各个分层都可以这么叫
  - 帧：数据链路层的单位
  - 数据报：网络层的信息单元
  - 段：传输层的单位
  - 消息：应用层

- 网络的构成

  - 分类一：
    - 骨干网
    - 边缘网络

  - 分类二：

    - ISP: internet Service Provider
    - IX: Internet Exchange
    - NOC: NetWork Operation Center


## 第二章： tcp/ip标准

- 一个完整的消息体结构
  - 接收MAC地址/发送MAC地址/以太网类型-------链路层-以太网头
  - 发送端Ip/接收端ip/协议类型（协议方案）-------网络层-ip首部
  - 源端口号/目标端口号-------传输层-tcp/udp首部
  - 数据-------应用层-应用包头及数据
  - 数据链路层的循环long余校验-------链路层层

## 第三层：数据链路层

- MAC地址的规则
  - 第一位：单薄还是多播
  - 第二位：全局还是本地
  - 第三到24：厂家码
  - 25-48：唯一识别
- 共享型网络介质和非共享型网络介质
  - 共享型网络介质：会存在冲突
    - 争用方式（CSMA）：可以通过CSMA/CD 来提前看是否信道被占用，若有先释放，后面再重新查看占用情况
    - 令牌传递：轮询取得令牌，取得令牌可发送数据
  - 非共享型网络介质
    - ATM：异步传输，一入一出，通过交换机
    - VLAN: 构建虚拟网络，对网段进行分开
- 网络链接的拓扑结构：总线型，环状，星型，混合型
- FDDI的连接方式
- 以太网
- 无线WAN
- PPP点对点
  - PPPOE：使用以太网的点对点传输

## 第四章：ip层-网络层

根据3.16号目前为止看到的只是做一些知识点的小结！

- IPV4地址的四大类 ABCD及地址范围分配（一共32位，分成四组，一组位8位，对应2^8-1=256）
  - 全部为0表示用于网络通讯有问题
  - 主机标识码全部为1用于广播地址
  - A类：0开头2^6-1=127，范围是0~127，其中主机标识码后24位，网络标识码前8位数，第一位为0
  - B类：10开头2^7-1=128，范围是128~192，其中主机标识码后16位，网络标识码也是，前两位10
  - C类：110开头，192以后，主机标识码8位，网络标识码24位，前三位110
  - D类：主机标识码全部为1
  
- IPV6地址128位表示，16进制，128/4=32个十六进制，每四个一组对应32/4=8组，全部为0可以直接::链接

- IP地址不够的解决方案：

  - CIDR+VLSM（可变长子网掩码） 采用任意长度分割ip地址的网络标识和主机标识
  - IPV4+NAT技术   
  - IPV6  

- 子网掩码（NAT技术）：控制主机地址的位数，主机地址和网络地址的标识

- 网络层发送包的过程：ip寻址、ip路由、ip分包与组包

- ip路由：每个主机都有一个路由表(netstate -r(n))可以看得到，且有默认的地址0.0.0.0进行路由转发，当不能提供需要路由的地址时，通过默认路由且进行转发操作，通过hop to hop 实现点对点（ppp）的转传输

- ip的拆包与组包：因为不统数据链路，传输的数据量大小受限，通过添加请求头先拆包再目标机器组装

  - tcp与udp之间是不一样的，因为udp是没有重发机制的，但总体都是以下方式：

    - 先通过不拆分，直接发送，此时拆包标志位是1，路由器会进行丢包操作

    - 丢包后，通过ICMP返回一个最小的MTU单元

    - 此时路由器会进行消息（数据包）的分包操作进行传输

  - 以上路由器需要进行分包会有性能问题，因此通过路径MTU发现可以进行优化

    - 通过ICMP返回的MTU最小单元
    - 直接在TCP层进行MTU分包
    - 通过网络层和数据链路层传达到目标机器的TCP层组包

## 第五章：ip协议相关技术

- dns服务器的解析：通过域名解析成ip地址

  - 域名的分层-顶级域名（顶点的下一层域名）

    如: pepper（子域名）.kusa（顶级域名）.ac（大学）.jp（国家）

  - 域名服务器-根据域名的属性，一层一层的往下解析域名
  - DNS查询
    - 向**DNS服务器**查询ip地址
    - 若DNS服务器不能解析，则请求转发到**根域名服务器**进行查询
    - 根域名服务器返回ip地址
    - 客户端将获得的ip地址进行与目标主机通信

- ARP(Address Resolution Protocol)的寻址：维护一个ip与mac地址的路由表信息，以ip地址为线索来定位下一个应该接受数据分包的网络设备对应的MAC地址

  - ARP寻址过程

    - 主机A广播发送一个ARP请求包，携带了目标主机的ip地址

    - 目标主机解析后，将自己的MAC地址塞入ARP响应包，返回给主机A

    - 此时MAC地址会被缓存，以提供后续使用，节省不必要的网络流量，超过缓存期限会自动清除

  - RARP的逆向寻址：mac地址反向生成ip地址，例如打印机

- MAC地址和IP地址的差别

  - MAC地址是数据链路层面的概念，而IP地址是网络层的概念
  - 路由器对MAC地址的重复转发做了过滤的操作（即A->C(路由器)->B，A->D(路由器,不会发给C导致数据包被重复发送两次)->C(路由器)->B）
  - 路由器会隔断网络，主机A无法将数据直接发送主机B
  - IP地址决定了主机所在的位置，如果每次都通过MAC地址来广播，那么每次广播的范围是整个互联网的所有主机，这样会造成巨大流量浪费

- NAT技术：多个主机通过一个路由对应的一个ip地址统一路由请求信息到局域网外的任意地址

- DHCP动态分配ip的使用，即插即用，自动分配ip地址

  - 主机要求设置IP地址和子网掩码（广播）
  - DHCP服务器通知可以使用的网络设置（单播）
  - 主机通知DHCP想要使用的IP地址（单播）
  - DHCP服务器通知允许主机的设置（单播）

- ICMP 与 ICMP ipv6.0 ：确认网络是否工作正常，异常时网络环境诊断

  - 主机A发送包
  - 路由器转发ARP请求包
  - 由于主机B电源关闭
  - 多次发送ARP请求包，依然没有响应
  - 路由器返回一个ICMP Destination Unreachable的包给主机A

- NAT：用于本地网络中使用的私有地址（解决一台主机一个公网ip地址导致地址不足问题）

  其实再一个域里面，只要保证地址唯一性即可！

  - 请求发出时，将源地址转换成全局IP地址
  - 请求响应时，将目标地址转换私有IP地址

  - NTPT: 带端口的NAT以解决内部地址不够问题

- IP隧道跨协议的信息传输：IPV6->隧道->IPV4

## 第六章：TCP与UDP-传输层

- 协议大都以客户端/服务端的形式运行，一般是以守护进程的方式，通过默认端口来监听请求
  - httpd:80
  - sshd:22
  - ftpd:21
  - smtp:25
  - dns:53
  - telnet: 23
  - https:443
  - pop:（post office protocol）：
- TCP/UDP
  - TCP: 面向链接的，可靠性的流协议，实行顺序控制，重发控制，流控制，拥塞控制
  - UDP: 不具有可靠性的数据报协议，允许数据又一定的失真
- TCP/UDP一般通过五个信息来识别：源IP地址，目标IP地址，协议号（类型），源端口，目标端口
- 端口号的确定一般有以下两种：
  - 标准既定的端口号
  - 时许分配方法
- UDP（user datagram protocol）特点：丢包不会重新发送，包到达后顺序不保证有序
  - 包总量较少的通信（DNS, SNMP）
  - 视频、音频等多媒体通信（即时通信）
  - 广播通信（广播、多播）
  - udp辅助协议
    - RTP：RTP首部加入时间戳和序列号字段以保证UDP数据的有序性
- TCP: 可靠性传输，通过序列号，确认应答，重发控制，连接管理以及窗口控制
  - 序列号：再进行tcp传输时，服务器会返回一个序列号，以告知请求的机器自己收到了它发送的包信息，以保证确认应答，请求客户端会以服务器告知的序列号，继续发送后面的包数据
  - ==**连接管理（超级重要）：**==四次握手三次挥手
    - 客户端发送SYN给服务器，请求建立连接
    - 服务器ACK响应客户端的请求，并发送SYN请求建立连接
    - 客户端应答ACK，此时客户端和服务器成功建立连接，可以发送传输数据
    - 客户端发送FIN给服务器，要求断开连接
    - 服务器响应ACK
    - 服务器发送FIN请求切断连接
    - 客户端响应ACK表示确认可以断开，此时双方连接中断，一个HTTP请求完成
  - TCP最大消息长度：MSS(max segment size)
    - 这里可以参考ip层的路径MTU发现，最大消息长度正好是ip中不会被分片处理的最大数据长度
    - 客户端为了防止路由器对信息进行分包和解包，消耗大量网络环境，再真正通讯前会做一个MSS请求包大小的确认
    - 通过建立连接的SYN报互相通知对方网络的MSS值
    - 在两者之间选一个较小作为MSS的值发送数据
  - 利用窗口控制提高速度
    - 简单的说，就是利用一个滑动窗口来控制多个包同时发送，并且保证可靠性传输和有序性，然后继续往下一个窗口滑动
- 关于TCP和UDP的MSS最大段号确定
  - TCP
    - 客户端发送一个ip首部分片标志位为1，路由器丢包
    - 由ICMP通知下一次的MTU的大小
    - 根据TCP的重发处理，数据包会被重新发送，TCP负责将数据分成ip层不会再被分片的力度以后传给IP层，ip层不会做分片处理
    - 不需要重组，数据被远扬的发送给接收端的TCP层
  - UDP
    - 发送时ip首部的分片标志位设置为部分篇，路由器丢包
    - 由ICMP通知下一次MTU的大小
    - UDP没有重发处理，应用在发送下一个消息是会被被骗
    - 所有的分片到达目标主机后被重组，再传给UDP层
- TCP控制位：
  - ACK（acknowledgement Flag）: 该位为1时，确认应答的字段变为有效。TCP规定除了最初建立连接时的SYN包之外必须设置为1
  - SYN(synchronize Flag): 用于建立连接，syn为1表示希望建立连接，并在其序列号的字段进行序列号初始值设定
  - FIN（Fin Flag）： 该为为1时，表示今后不会再有数据发送，希望断开连接，当通信结束希望断开连接时，通信双方的主机之间就可以互相交换FIN位置为1的TCP段，每个主机又对对方的FIN包进行确认影后就可以断开连接。

## 第七章：路由协议

## 第八章：应用协议-应用层

- TELNET远程登陆的过程

  - 客户端键入文字命令（telnet会发送文字命令）
  - 进行航模是或者透明模式处理后发送给服务器端的telnetd守护进程
  - telnetd守护进行会向shell发送文字命令
  - 解释从shell收到的命令，执行程序，获取结果
  - 获取从shell返回的结果
  - 进行行模式或者透明模式等处理后返回给TELENT客户端
  - 根据NVT（network virtual terminal）设置回显再屏幕上

- SSH是TELNET的加密远程登陆，建立了加密的通道

  - 可以用于更强的认证机制
  - 可以转发文件
  - 可以使用端口转发功能

- FTP文件传输的过程

  - 一般建立两个连接

    - 数据控制连接：默认21端口

      控制连接主要是为了用于设置一些传输属性：用户名，密码验证，发送文件名称，发送方式等

    - 数据传输连接：默认20端口，也可能是随意端口，为了安全性考量

- SMTP传输协议及POP3辅助

  - 客户端--SMTP--邮件服务器（POP）--接受端
  - 客户端通过SMTP协议将邮件发送到邮件服务器
  - 邮件服务器存储了邮件的信息，当接收端登陆时候会使用POP协议获取邮件
    - 发送认证信息
    - 返回邮件信息



# 面试知识梳理



# 其他知识

### 2.2. HTTP知识

#### 2.2.1. 带宽：

- 标识单位时间内通过链路的数据量（数据传输能力）
- 通常以bps来表示，每秒可传输的位数（10^9bit/s=1Gbit/s=>1Gbps, 比特/秒）	

#### 2.2.2. 全双工与半双工：

- 全双工，AB之间可以同时进行数据传输，有两根不统的数传线传送
- 交换机的全双工
- 网卡的双公司模式

#### 2.2.3. 重定向

​	url转发，当一个资源被临时或者永久转移之后，可以通过原始的资源进行重定向转发，以达到访问最新资源的目的。

a. 重定向几种情况

- 301 mved permanently 永久性重定向
- 302 found 临时性重定向
- 303 see other 有于请求对应的资源存在着另一个URI，应使用GET方法定向获取请求的资源
- 304 Not modified 资源未改变，可直接使用客户端未过期的缓存
- 307 Temporary Redirect 临时重定向，区别302，不会发生将POST转换成GET请求。

#### 2.2.4. REST风格

​	一种规范，规范了请求动作，get/post/put/delete/head/option/trace/，根据请求方法的不同，对应服务器不一样的处理方法

```
原来：http://localhost:80/getUser?id=5
Restful: GET http://localhost:80/user/5
原来：http://localhost:80/deleteUser?id=5
Restful: DELET http://localhost:80/user/5  
```

在spring中的应用：

	- @RequsetBody 可以直接请求的资源实体自动转换成对应的model层对象
	- 统一的响应结构{status:"200", mes: "success", data:{}}

#### 2.2.5. websocket

​	浏览器进行全双工通信！为了优化http通信而存在。

	- 传统的http是无状态，针对资源更新只有客户端请求后，服务器才能进行推送，无法知道资源是否更新，只有客户端反复请求服务器，这样产生了大量的请求，消耗了大量的资源。

-  可以通过ajax异步更新，此时虽然是局部更新，但是依然解决不了这个何时更新及时传输的问题。

-  使用comet延迟应答，模拟实现服务器端向客户端推送功能，未能根本解决。

-  SPDY（speedy）可以改善这种情况，通过在”应用层“与”传输层“加上”会话层“（位于SSL之前）
   - 赋予请求优先级
   - 压缩http首部
   - 推送功能
   - 服务器提示功能

-  使用wenSocket进行全双工通信
   - 一旦web服务器与客户端之间建立起webSocket协议的通信连接，之后所有的通信都会依靠这个专用协议进行，通信过程中可互相发送任意资源

![http与websocket](D:\code_ty\spring-framework\image\http与websocket.png)