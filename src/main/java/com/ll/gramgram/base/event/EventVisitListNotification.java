package com.ll.gramgram.base.event;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class EventVisitListNotification extends ApplicationEvent {
    private final LocalDateTime readDate;
    private final InstaMember instaMember;
    public EventVisitListNotification(Object source, InstaMember instaMember,LocalDateTime readDate) {
        super(source);
        this.instaMember=instaMember;
        this.readDate = readDate;
    }

}
