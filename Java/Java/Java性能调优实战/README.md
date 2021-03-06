# Java性能调优实战 #

## 开篇词 | 怎样才能做好性能调优？ ##

一个简单的系统就囊括了应用程序、数据库、容器、操作系统、网络等技术，线上一旦出现性能问题，就可能要你协调多方面组件去进行优化，这就是技术广度；而很多性能问题呢，又隐藏得很深，可能因为一个小小的代码，也可能因为线程池的类型选择错误…可归根结底考验的还是我们对这项技术的了解程度，这就是技术深度。

### 1. 扎实的计算机基础 ###

调优的对象不是单一的应用服务，而是错综复杂的系统。应用服务的性能可能与操作系统、网络、数据库等组件相关，所以我们需要储备计算机组成原理、操作系统、网络协议以及数据库等基础知识。具体的性能问题往往还与传输、计算、存储数据等相关，那我们还需要储备数据结构、算法以及数学等基础知识。

### 2. 习惯透过源码了解技术本质 ###

需要深入源码，通过分析来学习、总结一项技术的实现原理和优缺点，这样我们就能更客观地去学习一项技术，还能透过源码来学习牛人的思维方式，收获更好的编码实现方式。

### 3. 善于追问和总结 ###

从来不问自己：为什么这项技术可以提升系统性能？对比其他技术它好在哪儿？实现的原理又是什么呢？事实上，“知其然且知所以然”才是我们积累经验的关键。知道了一项技术背后的实现原理，我们才能在遇到性能问题时，做到触类旁通。

**综合这三点心得，我也就想清楚了该怎么交付这个专栏。**

**模块一，概述。**建立两个标准。一个是性能调优标准，告诉你可以通过哪些参数去衡量系统性能；另一个是调优过程标准，带你了解通过哪些严格的调优策略，我们可以排查性能问题，从而解决问题。
**模块二，Java 编程性能调优。**JDK 是 Java 语言的基础库，熟悉 JDK 中各个包中的工具类，可以帮助你编写出高性能代码。这里我会从基础的数据类型讲起，涉及容器在实际应用场景中的调优，还有现在互联网系统架构中比较重要的网络通信调优。
**模块三，多线程性能调优。**Java 应用程序是运行在 JVM 之上的，对 JVM 进行调优可以提升系统性能。这里重点讲解 Java 对象的创建和回收、内存分配等。
**模块四，JVM 性能监测及调优。**Java 应用程序是运行在 JVM 之上的，对 JVM 进行调优可以提升系统性能。这里重点讲解 Java 对象的创建和回收、内存分配等。
**模块五，设计模式调优。**在架构设计中，我们经常会用到一些设计模式来优化架构设计。
**模块六，数据库性能调优。**数据库最容易成为整个系统的性能瓶颈，这里我会重点解析一些数据库的常用调优方法。
**模块七，实战演练场。**

### 精选留言 ###

#### 1 ####

**Q**

老师你好，我的项目上用户量少，对性能要求也不高，很难遇到性能调优的大场面。缺少实践的话，通过学习也不会有很深的感触。在这种情况下应该如何提高自己的性能调优能力呢？

**A**

作者回复: 言凡你好，你提的这个问题非常好，我相信很多童鞋都有跟你一样的问题。

*具体的性能调优能力确实需要一些实战历练，但在具体的后面，是我们平时积累的大量基础知识。所以说，首先要保证练好扎实的基础功，到了真实战场，这些基础能帮上你的大忙。很多大公司面试的时候，对中高级开发首先要求的也是基础。*

*实战经验虽然是我们的短板，但平时我们也可以在很多源码上学习一些调优经验，例如锁的优化的方式，减小锁粒度是优化锁常用的方式，我们可以学习和借鉴使用，像这样的优化案例有很多。也可以通过一些渠道学习大公司的优化方案以及大神的分享优化方案，强调的是学习一种思维方式，不一定能在实际项目中应用到，但是我们能在遇到问题的时候想到优化方案。*

还有就是通过动手来实践，提高自己的实践能力。

#### 2 ####

**Q**

定位痛点，难定位，其实是不会定位，难重现。分析问题，不会分析，不知道怎么分析，不知道各数据代表什么等。解决问题，解决这个问题，可能引出另外一个问题等。

**A**

首先，感谢老师的分享，使我受益匪浅。
其实我在这想回答@业余草童靴在留言中的困惑
个人认为性能调优分为以下步骤：
1.目前现象----》2.提出猜想------》3.验证猜想-------》4.定位到问题-------》5.解决问题
分析问题难其实就对应的是第2步骤，说白了就是你提不猜想，为什么你提出猜想，那是因为你的知识面不广，基础知识不牢固。知识面不够，就导致针对现象，提不出问题。
定位问题难对应第4步骤，这个的问题就是不会使用工具，我们在佐证我们的猜想时需要一些辅助工具。不会使用工具，就导致不能佐证猜想。



模块1 概述

## 01 | 如何制定性能调优标准 ##

## 02 | 如何制定性能调优策略 ##

# 模块2 Java编程性能调优 #

## 03 | 字符串性能优化不容小觑，百M内存轻松存储几十G数据 ##

高效地使用字符串，可以提升系统的整体性能。

通过三种不同的方式创建了三个对象，再依次两两匹配，每组被匹配的两个对象是否相等？代码如下：

	String str1= "abc";
	String str2= new String("abc");
	String str3= str2.intern();
	assertSame(str1==str2);
	assertSame(str2==str3);
	assertSame(str1==str3)



### String对象是如何实现的？ ###

1. 在Java6以及之前的版本中
	* char[]
	* offset
	* count
	* hash
2. 从Java7版本开始到Java8版本
	* char[]
	* hash 
3. 从Java9版本开始
	* byte[]
	* coder
	* hash

* String对象是通过offset和count两个属性来定位char[]数组，获取字符串。这么做可以高效、快速地共享数组对象，同时节省内存空间。
* String类中不再有offset和count两个变量。String对象占用的内存稍微少了些

### String对象的不可变性 ###



#### Java这样做的好处在哪里呢？ ####

1. 保证String的安全性。
2. 保证hash属性值不会频繁变更。
3. 可以实现字符串常量池。

### String对象的优化 ###

#### 1.如何构建超大字符串？ ####

#### 2. 如何使用String.intern节省内存？ ####


