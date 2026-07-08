import javax.swing.*;
import java.awt.*;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
import java.util.Random;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Game {
    private int secretNumber;
    private int attempts;
    private Random random;

    private JLabel resultLabel;
    private JTextField guessText;

    public Game(){
        startNewGame();

        JFrame frame = new JFrame("CloudExify Guessing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,400);
        frame.setLayout(new GridLayout(1,1)); //BorderLayout or GridLayout(rows, cols) or FlowLayout

        JPanel panel = new JPanel();
        panel.setVisible(true);

        JLabel titleLabel =  new JLabel("CloudExify Guessing Game");
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(),Font.BOLD,16));
        resultLabel = new JLabel("");
        JLabel messageLabel = new JLabel("Please enter your guess: ");
        messageLabel.setFont(new Font(messageLabel.getFont().getFontName(),Font.BOLD,12));
        guessText = new JTextField();
        guessText.setColumns(10);
        JButton guessButton = new JButton("Guess");
        guessButton.setFont(new Font(guessButton.getFont().getFontName(),Font.BOLD,12));
        guessButton.addActionListener(e -> {
            String txt = guessText.getText();
            try{
                int guess = Integer.parseInt(txt);
                checkGuess(guess);

            }catch(NumberFormatException ex){
                resultLabel.setText("Invalid Guess!");
            }

        });

        panel.add(titleLabel);
        panel.add(resultLabel);
        panel.add(messageLabel);
        panel.add(guessText);
        panel.add(guessButton);

        frame.add(panel);

        frame.setVisible(true);

    }

    private void startNewGame(){

        random = new Random();
        secretNumber = random.nextInt(100)+1;
        attempts = 0;

    }

    private void checkGuess(int guess){
        attempts++;
        if(guess < secretNumber){
            resultLabel.setText("Higher");
        }
        else if(guess > secretNumber){
            resultLabel.setText("Lower");
        }
        else{
            resultLabel.setText("Yay! You Guessed it!");
        }

    }

    public static void main(String[] args) {
        new Game();
    }
}