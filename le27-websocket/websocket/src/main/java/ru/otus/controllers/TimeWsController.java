package ru.otus.controllers;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.otus.ApplConfig.DATE_TIME_FORMAT;

/*Это уже не REST Controller, а просто Controller*/
@Controller
public class TimeWsController {

    //Мы в этот контроллер инжектим SimpMessagingTemplate и через него собираемяс отправлять сообщения
    private final SimpMessagingTemplate template;

    public TimeWsController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Scheduled(fixedDelay = 1000)
    public void broadcastCurrentTime() {
        template.convertAndSend(
                "/topic/currentTime", //Мы указываем топик
                //А следующим параметром указываем что будем выдавать тем, кто на него подписан
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
        );
    }
}
