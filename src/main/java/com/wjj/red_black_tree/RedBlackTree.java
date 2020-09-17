package com.wjj.red_black_tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.wjj.red_black_tree.RedBlackNode.COLOR;

/**
 * 红黑树
 *
 * @param <T> 泛型类型
 * @author Aldebran
 * @since 17/19/2020
 */
public class RedBlackTree<T> {

    public RedBlackNode<T> root;

    private Comparator<T> comparator;

    /**
     * 指定比较器
     *
     * @param comparator 比较器
     */
    public RedBlackTree(final Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public RedBlackTree() {

    }

    // 获取节点颜色
    public static COLOR getColor(final RedBlackNode redBlackNode) {
        if (redBlackNode == null) {
            return COLOR.BLACK;
        } else {
            return redBlackNode.color;
        }
    }

    // 比较元素大小
    private int compare(final T v1, final T v2) {
        if (comparator != null) {
            return comparator.compare(v1, v2);
        } else if (v1 instanceof Comparable && v2 instanceof Comparable) {
            Comparable comparable1 = (Comparable) v1;
            return comparable1.compareTo(v2);
        } else {
            throw new RuntimeException("fail to compare values! ");
        }
    }

    /**
     * 设置父子关系
     *
     * @param parent 父亲节点
     * @param child  子节点
     * @param left   是否为左孩子
     */
    private void setRelationship(RedBlackNode<T> parent, RedBlackNode<T> child, boolean left) {
        if (child != null) {
            child.parent = parent;
        }
        if (parent != null) {
            if (left) {
                parent.left = child;
            } else {
                parent.right = child;
            }
        }
    }

    /**
     * 根据key取得value，需要指定比较器比较key
     *
     * @param elem 包含key的elem
     * @return value
     */
    public T get(final T elem) {
        RedBlackNode<T> current = root;
        while (current != null) {
            int cmpValue = compare(elem, current.elem);
            if (cmpValue == 0) {
                return current.elem;
            } else {
                if (cmpValue < 0) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            }
        }
        return null;
    }


    /**
     * 爷爷的左孩子1的左孩子2是当前节点，且1,2都是红色，叔叔是黑色，执行右单旋（爷爷）+换颜色
     *
     * @param current     当前节点
     * @param grandParent 爷爷
     * @param parent      父亲
     * @return 新的当前节点
     */
    private RedBlackNode<T> LR_LR_UB(RedBlackNode<T> current, RedBlackNode<T> grandParent, RedBlackNode<T> parent) {
        // 右单旋
        RedBlackNode<T> parentOriginRight = parent.right;
        RedBlackNode<T> grandParentOriginParent = grandParent.parent;
        setRelationship(parent, grandParent, false);
        setRelationship(grandParent, parentOriginRight, true);
        boolean originLeft = grandParentOriginParent == null ? false :
                (grandParentOriginParent.left == grandParent);
        setRelationship(grandParentOriginParent, parent, originLeft);
        // 修改颜色
        parent.color = COLOR.BLACK;
        grandParent.color = COLOR.RED;
        if (grandParentOriginParent == null) {
            root = parent;
        }
        return null;
    }

    /**
     * 爷爷的右孩子1的右孩子2是当前节点，且1,2都是红色，叔叔是黑色，执行右单旋（爷爷）+换颜色
     * 与LR_LR_UB镜像对称
     *
     * @param current     当前节点
     * @param grandParent 爷爷
     * @param parent      父亲
     * @return 新的当前节点
     */
    private RedBlackNode<T> RR_RR_UB(RedBlackNode<T> current, RedBlackNode<T> grandParent, RedBlackNode<T> parent) {
        // 左单旋
        RedBlackNode<T> parentOriginLeft = parent.left;
        RedBlackNode<T> grandParentOriginParent = grandParent.parent;
        setRelationship(parent, grandParent, true);
        setRelationship(grandParent, parentOriginLeft, false);
        boolean originLeft = grandParentOriginParent == null ? false :
                (grandParentOriginParent.left == grandParent);
        setRelationship(grandParentOriginParent, parent, originLeft);
        // 修改颜色
        parent.color = COLOR.BLACK;
        grandParent.color = COLOR.RED;
        if (grandParentOriginParent == null) {
            root = parent;
        }
        return null;
    }


    /**
     * 爷爷的孩子1的孩子2是当前节点，且1,2都是红色，叔叔是红色，换颜色
     *
     * @param current     当前节点
     * @param grandParent 爷爷
     * @param parent      父亲
     * @return 新的当前节点
     */
    private RedBlackNode<T> UR(RedBlackNode<T> current, RedBlackNode<T> grandParent, RedBlackNode<T> parent) {
        grandParent.left.color = grandParent.right.color = COLOR.BLACK;
        grandParent.color = COLOR.RED;
        return grandParent;
    }


    /**
     * 爷爷的左孩子1的右孩子2是当前节点，且1,2都是红色，叔叔是黑色，执行左单旋（父亲）+右单旋（爷爷）
     *
     * @param current     当前节点
     * @param grandParent 爷爷
     * @param parent      父亲
     * @return 新的当前节点
     */
    private RedBlackNode<T> LR_RR_UB(RedBlackNode<T> current, RedBlackNode<T> grandParent, RedBlackNode<T> parent) {
        // 一步到位，不再分成2次旋转
        RedBlackNode<T> originCurrentLeft = current.left;
        RedBlackNode<T> originCurrentRight = current.right;
        RedBlackNode<T> originGrandParentParent = grandParent.parent;
        setRelationship(current, parent, true);
        setRelationship(current, grandParent, false);
        setRelationship(parent, originCurrentLeft, false);
        setRelationship(grandParent, originCurrentRight, true);
        boolean originLeft = originGrandParentParent == null ? false :
                (originGrandParentParent.left == grandParent);
        setRelationship(originGrandParentParent, current, originLeft);
        // 改变颜色
        grandParent.color = COLOR.RED;
        parent.color = COLOR.RED;
        current.color = COLOR.BLACK;
        if (originGrandParentParent == null) {
            root = current;
        }
        return null;
    }

    /**
     * 爷爷的右孩子1的左孩子2是当前节点，且1,2都是红色，叔叔是黑色，执行右单旋（父亲）+左单旋（爷爷）
     * 与LR_RR_UB镜像对称
     *
     * @param current     当前节点
     * @param grandParent 爷爷
     * @param parent      父亲
     * @return 新的当前节点
     */
    private RedBlackNode<T> RR_LR_UB(RedBlackNode<T> current, RedBlackNode<T> grandParent, RedBlackNode<T> parent) {
        RedBlackNode<T> originCurrentRight = current.right;
        RedBlackNode<T> originCurrentLeft = current.left;
        RedBlackNode<T> originGrandParentParent = grandParent.parent;
        setRelationship(current, parent, false);
        setRelationship(current, grandParent, true);
        setRelationship(parent, originCurrentRight, true);
        setRelationship(grandParent, originCurrentLeft, false);
        boolean originLeft = originGrandParentParent == null ? false :
                (originGrandParentParent.left == grandParent);
        setRelationship(originGrandParentParent, current, originLeft);

        // 改变颜色
        grandParent.color = COLOR.RED;
        parent.color = COLOR.RED;
        current.color = COLOR.BLACK;
        if (originGrandParentParent == null) {
            root = current;
        }
        return null;
    }

    public void insert(final T elem) {
        // 新节点
        RedBlackNode<T> newNode = new RedBlackNode<>(elem, COLOR.RED);
        if (root == null) {
            root = newNode;
            root.color = COLOR.BLACK;
            return;
        }
        // 当前节点
        RedBlackNode<T> current = root;
        // 缓存比较结果
        int cmpValue = 0;
        // 按照二叉搜索树方式插入到末端
        while (true) {
            cmpValue = compare(elem, current.elem);
            if (cmpValue == 0) {
                return;
            } else {
                if (cmpValue < 0) {
                    if (current.left == null) {
                        current.left = newNode;
                        newNode.parent = current;
                        current = newNode;
                        break;
                    } else {
                        current = current.left;
                    }
                } else {
                    if (current.right == null) {
                        current.right = newNode;
                        newNode.parent = current;
                        current = newNode;
                        break;
                    } else {
                        current = current.right;
                    }
                }
            }
        }
        // 向上调整
        while (current != null) {
            // 父亲
            RedBlackNode<T> parent = current.parent;
            // 爷爷
            RedBlackNode<T> grandParent = parent == null ? null : parent.parent;
            // 叔叔
            RedBlackNode<T> uncle = grandParent == null ? null :
                    (grandParent.left == parent ? grandParent.right : grandParent.left);
            if (parent == null || grandParent == null) {
                // 不再需要调整
                break;
            }
            if (getColor(parent) == COLOR.RED) {
                if (getColor(uncle) == COLOR.RED) {
                    current = UR(current, grandParent, parent);
                } else {
                    // 父亲红色，叔叔是黑色
                    if (parent == grandParent.left) {
                        // 父亲是左孩子
                        if (current == parent.left) {
                            // 当前节点也是左孩子
                            current = LR_LR_UB(current, grandParent, parent);
                        } else {
                            // 当前节点是右孩子
                            current = LR_RR_UB(current, grandParent, parent);
                        }
                    } else {
                        // 父亲是右孩子
                        if (current == parent.right) {
                            // 当前节点是右孩子
                            current = RR_RR_UB(current, grandParent, parent);
                        } else {
                            // 当前节点是左孩子
                            current = RR_LR_UB(current, grandParent, parent);
                        }
                    }
                }
            } else {
                // 父亲是黑色，不影响平衡性
                break;
            }
        }
    }

    /**
     * 按照BFS方式打印
     *
     * @return 字符串
     */
    public String printBFS() {
        List<RedBlackNode<T>> nodes = new ArrayList<>();
        if (root != null) {
            nodes.add(root);
        }
        StringBuilder sb = new StringBuilder();
        while (!nodes.isEmpty()) {
            List<RedBlackNode<T>> copyNodes = nodes;
            nodes = new ArrayList<>();
            for (RedBlackNode<T> node : copyNodes) {
                sb.append(node);
                if (node.left != null) {
                    nodes.add(node.left);
                }
                if (node.right != null) {
                    nodes.add(node.right);
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
