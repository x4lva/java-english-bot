import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class DataBase {
    static String url = "jdbc:mysql://localhost/yep?serverTimezone=Europe/Moscow&useSSL=false";
    static String usernamec = "root";
    static String password = "";



    public static void dbConection(){

        try{

            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, usernamec, password)){

                System.out.println("Connection to Store DB succesfull!");
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }


    public int countTestF(){

        int answers = 0;
        try (Connection con = DriverManager.getConnection(url, usernamec, password)){

            PreparedStatement pr = con.prepareStatement("select count(*)"
                    + " from tests");

            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                answers = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return answers;
    }

    public String[] getTestF(){
        int testCount = countTestF();
        String img = "";
        String answer = "";
        int type = 0;
        try (Connection con = DriverManager.getConnection(url, usernamec, password)){

            PreparedStatement pr = con.prepareStatement("select img, answer, type"
                    + " from tests where id=?");
            int task = getRandomNumberInRange(1, testCount);
            pr.setString(1, String.valueOf(task));

            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                img = rs.getString(1);
                answer = rs.getString(2);
                type = rs.getInt(3);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] s =  {img,answer, String.valueOf(type)};
        return s;
    }


    public int countText(){

        int answers = 0;
        try (Connection con = DriverManager.getConnection(url, usernamec, password)){

            PreparedStatement pr = con.prepareStatement("select count(*)"
                    + " from text");

            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                answers = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return answers;
    }

    public String getText(){
        int testCount = countText();
        String text = "";

        try (Connection con = DriverManager.getConnection(url, usernamec, password)){

            PreparedStatement pr = con.prepareStatement("select text"
                    + " from text where id=?");
            int task = getRandomNumberInRange(1, testCount);
            pr.setString(1, String.valueOf(task));

            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                text = rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String s =  text;
        return s;
    }


    public int countTestWords(){

        int answers = 0;
        try (Connection con = DriverManager.getConnection(url, usernamec, password)){

            PreparedStatement pr = con.prepareStatement("select count(*)"
                    + " from words");

            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                answers = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return answers;
    }

    public String[] getTestWords(){
        int testCount = countTestWords();
        String vars = "";
        String qwestion = "";
        String righta = "";

        try (Connection con = DriverManager.getConnection(url, usernamec, password)){

            PreparedStatement pr = con.prepareStatement("select vars, qwestion, righta"
                    + " from words where id=?");
            int task = getRandomNumberInRange(1, testCount);
            pr.setString(1, String.valueOf(task));

            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                vars = rs.getString(1);
                qwestion = rs.getString(2);
                righta = rs.getString(3);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] s =  {vars,qwestion,righta};
        return s;
    }


    public int countTestAudio(){

        int answers = 0;
        try (Connection con = DriverManager.getConnection(url, usernamec, password)){

            PreparedStatement pr = con.prepareStatement("select count(*)"
                    + " from audio");

            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                answers = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return answers;
    }


    public String[] getTestAudio(){
        int testCount = countTestAudio();
        int id=0;
        String qwestion = "";
        String righta = "";
        String audio = "";

        try (Connection con = DriverManager.getConnection(url, usernamec, password)){

            PreparedStatement pr = con.prepareStatement("select id, qwestion, righta, audio"
                    + " from audio where id=?");
            int task = getRandomNumberInRange(1, testCount);
            pr.setString(1, String.valueOf(task));

            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                id = rs.getInt(1);
                qwestion = rs.getString(2);
                righta = rs.getString(3);
                audio = rs.getString(4);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] s =  {String.valueOf(id),qwestion,righta,audio};
        return s;
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public void setRightAnswer(String id){
        int answers = 0;
        int right = 0;

        try (Connection con = DriverManager.getConnection(url, usernamec, password)){

            PreparedStatement pr = con.prepareStatement("select answers, rights"
                    + " from users where id=?");
            pr.setString(1, id);

            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                answers = rs.getInt(1);
                right = rs.getInt(2);
            }

            PreparedStatement get = con.prepareStatement("update users set answers = ? where id = ?");
            get.setInt(1, answers+1);
            get.setString(2, id);
            get.executeUpdate();

            PreparedStatement set = con.prepareStatement("update users set rights = ? where id = ?");
            set.setInt(1, right+1);
            set.setString(2, id);
            set.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setWrongAnswer(String id){
        int answers = 0;
        int right = 0;

        try (Connection con = DriverManager.getConnection(url, usernamec, password)){

            PreparedStatement pr = con.prepareStatement("select answers, rights"
                    + " from users where id=?");
            pr.setString(1, id);

            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                answers = rs.getInt(1);
                right = rs.getInt(2);
            }

            PreparedStatement get = con.prepareStatement("update users set answers = ? where id = ?");
            get.setInt(1, answers+1);
            get.setString(2, id);
            get.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int[] getUserScore(String id){
        int answers = 0;
        int right = 0;

        try (Connection con = DriverManager.getConnection(url, usernamec, password)){

            PreparedStatement pr = con.prepareStatement("select answers, rights"
                    + " from users where id=?");
            pr.setString(1, id);

            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                answers = rs.getInt(1);
                right = rs.getInt(2);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        int[] res = new int[2];
        res[0] = answers;
        res[1] = right;

        return res;

    }

    public ArrayList<String> getAllusers(){
        ArrayList<String> res = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, usernamec, password)){

            PreparedStatement pr = con.prepareStatement("select id"
                    + " from users");

            ResultSet rs = pr.executeQuery();
            while (rs.next()){
                res.add(rs.getString(1));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;

    }

    public void createNewUser(String username, String firstname, String sexondname, String id){
        try (Connection con = DriverManager.getConnection(url, usernamec, password)){

            PreparedStatement pr = con.prepareStatement("select username, answers, rights, firstname, sexondname, id"
                    + " from users where username=?");
            pr.setString(1, username);

            ResultSet rs = pr.executeQuery();

            if (getResultSetRowCount(rs)==0){
                PreparedStatement get = con.prepareStatement("insert into "
                        + "users(username, answers, rights, firstname, sexondname, id) values(?,?,?,?,?,?)");
                get.setString(1, username);
                get.setInt(2, 0);
                get.setInt(3, 0);
                get.setString(4, firstname);
                get.setString(5, sexondname);
                get.setString(6, id);

                get.executeUpdate();
            }else{
                System.out.println("Користувач вже існує");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static int getResultSetRowCount(ResultSet resultSet) {
        int size = 0;
        try {
            resultSet.last();
            size = resultSet.getRow();
            resultSet.beforeFirst();
        }
        catch(SQLException ex) {
            return 0;
        }
        return size;
    }

}
