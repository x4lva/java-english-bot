import java.util.List;

public class Text {
    boolean result;
    List<Erors> errors;

}
class Erors{
    String id;
    int offset;
    int length;
    String bad;
    String[] better;
}
