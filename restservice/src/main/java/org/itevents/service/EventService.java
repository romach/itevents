package org.itevents.service;

import org.itevents.model.Event;
import org.itevents.parameter.FilteredEventsParameter;

import java.util.List;

public interface EventService {

    void addEvent(Event event);

    Event getEvent(int id);

    List<Event> getAllEvents();

    List<Event> getEventsInRadius(Location location, int radius);

    void removeEvent(int id);

    Event getFutureEventById(int days, int id);

    List<Event> getFilteredEvents(FilteredEventsParameter params);
}
