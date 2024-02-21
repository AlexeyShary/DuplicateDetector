package sh.alex.duplicatedetector;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Duplicate Detector");
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}