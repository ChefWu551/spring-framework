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

#### 1.4.3. 实验三：重定向redirect和转发forward

- 通过浏览器的F12可以看到forward是直接调用forward指定接口，转发请求

  Request URL:  http://localhost:8080/forward

  ```
  Connection: keep-alive
  Content-Language: zh-CN
  Content-Length: 93
  Content-Type: text/html;charset=UTF-8
  Date: Sun, 07 Mar 2021 12:26:12 GMT
  Keep-Alive: timeout=20
  ```

- redirect则经过了一层转发，后再次请求最后的重定向接口方法

  Request URL:  http://localhost:8080/redirect => Request URL: http://localhost:8080/redirectPage

  ```
  Connection: keep-alive
  Content-Language: zh-CN
  Content-Length: 0
  Date: Sun, 07 Mar 2021 12:26:34 GMT
  Keep-Alive: timeout=20
  -- 重定向接口，location 跳转
  Location: redirectPage
  ```

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

#### ==1.4.6. spring-mvc的数据绑定==

- 绑定过程

  - spring ioc容器启动，自动将类型转换器注入beanFactory中，对应ConverService

  - 适配器handerAdapter根据请求的url对应到@RequestMapping的方法上面的参数

  - 根据参数的情况对应converter的类型判断是否需要转换

  - 若需要则转换，否则遍历下一个，若无适配的，直接使用默认的适配器DefaultConversionService

    ```java
    DefaultConversionService->GenericConversionService->ConfigurableConversionService->ConversionService
    ```

- http请求带参转换成controller参数的经历过程
  - 类型转换（string->integer/boolean/..）
  - ==数据绑定期间的数据格式化问题（日期）==
  - ==数据校验合法性==

- webDataBinder绑定数据对象源码
  - conversionService
  - Validators
  - bindingResult

- 实现一个自定义类型转换器

  ```xml
  <!-- conversion-service="conversionService" 很重要！若不加，无法使转换器生效 -->
  <mvc:annotation-driven conversion-service="conversionService"/>
  
  <bean class="org.springframework.context.support.ConversionServiceFactoryBean" id="conversionService">
      <property name="converters">
          <set>
              <ref bean="bookParamConverter"/>
          </set>
      </property>
  </bean>
  
  <bean id = "bookParamConverter" class="com.mountain.converter.BookParamConverter">
  
  </bean>
  ```

  - 自定义converter放入conversionService中

    controller:

    ```java
    @Controller
    public class BookController {
        // http://localhost:8080/printBook?bookObject=xiyouji-wce-40
    	// @RequestParam("bookObject") 对应请求url的值，这个很重要，否则无法定位到当前的controller所需要的参数
        // 此时无法过自定义的convert类
        // 若无标注，默认类名首字母小写
        @RequestMapping("printBook")
        public void getBook(@RequestParam("bookObject") Book book) {
            System.out.println(book.toString());
        }
        
        @RequestMapping("printString")
        public void getBookString(Book book1) {
            System.out.println(book1.toString());
        }
    }
    ```

    - 步骤一：继承converter接口

      ```java
      public class BookParamConverter implements Converter<String, Book>, Ordered {
      
          @Override
          public Book convert(String source) {
              Book book = new Book();
              String[] results = source.split("-");
              book.setBookName(results[0]);
              book.setAuthor(results[1]);
              book.setAge(Integer.valueOf(results[2]));
              return book;
          }
      
          @Override
          public int getOrder() {
              return 1;
          }
      }
      ```

  - 将webDataBinder中的conversionService设置成我们加了自定义类型转换器的conversionService

    - 步骤二：配置conversionServiceFactoryBean中添加自定义的converter

      ```xml
      <bean class="org.springframework.context.support.ConversionServiceFactoryBean" id="conversionService">
          <property name="converters">
              <set>
                  <bean id = "bookParamConverter" class="com.mountain.converter.BookParamConverter"/>
              </set>
          </property>
      </bean>
      ```

    - 步骤三：使用\<mvc:annotation-driven conversion-service="转换器">使其生效

      ```xml
      <mvc:annotation-driven conversion-service="conversionService"/>
      ```

