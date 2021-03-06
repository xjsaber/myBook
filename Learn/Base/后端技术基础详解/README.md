# 后端技术基础详解 #

# 开篇词 #

## 开篇词 | 掌握软件开发技术的第一性原理 ##

第一性原理——建立技术体系的起点。

软件的基础原理、软件的设计原理、架构的核心原理。

我想从软件技术的第一性原理出发，写一写软件技术那些最基本的知识原理和知识体系。

### 精选留言 ###


# 软件的基础原理 #

## 01 | 程序运行原理：程序是如合运行又是如合崩溃的？ ##

### 程序是如合运行起来的 ###

* 程序，静态的
* 进程，动态的，除了包含可执行的程序代码，还包括进程在运行期使用的内存堆空间、栈空间、供操作系统管理用的数据结构。

要想让程序处理数据，完成计算任务，必须把程序从外部设备加载到内存中，并在操作系统的管理调度下交给CPU去执行，去运行起来，才能真正发挥软件的作用，程序运行起来以后，被称作进程。

进程除了包含可执行的程序代码，还包括进程在运行期使用的内存堆空间、栈空间、供操作系统管理用的数据结构。

### 一台计算机如何同时处理数以百计的任务 ###

* 运行
* 就绪
* 阻塞

### 系统为什么会变慢，为什么会崩溃 ###

![d40cc1e9a2a5ce3913670743f0543b9a.png](img/d40cc1e9a2a5ce3913670743f0543b9a.png)

Tomcat 启动多个线程，为每个用户请求分配一个线程，调用和请求 URL 路径相对应的 Servlet（或者 Controller）代码，完成用户请求处理。而 Tomcat 则在 JVM 虚拟机进程中，JVM 虚拟机则被操作系统当做一个独立进程管理。真正完成最终计算的，是 CPU、内存等服务器硬件，操作系统将这些硬件进行分时（CPU）、分片（内存）管理，虚拟化成一个独享资源让 JVM 进程在其上运行。

不管你是否有意识，你开发的 web 程序都是被多线程执行的，web 开发天然就是多线程开发。

CPU 以线程为单位进行分时共享执行，可以想象代码被加载到内存空间后，有多个线程在这些代码上执行，这些线程从逻辑上看，是同时在运行的，每个线程有自己的线程栈，所有的线程栈都是完全隔离的，也就是每个方法的参数和方法内的局部变量都是隔离的，一个线程无法访问到其他线程的栈内数据。

* 系统变慢
* 应用崩溃

此外必要时还需要在请求入口处进行限流，减小系统的并发请求数；在应用内进行业务降级，减小线程的资源消耗。

### 小结 ###

### 思考题 ###

线程安全的临界区需要依靠锁，而锁的获取必须也要保证自己是线程安全的，也就是说，不能出现两个线程同时得到锁的情况，那么锁是如何保证自己是线程安全的呢？或者说，在操作系统以及 CPU 层面，锁是如何实现的？

在java里 锁是通过cas把当前线程id刷新到对象的头信息里 在获取锁时会去头信息里拿这个信息 如果没有 则会cas刷新进去 刷新成功就获取到锁 刷新失败就表明有别的线程也在尝试刷新这个信息 在操作系统层面 有pv操作保证原子性 而pv操作也是利用cpu中原语指令 在获取锁时保证不会被别的指令打断（或被重排序）

### 精选留言 ###

#### 1. ####

有些地方可能不是很到位，欢迎补充
小结：
1. 我们平时开发出来的程序是文本格式代码，但只是在硬盘中还只是一个程序，只有加载到内存里面通过cpu执行成为进程才是发挥了程序作用。
2.进程里面有堆，栈，可执行代码和进程数据结构。
3.cpu分时共享技术进行并发操作，进程切换效率不高，所以有了线程切换
4.因为线程安全问题引入锁，不过也引入了更多造成阻塞的可能
5.线程阻塞可能是I/O,锁，网络请求，数据库链接获取
6利用分布式系统架构来减缓高并发的性能不佳

#### 2. ####

我们用文本格式书写的程序有三种执行方式:
1.解释执行。例子是脚本语言书写的程序或类似于BASIC语言书写的程序。著名的PYTHON也属于这种情况。
2.编译执行。通常C/C++程序属于这种情况。文本格式书写的程序称之为源程序，需要编译器编译成机器语言代码，称之为可执行程序一或目标程序。
3.虚拟机执行。将文本格式的程序先编译成一种中间代码，然后由驻留在计算机中的虚拟机解释执行。例子是通常的JAVA程序。

#### 3.  ####

我想请教下，一个JVM 是一个进程。JVM 上跑 tomcat，tomcat 可以部署多个应用？那每个跑在tomcat 上的应用是一个线程吗？那一个应用crash了，其他应用也会crash.这块感觉有点问题。不知道老师方便解释下吗？

作者回复: JVM是一个进程；tomcat是一个框架；tomcat会启动线程处理应用请求，执行应用的代码；应用自己不能跑起来的，只能被tomcat的线程执行，应用在tomcat线程中执行时，也可以自己启动线程，并发、异步执行自己的某些计算。

tomcat会对不同应用做一些隔离，但是如果某个应用导致JVM crash，所有应用都crash。

可参考后续专栏《JVM虚拟机原理》

#### 4.  ####

老师，看完文章，联想到两个关于协程的问题：

1. 使用协程在出现IO等待时，程序会自己调度去执行其他的(CPU)任务。理论上这样可以避免额外的IO等待导致的线程间切换。我的问题是从系统的角度上看，使用协程可以抢占到更多的CPU时间片吗？

2. 感觉系统崩溃（除人为Bug外) 主要是系统资源不足导致的。那么即使用轻量级的协程也不会变得更好。因为当协程数量过多，导致event loop过大，变慢，系统还是要崩溃的对吗？

作者回复: 1 多个协程通过自我调度复用同一个线程，所以某个协程IO等待的时候，会导致整个线程阻塞，并不能避免线程切换。更好的做法是使用异步IO，不要IO等待。

能否抢到时间片是操作系统调度的，协程自己控制不了，但是协程利用自己更轻量级，配合异步IO等方法，可以提高运行效率，整体性能得到优化。

2 是的。但是用好协程，减少额外的线程调度切换，可以提高整体的系统吞吐能力。

#### 5. ####

您好，老师，这节课太适合我这种新手了，这里有三个不太明白的地方，希望老师能够解惑。
问题1：CPU 分时共享技术同时执行进程的数量，取决于什么？
问题2：为什么线程切换的代价更小？
问题3：进程切换是不是必须要等到线程切换完毕后进行？如果不是，优先级是由什么决定的？
一点小建议：
有一些表达程度的词，如果能用数据举例简单说明一下，对于我们理解会更有帮助。比如2问题中，代价更小，小到什么程度，是进程切换速度的几倍?
思考题：
作为小白，我的思路是这样，锁是在线程的临界区，线程是在进程的线程栈，而 一个 cpu 同时只能运行一个进程，所以本质上都是轮流执行的……于是，只要保证在获取锁的时候，锁不在正在获取或已经被获取的状态即可，进而推断线程中会有一片内存区域用来存这些状态信息。
😂不知道这个思路对不对。
最后谢谢老师。

