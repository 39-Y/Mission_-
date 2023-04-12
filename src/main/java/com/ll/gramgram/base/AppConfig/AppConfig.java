package com.ll.gramgram.base.AppConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Getter
    static Long likablePerson_Max ;
    @Value("${custom.likeablePerson.from.max}")
    public void setLikablePerson_Max(long likablePerson_Max){
        AppConfig.likablePerson_Max = likablePerson_Max;
    }

}
