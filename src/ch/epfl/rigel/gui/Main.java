package ch.epfl.rigel.gui;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileInputStream;

public class Main extends Application {


    private final static double MIN_WIDTH = 800;
    private final static double MIN_HEIGHT = 600;
    private final static String TITLE = "Rigel";


    FinalSky finalSky = new FinalSky();











    @Override
    public void start(Stage stage) throws Exception {

        Image image = new Image("fond.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(image, null,null,null,null);


        GridPane mainPane = new GridPane();
        //mainPane.setPadding(new Insets(10,10,10,10));
        mainPane.setVgap(5);
        mainPane.setHgap(2);
        mainPane.setBackground(new Background(backgroundImage));

        stage.setMinHeight(MIN_HEIGHT);
        stage.setTitle(TITLE);
        stage.setMinWidth(MIN_WIDTH);


        String familyTitle = "Phosphate";
        double sizeTitle = 100;
        Text textTitle = new Text("Rigel.");
        textTitle.setFont(Font.font(familyTitle,sizeTitle));
        textTitle.setFill(Color.web("#D17008"));
        mainPane.add(textTitle,125,9);

       Text textButton = new Text("Observer le ciel");
       double sizetextButton = 50;
       String familyButton = "Rockwell";
       textButton.setFont(Font.font(familyButton,sizetextButton));
       Button button = new Button(textButton.getText());

       button.setStyle("-fx-background-color: \n" +
               "        linear-gradient(#ffd65b, #e68400),\n" +
               "        linear-gradient(#ffef84, #f2ba44),\n" +
               "        linear-gradient(#ffea6a, #efaa22),\n" +
               "        linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),\n" +
               "        linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));\n" +
               "    -fx-background-radius: 30;\n" +
               "    -fx-background-insets: 0,1,2,3,0;\n" +
               "    -fx-text-fill: #654b00;\n" +
               "    -fx-font-weight: bold;\n" +
               "    -fx-font-size: 25px;\n" +
               "    -fx-padding: 10 20 10 20;");
       mainPane.add(button,125,11);

        Text textButton2 = new Text("En savoir plus...");
        double sizetextButton2 = 50;
        String familyButton2 = "Rockwell";
        textButton.setFont(Font.font(familyButton2,sizetextButton2));
        Button button2 = new Button(textButton2.getText());

        button2.setStyle("-fx-background-color: \n" +
                "        linear-gradient(#ffd65b, #e68400),\n" +
                "        linear-gradient(#ffef84, #f2ba44),\n" +
                "        linear-gradient(#ffea6a, #efaa22),\n" +
                "        linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),\n" +
                "        linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,3,0;\n" +
                "    -fx-text-fill: #654b00;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 25px;\n" +
                "    -fx-padding: 10 20 10 20;");
        mainPane.add(button2,125,13);

        EventHandler<MouseEvent> eventHandler = mouseEvent -> {
            try {
                finalSky.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        button.addEventFilter(MouseEvent.MOUSE_CLICKED,eventHandler);






        stage.setScene(new Scene(mainPane));
        stage.show();
    }
}
