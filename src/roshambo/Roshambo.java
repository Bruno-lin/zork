package roshambo;

import acm.program.ConsoleProgram;
import acm.util.RandomGenerator;

public class Roshambo extends ConsoleProgram {

    public static final String[] SHAPES = {"石头", "剪刀", "布"};

    public void run() {
        RandomGenerator randomGenerator = RandomGenerator.getInstance();
        randomGenerator.setSeed(0);

        while (true) {
            print("这一轮你要出的手势是：");
            String yourShape = readLine();

            // 如果玩家输入的是退出，则结束游戏
            if (yourShape.equals("退出")) {
                break;
            }

            // 给电脑随机生成一个手势
            int index = randomGenerator.nextInt(0, 2);  // 产生一个0-2之间的随机数
            String computerShape = SHAPES[index];                  // 根据此随机数从数组中选择一个手势

            if (yourShape == "石头") {
                if (computerShape == "剪刀") {
                    printWinMessage(yourShape, computerShape);
                } else if (computerShape == "布") {
                    printLoseMessage(yourShape, computerShape);
                } else {
                    printTieMessage(yourShape);
                }
            } else if (yourShape == "剪刀") {
                if (computerShape == "布") {
                    printWinMessage(yourShape, computerShape);
                } else if (computerShape == "石头") {
                    printLoseMessage(yourShape, computerShape);
                } else {
                    printTieMessage(yourShape);
                }
            } else if (yourShape == "布") {
                if (computerShape == "石头") {
                    printWinMessage(yourShape, computerShape);
                } else if (computerShape == "剪刀") {
                    printLoseMessage(yourShape, computerShape);
                } else {
                    printTieMessage(yourShape);
                }
            } else {
                println("你的输入有误，游戏结束！");
                break;
            }
        }
    }

    /**
     * 打印获胜的信息
     * @param yourShape         你出的手势
     * @param computerShape     电脑出的手势
     */
    public void printWinMessage(String yourShape, String computerShape) {
        println("你赢了！" + "你出的是『" + yourShape + "』，电脑出的是『" + computerShape + "』。");
    }

    /**
     * 打印输掉的信息
     * @param yourShape         你出的手势
     * @param computerShape     电脑出的手势
     */
    public void printLoseMessage(String yourShape, String computerShape) {
        println("你输了！" + "你出的是『" + yourShape + "』，电脑出的是『" + computerShape + "』。");
    }

    /**
     * 打印平局信息
     * @param shape             你跟电脑的手势，应该是一样的，所以平局
     */
    public void printTieMessage(String shape) {
        println("平局。你和电脑出的都是『" + shape + "』。");
    }
}
