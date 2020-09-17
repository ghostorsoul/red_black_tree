import com.wjj.red_black_tree.RedBlackTree;
import org.junit.Test;

import java.util.Random;

/**
 * 测试效率
 *
 * @author Aldebran
 * @since 17/09/2020
 */
public class MyTest {

    // 随机数数量
    private static int total = 1000000;

    // 随机种子
    private static int seed = 1;

    @Test
    public void testRBTree() {
        RedBlackTree<Integer> redBlackTree = new RedBlackTree<>();
        Random random = new Random(seed);
        int[] randomNumbers = new int[total];
        for (int i = 0; i < total; i++) {
            randomNumbers[i] = random.nextInt(total);
        }
        // 插入到红黑树
        System.out.println("put elements");
        int count = 0;
        long st = System.currentTimeMillis();
        for (int i = 0; i < total; i++) {
            redBlackTree.insert(randomNumbers[i]);
            count++;
            if (count % (total / 10) == 0) {
                System.out.println((System.currentTimeMillis() - st));
                st = System.currentTimeMillis();
            }
        }
        // 查询
        System.out.println("get elements");
        st = System.currentTimeMillis();
        count = 0;
        for (int i = 0; i < total; i++) {
            Integer j = redBlackTree.get(randomNumbers[i]);
            if (j == null) {
                throw new RuntimeException();
            }
            count++;
            if (count % (total / 10) == 0) {
                System.out.println((System.currentTimeMillis() - st));
                st = System.currentTimeMillis();
            }
        }
        // 一般的查找
        System.out.println("normal find");
        st = System.currentTimeMillis();
        count = 0;
        for (int num : randomNumbers) {
            for (int j = randomNumbers.length - 1; j >= 0; j--) {
                if (randomNumbers[j] == num) {
                    count++;
                    if (count % (total / 10) == 0) {
                        System.out.println((System.currentTimeMillis() - st));
                        st = System.currentTimeMillis();
                        break;
                    }
                }
            }
        }

    }

}
