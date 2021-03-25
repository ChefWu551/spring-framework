# 一、Spring Framework

## 一、spring

### 1.1. IOC容器篇
#### 1.1.1. xml 篇

- 实验一：bean初始化及标签scope的使用；

  - bean初始化对象时，-<property>标签声明的属性对应的属性值是直接调用getter和setter的，而不是通过构造方法来实现的

    property赋值属性源码调用链：

    ClassPathXmlApplicationContext 加载所有的xml配置的组件信息，里面包含了bean的初始化，运行构造方法，调用refresh()来解析xml，获取对应的bean信息，调用beanFactory.preInstantiateSingletons()来初始化所有非懒加载的单例bean，判断是否是工厂bean，若不是，直接通过getBean来获取对应的bean信息，getBean会从

    ```java
    /** Cache of early singleton objects: bean name to bean instance. */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);
    ```

    以上 二级缓存中获取是否含有bean，若没有则会调用doCreateBean()来创建对应的bean信息，随后通过BeanWrapper bw对象的setPropertyValues()对对象的属性进行赋值，这一过程是通过反射调用对象方法set属性值进行赋值的。

    调用链代码：

    ```apl
    refresh()->beanFactory.preInstantiateSingletons()->getBean(beanName);->doGetBean(name, null, null, false);
    ->singletonFactory.getObject();->doCreateBean(beanName, mbdToUse, args);->applyPropertyValues(beanName, mbd, bw, pvs);
    ->bw.setPropertyValues(new MutablePropertyValues(deepCopy));->ma.invoke(obj, args);
    ```

  - bean初始化对象时，-<constructor-arg>标签对应的是构造方法的有参个数，必须一一对应，否则编译错误

- 实验二：构造方法初始化bean及标签index顺序赋值使用；

  ```xml
  <bean class="com.mountain.model.BookNum" id="book01">
      <constructor-arg value="红楼梦" index="0"/>
      <constructor-arg value="曹雪芹" index="1"/>
      <constructor-arg value="45" index="2"/>
  </bean>
  ```

- 实验三：泛型容器的赋值**list、map、List<Object>;**标签ref的外部引用；parent继承属性及修改对应的bean属性值[指向性，所以当重新对对象赋值后，原对象的属性值也会跟着改变]；

  ```xml
  <bean class="com.mountain.model.Person" id="person01">
      <property name="lastName" value="小明"/>
      <property name="age" value="12"/>
      <property name="list">
          <list>
              <value>123</value>
              <value>book</value>
          </list>
      </property>
      <property name="maps">
          <map>
              <entry key="key1" value="value1"/>
              <entry key="key2" value="value2"/>
              <!--<entry key="key3" key-ref="book" value=""></entry>-->
          </map>
      </property>
      <!--可直接声明引用，引用的是同一对象，可修改对象的属性值-->
      <!--<property name="book" ref="book01">-->
      <property name="book">
          <!--parent：对某个bean的引用，相当于类的extend，不是同一个对象！-->
          <bean class="com.mountain.model.Book" parent="book">
              <property name="bookName" value="西游记"/>
              <property name="age" value="13"/>
          </bean>
      </property>
      <property name="books">
          <list>
              <bean class="com.mountain.model.Book">
                  <property name="bookName" value="books01"/>
                  <property name="author" value="author01"/>
              </bean>
              <bean class="com.mountain.model.Book">
                  <property name="bookName" value="books02"/>
                  <property name="age" value="100"/>
              </bean>
          </list>
      </property>
  </bean>
  ```

