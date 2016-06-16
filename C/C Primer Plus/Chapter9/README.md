# 第9章 函数 #
## 9.1 复习函数 ##


### 9.1.4 定义带形式参数的函数 ###

## 9.5 查找地址：&运算符 ##
指针(pointer)是C语言最重要的，用于存储变量的地址。

### 9.7.1 间接运算符：* ###
地址运算符：&

一般注解：
后跟一个变量名时，&给出该变量的地址

地址运算符：*

一般注解：
后跟一个指针名或地址时，*给出储存在指针指向地址上的值。

示例：

	nurse = 22;
	ptr = &nurse; //指向nurse的指针
	val = *ptr; //把ptr指向的地址上的值赋给val
执行以上3条语句的最终结果是把22赋给val

### 9.7.3 使用指针在函数间通信 ###