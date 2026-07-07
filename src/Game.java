import javax.swing.*;
import java.awt.*;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Game {
    public static void main(String[] args) {
        JFrame frame = new JFrame("CloudExify Guessing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,400);
        frame.setLayout(new GridLayout(1,1)); //BorderLayout or GridLayout(rows, cols) or FlowLayout
        frame.setVisible(true);

        JPanel panel = new JPanel();

        JLabel titleLabel =  new JLabel("CloudExify Guessing Game");
        JLabel messageLabel = new JLabel("Please enter your guess: ");
        JTextField guessText = new JTextField();
        JButton guessButton = new JButton("Guess");

        panel.add(titleLabel);
        panel.add(messageLabel);
        panel.add(guessText);
        panel.add(guessButton);

        frame.add(panel);
    }
}