package rpg.simple;

import acm.program.ConsoleProgram;
import acm.util.RandomGenerator;

public class SimpleRPG extends ConsoleProgram {
    private String playerName;          // 玩家名字
    private String playerRole;          // 玩家角色
    private int playerMaxHp;            // 玩家最大血量
    private int playerCurrHp;           // 玩家当前血量
    private int playerMaxAttack;        // 玩家攻击力最大值
    private int playerMinAttack;        // 玩家攻击力最小值
    private int playerLevel;            // 玩家等级
    private int playerXp;               // 玩家经验值
    private int playerPotionsLeft;      // 玩家剩余回血药数量

    private String enemyName;           // 敌人名字
    private String enemyRole;           // 敌人角色
    private int enemyCurrHp;            // 敌人当前血量
    private int enemyMaxHp;             // 敌人最大血量
    private int enemyMaxAttack;         // 敌人攻击力最大值
    private int enemyMinAttack;         // 敌人攻击力最小值
    private int enemyLevel;             // 敌人等级

    private final RandomGenerator randomGenerator = RandomGenerator.getInstance();

    private static final String[] ENEMY_NAMES = {"甲", "乙", "丙"};           // 敌人名字
    private static final String[] ENEMY_ROLES = {"路人", "步兵", "盗贼"};     // 敌人角色

    public void run() {
        setupPlayer();

        playScene("一座老宅");
        if (!isPlayerDead()) {
            playScene("一口枯井");
        }

        if (!isPlayerDead()) {
            println("恭喜你！通关了！");
        }
    }

    /**
     * 请玩家选择名字和角色，并初始化玩家的各种属性
     */
    private void setupPlayer() {
        print("请输入你的名字：");
        playerName = readLine();

        playerRole = choose("请选择角色", "战士", "游侠", "术士");
        println("");

        playerLevel = 1;                // 初始等级是1
        playerXp = 0;                   // 初始经验是0
        playerPotionsLeft = 3;          // 游戏开始时赠送3瓶血药

        // 不同角色的基础血量、攻击力
        int baseHp = 0;
        int baseAttack = 0;
        switch (playerRole) {
            case "战士":
                baseHp = 16;
                baseAttack = 10;
                break;
            case "游侠":
                baseHp = 13;
                baseAttack = 13;
                break;
            case "术士":
                baseHp = 12;
                baseAttack = 14;
                break;
        }

        // 最大血量是角色基础血量加上1-6之间的一个随机数，看运气吧
        playerMaxHp = randomGenerator.nextInt(1, 6) + baseHp;
        playerCurrHp = playerMaxHp;

        // 最大攻击力角色基础攻击力加上1-6之间的一个随机数，也看运气吧
        playerMaxAttack = randomGenerator.nextInt(1, 6) + baseAttack;
        // 攻击力下限是上限减3
        playerMinAttack = playerMaxAttack - 3;
    }

    /**
     * 进入/触发一个新的游戏场景
     *
     * @param scene 游戏场景名称
     */
    private void playScene(String scene) {
        println(playerName + "来到了" + scene + "...");

        // 每个场景随机产生1-3个敌人
        int enemyCount = randomGenerator.nextInt(1, 3);
        for (int i = 0; i < enemyCount; i++) {
            if (isPlayerDead()) {
                println("胜败乃兵家常事，大侠请重新来过。");
                break;
            }
            generateRandomEnemy();
            println();
            println("你遇到了" + enemyRole + enemyName + "。");
            battle();
        }
    }

    /**
     * 玩家和敌人对战
     */
    private void battle() {
        printPlayer();
        printEnemy();

        // 回合制循环进行，直至某一方阵亡
        while (true) {
            println();

            // 每一回合都首先从玩家开始行动
            String userChoice = choose("请选择你的行动", "攻击", "逃跑", "补血", "查看状态");
            if (userChoice.equals("查看状态")) {
                printPlayerStatus();
                continue;
            } else if (userChoice.equals("补血")) {
                useHealthPotion();
                printPlayerStatus();
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
                attackEnemy();
                printEnemyStatus();
            }

            if (isEnemyDead()) {
                // 如果敌人阵亡，玩家经验值提升
                println(String.format("你杀死了%s。\n", enemyName));
                println("你获得了" + gainXp() + "点经验值。");
                checkLevelUp();
                println("你当前拥有" + playerXp + "点经验值。");
                printPlayer();
                break;
            } else {
                // 没阵亡则轮到敌人行动
                attackPlayer();
                printPlayerStatus();

                // 如果敌人将玩家打死，游戏结束
                if (isPlayerDead()) {
                    println("你挂了！");
                    break;
                }
            }
        }
    }

    /**
     * 检查玩家是否该升级
     */
    private void checkLevelUp() {
        // 每次升级所需要的经验值=当前级数*40
        if (playerXp >= playerLevel * 40) {
            playerXp -= (playerLevel * 40);
            playerLevel++;

            // 计算升级所获得的属性成长
            int hpMaxIncrease = 0;
            int attackMaxIncrease = 0;
            switch (playerRole) {
                case "战士":
                    hpMaxIncrease = 12;
                    attackMaxIncrease = 7;
                    break;
                case "游侠":
                    hpMaxIncrease = 10;
                    attackMaxIncrease = 19;
                    break;
                case "术士":
                    hpMaxIncrease = 8;
                    attackMaxIncrease = 11;
                    break;

            }

            // 调整攻击力和血量
            playerMaxHp += randomGenerator.nextInt(0, hpMaxIncrease);
            playerMaxAttack += randomGenerator.nextInt(0, attackMaxIncrease);
            playerMinAttack = playerMaxAttack - 3;
            playerCurrHp = playerMaxHp;
            println("你升级了！血量恢复满格！");
        }
    }

