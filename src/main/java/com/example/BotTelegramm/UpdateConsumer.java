package com.example.BotTelegramm;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    public UpdateConsumer() {
        this.telegramClient = new OkHttpTelegramClient("8012796102:AAFR82VP7BVGKIKMsWyIDQW4s-FfnCraThQ");
    }

    @Override
    public void consume(Update update) {
       if (update.hasMessage()) {
           String messageText = update.getMessage().getText();
           Long chatId = update.getMessage().getChatId();
           if (messageText.equals("/start")) {
               sendMainMenu(chatId);
           } else {
             sendMessage(chatId, "Я вас не понимаю !!! ");
           }
       } else if (update.hasCallbackQuery()) {
           handleCallbackQuery(update.getCallbackQuery());
       }
    }



   // Логика определения, на какую кнопку нажал пользователь
    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        var data = callbackQuery.getData();
        var chatId = callbackQuery.getFrom().getId();
        var user = callbackQuery.getFrom();
        switch (data){
            case "my_name" -> sendMyName(chatId, user);
            case "random_number" -> sendRandomNumber(chatId);
            case "long_process" -> sendImage(chatId);
            default -> sendMessage(chatId, "Неизвестная команда");
        }
    }

    private void sendMessage(Long chatId, String messageText) {
        SendMessage message = SendMessage.builder().text(messageText).chatId(chatId).build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendImage(Long chatId) {

        sendMessage(chatId, "Запустили запрос картинки");
    }

    private void sendRandomNumber(Long chatId) {
        var randomNumber = ThreadLocalRandom.current().nextInt();
        sendMessage(chatId, "Присылаю рандомное число : " + randomNumber);
    }

    private void sendMyName(Long chatId, User user) {
        var text = "Привет!\n\nВас зовут: %s\nВаш ник: @%s".formatted(user.getFirstName() + " " + user.getLastName(),user.getUserName());
        sendMessage(chatId, text);
    }

    // menu
    private void sendMainMenu(Long chatId) {
        SendMessage message = SendMessage.builder().text("Добро пожаловать ! Выберите действие: ").chatId(chatId).build();
        var button1 = InlineKeyboardButton.builder()
                .text("Как меня зовут ? ")
                .callbackData("my_name")
                .build();
        var button2 = InlineKeyboardButton.builder()
                .text("Случайное число")
                .callbackData("random_number")
                .build();
        var button3 = InlineKeyboardButton.builder()
                .text("Присылать картинку")
                .callbackData("long_process")
                .build();

        List <InlineKeyboardRow> keyBoardRow = List.of(
                new InlineKeyboardRow(button1),
                new InlineKeyboardRow(button2),
                new InlineKeyboardRow(button3)
        );
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(keyBoardRow);
        message.setReplyMarkup(markup);
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
