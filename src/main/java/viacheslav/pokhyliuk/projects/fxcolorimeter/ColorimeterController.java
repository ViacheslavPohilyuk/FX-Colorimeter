package viacheslav.pokhyliuk.projects.fxcolorimeter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import viacheslav.pokhyliuk.projects.fxcolorimeter.bean.GridProperties;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.input.KeyCode.F6;

class ColorimeterController implements Initializable {

    @FXML
    private Label redLabel;

    @FXML
    private Label greenLabel;

    @FXML
    private Label blueLabel;

    @FXML
    private Label colorLabel;

    @FXML
    private Label coordsLabel;

    @FXML
    private GridPane colorPane;

    @FXML
    private Slider scopeSlider;

    @FXML
    private Pane currentPixelPane;

    @FXML
    private BorderPane mainPane;

    private PointInfo currentPointInfo;

    private ColorsAnalyzer colorsAnalyzer;

    ColorimeterController(ColorsAnalyzer analyzer) {
        this.colorsAnalyzer = analyzer;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillGrid();
        configSlider();
        assignValues();
        colorsAnalyzer.analyze();
        saveCurrentScreen();
    }

    private void saveCurrentScreen() {
        mainPane.setOnKeyPressed(event -> {
            if (event.getCode() == F6) {
                try {
                    colorsAnalyzer.saveGridSnapshot(
                            currentPointInfo.getX(),
                            currentPointInfo.getY());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fillGrid() {
        int size = GridProperties.getCurrentScope();
        double width = colorPane.getPrefWidth() / size;
        double height = colorPane.getPrefHeight() / size;
        List<Pane> cells = GridProperties.getCells(size, width, height);
        assignCells(cells, size);
    }

    private void assignValues() {
        colorsAnalyzer.getInfo().addListener((obs, oldValue, newValue) ->
                Platform.runLater(() -> {
                    this.currentPointInfo = newValue;
                    assignLabels(newValue);
                    modifyCells(newValue.getPixels());
                })
        );
    }

    private void assignCells(List<Pane> cells, int size) {
        int cellCounter = 0;
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                colorPane.add(cells.get(cellCounter++), column, row);
            }
        }
    }

    private void updateGrid(int newSize) {
        List<Node> cells = colorPane.getChildren();
        cells.removeAll(colorPane.getChildren());
        GridProperties.changeScope(newSize);
        fillGrid();
    }

    private void assignLabels(PointInfo info) {
        redLabel.setText(String.valueOf(info.getRed()));
        greenLabel.setText(String.valueOf(info.getGreen()));
        blueLabel.setText(String.valueOf(info.getBlue()));
        colorLabel.setText(String.valueOf(info.getColor()));
        coordsLabel.setText(formatCoordinates(info.getX(), info.getY()));
    }

    private String formatCoordinates(int x, int y) {
        return String.format("%s ; %s",
                String.valueOf(x),
                String.valueOf(y)
        );
    }

    private void modifyCells(String[][] colors) {
        int cellCounter = 0;
        int size = GridProperties.getCurrentScope();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Pane cell = (Pane) colorPane.getChildren().get(cellCounter++);
                String cellStyle = String.format(
                        "-fx-background-color: %s;",
                        colors[col][row]
                );
                if (cellCounter == ((size * size) / 2) + 1) {
                    cellStyle += "-fx-border-color: black;-fx-border-width: 1;";
                    currentPixelPane.setStyle(cellStyle);
                }
                cell.setStyle(cellStyle);
            }
        }
    }

    private void configSlider() {
        int[] scopes = GridProperties.getScopes();
        scopeSlider.setMin(0);
        scopeSlider.setMax(scopes.length - 1);
        scopeSlider.setValue(0);
        scopeSlider.setMajorTickUnit(1);
        scopeSlider.setMinorTickCount(0);
        scopeSlider.setShowTickMarks(true);
        scopeSlider.setSnapToTicks(true);
        scopeSlider.setBlockIncrement(1.0);
        scopeSlider.setOnMouseClicked((obs) -> {
            if (!scopeSlider.isValueChanging()) {
                updateGrid(scopes[(int) scopeSlider.getValue()]);
            }
        });
        scopeSlider.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case RIGHT: {
                    if (!scopeSlider.isValueChanging()) {
                        int max = scopes.length - 1;
                        int val = (int) scopeSlider.getValue();
                        scopeSlider.setValue(val);
                        updateGrid(scopes[val < max ? ++val : val]);
                    }
                    break;
                }
                case LEFT: {
                    if (!scopeSlider.isValueChanging()) {
                        int val = (int) scopeSlider.getValue();
                        scopeSlider.setValue(val);
                        updateGrid(scopes[val > 0 ? --val : val]);
                    }
                    break;
                }
            }
        });
    }
}
