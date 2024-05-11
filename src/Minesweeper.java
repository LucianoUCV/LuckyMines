import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Minesweeper {
    private class MineTile extends JButton {
        int r;
        int c;

        public MineTile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    int tileSize = 90;
    int numRows = 5;
    int numColumns = numRows;
    int boardWidth = numColumns * tileSize;
    int boardHeight = numRows * tileSize;

    JFrame frame = new JFrame("Lucky Mines");

    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    int mineCount = 5;
    MineTile[][] board = new MineTile[numRows][numColumns];

    public void setFrame()
    {
        try {
            frame.setIconImage(ImageIO.read(new File("images/diamond.png")));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    ArrayList<MineTile> mineList;
    Random random = new Random();

    int tilesClicked = 0;
    boolean gameOver = false;

    //diamond tile
    ImageIcon diamond = new ImageIcon("images/diamond.png");
    Image imageD = diamond.getImage();
    Image resizeD = imageD.getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);

    //bomb tile
    ImageIcon bomb = new ImageIcon("images/bomb.png");
    Image imageB = bomb.getImage();
    Image resizeB = imageB.getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);

    Minesweeper()
    {
        //frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("<html><font color = 'black'>Lucky Mines</html>");
        textLabel.setBackground(new Color(250,3,127));
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numColumns, 5, 5));
        boardPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel);

        for(int r = 0; r < numRows; r++) {
            for(int c = 0; c < numColumns; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0,0,0,0));
                tile.setFont(new Font("Verdana", Font.PLAIN, 45));
                tile.setBackground(Color.gray);

                boardPanel.add(tile);
            }
        }

        //frame.setVisible(true);

    }

    public void startGame()
    {
        setFrame();
        for(int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                MineTile tile = board[r][c];
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(gameOver) {
                            return;
                        }
                        MineTile mineTile = (MineTile) e.getSource();
                        if(e.getButton() == MouseEvent.BUTTON1) {
                            if(Objects.equals(tile.getText(), "")) {
                                if(mineList.contains(tile)) {
                                    revealMines();
                                }
                                else {
                                    if(tile.isEnabled()) {
                                        revealDiamond(tile.r, tile.c);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
        frame.setVisible(true);

        setMines();
    }

    void setMines()
    {
        mineList = new ArrayList<>();

        int mineLeft = mineCount;
        while(mineLeft > 0) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numColumns);

            MineTile tile = board[r][c];
            if(!mineList.contains(tile)) {
                mineList.add(tile);
                mineLeft--;
            }
        }
    }

    void revealMines()
    {
        bomb = new ImageIcon(resizeB);

        for (MineTile tile : mineList) {
            tile.setIcon(bomb);
            tile.setDisabledIcon(bomb);
            tile.setEnabled(false);
            tile.setBackground(Color.darkGray);
        }

        gameOver = true;
        textLabel.setText("<html><font color = 'black'>You lost!</html>");
    }

    void revealDiamond(int r, int c)
    {
        diamond = new ImageIcon(resizeD);

        MineTile tile = board[r][c];
        tile.setIcon(diamond);
        tile.setDisabledIcon(diamond);
        tile.setEnabled(false);
        tile.setBackground(Color.darkGray);

        tilesClicked++;
        if(tilesClicked == numRows*numColumns - mineList.size()) {
            gameOver = true;
            textLabel.setText("<html><font color = 'dark green'>You won!</html>");
        }
    }
}
