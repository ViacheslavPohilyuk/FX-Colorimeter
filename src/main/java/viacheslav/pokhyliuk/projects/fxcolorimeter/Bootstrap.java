package viacheslav.pokhyliuk.projects.fxcolorimeter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import viacheslav.pokhyliuk.projects.fxcolorimeter.bean.ExecutorServices;

import java.io.IOException;

public class Bootstrap extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/colorimeter.fxml"));
        loader.setController(new ColorimeterController(
                new ScreenshotColorsAnalyzer(),
                new SnapshotMakerImpl("png", "./images")
        ));
        Parent root = loader.load();
        primaryStage.setTitle("FX Colorimeter");
        Scene scene = new Scene(root, 360, 220);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(360);
        primaryStage.setMinHeight(220);
        primaryStage.show();
    }

    @Override
    public void stop() {
        ExecutorServices.clear();
        System.exit(0);
    }
}
