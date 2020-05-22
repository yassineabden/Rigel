package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
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

import java.io.InputStream;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;


public final class Main extends Application {

    private final static GeographicCoordinates INITIAL_POSITION = GeographicCoordinates.ofDeg(6.57, 46.52);
    private final static HorizontalCoordinates INITIAL_OBSERVATION = HorizontalCoordinates.ofDeg(180.000000000001, 15);
    private final static double INITIAL_FIELD_OF_VIEW = 100;
    private final static double MIN_WIDTH = 800;
    private final static double MIN_HEIGHT = 600;
    private final static String TITLE = "Rigel";


    // Constructeur de la classe qui permt d'initialiser tous les flots
    /*private Main() throws Exception {
        try (InputStream hs = resourceStream("/hygdata_v3.csv");
             InputStream as = resourceStream("/asterisms.txt");
             InputStream fontStream = resourceStream("/Font Awesome 5 Free-Solid-900.otf")) {

            catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(as, AsterismLoader.INSTANCE)
                    .build();
            fontAwesome = Font.loadFont(fontStream, 15);
        }
    }
*/

    /**
     * Méthode main de la classe
     *
     * @param args lignes de commandes java
     */
    public static void main(String[] args) {
        launch(args);
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

    /**
     * Démarre l'application
     *
     * @param stage l'étape principale de cette application, sur laquelle la scène d'application va être définie
     */
    @Override
    public void start(Stage stage) throws Exception {
        try (InputStream hs = resourceStream("/hygdata_v3.csv");
             InputStream as = resourceStream("/asterisms.txt")) {


            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(as, AsterismLoader.INSTANCE)
                    .build();

            //Initialisation des beans
            ObserverLocationBean observerLocationBean =
                    new ObserverLocationBean();
            observerLocationBean.setCoordinates(INITIAL_POSITION);

            DateTimeBean dateTimeBean = new DateTimeBean();
            dateTimeBean.setZonedDateTime(ZonedDateTime.now());

            ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
            viewingParametersBean.setCenter(INITIAL_OBSERVATION);
            viewingParametersBean.setFieldOfViewDeg(INITIAL_FIELD_OF_VIEW);

            TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);
            timeAnimator.acceleratorProperty().set(NamedTimeAccelerator.TIMES_300.getAccelerator());

            // Fenêtre principale
            BorderPane mainPane = new BorderPane();
            stage.setMinHeight(MIN_HEIGHT);
            stage.setTitle(TITLE);
            stage.setMinWidth(MIN_WIDTH);

            // Barre de contrôle
            HBox controlBar = new HBox(observationPosition(observerLocationBean), new Separator(Orientation.VERTICAL),
                    observationTime(dateTimeBean, timeAnimator), new Separator(Orientation.VERTICAL),
                    timeLapsePane(timeAnimator, dateTimeBean));

            controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");
            mainPane.setTop(controlBar);

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
            mainPane.setBottom(informationPane(canvasManager, viewingParametersBean));

            stage.setScene(new Scene(mainPane));
            stage.show();
            sky.requestFocus();
        }
    }

    // Sous-panneau permettant de régler la position d'observation
    private HBox observationPosition(ObserverLocationBean observerLocationBean) {

        HBox observationPositionPane = new HBox();
        observationPositionPane.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        NumberStringConverter stringToNumberConverter =
                new NumberStringConverter("#0.00");

        Label lonLabel = new Label("Longitude (°) :");
        TextField lonTextField = positionTextField(CoordinatesType.LONGITUDE, stringToNumberConverter, observerLocationBean);
        Label latLabel = new Label("Latitude (°) :");
        TextField latTextField = positionTextField(CoordinatesType.LATITUDE, stringToNumberConverter, observerLocationBean);

        observationPositionPane.getChildren().addAll(lonLabel, lonTextField, latLabel, latTextField);

        return observationPositionPane;
    }

