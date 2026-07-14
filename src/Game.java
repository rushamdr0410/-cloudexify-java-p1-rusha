import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Game {
    private int secretNumber;
    private int attempts;
    private int range;
    private String currentDifficulty;

    private final JLabel resultLabel;
    private final JTextField guessText;
    private final JButton guessButton;
    private final JRadioButton easyBtn;
    private final JRadioButton mediumBtn;
    private final JRadioButton hardBtn;
    private final JLabel bestScoreLabel;
    private final JLabel attemptsLabel;
    private DefaultTableModel leaderBoardModel;
    private JTable leaderBoardTable;

    private List<ScoreEntry> easyScores = new ArrayList<>();
    private List<ScoreEntry> mediumScores = new ArrayList<>();
    private List<ScoreEntry> hardScores = new ArrayList<>();
    private boolean gameInProgress;
    private int maxAttempts;

    public Game(){
//        startNewGame();
        loadBestScores();

        JFrame frame = new JFrame("CloudExify Guessing Game");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                int choice = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to exit?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION
                );
                if(choice == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });
        frame.setSize(300,400);
        frame.setResizable(false);
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
        JButton startButton = createStartButton();
        bestScoreLabel = new JLabel();
        bestScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel = new JLabel("");
        resultLabel.setFont(new Font(resultLabel.getFont().getFontName(),Font.BOLD,16));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel messageLabel = new JLabel("Please enter your guess: ");
        messageLabel.setFont(new Font(messageLabel.getFont().getFontName(),Font.BOLD,12));
        attemptsLabel = new JLabel("Attempts: 0");
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
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
            guessText.setText("");
            guessText.requestFocus();
        });

        String[] columnsNames = {"Rank", "Attempts"};
        leaderBoardModel = new DefaultTableModel(columnsNames,0);
        leaderBoardTable = new JTable(leaderBoardModel);
        JScrollPane scrollPane = new JScrollPane(leaderBoardTable);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(500,500));
        scrollPane.setPreferredSize(new Dimension(500,500));

        panel.add(titleLabel);
        levelPanel.add(easyBtn);
        levelPanel.add(mediumBtn);
        levelPanel.add(hardBtn);
        panel.add(levelPanel);
        panel.add(startButton);
        panel.add(Box.createVerticalStrut(8));
        panel.add(bestScoreLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(resultLabel);
        panel.add(Box.createVerticalStrut(15));

        inputPanel.add(messageLabel);
        inputPanel.add(attemptsLabel);
        inputPanel.add(guessText);
        inputPanel.setMaximumSize(inputPanel.getPreferredSize());
        panel.add(inputPanel);
        panel.add(Box.createRigidArea(new Dimension(0,10)));

        panel.add(guessButton);


        panel.add(Box.createVerticalGlue());

        frame.add(panel);

        frame.setVisible(true);

    }

    private JButton createStartButton() {
        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> {
            if (gameInProgress){
                int choice = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to restart?",
                        "Confirm New Game",
                        JOptionPane.YES_NO_OPTION
                );
                if (choice != JOptionPane.YES_OPTION){
                    return;
                }
            }
            beginRound();
        });
        return startButton;
    }

    private void startNewGame(){
        gameInProgress = true;
        Random random = new Random();
        secretNumber = random.nextInt(range)+1;
        attempts = 0;

    }

    private String getBestScoreText(String label, List<ScoreEntry> scoreList) {
        if (scoreList.isEmpty()){
            return "Best("+label+"):None Yet";
        }
        return "Best("+label+"):"+scoreList.getFirst().attempts;
    }

    private void checkGuess(int guess){
        attempts++;
        attemptsLabel.setText("Attempts: " + attempts);
        guessText.setText("");

        if (attempts >= maxAttempts) {
            resultLabel.setForeground(Color.RED);
            resultLabel.setText("Game Over! The number was " + secretNumber);
            guessButton.setEnabled(false);
            guessText.setEnabled(false);
            gameInProgress = false;
            return;
        }

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
            gameInProgress = false;
            resultLabel.setForeground(Color.GREEN);
            resultLabel.setText("Yay! You Guessed it!");
            guessButton.setEnabled(false);
            guessText.setEnabled(false);

            if (currentDifficulty.equals("Easy")){
                addScore(easyScores, attempts);
            }else if(currentDifficulty.equals("Medium")){
                addScore(mediumScores, attempts);
            } else if (currentDifficulty.equals("Hard")) {
                addScore(hardScores, attempts);
            }
            saveBestScores();

            int choice = JOptionPane.showConfirmDialog(null,
                    "Yay! You guessed it in " + attempts + " attempts! Play again?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                beginRound();
            }
        }

    }

    private void beginRound() {
        if(easyBtn.isSelected()){
            range = 100;
            maxAttempts = 10;
            currentDifficulty = "Easy";
            bestScoreLabel.setText(getBestScoreText("Easy", easyScores));
        }else if(mediumBtn.isSelected()){
            range = 500;
            maxAttempts = 15;
            currentDifficulty = "Medium";
            bestScoreLabel.setText(getBestScoreText("Medium", mediumScores));
        }else if(hardBtn.isSelected()){
            range = 1000;
            maxAttempts = 20;
            currentDifficulty = "Hard";
            bestScoreLabel.setText(getBestScoreText("Hard", hardScores));
        }else{
            resultLabel.setForeground(Color.RED);
            resultLabel.setText("You need to select difficulty level to start the game!");
            return;
        }
        startNewGame();
        guessText.setText("");
        guessText.requestFocus();
        guessText.setEnabled(true);
        resultLabel.setForeground(Color.BLACK);
        resultLabel.setText("<html><div style='text-align: center;'>New Game Started!<br>Guess between 1-" + range + "</div></html>");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        guessButton.setEnabled(true);
    }

    private static class ScoreEntry{
        int attempts;
        ScoreEntry(int attempts){
            this.attempts = attempts;
        }
    }

    private void addScore(List<ScoreEntry> scoreList, int attempts) {
        scoreList.add(new ScoreEntry(attempts));
        scoreList.sort((a, b) -> a.attempts - b.attempts);
        if (scoreList.size() > 5) {
            scoreList.removeLast();
        }
    }

    private void loadBestScores(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader("bestscore.txt"));
            easyScores = lineToScores(reader.readLine());
            mediumScores = lineToScores(reader.readLine());
            hardScores = lineToScores(reader.readLine());
            reader.close();
        }catch (IOException ex){
            easyScores = new ArrayList<>();
            mediumScores = new ArrayList<>();
            hardScores = new ArrayList<>();
        }
    }

    private List<ScoreEntry> lineToScores(String line){
        List<ScoreEntry> list = new ArrayList<>();
        if (line == null || line.isEmpty()){
            return list;
        }
        String[] parts = line.split(",");
        for (String part : parts) {
            list.add(new ScoreEntry(Integer.parseInt(part)));
        }
        return list;
    }

    private void saveBestScores(){
        try{
            FileWriter writer = new FileWriter("bestscore.txt");
            writer.write(scoresToLine(easyScores) + "\n");
            writer.write(scoresToLine(mediumScores) + "\n");
            writer.write(scoresToLine(hardScores) + "\n");
            writer.close();
        } catch (IOException e) {
            resultLabel.setText("Error saving score!");
        }
    }

    private String scoresToLine(List<ScoreEntry> scoreList){
        StringBuilder sb = new StringBuilder();
        for (ScoreEntry entry : scoreList){
            sb.append(entry.attempts).append(",");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            new Game();
        });
    }
}