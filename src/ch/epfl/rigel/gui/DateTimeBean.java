package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public final class DateTimeBean {

    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private ObjectProperty<LocalTime> time = new SimpleObjectProperty<>();
    private ObjectProperty<ZoneId> zone = new SimpleObjectProperty<>();
    //TODO constructeur?
    /**
     * Retourne la propriété de la date
     *
     * @return la propriété de la date
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    /**
     * Retourne le contenu de la propriété de la date
     *
     * @return le contenu de la propriété de la date
     */
    public LocalDate getDate() {
        return date.get();
    }

    /**
     * Modifie le contenu de la propriéré de la date
     *
     * @param newLocalDate une nouvelle date
     */
    public void setDate(LocalDate newLocalDate) {
        date.setValue(newLocalDate);
    }

    /**
     * Retourne la propriété de la propriété du temps
     *
     * @return la propriété de la propriété du temps
     */
    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    /**
     * Retourne le contenu de la propriété du temps
     *
     * @return le contenu de la propriété du temps
     */
    public LocalTime getTime() {
        return time.get();
    }

    /**
     * Modifie le contenu de la propriéré du temps
     *
     * @param newLocalTime un nouveau temps
     */
    public void setTime(LocalTime newLocalTime) {
        time.setValue(newLocalTime);
    }

    /**
     * Retourne la propriété de la zone
     *
     * @return la propriété de la zone
     */
    public ObjectProperty<ZoneId> zoneProperty() {
        return zone;
    }

    /**
     * Retourne le contenu de la propriété de la zone
     *
     * @return le contenu de la propriété de la zone
     */
    public ZoneId getZone() {
        return zone.get();
    }

    /**
     * Modifie  le contenu de la propriéré de la zone
     *
     * @param newZoneId un nouveau temps
     */
    public void setZone(ZoneId newZoneId) {
        zone.setValue(newZoneId);
    }

    /**
     * Retourne l'instant d'observation
     *
     * @return l'instant d'observation
     */
    public ZonedDateTime getZonedDateTime() {

        return ZonedDateTime.of(getDate(), getTime(), getZone());
    }

    /**
     * Modifie l'instant d'observation
     *
     * @param zdt nouveau instant d'observation
     */
    public void setZonedDateTime(ZonedDateTime zdt) {
        setDate(zdt.toLocalDate());
        setTime(zdt.toLocalTime());
        setZone(zdt.getZone());
    }
}
