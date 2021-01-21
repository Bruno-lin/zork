package rpg.oop;

import acm.program.ConsoleProgram;
import acm.util.RandomGenerator;

public class RPG extends ConsoleProgram {

    public static final RandomGenerator randomGenerator = RandomGenerator.getInstance();

    public void run() {
        Player player = new Player();
        Enemy enemy = new Enemy();
        println(player.helloWorld());             // 输出Hello, World!
        println(enemy.helloWorld());              // 输出Hello, World!
    }
}
