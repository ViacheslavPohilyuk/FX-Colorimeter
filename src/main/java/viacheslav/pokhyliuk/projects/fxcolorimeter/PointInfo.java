package viacheslav.pokhyliuk.projects.fxcolorimeter;

class PointInfo {
    private final int red;
    private final int green;
    private final int blue;
    private final long color;
    private final int x;
    private final int y;
    private final String[][] pixels;

    PointInfo(int red, int green, int blue, long color, int x, int y, String[][] pixels) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.color = color;
        this.x = x;
        this.y = y;
        this.pixels = pixels;
    }

    int getRed() {
        return red;
    }

    int getGreen() {
        return green;
    }

    int getBlue() {
        return blue;
    }

    long getColor() {
        return color;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    String[][] getPixels() {
        return pixels;
    }
}