- 解释为什么类型转换器有多个时候会使用自定义的类型转换器（源码）

  - ```java
    // 获取获取请求方法适配器
    HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
    ```

  - 处理modelAndView对象，根据request对应的请求路径获取请求方法

  - 反射获取请求方法的参数类型，若类型包含了需要转换的自定义converter则使用 

    ```java
    getTypeConverter().convertIfNecessary(value, requiredType, methodParam);
    ```

    进行对应适配器类型转换

  - 最后通过GeneriConversionService.convert()->Converter<S, T>.convert()->BookParamConverter.convert()完成类型转换

#### ==1.4.7. spring-@RequstBody与@RequestResponse==

#### ==1.4.8. spring 文件上传与下载==

#### 1.4.9. 自定义HandlerInterceptor与源码解析

- 步骤一：编写拦截器代码，继承HandlerInterceptor

  ```java
  public class MyHandlerFilter implements HandlerInterceptor {
  
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
              throws Exception {
          System.out.println("MyHandlerFilter...preHandle");
          return true;
      }
  
      @Override
      public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                             @Nullable ModelAndView modelAndView) throws Exception {
          System.out.println("MyHandlerFilter...postHandle");
      }
  
      @Override
      public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                  @Nullable Exception ex) throws Exception {
          System.out.println("MyHandlerFilter...afterCompletion");
      }
  }
  ```

- 步骤二：配置到spring-mvc中

```xml
<mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/interceptor"/>
            <bean class="com.mountain.filter.MySencondHandlerFilter"/>
        </mvc:interceptor>
        <mvc:interceptor>
            <mvc:mapping path="/interceptor"/>
            <bean class="com.mountain.filter.MyHandlerFilter"/>
        </mvc:interceptor>
    </mvc:interceptors>
```

- 源码分析

  ```java
  protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
  		HttpServletRequest processedRequest = request;
  		HandlerExecutionChain mappedHandler = null;
  		boolean multipartRequestParsed = false;
  
  		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
  
  		try {
  			ModelAndView mv = null;
  			Exception dispatchException = null;
  
  			try {
  				processedRequest = checkMultipart(request);
  				multipartRequestParsed = (processedRequest != request);
  
  				// Determine handler for the current request.
                  // 这里获取方法执行链，HandlerExecutionChain mappedHandler 里面包含了拦截器的对象
  				mappedHandler = getHandler(processedRequest);
  				if (mappedHandler == null) {
  					noHandlerFound(processedRequest, response);
  					return;
  				}
  
  				// Determine handler adapter for the current request.
  				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
  
  				// Process last-modified header, if supported by the handler.
  				String method = request.getMethod();
  				boolean isGet = "GET".equals(method);
  				if (isGet || "HEAD".equals(method)) {
  					long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
  					if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
  						return;
  					}
  				}
  				// 执行前置拦截器，此时顺序是按照事先声明好的拦截器顺序执行
                  // this.interceptorIndex 对拦截器的个数进行了统计并保存了起来，等后置拦截器执行时候是使用逆序执行
  				if (!mappedHandler.applyPreHandle(processedRequest, response)) {
  					return;
  				}
  
  				// Actually invoke the handler.
  				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
  
  				if (asyncManager.isConcurrentHandlingStarted()) {
  					return;
  				}
  
  				applyDefaultViewName(processedRequest, mv);
                  // 调用后置处理器方法，若有异常则不会执行，这时候是按照逆序来执行的，具体看源码
  				mappedHandler.applyPostHandle(processedRequest, response, mv);
  			}
  			catch (Exception ex) {
  				dispatchException = ex;
  			}
  			catch (Throwable err) {
  				// As of 4.3, we're processing Errors thrown from handler methods as well,
  				// making them available for @ExceptionHandler methods and other scenarios.
  				dispatchException = new NestedServletException("Handler dispatch failed", err);
  			}
              // 渲染视图，若执行非异常直接调用AfterCopletion拦截器的方法，若异常则不会执行
  			processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
  		}
  		catch (Exception ex) {
              // 此时这时候这里会执行AfterCopletion拦截器的方法
  			triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
  		}
  		catch (Throwable err) {
  			triggerAfterCompletion(processedRequest, response, mappedHandler,
  					new NestedServletException("Handler processing failed", err));
  		}
  		finally {
  			if (asyncManager.isConcurrentHandlingStarted()) {
  				// Instead of postHandle and afterCompletion
  				if (mappedHandler != null) {
  					mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
  				}
  			}
  			else {
  				// Clean up any resources used by a multipart request.
  				if (multipartRequestParsed) {
  					cleanupMultipart(processedRequest);
  				}
  			}
  		}
  	}
  ```

  综上，无论如何，

