package engine;

import acm.program.ConsoleProgram;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Engine extends ConsoleProgram {

    public static final String GAME_FILE = "res/map.txt";

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
        // TODO: 请完成这个函数
    }

    /**
     * 读取地点
     * @param scanner   用来读取输入的scanner对象
     */
    private void loadPlaces(Scanner scanner) {
        // 读取地点
        int nPlaces = scanner.nextInt();            // 地点的数量

        // 读取所有地点
        for (int i = 0; i < nPlaces; i++) {
            String placeName = scanner.next();      // 地点的名称
            Place place = new Place(placeName);     // 从Place类别中创建一个实例，并
            places.add(place);                      // 将这个地点保存进places中
        }

        // 设置初始地点，也就是读进来的第一个地点
        currPlace = places.get(0);
    }
}
