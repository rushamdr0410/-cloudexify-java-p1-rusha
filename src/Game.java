import javax.swing.*;
import java.awt.*;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
import java.io.*;
import java.util.Random;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Game {
    private int secretNumber;
    private int attempts;
    private int range;

    private JLabel resultLabel;
    private JTextField guessText;
    private JButton guessButton;
    private final JRadioButton easyBtn;
    private final JRadioButton mediumBtn;
    private final JRadioButton hardBtn;
    private JLabel bestScoreLabel;

    private int bestEasy;
    private int bestMedium;
    private int bestHard;

    public Game(){
//        startNewGame();
        loadBestScores();

        JFrame frame = new JFrame("CloudExify Guessing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,400);
        frame.setLayout(new GridLayout(1,1)); //BorderLayout or GridLayout(rows, cols) or FlowLayout

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setVisible(true);

        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        levelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelPanel.setMaximumSize(new Dimension(500,100));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel =  new JLabel("CloudExify Guessing Game");
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(),Font.BOLD,16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        easyBtn = new JRadioButton("Easy");
        mediumBtn = new JRadioButton("Medium");
        hardBtn = new JRadioButton("Hard");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(easyBtn);
        buttonGroup.add(mediumBtn);
        buttonGroup.add(hardBtn);
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> {
            if(easyBtn.isSelected()){
                range=100;
                bestScoreLabel.setText("Best (Easy): " + (bestEasy == Integer.MAX_VALUE ? "None yet" : bestEasy));
            }else if(mediumBtn.isSelected()){
                range=500;
                bestScoreLabel.setText("Best (Medium): " + (bestMedium == Integer.MAX_VALUE ? "None yet" : bestMedium));
            }else if(hardBtn.isSelected()){
                range=1000;
                bestScoreLabel.setText("Best (Hard): " + (bestHard == Integer.MAX_VALUE ? "None yet" : bestHard));
            }else{
                resultLabel.setForeground(Color.RED);
                resultLabel.setText("You need to select difficulty level to start the game!");
                return;
            }
            startNewGame();
            guessText.setText("");
            guessText.setEnabled(true);
            resultLabel.setForeground(Color.BLACK);
            resultLabel.setText("<html>New Game Started!<br>Guess between 1-" + range + "</html>");
            guessButton.setEnabled(true);
        });
        bestScoreLabel = new JLabel();
        resultLabel = new JLabel("");
        resultLabel.setFont(new Font(resultLabel.getFont().getFontName(),Font.BOLD,16));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel messageLabel = new JLabel("Please enter your guess: ");
        messageLabel.setFont(new Font(messageLabel.getFont().getFontName(),Font.BOLD,12));
        guessText = new JTextField();
        guessText.setPreferredSize(new Dimension(100,30));
        guessText.setHorizontalAlignment(JTextField.CENTER);
        guessText.setMargin(new Insets(1, 5, 1, 5));
        guessButton = new JButton("Guess");
        guessButton.setEnabled(false);
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
        levelPanel.add(easyBtn);
        levelPanel.add(mediumBtn);
        levelPanel.add(hardBtn);
        panel.add(levelPanel);
        panel.add(startButton);
        panel.add(bestScoreLabel);
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

        Random random = new Random();
        secretNumber = random.nextInt(range)+1;
        attempts = 0;

    }

    private void checkGuess(int guess){
        attempts++;
        if(guess <1 || guess > range){
            resultLabel.setForeground(Color.RED);
            resultLabel.setText("Guessing Range is between 1-"+range);
            return;
        }
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
            guessButton.setEnabled(false);
            guessText.setEnabled(false);
            if (easyBtn.isSelected()){
                if(attempts < bestEasy){
                    bestEasy = attempts;
                    saveBestScores();
                    bestScoreLabel.setText("Best (Easy): " + bestEasy);
                }
            }else if(mediumBtn.isSelected()){
                if(attempts < bestMedium){
                    bestMedium = attempts;
                    saveBestScores();
                    bestScoreLabel.setText("Best (Medium): " + bestMedium);
                }
            } else if (hardBtn.isSelected()) {
                if(attempts < bestHard){
                    bestHard = attempts;
                    saveBestScores();
                    bestScoreLabel.setText("Best (Hard): " + bestHard);
                }
            }
        }

    }

    private void loadBestScores(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader("bestscore.txt"));
            bestEasy = Integer.parseInt(reader.readLine());
            bestMedium = Integer.parseInt(reader.readLine());
            bestHard = Integer.parseInt(reader.readLine());
            reader.close();
        }catch (IOException ex){
            bestEasy = Integer.MAX_VALUE;
            bestMedium = Integer.MAX_VALUE;
            bestHard = Integer.MAX_VALUE;
        }
    }

    private void saveBestScores(){
        try{
            FileWriter writer = new FileWriter("bestscore.txt");
            writer.write(bestEasy+"\n");
            writer.write(bestMedium+"\n");
            writer.write(bestHard+"\n");
            writer.close();
        } catch (IOException e) {
            resultLabel.setText("Error saving score!");
        }
    }

    public static void main(String[] args) {
        new Game();
    }
}