作者回复: 1 取决于CPU的核数
2 线程比进程占据的资源少，切换代价小，而且进程内线程可能执行相同的代码，切换线程后也许还可以复用CPU cache数据
3 线程切换和进程切换只依赖CPU是否空闲，和线程进程彼此没关系

PS，上面回复内容其实我也没有确切的资料证实，我是根据第一性原理推导出来的，我觉得仅根据专栏文章内容就可以推导出来，我希望你也可以自己分析推导，这样获得的知识会更加稳固

#### 6. ####

你好，想提个问题。文章中大部分知识点都掌握，但是遇到问题的时候没有从这些角度出发，只是跟着一些关联去分析问题，对遇到的问题反应比较慢。这种情况是不是知识没有成体系？那么怎样建立比较完整的知识体系呢？

作者回复: 思考问题从问题的根源出发思考，如果在解决问题的时候现场比较乱，无法做到。事后做复盘，复盘的时候重新从根源思考。

#### 7. ####

老师，别人常说java和go等是带有运行时的语言，难道c和cpp没有吗？运行时是指垃圾回收这些功能吗？

作者回复: C和C++的执行代码直接交给操作系统调度管理，Java和Go的执行代码在JVM和Go的虚拟机环境中运行，不仅仅是垃圾回收，Java和Go的虚拟机职责也不一样。详情可参考后续专栏《JVM虚拟机原理》。

## 02 | 数据结构原理：Hash表的时间复杂度为什么是O（1）？ ##

Hash表的时间复杂度为什么是O（1）？

Hash表，需要从数组讲起

## 数组 ##

数组是最常用的数据结构，创建数组必须要从内存一块**连续**的空间，并且数组中必须存放**相同**的数据类型。

随机快速读写是数组的一个重要特性，但是要随机放问数据，必须知道数据在数组中的下表。如果我们只知道数据的值，想要在数组中找到这个值，我们就只能遍历整个数组，时间复杂度位O(N)。

## 链表 ##

不同于数组必须要连续的内存空间，链表可以使用零散的内存空间存储数据。不过因为链表在内存中的数据不是连续的，所以链表中的每个数据元素都必须包含一个指向下一个数据元素的内存地址指针。如下图，链表的每个元素包含两部分。一部分是数据，一部分是指向下一个原申诉的地址指针。最后一个元素指向null，表示链表到此为止。

因为链表是不连续存储的，要想在链表中查找一个数据，只能遍历链表，所以链表的查找复杂度是O(N)。

## Hash表 ##

对数组中的数据进行快速访问必须通过数组的下标，时间复杂度为O(1)。

Hash表中数据结构以Key，Value的方式存储。

Hash表的物理存储其实是一个数组，如果能够根据Key计算出数组下标，那么就可以快速在数组中查找倒需要的Key和Value。

当我们要读取数据的时候，直摇给定Key abc，算一个算法过程，先求取它的HashCode 101，然后再对8取模，因为数组的长度不变，对8取模之后依然是余5，然后倒数组下标中取找5的的位置，就可以找到前面存储进去的abc对应的Value值。

Hash冲突，解决Hash冲突的常用的方法是链表法。

Key、Value数据并不会直接存储在Hash表的数组中，因为数组要求存储固定数据类型，主要目的是每个数组元素中要存放固定长度的数据。所以，数组中存储的是 Key、Value 数据元素的地址指针。一旦发生 Hash 冲突，只需要将相同下标，不同 Key 的数据元素添加到这个链表就可以了。查找的时候再遍历这个链表，匹配正确的 Key。

## 栈 ##

数组和链表都被称为线性表，因为里面的数据是按照线性组织存放的，每个数据元素的前面只能有一个（前驱）数据元素，后面也只能有一个（后继）数据元素，所以称为线性表

操作受限的线性表

## 队列 ##

队列也是一个操作受限的线性表，栈是后进先出，而队列是先进先出。

## 树 ##

数据、链表、栈和队列都是线性表，即有一个前驱，一个后继。而树则是非线性表。

## 小结 ##



## 03 | Java虚拟机原理：JVM为什么被称为机器（machine）？ ##

JVM的全称是Java Virtual Machine。

### JVM的组成构造 ###

JVM的主要由类加载器、运行时数据区、执行引擎三个部分组成。

![de1e68c3f603d1a7ab07d22a3761673a.png](img/de1e68c3f603d1a7ab07d22a3761673a.png)

运行时数据区主要包括方法区、堆、Java栈、程序计数寄存器。

方法区主要存放从磁盘加载的类字节码，而在程序运行过程中创建的类实例则存放在堆里。程序运行的时候，实际以线程为单位运行的，当JVM进入启动类的main方法的时候，就会为应用程序创建一个主线程，main方法里的代码就会被这个主线程执行，每个线程都有自己的Java栈，栈里存放着

一个典型的Java程序运行过程是下面这样的：

通过Java命令启动JVM，JVM的类加载器根据Java命令的参数到指定的路径加载.class类

### JVM的垃圾回收 ###

所谓自动垃圾回收是将JVM堆中的已经不再被使用的对象清理掉，释放宝贵的内存资源。

垃圾回收 -> 如何直到哪些对象是不再被使用的

JVM通过一种可达性分析算法进行垃圾对象的识别，具体过程是：从线程

#### 第一种方式是清理 ####

#### 第二种方式是压缩 ####

#### 第三种方式是复制 ####

#### 第四种方式是清理 ####

### 小结 ###

## 04 | 网络编程原理：一个字符的互联网之旅 ##

DNS -> CDN -> IP -> HTTP -> TCP

### DNS ###

构成互联网Internet的最基本的网络协议就是互联网协议InternetProtocol，简称IP协议。IP协议里面最重要的部分是IP地址，各种计算机设备之间能够互相通信。IP地址就是互联网的地址标识。

### CDN ###

如果用户请求的数据是静态

CDN是内容分发网络Content Delivery Network的缩写。

如果用户请求的数据是静态资源，域名解析的时候就会解析为淘宝CDM的IP地址，请求先被CDN处理，如果CDN中有需要的静态文件，就直接返回，如果没有，CDN会将请求发送到淘宝的数据中心，CDN从淘宝数据中心获得静态文件后，一方面缓存缓存在自己的服务器上，一方面将数据返回给用户的App。

如果请求的数据是动态的，请求的域名可能是search.taobao.com这样的二级域名，就会直接被DNS解析为淘宝的数据中心的服务器IP地址，App请求发送到数据中心处理。

### HTTP ###

HTTP是一个应用层协议，

1. 应用层的通信协议，我们通信的数据是如何编码，既能使网络传输过去的数据携带必要的信息
2. 通信的双方都能正确识别这些信息（双方英雄程序需要约定一个数据编码协议）
3. 网络底层通信协议，即如何为网络上需要通信的两个节点建立连接完成数据传输，目前互联网应用中最主要的就是TCP协议

在TCP传输层协议层面

1. 保证建立通信双方的稳定通信连接，将一方的数据以bit流的方式源源不断地发送到另一方
2. 

HTTP请求头

1. GET：请求头
2. POST：请求头+body部分 Content-length body

