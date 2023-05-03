package com.ll.gramgram.boundedContext.notification.entity;

import com.ll.gramgram.base.baseEntity.BaseEntity;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Notification extends BaseEntity {
    private LocalDateTime readDate;
    @ManyToOne
    @ToString.Exclude
    private InstaMember toInstaMember; // 메세지 받는 사람(호감 받는 사람)
    @ManyToOne
    @ToString.Exclude
    private InstaMember fromInstaMember; // 메세지를 발생시킨 행위를 한 사람(호감표시한 사람)
    private String typeCode; // 호감표시=Like, 호감사유변경=ModifyAttractiveType
    private String oldGender; // 해당사항 없으면 null
    private int oldAttractiveTypeCode; // 해당사항 없으면 0
    private String newGender; // 해당사항 없으면 null
    private int newAttractiveTypeCode; // 해당사항 없으면 0

    private String getAttractiveTypeDisplayName(int attractiveTypeCode) {
        return switch (attractiveTypeCode) {
            case 0 -> "없음";
            case 1 -> "외모";
            case 2 -> "성격";
            default -> "능력";
        };
    }

    public String getNewAttractiveTypeDisplayName(){
        return getAttractiveTypeDisplayName(newAttractiveTypeCode);
    }

    public String getOldAttractiveTypeDisplayName(){
        return getAttractiveTypeDisplayName(oldAttractiveTypeCode);
    }

    public String getElapsedTimeDisplay(){
        Period period = Period.between(super.getCreateDate().toLocalDate(), LocalDate.now());
        if (period.getYears() > 0) {
            return period.getYears() + "년";
        } else if (period.getMonths() > 0) {
            return period.getMonths() + "달";
        } else if (period.getDays() > 0) {
            return period.getDays() + "일";
        } else {
            Duration  elapsedTime = Duration.between(super.getCreateDate(),LocalDateTime.now());
            if(elapsedTime.toSeconds()<60)
                return elapsedTime.toSeconds()+"초";
            else if(elapsedTime.toMinutes()<60)
                return elapsedTime.toMinutes()+"분";
            else
                return elapsedTime.toHours()+"시간";
        }
    }

    public boolean markAsRead(){
        Duration duration = Duration.between(readDate, LocalDateTime.now());
        if(duration.toSeconds()<3)
            return true;
        return false;
    }


    public void updateReadDate(LocalDateTime readDate) {
        this.readDate =readDate;
    }
}
