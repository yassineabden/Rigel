package ch.epfl.rigel.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Main extends Application {


    private final static double MIN_WIDTH = 800;
    private final static double MIN_HEIGHT = 600;
    private final static String TITLE = "Rigel";


    FinalSky finalSky = new FinalSky();

    @Override
    public void start(Stage stage) throws Exception {

        GridPane mainPane = new GridPane();
        mainPane.setHgap(2);
        mainPane.setVgap(2);
        mainPane.setPadding(new Insets(10));

        Image image = new Image("sky.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(image, null,null,null,null);
       mainPane.setBackground(new Background(backgroundImage));

        stage.setMinHeight(MIN_HEIGHT);
        stage.setTitle(TITLE);
        stage.setMinWidth(MIN_WIDTH);


        String familyTitle = "Phosphate";
        double sizeTitle = 100;
        Text textTitle = new Text("Rigel.");
        textTitle.setFont(Font.font(familyTitle,sizeTitle));
        textTitle.setFill(Color.web("#D17008"));
        mainPane.add(textTitle,125,15);

        Text textButton = new Text("Observer le ciel");
        double sizetextButton = 50;
        String familyButton = "Rockwell";
        textButton.setFont(Font.font(familyButton,sizetextButton));
        Button button = new Button(textButton.getText());


       button.setStyle("-fx-background-color: \n" +
               "        #000000,\n" +
               "        linear-gradient(#7ebcea, #2f4b8f),\n" +
               "        linear-gradient(#426ab7, #263e75),\n" +
               "        linear-gradient(#395cab, #223768);\n" +
               "    -fx-background-insets: 0,1,2,3;\n" +
               "    -fx-background-radius: 3,2,2,2;\n" +
               "    -fx-padding: 12 30 12 30;\n" +
               "    -fx-text-fill: white;\n" +
               "    -fx-font-size: 25px;");

        mainPane.add(button,125,30);

        Text textButton2 = new Text("En savoir plus...");
        double sizetextButton2 = 50;
        String familyButton2 = "Rockwell";
        textButton.setFont(Font.font(familyButton2,sizetextButton2));
        Button button2 = new Button(textButton2.getText());

        button2.setStyle("-fx-background-color: \n" +
                "        #000000,\n" +
                "        linear-gradient(#7ebcea, #2f4b8f),\n" +
                "        linear-gradient(#426ab7, #263e75),\n" +
                "        linear-gradient(#395cab, #223768);\n" +
                "    -fx-background-insets: 0,1,2,3;\n" +
                "    -fx-background-radius: 3,2,2,2;\n" +
                "    -fx-padding: 12 30 12 30;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-font-size: 25px;");
        mainPane.add(button2,125,40);




        EventHandler<MouseEvent> eventButton1 = mouseEvent -> {
            try {
                finalSky.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        EventHandler<MouseEvent> eventButton2 = mouseEvent -> {
            showTextButton2(stage);
        };
        button.addEventFilter(MouseEvent.MOUSE_CLICKED,eventButton1);
        button2.addEventFilter(MouseEvent.MOUSE_CLICKED,eventButton2);









        stage.setScene(new Scene(mainPane));
        stage.show();
    }


    private void showTextButton2 ( Stage stage) {

        BorderPane borderPane = new BorderPane();
        Image image = new Image("sky.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(image, null,null,null,null);
        borderPane.setBackground(new Background(backgroundImage));

        Text text = new Text("Ce travail a été réalisé dans " +
                "le cadre d’un cours donné à l’EPFL" +
                " par Yassine Abdennadher et Juliette Aerni," +
                " deux jeunes programmeurs qui ne sont qu’à" +
                " leur début dans le monde de la programmation. " +
                "On espère que vous allez profiter de leur magnifique travail. " +
                "Hésitez pas non plus à envoyer votre avis aux adresses mail: " +
                "yassine.abdennadher@epfl.ch ou juliette.aerni@epfl.ch.");

        stage.setHeight(MIN_HEIGHT);
        stage.setTitle(TITLE);
        stage.setWidth(MIN_WIDTH);
        TextField textField = new TextField("Ce travail a été réalisé dans " +
                "le cadre d’un cours donné à l’EPFL" +
                " par Yassine Abdennadher et Juliette Aerni," +
                " deux jeunes programmeurs qui ne sont qu’à" +
                " leur début dans le monde de la programmation. " +
                "On espère que vous allez profiter de leur magnifique travail. " +
                "Hésitez pas non plus à envoyer votre avis aux adresses mail: " +
                "yassine.abdennadher@epfl.ch ou juliette.aerni@epfl.ch.");
        double size = 18;
        String family = "Rockwell";
        text.setFont(Font.font(family,size));



        borderPane.setCenter(text);

        stage.setScene(new Scene(borderPane));
        stage.show();





    }











}