## 二、其他知识

### 2.1. Spring知识

- ==autowired 是实现原理（UML）==
- ==bean生命周期源码及源码梳理（UML）==
- ==spring中 Filter和Interceptor的使用和区别==



### 2.3. WEB知识

#### 2.3.1. servlet

​	a. 关于servlet解释：server applet 服务端的轻量级小程序

​		tomcat的组成可以看成两部分，web服务器（将资源暴露到公网中以提供访问）和servlet（处理业务逻辑代码，后续又被分层，servlet+service+dao以提供解耦，等同于springmvc中的DispatcherServlet，而DispatcherServlet其实是继承了HttpServlet，只是在原来基础上又封装了一些逻辑）

​	b. servlet(<-javax.servlet.GenericServlet[abstract]<-HttpServlet[abstract])的

- servlet五个方法：

```java
 /*
 * <li>The servlet is constructed, then initialized with the <code>init</code> method.
 * <li>Any calls from clients to the <code>service</code> method are handled.
 * <li>The servlet is taken out of service, then destroyed with the
 * <code>destroy</code> method, then garbage collected and finalized.
 * </ol>
 */
public interface Servlet {
    /**
     * Called by the servlet container to indicate to a servlet that the
     * servlet is being placed into service.
     *
     * <p>The servlet container calls the <code>init</code>
     * method exactly once after instantiating the servlet.
     * The <code>init</code> method must complete successfully
     * before the servlet can receive any requests.
     *
     * <p>The servlet container cannot place the servlet into service
     * if the <code>init</code> method
     * <ol>
     * <li>Throws a <code>ServletException</code>
     * <li>Does not return within a time period defined by the Web server
     * </ol>
     *
     *
     * @param config a <code>ServletConfig</code> object
     * containing the servlet's configuration and initialization parameters
     *
     * @exception ServletException if an exception has occurred that
     * interferes with the servlet's normal operation
     *
     * @see UnavailableException
     * @see #getServletConfig
     */
    public void init(ServletConfig config) throws ServletException;

    /**
     * Returns a {@link ServletConfig} object, which contains
     * initialization and startup parameters for this servlet.
     * The <code>ServletConfig</code> object returned is the one
     * passed to the <code>init</code> method.
     *
     * <p>Implementations of this interface are responsible for storing the
     * <code>ServletConfig</code> object so that this
     * method can return it. The {@link GenericServlet}
     * class, which implements this interface, already does this.
     *
     * @return the <code>ServletConfig</code> object
     * that initializes this servlet
     *
     * <p>In addition to the life-cycle methods, this interface
     * provides the <code>getServletConfig</code> method, which the servlet
     * can use to get any startup information, and the <code>getServletInfo</code>
     * method, which allows the servlet to return basic information about itself,
     * such as author, version, and copyright.
     */
    // 返回一个servletConfig对象，里面包含了servlet的基本信息
    public ServletConfig getServletConfig();
    
```
    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--bean的配置文件位置在初始化时候进行加载,否则注入bean失败,ioc容器中不会有相应的bean组件的!-->
        <!--servlet的ServletConfig对象信息，在这里直接初始化了-->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>/WEB-INF/applicationContext.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>    
    ```
    
    /**
     * Called by the servlet container to allow the servlet to respond to
     * a request.
     *
     * <p>This method is only called after the servlet's <code>init()</code>
     * method has completed successfully.
     *
     * <p>  The status code of the response always should be set for a servlet
     * that throws or sends an error.
     *
     * <p>Servlets typically run inside multithreaded servlet containers
     * that can handle multiple requests concurrently. Developers must
     * be aware to synchronize access to any shared resources such as files,
     * network connections, and as well as the servlet's class and instance
     * variables.
     * More information on multithreaded programming in Java is available in
     * <a href="http://java.sun.com/Series/Tutorial/java/threads/multithreaded.html">
     * the Java tutorial on multi-threaded programming</a>.
     *
     *
     * @param req the <code>ServletRequest</code> object that contains
     * the client's request
     *
     * @param res the <code>ServletResponse</code> object that contains
     * the servlet's response
     *
     * @exception ServletException if an exception occurs that interferes
     * with the servlet's normal operation
     *
     * @exception IOException if an input or output exception occurs
     */
    // 每次请求到来时调用
    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException;
    
    /**
     * Returns information about the servlet, such
     * as author, version, and copyright.
     *
     * <p>The string that this method returns should
     * be plain text and not markup of any kind (such as HTML, XML,
     * etc.).
     *
     * @return a <code>String</code> containing servlet information
     */
    public String getServletInfo();
    
    /**
     * Called by the servlet container to indicate to a servlet that the
     * servlet is being taken out of service.  This method is
     * only called once all threads within the servlet's
     * <code>service</code> method have exited or after a timeout
     * period has passed. After the servlet container calls this
     * method, it will not call the <code>service</code> method again
     * on this servlet.
     *
     * <p>This method gives the servlet an opportunity
     * to clean up any resources that are being held (for example, memory,
     * file handles, threads) and make sure that any persistent state is
     * synchronized with the servlet's current state in memory.
     */
    public void destroy();
}

```

