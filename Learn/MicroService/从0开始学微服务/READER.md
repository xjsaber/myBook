# 01 到底什么是微服务？ #

# 02 从单体应用走向服务化 #

# 03 初探微服务架构 #

# 04 如何发布和引用服务 #

# 05 如何注册和发现服务 #

# 06 如何实现RPC远程服务调用 #

# 07 如何监控微服务调用 #

# 08 如何追踪微服务调用 #

# 09 微服务治理的手段有哪些 #

# 10 Dubbo框架里的微服务组件 #

# 11 服务发布和引用的实践 #

Restful API、XML配置以及IDL文件。

## XML配置方式的服务发布和引用流程 ##

### 1. 服务提供者定义接口 ###

服务提供者发布服务之前首先要定义接口，声明接口名、传递参数以及返回值类型，然后把接口打包成JAR包发布出去。

### 2. 服务提供者发布接口 ###

### 3. 服务消费者引用接口 ###

## 服务发布和引用的那些坑 ##

### 1. 服务发布预定义配置 ###

### 2. 服务引用定义配置 ###

### 3. 服务配置升级 ###

## 总结 ##

## 思考题 ##

# 12 如何将注册中心落地 #

掌握了服务注册和发现的原理之后，需要考虑如何把注册中心落地地实现。

## 注册中心如何存储服务信息 ##

注册中心既然是用来存储服务信息的。

服务一般会分成多个不同的分组，每个分组的目的不同。

* 核心与非核心，苍业务的核心程度来分。
* 机房，从机房的维度来分。
* 线上环境与测试环境，从业务场景维度来区分。

所以注册中心存储的服务信息一般包含三部分内容：分组、服务名以及节点信息，节点信息又包含节点地址和节点其他信息。

具体存储的时候，一般是按照“服务-分组-节点信息”三层结构来存储，Service代表服务的具体分组，Cluster代表服务的接口名，节点信息用KV存储。

## 注册中心是如何工作的 ##

### 1. 如何注册节点 ###

* 首先查看要注册的节点是否再白名单内？如何不在就抛出异常，在的话继续下一步。
* 其次要查看的CLuster（服务的接口名）是否存在？如果不存在就抛出异常，存在的话继续下一步。
* 然后要检查Service（服务的分组）是否存在？如果不存在则抛出异常，存在的话继续下一步。
* 然后将节点信息添加到对应的Service和Cluster下面的存储中。

### 2. 如何反注册 ###

* 查看Service（服务的分组）是否存在，不存在就抛出异常，存在就继续下一步。
* 查看Cluster（服务的接口名）是否存在，不存在就抛出异常，存在就继续下一步。
* 删除存储中Service和Cluster下对应的节点信息。
* 更新Cluster的sign的值。

### 3. 如何查询节点信息 ###

* 首先从localcache（本机内存）中查找，如果没有就继续下一步。这里为什么服务消费者要把服务信息存在本机内存呢？主要是因为服务节点信息并不总是时刻变化的，并不需要每一次服务调用都要调用注册中心获取最新的节点信息，只需要在本机内存中保留最新的服务提供者的节点列表就可以。
* 接着从snapshot（本地快照）中查找，如果没有就继续下一步。这里为什么服务消费者要在本地磁盘存储一份服务提供者的节点信息的快照呢？这是因为服务消费者同注册中心之间的网络不一定总是可靠的

# 14 开源RPC框架如何选型？ #

* Dubbo
* Motan
* Tars
* Spring Cloud

* gRPC
* Thrift

## 总结 ##


# 15 如何搭建一个可靠的监控系统？ #

比较流行的开源监控系统实现方案主要有两种：以ELK为代表的集中式日志解决方案。

# 16 如何搭建一套适合你的服务追踪系统？ #

# 17 如何识别服务节点是否存活？ #