    /**
     * 玩家攻击敌人
     */
    private void attackEnemy() {
        // 对敌人造成的真实伤害是玩家攻击力下限和上限之间的一个随机数
        int damage = randomGenerator.nextInt(playerMinAttack, playerMaxAttack);
        enemyCurrHp -= damage;
        println(String.format("%s对%s造成%d点伤害。", playerName, enemyName, damage));
    }

    /**
     * 敌人攻击玩家
     */
    private void attackPlayer() {
        // 敌人对玩家造成的真实伤害是敌人攻击力下限和上限之间的一个随机数
        int damage = randomGenerator.nextInt(enemyMinAttack, enemyMaxAttack);
        playerCurrHp -= damage;
        println(String.format("%s对%s造成%d点伤害。", enemyName, playerName, damage));
    }

    /**
     * 使用补血药剂，可以补血25。
     */
    private void useHealthPotion() {
        if (playerPotionsLeft > 0) {
            int hpIncreased = 25;
            if (hpIncreased > (playerMaxHp - playerCurrHp)) {
                hpIncreased = playerMaxHp - playerCurrHp;
            }
            playerCurrHp += hpIncreased;
            playerPotionsLeft--;
            println(String.format("成功回血%d。当前血量%d, 还剩%d瓶回血药。", hpIncreased, playerCurrHp, playerPotionsLeft));
        } else {
            println("回血失败：回血药已用完。");
        }
    }

    /**
     * 打印敌人状态：名字，当前血量
     */
    private void printEnemyStatus() {
        String message = String.format("%s当前血量%d/%d。", enemyName, enemyCurrHp, enemyMaxHp);
        println(message);
    }

    /**
     * 打印玩家状态：名字，当前血量
     */
    private void printPlayerStatus() {
        String message = String.format("%s当前血量%d/%d。", playerName, playerCurrHp, playerMaxHp);
        println(message);
    }

    /**
     * 打印敌人信息
     */
    private void printEnemy() {
        String message = String.format("『%s』等级为%d，当前有血量%d，攻击力是%d-%d。",
                enemyName, enemyLevel, enemyCurrHp, enemyMinAttack, enemyMaxAttack);
        println(message);
    }

    /**
     * 打印玩家信息
     */
    private void printPlayer() {
        String message = String.format("『%s』等级为%d，当前有血量%d，攻击力是%d-%d。",
                playerName, playerLevel, playerCurrHp, playerMinAttack, playerMaxAttack);
        println(message);
    }

    /**
     * 随机产生一个敌人，并设置好该敌人的各种属性
     */
    private void generateRandomEnemy() {
        int index;  // 用于储存数组下标

        // 随机选择一个名字
        index = randomGenerator.nextInt(0, ENEMY_NAMES.length - 1);
        enemyName = ENEMY_NAMES[index];

        // 随机选择一个角色
        index = randomGenerator.nextInt(0, ENEMY_ROLES.length - 1);
        enemyRole = ENEMY_ROLES[index];

        // 该敌人的各种属性
        int baseHp;                 // 基础血量
        int baseAttack;             // 基础攻击力
        int hpMaxIncrease;          // 每升一级，血量最大增加多少
        int attackMaxIncrease;      // 每升一级，攻击力最大增加多少

        // 不同角色有不同的基础属性和成长属性
        switch (enemyRole) {
            case "路人":
                baseHp = 16;
                baseAttack = 10;
                hpMaxIncrease = 12;
                attackMaxIncrease = 7;
                break;
            case "步兵":
                baseHp = 13;
                baseAttack = 13;
                hpMaxIncrease = 10;
                attackMaxIncrease = 9;
                break;
            case "盗贼":
                baseHp = 12;
                baseAttack = 14;
                hpMaxIncrease = 8;
                attackMaxIncrease = 11;
                break;
            default:
                baseHp = 0;
                baseAttack = 0;
                hpMaxIncrease = 0;
                attackMaxIncrease = 0;
                break;
        }

        // 随机生成该敌人1级时的属性
        enemyMaxHp = (randomGenerator.nextInt(1, 6) + baseHp);
        enemyMaxAttack = (randomGenerator.nextInt(1, 6) + baseAttack);
        enemyMinAttack = (enemyMaxAttack - 3);

        // 让该敌人升到跟玩家同一等级，以保持游戏的平衡性
        for (enemyLevel = 1; enemyLevel < playerLevel; enemyLevel++) {
            enemyMaxHp += randomGenerator.nextInt(0, hpMaxIncrease);
            enemyMaxAttack += randomGenerator.nextInt(0, attackMaxIncrease);;
        }

        // 将该敌人设置为满血
        enemyCurrHp = enemyMaxHp;
    }

    /**
     * 判断玩家是否已经死亡
     *
     * @return 死亡返回true，未死亡返回false
     */
    private boolean isPlayerDead() {
        return playerCurrHp <= 0;
    }

    /**
     * 判断敌人是否已经死亡
     *
     * @return 死亡返回true，未死亡返回false
     */
    private boolean isEnemyDead() {
        return enemyCurrHp <= 0;
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

    /**
     * 杀死敌人后，玩家可以获得经验值
     *
     * @return 获得的经验值
     */
    public int gainXp() {
        int baseXp = 20;                                            // 杀死敌人至少可以获得20经验值
        int randomXp = randomGenerator.nextInt(0, 40);  // 随机获得的额外经验值
        int gainedXp = enemyLevel * (baseXp + randomXp);            // 乘以敌人等级系数，即为最终获得的经验值
        playerXp += gainedXp;
        return gainedXp;
    }
}
