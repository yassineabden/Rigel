package ch.epfl.rigel.gui;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {

        BorderPane borderPane = new BorderPane();
        stage.setMinHeight(600);
        stage.setTitle("Rigel");
        stage.setMinWidth(800);

        HBox hBox = new HBox();
        HBox child1



    }
}
