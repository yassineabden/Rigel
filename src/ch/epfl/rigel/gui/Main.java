package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.util.function.UnaryOperator;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {

        ObserverLocationBean observerLocationBean =
                new ObserverLocationBean();
        observerLocationBean.setCoordinates(
                GeographicCoordinates.ofDeg(6.57, 46.52));

        BorderPane borderPane = new BorderPane();
        stage.setMinHeight(600);
        stage.setTitle("Rigel");
        stage.setMinWidth(800);

        Separator separator1 = new Separator(Orientation.VERTICAL);
        Separator separator2 = new Separator(Orientation.VERTICAL);

        HBox observPos = new HBox();
        HBox instObserv = new HBox();
        HBox timeLapse = new HBox();

        HBox controlBar = new HBox(observPos,separator1,instObserv,separator2,timeLapse);
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        observPos.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        Label labelLong = new Label("Longitude (°) :");
        Label labelLat = new Label("Latitude (°) :");

        TextField lonText = new TextField();
        TextField latText = new TextField();
        lonText.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        latText.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        NumberStringConverter stringConverter =
                new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> lonFilter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newLonDeg =
                        stringConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLonDeg(newLonDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> lonTextFormatter =
                new TextFormatter<>(stringConverter, 0, lonFilter);

        lonText.setTextFormatter(lonTextFormatter);

        UnaryOperator<TextFormatter.Change> latFilter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newLatDeg =
                        stringConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLatDeg(newLatDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> latTextFormatter =
                new TextFormatter<>(stringConverter, 0, latFilter);

        latText.setTextFormatter(latTextFormatter);

        observPos.getChildren().addAll(labelLong,lonText,labelLat,latText);

        //TODO check avec Ju bind value de lonTextFormatter et la position de l'observateur

        lonTextFormatter.valueProperty().bind(observerLocationBean.lonDegProperty());
        latTextFormatter.valueProperty().bind(observerLocationBean.latDegProperty());

        instObserv.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        Label dateText = new Label("Date :");











    }
}
