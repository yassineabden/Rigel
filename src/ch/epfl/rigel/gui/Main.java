package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Group;
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

import java.io.IOException;
import java.io.InputStream;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
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
        try (InputStream hs = resourceStream("/hygdata_v3.csv");
                InputStream as = resourceStream("/asterisms.txt")) {

            StarCatalogue catalogue = new StarCatalogue
                    .Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(as, AsterismLoader.INSTANCE)
                    .build();

            ObserverLocationBean observerLocationBean =
                    new ObserverLocationBean();
            observerLocationBean.setCoordinates(
                    GeographicCoordinates.ofDeg(6.57, 46.52));


            DateTimeBean dateTimeBean = new DateTimeBean();
            dateTimeBean.setZonedDateTime(ZonedDateTime.now());


            ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
            viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(180.000000000001, 15));
            viewingParametersBean.setFieldOfViewDeg(100);

            TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);
            timeAnimator.acceleratorProperty().set(NamedTimeAccelerator.TIMES_300.getAccelerator());

            // Fenêtre principale
            BorderPane mainPane = new BorderPane();
            stage.setMinHeight(600);
            stage.setTitle("Rigel");
            stage.setMinWidth(800);

            // Barre de contrôle
            mainPane.setTop(controlPane(observerLocationBean, dateTimeBean, timeAnimator));

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
            mainPane.setBottom(informationPane(canvasManager,viewingParametersBean));

            stage.setScene(new Scene(mainPane));
            stage.show();
            sky.requestFocus();
        }

    }

    private HBox controlPane (ObserverLocationBean observerLocationBean, DateTimeBean dateTimeBean,TimeAnimator timeAnimator ) throws IOException {

        HBox observationPositionPane = new HBox();
        HBox observationTimePane = new HBox();
        HBox timeLapsePane = new HBox();

        //observationPositionPane
        observationPositionPane.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        NumberStringConverter stringToNumberConverter =
                new NumberStringConverter("#0.00");

        Label lonLabel = new Label("Longitude (°) :");
        TextField lonTextField = positionTextField(CoordinatesType.LONGITUDE, stringToNumberConverter,observerLocationBean);
        Label latLabel = new Label("Latitude (°) :");
        TextField latTextField = positionTextField(CoordinatesType.LATITUDE, stringToNumberConverter, observerLocationBean);
        observationPositionPane.getChildren().addAll(lonLabel,lonTextField,latLabel, latTextField);

        //instant d'observation HBox
        observationTimePane.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        //Date field
        Label dateLabel = new Label("Date :");
        DatePicker datePicker = new DatePicker(dateTimeBean.getDate());
        datePicker.setStyle("-fx-pref-width: 120;");

        dateTimeBean.dateProperty().bindBidirectional(datePicker.valueProperty());
        //todo pourquoi on peut pas faire ça mais avec l'autre oui?

        datePicker.disableProperty().bind(timeAnimator.isRunning());


        // Hour Field
        DateTimeFormatter hmsFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");
        Label hourLabel = new Label("Heure :");

        TextField hourTextField = new TextField();
        hourTextField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");

        LocalTimeStringConverter stringConverter =
                new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter =
                new TextFormatter<>(stringConverter);

        hourTextField.setTextFormatter(timeFormatter);
        timeFormatter.setValue(LocalTime.now());
        timeFormatter.valueProperty().bindBidirectional(dateTimeBean.timeProperty());

        hourTextField.disableProperty().bind(timeAnimator.isRunning());

        Set <String> stringZoneId = new TreeSet<>(ZoneId.getAvailableZoneIds());

        List <ZoneId> zoneIdList = new ArrayList<>();

        for (String s: stringZoneId)
            zoneIdList.add(ZoneId.of(s));


        ComboBox <ZoneId> timeZone = new ComboBox<>(FXCollections.observableArrayList(List.copyOf(zoneIdList)));
        timeZone.setStyle("-fx-pref-width: 180;");

        timeZone.getSelectionModel().select(dateTimeBean.getZone());
        timeZone.valueProperty().bindBidirectional(dateTimeBean.zoneProperty());
        timeZone.disableProperty().bind(timeAnimator.isRunning());


        observationTimePane.getChildren().addAll(dateLabel,datePicker,hourLabel,hourTextField,timeZone);


        //time lapse pane
        timeLapsePane.setStyle("-fx-spacing: inherit;");
        ChoiceBox<NamedTimeAccelerator> timeAcceleratorChoiceBox = new ChoiceBox<>();
        timeAcceleratorChoiceBox.setItems(FXCollections.observableArrayList(NamedTimeAccelerator.values()));

        timeAcceleratorChoiceBox.setValue(NamedTimeAccelerator.TIMES_300);
        timeAnimator.acceleratorProperty().bind(Bindings.select(timeAcceleratorChoiceBox.valueProperty(), "accelerator"));

        timeAcceleratorChoiceBox.disableProperty().bind(timeAnimator.isRunning());

        String reset = "\uf0e2";
        Button resetButton = new Button(reset);
        Button playPauseButton = new Button();

        try (InputStream fontStream = resourceStream("/Font Awesome 5 Free-Solid-900.otf")) {

            Font fontAwesome = Font.loadFont(fontStream, 15);
            resetButton.setFont(fontAwesome);

            resetButton.setOnAction( e -> dateTimeBean.setZonedDateTime(ZonedDateTime.now()));
            resetButton.disableProperty().bind(timeAnimator.isRunning());

            String play = "\uf04b";
            String pause = "\uf04c";
            playPauseButton.setText(play);

            playPauseButton.setFont(fontAwesome);
            playPauseButton.setText(play);
            playPauseButton.setOnAction( e -> playPauseButton.setText((playPauseButton.getText().equals(play)) ? pause : play));
        }

        playPauseButton.textProperty().addListener( (p,o,n) -> {
                    if (timeAnimator.isRunning().get()) timeAnimator.stop();
                    else timeAnimator.start();
                }
        );
        timeLapsePane.getChildren().addAll(timeAcceleratorChoiceBox,resetButton,playPauseButton);

        HBox controlBar = new HBox(observationPositionPane,new Separator(Orientation.VERTICAL),observationTimePane,new Separator(Orientation.VERTICAL),timeLapsePane);
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        return controlBar;

    }

    private BorderPane informationPane( SkyCanvasManager canvasManager, ViewingParametersBean viewingParametersBean){
        canvasManager.objectUnderMouseProperty().addListener(
                (p, o, n) -> {
                    if (n != null) System.out.println(n);
                });

        Text fieldOfViewText = new Text();
        fieldOfViewText.textProperty().bind(Bindings.format("Champ de vue : %.2f °", viewingParametersBean.fieldOfViewDegProperty()));

        Text mousePositionText = new Text();
        mousePositionText.textProperty().bind(Bindings.format("Azimut : %.2f°, hauteur : %.2f°"
                , canvasManager.mouseAzDegProperty(), canvasManager.mouseAltDegProperty()));


        Text objectClosesToText = new Text();
        objectClosesToText.textProperty().bind(Bindings.createStringBinding(()
                -> canvasManager.getObjectUnderMouse() == null ? "" : canvasManager.getObjectUnderMouse().info(), canvasManager.objectUnderMouseProperty()));

        BorderPane informationPane = new BorderPane(objectClosesToText, null, mousePositionText, null, fieldOfViewText);
        informationPane.setStyle("-fx-padding: 4; -fx-background-color: white;");


        return informationPane;
    }

    private TextField positionTextField(CoordinatesType coordinatesType, NumberStringConverter stringToNumberConverter, ObserverLocationBean observerLocationBean){

        TextField textField = new TextField();
        textField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        UnaryOperator<TextFormatter.Change> filter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newCoord =
                        stringToNumberConverter.fromString(newText).doubleValue();
                if (coordinatesType == CoordinatesType.LONGITUDE) {
                    //todo demander à yass s'il se souvient du bail de l'assistant
                    return GeographicCoordinates.isValidLonDeg(newCoord)
                            ? change
                            : null;
                } else {
                    return GeographicCoordinates.isValidLatDeg(newCoord)
                            ? change
                            : null;
                }

            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> textFormatter = (coordinatesType == CoordinatesType.LONGITUDE) ?
                new TextFormatter<>(stringToNumberConverter, observerLocationBean.getLonDeg(), filter)
                :  new TextFormatter<>(stringToNumberConverter, observerLocationBean.getLatDeg(), filter);

        textField.setTextFormatter(textFormatter);

        if(coordinatesType == CoordinatesType.LONGITUDE) observerLocationBean.lonDegProperty().bindBidirectional(textFormatter.valueProperty());
        else observerLocationBean.latDegProperty().bindBidirectional(textFormatter.valueProperty());

        return textField;

    }
        //todo je suis pas sûr que faire une enum pour 2 cas soit très strat mais c'est plus joli
    private enum CoordinatesType {
        LATITUDE, LONGITUDE;

    }

}
