public class User {
    String nick;
    int score;
    int right;
    String firstname;
    String sexondname;
    String id;

    User(){

    }

    User(String nick, int score, int right, String firstname, String sexondname, String id){
        this.nick = nick;
        this.score = score;
        this.right = right;
        this.firstname = firstname;
        this.sexondname = sexondname;
        this.id = id;
    }
}