- 实验四：abstract声明true时候不会生成对应的bean，此时对象只能作为模板，被其他bean所引用；

  ```xml
  <bean class="com.mountain.model.BookNum" id="bookTemplate" abstract="true">
      <constructor-arg value="bookNameTemp"/>
      <constructor-arg value="authorTemp"/>
      <constructor-arg value="12"/>
  </bean>
  ```

  拓展：这里就是实验一中提到的：

  ```java
  public void preInstantiateSingletons() throws BeansException {
     if (logger.isTraceEnabled()) {
        logger.trace("Pre-instantiating singletons in " + this);
     }
  
     // Iterate over a copy to allow for init methods which in turn register new bean definitions.
     // While this may not be part of the regular factory bootstrap, it does otherwise work fine.
     List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);
  
     // Trigger initialization of all non-lazy singleton beans...
     for (String beanName : beanNames) {
        RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
         // 执行在这里，因为声明的是个抽象的模板bean
        if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
           if (isFactoryBean(beanName)) {
              Object bean = getBean(FACTORY_BEAN_PREFIX + beanName);
              if (bean instanceof FactoryBean) {
                 final FactoryBean<?> factory = (FactoryBean<?>) bean;
                 boolean isEagerInit;
                 if (System.getSecurityManager() != null && factory instanceof SmartFactoryBean) {
                    isEagerInit = AccessController.doPrivileged((PrivilegedAction<Boolean>)
                                ((SmartFactoryBean<?>) factory)::isEagerInit,
                          getAccessControlContext());
                 }
                 else {
                    isEagerInit = (factory instanceof SmartFactoryBean &&
                          ((SmartFactoryBean<?>) factory).isEagerInit());
                 }
                 if (isEagerInit) {
                    getBean(beanName);
                 }
              }
           }
           else {
              getBean(beanName);
           }
        }
     }
  
     // Trigger post-initialization callback for all applicable beans...
     for (String beanName : beanNames) {
        Object singletonInstance = getSingleton(beanName);
        if (singletonInstance instanceof SmartInitializingSingleton) {
           final SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton) singletonInstance;
           if (System.getSecurityManager() != null) {
              AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                 smartSingleton.afterSingletonsInstantiated();
                 return null;
              }, getAccessControlContext());
           }
           else {
              smartSingleton.afterSingletonsInstantiated();
           }
        }
     }
  }
  ```

- 实验五：三种初始化bean的方法：静态工厂方法、实例化工厂方法、bean的直接初始化；bean的生命周期；

  - 静态工程方法

    通过静态方法直接getInstance，获取一个new的对象即可

    ```java
    public static Book getBookInstance(String bookName) {
        System.out.println("静态工厂方法执行");
        return new Book(bookName);
    }
    ```

  - 实例工厂方法

    ```java
    public Book getInstance(String bookName, String author, int age) {
        return new Book(bookName, author, age);
    }
    ```

    通过实例对象，获取一个new方法来实现

  - bean的直接初始化

    直接new一个自己，或者在bean中配置都可以

  - **==bean的生命周期==**

- 实验六：为bean初始化过程添加后置处理器，继承BeanPostProcessor

  - 主导bean生成前该做什么，bean生成后又改做什么
  - 实现：
    - 实现BeanPostProcessor
    - 添加@Component注解告诉bean这是个一个组件或者在xml中配置对应的组件即可（工厂模式，工厂模式很重要！）

- 实验七：SpLE(spring expression language)语法知识

  - ${book.name}引用
  - #{50*20}带计算表达式

- 实验八：基于xml的自动装配，autowire标签

  - 原理：利用反射扫描实现对引用对象的初始化
  - ==源码：==

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

## 二、spring-mvc

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

  - 若需要根据传入类型和传出类型找到对应的converter

    ```java
    // GenericConversionService
    public GenericConverter find(TypeDescriptor sourceType, TypeDescriptor targetType) {
       // Search the full type hierarchy
       List<Class<?>> sourceCandidates = getClassHierarchy(sourceType.getType());
       List<Class<?>> targetCandidates = getClassHierarchy(targetType.getType());
       for (Class<?> sourceCandidate : sourceCandidates) {
          for (Class<?> targetCandidate : targetCandidates) {
             ConvertiblePair convertiblePair = new ConvertiblePair(sourceCandidate, targetCandidate);
             GenericConverter converter = getRegisteredConverter(sourceType, targetType, convertiblePair);
             if (converter != null) {
                return converter;
             }
          }
       }
       return null;
    }
    ```
  
  - 最后通过GeneriConversionService.convert()->Converter<S, T>.convert()->BookParamConverter.convert()完成类型转换
  
  #### 

#### 1.4.7. spring-@RequestBody与@ResponseBody

- @RequestBody

  ```java
  
  /**
   * Annotation indicating a method parameter should be bound to the body of the web request.
   * The body of the request is passed through an {@link HttpMessageConverter} to resolve the
   * method argument depending on the content type of the request. Optionally, automatic
   * validation can be applied by annotating the argument with {@code @Valid}.
   *
   * <p>Supported for annotated handler methods.
   *
   * @author Arjen Poutsma
   * @since 3.0
   * @see RequestHeader
   * @see ResponseBody
   * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
   */
  
  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface RequestBody {
  ```

  - 使用目标是请求的参数，将请求的参数组装成对象
  - 使用注意事项：
    - 需要声明请求体是json格式：HEADER: Content-Type:application/json
    - 需要引入jsckson来对消息转换做支持，否则提示方法报错。

