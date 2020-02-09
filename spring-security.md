# spring security 介绍

## 授权相关
### [@EnableGlobalMethodSecurity](https://docs.spring.io/spring-security/site/docs/5.2.2.BUILD-SNAPSHOT/reference/htmlsingle/#enableglobalmethodsecurity)

Spring Security默认是禁用注解的，需要手动开启，作用是激活注解AOP校验，以方便Controller层代码判断用户是否具有访问权限 

- `securedEnabled = true` 开启 `@Secured` 注解过滤权限，根据[官方文档](https://docs.spring.io/spring-security/site/docs/5.2.2.BUILD-SNAPSHOT/reference/htmlsingle/#el-access) 介绍
    - Spring Security允许我们在定义URL访问或方法访问所应有的权限时使用Spring EL表达式，
    在定义所需的访问权限时如果对应的表达式返回结果为true则表示拥有对应的权限，反之则无。
    Spring Security可用表达式对象的基类是SecurityExpressionRoot，
    其为我们提供了如下在使用Spring EL表达式对URL或方法进行权限控制时通用的内置表达式。
    - 参见 [官方文档](https://docs.spring.io/spring-security/site/docs/5.2.2.BUILD-SNAPSHOT/reference/htmlsingle/#el-access)

- `jsr250Enabled = true`开启@RolesAllowed 注解过滤权限（JSR-250注释不支持元注释。）
    - `@RolesAllowed({"USER", "ADMIN"})`
        需要具有"USER", "ADMIN"任意一种角色。可以省略前缀`ROLE_`，也可以是`ROLE_ADMIN`
    - `@DenyAll` 
            拒绝所有访问    
    - `@PermitAll`
        - 允许所有访问    

- `prePostEnabled = true` 激活4个表达式注解

    - `@PreAuthorize`
        - 在方法调用之前,基于表达式的计算结果来限制对方法的访问
        - 这里可以调用方法的参数，也可以得到参数值，这是利用JAVA8的参数名反射特性，如果没用JAVA8，那么也可以利用Spring Security的@P标注参数，或者Spring Data的@Param标注参数
        - ```java
            @PreAuthorize("#userId == authentication.principal.userId or hasAuthority(‘ADMIN’)")
            void changePassword(@P("userId") long userId ){...}
         ```
        - 参数userId的值是否等于principal中保存的当前用户的userId，或者当前用户是否具有ROLE_ADMIN权限
         
    - `@PostAuthorize`
        - 允许方法调用,但是如果调用后表达式计算结果为false,将抛出一个安全性异常
        - 方法执行之后执行，而且这里可以调用方法的返回值，如果EL为false，那么该方法也已经执行完了，可能会回滚。EL变量returnObject表示返回的对象。
          `@PostAuthorize("returnObject.userId == authentication.principal.userId or hasPermission(returnObject, 'ADMIN')");`
    - `@PreFilter` 允许方法调用,但必须在进入方法之前过滤输入值
        - 在方法执行之前执行，而且这里可以调用方法的参数，然后对参数值进行过滤或处理或修改，EL变量filterObject表示参数，如有多个参数，使用filterTarget注解参数。只有方法参数是集合或数组才行。（很少会用到，与分页技术不兼容）
    - `@PostFilter` 允许方法调用,但必须按照表达式来过滤方法的结果
        - 类似 `@PreFilter`

### 元注解
```java
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("#contact.name == authentication.name")
 public  @interface MyPermission {}
``` 
用 `@MyPermission` 替代 `@PreAuthorize("#contact.name == authentication.name")`

## 源码帮助

Spring Security 中比较具有特色的就是 DSL，这个设计用起来非常灵活，但却让刚接触的小白十分头疼，一脸懵逼，写代码的时候各种搜索复制粘贴，好坏都有。DSL 设计的关键就是泛型，看Spring Security 代码你会发现他用泛型用上瘾了。

- `SecurityBuilder<O>`
	- 他只有一个方法 `O build() throws Exception`，可以看到这个接口的作用就是创建一个对象

- `interface SecurityConfigurer<O, B extends SecurityBuilder<O>>`
    - SecurityConfigurer 是 Spring Security dsl体系中最核心的一个接口，几乎 Spring Security 所有的dsl都围绕它来设计的
	- 他包含两个泛型，第一个O是object，相当于待配置的对象，第二次B表示一个O的Builder
	- 包含两个方法
		- `init(B builder)`
			- 框架会先调用该方法，做一些初始化的动作
		- `configure(B builder)`**常用**
			- 用户在该方法里通过builder配置。框架会调用该方法
	
- 	`abstract class SecurityConfigurerAdapter<O, B extends SecurityBuilder<O>> implements SecurityConfigurer<O, B>`
	- `SecurityConfigurer` 的其中一个主要基类
	- 这个类里有两个变量，一个就是 builder，另一个是 List<ObjectPostProcessor>，负责在创建完毕后执行后置操作
	- 除继承的外关键方法
		- `and()`**常用**
			- 返回 builder，方便继续配置
		- `protected postProcess()`	
			- 注意该方法是保护范围的，因此该方法应该在子类中调用
			
			

	
- `interface ObjectPostProcessor<T>`
	- `<O extends T> O postProcess(O object)`


- `abstract class SecurityConfigurerAdapter`
	- `SecurityConfigurer` 基础的抽象类
	
- interface `WebSecurityConfigurer` extends SecurityConfigurer
    - 标记接口，无任何代码，Spring Security web方面比较核心的一个配置类，`WebSecurityConfigurerAdapter` 是它唯一的直接实现
    
- abstract class `WebSecurityConfigurerAdapter` implements WebSecurityConfigurer<WebSecurity>
    - 

    
Spring security 有一套 dsl，其源码命名比较规范，如下:

- `final class AAAaaaConfigurer`
    - DSL 模型类，一般位于`configurers`包路径下。通常负责AAA 模块 aaa部分的配置项，`XxxConfigurer`接口中`configure`方法的参数，一般可以通过 aaa.dsl()进行配置
    - 一般会伴有 `final class AAAaaaConfigurer`、`final class AAAbbbConfigurer`、`final class AAAcccConfigurer`
    
- `interface AAAConfigurer`
    - 继承了配置项的接口，一般带有 `configure(AAAaaaConfigurer aaa)`，参数的类型就是上面提到的`final`类型的配置项
    
- `class AAAConfigurerAdapter`
    - 实现了 `AAAConfigurer`，作为`AAAConfigurer` 接口的适配器，如`SecurityConfigurerAdapter`实现了 `SecurityConfigurer`
    - 用户代码中应继承这种(`AAAConfigurerAdapter`)，实现自定义的配置

- `class AAAConfiguration`
    - 继承了 `AAAConfigurerAdapter`，通常这种类应该由用户代码提供。
    - 但为了演示如何使用，以及省略通用场景下的配置代码，Spring 提供了默认实现，且是自动配置的基础。
        如果用户没有继承 `AAAConfigurerAdapter` 并注入到 Spring 中，那么 Spring 就会使用缺省配置。