HTTP响应头

200，302，4xx，5xx 相应body HTML或JSON字符

### TCP ###

### LB（负载均衡 ###）

搜索请求到达数据中心的时候

1. 首先到达的饿是搜索服务器集群的负载均衡服务器。
2. DNS解析出来的是负载均衡服务器的IP地址。
3. 由负载均衡服务器将请求分发到搜索服务器集群中的某台服务器上。

负载均衡服务器的实现手段->通常使用Linux内核支持的链路层负载均衡。

直接路由模式，在负载均衡服务器的Linux操作系统内核拿到数据包后，直接修改数据帧的mac地址，将其修改为搜索服务器集群中某个服务器的mac地址，然后将数据重新发送回服务器集群所在的局域网，这个数据帧就会被某个真实的搜索服务器接收到。

负载均衡服务器和集群内的搜索服务器配置相同的虚拟IP地址。在网络通信的IP层面，负载均衡服务器变更mac地址的操作是透明的，不影响TCP/Ip的通信连接。所以真实的搜索服务器处理完搜索请求，发送应答相应的时候，就会直接发送回请求的App手机，不会再经过负载均衡服务器。

### 小结 ###



## 05 | 文件系统原理：如何用1分钟遍历100TB的文件？ ##

### 硬盘 ###

硬盘是一种可持久保存、多次读写数据的存储介质。硬盘的形式主要两种，一种是机械式硬盘，一种是固态硬盘。

机械式硬盘的结构，主要包含盘片、主轴、磁头臂，主轴带动盘片高速旋转，当需要读写盘上的数据的时候，磁头臂会移动磁头到盘片所在的磁道上，磁头读取磁道上的数据。读写数据需要移动磁头，这样一个机械的动作，至少需要花费数毫秒的时间，这是机械式硬盘访问延迟的主要原因。

### 文件系统 ###

作为应用程序开发者，我们不需要直接操作硬盘，而是通过操作系统，以文件的方式对硬盘上的数据进行读写访问。文件系统将硬盘空间以块为单位进行划分，每个文件占据若干个块，然后再通过一个文件控制块 FCB 记录每个文件占据的硬盘数据块。

这个文件控制块在 Linux 操作系统中就是 inode，要想访问文件，就必须获得文件的 inode 信息，在 inode 中查找文件数据块索引表，根据索引中记录的硬盘地址信息访问硬盘，读写数据。

inode 中记录着文件权限、所有者、修改时间和文件大小等文件属性信息，以及文件数据块硬盘地址索引。inode 是固定结构的，能够记录的硬盘地址索引数也是固定的，只有 15 个索引。其中前 12 个索引直接记录数据块地址，第 13 个索引记录索引地址，也就是说，索引块指向的硬盘数据块并不直接记录文件数据，而是记录文件数据块的索引表，每个索引表可以记录 256 个索引；第 14 个索引记录二级索引地址，第 15 个索引记录三级索引地址，如下图：

![30e8aaa432b315e5b16a06a787ff0437.jpg](img/30e8aaa432b315e5b16a06a787ff0437.jpg)

这样，每个 inode 最多可以存储 12+256+256*256+256*256*256 个数据块，如果每个数据块的大小为 4k，也就是单个文件最大不超过 70G，而且即使可以扩大数据块大小，文件大小也要受单个硬盘容量的限制。

### RAID ###

RAID，即独立硬盘冗余阵列，将多块硬盘通过硬件 RAID 卡或者软件 RAID 的方案管理起来，使其共同对外提供服务。RAID 的核心思路其实是利用文件系统将数据写入硬盘中不同数据块的特性，将多块硬盘上的空闲空间看做一个整体，进行数据写入，也就是说，一个文件的多个数据块可能写入多个硬盘。

根据硬盘组织和使用方式不同，常用 RAID 有五种，分别是 RAID 0、RAID 1、RAID 10、RAID 5 和 RAID 6。

* RAID 0 将一个文件的数据分成 N 片，同时向 N 个硬盘写入，这样单个文件可以存储在 N 个硬盘上，文件容量可以扩大 N 倍
* RAID 1 则是利用两块硬盘进行数据备份，文件同时向两块硬盘写入，这样任何一块硬盘损坏都不会出现文件数据丢失的情况，文件的可用性得到提升。
* RAID 10 结合 RAID 0 和 RAID 1，将多块硬盘进行两两分组，文件数据分成 N 片，每个分组写入一片，每个分组内的两块硬盘再进行数据备份。
* RAID 5 针对 RAID 10 硬盘浪费的情况，将数据分成 N-1 片，再利用这 N-1 片数据进行位运算，计算一片校验数据，然后将这 N 片数据写入 N 个硬盘。
* 实践中，使用最多的是 RAID 5，数据被分成 N-1 片并发写入 N-1 块硬盘，这样既可以得到较好的硬盘利用率，也能得到很好的读写速度，同时还能保证较好的数据可用性。

### 分布式文件系统 ###

文件的基本信息，也就是文件元信息记录在文件控制块 inode 中，文件的数据记录在硬盘的数据块中，inode 通过索引记录数据块的地址，读写文件的时候，查询 inode 中的索引记录得到数据块的硬盘地址，然后访问数据。

如果将数据块的地址改成分布式服务器的地址呢？也就是查询得到的数据块地址不只是本机的硬盘地址，还可以是其他服务器的地址，那么文件的存储容量就将是整个分布式服务器集群的硬盘容量，这样还可以在不同的服务器上同时并行读取文件的数据块，文件访问速度也将极大的加快。这样的文件系统就是分布式文件系统，分布式文件系统的思路其实和 RAID 是一脉相承的，就是将数据分成很多片，同时向 N 台服务器上进行数据写入。针对一片数据丢失就导致整个文件损坏的情况，分布式文件系统也是采用数据备份的方式，将多个备份数据片写入多个服务器，以保证文件的可用性。当然，也可以采用 RAID 5 的方式通过计算校验数据片的方式提高文件可用性。

HDFS 的关键组件有两个，一个是 DataNode，一个是 NameNode。

* DataNode 负责文件数据的存储和读写操作，HDFS 将文件数据分割成若干数据块（Block），每个 DataNode 存储一部分数据块，这样文件就分布存储在整个 HDFS 服务器集群中。应用程序客户端（Client）可以并行对这些数据块进行访问，从而使得 HDFS 可以在服务器集群规模上实现数据并行访问，极大地提高了访问速度。在实践中，HDFS 集群的 DataNode 服务器会有很多台，一般在几百台到几千台这样的规模，每台服务器配有数块硬盘，整个集群的存储容量大概在几 PB 到数百 PB。
* NameNode 负责整个分布式文件系统的元数据（MetaData）管理，也就是文件路径名、访问权限、数据块的 ID 以及存储位置等信息，相当于 Linux 系统中 inode 的角色。HDFS 为了保证数据的高可用，会将一个数据块复制为多份（缺省情况为 3 份），并将多份相同的数据块存储在不同的服务器上，甚至不同的机架上。这样当有硬盘损坏，或者某个 DataNode 服务器宕机，甚至某个交换机宕机，导致其存储的数据块不能访问的时候，客户端会查找其备份的数据块进行访问。

