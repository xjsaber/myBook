# SQL必知必会 #

## 开篇词|SQL可能是你掌握的最有用的技能 ##

OLTP（联机事务处理过程）、OLAP（联机分析处理过程）、RDBMS（对象关系型数据库管理系统）

### 1. 基础篇 ###

### 2. 进阶篇 ###

### 3. 高级篇 ###

* SQL阵营：Oracle、MySQL、SQL Server、Access、WebSQL、SQLite
* NoSQL阵营：MongoDB、Redis

### 4. 实战篇 ###

## 1 | 了解SQL：一门半衰期很长的语言 ##

### 半衰期很长的SQL ###

SQL92 SQL99

### 入门SQL并不难 ###

1. DDL，英文叫做Data Definition Language
2. DML，
3. DCL，
4. DQL，英文叫做Data Query Language

### 开启SQL之旅 ###

1. 表名、表别名、字段名、字段别名等都小写
2. SQL保留字、函数名、绑定变量等都大写

# 2 | DBMS的前世今生 #

### DB、DBS和DBMS的区别是什麽 ###

DBMS 的英文全称是 DataBase Managemen System，数据库管理系统，可以对多个数据库进行管理，理解为DBMS = 多个数据库（DB） + 管理程序。

DB的英文是DabaBase，也就是数据库。

DBS的英文是DataBase System，数据库系统。

键值型数据库通过Key-Value键值的方式来存储数据。键值型数据库典型的使用场景是作为内存缓存。Redis是最流行的键值型数据库。

文档型数据库管理文档，在数据库中文档作为处理信息的基本单元，一个文档就相当于一条记录，MongoDB是最流行的文档型数据库。

搜索引擎，常见的全文搜索引擎有Elasticsearch、Splunk和Solr。虽然关系型数据库采用了索引提升检索效率，但是针对全文索引效率却较低。搜索引擎的优势在于采用了全文搜索的技术，核心原理是“倒排索引”

列式数据库是相对于行式存储的数据库，Oracle、MySQL、SQL Server 等数据库都是采用的行式存储（Row-based），而列式数据库是将数据按照列存储到数据库中，这样做的好处是可以大量降低系统的 I/O，适合于分布式文件系统，不足在于功能相对有限。

图形数据库，利用了图这种数据结构存储了实体（对象）之间的关系。最典型的例子就是社交网络中人与人的关系，数据模型主要是以节点和边（关系）来实现，特点在于能高效地解决复杂的关系问题。

### SQL阵营和NoSQL阵营 ###


### 总结 ###

行式存储是把一行的数据都串起来进行存储，然后再存储下一行。
列式存储是把一列的数据都串起来进行存储，然后再存储下一列。
就是相邻的数据的数据类型是一样的，因此也更容易压缩。压缩之后就自然降低了IO。

