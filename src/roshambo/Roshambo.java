package roshambo;

import acm.program.ConsoleProgram;
import acm.util.RandomGenerator;

public class Roshambo extends ConsoleProgram {

    public static final String[] SHAPES = {"石头", "剪刀", "布"};

    public void run() {
        RandomGenerator randomGenerator = RandomGenerator.getInstance();

        while (true) {
            print("请输入你要出的手势（“石头”、“剪刀”、“布”），或输入“退出”结束游戏：");
            String yourShape = readLine();

            // 如果玩家输入的是退出，则结束游戏
            if (yourShape.equals("退出")) {
                break;
            }

            // 给电脑随机生成一个手势
            randomGenerator.setSeed(123);
            int index = randomGenerator.nextInt(0, 2);  // 产生一个0-2之间的随机数
            int index1 = randomGenerator.nextInt(0, 1); // 产生一个0-2之间的随机数
            int index2 = randomGenerator.nextInt(0, 2); // 产生一个0-2之间的随机数
            System.out.println(index+" "+index1+" "+index2);
            String computerShape = SHAPES[index];                  // 根据此随机数从数组中选择一个手势
            String computerShape1 = SHAPES[index1];                  // 根据此随机数从数组中选择一个手势
            String computerShape2 = SHAPES[index2];                  // 根据此随机数从数组中选择一个手势

            if (yourShape.equals("石头")) {
                if (computerShape1.equals("剪刀")) {
                    printWinMessage(yourShape, computerShape);
                } else if (computerShape1.equals("布")) {
                    printLoseMessage(yourShape, computerShape);
                } else {
                    printTieMessage(yourShape);
                }
            } else if (yourShape.equals("剪刀")) {
                if (computerShape.equals("布")) {
                    printWinMessage(yourShape, computerShape);
                } else if (computerShape.equals("石头")) {
                    printLoseMessage(yourShape, computerShape);
                } else {
                    printTieMessage(yourShape);
                }
            } else if (yourShape.equals("布")) {
                if (computerShape2.equals("石头")) {
                    printWinMessage(yourShape, computerShape);
                } else if (computerShape2.equals("剪刀")) {
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
     *
     * @param yourShape     你出的手势
     * @param computerShape 电脑出的手势
     */
    public void printWinMessage(String yourShape, String computerShape) {
        println("你赢了！" + "你出的是『" + yourShape + "』，电脑出的是『" + computerShape + "』。");
    }

    /**
     * 打印输掉的信息
     *
     * @param yourShape     你出的手势
     * @param computerShape 电脑出的手势
     */
    public void printLoseMessage(String yourShape, String computerShape) {
        println("你输了！" + "你出的是『" + yourShape + "』，电脑出的是『" + computerShape + "』。");
    }

    /**
     * 打印平局信息
     *
     * @param shape 你跟电脑的手势，应该是一样的，所以平局
     */
    public void printTieMessage(String shape) {
        println("平局。你和电脑出的都是『" + shape + "』。");
    }
}
