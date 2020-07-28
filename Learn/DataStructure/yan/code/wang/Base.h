//
// Created by xjsaber on 2020/5/8.
//

#ifndef SRC_BASE_H
#define SRC_BASE_H

#define ElemType int
#define SElemType int
#define QElemType int
#define MAXSIZE 100
#define MAXQSIZE 100
#define MAXLEN 255

// 函数结果状态代码
#define OK 1
#define ERROR 0
#define OVERFLOW -2
// Status是函数返回值类型，其值是函数结果状态代码
typedef int Status ;

typedef struct {
    ElemType data[MAXSIZE];
    int length;
} SqlList;

typedef struct {
    ElemType *data;
    int length;
} SeqList;

typedef struct LNode
{
    ElemType data;
    struct LNode *next;
}LNode, *LinkList;

typedef struct
{
    SElemType *base; //栈底元素
    SElemType *top; //栈顶元素
    int stacksize; //栈可用的最大容量
}SqStack;

typedef struct StackNode
{
    ElemType data;
    struct StackNode *next;
}StackNode, *LinkStack;

typedef struct
{
    QElemType *base;
    int front;
    int rear;
}SqQueue;

typedef struct QNode
{
    QElemType data;
    struct QNode *next;
}QNode, *QueuePtr;

typedef struct
{
    QueuePtr front; //队头指针
    QueuePtr rear; //队尾指针
}LinkQueue;

/**
 * 串的定长顺序存储结构
 */
typedef struct{
    char ch[MAXLEN + 1];
    int length;
} SString;

/**
 * 串的堆式顺序存储结构
 */
typedef struct{
    char ch[MAXLEN + 1];
    int length;
} HString;

#endif //SRC_BASE_H