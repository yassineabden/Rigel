package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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


public final class MainWithName extends Application {


    private Font fontAwesome;

    // Valeurs initiales de la position d'observation et du champ de vue
    private final static GeographicCoordinates INITIAL_POSITION = GeographicCoordinates.ofDeg(6.57, 46.52);
    private final static HorizontalCoordinates INITIAL_OBSERVATION = HorizontalCoordinates.ofDeg(180.000000000001, 15);
    private final static double INITIAL_FIELD_OF_VIEW = 100;

    // Valeurs du canevas
    private final static double MIN_WIDTH = 800;
    private final static double MIN_HEIGHT = 600;
    private final static String TITLE = "Rigel";

    // Noms des différentes sources
    private final static String STARS_RESOURCES_FILE_NAME = "/hygdata_v3.csv";
    private final static String ASTERISMS_RESOURCES_FILE_NAME = "/constellation_lines_simplified.dat";
    private static final String FONT_AWESOME_FILE_NAME = "/Font Awesome 5 Free-Solid-900.otf";

    // Styles FX et séparateur utilisés pour les différents noeuds
    private static final String STYLE_BASELINE_RIGHT = "-fx-alignment: baseline-right;";
    private static final String STYLE_BASELINE_LEFT = "-fx-alignment: baseline-left;";
    private static final String STYLE_SPACING_INHERIT = "-fx-spacing: inherit;";
    private static final String STYLE_CONTROL_BAR = "-fx-spacing: 4; -fx-padding: 4;";
    private static final String STYLE_INFORMATION_PANE = "-fx-padding: 4; -fx-background-color: white;";
    private static final String STYLE_PREF_WIDHT = "-fx-pref-width:";
    private static final String STYLE_BOUTON_MENU = "-fx-background-color: \n" +
            "        #090a0c,\n" +
            "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
            "        linear-gradient(#20262b, #191d22),\n" +
            "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
            "    -fx-background-radius: 5,4,3,5;\n" +
            "    -fx-background-insets: 0,1,2,0;\n" +
            "    -fx-text-fill: white;\n" +
            "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
            "    -fx-font-family: \"Arial\";\n" +
            "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
            "    -fx-font-size: 25px;\n" +
            "    -fx-padding: 10 20 10 20;";

    //Propriétés du Menu
    private final static int HGAP_VGAP = 2;
    private final static int INSETS = 10;
    private final static String BACKGROUND_NAME = "sky.jpg";
    private final static String TITLE_MENU = "Rigel.";
    private final static int TITLE_SIZE = 100;
    private final static int BOUTON_SIZE = 50;
    private final static int COLUMN_INDEX = 125;

    //Propriétés de la fenêtre du bouton "En savoir plus"
    private final static int TEXT_X = 50;
    private final static int TEXT_Y = 200;
    private final static int SIZE_TEXT_BUTTON = 18;


    // Retourne l'input de la ressource en fonction de son nom
    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

    /**
     * Démarre l'application
     *
     * @param stage Fenêtre pricipale
     */

    @Override
    public void start(Stage stage)  {

        stage.setMinHeight(MIN_HEIGHT);
        stage.setTitle(TITLE);
        stage.setMinWidth(MIN_WIDTH);

        stage.setScene(startMenu(stage));
        stage.show();

    }

    //Affiche le ciel, la barre de contrôle ainsi que la barre d'information
    private Scene startSky(Stage stage) throws Exception {
        try (InputStream hs = resourceStream(STARS_RESOURCES_FILE_NAME);
             InputStream as = resourceStream(ASTERISMS_RESOURCES_FILE_NAME);
             InputStream fontStream = resourceStream(FONT_AWESOME_FILE_NAME)) {

            // Chargement des ressources
            fontAwesome = Font.loadFont(fontStream, 15);

            StarCatalogueWithName catalogue = new StarCatalogueWithName.Builder()
                    .loadFrom(hs, HygDatabaseWithNameLoader.INSTANCE)
                    .loadFrom(as, AsterismWithNameLoader.INSTANCE)
                    .build();

            //Initialisation des beans et de l'accélérateur de temps
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

            SkyCanvasManagerWithName canvasManager = new SkyCanvasManagerWithName(
                    catalogue,
                    dateTimeBean,
                    observerLocationBean,
                    viewingParametersBean);

            // Fenêtre principale
            BorderPane mainPane = new BorderPane();

            //Bouton "Retour"
            Button back = backButton();
            back.setOnAction(actionEvent -> stage.setScene(startMenu(stage)));

            // Barre de contrôle
            HBox controlBar = new HBox(back
                    , new Separator(Orientation.VERTICAL)
                    , observationPosition(observerLocationBean)
                    , new Separator(Orientation.VERTICAL)
                    , observationTime(dateTimeBean, timeAnimator)
                    , new Separator(Orientation.VERTICAL)
                    , asterismsButton(timeAnimator, canvasManager.drawAsterismsProperty())
                    , new Separator(Orientation.VERTICAL)
                    , timeLapsePane(timeAnimator, dateTimeBean));


            controlBar.setStyle(STYLE_CONTROL_BAR);
            mainPane.setTop(controlBar);

            // Ciel

            Canvas sky = canvasManager.canvas();
            Pane skyPane = new Pane(sky);

            sky.widthProperty().bind(skyPane.widthProperty());
            sky.heightProperty().bind(skyPane.heightProperty());

            mainPane.setCenter(skyPane);

            // Barre d'information
            mainPane.setBottom(informationPane(canvasManager, viewingParametersBean));

            // Affiche le programme

            sky.requestFocus();
            return new Scene(mainPane);
        }
    }

