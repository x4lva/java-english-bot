import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineButtons {
    public static List<List<InlineKeyboardButton>> sendThirdOpcions() {

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();

        inlineKeyboardButton1.setText("A");
        inlineKeyboardButton1.setCallbackData("A");

        inlineKeyboardButton2.setText("B");
        inlineKeyboardButton2.setCallbackData("B");

        inlineKeyboardButton3.setText("C");
        inlineKeyboardButton3.setCallbackData("C");

        inlineKeyboardButton4.setText("D");
        inlineKeyboardButton4.setCallbackData("D");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        keyboardButtonsRow1.add(inlineKeyboardButton3);
        keyboardButtonsRow1.add(inlineKeyboardButton4);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(keyboardButtonsRow1);
        return rowList;
    }

    public static List<List<InlineKeyboardButton>> sendTrueFalseOpcions() {

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();

        inlineKeyboardButton1.setText("TRUE");
        inlineKeyboardButton1.setCallbackData("TRUE");

        inlineKeyboardButton2.setText("FALSE");
        inlineKeyboardButton2.setCallbackData("FALSE");


        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(keyboardButtonsRow1);
        return rowList;
    }

    public static List<List<InlineKeyboardButton>> sendWordOpcions(String a) {

        String[] words = a.split(" ");

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();

        inlineKeyboardButton1.setText("A: "+words[0]);
        inlineKeyboardButton1.setCallbackData("A");

        inlineKeyboardButton2.setText("B: "+words[1]);
        inlineKeyboardButton2.setCallbackData("B");

        inlineKeyboardButton3.setText("C: "+words[2]);
        inlineKeyboardButton3.setCallbackData("C");

        inlineKeyboardButton4.setText("D: "+words[3]);
        inlineKeyboardButton4.setCallbackData("D");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        keyboardButtonsRow1.add(inlineKeyboardButton3);
        keyboardButtonsRow1.add(inlineKeyboardButton4);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(keyboardButtonsRow1);
        return rowList;
    }

    public static List<List<InlineKeyboardButton>> sendEightOpcions() {

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton7 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton8 = new InlineKeyboardButton();

        inlineKeyboardButton1.setText("A");
        inlineKeyboardButton1.setCallbackData("A");

        inlineKeyboardButton2.setText("B");
        inlineKeyboardButton2.setCallbackData("B");

        inlineKeyboardButton3.setText("C");
        inlineKeyboardButton3.setCallbackData("C");

        inlineKeyboardButton4.setText("D");
        inlineKeyboardButton4.setCallbackData("D");

        inlineKeyboardButton5.setText("E");
        inlineKeyboardButton5.setCallbackData("E");

        inlineKeyboardButton6.setText("F");
        inlineKeyboardButton6.setCallbackData("F");

        inlineKeyboardButton7.setText("G");
        inlineKeyboardButton7.setCallbackData("G");

        inlineKeyboardButton8.setText("H");
        inlineKeyboardButton8.setCallbackData("H");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        keyboardButtonsRow1.add(inlineKeyboardButton3);
        keyboardButtonsRow1.add(inlineKeyboardButton4);
        keyboardButtonsRow1.add(inlineKeyboardButton5);
        keyboardButtonsRow1.add(inlineKeyboardButton6);
        keyboardButtonsRow1.add(inlineKeyboardButton7);
        keyboardButtonsRow1.add(inlineKeyboardButton8);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(keyboardButtonsRow1);
        return rowList;
    }
}
