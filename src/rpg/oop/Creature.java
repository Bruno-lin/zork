package rpg.oop;

import static rpg.oop.RPG.randomGenerator;

public class Creature {

    public String name;     // 玩家和敌人都有名字
    public String role;     // 玩家和敌人都有角色
    public int level;       // 玩家和敌人都有等级
    public int maxHp;       // 玩家和敌人都有最大血量
    public int curHp;       // 玩家和敌人都有当前血量
    public int maxAtt;      // 玩家和敌人都有攻击力下限
    public int minAtt;      // 玩家和敌人都有攻击力上限


    public Creature() {
    }

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
        int att = randomGenerator.nextInt(minAtt, maxAtt);
        other.curHp -= att;
        return att;
    }

    /**
     * 判断自己是否已经死亡
     *
     * @return 如果死亡返回true，否则返回false
     */
    public boolean isDead() {
        return this.curHp <= 0;
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
     * 返回一个酷炫的名字，用『』包起来
     * @return 酷炫名字字符串
     */
    public String getFancyName() {
        return "『" + name + "』";
    }

    /**
     * 返回当前血量状态
     * @return 状态字符串
     */
    public String status() {
        return getFancyName() + "当前血量" + curHp + "/" + maxHp + "。";
    }

    /**
     * 当前所有状态信息
     * @return 包含了所有信息的字符串
     */
    public String toString() {
        return getFancyName() + "是等级为" + level + "的" + role + "，当前有血量" + curHp + "/" + maxHp + "，攻击力是" + minAtt + "-" + maxAtt;
    }

    /**
     * 初始化玩家/敌人，此方法应该在子类中被overridden
     */
    void initialize() {
    }

    public String hello() {
        return "Hello，我是一个生物，Creature！";
    }

    public String helloWorld() {
        return "Hello, World!";
    }
}