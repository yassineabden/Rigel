package ch.epfl.rigel.astronomy;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;
public class MyStarTest {

    @Test
    void isStarConstructorValidWithValidValues() {
        Star star = new Star(0, "Rigel", EquatorialCoordinates.of(0.5, -0.2), (float) 3.2, -0.03f);
        assertEquals(0, star.hipparcosId());
        assertEquals("Rigel", star.name());
        assertEquals(EquatorialCoordinates.of(0.5, -0.2).toString(), star.equatorialPos().toString());
        assertEquals(3.2, star.magnitude(), 1e-4);
        assertEquals(10515, star.colorTemperature());
        Star star2 = new Star(27989, "Betelgeuse", EquatorialCoordinates.of(0, 0), 0, 1.50f);
        assertEquals(27989, star2.hipparcosId());
        assertEquals("Betelgeuse", star2.name());
        assertEquals(EquatorialCoordinates.of(0, 0).toString(), star2.equatorialPos().toString());
        assertEquals(0,star2.magnitude(),1e-4);
        assertEquals(3793, star2.colorTemperature()); }

    @Test
    void isStarConstructorValidWithInvalidValues(){
        Star star = new Star(0, "Rigel", EquatorialCoordinates.of(0.5, -0.2), (float) 3.2, -0.03f);
        assertEquals(0,star.angularSize());
        assertThrows(IllegalArgumentException.class, () -> {
            new Star(-1, "Rigel", EquatorialCoordinates.of(0.5, -0.2), (float) 3.2, -0.03f);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Star(0, "Rigel", EquatorialCoordinates.of(0.5, -0.2), (float) 3.2, -0.6f);
        }); }






}