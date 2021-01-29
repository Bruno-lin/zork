package rpg_仙侠传;

import java.io.Serializable;

public class Place implements Serializable {
    // 编号
    private String name;    // 当前地点名称
    private String message; // 地点描述
    private String item;  //地点存在的宝物
    private Place north;    // 北边的地点
    private Place south;    // 南边的地点
    private Place east;     // 东边的地点
    private Place west;     // 西边的地点

    public Place(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getItem() {

        return item;
    }

    public void setItem(String item) {

        this.item = item;
    }

    public String getName() {
        return name;
    }

    public Place getNorth() {
        return north;
    }

    public void setNorth(Place north) {
        this.north = north;
    }

    public Place getSouth() {
        return south;
    }

    public void setSouth(Place south) {
        this.south = south;
    }

    public Place getEast() {
        return east;
    }

    public void setEast(Place east) {
        this.east = east;
    }

    public Place getWest() {
        return west;
    }

    public void setWest(Place west) {
        this.west = west;
    }
}
