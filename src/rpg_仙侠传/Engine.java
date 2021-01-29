package rpg_仙侠传;

import acm.program.ConsoleProgram;
import acm.util.RandomGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Engine extends ConsoleProgram {

    public static final RandomGenerator randomGenerator = RandomGenerator.getInstance();
    public static final String GAME_FILE = "res/map-paladin.txt";

    public static final String PLAYER_ARCHIVE_FILE = "res/archive.player";  //角色存档
    public static final String PLACES_ARCHIVE_FILE = "res/archive.places";  //地点存档
    public static final String BAG_ARCHIVE_FILE = "res/archive.bag";        //背包存档
    public static final String LOGO = "res/logo.txt";
    public static final String MAP = "res/checkMap.txt";
    public Player player;
    public Enemy enemy;
    ArrayList<Place> places;    // 保存所有的地点
    private ArrayList<String> bag = new ArrayList<>(); //背包
    private Place currPlace;    // 当前所处的地点
    private boolean gameEnded;          // 玩家是否退出游戏
    private boolean isPass = false;       // 玩家是否退出游戏

    public void run() {
        File image = new File(LOGO);
        printImage(image);
        if (loadGame()) {
            gameStart();
            mainLoop();
        }

        if (!player.isDead())
            combat();
    }

    /**
     * 游戏开始
     */
    private void gameStart() {
        println();
        String userChoice = choose("请选择游戏方式", "新的游戏", "继续游戏");
        println();
        switch (userChoice) {
            case "新的游戏":
                setupPlayer();
                break;
            case "继续游戏":
                readArchive();
                break;
        }
    }

    /**
     * 主循环。只要玩家不选择退出，游戏就一直运行下去
     */
    public void mainLoop() {
        gameEnded = false;
        moveTo(currPlace);
        while (!gameEnded) {
            println();
            println("请输入指令（输入\"退出\"结束游戏）");
            println("功能指令:\"搜索\"\"战斗\"\"存档\"\"查看行李\"\"查看地图\"");
            print("> ");

            String direction = readLine();
            switch (direction) {
                case "东":
                    if (currPlace.getEast() != null) {
                        moveTo(currPlace.getEast());
                    } else {
                        println("那边好像无路可走了……");
                    }
                    break;
                case "南":
                    if (currPlace.getSouth() != null) {
                        moveTo(currPlace.getSouth());
                    } else {
                        println("那边好像无路可走了……");
                    }
                    break;
                case "西":
                    if (currPlace.getWest() != null) {
                        if (hiddenStory(currPlace.getWest())) {
                            moveTo(currPlace.getWest());
                        }
                    } else {
                        println("那边好像无路可走了……");
                    }
                    break;
                case "北":
                    if (currPlace.getNorth() != null) {
                        moveTo(currPlace.getNorth());
                    } else {
                        println("那边好像无路可走了……");
                    }
                    break;
                case "搜索":
                    search();
                    break;
                case "查看行李":
                    checkBag();
                    break;
                case "查看地图":
                    File image = new File(MAP);
                    printImage(image);
                    break;
                case "战斗":
                    combat();
                    break;
                case "存档":
                    archive();
                    break;
                case "退出":
                    gameEnded = true;
                    isPass = true;
                    println("欢迎再来！");
                    break;
                default:
                    println("你输入的命令有误，请重新输入");
            }
        }
    }

    /**
     * 搜索背包
     */
    private void search() {
        print("提示:  ");
        String item = currPlace.getItem();
        if (null == item) {
            println("你什么没找到。");
        } else {
            println("你找到了一个" + item + "！");
            currPlace.setItem(null);
            bag.add(item);
            useItem(item);
        }
    }

    /**
     * 使用道具
     *
     * @param item
     */
    private void useItem(String item) {
        println();
        String userChoice = choose("是否使用道具", "是", "否");
        switch (userChoice) {
            case "是":
                println("使用" + item);
                if (item.equals("绛苍剑")) sword();
                if (item.equals("大补丹")) hpCap();
                if (item.equals("灵符丹")) expCap();
                break;
            case "否":
                println("未使用" + item);
        }
    }

    /**
     * 道具
     */
    private void sword() {
        int incAtt = randomGenerator.nextInt(10, 20);
        player.maxAtt += incAtt;
        player.minAtt += incAtt;
        println("你的攻击力增加" + incAtt);
    }

    private void hpCap() {
        int incHp = randomGenerator.nextInt(5, 10);
        player.maxHp += incHp;
        player.curHp = player.maxHp;
        println("你的生命值增加" + incHp);
    }

    private void expCap() {
        int incExp = randomGenerator.nextInt(50, 100);
        player.xp += incExp;
        println("你的经验值增加" + incExp);
        if (player.checkLevelUp())// 升级检查
        {
            println("你升级了！血量恢复满格！");
        }
        println("你当前拥有" + player.xp + "点经验值。");
        println(player.printInfo());
    }

    /**
     * 检查背包
     */
    private void checkBag() {
        int nItem = bag.size();
        if (nItem == 0) {
            println("你的行李箱内空无一物。");
        } else {
            println("你现在共有" + nItem + "个道具，依次是：");
            for (int i = 0; i < nItem; i++) {
                println(i + 1 + "." + bag.get(i));
            }
        }
    }

    /**
     * 移动到一个新的地点
     *
     * @param place 要移动到的目的地
     */
    private void moveTo(Place place) {
        println("你现在到了" + place.getName());
        println(place.getMessage());
        print("提示:  ");
        if (null == place.getItem()) {
            println("这里什么都没有。");
        } else {
            println("这里有一个" + place.getItem());
        }
        currPlace = place;
    }

    /**
     * 读取游戏配置，如地图等
     *
     * @return 读取成功返回true，失败返回false
     */
    private boolean loadGame() {
        places = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(GAME_FILE));
            loadPlaces(scanner);
            loadRoutes(scanner);
            loadItems(scanner);
        } catch (FileNotFoundException e) {
            println("游戏文件读取错误！");
            return false;
        }
        return true;
    }

    /**
     * 读取道路
     *
     * @param scanner 用来读取输入的scanner对象
     */
    private void loadRoutes(Scanner scanner) {
        int nRoutes = scanner.nextInt();         // 路线的数量
        scanner.next();

        // 读取所有路线
        for (int i = 0; i < nRoutes; i++) {
            int placeId = scanner.nextInt();
            String direction = scanner.next();
            int placeId1 = scanner.nextInt();

            Place place1 = places.get(placeId);
            Place place2 = places.get(placeId1);

            //如果出发地点或者目标地点不为空
            if (place1 != null && place2 != null) {
                switch (direction) {
                    case "东":
                        place1.setEast(place2);
                        place2.setWest(place1);
                        break;
                    case "南":
                        place1.setSouth(place2);
                        place2.setNorth(place1);
                        break;
                    case "西":
                        place1.setWest(place2);
                        place2.setEast(place1);
                        break;
                    case "北":
                        place1.setNorth(place2);
                        place2.setSouth(place1);
                        break;
                }
            }
        }
    }

    /**
     * 读取地点
     *
     * @param scanner 用来读取输入的scanner对象
     */
    private void loadPlaces(Scanner scanner) {
        // 读取地点
        int nPlaces = scanner.nextInt();            // 地点的数量
        scanner.next();

        // 读取所有地点
        for (int i = 0; i < nPlaces; i++) {
            int placeId = scanner.nextInt();        // 地点编号
            String placeName = scanner.next();      // 地点的名称
            Place place = new Place(placeName);     // 从Place类别中创建一个实例，并
            places.add(placeId, place);                      // 将这个地点保存进places中

            String message = scanner.next();        //地点的信息
            place.setMessage(message);
        }

        // 设置初始地点，也就是读进来的第一个地点
        currPlace = places.get(0);
    }

    /**
     * 读取道具
     *
     * @param scanner
     */
    private void loadItems(Scanner scanner) {
        // 读取宝物
        int nItems = scanner.nextInt();            // 宝物的数量
        scanner.next();
        // 读取所有宝物
        for (int i = 0; i < nItems; i++) {
            int itemId = scanner.nextInt();
            String items = scanner.next();
            Place item = places.get(itemId);
            item.setItem(items);
        }
    }

    /**
     * 建立玩家
     */
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

    /**
     * 战斗
     */
    private void combat() {

        // 每个场景随机产生1-3个敌人
        int enemyCount = randomGenerator.nextInt(1, 3);
        for (int i = 0; i < enemyCount; i++) {
            if (isPass) {
                return;
            }
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
     * 玩家和敌人对boo战
     */
    public void battle() {
        println(player.printInfo());
        println(enemy.printInfo());

        // 回合制循环进行，直至某一方阵亡
        label:
        while (true) {
            println();

            // 每一回合都首先从玩家开始行动
            String userChoice = choose("请选择你的行动", "攻击", "逃跑", "补血", "查看状态");
            switch (userChoice) {
                case "查看状态":
                    println(player.printStatus());
                    continue;
                case "补血":
                    println(player.useHealthPotion());
                    println(player.printStatus());
                    continue;
                case "逃跑":
                    boolean success = randomGenerator.nextBoolean();
                    if (success) {
                        println("逃跑成功！");
                        break label;
                    } else {
                        println("逃跑失败！");
                    }
                    break;
                case "攻击":
                    int damage = player.attack(enemy);
                    println(player.attackMessage(enemy, damage));
                    println(enemy.printStatus());
                    break;
            }

            if (enemy.isDead()) {
                // 如果敌人阵亡，玩家经验值提升
                println(String.format("你杀死了%s。\n", enemy.name));
                println("你获得了" + player.gainXp(enemy) + "点经验值。");
                if (player.checkLevelUp())// 升级检查
                {
                    println("你升级了！血量恢复满格！");
                    if (player.level >= 15) {
                        println("少侠已名留江湖，且听少侠后闻");
                        gameEnded = true;
                        isPass = true;
                        return;
                    }
                }
                println("你当前拥有" + player.xp + "点经验值。");
                println(player.printInfo());
                break;
            } else {
                // 没阵亡则轮到敌人行动
                int damage = enemy.attack(player);
                println(enemy.attackMessage(player, damage));
                println(player.printStatus());

                // 如果敌人将玩家打死，游戏结束
                if (player.isDead()) {
                    println("你挂了！");
                    break;
                }
            }
        }
    }

    /**
     * 隐藏剧情
     *
     * @param place
     * @return
     */
    private boolean hiddenStory(Place place) {
        if (place.getName().equals("檾山仙市")) {
            println("你现在到了" + place.getName());
            for (String item : bag) {
                if (item.equals("舍利子")) {
                    println("官府:" + "抓捕此贼，此贼手上有高僧設利罗");
                    println("少侠您已被捕，此次仙旅失败");
                    gameEnded = true;
                    isPass = true;
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * 读档
     */
    @SuppressWarnings("unchecked")
    private void readArchive() {
        File playArchive = new File(PLAYER_ARCHIVE_FILE);
        File placesArchive = new File(PLACES_ARCHIVE_FILE);
        File bagArchive = new File(BAG_ARCHIVE_FILE);
        if (!playArchive.exists() || !placesArchive.exists()) {
            println("错误存档不存在或者不完整，请检测" + PLAYER_ARCHIVE_FILE + "以及" + PLACES_ARCHIVE_FILE + "以及" + BAG_ARCHIVE_FILE);
            return;
        }

        try (FileInputStream fis = new FileInputStream(playArchive);
             FileInputStream fis2 = new FileInputStream(placesArchive);
             FileInputStream fis3 = new FileInputStream(bagArchive);

             ObjectInputStream ois = new ObjectInputStream(fis);
             ObjectInputStream ois2 = new ObjectInputStream(fis2);
             ObjectInputStream ois3 = new ObjectInputStream(fis3)) {
            player = (Player) ois.readObject();
            currPlace = (Place) ois2.readObject();
            bag = (ArrayList<String>) ois3.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 存档
     */
    private void archive() {

        File playerArchive = new File(PLAYER_ARCHIVE_FILE);
        File placesArchive = new File(PLACES_ARCHIVE_FILE);
        File bagArchive = new File(BAG_ARCHIVE_FILE);

        try (FileOutputStream fos = new FileOutputStream(playerArchive);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
             FileOutputStream fos2 = new FileOutputStream(placesArchive);
             ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
             FileOutputStream fos3 = new FileOutputStream(bagArchive);
             ObjectOutputStream oos3 = new ObjectOutputStream(fos3)) {
            if (!playerArchive.exists()) {
                playerArchive.createNewFile();
            }
            if (!placesArchive.exists()) {
                placesArchive.createNewFile();
            }
            oos.writeObject(player);
            oos2.writeObject(currPlace);
            oos3.writeObject(bag);
        } catch (IOException e) {
            e.printStackTrace();
        }
        println("正在存档...成功！");
    }

    /**
     * 展示字符画
     **/
    private void printImage(File file) {

        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while (true) {
                String line = bufferedReader.readLine();
                if (null == line) {
                    break;
                }
                println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
