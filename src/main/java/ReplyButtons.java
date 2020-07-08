import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyButtons {
    KeyboardRow keyboardFirstRow = new KeyboardRow();
    KeyboardRow keyboardSexondRow = new KeyboardRow();
    KeyboardRow keyboardThirdRow = new KeyboardRow();

    List<KeyboardRow> keyboard = new ArrayList<>();



    public List<KeyboardRow> setReplyButtons(String a){

        keyboardFirstRow.clear();
        keyboardSexondRow.clear();
        keyboardThirdRow.clear();
        keyboard.clear();

        String[] rows = a.split("-");
        String[] firstrow = null;
        String[] sexondrow = null;
        String[] thirdrow = null;

        for (int i = 0; i < rows.length; i++) {
            switch (i){
                case 0:
                    firstrow = rows[0].split(" ");
                    break;
                case 1:
                    sexondrow = rows[1].split(" ");
                    break;
                case 2:
                    thirdrow = rows[2].split(" ");
                    break;
                default:
                    break;
            }
        }

        for (int i = 0; i < rows.length; i++) {
            switch (i){
                case 0:
                    for (int j = 0; j < firstrow.length; j++) {
                        keyboardFirstRow.add(firstrow[j]);
                    }
                    break;
                case 1:
                    for (int j = 0; j < sexondrow.length; j++) {
                        keyboardSexondRow.add(sexondrow[j]);
                    }
                    break;
                case 2:
                    for (int j = 0; j < thirdrow.length; j++) {
                        keyboardThirdRow.add(thirdrow[j]);
                    }
                    break;
                default:
                    break;
            }
        }

        switch (rows.length){
            case 1:
                keyboard.add(keyboardFirstRow);
                break;
            case 2:
                keyboard.add(keyboardFirstRow);
                keyboard.add(keyboardSexondRow);
                break;
            case 3:
                keyboard.add(keyboardFirstRow);
                keyboard.add(keyboardSexondRow);
                keyboard.add(keyboardThirdRow);
                break;
            default:
                break;
        }


        return keyboard;

    }

}
