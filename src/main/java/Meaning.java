import java.util.List;

class Meaning {
    Integer id;
    String text;
    Texts[] meanings;
}
class Texts{
    Integer id;
    String partOfSpeechCode;
    Translate translation;
    String previewUrl;
    String imageUrl;
    String transcription;
    String soundUrl;
}

class Translate{
    String text;
    String note;
}