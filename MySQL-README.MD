# Mysql知识梳理

## 1. 常见知识概念

### 1.1. mysql架构

- 连接池，授权，安全，链接处理
- 解析器，在此之前查缓存，若缓存已经有则不会过解析器，缓存直接返回
- 优化器
- 存储引擎：开启一个事物、根据主键提取一行记录

### 1.2. 一个查询语句执行顺序

- 客户端请求发起
- 查缓存，若缓存有直接返回结果集
- 过解析器，生成解析树
- 预处理器，生成解析树
- 优化器：重写查询、决定表的读取顺序、选择合适的索引
- 生成执行计划
- 存储引擎
- 数据层，筛选返回结果

### 1.3. 并发控制

​	多个进程在同一时刻修改或查询数据，顺序控制

#### 1.3.1. 服务器层

#### 1.3.2. 存储引擎层

#### 1.3.3. 读写锁

- 共享锁-读锁，共享互不阻塞【select】
- 排它锁-写锁，需要征用锁，获取锁的线程才可进行写操作【update, delete, insert】

#### 1.3.4. 锁粒度

​	所策略，在数据安全和执行效率上面寻找平衡点

- 表锁，整个表进行加锁，
- 行级锁-只在存储引擎层实现，例如innodb，xtradb

### 1.4. 事务

#### 1.4.1. 事务特性ACID

​	原子性、一致性、隔离性、持久性(durability)

- **Transaction**（整理事务隔离性笔记和传播性笔记）
  - **isolation**(非常重要)
  - **propagation**(非常重要，required和requied_new再主事务和嵌套事务中的使用及影响)
  - noRollbackFor/noRollbackForClassName/rollbackFor/rollbackForClassName
  - readOnly/Timeout
- 受检异常和未受检异常回滚机制
  - Transaction-readOnly/Timeout

#### 1.4.2. 事务隔离性级别及存在的问题

- 事务并发问题及处理对应情况

  - 脏读: 事务1修改了value，事务2独取了value，事务1回滚，这时候事务2独取到的值是脏数据
  - 幻读: 事务1读取了value，事务2修改了value，事务1再次独取value时两次独取不一致
  - 不可重复读: 两次独取到的数量不一致

  |                                         | 脏读 | 不可重复读 | 幻读 |
  | --------------------------------------- | ---- | ---------- | ---- |
  | READ_UNCOMMITTED(读未提交)              | 有   | 有         | 有   |
  | READ_COMMITTED(读已提交)                | 无   | 有         | 有   |
  | **REPEATABLE_READ(可重复读,mysql默认)** | 无   | 无         | 有   |
  | SERIALIZABLE(串行化)                    | 无   | 无         | 无   |

- 事务的传播性propagation

  - Propagation.REQUIRES_NEW 另起一个事务，该事务与主事务无关
  - Propagation.REQUIRED 和主事务绑定，若主事务执行异常则一同失败

#### 1.4.3. 死锁

​	两个或多个事务在统一资源互相占用，从而导致恶性循环的现象

- innodb目前处理死锁的方法是，将持有最少的行级排它锁的事务进行回滚

#### 1.4.4. 事务日志

- 提高事务的效率，使用事务日志，存储引擎在修改表的数据时只需要修改其内存拷贝，再把该修改行为记录到持久在磁盘硬件上，而不用每次都将修改的数据本身持久到磁盘（减少了io，保证有序性）- 预写式日志，修改数据需要写两次磁盘。
- 系统奔溃时，事务日志在系统恢复后会自动写入。

#### 1.4.5. 事务默认提交

- show variables like 'autocommit'; 查询事务是否默认提交；
- DDL - data definition language 数据定义语言 create、drop、alter改变或创建表结构
- 同一个事务只有在同一个存储引擎下才能保证被正常acid，多个存储引擎不可靠。

#### 1.4.6. 事务管理(105课)

- 查询默认隔离级别

  ```sql
  SELECT @@transaction_isolation;
  ```

- 修改当前session(glabal)的事务隔离级别

  - Read uncommitted

    ![mysql-tx-read-uncommited](/Users/yuefengwu/code/spring-framework/image/mysql-tx-read-uncommited.png)

  - read committed

  - Repeatable read

    ![mysql-tx-repeatable-read](/Users/yuefengwu/code/spring-framework/image/mysql-tx-repeatable-read.png)

  - Serializable

  ```sql
  SET session TRANSACTION ISOLATION LEVEL Serializable;
  ```

- 根据事务隔离级别进行测试提交及回滚操作

### 1.5. 多版本控制-MVC(currency)C	

​	**可认为mvcc时行级锁的一个变种【一种支持更好并发的锁机制】，而锁机制是为了支持并发事务而存在的**。但是他在很多情况下避免了加锁操作，因此开销更低，大都实现了非阻塞的读操作，写操作也只是锁定必要的行。

​	innodb的mvcc时通过在每行记录后面保存两个隐藏的列来实现的，这两个列，一个保存了行的创建时间，一个保存了行的过期时间。存储的时间意思是版本号，每开始一个新的事务，系统版本号都会自动递增，事务开始时刻的系统版本号会做为事务的版本号，用来和查询到的每行记录的版本号进行比较，因为有了版本号的控制，使得大多数读操作都可以不用加锁，不足之处是额外增加了存储空间。

- mvcc只会在读已提交和可重复读下生效，因为读未提交会每次都会读取最新的行，而不是符合当前事务版本的数据行，串行化会每次读取的行进行加锁

## 2. 基准测试

## 3. 服务器性能剖析

## 4. schema与数据类型优化

## 5. 高性能索引

## 6. 查询性能优化

## 7. 高级特性

## 知识补充

### 1. innodb和MyISAM存储引擎区别

- innodb索引可采用表锁，行级锁而MyISAM没有行级锁，只有表锁，支持
- InnoDB支持事物，而MyISAM不支持事物
- InnoDB支持MVCC, 而MyISAM不支持
- InnoDB支持外键，而MyISAM不支持
- InnoDB不支持全文索引，而MyISAM支持。

### 2. Mysql常用查询

#### 1. 查询表情况

```sql
show table status like 'student';
```

#### 2. 事务

- 查询隔离级别

```sql
select @@transaction_isolation
```

- 设置当前事务隔离级别

```sql
set transaction session isolation level read committed;
```

#### 3. 性能查询

- 执行查询性能

```sql
explain select 
```

- 

#### 4. 修改表的存储引擎

```sql
alter table tablename set engine=InnoDB
```



