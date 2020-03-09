package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;
/**
 * Une Lune
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public class Moon extends CelestialObject {

    private final float phase;
    private final ClosedInterval INTERVAL_PHASE= ClosedInterval.of(0,1);


    /**
     * Unique constructeur visible dans le package.
     *
     * @param equatorialPos Position de l'objet en cordonées equatoriales
     * @param angularSize   taille angulaire
     * @param magnitude     magnitude
     * @param phase phase donnée

     * @throws IllegalArgumentException si la taille angulaire est négative et si la phase n'appartiemt pas à l'intervalle (0,1)
     * @throws NullPointerException     si le nom ou la position equatoriale sont nuls
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super("Lune",equatorialPos,angularSize,magnitude);
        this.phase = phase;
        Preconditions.checkInInterval(INTERVAL_PHASE,phase); }

    /**
     * La classe Moon redéfinit la méthode info pour que la phase apparaisse après le nom, entre parenthèses et exprimé en pourcent, avec une décimale.
     * @return une chaine de caractères.
     */
    @Override
    public String info(){
        return String.format(Locale.ROOT,"Lune (%.1f%%)",phase*100);

    }










}
