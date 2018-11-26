package viacheslav.pokhyliuk.projects.fxcolorimeter.bean;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum GridProperties {
    INSTANCE;

    GridProperties() {
        this.scope = 5;
        this.cells = new HashMap<>();
    }

    private int scope;
    private Map<Integer, List<Pane>> cells;
    private int[] scopes = {5, 15, 25, 45};

    public static int getCurrentScope() {
        return INSTANCE.scope;
    }

    public static void changeScope(int scope) {
        validate(scope);
        INSTANCE.scope = scope;
    }

    public static List<Pane> getCells(int gridSize, double width, double height) {
        return INSTANCE.cells.compute(gridSize, (k, v) -> {
            if (v == null) {
                List<Pane> panes = new ArrayList<>();
                for (int i = 0; i < (gridSize * gridSize); i++) {
                    Pane pane = new Pane();
                    pane.setPrefWidth(width);
                    pane.setPrefHeight(height);
                    panes.add(pane);
                }
                return panes;
            }
            return v;
        });
    }

    public static int[] getScopes() {
        return INSTANCE.scopes;
    }

    public static int getMaxScope() {
        int[] scopes = INSTANCE.scopes;
        return scopes[scopes.length - 1];
    }

    private static void validate(int size) {
        if ((size % 2 != 0 && size % 5 != 0)) {
            throw new IllegalStateException(String.format(
                    "Size %s is wrong, grid scope must be odd and multiple of 5", size
            ));
        }
    }
}
