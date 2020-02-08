# jwt 知识介绍

jwt(json web token)，本质上也是token，它是 json 形式用于 web 领域的 token。

用途：使用 jwt 替代传统 sessionId 的方案。

## 回顾 Session 方案

在浏览器上访问服务器后，浏览器与服务器建立连接后，服务器创建一个 sessionId，

然后在内存中有一个类似 Map的数据结构(key为sessionId，value为Map<String,Object>)来保存 session 相关的信息，如用户信息，

在给浏览器返回响应时，按照http约定的方案通知浏览器，将sessionId=xxx保存至cookie中

浏览器收到后，将sessionId 保存至cookie，以后每次访问该服务器，都会将sessionId带在cookie中。

服务器再次收到请求时，先从cookie中找sessionId，找到后，再根据sessionId就能拿到与session相关的信息了

## 演变过程

### session 方案缺点
- 无法轻松水平扩展
    - 因为session信息是保存在内存中的
- 如果保存在外部，则会依赖外部存储
    - 如保存在数据库、redis。虽然这种方案架构清晰，但实现较为麻烦，增加了任务量，且有以下两个主要缺点：
    - 性能问题，保存在外部后，增加了网络IO
    - 会导致强依赖数据库或redis，若该服务不可用，则整个网站将不可用。
    
### 解决方案：

把信息保存在客户端，客户端每次请求时带着保存的信息。

假设需要保存的信息json形式如下
```json
{
"UserName": "Chongchong",
"Role": "Admin",
"Expire": "2018-08-08 20:15:56"
}
```
json作为token这样就可以叫 token 了，但还不够完善，不是 `JWT` 标准的格式，在标准的 JWT 中数据部分称之为载荷 `PayLoad`。

**载荷**就是存放有效信息的地方,有效信息包含三个部分:

- 标准中注册的声明（建议但不强制使用）
    - iss: jwt签发者
    - sub: jwt所面向的用户
    - aud: 接收jwt的一方
    - exp: jwt的过期时间，这个过期时间必须要大于签发时间
    - nbf: 定义在什么时间之前，该jwt都是不可用的.
    - iat: jwt的签发时间
    - jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
- 公共的声明
    - 公共的声明可以添加任何的信息，一般添加用户的相关信息或其他业务需要的必要信息. 但不建议添加敏感信息，因为该部分在客户端可解密.
- 私有的声明
    - 私有声明是提供者和消费者所共同定义的声明，一般不建议存放敏感信息，因为base64 是对称解密的，意味着该部分信息可以归类为明文信息。

以 json 形式，将数据保存在客户端虽然解决了上面的问题，但他还有一定的安全等问题，因此需要继续完善它，所以有了`jws(JSON Web Signature)`、`jwe(JSON Web Encryption)`两种实现，一般说的`jwt`就是指`jws`

下面以 `jws` 为例来看一下是如何改进它的。

#### 进一步完善（JWS）

虽然上面确定了解决方案，但不够优雅。因此继续完善它。

##### 添加标示信息（Header）

头部信息只是用来标示后面的数据是什么类型，要用什么方式来解析，有利于识别和升级约定的数据格式。

由于计算机只能真正表示`0`或`1`，其他能表示的信息都是约定出来的，因此计算机中很广泛的一种习惯就是在格式化后的数据前添加头部信息，标示这是什么格式的数据。
如不论windows还是linux，他们在保存文件时，都会添加文件头标示。加密存储时经常会数据头部标记加密算法等。

这样上面json形式的token就成了 `jwtHeader` + `jsonData`

##### 防伪 

这样的json串虽然可以识别不同客户端信息，但是太容易伪造，因此需要添加防伪措施，计算机中一种广泛使用的防伪手段就是**数字签名**，因此将其签名也附上

这样，token就变成了 `jwtHeader` + `jsonData` + `sign` 三部分，中间用 `.` 来分割。

##### 便于传输

由于 http 协议是文本协议，且在传递中需要进行一定的编码才行，因此将上述的部分进行 base64 编码，编码后就是`JWT`了

 
##### （更多完善）

进行以上完善后，解决了标示、自包含(存储信息)、防伪、传输、可扩展问题，已经处于可用状态了，但还有一些可改进的点（但非适用于所有场景）

- 信息安全
    - 上面的 json 形式的数据还是明文表示的，因此不应该在jwt中存放敏感数据，否则所有人都可以读到。
    - 主流使用 https 来保证信息安全
    - 安全升级版方案：**JWE**


### JWE 介绍    

相对于JWS，JWE则同时保证了安全性与数据完整性。JWE并非某种特定的加密算法，是一套加密的协议，有固定的格式，固定的加密流程，但具体算法不做限制。

JWE由五部分组成：

- JOSE Header
    - JOSE含义与JWS头部相同。用于标示该数据串是 JWE 格式的
    
- JWE Encrypt Key
    - 数据秘钥密文。与`Initialization Vector` 共同保证数据密文(`CipherText`)的安全性
    - 随机生成加密秘钥 Content Encryption Key （CEK）。
    - 使用`RSAES-OAEP` 加密算法，用服务器公钥（JWK）加密上面的`CEK`，生成`JWE Encrypted Key`。
- Initialization Vector
    - 初始化向量，加密相关参数。
    
- CipherText
    - 密文数据。作用同 JWS 中间 PayLoad 部分
    - 使用`AES GCM`加密算法对明文部分进行加密生成密文CipherText。该算法会随之生成一个128位的认证标记Authentication Tag。
- Authentication Tag
    - 数字认证标签，作用同 JWS 最后一部分
    - 生成认证算法生成JWE AAD(Addtional Authentication Data,附加的认证数据),截断AAD，获取Authentication Tag
    
这五部分均通过 `Base64UrlEncode` 编码后拼接为 JWE

#### JWE可选算法介绍

|加密过程|采用的算法|举例|
|---|--|--|
|原始报文加密|对称算法|AES128、AES256|
|密钥加密	|非对称算法|RSA、ECC|
|认证码生成	|HMAC	HMAC-MD5、HMAC-SHA1、HMAC-SHA256、HMAC-SHA384|

#### 特点
- 安全
    - 2次加密过程保证
- 真实（防伪）
    - 通过数字认证机制保证
- 不可抵抗性(双向认证)
    - 通过非对称加密算法进行验证
- 会话上下文安全
    - 对称算法的密钥每次都随机生成，即使破解一次请求的秘钥，不能解密响应也不能解密之前或之后的请求


[JWE参考文章](https://www.jianshu.com/p/9c2a8676b8d1)

## 相关名词

- JWT：JSON Web Token
    - json 形式的 token
    
- JWS：JSON Web Signature，Digital signature/HMAC specification
    - jwt 的一种基本防伪实现
- JWE：JSON Web Encryption，Encryption specification
    - jwt 的一种安全传输实现（可以理解为 JWS 的升级）
    
- JWK：JSON Web Key，Public key specification
    - 授权服务器的公钥（用于 JWE 中）

- JWA：JSON Web Algorithms，Algorithms and identifiers specification
    - JWT 算法和标识符规范









    