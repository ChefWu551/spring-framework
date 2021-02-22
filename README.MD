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

- 实验一: 通过注解创建Dao service controller
- 实验二: 使用context:include-filter指定扫描包时要包含的类
- 实验三：使用context:exclude-filter指定扫描包是不包含的类
- 实验四：DI依赖注入
- 实验五：测试泛型依赖注入[实践及画图]
- ==任务六：ioc源码，调试ioc容器[画画流程图、xmind图来看清楚每个方法做什么]==
  - 通过打印内容找到对应的ioc创建bean的关键代码
  - 保存单例的HashMap的一级缓存
  - ApplicationContext和BeanFactory的区别

### 1.2. AOP

#### 1.2.1. 注解篇

- ==任务一：实现注解切面配置（`@Before`/`@AfterReturning`/`@AfterThrowing`/`@After`）及官方词语详解==
- ==任务二：spring除了默认`java.lang.reflect.Proxy`还有使用的AOP增强包(`springSource.cglib`)[接口代理转类代理]==
- ==任务三：切入点表达式`@PointCut(excution(public int com.mountain.*.*(int, int)))`==
- ==任务四：通知方法的执行顺序`@Order`与切面嵌套图分析(多切面顺序分析)==
- ==任务五：对切面的方法及返回值进行打印日志==
- ==任务六：环绕通知`@Around`且环绕通知与其他通知的执行顺序，多切面的执行顺序==
- ==任务七：使用xml实现和注解一样的功能==

### 1.3. spring-jdbcTemplate

- ==任务一：jdbcTemplate的增删改查==
- ==任务二：配置事务管理器控制事务的一致性==

## 二、重点知识梳理

### 1.1.2. [实验四：DI依赖注入]()

![spring-泛型类调用](image\spring-泛型类调用.png)

## 三、待办清单

### 3.1.重点知识待办

- autowired 是实现原理
- bean生命周期源码及源码梳理
- 