有了 HDFS，可以实现单一文件存储几百 T 的数据，再配合大数据计算框架 MapReduce 或者 Spark，可以对这个文件的数据块进行并发计算。也可以使用 Impala 这样的 SQL 引擎对这个文件进行结构化查询，在数千台服务器上并发遍历 100T 的数据，1 分钟都是绰绰有余的。

### 小结 ###

文件系统从简单操作系统文件，到 RAID，再到分布式文件系统，其设计思路其实是具有统一性的。这种统一性一方面体现在文件数据如何管理，也就是如何通过文件控制块管理文件的数据，这个文件控制块在 Linux 系统中就是 inode，在 HDFS 中就是 NameNode。另一方面体现在如何利用更多的硬盘实现越来越大的文件存储需求和越来越快的读写速度需求，也就是将数据分片后同时写入多块硬盘。单服务器我们可以通过 RAID 来实现，多服务器则可以将这些服务器组成一个文件系统集群，共同对外提供文件服务，这时候，数千台服务器的数万块硬盘以单一存储资源的方式对文件使用者提供服务，也就是一个文件可以存储数百 T 的数据，并在一分钟完成这样一个大文件的遍历。

### 思考题 ###

在 RAID 5 的示意图中，P 表示校验位数据，我们看到 P 不是单独存储在一块硬盘上，而是分散在不同的盘上，实际上，校验数据 P 的存储位置是螺旋式地落在所有硬盘上的，为什么要这样设计？


## 06 | 数据库原理：为什么PrepareStatement性能更好更安全？ ##

1. 通过Statement直接提交SQL
2. 先通过PrepareStatement预编译SQL，然后设置可变参数再提交执行

	statement.executeUpdate("UPDATE Users SET stateus = 2 WHERE userID=233");


	PreparedStatement updateUser = con.prepareStatement("UPDATE Users SET stateus = ? WHERE userID = ?"); 
	updateUser.setInt(1, 2); 
	updateUser.setInt(2,233); 
	updateUser.executeUpdate();

### 数据库架构与SQL执行过程 ###

关系数据库RDBMS有很多种，但是关系数据库的架构基本差不多，包括支持SQL语言的Hadoop大数据从仓库。一个SQL提交到数据库，经过连接器将SQL语句交给语法分析器，生成一个抽象语法树AST；AST经过语义分析与优化器，进行语义优化，使计算过程和需要获取的中间数据尽可能少，然后得到数据库执行计划；执行计划提交给具体的执行引擎进行计算，将结果通过连接器再返回给应用程序。

这些连接一旦建立，不管是否有SQL执行，都会消耗一定的数据库内存资源，所以对于一个大规模互联网集群了来说，如果启动了很多应用程序实例，这些程序每个都会和数据库建立若干个连接，即使不提交SQL到数据库执行，也就会对数据库产生很大的压力。

连接器收到SQL以后，会将SQL交给语法分析器进行处理，语法分析器工作比较简单机械，就是根据SQL语法规则生成对应的抽象语法树。

如果SQL语句中存在语法错误，那么在生成语法树的时候就会报错。

	mysql> explain select * from users whee id = 1;
	
	ERROR 1064 (42000): You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'id = 1' at line 1

因为语法错误是在构建抽象语法树的时候发现的，上面例子中，虽然语法分析器不能知道whee是一个语法拼写错误，因为这个whee可能是表名users的别名，但是语法分析器在构建语法树到了id=1这里的时候就出错了，所以返回的报错信息可以提示，在“id=1”附近有语法错误。

语法分析器生成的抽象语法树并不仅仅可以用来做语法校验，它是下一步处理的基础。语义分析与优化器会对抽象语法树进一步做语义优化，也就是在保证SQL语义不变的前提下，进行语义等价转换，使最后的计算量和中间过程数据量尽可能小。

	select f.id from orders f where f.user_id = (select id from users); 

等价于

	select f.id from orders f join users u on f.user_id = u.id;

SQL语义分析与优化器就是将各种复杂嵌套的SQL进行语义等价转化，得到有限几种关系代数计算结构，并历用索引等信息进一步进行优化。

语义分析与优化器最后会输出一个执行计划，由执行引擎完成数据查询或者更新。MySQL执行的计划：

![f8dd0ab58f327832485bb412b2ca1b55.png](img/f8dd0ab58f327832485bb412b2ca1b55.png)

执行引擎是可替换的，直摇能够执行这个执行计划就可以了。所以MySQL有多种执行引擎（也叫存储殷勤）可以选择，缺省的是InnoDB，此外还有MyISAM、Memory等，我们可以在创建表的时候指定存储引擎。大数据仓库Hive也是这样的架构，Hive输出的执行计划可以在Hadoop上执行。

### 使用PrepareStatement执行SQL的好处 ###

1. PrepareStatement会预先提交带占位符的SQL到数据库进行预处理，提前生成执行计划，当给定占位符参数，真正执行SQL的时候，执行引擎可以直接执行，效率会更好一点。
2. PrepareStatement可以防止SQL注入攻击。

	select * from users where username = 'Frank';
	
	用户输入 
	'Frank';drop table users;--
	
	最后生成 
	select * from users where username = 'Frank';drop table users;--';
	
通过Statement提交SQL，会被当作两条SQL执行，一条是正常的select查询SQL，一条是删除users的SQL。

但PrepareStatement会提前进行语义分析，就不会被当作两条SQL处理，避免SQL注入。

	select * from users where username = ?;

### 数据库文件存储原理 ###

数据库通过索引进行查询加快查询速度 => 为什么索引能加快查询速度

数据库索引使用B+树，B+树是一种N叉排序树，树的每个节点包含N个数据，这些数据按顺序排好，两个数据之间是一个指向子节点的指针，而子节点的数据则在这两个数据大小之间。

B+树的节点存储在磁盘上，每个节点存储1000多个数据，这样树的深度只要4层，就可存储数亿的数据。如果将树的根节点缓存在内存中，则最多只需要三次磁盘访问就可以检索倒需要的索引数据。

B+树加快了索引的检索速度。

数据库索引有两种

1. 聚簇索引，聚簇索引的数据库记录和索引存储在一起，在叶子节点中，索引1和记录行r1存储在一起，查找到索引就是查找到数据库记录。像MySQL数据库的主键就是聚簇索引，主键ID和所在记录行存储在一起。MySQL的数据库文件实际上是以主键作为中间节点，行记录作为叶子节点的一颗B+树。
2. 非聚簇索引，非聚簇索引在叶子节点记录就不是数据行记录，而是聚簇索引，也就是主键。

通过B+树在叶子节点中找到非聚簇索引a，和索引a在一起存储的时主键1，再根据主键1通过主键（聚簇）索引就可以找到对应的记录r1，这种通过非聚簇索引找到主键索引，再通过主键索引找到行记录的过程也被称作回表。

数据库可以支持事务，一个事务对多条记录进行更新，要么全部更新，要么全部不更新，不能部分更新。数据库实现事务主要就是依靠事务日志文件。在进行事务操作时，事务日志文件会记录更新前的数据记录，然后再更新数据库中的记录，如果全部记录都更新成功，那么事务正常结束，如果过程中某条记录更新失败，那么整个事务全部回滚，已经更新的记录根据事务日志中记录的数据进行恢复，这样全部数据都恢复倒事务提交前的状态，仍然保持数据一致性。