- @ResponseBody

  ```java
  /**
   * Annotation that indicates a method return value should be bound to the web
   * response body. Supported for annotated handler methods.
   *
   * <p>As of version 4.0 this annotation can also be added on the type level in
   * which case it is inherited and does not need to be added on the method level.
   *
   * @author Arjen Poutsma
   * @since 3.0
   * @see RequestBody
   * @see RestController
   */
  @Target({ElementType.TYPE, ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface ResponseBody {
  ```

  - 用于标注返回值的，直接返回结果

#### ==1.4.8. spring 文件上传与下载==

#### 1.4.9. 自定义HandlerInterceptor与源码解析

- 步骤一：编写拦截器代码，继承HandlerInterceptor

  ```java
  public class MyHandlerInterceptor implements HandlerInterceptor {
  
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
              throws Exception {
          System.out.println("MyHandlerInterceptor...preHandle");
          return true;
      }
  
      @Override
      public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                             @Nullable ModelAndView modelAndView) throws Exception {
          System.out.println("MyHandlerInterceptor...postHandle");
      }
  
      @Override
      public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                  @Nullable Exception ex) throws Exception {
          System.out.println("MyHandlerInterceptor...afterCompletion");
      }
  }
  ```

- 步骤二：配置到spring-mvc中

```xml
<mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/interceptor"/>
            <bean class="com.mountain.interceptor.MySencondHandlerInterceptor"/>
        </mvc:interceptor>
        <mvc:interceptor>
            <mvc:mapping path="/interceptor"/>
            <bean class="com.mountain.interceptor.MyHandlerInterceptor"/>
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

  综上，无论如何，再拦截器执行后，AfterCompletion 是一定会被执行的。

#### ==1.4.10. 国际化ResourceBudleMessageSource==

- 配置bean实现返回内容国际化
- 更改浏览器语言测试功能
- 源码调试页面渲染该过程
- 在controller层使用MessageResolver获取对象，使用getMessage获取不统语言的调试
- 根据入参不同（zh/eng） 返回对应国际化的内容的两种方式
  - 使用httpResquestServlet
  - 使用 Local对象

#### ==1.4.11. spring异常HandlerExceptionResolver==

- 三种默认异常

  - ExceptionHandlerExceptionResolver

  - ResponseStatusExceptionResolver

  - DefaultHandlerExceptionResolver

 - 源码调试

 - 自定义异常@ExceptionHandler/@ControllerAdvice

   - 标注在业务类上面@ExceptionHandler
   - 抽象成通用异常处理@ControllerAdvice
   - 看以上异常捕获生效情况
   - 使用@ResponseStatus定义返回自定义内容

#### ==1.4.12.梳理spring-mvc 运行流程==

doDispatch->往后

#### ==1.4.13.spring-mvc整合spring框架==

- 多个ioc容器执行
- 两个ioc容器配合执行
- 父子容器执行依赖注入关系

#### 

# 二、其他知识

### 2.1. Spring知识

- ==autowired 是实现原理（UML）==
- ==bean生命周期源码及源码梳理（UML）==
- ==spring三级缓存知识梳理，源码调试==
- spring中 Filter和Interceptor的使用和区别
  - filter为java web自带的组件(javax.servlet.Filter)，功能比较单一，且必须使用web.xml进行配置，因为他是非ioc组件不能通过@Component来初始化到ioc容器内
  - interceptor是ioc组件，有多种处理方式，preHandler、postHandler、afterCompletion

- @RequestParam和@PathVariable的区别

  - @RequestParam：简单的说，就是用来映射请求参数的，若请求参数是个map对象会自动生成对应的key和value

    ```java
    /**
     * Annotation which indicates that a method parameter should be bound to a web
     * request parameter.
     *
     * <p>Supported for annotated handler methods in Spring MVC and Spring WebFlux
     * as follows:
     */
    
    ```

  - @PathVariable 从uri中获取请求参数，符合rest api的请求风格(should be bound to a URI template variable)

    ```java
    /**
     * Annotation which indicates that a method parameter should be bound to a URI template
     * variable. Supported for {@link RequestMapping} annotated handler methods.
     *
     * <p>If the method parameter is {@link java.util.Map Map&lt;String, String&gt;}
     * then the map is populated with all path variable names and values.
     */
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface PathVariable {
    ```

    ```java
    // 使用示例
    @RequestMapping("pathVariable/{name}")
    public JSONObject pathVariable(@PathVariable String name, @PathVariable Map<String, Integer> character) {
        System.out.println("name: " + name + "; character: " + character);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "王刚");
        jsonObject.put("age", 23);
        return jsonObject;
    }
    ```

- @Controller和@RestController的区别

  - @Controller 控制器，是component组件中的一种，用于声明当前的类是一个控制器（web控制器）
  - @RestController
    - 继承了@Controller控制器
    - 继承了@ResponseBody控制器
    - 综上两点，就很能说明问题了，带有controller控制器的属性，且直接返回的是请求体内容，而不是某个具体的页面。

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

# 三、设计模式

## 3.1. 工厂模式

- 多种工厂模式梳理

## 3.2. 模板方法模式

​	定义了执行的框架，将一些步骤延迟到之类中，模板方法使得子类可以在不改变算法结构的情况下重新定义算法中的某些步骤。

**特点：**

	- 框架规范了算法执行的顺序
	- 对需要重写的子类提供了开放，可以针对不同业务逻辑定制自己的处理方法
	- 提供了hook功能，根据子类选择需要执行的方法

**比较常用的情景：**

	- 实现comparable接口的，使用Arrays.sort()自定义排序
	- servlet的五个方法，httpServlet被springframeworkServlet实现改写

## 3.3. 单例模式

- 单例模式的多种实现方法
- 根据业务场景来使用不同的单例bean

# 四、MyBatis

## 4.1. mybatis与Hibernate区别及适用场景

## 4.2. MyBatis

### 4.2.1. 根据官方文档配置log4j

### 4.2.2. mybatis全局配置文件及启动项目

- 导入全局配置的dtd标签提示文件

- namespace

- setting
  - 缓存
  - 懒加载
  - 驼峰
  - 等，参照官方文档
  
  ```xml
  <settings>
      <!--日志标签:指定 MyBatis 所用日志的具体实现，未指定时将自动查找。-->
      <setting name="logImpl" value="LOG4J"/>
      <setting name="cacheEnabled" value="true"/>
      <setting name="mapUnderscoreToCamelCase" value="false"/>
      <setting name="lazyLoadingEnabled" value="true"/>
  </settings>
  ```
  
- environment 
  
  - datasource
  - transactionManager
  - 指定不同环境使用的数据库配置-全局配置文件
  - 指定不同db使用的sql配置-mapper层
  
  ```xml
  <environments default="development">
      <environment id="development">
          <!--
                  type:
                      JDBC – 这个配置直接使用了JDBC的提交和回滚设施，它依赖从数据源获得的连接来管理事务作用域。
                      MANAGED – 这个配置几乎没做什么。它从不提交或回滚一个连接，而是让容器来管理事务的整个生命周期（比如 JEE 应用服务器的上下文）。
                          默认情况下它会关闭连接。然而一些容器并不希望连接被关闭，因此需要将 closeConnection 属性设置为 false 来阻止默认的关闭行为。
               -->
          <transactionManager type="JDBC"/>
          <dataSource type="POOLED">
              <property name="driver" value="${mysql.driver}"/>
              <property name="url" value="${mysql.url}"/>
              <property name="username" value="${mysql.username}"/>
              <property name="password" value="${mysql.password}"/>
          </dataSource>
      </environment>
  </environments>
  ```
  
- 引入外部文件标签 property

  ```xml
  <properties resource="mysql.properties"></properties>
  ```

### 4.2.3. 使用sqlsessionFactory进行测试

- 获取sqlsessionFactory

  ```java
  String resource = "mybatis-config.xml";
  InputStream inputStream = Resources.getResourceAsStream(resource);
  sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
  ```

- 执行查询语句

  ```java
  SqlSession session = sqlSessionFactory.openSession();
  TestMybatisMapper mybatisMapper = session.getMapper(TestMybatisMapper.class);
  System.out.println("学生姓名是： " + mybatisMapper.selectStudentNameById(5));
  ```

### 4.2.4. ==typerhandler类型处理器==

- 可通过实现TypeHandler接口或BaseTyperHander重写
- 把自定义的类型处理器加入到mybatis.xml文件中

### 4.2.5. ObjectFactory查询出数据封装对象ORM

- dao层

  ```java
  public interface TestMybatisMapper {
      Student selectStudentById(Integer id);
  }
  ```

- mapper层

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.mountain.mapper.TestMybatisMapper">
  
      <resultMap id="student" type="com.mountain.model.Student">
          <id column="id" property="id"/>
          <result column="student_name" property="studentName"/>
          <result column="student_age" property="studentAge"/>
      </resultMap>
  
      <select id="selectStudentById" resultMap="student">
          select id, student_name, student_age from student where id = #{id}
      </select>
  </mapper>
  ```

