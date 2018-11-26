package viacheslav.pokhyliuk.projects.fxcolorimeter;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import viacheslav.pokhyliuk.projects.fxcolorimeter.bean.Automation;
import viacheslav.pokhyliuk.projects.fxcolorimeter.bean.ExecutorServices;
import viacheslav.pokhyliuk.projects.fxcolorimeter.bean.GridProperties;
import viacheslav.pokhyliuk.projects.fxcolorimeter.bean.ScreenBounds;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ScreenshotColorsAnalyzer implements ColorsAnalyzer {
    private final String[][] colorsGrid;
    private final AtomicReference<BufferedImage> screenshot = new AtomicReference<>();
    private final ReadOnlyObjectWrapper<PointInfo> pointInfoWrapper = new ReadOnlyObjectWrapper<>();

    ScreenshotColorsAnalyzer() {
        int max = GridProperties.getMaxScope();
        this.colorsGrid = new String[max][max];
    }

    @Override
    public ReadOnlyObjectProperty<PointInfo> getInfo() {
        return this.pointInfoWrapper.getReadOnlyProperty();
    }

    @Override
    public void analyze() {
        scheduleScreenshots();
        scheduleColorsCalculation();
    }

    private void scheduleColorsCalculation() {
        ExecutorServices.getScheduled().scheduleAtFixedRate(
                () -> pointInfoWrapper.set(computePointInfo()),
                0, 5, TimeUnit.MILLISECONDS
        );
    }

    private void scheduleScreenshots() {
        ExecutorServices.getScheduled().scheduleAtFixedRate(() -> {
            Rectangle screen = ScreenBounds.getInstance();
            this.screenshot.set(Automation.getInstance().createScreenCapture(screen));
        }, 0, 250, TimeUnit.MILLISECONDS);
    }

    private PointInfo computePointInfo() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        int x = (int) location.getX();
        int y = (int) location.getY();
        Color color = Automation.getInstance().getPixelColor(x, y);
        analyzeAdjacentPixels(x, y);
        return new PointInfo(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                Math.abs(color.getRGB()),
                x, y,
                colorsGrid
        );
    }

    private void analyzeAdjacentPixels(int x, int y) {
        int size = GridProperties.getCurrentScope();
        int diagonal = size / 2;
        for (int row = 0, j = y - diagonal; j < y + size - diagonal; j++, row++) {
            for (int col = 0, i = x - diagonal; i < x + size - diagonal; i++, col++) {
                assignPixelColor(col, row, i, j);
            }
        }
    }

    private void assignPixelColor(int col, int row, int x, int y) {
        double width = ScreenBounds.getInstance().getWidth();
        double height = ScreenBounds.getInstance().getHeight();
        colorsGrid[col][row] = (x >= width || x <= 0 || y >= height || y <= 0)
                ? String.format("#%02x%02x%02x", 0, 0, 0)
                : computeColor(this.screenshot.get().getRGB(x, y));
    }

    private String computeColor(int color) {
        final int red = (color & 0x00ff0000) >> 16;
        final int green = (color & 0x0000ff00) >> 8;
        final int blue = color & 0x000000ff;
        return String.format("#%02x%02x%02x", red, green, blue);
    }

    private void saveCurrentImage() {
        try {
            int size = GridProperties.getCurrentScope();
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    int rgb = this.screenshot.get().getRGB(x, y);
                    image.setRGB(x, y, rgb);
                }
            }
            File outputFile = new File("./images/" + LocalDateTime.now().toString() + ".bmp");
            ImageIO.write(image, "bmp", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}