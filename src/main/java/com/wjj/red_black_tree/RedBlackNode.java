package com.wjj.red_black_tree;

/**
 * 红黑树节点定义
 *
 * @param <T> 泛型类型
 * @author Aldebran
 * @since 17/09/2020
 */
public class RedBlackNode<T> {

    // 颜色枚举
    public enum COLOR {
        RED, BLACK
    }

    // 元素值
    public T elem;

    // 颜色
    public COLOR color;

    // 左孩子
    public RedBlackNode<T> left;

    // 右孩子
    public RedBlackNode<T> right;

    // 父亲
    public RedBlackNode<T> parent;

    public RedBlackNode() {
    }

    public RedBlackNode(T elem, COLOR color) {
        this.elem = elem;
        this.color = color;
    }

}
