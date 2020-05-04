package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class DateTimeBean {

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> time = new SimpleObjectProperty<>();
    private final ObjectProperty<ZoneId> zone = new SimpleObjectProperty<>();
    private ZonedDateTime zonedDateTime = ZonedDateTime.of(getDate(),getTime(),getZone());

    public ObjectProperty<LocalDate> dateProperty(){ return date; }

    public LocalDate getDate(){ return date.get(); }

    public void setDate( LocalDate newLocalDate){ date.setValue(newLocalDate); }

    public ObjectProperty<LocalTime> timeProperty(){ return time; }

    public LocalTime getTime(){ return time.get(); }

    public void setTime ( LocalTime newLocalTime) { time.setValue(newLocalTime); }

    public ObjectProperty<ZoneId> zoneProperty() { return zone; }

    public ZoneId getZone(){ return zone.get(); }

    public void setZone(ZoneId newZoneId){  zone.setValue(newZoneId); }

    public ZonedDateTime getZonedDateTime(){ return zonedDateTime; }

    public void setZonedDateTime(ZonedDateTime zdt){ this.zonedDateTime = zdt; }

































}
