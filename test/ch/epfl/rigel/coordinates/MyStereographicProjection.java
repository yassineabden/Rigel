package ch.epfl.rigel.coordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class MyStereographicProjection {

    @Test
    void applyWorksOnKnownValues(){
        StereographicProjection s = new StereographicProjection(HorizontalCoordinates.of(Angle.ofHr(15), 0));
        assertEquals(CartesianCoordinates.of(8433.99, -18.56).toString(),s.apply(HorizontalCoordinates.of(Angle.ofHr(14.84), Angle.ofDMS(74,9,20))).toString()); }





}