MySQL数据库还有binlog日志文件，记录全部的数据更新操作记录，只要有binlog就可以完整复现数据库的历史变更，实现主从数据库的主从复制，构建高性能、高可用的数据库系统。

### 小结 ###

1. 掌握数据库的架构原理与执行过程
2. 数据库文件的存储原理与索引的实现方式
3. 数据库事务与数据库复制的基本原理

在开发工作中针对各种数据库问题去思考背后原理

### 思考题 ###

索引可以提高数据库的查询性能，那么是不是尽量多的使用索引呢？如果不是，为什么？你还了解哪些改善数据库放问性能的技巧方法？

<div class="_2_QraFYR_0">您好，老师：<br>回答上述问题<br>1.创建多的索引，会占用更多磁盘空间。如果有一张很大的表，索引文件的大小可能达到操作系统允许的最大文件限制；<br>2.对于DML操作的时候，索引会降低他们的速度。因为MySQL不仅要把搞定的数据写入数据文件，而且它还要把这些改动写入索引文件;<br>改善数据库性能：<br>1.索引优化，选择合适的索引列，选择在where、group by、order by、on 从句中出现的列作为索引项，对于离散度不大的列没有必要创建索引。<br>2.索引字段越小越好。<br>3.SQL语句的优化、数据表结构的优化。<br>&nbsp;&nbsp;&nbsp;&nbsp;3.1：选择可存数据最小的数据类型，选择最合适的字段类型，进行数据的存储;<br>&nbsp;&nbsp;&nbsp;&nbsp;3.2：数据量很大的一张表，应该考虑水平分表或垂直分表；<br>&nbsp;&nbsp;&nbsp;&nbsp;3.3：尽量不要使用text字段，如果非要用，那么应考虑将它存放另一张表中；<br>4.数据库配置的优化：<br>&nbsp;&nbsp;&nbsp;&nbsp;4.1：连接数的配置，因为大量的连接，不进行操作，那样也会占用内存。<br>&nbsp;&nbsp;&nbsp;&nbsp;4.2：查询缓存的配置，但在MySQL 8.0就删除了此功能。<br>5.硬件的配置;<br>&nbsp;额外加说一下，常见性能的问题：<br>1.条件字段函数的操作，给索引字段做了函数计算，就会破坏索引值，因此优化器就放弃了走树搜索能够;<br>2.隐式类型转换，比如数据库字段是varchar类型，创建的索引，但是使用的时候传入的是int类型，那么会走全表扫面;<br>3.隐式字符编码转换，如果join 两表的时候，两表的字符集不同，也不能用上索引；<br></div>

## 07 | 编程语言原理：面向对象编程是编程的终极形态？ ##

## 答疑 | Java Web程序的运行时环境到底是怎样的？ ##

### 问题一 ###


	java org.apache.catalina.startup.Bootstrap "$@" start 

我们在 Linux 操作系统执行 Tomcat 的 Shell 启动脚本，Tomcat 启动以后，其实在操作系统里看到的是一个 JVM 虚拟机进程。这个虚拟机进程启动以后，加载 class 进来执行，首先加载的就这个org.apache.catalina.startup.Bootstrap类，这个类里面有一个main()函数，是整个 Tomcat 的入口函数，JVM 虚拟机会启动一个主线程从这个入口函数开始执行。





## 08 | 软件设计的方法论：软件为什么要建模？ ##

### 软件建模 ###

### 4+1视图模型 ###

1. 逻辑视图：描述软件的功能逻辑，由哪些模块组成，模块中包含哪些类，其依赖关系如何。
2. 开发视图：包括系统架构层面的层次划分，包的管理，依赖的系统与第三方的程序包。
3. 过程视图：描述程序运行期的进程、线程、对象实例，以及与此相关的并发、同步、通信等问题。
4. 物理视图：描述软件如何安装并部署到物理的服务上，以及不同的服务器之间如何关联、通信。
5. 场景视图：针对具体的用例场景，将上述 4 个视图关联起来，一方面从业务角度描述，功能流程如何完成，一方面从软件角度描述，相关组成部分如何互相依赖、调用。

### UML 建模 ###

UML，即统一建模语言，是目前最常用的建模工具，使用 UML 可以实现 4+1 视图模型。

UML 也符合语言的这两个特点，一方面满足设计阶段和各个相关方沟通的目的；一方面可以用来思考，即使软件开发过程不需要跟其他人沟通，或者还没到沟通的时候，依然可以使用 UML 建模画图，帮助自己进行设计思考。

### 小结 ###

### 思考题 ###

### 精选留言 ###

#### 1. ####

一个合格的架构师必须是一个合格的产品经理、一个合格的研发、一个合格的测试，架构师的职责就是基于公司的战略和资源定位产品的核心价值、基于业务做技术选型、规范和指导研发、构建各维度的测试。
如果我刚刚称为公司的架构师，我会和决策层统一公司的核心价值，并以PPT的形式体现承载价值的核心功能。达成一致后，召集全体研发、测试(如有)开会统一思想，包括产品功能、性能指标、版本管理、部署上线、交付时间等，都必须是可量化的。期间，可以发动群众集思广益，收集有建设性建议并和决策层讨论并最终定版，以产品功能说明书作为交付物。之后，基于终版功能及指标，做技术架构选择，以架构部署图作为交付物，并召集资深开会讨论，查缺补漏。之后，召集全体开发、测试(如有)开会，以使每个人都清楚自己的交付物和指标。之后，就可以开始功能迭代过程了。
综上，核心价值确定第一位，统一思想第二位，技术选型第三位，其他都可以见招拆招，或直接水到渠成了。

#### 2. ####



## 09 | 软件设计实践：如合使用UML完成一个设计文档 ##

类图、时序图、组件图、部署图、用例图、状态图

### 类图 ###

类图是最常见的UML图形，用来描述类的特性和类之间的静态关系。

一个类包含三个部分：类的名字、类的属性列表和类的方法列表。

类之间有6种静态关系：关联、依赖、组合、聚合、继承、泛化。

需求分析阶段

### 序列图 ###

软件设计的不同阶段

### 组件图 ###

组件是比类粒度更大的设计元素，一个组件中通常包含多个类。组件图有的时候和包图的用途比较接近，组件图通常用来描述物理上的组件，比如一个JAR，一个DLL等等。在实际种，进行模块设计的时候使用组件图。

### 部署图 ###

概要设计阶段

### 用例图 ###

需求分析阶段，通过反映用户和软件系统的交互，描述系统的功能需求。

### 状态图 ###

状态图用来展示单个对象生命周期的的状态变迁。

### 活动图 ###

活动图主要用来描述过程

### 使用合适的UML模型构建一个设计文档 ###

#### 需求分析阶段 ####

用例图来描述系统的功能与使用场景

关键的业务场景可以使用活动图来描述

如果在需求阶段就提出和现有的某些子系统整合，通过时序图描述新系统和原来的子系统的调用关系

通过简化的类图进行领域模型抽象，并描述核心领域对象之间的关系。

