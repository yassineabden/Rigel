package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Transform;

public final class SkyCanvasPainter {

    private final Canvas canvas;
    private final GraphicsContext graphicsContext;


    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        graphicsContext = canvas.getGraphicsContext2D(); }


    public void clear(){
        graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getWidth()); }

    public void drawStars(ObservedSky sky, StereographicProjection stereographicProjection, Transform transform) {

    }













}
