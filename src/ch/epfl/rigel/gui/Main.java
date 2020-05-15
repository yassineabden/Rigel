package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
import java.util.*;
import java.util.function.UnaryOperator;


public final class Main extends Application {

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try (InputStream hs = resourceStream("/hygdata_v3.csv")) {

            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .build();


            ObserverLocationBean observerLocationBean =
                    new ObserverLocationBean();
            observerLocationBean.setCoordinates(
                    GeographicCoordinates.ofDeg(6.57, 46.52));

            ZonedDateTime when = ZonedDateTime.now();
            DateTimeBean dateTimeBean = new DateTimeBean();
            dateTimeBean.setZonedDateTime(when);

            ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
            viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(180.000000000001, 15));
            viewingParametersBean.setFieldOfViewDeg(100);

            TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);
            timeAnimator.acceleratorProperty().set(NamedTimeAccelerator.TIMES_300.getAccelerator());



            BorderPane mainPane = new BorderPane();




            // Fenêtre principale
            stage.setMinHeight(600);
            stage.setTitle("Rigel");
            stage.setMinWidth(800);

            // Barre de contrôle

            //Sous-panneau position d'observation
            HBox observationPositionPane = new HBox();
            HBox observationTimePane = new HBox();
            HBox timeLapsePane = new HBox();

            Separator separator1 = new Separator(Orientation.VERTICAL);
            Separator separator2 = new Separator(Orientation.VERTICAL);

            HBox controlBar = new HBox(observationPositionPane, separator1, observationTimePane, separator2, timeLapsePane);
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
            // todo formatter
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

            observationPositionPane.getChildren().addAll(lonLabel, lonTextField, latLabel, latTextField);
            //todo il y a un ordre précis?

            lonTextFormatter.valueProperty().bind(observerLocationBean.lonDegProperty());
            latTextFormatter.valueProperty().bind(observerLocationBean.latDegProperty());


            //instant d'observation HBox
            observationTimePane.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

            //Date field
            Label dateLabel = new Label("Date :");
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

            ComboBox<ZoneId> timeZone = new ComboBox<>();
            timeZone.setStyle("-fx-pref-width: 180;");
            timeZone.valueProperty().bind(dateTimeBean.zoneProperty());
            Set<String> allTimeZone = new TreeSet<>();
            allTimeZone = Set.copyOf(ZoneId.getAvailableZoneIds());

            for ( String s : allTimeZone)
                timeZone.setAccessibleText(s);
            timeZone.disableProperty().bind(timeAnimator.isRunning());

            observationPositionPane.getChildren().addAll(dateLabel,datePicker,hourLabel,hourTextField,timeZone);

            mainPane.setTop(observationPositionPane);

            //time lapse pane

            timeLapsePane.setStyle("-fx-spacing: inherit;");
            ChoiceBox<NamedTimeAccelerator> timeAcceleratorChoiceBox = new ChoiceBox<>();
            timeAcceleratorChoiceBox.setItems(FXCollections.observableArrayList(NamedTimeAccelerator.values()));


           // timeAcceleratorChoiceBox.valueProperty().bind(Bindings.select(timeAnimator.acceleratorProperty(), "name"));
            timeAcceleratorChoiceBox.setValue(NamedTimeAccelerator.TIMES_300);
            timeAnimator.acceleratorProperty().bind(Bindings.select(timeAcceleratorChoiceBox.valueProperty(), "accelerator"));

            try (InputStream fontStream = getClass()
                    .getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf")) {

                Font fontAwesome = Font.loadFont(fontStream, 15);

                String reset = "\uf0e2";
                Button resetButton = new Button(reset);
                resetButton.setFont(fontAwesome);

                String pause = "\uf04b";
                String play = "\uf04c";
                Button playPauseButton = new Button();
                playPauseButton.;
                playPauseButton.setFont(fontAwesome);
            }




            //todo gérer pause/play

            // Ciel
            SkyCanvasManager canvasManager = new SkyCanvasManager(
                    catalogue,
                    dateTimeBean,
                    observerLocationBean,
                    viewingParametersBean);

            Canvas sky = canvasManager.canvas();
            Pane skyPane = new Pane(sky);

            sky.widthProperty().bind(skyPane.widthProperty());
            sky.heightProperty().bind(skyPane.heightProperty());

            mainPane.setCenter(skyPane);

            // Barre d'information

            canvasManager.objectUnderMouseProperty().addListener(
                    (p, o, n) -> {if (n != null) System.out.println(n);});

            Text fieldOfViewText = new Text();
            fieldOfViewText.textProperty().bind(Bindings.format("Champ de vue : %.2f °",viewingParametersBean.fieldOfViewDegProperty()));

            Text mousePositionText = new Text();
            mousePositionText.textProperty().bind(Bindings.format("Azimut : %.2f°, hauteur : %.2f°"
                    ,canvasManager.mouseAzDegProperty(),canvasManager.mouseAltDegProperty()));


            Text objectClosesToText = new Text();
            objectClosesToText.textProperty().bind(Bindings.createStringBinding(() -> (canvasManager.getObjectUnderMouse() == null) ? "" : canvasManager.getObjectUnderMouse().info()));

            BorderPane informationPane = new BorderPane(objectClosesToText,null,mousePositionText,null,fieldOfViewText);

            informationPane.setStyle("-fx-padding: 4; -fx-background-color: white;");


            mainPane.setBottom(informationPane);

            stage.setScene(new Scene(mainPane));
            stage.show();
            sky.requestFocus();
        }

    }


}
