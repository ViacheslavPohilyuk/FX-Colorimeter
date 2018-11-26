package viacheslav.pokhyliuk.projects.fxcolorimeter.bean;

import java.awt.*;

public enum Automation {
    INSTANCE;

    Automation() {
        try {
            this.robot = new java.awt.Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private java.awt.Robot robot;

    public static java.awt.Robot getInstance() {
        return INSTANCE.robot;
    }
}