如果某些对象内部会有复杂的状态，比如用户、订单这些，可以用状态图进行描述。

#### 概要设计阶段 ####

通过部署图描述系统最终的物理蓝图

通过组件图以及组件时序图设计软件主要模块及其关系

通过组件活动图描述组件间的流程逻辑

#### 详细设计阶段 ####

输出类图和类的时序图，指导最终的代码开发。

如果某个类方法内部有复杂的逻辑，可以用画方法的活动图进行描述。

### 小结 ###

## 加餐 ##

概要设计文档和详细设计文档合并成一个设计文档。

1. 文档开头是设计概览，简单描述业务场景要解决的核心问题领域是什么。
2. 组件图，描述子系统由哪些组件组成，不同场景中，组件之间的调用序列图是什么样的。
3. 每个组件内部，需要用类图进行建模描述，对于不同场景，用时序图描述类之间的动态调用关系，对于复杂状态的类，用状态图描述其状态转换。

## 10 | 软件设计的目的：糟糕的程序员逼优秀的程序员差在哪里？ ##

程序员的好坏：

1. 体现在编程能力上
2. 体现在程序设计上

在软件设计开发这个领域，好的设计和坏的设计最大的差别就体现在应对需求变更的能力上。而好的程序员和差的程序员的一个重要区别，就是对待需求变更的态度。差的程序员害怕需求变更；好的程序员则欢迎需求变更，因为他们一开始就针对需求变更进行了软件设计。

### 糟糕的设计 ###

僵化性
脆弱性
牢固性
粘滞性
晦涩性

### 一个设计腐坏的例子 ###

### 解决之道 ###

所以你能看到，应对需求变更最好的办法就是一开始的设计就是针对需求变更的，并在开发过程中根据真实的需求变更不断重构代码，保持代码对需求变更的灵活性。

### 小结 ###

### 思考题 ###

### 精选留言 ###

#### 1. ####

僵化性代码的例子是滥用了继承，导致添加一个小功能，所有的基类和派生类都要修改。
脆弱性代码的例子是引入全局依赖，导致意外的修改扩散。每当我看到很多全局变量的时候，对程序的掌控感荡然无存。
牢固性代码的例子是超大类，由于类内部是可以任意访问，所有的巨量函数和属性组成了一个巨大完全图，牵一发而动全身，根本不知道从哪里下手。
粘滞性代码的一个例子还是全局变量，大家觉得觉得用得也挺顺手的，还有人说重用这些能提高效率，让我也很无语。有了注入依赖以后，这些全局变量被包了一层外衣，到处泛滥而不可收拾。
晦涩性代码的例子是过多if语句，一开始可能还好，最后if越加越多，导致看完都成问题。

## 11 | 软件设计的开闭原则：如何不修改代码却能实现需求变更？ ##

### 开闭原则 ###

开闭原则说：**软件实体（模块、类、函数等等）应该对扩展是开放的，对修改是关闭的。**

对扩展是开放的，意味着软件实体的行为是可扩展的，当需求变更的时候，可以对模块进行扩展，使其满足需求变更的要求。

对修改是关闭的，意味着当对软件实体进行扩展的时候，不需要改动当前的软件实体；不需要修改代码；对于已经完成的类文件不需要重新编辑；对于已经编译打包好的模块，不需要再重新编译。

软件功能可以扩展，但是软件实体不可以被修改。

### 一个违反开闭原则的例子 ###



## 12 | 软件设计的依赖倒置原理：如何不依赖代码却可以复用它的功能？ ##

### 依赖倒置原则 ###

* 高层模块不应该依赖低层模块，二者都应该依赖抽象。
* 抽象不应该依赖具体实现，具体实现应该依赖抽象。

高层模块依赖低层模块

1. 维护困难
2. 复用困难

### 依赖倒置的关键是接口所有权的倒置 ###

策略层依赖方法层，方法层依赖工具层，策略层对方法层和工具层是传递依赖的，下面两层的任何改动都会导致策略层的改动。

### 小结 ###

依赖倒置原则通俗点说，高层不依赖低层模块

1. 应用代码中多使用抽象接口，尽量避免使用那些多变的具体实现类。
2. 不要继承具体类，如果一个类在设计之初不是抽象类，那些尽量不要去继承它。对具体类的继承是一种强依赖关系，维护的时候难以改变。
3. 不要重写（override）包含具体实现的函数。

## 15 | 软件设计的接口隔离原则：如何对类的调用者隐藏类的公有方法？ ##

缓存Client类的方法主要包含两个部分：

1. 一部分是缓存服务方法，get()、put()、delete()
2. 一部分是配置更新方法，reBuild()

### 接口隔离原则 ###

接口隔离原则：**不应该强迫用户依赖他们不需要的方法。**

### 一个使用接口隔离原则优化的例子 ###

#### Base ####

* Door 
* Timer
* TimerClient

#### Adapter ####

* Door 
* DoorTimerAdapter
* Timer
* TimerClient

#### ISP ####

* Door 
* TimedDoor 
* Timer
* TimerClient

### 接口隔离原则在迭代器设计模式的应用 ###

Java 的数据结构容器类可以通过 for 循环直接进行遍历

	List<String> ls = new ArrayList<String>();
	ls.add("a");
	ls.add("b");
	for(String s: ls) {
	  System.out.println(s);
	}

这种 for 语法结构并不是标准的 Java for 语法，标准的 for 语法在实现上述遍历时应该是这样的：

	for(Iterator<String> itr=ls.iterator();itr.hasNext();) {
	  System.out.println(itr.next());
	}

在 Java5 以前，每种容器的遍历方法都不相同，在 Java5 以后，可以统一使用这种简化的遍历语法实现对容器的遍历。而实现这一特性，主要就在于 Java5 通过 Iterable 接口，将容器的遍历访问从容器的其他操作中隔离出来，使 Java 可以针对这个接口进行优化，提供更加便利、简洁、统一的语法。

### 小结 ###

如何让缓存类的使用者看不到缓存重构的方法，以避免不必要的依赖和方法的误用。答案就是使用接口隔离原则，通过多重继承的方式进行接口隔离。

Cache 实现类 BazaCache（Baza 是当时开发的统一缓存服务的产品名）同时实现 Cache 接口和 CacheManageable 接口，其中 Cache 接口提供标准的 Cache 服务方法，应用程序只需要依赖该接口。而 CacheManageable 接口则对外暴露 reBuild() 方法，使远程配置服务可以通过自己的本地代理调用这个方法，在运行期远程调整缓存服务的配置，使系统无需重新部署就可以热更新。

当一个类比较大的时候，如果该类的不同调用者被迫依赖类的所有方法，就可能产生不必要的耦合。对这个类的改动也可能会影响到它的不同调用者，引起误用，导致对象被破坏，引发 bug。

使用接口隔离原则，就是定义多个接口，不同调用者依赖不同的接口，只看到自己需要的方法。而实现类则实现这些接口，通过多个接口将类内部不同的方法隔离开来。

### 思考题 ###

在你的开发实践中，你看到过哪些地方使用了接口隔离原则？你自己开发的代码，哪些地方可以用接口隔离原则优化？

### 精选留言 ###

