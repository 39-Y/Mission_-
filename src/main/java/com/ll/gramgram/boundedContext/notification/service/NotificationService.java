package com.ll.gramgram.boundedContext.notification.service;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.ll.gramgram.boundedContext.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<Notification> findByToInstaMember(InstaMember toInstaMember) {
        return notificationRepository.findByToInstaMember(toInstaMember);
    }

    public List<Notification> findByToInstaMemberAndReadDateIsNull(InstaMember toInstaMember) {
        return notificationRepository.findByToInstaMemberAndReadDateIsNull(toInstaMember);
    }

    //New Like
    public Notification create(LikeablePerson newLikeablePerson, String typeCode) {
        Notification newLike = Notification.builder()
                .newAttractiveTypeCode(newLikeablePerson.getAttractiveTypeCode())
                .newGender(newLikeablePerson.getFromInstaMember().getGender())
                .fromInstaMember(newLikeablePerson.getFromInstaMember())
                .toInstaMember(newLikeablePerson.getToInstaMember())
                .typeCode(typeCode)
                .build();
        return notificationRepository.save(newLike);
    }
    //Modified Like
    public Notification create(LikeablePerson modifiedLikeablePerson, int oldAttractiveTypeCode) {
        Notification modifiedLike = Notification.builder()
                .newGender(modifiedLikeablePerson.getFromInstaMember().getGender())
                .fromInstaMember(modifiedLikeablePerson.getFromInstaMember())
                .toInstaMember(modifiedLikeablePerson.getToInstaMember())
                .oldAttractiveTypeCode(oldAttractiveTypeCode)
                .newAttractiveTypeCode(modifiedLikeablePerson.getAttractiveTypeCode())
                .typeCode("ModifyAttractiveType")
                .build();
        return notificationRepository.save(modifiedLike);
    }

    public void updateReadDate(InstaMember instaMember,LocalDateTime readDate) {
        List<Notification> notReadNotifications = findByToInstaMemberAndReadDateIsNull(instaMember);
        for(Notification noRead : notReadNotifications){
            noRead.updateReadDate(readDate);
        }
    }
}
