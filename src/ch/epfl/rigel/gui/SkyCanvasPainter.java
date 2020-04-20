package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import java.awt.geom.Point2D;

import static javafx.scene.input.KeyCode.T;

public final class SkyCanvasPainter {

    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private final  Transform transformation;


    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        graphicsContext = canvas.getGraphicsContext2D();
        transformation = Transform.affine(1300, 0,0,-1300,- (canvas.getWidth()/2), - (canvas.getHeight()/2));
    }


    public void clear(){
        graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getWidth()); }

    public void drawStars(ObservedSky sky, StereographicProjection stereographicProjection, Transform transform) {
    }
    public void drawHorizon (StereographicProjection stereographicProjection, Transform transform){
        HorizontalCoordinates equator = HorizontalCoordinates.ofDeg(180,0);
        double equatorRadius = stereographicProjection.circleRadiusForParallel(equator);
        CartesianCoordinates eqCoord = stereographicProjection.circleCenterForParallel(equator);

        graphicsContext.setFill(Color.RED);
        graphicsContext.setLineWidth(2.0);
        graphicsContext.lineTo();


    }











}
