package com.example.BotTelegramm;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

import java.security.PublicKey;


@Component
public class MyTelegramBot implements SpringLongPollingBot {

    private final  UpdateConsumer updateConsumer;

    public  MyTelegramBot(UpdateConsumer updateConsumer) {
        this.updateConsumer = updateConsumer;
    }

    @Override
    public String getBotToken() {
        return "8012796102:AAFR82VP7BVGKIKMsWyIDQW4s-FfnCraThQ";
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumer;
    }
}
