package org.itevents.controller;

import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itevents.model.Event;
import org.itevents.model.User;
import org.itevents.model.VisitLog;
import org.itevents.mybatis.mapper.UserMapper;
import org.itevents.service.EventService;
import org.itevents.service.VisitLogService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.Random;


@RestController
@Api(value = "visits", description = "Visit log")
public class VisitLogRestController {

    private static final Logger logger = LogManager.getLogger();

    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    private EventService eventService = context.getBean("eventService", EventService.class);
    private VisitLogService visitLogService = context.getBean("visitLogService", VisitLogService.class);
    private UserMapper userMapper = context.getBean("userMapper", UserMapper.class);

    @RequestMapping(method = RequestMethod.GET, value = "/events/{event_id}/register")
    public ResponseEntity getRegLink(@PathVariable("event_id") int eventId) {
        Event event = eventService.getEvent(eventId);
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setLocation(new URL(event.getRegLink()).toURI());
        } catch (Exception e) {
            logger.error("Exception :", e);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        User user = getUserFromSession();
        visitLogService.addVisitLog(new VisitLog(event, user));
        return new ResponseEntity(headers, HttpStatus.FOUND);
    }

    private User getUserFromSession() {
        return userMapper.getUser(new Random().nextInt(3) + 1);
    }
}