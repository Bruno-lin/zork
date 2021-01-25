package rpg_仙侠传;

import acm.util.RandomGenerator;


public abstract class Creature {

    public static final RandomGenerator randomGenerator = RandomGenerator.getInstance();

    public String name;     // 玩家和敌人都有名字
    public String role;     // 玩家和敌人都有角色
    public int level;       // 玩家和敌人都有等级
    public int maxHp;       // 玩家和敌人都有最大血量
    public int curHp;       // 玩家和敌人都有当前血量
    public int maxAtt;      // 玩家和敌人都有攻击力下限
    public int minAtt;      // 玩家和敌人都有攻击力上限

    public Creature(String name, String role, int level) {
        this.name = name;
        this.role = role;
        this.level = level;
        initialize();
    }

    /**
     * 攻击另一个生物，可以是玩家攻击敌人，也可以是敌人攻击玩家。
     *
     * @param other 被攻击的生物
     * @return 本次攻击造成的伤害值
     */
    public int attack(Creature other) {
        int damage = randomGenerator.nextInt(minAtt, maxAtt);
        other.curHp -= damage;
        return damage;
    }

    /**
     * 攻击信息
     */
    public String attackMessage(Creature creature, int damage)
    {
        return String.format("%s对%s造成%d点伤害。",this.name,creature.name,damage);
    }

    public int getCurHp() {
        return curHp;
    }

    /**
     * 设置当前血量，不能超过该生物的最大血量
     *
     * @param hp 设置为的血量
     */
    public void setCurrHp(int hp) {
        if (hp >= maxHp) {
            curHp = maxHp;
        } else {
            curHp = hp;
        }
    }

    /**
     * 当前所有状态信息
     *
     * @return 包含了所有信息的字符串
     */
    public String toString() {
        return "『" + name + "』" + "是等级为" + level + "的" + role + "，当前有血量" + curHp + "/" + maxHp + "，攻击力是" + minAtt + "-" + maxAtt;
    }

    /**
     * 初始化玩家/敌人，此方法应该在子类中被overridden
     */
    public abstract void initialize();

    /**
     * 判断玩家是否已经死亡
     *
     * @return 死亡返回true，未死亡返回false
     */
    public boolean isDead() {
        return curHp <= 0;
    }

    /**
     * 打印玩家信息
     */
    public String printInfo() {
        return String.format("『%s』是等级为%d的%s，当前有血量%d/%d，攻击力是%d-%d。",
                name, level, role, curHp, maxHp, minAtt, maxAtt);
    }

    /**
     * 打印玩家状态：名字，当前血量
     */
    public String printStatus() {
        return String.format("%s当前血量%d/%d。", name, curHp, maxHp);
    }

    public String hello() {
        return "Hello，我是一个生物，Creature！";
    }

    public String helloWorld() {
        return "Hello, World!";
    }
}