import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.Inet4Address;
import java.net.StandardSocketOptions;
import java.net.URL;
import java.util.*;

public class Bot extends TelegramLongPollingBot {
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    InlineButtons inlineButtons = new InlineButtons();

    boolean grama = false;
    boolean words = false;
    boolean audio = false;
    boolean istext = false;
    boolean trace = false;
    boolean photo_trace = false;
    boolean is_qwiz = false;
    boolean is_wrong = false;
    int wrongs = 0;
    ArrayList<Words> qwiz = null;
    ArrayList<Words> asks = null;
    ArrayList<Words> wrong_wars = new ArrayList<Words>();

    ArrayList<Words> wrong = new ArrayList<Words>();

    Set<Integer> vars =null;
    ArrayList<Integer> var = null;
    String audio_answer;
    String word_answer;
    String grama_ans;
    int curent = -1;
    int qwiz_curent = -1;


    String menu = "Теорія Тести-Результати Рейтинг-Вивчення слів";

    static DataBase db =new  DataBase();
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        db.dbConection();
        String word = "to get on well";

        try {
            botsApi.registerBot(new Bot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpdateReceived(Update update) {
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);


        Message message = update.getMessage();
        if(update.hasMessage()) {
            if (istext){
                SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
                sendMessage.enableHtml(true);
                if (message.getText().equals("Скасувати")) {
                    grama = false;
                    words = false;
                    audio = false;
                    istext = false;
                    sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons(menu)));
                    sendMessage.setText("Меню");
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    istext=false;
                }else{
                    sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons(menu)));
                    String ress=null;
                    try {
                        ress = checkText(message.getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Gson g = new Gson();
                    String erororses = "";
                    String betters = "";
                    SendMessage sendMessage2 = new SendMessage().setChatId(message.getChatId());
                    sendMessage2.enableHtml(true);

                    SendMessage sendMessage3 = new SendMessage().setChatId(message.getChatId());
                    sendMessage3.enableHtml(true);

                    Text person = g.fromJson(ress, Text.class);
                    if (person.errors.size()==0){
                        sendMessage.setText("У вашому листі не знайдено помилок");
                    }else{
                        sendMessage.setText("У вашому листі присутні помилки: ");
                        for (int i = 0; i < person.errors.size(); i++) {
                            betters="";
                            for (int j = 0; j < person.errors.get(i).better.length; j++) {
                                betters = betters + person.errors.get(i).better[j]+", ";
                            }
                            erororses = erororses + "\n • "+person.errors.get(i).bad+" ["+person.errors.get(i).offset+" - "+(person.errors.get(i).offset+person.errors.get(i).length)+"]\n Можливий варіант: "+betters.substring(0,betters.length()-2)+"\n";
                        }
                        sendMessage2.setText(erororses);

                        String returned = message.getText();

                        for (int i = 0; i < person.errors.size(); i++) {
                            returned = returned.replace(person.errors.get(i).bad,"<u><i><b>"+person.errors.get(i).bad+"</b></i></u>");
                        }
                        sendMessage3.setText(returned);
                    }
                    try {
                        execute(sendMessage);
                        execute(sendMessage2);
                        if (person.errors.size()!=0){
                            execute(sendMessage3);
                        }
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    for (Erors errors : person.errors) {
                        System.out.println(errors.bad);
                    }
                    istext = false;
                }
            }else {
                if (photo_trace) {
                    SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
                    sendMessage.enableHtml(true);
                    if (message.getText().equals("Скасувати")) {
                        grama = false;
                        words = false;
                        audio = false;
                        istext = false;
                        sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons(menu)));
                        sendMessage.setText("Меню");
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        photo_trace = false;
                    } else {
                        ArrayList<String> users = db.getAllusers();
                        SendDocument p = new SendDocument();
                        for (int i = 0; i < users.size(); i++) {
                            try {
                                p.setChatId(users.get(i)).setDocument("Example", new FileInputStream(new File("C:\\Users\\chevp\\Desktop\\YepZnoBot\\src\\main\\resources\\trace_photo\\" + update.getMessage().getText())));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            System.out.println(users.get(i));
                            try {
                                execute(p);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        sendMessage.setChatId(message.getChatId());
                        sendMessage.setText("Фото надіслано");
                        sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons(menu)));
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        photo_trace = false;
                    }
                } else {
                    if (trace) {
                        SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
                        sendMessage.enableHtml(true);
                        if (message.getText().equals("Скасувати")) {
                            grama = false;
                            words = false;
                            audio = false;
                            istext = false;
                            sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons(menu)));
                            sendMessage.setText("Меню");
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                            trace = false;
                        } else {
                            ArrayList<String> users = db.getAllusers();
                            sendMessage.setText(message.getText()).enableHtml(true);
                            for (int i = 0; i < users.size(); i++) {
                                sendMessage.setChatId(users.get(i));
                                System.out.println(users.get(i));
                                try {
                                    execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            }
                            sendMessage.setChatId(message.getChatId());
                            sendMessage.setText("Повідомлення надіслано");
                            sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons(menu)));
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                            trace = false;
                        }
                    }else {
                        if (is_qwiz){
                            if (update.getMessage().getText().equals("Меню")){
                                grama = false;
                                words = false;
                                audio = false;
                                istext = false;
                                is_qwiz = false;
                                curent=-1;
                                qwiz_curent=-1;
                                SendMessage sendMessage = new SendMessage().setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons(menu)));

                                sendMessage.setText("Меню").setChatId(message.getChatId());
                                try {
                                    execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                if (qwiz_curent==5){
                                    System.out.println("ТИ СУКА");
                                }
                                if (qwiz_curent==4){
                                    wrong.remove(0);
                                    SendMessage ur = new SendMessage().setChatId(message.getChatId());
                                    if (update.getMessage().getText().replace(" ","").replace(" ","").equals(qwiz.get(4).translate.replace(" ","").replace(" ",""))){
                                        ur.setText("Відповідь вірна");
                                    }else {
                                        wrong.add(qwiz.get(4));
                                        ur.setText("Відповідь невірна");
                                    }
                                    try {
                                        execute(ur);
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                    if (wrong.size() !=0 ){
                                        is_wrong = true;
                                        wrongs = wrong.size();
                                        qwiz_curent = -1;
                                    }
                                    is_qwiz = false;
                                }else{
                                    qwiz_curent++;
                                    vars = new HashSet<Integer>();
                                    var = new ArrayList<Integer>();
                                    SendMessage sm = null;
                                    if (qwiz_curent == 0) {
                                        sm = new SendMessage().setChatId(message.getChatId()).setText("Виберіть вірну відповідь:");
                                    }
                                    vars.add(qwiz_curent);
                                    while (vars.size() != 4) {
                                        int temp = getRandomNumberInRange(0, 4);
                                        vars.add(temp);
                                    }
                                    Iterator<Integer> i = vars.iterator();
                                    while (i.hasNext()) {
                                        var.add(i.next());
                                    }
                                    Collections.shuffle(var);

                                    if (qwiz_curent == 0) {
                                        if (update.getMessage().getText().replace(" ", "").replace(" ", "").equals(qwiz.get(0).translate.replace(" ", "").replace(" ", ""))) {
                                            SendMessage sd = new SendMessage().setChatId(message.getChatId()).setText("Відповідь вірна");
                                            try {
                                                execute(sd);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            SendMessage sd = new SendMessage().setChatId(message.getChatId()).setText("Відповідь невірна");
                                            wrong.add(qwiz.get(0));
                                            try {
                                                execute(sd);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        if (update.getMessage().getText().replace(" ", "").replace(" ", "").equals(qwiz.get(qwiz_curent - 1).translate.replace(" ", "").replace(" ", ""))) {
                                            SendMessage sd = new SendMessage().setChatId(message.getChatId()).setText("Відповідь вірна");
                                            try {
                                                execute(sd);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            SendMessage sd = new SendMessage().setChatId(message.getChatId()).setText("Відповідь невірна");
                                            wrong.add(qwiz.get(qwiz_curent-1));
                                            try {
                                                execute(sd);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    SendMessage word = new SendMessage().setChatId(message.getChatId()).setText(qwiz.get(qwiz_curent).word).setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons(qwiz.get(var.get(0)).translate.replace(" ", " ") + " " + qwiz.get(var.get(1)).translate.replace(" ", " ") + "-" + qwiz.get(var.get(2)).translate.replace(" ", " ") + " " + qwiz.get(var.get(3)).translate.replace(" ", " ") + "-Меню")));
                                    try {
                                        if (qwiz_curent == 0) {
                                            execute(sm);
                                        }
                                        execute(word);
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }else{
                            if (is_wrong){
                                if (update.getMessage().getText().equals("Меню")){
                                    grama = false;
                                    words = false;
                                    audio = false;
                                    istext = false;
                                    is_qwiz = false;
                                    curent=-1;
                                    qwiz_curent=-1;
                                    SendMessage sendMessage = new SendMessage().setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons(menu)));

                                    sendMessage.setText("Меню").setChatId(message.getChatId());
                                    try {
                                        execute(sendMessage);
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    if (qwiz_curent==wrongs-1){
//                                        wrong.remove(0);
                                        System.out.println("-------------------");
                                        for (int i = 0; i < wrong.size(); i++) {
                                            System.out.println(wrong.get(i).word);
                                        }
                                        SendMessage ur = new SendMessage().setChatId(message.getChatId());
                                        if (update.getMessage().getText().replace(" ","").replace(" ","").equals(wrong.get(wrongs-1).translate.replace(" ","").replace(" ",""))){
                                            ur.setText("Відповідь вірна");

                                        }else {
                                            wrong.add(wrong.get(wrongs-1));
                                            ur.setText("Відповідь невірна");
                                        }
                                        try {
                                            execute(ur);
                                        } catch (TelegramApiException e) {
                                            e.printStackTrace();
                                        }

                                        if (wrong.size() !=wrongs ){
                                            is_wrong = true;
                                            for (int i = 0; i < wrongs; i++) {
                                                wrong.remove(0);
                                            }
                                            wrongs = wrong.size();
                                            qwiz_curent = -1;
                                        }else{
                                            SendMessage sd = new SendMessage().setChatId(message.getChatId()).setText("ИЗИ НАХУЙ");

                                            try {
                                                execute(sd);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        wrongs = wrong.size();

                                    }else{

                                        qwiz_curent++;
                                        vars = new HashSet<Integer>();
                                        var = new ArrayList<Integer>();
                                        SendMessage sm = null;
                                        if (qwiz_curent == 0) {
                                          //  wrong.remove(wrong.size()-1);
                                            sm = new SendMessage().setChatId(message.getChatId()).setText("Виберіть вірну відповідь:");
                                        }
                                        vars.add(qwiz_curent);
                                        while (vars.size() != 4) {
                                            int temp = getRandomNumberInRange(0, 4);
                                            vars.add(temp);
                                        }
                                        Iterator<Integer> i = vars.iterator();
                                        while (i.hasNext()) {
                                            var.add(i.next());
                                        }
                                        Collections.shuffle(var);

                                        if (qwiz_curent == 0) {
                                            if (update.getMessage().getText().replace(" ", "").replace(" ", "").equals(wrong.get(0).translate.replace(" ", "").replace(" ", ""))) {
                                                SendMessage sd = new SendMessage().setChatId(message.getChatId()).setText("Відповідь вірна");
                                                try {
                                                    execute(sd);
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                SendMessage sd = new SendMessage().setChatId(message.getChatId()).setText("Відповідь невірна");
                                                wrong.add(wrong.get(0));
                                                try {
                                                    execute(sd);
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } else {
                                            if (update.getMessage().getText().replace(" ", "").replace(" ", "").equals(wrong.get(qwiz_curent - 1).translate.replace(" ", "").replace(" ", ""))) {
                                                SendMessage sd = new SendMessage().setChatId(message.getChatId()).setText("Відповідь вірна");
                                                try {
                                                    execute(sd);
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                SendMessage sd = new SendMessage().setChatId(message.getChatId()).setText("Відповідь невірна");
                                                wrong.add(wrong.get(qwiz_curent-1));
                                                try {
                                                    execute(sd);
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        SendMessage word = new SendMessage().setChatId(message.getChatId()).setText(wrong.get(qwiz_curent).word).setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons(qwiz.get(var.get(0)).translate.replace(" ", " ") + " " + qwiz.get(var.get(1)).translate.replace(" ", " ") + "-" + qwiz.get(var.get(2)).translate.replace(" ", " ") + " " + qwiz.get(var.get(3)).translate.replace(" ", " ") + "-Меню")));
                                        try {
                                            if (qwiz_curent == 0) {
                                                execute(sm);
                                            }
                                            execute(word);
                                        } catch (TelegramApiException e) {
                                            e.printStackTrace();
                                        }
                                        System.out.println("-------------------");
                                        for (int j = 0; j < wrong.size(); j++) {
                                            System.out.println(wrong.get(j).word);
                                        }
                                        if (qwiz_curent == 0){
                                            wrong.remove(wrong.size()-1);
                                        }
                                    }
                                }
                            }else{
                                if (message.getFrom().getId() == 502952440) {
                                    SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
                                    sendMessage.enableHtml(true);
                                    switch (message.getText()) {
                                        case "Адмін":
                                            sendMessage.setText("Панель адміністратора");
                                            sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons("Розсилка Фото-Меню")));
                                            try {
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Розсилка":
                                            sendMessage.setText("Напишіть повідомлення для розсилки");
                                            sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons("Скасувати")));
                                            trace = true;
                                            try {
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Фото":
                                            sendMessage.setText("Надішліть назву фото для розсилки");
                                            sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons("Скасувати")));
                                            photo_trace = true;
                                            try {
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                    }
                                }
                                if (update.getMessage().hasText()) {
                                    SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
                                    sendMessage.enableHtml(true);
                                    switch (update.getMessage().getText()) {
                                        case "/start":
                                            grama = false;
                                            words = false;
                                            audio = false;
                                            istext = false;
                                            db.createNewUser(message.getFrom().getUserName(), message.getFrom().getFirstName(), message.getFrom().getLastName(), String.valueOf(message.getFrom().getId()));
                                            sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons(menu)));
                                            sendMessage.setText("Старт");
                                            try {
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Текст":
                                            grama = false;
                                            words = false;
                                            audio = false;
                                            istext = true;
                                            SendMessage text = new SendMessage().setChatId(message.getChatId()).setText(db.getText()).enableHtml(true).setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons("Скасувати")));
                                            try {
                                                execute(text);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Меню":
                                            grama = false;
                                            words = false;
                                            audio = false;
                                            istext = false;
                                            is_qwiz = false;
                                            curent=-1;
                                            qwiz_curent=-1;
                                            sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons(menu)));
                                            sendMessage.setText("Меню");
                                            try {
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Вивчення слів":
                                            grama = false;
                                            words = false;
                                            audio = false;
                                            istext = false;
                                            is_qwiz = false;
                                            curent=-1;
                                            qwiz_curent=-1;
                                            sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons("Мистецтво Тема1 Тема2 Тема3-Тема4 Тема5 Тема6 Тема7-Меню")));
                                            sendMessage.setText("Виберіть тему");
                                            try {
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Мистецтво":
                                            grama = false;
                                            words = false;
                                            audio = false;
                                            istext = false;
                                            is_qwiz = false;
                                            curent++;
                                            qwiz = db.getTestWord("art");
                                            SendAudio sa = new SendAudio();

                                            sa.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons("Далі-Меню")));
                                            sa.setCaption(qwiz.get(curent).word+" - "+qwiz.get(curent).translate).setChatId(message.getChatId());
                                            String[] res = new String[0];
                                            try {
                                                res = getExample(qwiz.get(curent).word);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            sendMessage.setText("<b>Приклад:</b> "+res[0]).setChatId(message.getChatId()).enableHtml(true);
                                            try {
                                                downloadWordAudio("https:"+res[1], "C:\\Users\\chevp\\Desktop\\YepZnoBot\\src\\main\\resources\\word_audio\\"+qwiz.get(curent).word+".mp3");
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                sa.setAudio(qwiz.get(curent).word, new FileInputStream(new File("C:\\Users\\chevp\\Desktop\\YepZnoBot\\src\\main\\resources\\word_audio\\"+qwiz.get(curent).word+".mp3")));
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                execute(sa);
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Далі":
                                            grama = false;
                                            words = false;
                                            audio = false;
                                            istext = false;
                                            is_qwiz = false;
                                            curent++;
                                            SendAudio sa2 = new SendAudio();

                                            sa2.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons("Далі-Меню")));
                                            sa2.setCaption(qwiz.get(curent).word+" - "+qwiz.get(curent).translate).setChatId(message.getChatId());
                                            String[] res2 = new String[0];
                                            System.out.println(curent);
                                            try {
                                                res2 = getExample(qwiz.get(curent).word);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            sendMessage.setText("<b>Приклад:</b> "+res2[0]).setChatId(message.getChatId()).enableHtml(true);
                                            try {
                                                downloadWordAudio("https:"+res2[1], "C:\\Users\\chevp\\Desktop\\YepZnoBot\\src\\main\\resources\\word_audio\\"+qwiz.get(curent).word+".mp3");
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                sa2.setAudio(qwiz.get(curent).word, new FileInputStream(new File("C:\\Users\\chevp\\Desktop\\YepZnoBot\\src\\main\\resources\\word_audio\\"+qwiz.get(curent).word+".mp3")));
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                execute(sa2);
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            if (curent==4){
                                                is_qwiz =true;
                                            }
                                            break;
                                        case "Теорія":
                                            grama = false;
                                            words = false;
                                            audio = false;
                                            istext = false;
                                            sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons("Часи Дієслово Правопис Речення-Словотворення Конструкції Частини мови Модальні-Меню")));
                                            sendMessage.setText("Меню");
                                            try {
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Дієслово":
                                            grama = false;
                                            words = false;
                                            audio = false;
                                            istext = false;
                                            sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons("Інфінітив Герундій-Меню")));
                                            sendMessage.setText("Меню");
                                            try {
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Інфінітив":
                                            grama = false;
                                            words = false;
                                            audio = false;
                                            istext = false;
                                            sendMessage.setText("https://telegra.ph/Infinitive-07-08");
                                            try {
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Герундій":
                                            grama = false;
                                            words = false;
                                            audio = false;
                                            istext = false;
                                            sendMessage.setText("https://telegra.ph/Gerund-07-08");
                                            try {
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Тести":
                                            grama = false;
                                            words = false;
                                            audio = false;
                                            istext = false;
                                            sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons("Граматика Слова Слухання Текст-Меню")));
                                            sendMessage.setText("Виберіть тему\uD83D\uDCDA");
                                            try {
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Граматика":
                                            grama = true;
                                            words = false;
                                            audio = false;
                                            istext = false;
                                            sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons("Меню")));
                                            sendMessage.setText("Виберіть правильну відповідь");
                                            String[] test = db.getTestF();

                                            File img = new File("C:\\Users\\chevp\\Desktop\\YepZnoBot\\src\\main\\resources\\grama_1\\" + test[0]);
                                            grama_ans = test[1];
                                            SendPhoto photo = null;
                                            if (test[2].equals("1")){
                                                photo = new SendPhoto().setChatId(message.getChatId()).setReplyMarkup(inlineKeyboardMarkup.setKeyboard(inlineButtons.sendThirdOpcions()));
                                            }else{
                                                photo = new SendPhoto().setChatId(message.getChatId()).setReplyMarkup(inlineKeyboardMarkup.setKeyboard(inlineButtons.sendEightOpcions()));
                                            }
                                            try {
                                                photo.setPhoto("SomeText", new FileInputStream(img));
                                                execute(sendMessage);
                                                execute(photo);
                                            } catch (TelegramApiException | FileNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Слова":
                                            words = true;
                                            grama = false;
                                            audio = false;
                                            istext = false;
                                            String[] wordst = db.getTestWords();
                                            String qwestion = wordst[1];
                                            word_answer = wordst[2];
                                            sendMessage.setReplyMarkup(inlineKeyboardMarkup.setKeyboard(inlineButtons.sendWordOpcions(wordst[0])));
                                            sendMessage.setText("Вкажіть переклад слова <b>" + qwestion + "</b>").setChatId(update.getMessage().getChatId());
                                            try {
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Слухання":
                                            words = false;
                                            grama = false;
                                            audio = true;
                                            istext = false;
                                            sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons("Меню")));
                                            sendMessage.setText("Вкажи чи правильне твердження:");
                                            String[] audiot = db.getTestAudio();
                                            File myFolders = new File("C:\\Users\\chevp\\Desktop\\YepZnoBot\\src\\main\\resources\\audio\\" + audiot[3]);
                                            audio_answer = audiot[2];
                                            String qwestion_a = audiot[1];

                                            SendAudio sendAudio = null;
                                            try {
                                                sendAudio = new SendAudio().setAudio("Сулахання №" + audiot[0], new FileInputStream(myFolders)).setChatId(message.getChatId()).setCaption(qwestion_a).setReplyMarkup(inlineKeyboardMarkup.setKeyboard(inlineButtons.sendTrueFalseOpcions()));
                                                execute(sendMessage);
                                                execute(sendAudio);
                                            } catch (TelegramApiException | FileNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Результати":
                                            words = false;
                                            grama = false;
                                            audio = false;
                                            istext = false;
                                            int[] score = db.getUserScore(String.valueOf(message.getFrom().getId()));
                                            double perc = ((double) score[1] / (double) score[0]) * 100.0;
                                            sendMessage.setReplyMarkup(replyKeyboardMarkup.setKeyboard(new ReplyButtons().setReplyButtons("Меню")));
                                            sendMessage.setText("Ваші результати:\n - Пройдено завдань: <b>" + score[0] + "</b>\n - Кільість балів: <b>" + score[1] + "</b>\n - Середній результат: <b>" + Math.round(perc) + "%</b>");
                                            try {
                                                execute(sendMessage);
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        default:
                                            throw new IllegalStateException("Unexpected value: " + update.getMessage().getText());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else if (update.hasCallbackQuery()){
            if (grama){
                if (grama_ans.equals(update.getCallbackQuery().getData())){
                    db.setRightAnswer(String.valueOf(update.getCallbackQuery().getFrom().getId()));
                    try {
                        execute(new SendMessage().setText("✅ Ваша відповідь: <b>" +
                                update.getCallbackQuery().getData() + "</b>. Відповідь вірна!")
                                .setChatId(update.getCallbackQuery().getMessage().getChatId()).enableHtml(true));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    String[] test = db.getTestF();

                    File img = new File("C:\\Users\\chevp\\Desktop\\YepZnoBot\\src\\main\\resources\\grama_1\\"+test[0]);
                    grama_ans = test[1];
                    SendPhoto photo = null;
                    if (test[2].equals("1")){
                        photo = new SendPhoto().setChatId(update.getCallbackQuery().getMessage().getChatId()).setReplyMarkup(inlineKeyboardMarkup.setKeyboard(inlineButtons.sendThirdOpcions()));
                    }else{
                        photo = new SendPhoto().setChatId(update.getCallbackQuery().getMessage().getChatId()).setReplyMarkup(inlineKeyboardMarkup.setKeyboard(inlineButtons.sendEightOpcions()));
                    }
                    try {
                        photo.setPhoto("SomeText", new FileInputStream(img));
                        execute(photo);
                    } catch (TelegramApiException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    db.setWrongAnswer(String.valueOf(update.getCallbackQuery().getFrom().getId()));
                    try {
                        execute(new SendMessage().setText("❎ Ваша відповідь: <b>" +
                                update.getCallbackQuery().getData() + "</b>. Вірна відповідь <b>" + grama_ans + "</b>")
                                .setChatId(update.getCallbackQuery().getMessage().getChatId()).enableHtml(true));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    String[] test = db.getTestF();
                    File img = new File("C:\\Users\\chevp\\Desktop\\YepZnoBot\\src\\main\\resources\\grama_1\\"+test[0]);
                    grama_ans = test[1];
                    SendPhoto photo = null;
                    if (test[2].equals("1")){
                        photo = new SendPhoto().setChatId(update.getCallbackQuery().getMessage().getChatId()).setReplyMarkup(inlineKeyboardMarkup.setKeyboard(inlineButtons.sendThirdOpcions()));
                    }else{
                        photo = new SendPhoto().setChatId(update.getCallbackQuery().getMessage().getChatId()).setReplyMarkup(inlineKeyboardMarkup.setKeyboard(inlineButtons.sendEightOpcions()));
                    }                    try {
                        photo.setPhoto("SomeText", new FileInputStream(img));
                        execute(photo);
                    } catch (TelegramApiException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                if (words){
                    if (word_answer.equals(update.getCallbackQuery().getData())){
                        db.setRightAnswer(String.valueOf(update.getCallbackQuery().getFrom().getId()));
                        try {
                            execute(new SendMessage().setText("✅ Ваша відповідь: <b>" +
                                    update.getCallbackQuery().getData() + "</b>. Відповідь вірна!")
                                    .setChatId(update.getCallbackQuery().getMessage().getChatId()).enableHtml(true));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        String[] wordst = db.getTestWords();
                        String qwestion = wordst[1];
                        word_answer = wordst[2];
                        SendMessage sendMessage = new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId());
                        sendMessage.enableHtml(true);
                        sendMessage.setReplyMarkup(inlineKeyboardMarkup.setKeyboard(inlineButtons.sendWordOpcions(wordst[0])));
                        sendMessage.setText("Вкажіть переклад слова <b>" + qwestion + "</b>");
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }else{
                        db.setWrongAnswer(String.valueOf(update.getCallbackQuery().getFrom().getId()));
                        try {
                            execute(new SendMessage().setText("❎ Ваша відповідь: <b>" +
                                    update.getCallbackQuery().getData() + "</b>. Вірна відповідь <b>" + word_answer + "</b>")
                                    .setChatId(update.getCallbackQuery().getMessage().getChatId()).enableHtml(true));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        String[] wordst = db.getTestWords();
                        String qwestion = wordst[1];
                        word_answer = wordst[2];
                        SendMessage sendMessage = new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId());
                        sendMessage.enableHtml(true);
                        sendMessage.setReplyMarkup(inlineKeyboardMarkup.setKeyboard(inlineButtons.sendWordOpcions(wordst[0])));
                        sendMessage.setText("Вкажіть переклад слова <b>" + qwestion + "</b>");
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    if (audio){
                        if (audio_answer.equals(update.getCallbackQuery().getData())){
                            db.setRightAnswer(String.valueOf(update.getCallbackQuery().getFrom().getId()));
                            try {
                                execute(new SendMessage().setText("✅ Ваша відповідь: <b>" +
                                        update.getCallbackQuery().getData() + "</b>. Відповідь вірна!")
                                        .setChatId(update.getCallbackQuery().getMessage().getChatId()).enableHtml(true));
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                            String[] audiot = db.getTestAudio();
                            File myFolders = new File("C:\\Users\\chevp\\Desktop\\YepZnoBot\\src\\main\\resources\\audio\\"+audiot[3]);
                            audio_answer = audiot[2];
                            String qwestion_a = audiot[1];

                            SendAudio sendAudio = null;
                            try {
                                sendAudio = new SendAudio().setAudio("Сулахання №" + audiot[0], new FileInputStream(myFolders)).setChatId(update.getCallbackQuery().getMessage().getChatId()).setCaption(qwestion_a).setReplyMarkup(inlineKeyboardMarkup.setKeyboard(inlineButtons.sendTrueFalseOpcions()));
                                execute(sendAudio);
                            } catch (TelegramApiException | FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else {
                            db.setWrongAnswer(String.valueOf(update.getCallbackQuery().getFrom().getId()));
                            try {
                                execute(new SendMessage().setText("❎ Ваша відповідь: <b>" +
                                        update.getCallbackQuery().getData() + "</b>. Вірна відповідь <b>" + audio_answer + "</b>")
                                        .setChatId(update.getCallbackQuery().getMessage().getChatId()).enableHtml(true));
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                            String[] audiot = db.getTestAudio();
                            File myFolders = new File("C:\\Users\\chevp\\Desktop\\YepZnoBot\\src\\main\\resources\\audio\\"+audiot[3]);
                            audio_answer = audiot[2];
                            String qwestion_a = audiot[1];

                            SendAudio sendAudio = null;
                            try {
                                sendAudio = new SendAudio().setAudio("Сулахання №" + audiot[0], new FileInputStream(myFolders)).setChatId(update.getCallbackQuery().getMessage().getChatId()).setCaption(qwestion_a).setReplyMarkup(inlineKeyboardMarkup.setKeyboard(inlineButtons.sendTrueFalseOpcions()));
                                execute(sendAudio);
                            } catch (TelegramApiException | FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public static String checkText(String a) throws IOException {
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        String res = null;
        final HttpUriRequest httpGet = new HttpGet("https://api.textgears.com/check.php?text="+a.replace(" ","+")+"&key=gjpwhjK6RKhZmCrH");
        try (
                CloseableHttpResponse response1 = httpclient.execute(httpGet)
        ){
            final HttpEntity entity1 = response1.getEntity();
            res = EntityUtils.toString(entity1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        httpclient.close();
        System.out.print(res);
        return res;
    }

    public static int getWordExample(String a) throws IOException {
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        String res = null;
        final HttpUriRequest httpGet = new HttpGet("https://dictionary.skyeng.ru/api/public/v1/words/search?_format=json&search="+a.replace(" ","+"));
        try (
                CloseableHttpResponse response1 = httpclient.execute(httpGet)
        ){
            final HttpEntity entity1 = response1.getEntity();
            res = EntityUtils.toString(entity1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        httpclient.close();

        Gson g = new Gson();

        Meaning[] mean = g.fromJson(res, Meaning[].class);

        int id = mean[0].meanings[0].id;

        return id;
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static String[] getExample(String a) throws IOException {
        int id = getWordExample(a);
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        String res = null;
        final HttpUriRequest httpGet = new HttpGet("http://dictionary.skyeng.ru/api/public/v1/meanings?_format=json&ids="+id);
        try (
                CloseableHttpResponse response1 = httpclient.execute(httpGet)
        ){
            final HttpEntity entity1 = response1.getEntity();
            res = EntityUtils.toString(entity1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        httpclient.close();

        Gson g = new Gson();

        Example[] mean = g.fromJson(res, Example[].class);

        String example = mean[0].examples[0].text;
        String audiourl = mean[0].soundUrl;

        return new String[]{example.replace("[","").replace("]",""),audiourl};
    }

    public static void downloadWordAudio(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = bis.read(buffer, 0, 1024)) != -1) {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }


    public java.lang.String getBotUsername() {
        return "yep_eng_bot";
    }

    public java.lang.String getBotToken() {
        return "1120165931:AAFy7k7-wNI0gI3d_HNRRYkDzBZam0D8Fx0";
    }

}
