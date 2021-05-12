# Redis常见知识梳理

## 1. NoSQL数据简介

​		not only sql，非关系型数据库(key-value模式的存储结构)，打破了传统的关系型数据库的存储方式，根据实际业务场景，性能优先存储。

- 不遵循sql标准
- 不支持acid
- 远超于sql性能

### 1.1. 使用场景

- 高并发读写
- 海量数据的读写
- 对数据高可扩展性

### 1.2. 拓展

- 行存储类型数据库和列存储类型数据库

## 2. Redis6概述和安装

#### 2.1. 特点概述

- key-value存储结构
- 支持存储的value类型相对更多，包括了string（字符串）、list（链表）、set（集合）、zset（sort set 有序集合）、hash（哈希类型）
- 支持push/pop、add/remove、取交集并集差集，且这些操作都具有原子性（redis本身不支持acid）
- 支持各种排序
- 数据都是缓存在内存中
- redis会周期性的把更新的数据写入磁盘或者把修改操作写入追加的记录文件
- 可以实现主从操作

#### 2.2. 多样数据结构存储持久化（场景）

- 通过list实现自然时间排序的数据，获取最新的N个数据
- 利用Zset（有需集合）获取排名靠前的几个
- Expire过期，短信的时效性控制
- 原子性，自增方法increase，decrease，例如计数器，秒杀
- 力用set集合，对数据大量去重
- 力用list集合，构建队列
- pub/sub模式，发布订阅消息系统

#### 2.3. linux下安装linux

- 系统安装C编译环境
- 解压redis压缩包，进入到文件夹中，make 编译解压的文件
- 用make install完成安装
- cd /usr/local/bin 目录下查看安装结果
  - redis-benchmark: 性能测试工具，可运行测试自己的电脑性能
  - redis-check-aof: 修复有问题的AOF文件，rdb和aof后面讲
  - redis-check-dum: 修复有问题的dump.rdb文件
  - redis-sentinel: redis集群使用
  - **redis-server: redis服务器启动命令**
  - **redis-cli: 客户端，操作入口**
- 配置后台启动
  - 修改redis.conf文件
  - 修改daemonize 的值为yes
  - 使用 redis-server启动
  - 使用redis-cli测试链接状态，eg:pint

#### 2.4. 默认6379端口

- 默认初始化0号数据库，一共有16个数据库，通过select来切换数据库，例如：select 15
- redis是**单线程**+**多路IO复用技术**
  - 多路复用：使用一个线程来检查多个文件的描述符（socket）就绪状态，比如调用select和poll函数，传入多个文件描述符，如果有一个文件描述符就绪，则返回，否则阻塞直到超时，得到就绪状态后进行真正的操作可以同一个线程里执行，也可以启动线程执行（例如使用线程池）
- **串行 vs 多线程+锁 vs 单线程(memcached)+多路IO复用(redis)**

## 3. 常用五大数据类型

## 4. Redis6配置文件详解

## 5. Redis6的发布和订阅

## 6. Redis6新数据类型

## 7. Jedis操作Redis6

## 8. Redis6整合spring-boot

## 9. Redis的事务操作

## 10. Redis持久化之RDB

## 11. Redis持久化之AOF

## 12. Redis的主从复制

## 13. Redis6集群

## 14. Redis6应用问题解决

## 15. Redis6新功能