package org.itevents.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itevents.dao.EventDao;
import org.itevents.model.Event;
import org.itevents.model.Location;
import org.itevents.model.Filter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("eventService")
@Transactional
public class EventServiceImpl implements EventService {

    private static final Logger logger = LogManager.getLogger();

    @Inject
    private EventDao eventDao;

    @Override
    public void addEvent(Event event) {
        eventDao.addEvent(event);
    }

    @Override
    public Event getEvent(int id) {
        return eventDao.getEvent(id);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventDao.getAllEvents();
    }

    @Override
    public List<Event> getEventsInRadius(Location location, int radius) {
        return eventDao.getEventsInRadius(location, radius);
    }

    @Override
    public Event removeEvent(Event event) {
        Event deletingEvent = eventDao.getEvent(event.getId());
        if (deletingEvent != null) {
            eventDao.removeEvent(event);
        }
        return deletingEvent;
    }

    @Override
    public List<Event> getFilteredEvents(Filter params) {
        List<Event> result;
        try {
            result = eventDao.getFilteredEvents(params);
        } catch (Exception e) {
            logger.error("Exception :", e);
            result = new ArrayList<>();
        }
        return result;
    }

    @Override
    public List<Event> getFutureFilteredEvents(Filter params, Date date) {
        List<Event> result;
        try {
            result = eventDao.getFutureFilteredEvents(params, date);
        } catch (Exception e) {
            logger.error("Exception :", e);
            result = new ArrayList<>();
        }
        return result;
    }
}