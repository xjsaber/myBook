# 深入理解Java虚拟机：JVM高级特性与最佳实践（第3版） #

## 第一部分 走近Java ##

### 第1章 走近Java ###

#### 1.1 概述 ####

#### 1.2 Java技术体系 ####

* Java程序设计语言
* 各种硬件平台上的Java虚拟机实现
* Class文件格式
* Java类库API
* 来自商业机构和开源社区的第三方Java类库

#### 1.3 Java发展史 ####



#### 1.4 Java虚拟机家族 ####

##### 1.4.1 虚拟机始祖：Sun Classic/Exact VM #####

##### 1.4.2 武林盟主：HotSpot VM #####

## 第二部分 自动内存管理 ##

### 第2章 Java内存区域与内存溢出异常 ###

#### 2.1 概述

#### 2.2 运行时数据区域

Java虚拟机在执行Java程序的过程中会把它所管理的内存划分为若干个不同的数据区域。这些区域有各自的用途，以及创建和销毁的时间，有的区域随着虚拟机进程的启动而一直存在，有些区域则是依赖用户线程的启动和结束而建立和销毁。



##### 2.2.1 程序计数器

程序计数器（Program Counter Register）是一个较小的内存空间，它可以看作是当前线程所执行的字节码的行号指示器。在Java虚拟机的概念模型里，字节码解释器工作时就是通过改变这个计数器的值来选取下一条需要执行的字节码指令，它是程序控制流的指示器，分支、循环、跳转、异常处理、线程恢复等基础功能都需要依赖这个计数器来完成。由于Java虚拟机的多线程是通过线程轮流切换、分配处理器执行时间的方式来实现的、在任何一个确定的时刻，一个处理器（对于多核处理器来说是一个内核）都只会执行一条线程中的指令。

##### 2.2.2 Java虚拟机栈

与程序计数器一样，Java虚拟机栈（Java Virtual Machine Stack）也是线程私有的，它的生命周期与线程相同。虚拟机栈描述的是Java方法执行的线程内存模型：每个方法被执行的时候，Java虚拟机都会同步创建一个栈帧用于存储局部变量表、操作数栈、动态连接、方法出口等信息。



域是“堆”和“栈”两块。

HotSpot虚拟机

##### 2.2.3 本地方法栈

本地工作栈（Native Method Stacks）与虚拟机栈所发挥的作用是非常相似的，其区别只是虚拟机栈为虚拟机执行Java方法（也就是字节码）服务，而本地方法栈则是虚拟机使用到的

HotSpot虚拟机的栈容量是不可以动态扩展的，以前的Classic虚拟机倒是可以。所以在HotSpot虚拟机上是不会由于虚拟机栈无法扩展而导致OutOfMemoryError异常——只要线程申请栈空间成功就不会有OOM，但是如果申请时就失败，仍然是会出现OOM异常的。

##### 2.2.4 Java堆

对于Java应用程序来说，Java堆（Java Heap）是虚拟机锁管理的内存中最大的一块。Java堆是被所有线程共享的一块内存区域，在虚拟机启动时创建。此内存区域的唯一目的就是存放对象实例，Java世界里“几乎”所有对象实例都在这里分配内存。