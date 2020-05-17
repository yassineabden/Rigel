package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public final class DateTimeBean {

    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private ObjectProperty<LocalTime> time = new SimpleObjectProperty<>();
    private ObjectProperty<ZoneId> zone = new SimpleObjectProperty<>();

    /**
     * @return
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    /**
     * @return
     */
    public LocalDate getDate() {
        return date.get();
    }

    /**
     * @param newLocalDate
     */
    public void setDate(LocalDate newLocalDate) {
        date.setValue(newLocalDate);
    }

    /**
     * @return
     */
    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    /**
     * @return
     */
    public LocalTime getTime() {
        return time.get();
    }

    /**
     * @param newLocalTime
     */
    public void setTime(LocalTime newLocalTime) {
        time.setValue(newLocalTime);
    }

    /**
     * @return
     */
    public ObjectProperty<ZoneId> zoneProperty() {
        return zone;
    }

    /**
     * @return
     */
    public ZoneId getZone() {
        return zone.get();
    }

    /**
     * @param newZoneId
     */
    public void setZone(ZoneId newZoneId) {
        zone.setValue(newZoneId);
    }

    /**
     * @return
     */
    public ZonedDateTime getZonedDateTime() {
        //return zonedDateTime;
        return ZonedDateTime.of(getDate(), getTime(), getZone());
    }

    /**
     * @param zdt
     */
    public void setZonedDateTime(ZonedDateTime zdt) {
        setDate(zdt.toLocalDate());
        setTime(zdt.toLocalTime());
        setZone(zdt.getZone());
    }
}
