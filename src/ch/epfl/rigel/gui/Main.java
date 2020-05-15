package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import javax.lang.model.element.Name;
import java.io.InputStream;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.UnaryOperator;


public final class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        ObserverLocationBean observerLocationBean =
                new ObserverLocationBean();
        observerLocationBean.setCoordinates(
                GeographicCoordinates.ofDeg(6.57, 46.52));

        ZonedDateTime when = ZonedDateTime.now();
        DateTimeBean dateTimeBean = new DateTimeBean();
        dateTimeBean.setZonedDateTime(when);

        ViewingParametersBean viewingParametersBean = new ViewingParametersBean();

        TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);
        timeAnimator.acceleratorProperty().set(NamedTimeAccelerator.TIMES_300.getAccelerator());

        Pane skyPane = new Pane();
        Pane informationPane = new Pane();
        BorderPane mainPane = new BorderPane();


        //BorderPane mainPane = new BorderPane(skyPane,controlPane,null,informationPane,null);

        // Fenêtre principale
        stage.setMinHeight(600);
        stage.setTitle("Rigel");
        stage.setMinWidth(800);

        // Barre de contrôle
        //je pense qu'on va devoir les set children à la controlePane

        //Sous-panneau position d'observation
        HBox observationPositionPane = new HBox();
        HBox observationTimePane = new HBox();
        HBox timeLapsePane = new HBox();

        Separator separator1 = new Separator(Orientation.VERTICAL);
        Separator separator2 = new Separator(Orientation.VERTICAL);

        HBox controlBar = new HBox(observationPositionPane,separator1,observationTimePane,separator2,timeLapsePane);
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        //observationPositionPane
        observationPositionPane.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        Label lonLabel = new Label("Longitude (°) :");
        Label latLabel = new Label("Latitude (°) :");

        TextField lonTextField = new TextField();
        TextField latTextField = new TextField();
        lonTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        latTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        NumberStringConverter stringToNumberConverter =
                new NumberStringConverter("#0.00");

        //longitude texte field
        UnaryOperator<TextFormatter.Change> lonFilter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newLonDeg =
                        stringToNumberConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLonDeg(newLonDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> lonTextFormatter =
                new TextFormatter<>(stringToNumberConverter, 0, lonFilter);

        lonTextField.setTextFormatter(lonTextFormatter);

        // latitude text field
        UnaryOperator<TextFormatter.Change> latFilter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newLatDeg =
                        stringToNumberConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLatDeg(newLatDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> latTextFormatter =
                new TextFormatter<>(stringToNumberConverter, 0, latFilter);

        latTextField.setTextFormatter(latTextFormatter);

        observationPositionPane.getChildren().addAll(lonLabel,lonTextField,latLabel,latTextField);

        //TODO check avec Ju bind value de lonTextFormatter et la position de l'observateur
        //sans doutes ouais mais il faut def ce qui va se passer?

        lonTextFormatter.valueProperty().bind(observerLocationBean.lonDegProperty());
        latTextFormatter.valueProperty().bind(observerLocationBean.latDegProperty());


        //instant d'observation HBox
        observationTimePane.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        //Date field
        Label dateText = new Label("Date :");
        DatePicker datePicker = new DatePicker();
        datePicker.setStyle("-fx-pref-width: 120;");
        datePicker.valueProperty().bind(dateTimeBean.dateProperty());

        //todo peut -être les liés avant de les setChildren à observationTimePane?

        // Hour Field
        Label hourLabel = new Label("Heure :");
        TextField hourTextField = new TextField();
        hourTextField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");

        DateTimeFormatter hmsFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter =
                new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter =
                new TextFormatter<>(stringConverter);
        timeFormatter.valueProperty().bind(dateTimeBean.timeProperty());

        // todo Time zone pane

        //time lapse pane

        timeLapsePane.setStyle("-fx-spacing: inherit;");
        ChoiceBox <NamedTimeAccelerator> timeAcceleratorChoiceBox = new ChoiceBox<>();
        timeAcceleratorChoiceBox.setItems( FXCollections.observableArrayList(NamedTimeAccelerator.values()));

        //todo wtf?
        timeAcceleratorChoiceBox.valueProperty().bind(Bindings.select(timeAnimator, "name"));
        timeAnimator.acceleratorProperty().bind(Bindings.select(timeAcceleratorChoiceBox.valueProperty(), "accelerator"));

        //Bouton reset play pause
        InputStream fontStream = getClass()
                .getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf");
        Font fontAwesome = Font.loadFont(fontStream, 15);

        Button resetButton = new Button("\uf0e2");
        resetButton.setFont(fontAwesome);


    }




}