- 测试

  ```java
  @Test
  public void printSelectStudentById() {
      SqlSession session = sqlSessionFactory.openSession();
      TestMybatisMapper mybatisMapper = session.getMapper(TestMybatisMapper.class);
      System.out.println("学生信息是： " + mybatisMapper.selectStudentById(5));
  }
  ```

### 4.2.6. ==Mybatis插件使用==

​	可以修改mybatis核心功能，插件通过动态代理机制可以介入四大对象任意执行方法

- Executor
- ParameterHandler
- ResultSetHander
- StatementHandler

### 4.2.7. Mapper层的三个引用

- URL: 可以引用外部的文件
- Resource：项目内部路径文件的引用
- class：指向的是DAO接口层，mybatis会根据接口层的接口生成对应的javaBean

### 4.2.8. Mybatis获取自增主键及非自增主键id获取

- userGenerationKey
- keyProperty
- selectKey标签order属性、resultType属性、keyProperty属性

### 4.2.9. mapper查询参数

- 一个参数
- 多个参数
- 传入pojo
- map
- 对象嵌套对象的引用

### 4.2.10. 动态传参${}和#{}区别

|      | #{}                         | ${}                                  |
| ---- | --------------------------- | ------------------------------------ |
|      | 为参数占位符？，sql预编译   | 为字符串替换，sql拼接                |
|      | 变量替换是在DBMS 中         | 变量替换是在 DBMS 外                 |
|      | 对应的变量自动加上单引号 '' | 对应的变量不会加上单引号 ''          |
|      | 能防止sql 注入              | 不能防止sql 注入(student="x" or 1=1) |

