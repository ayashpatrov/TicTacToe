import javax.swing.*;
import java.util.Date;
import java.util.Random;

public class Main {
    public static void main(String [] args) {
        Random random = new Random();
        System.out.println(new Date());
        random.setSeed(new Date().getTime());
        new MainForm(random);
    }
}
