package com.example.springtgbot.service;

import com.example.springtgbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsService {

    private UserRepository userRepository;

    @Autowired
    public StatisticsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SendMessage getStatisticsMessage(Message message) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyBoard();
        final String textMessage = "Ваши расходы по всем категориям ежедневного кошелька: \n"
                + userRepository.getExpenses(message.getChatId(), "Расход");

        return createMessageWithKeyboard(message.getChatId(), textMessage, replyKeyboardMarkup);
    }

    private ReplyKeyboardMarkup getMainMenuKeyBoard() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow rowStat = new KeyboardRow();
        KeyboardRow rowBalanceOfDaily = new KeyboardRow();
        KeyboardRow rowBalanceOfAccumulative = new KeyboardRow();

        rowStat.add(new KeyboardButton("Получить статистику по кошелькам"));
        rowBalanceOfDaily.add(new KeyboardButton("Ежедневный кошелек"));
        rowBalanceOfAccumulative.add(new KeyboardButton("Накопительный кошелек"));

        keyboardRowList.add(rowStat);
        keyboardRowList.add(rowBalanceOfDaily);
        keyboardRowList.add(rowBalanceOfAccumulative);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;

    }

    private SendMessage createMessageWithKeyboard(final long chatId,
                                                  String textMessage,
                                                  final ReplyKeyboardMarkup replyKeyboardMarkup) {

        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(Long.toString(chatId));



        sendMessage.setText(textMessage);

        if(replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }

        return sendMessage;

    }
}
