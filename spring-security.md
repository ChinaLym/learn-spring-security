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