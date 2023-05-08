package com.ll.gramgram.boundedContext.notification.eventListener;

import com.ll.gramgram.base.event.*;
import com.ll.gramgram.boundedContext.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;

    //New like
    @EventListener
    @Transactional
    public void listen(EventAfterLike event){
        notificationService.create(event.getLikeablePerson(), "Like");
    }

    //Modify like
    @EventListener
    @Transactional
    public void listen(EventAfterModifyAttractiveType event){
        notificationService.create(event.getLikeablePerson(), event.getOldAttractiveTypeCode());
    }

    //Cancel like
    @EventListener
    @Transactional
    public void listen(EventBeforeCancelLike event){
        notificationService.create(event.getLikeablePerson(), "Cancel");
    }

    @EventListener
    @Transactional
    public void listen(EventVisitListNotification event){
        notificationService.updateReadDate(event.getInstaMember(), event.getReadDate());
    }
}
