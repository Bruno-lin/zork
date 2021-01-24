package rpg.oop;

import acm.program.ConsoleProgram;
import acm.util.RandomGenerator;

public class RPG extends ConsoleProgram {

    public static final RandomGenerator randomGenerator = RandomGenerator.getInstance();

    public static Player player;
    public static Enemy enemy;

    public void run() {
        setupPlayer();

        playScene("一座老宅");

        if (!player.isDead()) {
            playScene("一口枯井");
        }

        if (!player.isDead()) {
            println("恭喜你！通关了！");
        }
    }

    public void setupPlayer() {
        print("请输入你的名字：");
        String playerName = readLine();

        String playerRole = choose("请选择角色", "战士", "游侠", "术士");
        println("");

        player = Player.createPlayer(playerName, playerRole);
    }

    /**
     * 提示用户做选择，如果用户的选择无效，就让用户重新输入
     *
     * @param prompt  提示语
     * @param choices 允许的几种选择
     * @return 玩家最终的选择
     */
    public final String choose(String prompt, String... choices) {
        // 将选择用逗号连起来
        String concatenatedChoices = String.join(", ", choices);
        // 最终的提示语
        String actualPrompt = String.format("%s (%s): ", prompt, concatenatedChoices);

        // 如果玩家的输入无效，就提示玩家重新输入
        while (true) {
            print(actualPrompt);
            String userChoice = readLine();

            // 逐个对比，看是否相等
            for (String choice : choices) {
                if (userChoice.equals(choice)) {
                    return choice;
                }
            }
            println("您的选择无效，请重新输入。");
        }
    }

    private void playScene(String scene) {
        println(player.name + "来到了" + scene + "...");

        // 每个场景随机产生1-3个敌人
        int enemyCount = randomGenerator.nextInt(1, 3);
        for (int i = 0; i < enemyCount; i++) {
            if (player.isDead()) {
                println("胜败乃兵家常事，大侠请重新来过。");
                break;
            }
            enemy = Enemy.createEnemy(player.level);
            println();
            println("你遇到了" + enemy.role + enemy.name + "。");
            battle();
        }
    }

    /**
     * 玩家和敌人对战
     */
    public void battle() {
        println(player.printInfo());
        println(enemy.printInfo());

        // 回合制循环进行，直至某一方阵亡
        while (true) {
            println();

            // 每一回合都首先从玩家开始行动
            String userChoice = choose("请选择你的行动", "攻击", "逃跑", "补血", "查看状态");
            if (userChoice.equals("查看状态")) {
                player.printStatus();
                continue;
            } else if (userChoice.equals("补血")) {
                println(player.useHealthPotion());
                println(player.printStatus());
                continue;
            } else if (userChoice.equals("逃跑")) {
                boolean success = randomGenerator.nextBoolean();
                if (success) {
                    println("逃跑成功！");
                    break;
                } else {
                    println("逃跑失败！");
                }
            } else if (userChoice.equals("攻击")) {
                int damage = player.attack(enemy);
                println(player.attackMessage(enemy,damage));
                println(enemy.printStatus());
            }

            if (enemy.isDead()) {
                // 如果敌人阵亡，玩家经验值提升
                println(String.format("你杀死了%s。\n", enemy.name));
                println("你获得了" + player.gainXp(enemy) + "点经验值。");
                if(player.checkLevelUp())// 升级检查
                {
                    println("你升级了！血量恢复满格！");
                }
                println("你当前拥有" + player.xp + "点经验值。");
                println(player.printInfo());
                break;
            } else {
                // 没阵亡则轮到敌人行动
                int damage=enemy.attack(player);
                println(enemy.attackMessage(player,damage));
                println(player.printStatus());

                // 如果敌人将玩家打死，游戏结束
                if (player.isDead()) {
                    println("你挂了！");
                    break;
                }
            }
        }
    }
//        Creature player = new Player();
//        Creature enemy = new Enemy();
//        println(player.hello());             // 输出Hello!
//        println(enemy.hello());              // 输出Hello!
}
