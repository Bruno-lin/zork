package rpg.oop;

import static rpg.oop.RPG.randomGenerator;

public class Enemy extends Creature {
    @Override
    public String hello() {
        return "Hello，我是一个敌人，Enemy";
    }
}