#### 1.  ####

老师 Iterable 为什么是属于接口隔离原则这个没太理解 能再详细讲一下吗

作者回复: 每种数据容器实现类的内部都是不同的，但是它们都可以通过Iterable遍历，Client无需关注容器类的实现就能用统一的方式遍历容器。而Iterable接口也看不到容器类的内部实现，所以是一种接口隔离。

## 18 | 反应式编程框架设计：如何使程序调用不阻塞等待，立即响应？ ##

### 反应式编程 ###

反应式编程本质上是一种异步编程方案，在多线程（协程）、异步方法调用、异步 I/O 访问等技术基础之上，提供了一整套与异步调用相匹配的编程模型，从而实现程序调用非阻塞、即时响应等特性，即开发出一个反应式的系统，以应对编程领域越来越高的并发处理需求。

* 即时响应
* 回弹性
* 弹性
* 消息驱动

* RxJava、Reactor，主要特点基于观察者设计模式的异步编程方案，编程模型采用函数式编程。
* Flower 就是一个纯消息驱动，完全异步，支持命令式编程的反应式编程框架。

### 反应式编程框架 Flower 的基本原理 ###

使用 Flower 框架只需要极少的容器线程就可以处理较多的并发用户请求，而且容器线程不会阻塞。

只需要有限的几个线程就可以完成大量的 Service 处理和消息传输，这些线程不会阻塞等待。

* 有限的线程就可以完成大量的并发用户请求，从而大大地提高了系统的吞吐能力和响应时间
* 应用就不会因为并发量太大或者数据库处理缓慢而宕机，从而提高了系统的可用性

### 反应式编程框架 Flower 的设计方法 ###



## 19 | 组件设计原则：组件的边界在哪里？ ##

## 20 | 领域驱动设计：35岁的程序员应该写什么样的代码？ ##

如果你对自己要开发的业务领域没有清晰的定义和边界，**没有设计系统的领域模型**，而仅仅跟着所谓的需求不断开发功能，一旦需求来自多个方面，就可能发生需求冲突，或者随着时间的推移，前后功能也会发生冲突，这时你越是试图弥补这些冲突，就越是陷入更大的冲突之中。

软件只有需求分析，并没有真正的设计，系统没有一个统一的领域模型维持其内在的逻辑一致性。功能特性并不是按照领域模型内在的逻辑设计，而是按照各色人等自己的主观想象设计。

### 领域模型模式 ###

目前企业级应用开发中，业务逻辑的组织方式主要是**事务脚本**模式。事务脚本按照业务处理的过程组织业务逻辑，每个过程处理来自客户端的单个请求。客户端的每次请求都包含了一定的业务处理逻辑，而程序则按照每次请求的业务逻辑进行划分。

事务脚本模式典型的就是 Controller→Service→Dao 这样的程序设计模式。Controller 封装用户请求，根据请求参数构造一些数据对象调用 Service，Service 里面包含大量的业务逻辑代码，完成对数据的处理，期间可能需要通过 Dao 从数据库中获取数据，或者将数据写入数据库中。

### 领域驱动设计（DDD） ###

**领域**是一个组织所做的事情以及其包含的一切，就是组织的业务范围和做事方式，也是软件开发的目标范围。**领域驱动设计就是从领域出发，分析领域内模型及其关系，进而设计软件系统的方法**。

如果我们说要对 C2C 电子商务这个领域进行建模设计，那么这个范围就太大了，不知道该如何下手。所以通常的做法是把整个领域拆分成多个**子域**，比如用户、商品、订单、库存、物流、发票等。强相关的多个子域组成一个**界限上下文**，界限上下文是对业务领域范围的描述，对于系统实现而言，可以想象成相当于是一个子系统或者是一个模块。界限上下文和子域共同组成组织的领域。

在 DDD 中，领域模型对象也被称为**实体**，每个实体都是唯一的，具有一个唯一标识，一个订单对象是一个实体，一个产品对象也是一个实体，订单 ID 或者产品 ID 是它们的唯一标识。实体可能会发生变化，比如订单的状态会变化，但是它们的唯一标识不会变化。

**实体设计**是 DDD 的核心所在，首先通过业务分析，识别出实体对象，然后通过相关的业务逻辑设计实体的属性和方法。事实上，并不是领域内的对象都应该被设计为实体，DDD 推荐尽可能将对象设计为**值对象**。

通过领域实体及其交互完成业务逻辑处理才是 DDD 的核心目标。

### 小结 ###

如何将这些业务沉淀和理解反映到工作中，体现在代码中呢？也许可以尝试探索领域驱动设计。如果一个人有多年的领域经验，那么必然对领域模型设计有更深刻的认识，把握好领域模型在不断的需求变更中的演进，使系统维持更好的活力，并因此体现自己真正的价值。

### 思考题 ###

你觉得大龄程序员的优势是什么？如何在公司保持自己的优势和地位？

### 精选留言 ###

#### 1. ####

事务脚本模式的特点在于状态都在数据库里，业务逻辑在service层，业务逻辑与状态完全分离。
领域对象模型，特点是聚合状态和操作，提供相对独立的模块和类。领域的模型的对象之一是实体，有唯一标识，实体被销毁前标识不变，能通过标识获得，实体的状态会变化。领域的另外一种对象是值对象，状态在生命周期内不变，因而更简单。领域对象因为有状态可以表达更为丰富的关系。但是设计好的领域对象依靠的主要不是技术而是对业务的理解。大龄程序员选择的业务领域的余地还是越来越小的，可能人老了就是这样。

架构的核心原理

## 21 | 分布式架构：如何应对高并发的用户请求 ##

面对同样的高并发用户的访问请求压力

# 垂直伸缩与水平伸缩 #

1. 垂直伸缩
2. 

## 22 | 缓存架构：如何减少不惜要的计算？ ##

互联网应用的主要挑战就是在高并发情况下，大量的用户请求到达应用系统服务器，造成了巨大的计算压力。互联网应用的核心解决思路就是采用分布式架构，提供更多的服务器，从而提供更多的计算资源，以应对高并发带来的计算压力及资源消耗。

减少不必要的计算，降低服务器的计算资源消耗，尽快返回计算结果给用户呢？ => 缓存

缓存可以分成两种，通读缓存和旁路缓存

* 通读（read-through）缓存：应用程序访问通读缓存获取数据的时候，如果通读缓存有应用程序需要的数据，那么就返回这个数据；如果没有，那么通读缓存就自己负责访问数据源，从数据源获取数据返回给应用程序，并将这个数据缓存在自己的缓存中。
* 旁路（cache-aside）缓存：应用程序访问旁路缓存获取数据的时候，如果旁路缓存中有应用程序需要的数据，那么就返回这个数据；如果没有，就返回空（null）。应用程序需要自己从数据源读取数据，然后将这个数据写入到旁路缓存中。

### 通读缓存 ###

CDN和反向代理缓存

* CDN（Content Delivery Network）即内容分发网络。CDN只能缓存静态数据内容。
* 反向代理缓存也是一种通读缓存。用户请求到达反向代理缓存服务器，反向代理检查本地是否有需要的数据，如果有就直接返回，如果没有，就请求应用服务器，得到需要的数据后缓存在本地，然后返回给用户。

