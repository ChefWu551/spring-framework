# Spring Framework

## 一、任务清单

### 1.1. IOC容器篇
#### 1.1.1. xml 篇

- 实验一：bean初始化及标签scope的使用；
- 实验二：构造方法初始化bean及标签index顺序赋值使用；
- 实验三：泛型容器的赋值**list、map、List<Object>;**标签ref的外部引用；parent继承属性及修改对应的bean属性值[指向性，所以当重新对对象赋值后，原对象的属性值也会跟着改变]；
- 实验四：abstract声明true时候不会生成对应的bean，此时对象只能作为模板，被其他bean所引用；
- 实验五：三种初始化bean的方法：静态工厂方法、实例化工厂方法、bean的直接初始化；bean的生命周期；
- 实验六：为bean初始化过程添加后置处理器，继承BeanPostProcessor
- 实验七：SpLE(spring expression language)语法知识
- 实验八：基于xml的自动装配，autowire标签

#### 1.1.2. 注解篇

##### 1.1.2.1. 实验一: 通过注解创建Dao service controller

##### 1.1.2.2.实验二: context:include-filter

- 指定扫描包时要包含的类

##### 1.1.2.3.实验三：使用context:exclude-filter

- 指定扫描包是不包含的类

##### 1.1.2.4.**实验四：DI依赖注入**[重要]

![spring-泛型类调用](image\spring-泛型类调用.png)

##### 1.1.2.5.实验五：测试泛型依赖注入[实践及画图]

##### 1.1.2.6.==**任务六：ioc源码，调试ioc容器[重要]**==

[画画流程图、xmind图来看清楚每个方法做什么]

- 通过打印内容找到对应的ioc创建bean的关键代码
- 保存单例的HashMap的一级缓存
- ApplicationContext和BeanFactory的区别

### 1.2. AOP

#### 1.2.1. 注解篇

##### 1.2.1.1. 实验一：实现注解切面配置

- （`@Before`/`@AfterReturning`/`@AfterThrowing`/`@After`）及官方词语详解

​	若使用的是jdk自带的代理包时，应注意代理对象必须为接口不能是起本身。

```java
/**
         *
         * 实验一：spring+proxy.newInstance
         * 注意:
         *  - 这里spring使用的是jdk的proxy包，所以getBean应该是代理对象（Calculator）而不是实际对象（MyCalculator），否则报错，
         *com.mountain
         *  - 若织入了aop的增强，则此时获取的bean必须是代理对象，实际对象是不能通过BeanFactory的getName/getTypcom.mountain         */
Calculator calculator = applicationContext.getBean(Calculator.class);
// 注意此时对象类型是：对象类是：class com.sun.proxy.$Proxy20[代理对象]
System.out.println("获取的对象是：" + calculator + "; 对象类是：" + calculator.getClass());
```

- aop常用词汇：切入点（切入点表达式）、连接点

- aop切面执行顺序：

​		a. 切面1（@Before）--- 切面1（@Around(前切)）

​		b. 切面2（@Before）--- @Around(前切)

​		c. 方法执行 

​		d. 切面2（@Around(后切返回/异常)）--- 切面2（@after）

​		e. 切面1（@Around(后切返回/异常)）--- 切面1（@Before）

​		@Around和@Before若使用@Order指定顺序则按照类名首字母默认开始切入，步骤a、b无固定顺序，但是有相对顺序，先入后出，后入先出；步骤d、e同理

==任务二：spring除了默认`java.lang.reflect.Proxy`还有使用的AOP增强包(`springSource.cglib`)[接口代理转类代理]==

实验三：切入点表达式`@PointCut(excution(public int com.com.mountain.*.*(int, int)))`

实验四：通知方法的执行顺序`@Order`与切面嵌套图分析(多切面顺序分析)

实验五：对切面的方法及返回值进行打印日志

实验六：环绕通知`@Around`且环绕通知与其他通知的执行顺序，多切面的执行顺序

实验七：使用xml实现和注解一样的功能

### 1.3. spring-jdbcTemplate

