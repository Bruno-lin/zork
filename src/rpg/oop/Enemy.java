package rpg.oop;

import acm.util.RandomGenerator;

import static rpg.oop.RPG.randomGenerator;

public class Enemy extends Creature {

    public static final RandomGenerator randomGenerator = RandomGenerator.getInstance();

    private static final String[] ENEMY_NAMES = {"甲", "乙", "丙"};           // 敌人名字
    private static final String[] ENEMY_ROLES = {"路人", "步兵", "盗贼"};     // 敌人角色

    private Enemy(String name, String role, int level) {
        super(name, role, level);
    }

    /**
     * 创建Enemy
     */
    public static Enemy createEnemy(int level) {
        int index;  // 用于储存数组下标

        // 随机选择一个名字
        index = randomGenerator.nextInt(0, ENEMY_NAMES.length - 1);
        String name = ENEMY_NAMES[index];

        // 随机选择一个角色
        index = randomGenerator.nextInt(0, ENEMY_ROLES.length - 1);
        String role = ENEMY_ROLES[index];

        return new Enemy(name, role, level);
    }


    @Override
    public void initialize() {
        // 该敌人的各种属性
        int baseHp;                 // 基础血量
        int baseAttack;             // 基础攻击力
        int hpMaxIncrease;          // 每升一级，血量最大增加多少
        int attackMaxIncrease;      // 每升一级，攻击力最大增加多少

        // 不同角色有不同的基础属性和成长属性
        switch (role) {
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
        maxHp = (randomGenerator.nextInt(1, 6) + baseHp);
        maxAtt = (randomGenerator.nextInt(1, 6) + baseAttack);
        minAtt = (maxAtt - 3);

        // 让该敌人升到跟玩家同一等级，以保持游戏的平衡性
        for (int i = 1; i < level; i++) {
            maxHp += randomGenerator.nextInt(0, hpMaxIncrease);
            maxAtt += randomGenerator.nextInt(0, attackMaxIncrease);
        }

        // 将该敌人设置为满血
        curHp = maxHp;
    }


//    @Override
//    public String hello() {
//        return "Hello，我是一个敌人，Enemy！";
//    }
}