#### 2.3.2. javaweb三大组件

 - Servlet

   servlet(<-javax.servlet.GenericServlet[abstract]<-HttpServlet[abstract])

   - 因为GnericServlet和HttpServlet都是抽象方法，所以不提供实例话，springmvc通过FrameworkServlet 重写了HttpServlet的两个个请求方法doGet/doPost

     - 若没有重写这些方法

       ```java
       /**
        * Provides an abstract class to be subclassed to create
        * an HTTP servlet suitable for a Web site. A subclass of
        * <code>HttpServlet</code> must override at least
        * one method, 
        
        * <p>There's almost no reason to override the <code>service</code>
        * method. <code>service</code> handles standard HTTP
        * requests by dispatching them to the handler methods
        * for each HTTP request type (the <code>do</code><i>XXX</i>
        * methods listed above).
        */
       public abstract class HttpServlet extends GenericServlet
               implements java.io.Serializable {
       // HttpServletResponse.SC_METHOD_NOT_ALLOWED 405
       // HttpServletResponse.SC_BAD_REQUEST 400
       protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                   throws ServletException, IOException {
               String protocol = req.getProtocol();
               String msg = lStrings.getString("http.method_get_not_supported");
               if (protocol.endsWith("1.1")) {
                   resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
               } else {
                   resp.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
               }
           }
       }
```

 - Filter

   用到了责任链模式

 - Listener

   观察者模式

