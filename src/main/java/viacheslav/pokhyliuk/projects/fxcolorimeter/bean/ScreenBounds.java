package viacheslav.pokhyliuk.projects.fxcolorimeter.bean;

import java.awt.*;

public enum ScreenBounds {
    INSTANCE;

    ScreenBounds() {
        screenBounds = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();
    }

    private Rectangle screenBounds;

    public static Rectangle getInstance() {
        return INSTANCE.screenBounds;
    }
}
