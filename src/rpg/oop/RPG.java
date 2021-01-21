package rpg.oop;

import acm.program.ConsoleProgram;
import acm.util.RandomGenerator;

public class RPG extends ConsoleProgram {

    public static final RandomGenerator randomGenerator = RandomGenerator.getInstance();

    public void run() {
        Creature player = new Player();
        Creature enemy = new Enemy();
        println(player.hello());             // 输出Hello!
        println(enemy.hello());              // 输出Hello!
    }
}
