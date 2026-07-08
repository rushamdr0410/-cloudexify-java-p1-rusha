import javax.swing.*;
import java.awt.*;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
import java.util.Random;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Game {
    private int secretNumber;
    private int attempts;
    private Random random;
    private int range;

    private JLabel resultLabel;
    private JTextField guessText;

    public Game(){
        startNewGame();

        JFrame frame = new JFrame("CloudExify Guessing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,400);
        frame.setLayout(new GridLayout(1,1)); //BorderLayout or GridLayout(rows, cols) or FlowLayout

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setVisible(true);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel =  new JLabel("CloudExify Guessing Game");
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(),Font.BOLD,16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel = new JLabel("");
        resultLabel.setFont(new Font(resultLabel.getFont().getFontName(),Font.BOLD,16));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel messageLabel = new JLabel("Please enter your guess: ");
        messageLabel.setFont(new Font(messageLabel.getFont().getFontName(),Font.BOLD,12));
        guessText = new JTextField();
        guessText.setPreferredSize(new Dimension(100,30));
        guessText.setHorizontalAlignment(JTextField.CENTER);
        guessText.setMargin(new Insets(1, 5, 1, 5));
        JButton guessButton = new JButton("Guess");
        guessButton.setFont(new Font(guessButton.getFont().getFontName(),Font.BOLD,12));
        guessButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        guessButton.addActionListener(e -> {
            String txt = guessText.getText();
            try{
                int guess = Integer.parseInt(txt);
                checkGuess(guess);

            }catch(NumberFormatException ex){
                resultLabel.setForeground(Color.RED);
                resultLabel.setText("Invalid Guess!");
            }

        });

        panel.add(titleLabel);
        panel.add(resultLabel);

        inputPanel.add(messageLabel);
        inputPanel.add(guessText);
        inputPanel.setMaximumSize(inputPanel.getPreferredSize());
        panel.add(inputPanel);
        panel.add(Box.createRigidArea(new Dimension(0,10)));

        panel.add(guessButton);


        panel.add(Box.createVerticalGlue());

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
//        if(guess <1 || guess > 100){
//            resultLabel.setForeground(Color.RED);
//            resultLabel.setText("Guessing Range is between 1-100");
//            return;
//        }
        if(guess < secretNumber){
            resultLabel.setForeground(Color.BLUE);
            resultLabel.setText("HIGHER~");
        }
        else if(guess > secretNumber){
            resultLabel.setForeground(Color.BLUE);
            resultLabel.setText("LOWER~");
        }
        else{
            resultLabel.setForeground(Color.GREEN);
            resultLabel.setText("Yay! You Guessed it!");
        }

    }

    public static void main(String[] args) {
        new Game();
    }
}