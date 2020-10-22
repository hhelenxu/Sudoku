import java.awt.*;
import javax.swing.*;
import java.util.Random;


public class Sudoku extends JFrame{
    public static final int GRID_SIZE = 9;
    public static final int SUBGRID_SIZE = 3;
    public static final int CELL = 100;
    public static final int BOARD_DIM = CELL*GRID_SIZE;
    public static final Color OPEN_CELL_COLOR = Color.PINK;
    public static final Color CLOSED_CELL_COLOR = new Color(240, 240, 240);
    public static final Color CLOSED_CELL_TEXT = Color.BLACK;
    public static final Font FONT = new Font("Monospaced", Font.BOLD, 20);

    private JTextField[][] tfCells = new JTextField[GRID_SIZE][GRID_SIZE];
    private int[][] puzzle;
    private boolean[][] mask =
            {{false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false}};

    public Sudoku() {
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(null, "Welcome to Sudoku!",
                "Begin Game", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, null);

        boolean stop = false;
        switch(choice) {
            case JOptionPane.YES_OPTION:
                setDifficulty(1);
                break;
            case JOptionPane.NO_OPTION:
                setDifficulty(2);
                break;
            case JOptionPane.CANCEL_OPTION:
                setDifficulty(3);
                break;
            default:
                stop = true;
        }

        if (!stop) {
            generatePuzzle();
            Container cp = getContentPane();

//            JPanel cp = new JPanel(new GridLayout(GRID_SIZE,GRID_SIZE));
//            cp.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
            //cp.setLayout(panel);

            cp.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));

            InputListener listener = new InputListener(tfCells, puzzle, mask);

            for (int r = 0; r < GRID_SIZE; r++) {
                for (int c = 0; c < GRID_SIZE; c++) {
                    tfCells[r][c] = new JTextField();
                    cp.add(tfCells[r][c]);
                    if (mask[r][c]) {
                        tfCells[r][c].setText("");
                        tfCells[r][c].setEditable(true);
                        tfCells[r][c].setBackground(OPEN_CELL_COLOR);
                        tfCells[r][c].addActionListener(listener);
                    } else {
                        tfCells[r][c].setText(puzzle[r][c] + "");
                        tfCells[r][c].setEditable(false);
                        tfCells[r][c].setBackground(CLOSED_CELL_COLOR);
                        tfCells[r][c].setForeground(CLOSED_CELL_TEXT);
                    }
                    tfCells[r][c].setHorizontalAlignment(JTextField.CENTER);
                    tfCells[r][c].setFont(FONT);
                }
            }

            cp.setPreferredSize(new Dimension(BOARD_DIM, BOARD_DIM));
            pack();

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setTitle("Sudoku Game!");
            setVisible(true);
            listener.startTime();

        }
    }

    private void setDifficulty(int n)
    {
        for (int i=0;i<15*n;i++)
        {
            Random rand = new Random();
            int num = rand.nextInt(GRID_SIZE*GRID_SIZE);
            while (mask[num/GRID_SIZE][num%GRID_SIZE])
                num = rand.nextInt(GRID_SIZE*GRID_SIZE);
            mask[num/GRID_SIZE][num%GRID_SIZE] = true;
        }
    }

    private void generatePuzzle() {
        puzzle = new int[GRID_SIZE][GRID_SIZE];
        Random rand = new Random();
        for (int i=0; i<3; i++)
        {
            int[] frequency = {0,0,0,0,0,0,0,0,0};
            for (int r=0; r<SUBGRID_SIZE; r++)
            {
                for (int c=0; c<SUBGRID_SIZE; c++) {
                    int num = rand.nextInt(9)+1;
                    while (frequency[num-1]!=0) {
                        num = rand.nextInt(9)+1;
                    }
                    puzzle[i * 3 + r][i * 3 + c] = num;
                    frequency[num-1]++;
                }
            }
        }
        solveSudoku();
    }

    private boolean solveSudoku()
    {
        int row = -1;
        int col = -1;
        boolean isEmpty = true;
        for (int r=0; r<GRID_SIZE; r++)
        {
            for (int c=0; c<GRID_SIZE; c++)
            {
                if (puzzle[r][c]==0) {
                    row = r;
                    col = c;
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty)
                break;
        }
        if (isEmpty)
            return true;
        int[] frequency = {0,0,0,0,0,0,0,0,0};
        Random rand = new Random();
        for (int i=0; i<GRID_SIZE; i++)
        {
            int n = rand.nextInt(9)+1;
            while (frequency[n-1]!=0)
                n = rand.nextInt(9)+1;
            frequency[n-1]++;
            if(isSafe(row,col,n))
            {
                puzzle[row][col] = n;
                if (solveSudoku())
                    return true;
                else
                    puzzle[row][col] = 0;
            }
        }
        return false;
    }

    private boolean isSafe(int row, int col, int num)
    {
        for (int i=0; i<GRID_SIZE; i++)
        {
            if (puzzle[row][i]==num || puzzle[i][col]==num)
                return false;
        }
        int rowStart = row-row%SUBGRID_SIZE;
        int colStart = col-col%SUBGRID_SIZE;
        for (int r=rowStart; r<rowStart+SUBGRID_SIZE; r++)
        {
            for (int c=colStart; c<colStart+SUBGRID_SIZE; c++)
            {
                if (puzzle[r][c]==num)
                    return false;
            }
        }
        return true;
    }

    public static void main(String[] args)
    {
        Sudoku s = new Sudoku();
    }
}