```xml
<select id="selectStudentByNameAndAge" resultMap="student">
    select id, student_name, student_age from student where student_age = #{age} and student_name = '${studentName}'
</select>
```

```log
[DEBUG] 2021-03-24 21:57:58,337 org.apache.ibatis.transaction.jdbc.JdbcTransaction - Setting autocommit to false on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@77fbd92c]
[DEBUG] 2021-03-24 21:57:58,339 com.mountain.mapper.TestMybatisMapper.selectStudentByNameAndAge - ==>  Preparing: select id, student_name, student_age from student where student_age = ? and student_name = 'xiaoming' 
[DEBUG] 2021-03-24 21:57:58,378 com.mountain.mapper.TestMybatisMapper.selectStudentByNameAndAge - ==> Parameters: 12(Integer)
[DEBUG] 2021-03-24 21:57:58,403 com.mountain.mapper.TestMybatisMapper.selectStudentByNameAndAge - <==      Total: 1
学生信息是： Student(id=1, studentName=xiaoming, studentAge=12)
```

由上面执行语句可知在prepareStatement的时候，${}已经完成了值的替换，而#{}则是通过占位符，进行预编译处理（对jdbc类型自动转换，字符串自动添加双引号，数值型直接赋值）

${} 应用场景：可以用于动态表 例如 log_2 或者 log_1 这种替换，主要用于非传参值的表名，字段名替换

**编译和预编译：**

- 预编译：预编译又称为预处理，是做些代码文本的替换工作。处理#开头的指令，比如拷贝#include包含的文件代码，#define宏定义的替换,条件编译等，就是为编译做的预备工作的阶段主要处理#开始的预编译指令。
- 编译：
  - 利用编译程序从源语言编写的源程序产生目标程序的过程。
  - 用编译程序产生目标程序的动作。 编译就是把高级语言变成计算机可以识别的2进制语言，计算机只认识1和0，编译程序把人们熟悉的语言换成2进制的

### 4.2.11. sql查询返回集合处理

- 查询返回list

  

- 返回map

- 查询多条记录封装map

### 4.2.12. 自定义字段名映射

- resultMap
  - 标签id
  - 标签result
- resultMap与resultType

### 4.2.13. 标签association使用

- 联查对象
- 一对多、多对多、多对一

### 4.2.14. 开启查询的按需加载

- lazy
- eager

### 4.2.15. Mybatis动态sql

- 标签if
- 标签where
- 标签trim
- 标签foreach
  - collection
  - item
  - close
  - open
  - seperator
  - index
    - 针对`map`
    - 针对list

- 标签set
- 标签choose

### 4.2.16. Mybatis一级缓存

- 失效情况
- 源码
  - 接口cache-perpetualCache-Map缓存对象
  - BaseExecutor-query方法
  - 源码调试



