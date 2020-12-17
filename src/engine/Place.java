package engine;

public class Place {

    private String name;    // 当前地点名称
    private Place north;    // 北边的地点
    private Place south;    // 南边的地点
    private Place east;     // 东边的地点
    private Place west;     // 西边的地点

    public Place(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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