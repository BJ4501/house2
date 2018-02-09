## Spring Cloud 
- eureka server 支持REST调用，具体地址参考Eureka Github wiki

#### JWT
- 基于Token的身份验证方案
- jwt是一个字符串由和header,payload,signature组成
- 具备安全，自包含，紧凑等特点
- 缺点：
    - 消息体是可以被解析成名文
    - 不适合存储大量信息
    - 无法作废未过期的jwt(通过Redis解决)