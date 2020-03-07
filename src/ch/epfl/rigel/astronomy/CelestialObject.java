package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;
/**
 * Un objet céleste
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */


public abstract class CelestialObject {
    private final String name;
    private final EquatorialCoordinates equatorielPos;
    private final float angularSize;
    private final float magnitude;

    /**
     * Unique constructeur visible dans le package.
     * @param name nom de l'objet céleste
     * @param equatorialPos Position de l'objet en cordonées equatoriales
     * @param angularSize taille angulaire
     * @param magnitude magnitude
     * @throws IllegalArgumentException si la taille angulaire est négative
     * @throws NullPointerException si le nom ou la position equatoriale sont nuls
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude){

        Preconditions.checkArgument(angularSize>0);
        Objects.requireNonNull(name);
        Objects.requireNonNull(equatorialPos);
        this.name= name;
        this.equatorielPos=equatorialPos;
        this.angularSize= angularSize;
        this.magnitude= magnitude; }

    /**
     * Retourne le nom de l'objet céleste
     * @return le nom de l'objet céleste
     */
    public String name(){
        return name; }

    /**
     * Retourne la taille angulaire
     * @return la taille angulaire
     */
    public double angularSize(){
        return angularSize; }

    /**
     * Retourne la magnitude
     * @return la magnitude
     */
    public double magnitude(){
        return magnitude; }

    /**
     * Retourne les coordonnées equatoriales
     * @return les coordonnées equatoriales
     */
    public EquatorialCoordinates EquatorielPos(){
        return equatorielPos; }

    /**
     * Retourne un (court) texte informatif au sujet de l'objet, destiné à être montré à l'utilisateur.
     * @return  un (court) texte informatif au sujet de l'objet, destiné à être montré à l'utilisateur.
     */

    public String info(){
        return name;
    }

    /**
     * Redéfinit la méthode toString afin qu'elle retourne la même chose que la méthode info.
     * @return la même chose que la méthode info
     */

    @Override
    public String toString(){
        return info();
    }






















}