    //Bouton qui permet de retourner au menu
    private Button backButton() {

        Button button = new Button();
        button.setFont(fontAwesome);
        button.setText("Retour");


        return button;

    }

    // Sous-panneau permettant de régler la position d'observation
    private HBox observationPosition(ObserverLocationBean observerLocationBean) {

        HBox observationPositionPane = new HBox();
        observationPositionPane.setStyle(STYLE_SPACING_INHERIT + STYLE_BASELINE_LEFT);

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
        observationTimePane.setStyle(STYLE_SPACING_INHERIT + STYLE_BASELINE_LEFT);

        //Date
        Label dateLabel = new Label("Date :");
        DatePicker datePicker = new DatePicker(dateTimeBean.getDate());
        datePicker.setStyle(STYLE_PREF_WIDHT + " 120;");
        datePicker.valueProperty().bindBidirectional(dateTimeBean.dateProperty());
        datePicker.disableProperty().bind(timeAnimator.isRunning());

        //Temps
        DateTimeFormatter hmsFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");
        Label hourLabel = new Label("Heure :");
        TextField hourTextField = new TextField();
        hourTextField.setStyle(STYLE_PREF_WIDHT + " 75;" + STYLE_BASELINE_RIGHT);

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
        timeZone.setStyle(STYLE_PREF_WIDHT + " 180;");
        timeZone.getSelectionModel().select(dateTimeBean.getZone());
        timeZone.valueProperty().bindBidirectional(dateTimeBean.zoneProperty());
        timeZone.disableProperty().bind(timeAnimator.isRunning());

        observationTimePane.getChildren().addAll(dateLabel, datePicker, hourLabel, hourTextField, timeZone);

        return observationTimePane;

    }

    private Pane asterismsButton(TimeAnimator timeAnimator, BooleanProperty drawAsterisms) {
        Pane pane = new Pane();
        pane.setStyle(STYLE_SPACING_INHERIT);
        String asterismsOn = "Asterisms on";
        String asterismsOff = "Asterisms off";

        Button asterismsButton = new Button();
        asterismsButton.setText(asterismsOn);
        asterismsButton.setFont(fontAwesome);

        asterismsButton.setOnAction(a -> asterismsButton.setText(asterismsButton.getText().equals(asterismsOn) ? asterismsOff : asterismsOn));
        asterismsButton.disableProperty().bind(timeAnimator.isRunning());
        asterismsButton.textProperty().addListener((p, o, n) -> {
            if (drawAsterisms.get()) drawAsterisms.set(false);
            else drawAsterisms.set(true);
        });

        pane.getChildren().add(asterismsButton);
        return pane;
    }

    // Sous-panneau permettant de régler l'ecoulement du temps
    private HBox timeLapsePane(TimeAnimator timeAnimator, DateTimeBean dateTimeBean) {

        HBox timeLapsePane = new HBox();
        timeLapsePane.setStyle(STYLE_SPACING_INHERIT);

        // Accélérateur
        ChoiceBox<NamedTimeAccelerator> timeAcceleratorChoiceBox = new ChoiceBox<>();
        timeAcceleratorChoiceBox.setItems(FXCollections.observableArrayList(NamedTimeAccelerator.values()));
        timeAcceleratorChoiceBox.setValue(NamedTimeAccelerator.TIMES_300);
        timeAnimator.acceleratorProperty().bind(Bindings.select(timeAcceleratorChoiceBox.valueProperty(), "accelerator"));
        timeAcceleratorChoiceBox.disableProperty().bind(timeAnimator.isRunning());

        // Boutons reset, pause, play
        String reset = "\uf0e2";
        Button resetButton = new Button(reset);
        Button playPauseButton = new Button();

        resetButton.setFont(fontAwesome);
        resetButton.setOnAction(e -> dateTimeBean.setZonedDateTime(ZonedDateTime.now()));
        resetButton.disableProperty().bind(timeAnimator.isRunning());

        String play = "\uf04b";
        String pause = "\uf04c";
        playPauseButton.setText(play);

        playPauseButton.setFont(fontAwesome);
        playPauseButton.setText(play);
        playPauseButton.setOnAction(e -> playPauseButton.setText((playPauseButton.getText().equals(play)) ? pause : play));

        playPauseButton.textProperty().addListener((p, o, n) -> {
                    if (timeAnimator.isRunning().get()) timeAnimator.stop();
                    else timeAnimator.start();
                }
        );

        timeLapsePane.getChildren().addAll(timeAcceleratorChoiceBox, resetButton, playPauseButton);


        return timeLapsePane;
    }