- spring-mvc使用tomcat启动三大组件关系

  web.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
           version="4.0">
      <listener>
          <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
      </listener>
      <servlet>
          <servlet-name>dispatcher</servlet-name>
          <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
          <!--bean的配置文件位置在初始化时候进行加载,否则注入bean失败,ioc容器中不会有相应的bean组件的!-->
          <!--servlet的ServletConfig对象信息，在这里直接初始化了-->
          <init-param>
              <param-name>contextConfigLocation</param-name>
              <param-value>/WEB-INF/applicationContext.xml</param-value>
          </init-param>
          <load-on-startup>1</load-on-startup>
      </servlet>
      <servlet-mapping>
          <servlet-name>dispatcher</servlet-name>
          <url-pattern>/</url-pattern>
      </servlet-mapping>
  </web-app>
  ```

  

  

  ![tomcat执行伪代码](image\tomcat执行伪代码.jpg)![tomcat内部组件执行顺序](image\tomcat内部组件执行顺序.png)

### 2.4. JAVA基础

- 装饰类整理和io流装饰类整理（xmind）

- 复习动态代理

  - JDK针对某个接口:
    - Proxy.newInstance(ClassLoader loader, Class<?>[] interface, InvacationHandler hander)
    - InvacationHandler<-invoke(Proxy proxy, Method method, Object[] args)

  - Cglib针对某个具体的类:
    - Enhancer eh = new Enhancer(); // 增强 
    - enhancer.setSuperclass(class); // 若是接口则使用接口，否则是否是Object，否则使用其本身
    - enhancer.setCallback(MethodInterceptor()); // 映射对应的回调方法
    - (T) enhancer.create(); // 创建代理对象

- 复习异常常见类型（xmind）及finally执行返回、赋值与try return中返回值的关系（coding）

  - 异常类型

    ![异常类型](image\异常类型.png)

  - try finally 与 return

    ```java
    	方法一：
    	// 无异常，执行顺序为 先执行finally但是返回value=10
    	public static Integer getValueWithoutString() {
            int value = 10;
            try {
                return value;
            } finally {
                value = 4;
                System.out.println("无异常执行finally返回值：" + value);
            }
        }
    	方法二：
    	// 无异常，最后返回 7
    	public static Integer getValueWithException() {
            int value = 10;
            try {
                value = 5;
                return value+1;
            } finally {
                return value+2;
            }
        }
    	方法三：
    	// 有异常，最后返回 1234 finally 
    	public static String getStringWithException() {
            String value = "1234";
            try {
                return value + " try ";
            } finally {
                return value + " finally ";
            }
        }
    	方法四：
    	// 有异常，最后返回 1234 try  finally 
        public static String getStringWithoutException() {
            String value = "1234";
            try {
                value = value + " try ";
                return value;
            } finally {
                return value + " finally ";
            }
        }
    ```

    以上实例主要阐述了：

    - 方法一和方法二中，在返回前会声明一个返回值的栈，当try执行return时，会把value值写入这个栈（基本类型），若finally中无return则此时返回值无需修改，直接返回即可；若finally中有return则返回修改的值；finally一定会在方法结束前执行！

    - 方法一和方法四中：如果针对基本类型，返回的是实际的值是实现开辟好的基本类型的栈；若非基类，则返回的是对象的引用，因此会按照finally最后对象引用之返回！

### 2.5. 其他

#### 2.5.1. 查看认证权限的用户id是怎么带入到业务代码中的

​	通过实现Filter接口，重写doFilter方法，获取请求头的token信息，然后解析token获取userid后重新set到parameterRequestWrapper（是HttpServletRequest的实现类）对象中

## 三、设计模式

### 3.1. 工厂模式

- 多种工厂模式梳理

### 3.2. 模板方法模式

​	定义了执行的框架，将一些步骤延迟到之类中，模板方法使得子类可以在不改变算法结构的情况下重新定义算法中的某些步骤。

**特点：**

	- 框架规范了算法执行的顺序
	- 对需要重写的子类提供了开放，可以针对不同业务逻辑定制自己的处理方法
	- 提供了hook功能，根据子类选择需要执行的方法

**比较常用的情景：**

	- 实现comparable接口的，使用Arrays.sort()自定义排序
	- servlet的五个方法，httpServlet被springframeworkServlet实现改写

## 四、待办清单

### 3.1.重点知识待办

- 