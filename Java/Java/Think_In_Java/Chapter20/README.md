# 第20章 注解 #

*元数据被定义为：描述数据的数据，对数据及信息资源的描述性信息。
元数据（Metadata）是描述其它数据的数据（data about other data），或者说是用于提供某种资源的有关信息的结构数据（structured data）。元数据是描述信息资源或数据等对象的数据，其使用目的在于：识别资源；评价资源；追踪资源在使用过程中的变化；实现简单高效地管理大量网络化数据；实现信息资源的有效发现、查找、一体化组织和对使用资源的有效管理。 元数据的基本特点主要有：
a）元数据一经建立，便可共享。元数据的结构和完整性依赖于信息资源的价值和使用环境；元数据的开发与利用环境往往是一个变化的分布式环境；任何一种格式都不可能完全满足不同团体的不同需要；
b）元数据首先是一种编码体系。元数据是用来描述数字化信息资源，特别是网络信息资源的编码体系，这导致了元数据和传统数据编码体系的根本区别；元数据的最为重要的特征和功能是为数字化信息资源建立一种机器可理解框架。*

注解（也被称为元数据）为我们在代码中添加信息提供了一种形式化的方法，使我们可以在稍后某个时刻非常方便的使用这些数据。

Java SE内置了三种，定义在java.lang中的注解：

* @Override，表示当前的方法定义将覆盖超类中的方法。如果不小心拼写错误，或者方法签名对不上被覆盖的方法，编译器就会发出错误提示。
* @Deprecated，如果程序员是用了注解为它的元素，那么编译器会发出警告信息。
* @SuppressWarnings，关闭不当的编译器警告信息。

## 20.1 基本语法 ##

@Test该注解本身不作任何事情，但是编译器要确保在其构造路径上必须有Test注解的定义。

	@Test
	void testExecute...

注解@Test可以和任何修饰符共同作用于方法，例如public、static或void。

### 20.1.1 定义注解 ###

@Test 注解的定义看起来很像接口的定义，与其他任何Java接口一样，注解也将会编译成class文件。

### 20.1.2 元注解 ###

Java目前只内置了三种标准注解，以及四种元注解。

|--|--|
|@Target|表示该注解可以用于什么地方。 CONSTRUCTOR:构造器的声明 FIELD：域声明（包括enum实例） LOCAL_VARIABLE：局部变量声明 METHOD：方法声明 PACKAGE：包声明 PARAMETER：参数声明 TYPE：类、接口（包括注解类型）或enum声明|
|@Retemtion|表示需要在什么级别保存该注解信息可选的RetentionPolicy参数包括：SOURCE：注解将被编译器丢弃。 CLASS：注解在class文件中可用，但会被VM丢弃。 RUNTIME：VM将在运行期也保留注解，因此可以通过反射机制读取注解的的信息|
|@Documented|将此注解包含在Javadoc中|
|@Inherited|允许子类继承父类中的注解|

## 20.2 编写注解处理器 ##

使用注解的过程中，很重要的一个部分就是创建与使用 *注解处理器*。

### 20.2.1 注解元素 ###

标签@UseCase由UseCase.java定义，其中包含int元素id，以及一个String元素description

* 所有基本类型（int， float， boolean等）
* String
* Class
* enum
* Annotation
* 以上类型的数组

### 20.2.2 默认值限制 ###

1. 元素不能由不确定的值
2. 元素必须要么具有默认值，要么在使用注解时提供元素的值。

对于非基本类型的元素，在源代码中声明时，或是在注解接口中定义默认值时，都不能以null作为其值。

### 20.2.3 生成外部文件 ###

Web Service、自定义标签库以及对象/关系映射工具，一般需要XML描述文件，而这些描述文件脱离于源代码之外。

假设希望提供一些基本的对象/关系映射功能，能够自动生成数据库表，用以存储JavaBean对象。可以选择使用XML描述文件，指明类的名字、每个成员以及数据库映射的相关信息。然而，如果使用注解的话，可以将搜娱哦的信息保存在JavaBean源文件中。

### 20.2.4 注解不支持继承 ###

不能使用关键字extends来继承某个@interface。

### 20.2.5 实现处理器 ###


## 20.3 使用apt处理注解 ##