    //Barre d'information
    private BorderPane informationPane(SkyCanvasManagerWithName canvasManager, ViewingParametersBean viewingParametersBean) {

        // Champ de vue
        Text fieldOfViewText = new Text();
        fieldOfViewText.textProperty().bind(Bindings.format("Champ de vue : %.2f °", viewingParametersBean.fieldOfViewDegProperty()));

        // Coordonnées de la souris
        Text mousePositionText = new Text();
        mousePositionText.textProperty().bind(Bindings.format("Azimut : %.2f°, hauteur : %.2f°"
                , canvasManager.mouseAzDegProperty(), canvasManager.mouseAltDegProperty()));

        // Objet céleste sous la souris
        Text objectClosesToText = new Text();
        objectClosesToText.textProperty().bind(Bindings.createStringBinding(()
                        -> canvasManager.getObjectUnderMouse() == null ? "" : canvasManager.getObjectUnderMouse().info()
                , canvasManager.objectUnderMouseProperty()));

        BorderPane informationPane = new BorderPane(objectClosesToText, null, mousePositionText, null, fieldOfViewText);
        informationPane.setStyle(STYLE_INFORMATION_PANE);

        return informationPane;
    }

    //Retourne des champs de texte en leur attachant un formateur de texte
    private TextField positionTextField(CoordinatesType coordinatesType, NumberStringConverter stringToNumberConverter, ObserverLocationBean observerLocationBean) {

        TextField textField = new TextField();
        textField.setStyle(STYLE_PREF_WIDHT + " 60;" + STYLE_BASELINE_RIGHT);

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


    // Enum représentant les types de coordonnées
    private enum CoordinatesType {
        LATITUDE, LONGITUDE;
    }

    //Affiche le Menu
    private Scene startMenu(Stage stage) {

        //Fenêtre principale
        GridPane mainPane = new GridPane();
        mainPane.setHgap(HGAP_VGAP);
        mainPane.setVgap(HGAP_VGAP);
        mainPane.setPadding(new Insets(INSETS));

        // Fond d'écran
        Image image = new Image(BACKGROUND_NAME);
        BackgroundImage backgroundImage = new BackgroundImage(image, null, null, null, null);

        mainPane.setBackground(new Background(backgroundImage));

        // Titre
        String familyTitle = "Phosphate";
        double sizeTitle = TITLE_SIZE;
        Text textTitle = new Text(TITLE_MENU);
        textTitle.setFont(Font.font(familyTitle, sizeTitle));
        textTitle.setFill(Color.web("#D17008"));

        mainPane.add(textTitle, COLUMN_INDEX, 15);

        //Premier bouton
        Button button = menuButton("Observer le ciel");
        button.setOnAction(actionEvent -> {
            try {
                stage.setScene(startSky(stage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mainPane.add(button, COLUMN_INDEX, 30);

        //Deuxième bouton
        Button button2 = menuButton("En savoir plus...");
        button2.setOnAction(actionEvent -> stage.setScene(showTextButton2(stage)));
        mainPane.add(button2, COLUMN_INDEX, 40);

        return new Scene(mainPane);
    }

    //Affiche un texte une fois que l'utilisateur a appuyé sur le bouton "En savoir plus"
    private Scene showTextButton2(Stage stage) {

        //Fenêtre principale
        Pane pane = new Pane();

        //Fond d'écran
        Image image = new Image(BACKGROUND_NAME);
        BackgroundImage backgroundImage = new BackgroundImage(image, null, null, null, null);
        pane.setBackground(new Background(backgroundImage));

        //Texte affiché
        Text text = new Text("Ce travail a été réalisé dans " +
                "le cadre d’un cours donné à l’EPFL" +
                " par Yassine Abdennadher et Juliette Aerni," + "\n" +
                "deux  programmeurs qui ne sont qu’à" +
                " leur début dans le monde de la programmation. " + "\n" +
                "On espère que vous allez profiter de leur magnifique travail. " +
                "Hésitez pas à leur envoyer votre avis aux adresses mail: " + "\n" +
                "yassine.abdennadher@epfl.ch ou juliette.aerni@epfl.ch.");
        text.setX(TEXT_X);
        text.setY(TEXT_Y);
        double size = SIZE_TEXT_BUTTON;
        String family = "Rockwell";
        text.setFont(Font.font(family, size));
        text.setFill(Color.YELLOW);

        //Bouton retour
        Button buttonBack = menuButton("Retour");
        buttonBack.setOnAction(actionEvent -> stage.setScene(startMenu(stage)));

        pane.getChildren().addAll(buttonBack, text);

        return new Scene(pane);
    }

    // Initialise un bouton du menu
    private Button menuButton(String name) {

        Text textback = new Text(name);
        double sizetext = BOUTON_SIZE;
        String familyButton = "Rockwell";
        textback.setFont(Font.font(familyButton, sizetext));
        Button but = new Button(textback.getText());
        but.setStyle(STYLE_BOUTON_MENU);

        return but;
    }
}