    // Sous-panneau permettant de régler l'instant d'observation
    private HBox observationTime(DateTimeBean dateTimeBean, TimeAnimator timeAnimator) {

        HBox observationTimePane = new HBox();
        observationTimePane.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        //Date
        Label dateLabel = new Label("Date :");
        DatePicker datePicker = new DatePicker(dateTimeBean.getDate());
        datePicker.setStyle("-fx-pref-width: 120;");
        datePicker.valueProperty().bindBidirectional(dateTimeBean.dateProperty());
        datePicker.disableProperty().bind(timeAnimator.isRunning());

        //Temps
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

        //Zone
        Set<String> stringZoneId = new TreeSet<>(ZoneId.getAvailableZoneIds());
        List<ZoneId> zoneIdList = new ArrayList<>();

        for (String s : stringZoneId)
            zoneIdList.add(ZoneId.of(s));

        ComboBox<ZoneId> timeZone = new ComboBox<>(FXCollections.observableArrayList(List.copyOf(zoneIdList)));
        timeZone.setStyle("-fx-pref-width: 180;");
        timeZone.getSelectionModel().select(dateTimeBean.getZone());
        timeZone.valueProperty().bindBidirectional(dateTimeBean.zoneProperty());
        timeZone.disableProperty().bind(timeAnimator.isRunning());

        observationTimePane.getChildren().addAll(dateLabel, datePicker, hourLabel, hourTextField, timeZone);

        return observationTimePane;

    }

    // Sous-panneau permettant de régler l'ecoulement du temps
    private HBox timeLapsePane(TimeAnimator timeAnimator, DateTimeBean dateTimeBean) throws Exception {

        HBox timeLapsePane = new HBox();
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

            resetButton.setOnAction(e -> dateTimeBean.setZonedDateTime(ZonedDateTime.now()));
            resetButton.disableProperty().bind(timeAnimator.isRunning());

            String play = "\uf04b";
            String pause = "\uf04c";
            playPauseButton.setText(play);

            playPauseButton.setFont(fontAwesome);
            playPauseButton.setText(play);
            playPauseButton.setOnAction(e -> playPauseButton.setText((playPauseButton.getText().equals(play)) ? pause : play));
        }
        playPauseButton.textProperty().addListener((p, o, n) -> {
                    if (timeAnimator.isRunning().get()) timeAnimator.stop();
                    else timeAnimator.start();
                }
        );
        timeLapsePane.getChildren().addAll(timeAcceleratorChoiceBox, resetButton, playPauseButton);


        return timeLapsePane;
    }

    //Barre d'information
    private BorderPane informationPane(SkyCanvasManager canvasManager, ViewingParametersBean viewingParametersBean) {
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

    //Retourne des champs de texte en leur attachant un formateur de texte
    private TextField positionTextField(CoordinatesType coordinatesType, NumberStringConverter stringToNumberConverter, ObserverLocationBean observerLocationBean) {

        TextField textField = new TextField();
        textField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        UnaryOperator<TextFormatter.Change> filter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newCoord =
                        stringToNumberConverter.fromString(newText).doubleValue();

                if (coordinatesType == CoordinatesType.LONGITUDE) {
                    return checkCoordinates(GeographicCoordinates::isValidLonDeg, newCoord, change);
                } else {
                    return checkCoordinates(GeographicCoordinates::isValidLatDeg, newCoord, change);
                }
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> textFormatter = (coordinatesType == CoordinatesType.LONGITUDE) ?
                new TextFormatter<>(stringToNumberConverter, observerLocationBean.getLonDeg(), filter)
                : new TextFormatter<>(stringToNumberConverter, observerLocationBean.getLatDeg(), filter);

        textField.setTextFormatter(textFormatter);

        if (coordinatesType == CoordinatesType.LONGITUDE)
            observerLocationBean.lonDegProperty().bindBidirectional(textFormatter.valueProperty());
        else observerLocationBean.latDegProperty().bindBidirectional(textFormatter.valueProperty());

        return textField;

    }

    //Contrôle si la coordonnée est valide (c-à-d nombre à deux décimales et compris dans les bons intervalles)
    private TextFormatter.Change checkCoordinates(Predicate<Double> predicate, double coordinates, TextFormatter.Change change) {

        return predicate.test(coordinates) ? change : null;
    }


    private enum CoordinatesType {
        LATITUDE, LONGITUDE;

    }

}
