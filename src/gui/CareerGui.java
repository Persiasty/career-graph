package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CareerGui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fl = new FXMLLoader(getClass().getResource("views/Gui.fxml"));
        Parent root = fl.load();

        Scene main = new Scene(root);
        primaryStage.setScene(main);
        primaryStage.show();
    }
}
