import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.time.Instant;
import java.time.Duration;

public class InputListener implements ActionListener {
    private static final int GRID_SIZE = 9;
    private JTextField[][] tfCells;
    private int[][] board;
    private boolean[][] mask;
    private int[][] left;
    private Instant start;
    private int incorrect;

    public InputListener(JTextField[][] field, int[][] b, boolean[][] m)
    {
        tfCells = field;
        board = b;
        mask = m;
        left = new int[GRID_SIZE][GRID_SIZE];
        incorrect = 0;
        for (int r=0;r<GRID_SIZE;r++)
            for (int c=0;c<GRID_SIZE;c++)
                if (mask[r][c])
                    left[r][c] = 1;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        int rowChosen = -1;
        int colChosen = -1;

        JTextField source = (JTextField)e.getSource();

        boolean found = false;
        for (int r=0;r<GRID_SIZE && !found;r++)
        {
            for (int c=0;c<GRID_SIZE && !found;c++)
            {
                if (tfCells[r][c] == source)
                {
                    rowChosen = r;
                    colChosen = c;
                    found = true;
                }
            }
        }

        String in = tfCells[rowChosen][colChosen].getText();
        if (in.length() > 1 || (in.charAt(0)<'1'||in.charAt(0)>'9')) {
            JFrame f = new JFrame();
            JOptionPane.showMessageDialog(f,"Error: please enter a valid number.\nInput should be one number 1-9");
            return;
        }
        int input = Integer.parseInt(in);
        if (board[rowChosen][colChosen]==input) {
            tfCells[rowChosen][colChosen].setBackground(Color.GREEN);
            left[rowChosen][colChosen] = 0;
            isFinished();
        }
        else {
            tfCells[rowChosen][colChosen].setBackground(Color.RED);
            incorrect++;
        }
    }

    private void isFinished() {
        if (!contains(left, 1)) {
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start,end);
            int choice = JOptionPane.showOptionDialog(null, "Congratulations! You did it!\nTime taken: "+timeElapsed.toMinutesPart()+" minutes and "
                            +timeElapsed.toSecondsPart()+" seconds\nNumber of incorrect answers: "+incorrect+"\nStart new game?",
                    "You finished!", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    new ImageIcon("C:\\Users\\helen\\Documents\\Code\\Sudoku Solver\\Sudoku\\confetti.png"), null, null);
            if (choice == JOptionPane.YES_OPTION)
                new Sudoku();
        }
    }

    private boolean contains(int[][] arr, int n) {
        for (int r=0;r<arr.length;r++) {
            for (int c=0;c<arr[r].length;c++) {
                if (arr[r][c] == 1)
                    return true;
            }
        }
        return false;
    }

    public void startTime() {
        start = Instant.now();
    }
}
