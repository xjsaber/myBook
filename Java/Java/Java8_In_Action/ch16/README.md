# ch16 结论以及Java 的未来 #

## 16.1 回顾Java8的语言特性 ##

### 16.1.1 行为参数化 ###

* 传递一个Lambda表达式，即一段精简的代码片段，比如 apple -> apple.getWeight() > 150
* 传递一个方法引用，该方法引用指向了一个现有的方法，比如这样的代码：Apple::isHeavy

Function<T, R> 、 Predicate<T> 或者 BiFunction<T, U, R> 这样的类型

### 16.1.2 流 ###

Stream API则与之相反，它采用延迟算法将这些操作组成一个流水线，通过单次流遍历，一次性完成所有的操作。对于大型的数据集，这种操作方式要高效得多。

Stream，尤其是它的 parallel 方法能帮助将一个Stream标记为适合进行并行处理。

### 16.1.3 CompleteableFuture ###

Future对于充分利用多核处理能力是非常有益的，因为它允许一个任务在一个新的核上生成一个新的子线程，新生成的任务可以和原来的任务同时
运行。原来的任务需要结果时，它可以通过 get 方法等待 Future 运行结束（生成其计算的结果值）。

* 通过 Stream 你可以对一系列的操作进行流水线，通过 map 、 filter 或者其他类似的方法提供行为参数化，它可有效避免使用迭代器时总是出现模板代码。
* CompletableFuture提供了像 thenCompose 、 thenCombine 、 allOf 这样的操作，对 Future 涉及的通用设计模式提供了函数式编程的细粒度控制，有助于避免使用命令式编程的模板代码。

### 16.1.4 Optional ###

	Optional<T>，应该永远不会发生 NullPointerException 异常
 由静态方法 Optional.empty 表示的缺失值

### 16.1.5 默认方法 ###

	public interface A {
	    default void foo(){
	       System.out.println("Calling A.foo()");
	    }
	}
	 
	public class Clazz implements A {
	}

## 16.2 Java的未来 ##

### 16.2.1 集合 ###
	Double [] a = new Double[]{1.2, 3.4, 5.9};
	=>
	Double[] a = {1.2, 3.4, 5.9};

	Map<String, Integer> map = new HashMap<>();
	map.put("raoul", 23);
	map.put("mario", 40);
	map.put("alan", 53);
	=>
	Map<String, Integer> map = #{"Raoul" -> 23, "Mario" -> 40, "Alan" -> 53};

### 16.2.2 类型系统的改进 ###

声明位置变量（declaration-site variance）和本地变量类型推断（local variable type inference）

#### 1. 声明位置变量 ####

	List<? extends Number> numbers = new ArrayList<Integer>();
	List<Number> numbers = new ArrayList<Integer>(); 类型不兼容

#### 2. 更多的类型推断 ####

### 16.2.3 模式匹配 ###

不推荐使用 switch 

### 16.2.4 更加丰富的泛型形式 ###

#### 1.具化泛型 ####

泛型多态（generic polymorphism）的消除模式（erasure model）

泛型多态的具化模式，或具化泛型

#### 2.泛型中特别为函数类型增加的语法灵活性 ####

	Function<Integer, Integer> square = x -> x * x;

#### 3.原型特化和泛型 ####

### 16.2.5 对不变性的更深层支持 ###

Java8只支持三种类型的值，分别为

* 简单类型值
* 指向对象的引用
* 指向函数的引用

	final int[] arr = {1, 2, 3};
	final List<T> list = new ArrayList<>();
	前者禁止了直接的赋值操作 arr = ... ，不过它并未阻止以 arr[1]=2 这样的方式对数组进行修改；而后者禁止了对列表的赋值操作，但并未禁止以其他方法修改列表中的元素！关键字final 对于简单数据类型的值操作效果很好，不过对于对象引用，它通常只是一种错误的安全感。

### 16.2.6 值类型 ###

1. 为什么编译器不能对Integer和int一视同仁

2. 值对象——无论简单类型还是对象类型都不能包天下

(1) 任何事物，如果不是简单数据类型，就是对象类型，所有的对象类型都继承自 Object
(2) 所有的引用都是指向对象的引用。

对于值类型，默认情况下，硬件对 int 进行比较时会以一个字节接着一个字节逐次的方式进行， == 会以同样的方式一个元素接着一个元素地对两个变量进行比较。
你可以将这看成对浮点比较的覆盖

#### 3.装箱、泛型、值类型——互相交织的问题 ####



## 16.3 写在最后的话 ##

