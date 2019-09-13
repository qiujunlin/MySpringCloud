
---
# spring cloud 介绍

spring cloud 是==一系列框架的集合==。它利用 spring boot 的开发便利性巧妙地简化了分布式系统基础设施的开发，如服==务发现注册、配置中心、消息总线、负载均衡、断路器、数据监控等==，都可以用 spring boot 的开发风格做到一键启动和部署。spring cloud 并没有重复制造轮子，它只是将目前各家公司开发的比较成熟、经得起实际考验的服务框架组合起来，通过 spring boot 风格进行再封装屏蔽掉了复杂的配置和实现原理，最终给开发者留出了一套==简单易懂、易部署和易维护==的分布式系统开发工具包。

spring cloud 对于中小型互联网公司来说是一种福音，因为这类公司往往没有实力或者没有足够的资金投入去开发自己的分布式系统基础设施，使用 spring cloud ==一站式解决方案==能在从容应对业务发展的同时==大大减少开发成本==。同时，随着近几年微服务架构和 docker 容器概念的火爆，也会让 spring cloud 在未来越来越“云”化的软件开发风格中立有一席之地，尤其是在目前五花八门的分布式解决方案中提供了==标准化==的、一站式的技术方案，意义可能会堪比当年 servlet 规范的诞生，有效推进服务端软件系统技术水平的进步。

# spring cloud 技术组成

- **eureka**<br />
微服务治理，服务注册和发现

- **ribbon**<br />
负载均衡、请求重试

- **hystrix**<br />
断路器，服务降级、熔断

- **feign**<br />
ribbon + hystrix 集成，并提供生命式客户端

- **hystrix dashboard 和 turbine**<br />
hystrix 微服务监控

- **zuul**<br />
API 网关，提供微服务的统一入口，并提供统一的权限验证

- **config**<br />
配置中心

- **bus**<br />
消息总线, 配置刷新

- **sleuth+zipkin**<br />
链路跟踪



# Spring Cloud 对比 Dubbo

- dubbo
    - Dubbo只是一个远程调用(RPC)框架
    - 默认基于长连接,支持多种序列化格式



- Spring Cloud
 - 是一整套微服务解决方案,是一个框架集

 - 不是一个单独的框架


##  eureka
注册中心服务器
维护一组微服务的地址列表,其他微服务启动时,要把自己的地址,向eureka服务器进行注册
- 客户端注册

连接eureka服务器的 localhost:xxx/eureka进行注册,连接失败,注册失败,会稍后重试连接
- 心跳

默认每30秒发送一次心跳数据

 

- 丢失三次心跳数据

服务器 90 秒没有收到心跳数据,会认为服务不可用

 

-  eureka 的保护模式

当一个服务不可用,eureka会保留它的地址,不删除

 

- 客户端拉取地址表

每30秒更新一次地址表

 

 

###   eureka 和 zookeeper
spring cloud 可以支持这两种注册中心,

- eureka - AP

 -  A - 可用性

 -  P - 分区容错性

 -  对等
-  zookeeper - CP

  -  C - 一致性

  -  P - 分区容错性

  - 主从

##  ribbon
- 做负载均衡和重试

  负载均衡: 对集群中的服务器,轮询调用

  重试: 当调用失败或超时,可以重试调用


-  RestTemplate

 是spring boot提供的一个远程调用工具

  方法
  getForObject(url, 对象类型, 参数)
  postForObject(url, 对象类型, 参数)
  ribbon 对RestTemplate 做了封装,添加了负载均衡和重试的功能
  用AOP 生成动态代理,在 RestTemplate 切入了代码
 对RestTemplate 添加 @LoadBalanced 注解,就可以生成动态代理


##   hystrix
降级和熔断

-  降级

  在服务不可用,出错,超时时,直接向客户端发回响应结果,可以返回错误提示,或缓存数据等

-  熔断

 当瞬间有大量访问,大量访问失败降级,会触发熔断
-  触发条件:

  10秒20次请求

 50%失败降级

  熔断: 断路器打开

 断路器打开后,所有访问都直接执行降级代码  5秒后,会进入"半开"状态,会尝试向后台服务调用,如果成功,关闭断路器;如果仍然失败,继续保持打开状态


 - actuator
spring boot提供的数据监控工具,可以用actuator来暴露各种监控数据

##     feign
- 提供声明式客户端

  声明式客户端,不需要写远程调用代码,只需要定义一个远程调用接口、
  
```java
@FeignClient("item-service")

public interface ItemFeign {

    //指定调用此方式时,访问后台服务的哪个路径

    @GetMapping("/{orderId}")

   JsonResult<List<Item>> getItems(@PathVariable String orderId);

}
```


 -  使用SpringMVC注解,来拼接路径

http://localhost:8001/123


-  整合ribbon

  不做任何配置,已经实现负载均衡和重试
  AutoRetries=0
  AutoRetriesNextServer=1
  ConnectTimeout=1000
  ReadTimeout=1000
- 降级方法:
  实现远程调用接口,添加降级代码
  @FeignClient(..., fallback=降级类)

-  熔断监控:

  完整的hystrix依赖

  actuator依赖

  @EnableCircuitBreaker

  暴露监控端点

  timeoutInMilliseconds=5000

##     zuul API网关
- 作用
 - 统一的入口

 -  过滤器,可以做权限校验


-    feign 和 zuul
  feign - 微服务之间的调用
 zuul - 在最前面,作为入口


##   config配置中心
每个微服务都维护自己的配置文件,维护管理不方便
配置中心可以集中管理维护所有项目的配置文件,一个微服务启动时,连接配置中心服务器,从配置中心获取配置文件

##    bus
消息总线

 辅助连接rabbitmq

  发送或接收刷新消息
##    链路跟踪 sleuth+zipkin