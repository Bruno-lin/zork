package engine;

import acm.program.ConsoleProgram;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Engine extends ConsoleProgram {

    public static final String GAME_FILE = "res/map-with-item.txt";

    private Place currPlace;    // 当前所处的地点
    ArrayList<Place> places;    // 保存所有的地点
    boolean gameEnded;          // 玩家是否退出游戏

    public void run() {
        if (loadGame()) {
            mainLoop();
        }
    }

    /**
     * 主循环。只要玩家不选择退出，游戏就一直运行下去
     */
    private void mainLoop() {
        gameEnded = false;
        moveTo(currPlace);
        while (!gameEnded) {
            println();
            println("请输入移动方向（输入\"退出\"结束游戏）");
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
                        moveTo(currPlace.getWest());
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
                    print("DEBUG2 - ");
                    String item = currPlace.getItem();
                    if (null != item) {
                        println("你找到了一个" + item + "！");
                        currPlace.setItem(null);
                    } else {
                        println("你什么没找到。");
                    }
                    break;
                case "退出":
                    gameEnded = true;
                    println("欢迎再来！");
                    break;
                default:
                    println("你输入的命令有误，请重新输入");
            }
        }
    }

    /**
     * 移动到一个新的地点
     * @param place 要移动到的目的地
     */
    private void moveTo(Place place) {
        println("你现在到了" + place.getName());
        println(place.getMessage());
        print("DEBUG - ");
        if (null == (place.getItem())) {
            println("这里什么都没有。");
        } else {
            println("这里有一个" + place.getItem());
        }
        currPlace = place;
    }

    /**
     * 读取游戏配置，如地图等
     * @return  读取成功返回true，失败返回false
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
     * @param scanner   用来读取输入的scanner对象
     */
    private void loadRoutes(Scanner scanner) {
        int nRoutes = scanner.nextInt();         // 路线的数量
        String title = scanner.next();                //列表
        
        // 读取所有路线
        for (int i = 0; i < nRoutes; i++) {
            int placeId = scanner.nextInt();
            String direction = scanner.next();
            int placeId1 = scanner.nextInt();

            Place place1 = places.get(placeId);
            Place place2 = places.get(placeId1);

//            //这个地方的冒号就是遍历places的集合，取出每一个元素
//            for (Place place : places) {
//                //遍历结果和出发地点对比，如果为true
//                if (place.getId() == placeId) {
//                    place1 = place;                 //赋值给place1
//                }
//                //遍历结果和目标地点对比，如果为true
//                if (place.getId() == placeId1) {
//                    place2 = place;                //赋值给place2
//                }
//            }
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
     * @param scanner   用来读取输入的scanner对象
     */
    private void loadPlaces(Scanner scanner) {
        // 读取地点
        int nPlaces = scanner.nextInt();            // 地点的数量
        String title = scanner.next();                //列表

        // 读取所有地点
        for (int i = 0; i < nPlaces; i++) {
            int placeId = scanner.nextInt();        // 地点编号
            String placeName = scanner.next();      // 地点的名称
            Place place = new Place(placeName);     // 从Place类别中创建一个实例，并
            places.add(placeId,place);                      // 将这个地点保存进places中

            String message = scanner.next();        //地点的信息
            place.setMessage(message);
        }

        // 设置初始地点，也就是读进来的第一个地点
        currPlace = places.get(0);
    }

    /**
     * 读取宝物
     * @param scanner
     */
    private void loadItems(Scanner scanner) {
        // 读取路线
        int nItems = scanner.nextInt();            // 宝物的数量
        scanner.next();
        // 读取所有宝物
        for (int i = 0; i < nItems; i++) {
            int situation = scanner.nextInt();
            String treasure = scanner.next();
            Place place = places.get(situation);
            place.setItem(treasure);
        }
    }
}