### 旁路缓存 ###

对象缓存是一种旁路缓存，缓存通常都是以 <key, value> 的方式存储在缓存中。因此通读缓存和旁路缓存在实现上，基本上用的是 Hash 表。

程序中使用的对象缓存，可以分成两种。

1. 本地缓存，缓存和应用程序在同一个进程中启动，使用程序的堆空间存放缓存数据。
2. 分布式缓存，将一组服务器构成一个缓存集群，共同对外提供缓存服务，那么应用程序在每次读写缓存的时候

![ca000caeaca469128dee3a59ddd0896e.png](img\ca000caeaca469128dee3a59ddd0896e.png)

Memcached 将多台服务器构成一个缓存集群，缓存数据存储在每台服务器的内存中。事实上，使用缓存的应用程序服务器通常也是以集群方式部署的，每个程序需要依赖一个 Memcached 的客户端 SDK，通过 SDK 的 API 访问 Memcached 的服务器。

应用程序调用 API，API 调用 SDK 的路由算法，路由算法根据缓存的 key 值，计算这个 key 应该访问哪台 Memcached 服务器，计算得到服务器的 IP 地址和端口号后，API 再调用 SDK 的通信模块，将 <key, value> 值以及缓存操作命令发送给具体的某台 Memcached 服务器，由这台服务器完成缓存操作。

### 缓存注意事项 ###

使用缓存可以减少不必要的计算

1. 缓存的数据通常存储在内存中，距离使用数据的应用也更近一点，因此相比从硬盘上获取，或者从远程网络上获取，它获取数据的速度更快一点，响应时间更快，性能表现更好。
2. 存的数据通常是计算结果数据，比如对象缓存中，通常存放经过计算加工的结果对象，如果缓存不命中，那么就需要从数据库中获取原始数据，然后进行计算加工才能得到结果对象，因此使用缓存可以减少 CPU 的计算消耗，节省计算资源，同样也加快了处理的速度。
3. 通过对象缓存获取数据，可以降低数据库的负载压力；通过 CDN、反向代理等通读缓存获取数据，可以降低服务器的负载压力。这些被释放出来的计算资源，可以提供给其他更有需要的计算场景，比如写数据的场景，间接提高整个系统的处理能力。

技术人的思维修炼

## 答疑 | 工作中的交往和沟通，都有哪些小技巧呢？ ##

### 保持交际和赞美 ###

赞美是对对方做得好的事情，明确表达你的称赞。

### 平衡力量和温暖 ###



## 34 | 技术修炼之道：同样工作十几年，为什么有的人成为大厂架构师，有的人失业？ ##

### 德雷福斯模型 ###

1. 专家：用最直接、最简单的方法把问题解决
2. 精通者：精通者需要通过主动学习进行提升，主动进行大量的阅读和培训，拥有了自我改进的能力。
3. 胜任者：做事具有主动性，遇到心的问题，会积极寻求新的解决方案去解决问题
4. 高级新手：高级新手不知道自己是高级新手。
5. 新手

### 如何在工作中成长 ###

#### 1. 用于承担责任 ####

所有真正的领悟都是痛的领悟，只有你对自己的工作的结果承担责任和后果，在出现问题或者可能出现的时候，倒逼自己思考技术的关键点，技术的缺陷与优势，才能真正地理解这项技术。

如果你只是去遵循别人的指令，按别人的规则去做事情，你永远不会知道事物的真相是什么。只有你对结果负责的时候，在压力之下，你才会看透事物的本质，才会抓住技术的核心和关键，才能够让你去学好技术，用好技术，在团队中承担核心的技术职责和产生自己的技术影响，并巩固自己的技术地位。

#### 2. 在实践中保持技能 ####

1万小时定律

但是1万个小时的编程时间并不是说你重复的编程1万小时就能够自动提升成为专家。真正对你有帮助的是不断超越自我，挑战自我的工作。也就是说，每一次在完成一个工作以后，下一次的工作都要比上一次的工作难度再增加一点点，不断地让自己去挑战更高难度的工作，从而拥有更高的技术能力和技术认知。

### 3. 关注问题场景 ###

所谓的专家其实是善于根据问题场景发现解决方法的那个人，如果你关注场景，根据场景去寻找解决办法，也许你会发现解决问题的办法可能会非常简单，也许并不需要多么高深的工具和方法就能够解决，这时候你才能成为真正的专家。也就是在这个时候你会意识到方法、技术、工具这些都不是最复杂的，而真正复杂的是问题的场景，是如何真正地理解问题。

### 小结 ###

### 思考题 ###

### 精选留言 ###

## 35 | 技术进阶之道：你和这个星球最顶级的程序员差几个等级？ ##



# 不定期加餐 #

## 加餐 | 软件设计文档示例模板 ##

1. 文档开头是设计概述，简单描述业务场景要解决的核心问题领域是什么。至于业务场景，应该在专门的需求文档中描述，但是在设计文档中，必须要再简单描述一下，以保证设计文档的完整性，这样，即使脱离需求文档，阅读者也能理解主要的设计。
2. 此外，在设计概述中，还需要描述设计的非功能约束，比如关于性能、可用性、维护性、安全性，甚至开发和部署成本方面的设计目标。
3. 然后就是具体的设计了，第一张设计图应该是部署图，通过部署图描述系统整个物理模型蓝图，包括未来系统长什么样。
	1. 如果系统中包含几个子系统，那么还需要描述子系统间的关系，可以通过子系统序列图，子系统活动图进行描述。
	2. 子系统内部的最顶层设计就是组件图，描述子系统由哪些组件组成，不同场景中，组件之间的调用序列图是什么样的。
	3. 每个组件内部，需要用类图进行建模描述，对于不同场景，用时序图描述类之间的动态调用关系，对于有复杂状态的类，用状态图描述其状态转换。

# 结束语 #

## 结束语 | 期待未来的你，称为优秀的软件架构师 ##

一个优秀的软件架构师

* 能够设计一个良好架构的系统，并在它漫长的生命周期中保持架构持续演进、清晰合理
* 能够写漂亮的技术 PPT，也能写漂亮的代码，让自己开发的核心代码支撑起系统的核心架构，又让自己的架构方案得到大多数人的拥护
* 应该有宏观的技术视角，能够用更广阔的愿景去诠释当前项目的技术、架构和未来的演化趋势
* 应该拥有某种技术影响力和领导力，无需职位上的权力就可以让其他工程师听信于他
* 应该掌握一些特别的管理、谈判技能，让自己的技术构想被其他工程师、项目经理、老板和用户接纳

我们在职业技能进阶的道路上也是如此，你如果依着本能，跟着潮流，除非你极有天分，否则很难超越自己和环境。架构师的成长之路是一条攀登之路，你需要有意识地训练自己，不断挑战自己。架构师的成长之路是一条修行之路，你要和自己的本能做对，不断审视自己，让自己从舒适区跳出来，针对自己的不足和缺陷为自己设计有困难的任务和目标。

这条路注定艰辛，但是走在这样的人生之路上，你会充分体验到超越自我的愉悦，理解到生而为人的自由意志，也许这也是人生的某种意义吧。