[一分钟搞懂列式与行式数据库](https://blog.csdn.net/qq_35952082/article/details/68490536)

## 03 | 学会用数据库的方式思考SQL是如何执行的 ##

1. Oracle种的SQL是如何执行的，什么是硬解析和软解析；
2. MySQL中的SQL是如何执行的，MySQL的体系结构又是怎么样的；
3. 什么是存储引擎，MySQL的存储引擎都有哪些？

### Oracle的SQL是如何执行的 ###

1. 语法检查：检查SQL拼写是否正确，如果不正确，Oracle会报语法错误。
2. 语义检查：检查SQL中的访问对象是否存在。比如写SELECT语句的时候，列名写错了，系统就会提示错误，语法检查和语义检查的作用是保证SQL语句没有错误。
3. 权限检查：看用户是否具备访问该数据的权限。
4. 共享池检查：共享池（Shared Pool）是一块内存池，最主要的作用是缓存SQL语句和该语句的执行计划。Oracle通过检查共享池是否存在SQL语句的执行计划，来判断进行软解析，还是硬解析。那软解析和硬解析又该怎么理解。如果没有找到SQL语句和执行计划，Oracle就需要创建解析树进行解析，生成执行计划，进入“优化器”这个步骤，这就是硬解析。
5. 优化器：优化器就是要进行硬解析，也就是决定怎么做，比如创建解析树，生成执行计划。
6. 执行器：当有了解析数和执行计划之后，就知道了SQL该怎么被执行，这样就可以在执行器中执行语句了。

共享池是Oracle中的术语，包括了库缓存，数据字典缓冲区等。库缓存区，主要缓存SQL语句和执行计划。而数据字典缓冲区存储的是Oracle中的对象定义，比如表、视图、索引等对象。当对SQL语句进行解析的时候，如果需要相关数据，会从数据字典缓冲区中提取。

库缓存这一个步骤，决定了SQL语句是否需要硬解析。为了提升SQL的执行效率，应该尽量避免硬解析，因为在SQL的执行过程中，创建解析树，生成执行计划是很消耗资源的。

如何避免硬解析，尽量使用软解析。在Oracle中，绑定变量是它的一个特色。绑定变量就是在SQL语句中使用变量，通过不同的变量取值来改变SQL的执行结果。

	select * from player where player_id = 10001;

	select * from pyayer where player_id = :player_id;

使用绑定变量来减少硬解析，减少Oracle的解析工作量。但是这种方式也有缺点，使用动态SQL的方式，因为参数不同，会导致SQL的执行效率不同，同时SQL的优化比较困难

### MySQL中的SQL是如何执行的 ###

MySQL由三层组成：

1. 连接层：客户端和服务端建立连接，客户端发送SQL至服务器端；
2. SQL层：对SQL语句进行查询处理；
3. 存储引擎层：与数据库文件打交道，负责数据的存储和读取。

---

1. 查询缓存：Server如果在查询缓存中发现这条SQL语句。（MySQL8.0之后就抛弃了这个功能）
2. 解析器：在解析器中对SQL语句进行语法分析、语义分析。
3. 优化器：在优化器中会确定SQL语句的执行路径，比如是根据全表检索，还是根据索引检索等。
4. 执行器：在执行之前需要判断该用户是否具备权限，如果具备权限就执行SQL查询并返回结果。在MySQL8.0以下的版本，如果设置了查询缓存，这时就会将查询结果进行缓存。

SQL语句在MySQL中的流程是：SQL语句->缓存查询->解析器->优化器->执行器。

与Oracle不同的是，MySQL的存储引擎采用了插件的形式，每个存储引擎都面向一种特定的数据库应用环境。同时开源的MySQL还允许开发人员设置自己的存储引擎：

1. InnoDB存储引擎：它是MySQL5.5版本之后默认的存储引擎，最大的特点是支持事务、行级锁定、外键约束等。
2. MyISAM存储引擎：在MySQL5.5版本之前是默认的存储引擎，不支持事务，也不支持外键，最大的特点是速度快，占用资源少。
3. Memory存储引擎：使用系统内存作为存储介质，以便得到更快的响应速度。不过如果mysqld进程崩溃，则会导致所有的数据丢失，因此我们只有当数据是临时的情况下才使用Memory存储引擎。
4. NDB存储引擎：也叫做NDB Cluster存储引擎，主要是用于MySQL Cluster分布式集群环境，类似Oracle的RAC集群。
5. Archive存储引擎：它有很好的压缩机制，用于文件归档，在请求写入时会进行压缩，所以也经常用来做仓库。

数据库的设计在于表的设计，而在MySQL中每个表的设计都可以采用不同的存储引擎，可以根据实际的数据处理需要来选择引擎。

### 数据库管理系统也是一种软件 ###

### 总结 ###

相同的地方在于Oracle和MySQL都是通过解析器->优化器->执行器这样的流程来执行SQL的。

但Oracle和MySQL在进行SQL的查询上面有软件实现层面的差异。

Oracle提出了共享池的概念，通过共享池来判断进行软解析，还是硬解析。

MySQL，8.0以后的版本不再支持查询缓存，而是直接执行解析器->优化器->执行器的流程，这一点从MySQL中的show profile里也能看到。

## 04 使用DDL创建数据库&数据表需要注意什么？ ##

DDL是DBMS的核心组件，也是SQL的重要组成部分。

1. 了解DDL的基础语法，它如何定义数据库和数据表；
2. 使用DDL定义数据表时，都有哪些约束性；
3. 使用DDL设计数据库时，都有哪些重要原则。

### DDL的基础语法及设计工具 ###

DDL的英文全称是Data Definition Language，中文是数据定义语言。定义了数据库的解耦和数据表的结构。

在 DDL 中，我们常用的功能是增删改，分别对应的命令是 CREATE、DROP 和 ALTER。需要注意的是，在执行 DDL 的时候，不需要 COMMIT，就可以完成执行任务。

#### 1. 对数据库进行定义 ####

#### 2. 对数据表进行定义 ####

### 创建表结构 ###

## 5 检索数据：你还在SELECT * ##

### SELECT 查询的基础语法 ###

#### 查询列 ####

#### 起别名 ####

#### 查询常数 ####

#### 去除重复行 ####

### 如何排序检索数 ###

### 约束返回结果的数量 ###

### SELECT的执行顺序 ###

### 总结 ###

1. 不使用`*`
2. 使用`limit 1` 或者 `top 1`

### 思考题 ###

你可以说明下使用的 DBMS 及相应的 SQL 语句。

## 06 | 数据过滤：SQL数据过滤都有哪些方法？ ##

### 逻辑运算符 ###

### 使用通配符过滤 ###

### 总结 ###

比较运算符是对数值进行比较，不同的 DBMS 支持的比较运算符可能不同，需要事先查询相应的DBMS文档。逻辑运算符可以让我们同时使用多个`WHERE`字句，需要注意`AND`和`OR`运算符的执行顺序。通配符可以对文本类型的字段进行模糊查询，不过检索的代价很高，通常需要全表扫描，效率很低。只有当`LIKE`语句后面不用通配符，对字段进行索引的时候才不会对全表进行扫描。


保持高效率的一个很重要的原因，就是要避免全表扫描，所以我们会考虑在`WHERE`及`ORDER BY`涉及到的列上增加索引。

## 07 | 什么是SQL函数？为什么使用SQL函数可能会带来问题 ##

### 什么是SQL函数 ###

### 算术函数 ###

|函数名|定义|
|--|--|
|ABS()|取绝对值|
|MOD()|取余|
|ROUND()|四舍五入为指定的小数位数，需要有两个参数，分别为字段名称、小数位数| 

### 字符串函数 ###

常用的字符串函数操作包括了字符串拼接，大小写转换，求长度以及字符串替换和截取等

|函数名|定义|
|--|--|
|CONCAT|将多个字符串进行拼接|
|LENGTH|计算字段的长度、一个汉字算三个字符，一个数字或字母算一个字符|
|CHAR_LENGTH|计算字段的长度，汉字、数字、字母都算一个字符|
|LOWER|将字符串中的字符转化为小写|
|UPPER|将字符串中的字符转化为小写|
|REPLACE|替换函数，有3个参数，分别为要替换的表达式或者字段名、想要查找的被替换字符串、替换成哪个字符串|
|SUBSTRING|截取字符串，要3个参数，分别为：待截取的表达式或字段名、开始截取的位置、想要截取的字符串长度|

	SELECT REPLACE('FABCD', 'ABC'， 123)

	SELECT SIBSTROMG('FABCD', 1， 3)

### 日期函数 ###

|函数名|定义|
|--|--|
|CURRENT_DATE()|系统当前日期|
|CURRENT_TIME()|系统当前时间，没有具体的日期|
|CURRENT_TIMESTAMP()|系统当前时间，包括具体的日期+时间|
|EXTRACT()|抽取具体的年、月、日|
|DATE()|返回时间的日期部分|
|YEAR()|返回时间的年份部分|
|MONTH()|返回时间的月份部分|
|DAT()|返回时间的天数部分|
|HOUR()|返回时间的小时部分|
|MINUTE()|返回时间的分钟部分|
|SECOND()|返回时间的秒部分|

### 转换函数 ###

|函数名|定义|
|--|--|
|CAST()|数据类型转换，参数是一个表达式，表达式通过AS关键词分割了2个参数，分别是原始数据和目标数据类型|
|COALESCE()|返回第一个非空数值|

TODO

### 为什么使用SQL函数会带来问题 ###

SQL函数的代码可移植性很差

### 关于大小写的规范 ###

MySQL在Linux的环境下，数据库名、表名、变量名是严格区分大小写，而字段名是忽略大小写的。

而MySQL在Windows的环境下全部不区分大小写。

如果变量名命名规范没有统一，就可能产生错误。

1. 关键字和函数名称全部大写；
2. 数据库名、表名、字段名称全部小写；
3. SQL语句必须以分号结尾；

虽然关键字和函数名称在SQL中不区分大小写，小写的话同样可以执行，但是数据库名、表名和字段名在Linux MySQL环境下也是区分大小写的，统一这些字段的命名规则，比如全部采用小写的方式。同时将关键词和函数名称全部大写，以便于区分数据库名、表名、字段名。

### 总结 ###

多个DBMS同时存在的情况下，使用函数需要慎重

# 08 | 什么是SQL的聚集函数，如何利用他们汇总表的数据？ #

### 聚集函数 ###

|函数|说明|
|--|--|
|COUNT()|总行数|
|MAX()|最大值|
|MIN()|最小值|
|SUM()|求和|
|AVG()|平均值|

### 如何对数据进行分组，并进行聚集统计 ###

### 如何使用HAVING过滤分组，它与WHERE的区别是什么？ ###

WHERE是用于数据行，HAVING则作用于分组。

### 总结 ###

```
SELECT ... FROM ... WHERE ... GROUP BY ... HAVING ... ORDER BY
```

## 09 | 子查询：子查询的种类都有哪些，如何提高子查询的性能？ ##

### 什么是关联子查询，什么是非关联子查询 ###

1. 子查询可以分为关联子查询和非关联子查询
2. 子查询有一些关键字，可以方便对子查询的结果进行比较，比如存在性子查询，也就是EXIST子查询，以及集合比较子查询，其中集合比较子查询关键词有IN、SOME、ANY和ALL

如果子查询的执行依赖于外部查询，通常情况下都是因为子查询中的表用到了外部的表，并进行了条件关联，因此每执行一次外部查询，子查询都要重新计算一次。

### EXISTS子查询 ###

关联子查询通常也会和 EXISTS 一起来使用，EXISTS 子查询用来判断条件是否满足，满足的话为 True，不满足为 False。

### 集合比较子查询 ###

* IN：判断是否在集合中
* ANY：需要与比较操作符一起使用，与子查询返回的任何值做比较
* ALL：需要与比较操作符一起使用，与子查询返回的任何值做比较
* SOME：实际上是ANY的别名，作用相同，一般常使用ANY

#### IN ####

**IN与EXISTS比较**

实际上在查询过程中，在我们对 cc 列建立索引的情况下，我们还需要判断表 A 和表 B 的大小。在这里例子当中，表 A 指的是 player 表，表 B 指的是 player_score 表。如果表 A 比表 B 大，那么 IN 子查询的效率要比 EXIST 子查询效率高，因为这时 B 表中如果对 cc 列进行了索引，那么 IN 子查询的效率就会比较高。

同样，如果表 A 比表 B 小，那么使用 EXISTS 子查询效率会更高，因为我们可以使用到 A 表中对 cc 列的索引，而不用从 B 中进行 cc 列的查询。

#### ANY、ALL ####

当我们使用ANY或ALL的时候，一定要使用比较操作符

### 将子查询作为计算字段 ###

### 总结 ###

在子查询使用到的EXIST、IN、ANY和SOME等关键字。在某些情况下使用EXIST和IN可以得到相同的效果，具体使用哪个执行效率更高，则需要看字段的索引情况以及表A和表B哪个表更大。同样，IN、ANY、ALL、SOME这些关键字是用于集合比较的，SOME是ANY的别名，当使用ANY和ALL的时候，一定要用比较操作符。

SQL中，子查询的使用大大增强了SELECT查询的能力，因为很多时候查询需要从结果集中获取数据，或者需要从同一个表中先计算得到一个数据结果，然后与这个数据结果（可能是某个标量，也可能是某个集合）进行比较。

IN表是外边和内表进行hash连接，是先执行子查询。
EXISTS是对外表进行循环，然后在内表进行查询。
因此如果外表数据量大，则用IN，如果外表数据量小，也用EXISTS。
IN有一个缺陷是不能判断NULL，因此如果字段存在NULL值，则会出现返回，因为最好使用NOT EXISTS。

	SELECT player_id, team_id, player_name FROM player WHERE player_id IN (SELECT player_id FROM player_score 
	WHERE score > 20)

## 10 | 常用的SQL标准有哪些，在SQL92中是如何使用连接的？ ##

关系型数据库的核心之一就是连接

### 常用的SQL标准有哪些 ###

### 在SQL92中是如何使用连接的 ###

笛卡尔积、等值连接、非等值连接、外连接（左连接、右连接）和自连接

#### 笛卡尔积 ####

笛卡尔乘积是一个数学运算。假设我有两个集合 X 和 Y，那么 X 和 Y 的笛卡尔积就是 X 和 Y 的所有可能组合，也就是第一个对象来自于 X，第二个对象来自于 Y 的所有可能。


	SELECT * FROM player
	SELECT * FROM team
	SQL: SELECT * FROM player, team

#### 等值连接 ####

	SELECT player_id, player.team_id, player_name, height, team_name FROM player, team WHERE player.team_id = team.team_id


	SELECT player_id, a.team_id, player_name, height, team_name FROM player AS a, team AS b WHERE a.team_id = b.team_id

如果我们使用了表的别名，在查询字段中就只能使用别名进行代替，不能使用原有的表名

#### 非等值连接 ####

	SQL：SELECT p.player_name, p.height, h.height_level
	FROM player AS p, height_grades AS h
	WHERE p.height BETWEEN h.height_lowest AND h.height_highest

区分player的身高等级

#### 外连接 ####

左外连接，就是指左边的表是主表，需要显示左边表的全部行，而右侧的表是从表，（+）表示哪个是从表。

右外连接，指的就是右边的表是主表，需要显示右边表的全部行，而左侧的表是从表。

需要注意的是，LEFT JOIN 和 RIGHT JOIN 只存在于 SQL99 及以后的标准中，在 SQL92 中不存在，只能用（+）表示。

	SQL：SELECT * FROM player, team where player.team_id = team.team_id(+)
	=
	SQL：SELECT * FROM player LEFT JOIN team on player.team_id = team.team_id

#### 自连接 ####

SQL：SELECT b.player_name, b.height FROM player as a , player as b WHERE a.player_name = '布雷克-格里芬' and a.height < b.height

## 20 | 当我们思考数据库调优的时候，都有哪些维度可以选择？ ##

今天的课程你需要掌握以下几个方面的内容：

1. 数据库调优的目标是什么？
2. 如果要进行调优，都有哪些维度可以选择？
3. 如何思考和分析数据库调优这件事？

### 数据库调优的目标 ###

数据库调优的目的就是要让数据库运行得更快，也就是说响应的时间更快，吞吐量更大。

#### 用户的反馈 ####

#### 日志分析 ####

通过查看数据库日志和操作系统日志等方式找出异常情况，通过它们来定位遇到的问题。

#### 服务器资源使用监控 ####



#### 数据库内部状况监控 ####

### 对数据库进行调优，都有哪些维度可以进行选择？ ###

#### 第一步，选择适合的DBMS ####

SQL 阵营和 NoSQL 阵营

NoSQL 阵营包括键值型数据库、文档型数据库、搜索引擎、列式存储和图形数据库。

#### 第二步，优化表设计 ####

RDBMS 中，每个对象都可以定义为一张表，表与表之间的关系代表了对象之间的关系。如果用的是 MySQL，我们还可以根据不同表的使用需求，选择不同的存储引擎。

1. 表结构要尽量遵循第三范式的原则。这样可以让数据结构更加清晰规范，减少冗余字段，同时也减少了在更新，插入和删除数据时等异常情况的发生。
2. 如果分析查询应用比较多，尤其是需要进行多表联查的时候，可以采用反范式进行优化。反范式采用空间换时间的方式，通过增加冗余字段提高查询的效率。
3. 表字段的数据类型选择，关系到了查询效率的高低以及存储空间的大小。一般来说，如果字段可以采用数值类型就不要采用字符类型；字符长度要尽可能设计得短一些。针对字符类型来说，当确定字符长度固定时，就可以采用 CHAR 类型；当长度不固定时，通常采用 VARCHAR 类型。

#### 第三步，优化逻辑查询 ####

SQL 查询优化，可以分为逻辑查询优化和物理查询优化。逻辑查询优化就是通过改变 SQL 语句的内容让 SQL 执行效率更高效，采用的方式是对 SQL 语句进行等价变换，对查询进行重写。重写查询的数学基础就是关系代数。

SQL 的查询重写包括了子查询优化、等价谓词重写、视图重写、条件简化、连接消除和嵌套连接消除等。

#### 第四步，优化物理查询 ####

物理查询优化是将逻辑查询的内容变成可以被执行的物理操作符，从而为后续执行器的执行提供准备。它的核心是高效地建立索引，并通过这些索引来做各种优化。

1. 如果数据重复度高，就不需要创建索引。通常在重复度超过 10% 的情况下，可以不创建这个字段的索引。比如性别这个字段（取值为男和女）。
2. 要注意索引列的位置对索引使用的影响。比如我们在 WHERE 子句中对索引字段进行了表达式的计算，会造成这个字段的索引失效。
3. 要注意联合索引对索引使用的影响。我们在创建联合索引的时候会对多个字段创建索引，这时索引的顺序就很重要了。比如我们对字段 x, y, z 创建了索引，那么顺序是 (x,y,z) 还是 (z,y,x)，在执行的时候就会存在差别。
4. 要注意多个索引对索引使用的影响。索引不是越多越好，因为每个索引都需要存储空间，索引多也就意味着需要更多的存储空间。此外，过多的索引也会导致优化器在进行评估的时候增加了筛选出索引的计算时间，影响评估的效率。

查询优化器在对 SQL 语句进行等价变换之后，还需要根据数据表的索引情况和数据情况确定访问路径，这就决定了执行 SQL 时所需要消耗的资源。SQL 查询时需要对不同的数据表进行查询，因此在物理查询优化阶段也需要确定这些查询所采用的路径，具体的情况包括：

1. 单表扫描：对于单表扫描来说，我们可以全表扫描所有的数据，也可以局部扫描。
2. 两张表的连接：常用的连接方式包括了嵌套循环连接、HASH 连接和合并连接。
3. 多张表的连接：多张数据表进行连接的时候，顺序很重要，因为不同的连接路径查询的效率不同，搜索空间也会不同。我们在进行多表连接的时候，搜索空间可能会达到很高的数据量级，巨大的搜索空间显然会占用更多的资源，因此我们需要通过调整连接顺序，将搜索空间调整在一个可接收的范围内。

物理查询优化是在确定了逻辑查询优化之后，采用物理优化技术（比如索引等），通过计算代价模型对各种可能的访问路径进行估算，从而找到执行方式中代价最小的作为执行计划。在这个部分中，我们需要掌握的重点是对索引的创建和使用。

#### 第五步，使用 Redis 或 Memcached 作为缓存 ####

#### 第六步，库级优化 ####

### 我们该如何思考和分析数据库调优这件事 ###

1. **首先，选择比努力更重要。**不同的DBMS直接影响了后续的SQL查询语句
2. **另外，你可以把 SQL 查询优化分成两个部分，逻辑查询优化和物理查询优化。** 逻辑查询优化就是通过 SQL 等价变换提升查询效率，换一种查询写法执行效率可能更高。物理查询优化则是通过索引和表连接方式等技术来进行优化（重点掌握）。
3. **最后，我们可以通过外援来增强数据库的性能。**
	* 单一的数据库总会遇到各种限制，不如取长补短，利用外援的方式。
	* 通过对数据库进行垂直或者水平切分，突破单一数据库或数据表的访问限制，提升查询的性能。

### 精选留言 ###

请问比较好用的业务应用服务器资源监控工具有哪些？（Zabbix, OpManager）

## 21 | 范式设计：数据表的范式有哪些，3NF指的是什么？ ##

## 22 | 反范式设计：3NF有什么不足，为什么有时候需要反范式设计？ ##

## 23 | 索引的概览：用还是不用索引，这是个问题 ##

## 24 | 索引的原理：我们为什么用B+树来做索引？ ##

## 25 | Hash索引的底层原理是什么？ ##

## 26 | 索引的使用原则：如何通过索引让SQL查询效率最大化？ ##

提升SQL的查询效率，如何通过索引让效率最大化

1. 什么情况下使用索引？当进行数据表查询的时候，都有哪些特征需要创建索引？
2. 索引不是万能的，索引涉及的不合理可能会阻碍数据库和业务处理的性能。什么情况下不需要创建索引？
3. 创建了索引不一定代表用得上，甚至有些情况下索引会失效。哪些情况下，索引会失效呢？又该如何避免这一情况？

### 创建索引有哪些规律？ ###

#### 1. 字段的数值有唯一性的限制，比如用户名 ####

索引本身可以起到约束的作用，比如唯一索引、主键索引都是可以起到唯一性约束的，因此在我们的数据表中，如果某个字段是唯一性的，就可以直接创建唯一性索引，或者主键索引。

#### 2. 频繁作为 WHERE 查询条件的字段，尤其在数据表大的情况下 ####

在数据量大的情况下，某个字段在 SQL 查询的 WHERE 条件中经常被使用到，那么就需要给这个字段创建索引了。创建普通索引就可以大幅提升数据查询的效率。

#### 3. 需要经常 GROUP BY 和 ORDER BY 的列 ####

索引就是让数据按照某种顺序进行存储和检索，因此当我们使用 GROUP BY 对数据进行分组查询，或者使用 ORDER BY 对数据进行排序的时候，就需要对分组或者排序的字段进行索引。

#### 4. UPDATE、DELETE的WHERE条件列，一般也需要创建索引 ####

对数据按照某个条件进行查询后再进行 UPDATE 或 DELETE 的操作，如果对 WHERE 字段创建了索引，就能大幅提升效率。原理是因为我们需要先根据 WHERE 条件列检索出来这条记录，然后再对它进行更新或删除。如果进行更新的时候，更新的字段是非索引字段，提升的效率会更明显，这是因为非索引字段更新不需要对索引进行维护。

#### 5. DISTINCT 字段需要创建索引 ####