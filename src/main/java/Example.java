public class Example {
    String id;
    Integer wordId;
    Integer difficultyLevel;
    String partOfSpeechCode;
    String prefix;
    String text;
    String soundUrl;
    String transcription;
    Object properties;
    String updatedAt;
    String mnemonics;
    Translation translation;
    WordImage[] images;
    Definition definition;
    Examples[] examples;
    Similar[] meaningsWithSimilarTranslation;
    Alternative[] alternativeTranslations;
}

class Translation{
    String text;
    String note;
}

class WordImage{
    String url;
}

class Definition{
    String text;
    String soundUrl;
}

class Examples{
    String text;
    String soundUrl;
}

class Similar{
    Integer meaningId;
    String frequencyPercent;
    String partOfSpeechAbbreviation;
    Translation translation;
}

class Alternative{
    String text;
    Translation translation;
}