#### 1.3.1. 实验一：jdbcTemplate的增删改查

#### 1.3.2. **实验二：配置事务管理器控制事务的一致性**</a>

- **Transaction**（整理事务隔离性笔记和传播性笔记）
  - **isolation**(非常重要)
  - **propagation**(非常重要，required和requied_new再主事务和嵌套事务中的使用及影响)
  - noRollbackFor/noRollbackForClassName/rollbackFor/rollbackForClassName
  - readOnly/Timeout
- 受检异常和未受检异常回滚机制
- Transaction-readOnly/Timeout
- 并发修改

**实现:**

- a. 依赖相关:5.3.3中自带了spring-tx包相关依赖

```xml
<!-- spring 命名空间 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.3.4</version>
</dependency>
<!--spring AOP @Aspect-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>5.3.4</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>5.3.3</version>
</dependency>
```

- b. 配置spring-xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns:context="http://www.springframework.org/schema/context"
         xmlns:tx="http://www.springframework.org/schema/tx"
         xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd
         http://www.springframework.org/schema/context
         https://www.springframework.org/schema/context/spring-context.xsd
         http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">
  
      <context:component-scan base-package="com.mountain"/>
  
      <bean class="com.alibaba.druid.pool.DruidDataSource" id="dataSource">
          <property name="username" value="root"/>
          <property name="password" value="root"/>
  com.mountain  <property name="url" value="jdbc:mysql://localhost:3306/spring_tx"/>
          <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
      </bean>
  
      <bean class="org.springframework.jdbc.core.JdbcTemplate" id="jdbcTemplate">
          <constructor-arg ref="dataSource" name="dataSource" />
      </bean>
  
      <bean class="org.springframework.jdbc.datasource.DataSourceTransactionManager" id="transactionManager">
          <property name="dataSource" ref="dataSource"/>
      </bean>
      <!--
          注意此处开启注解，需添加事务的xml文件约束，否则事务控制失效
          xmlns:tx="http://www.springframework.org/schema/tx"
          xsi:schemaLocation="http://www.springframework.org/schema/tx 
          http://www.springframework.org/schema/tx/spring-tx.xsd"
      -->
    	<!-- 开启事务注解,默认的事务注解名称是transactionManager,所以此处可以不写,若非默认,则:
  		<tx:annotation-driven transaction-manager="自定义事务的bean id"/>
  	-->  
      <tx:annotation-driven/>
  ```

- c. 在要处理的方法上加@Transactional注解

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

### 1.4. spring-javaweb

#### 1.4.1. 实验一：spring与javaweb整合

- WebApplicationContext(ContextInitialized & ContentDestory)

#### 1.4.2. 实验二：请求Controller层的一些注解

- @RequestMapping注解源码及使用
  - scope 类与方法
  - value=path=name
  - method限制请求方法 get/post/options/trace/delete/put/head//patch
  - params缩小请求范围，可指定必要参数和一定不含有参数及对应参数值
  - headers规定请求头
  - consumes请求实体首部内容限制
  - produce相应实体首部内容限制
- url带{} eg: user/{id}
- @PathVarible使用
- @RequestParam
- @RequestHeader
- @CookieValue：header内，key是 Cookie, value是eg:cookieParameter=qwe
- 自动对象映射Book及嵌套自动映射Book(Author)

- CharacterEncodingFilter对编码的设置
  
- ~~任务三：REST风格请求~~
  
  - PUT和DELETE等Http方法实现
  
  - 在spring mvc中的支持（HiddenHttpMethodFilter<-doFilterInternal()）
- **==任务四：spring+web==(核心源码位置**)

  - web.xml中

    ```java
    <servlet> 
    	<servlet-name>springDispatcherServlet</>
    	<servlet-class>org.springframwork.web.servletDispatcherServlet
    </servlet>
    ```

  - HttpRequest/HttpResponse

  - 在controller方法中使用Map/Model/ModelMap及三个实现的原理（**BindingAwareModelMap**）

  - **解析DisptcherServlet 启动流程源码-继承实现树及源码**

    - 发起请求
    - HttpServlet-doGet/doPost
    - FramworkServlet-processRequest
    - DisptcherServlet-doService-**doDisptch**(spring 核心)
      - 1. getHeadler()根据请求地址找到处理这个请求的目标处理器
        2. getHandlerAdapter()根据当前处理器类获取能执行的处理适配器方法
        3. 使用刚才获取的适配器AnnotationMethodHandlerAdapter执行目标方法
        4. 目标执行后返回一个modelAndView对象
        5. 根据modelAndView的信息转发到目标具体页面并可以在请求域取出改对象

    - spring mvc九大组件-《看透springmvc源码分析与实践》DisptcherServlet-onRefresh
      - ha.handle
  
    ![spring-webmvc GET请求执行流程](image\spring-webmvc GET请求执行流程.PNG)
  
  - InternalViewResolver源码，为什么prefix和suffix会自动和指向页面拼串
  
  - 视图解析其和渲染器ha.handle->processDispatherResult->render->viewResolver->resolveViewName->

#### ==1.4.3. 实验三：重定向redirect和转发forward==

#### ==1.4.4. 实验四：\<mvc>标签及源码==

 -  \<mvc: view-controller  path="url"  view-name="默认|forward|redirect">

 - \<mvc：annotation-driven>自动注册

   - RequestMappingHanderMapping/RequestMappingHanderAdapter/ExceptionHanderExceptionRsolver

   - BeanDefinitionParser
     - propertyPlaceholderBeanDefinitionParser
     - listBeanDefinitionParser/MapBeanDP/SetBeanDP
     - AnnotationDrivenBeanDefinitionParser
     - ViewControllerDefinitionParser

 - 对@requestMapping注解的影响

 - 对静态资源和动态资源的影响（主要看handlerMapping和handlerAdapters初始化的类变化）

   - \<mvc:default-servlet-handler>对动态资源和静态资源的影响
   - \<mvc:annotation-driven>对动态资源的影响

#### ==1.4.5. 实验五：自定义视图解析器ViewResolver==

- 写一个自定义视图解析器
- 使用order接口指定其解析顺序
- 打到源码断点去看实际情况
- 反思spring的面向接口编程实现

#### ==1.4.6. spring-mvc的CURD==

- http请求带参转换成controller参数的经历过程
  - 类型转换（string->integer/boolean/..）
  - 数据绑定期间的数据格式化问题（日期）
  - 数据校验合法性

- webDataBinder绑定数据对象源码
  - conversionService
  - Validators
  - bindingResult

- 编写自定义一个类型转换器

  - 自定义converter放入conversionService中

  - 将webDataBinder中的conversionService设置成我们加了自定义类型转换器的voncersionService

    - 配置conversionServiceFactoryBean中添加自定义的converter

      \<bean>\<property>\<set>

    - 使用\<mvc:annotation-driven conversion-service="转换器">使其生效

- 解释为什么类型转换器有多个时候会优先使用自定义的类型转换器（源码）
- 

## 二、其他知识

### 2.1. Spring知识

- ==autowired 是实现原理（UML）==
- ==bean生命周期源码及源码梳理（UML）==

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

- SPDY（speedy）可以改善这种情况，通过在”应用层“与”传输层“加上”会话层“（位于SSL之前）
  - 赋予请求优先级
  - 压缩http首部
  - 推送功能
  - 服务器提示功能

- 使用wenSocket进行全双工通信
  - 一旦web服务器与客户端之间建立起webSocket协议的通信连接，之后所有的通信都会依靠这个专用协议进行，通信过程中可互相发送任意资源

![http与websocket](image\http与websocket.png)

### 2.3. WEB知识

#### ==2.3.1. servlet==

### 2.4. JAVA基础

- 装饰类整理和io流装饰类整理（xmind）

- 复习动态代理

- 复习异常常见类型（xmind）及finally执行返回、赋值与try return中返回值的关系（coding）

### 2.5. 其他

#### ==2.5.1. 查看认证权限的用户id是怎么带入到业务代码中的==



## 三、待办清单

### 3.1.重点知识待办

- 