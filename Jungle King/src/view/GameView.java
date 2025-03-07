package src.view;

import javax.swing.BorderFactory;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;
import javax.swing.*;

import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import src.controller.GameController;
import src.model.GameModel;
import src.model.pieces.*;

public class GameView extends JFrame {
    private final GameController gameController;

    public GameView(GameController controller) {
        this.gameController = controller;
        setTitle("Jungle King Game Board");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        
        JPanel topPanel = new JPanel();
        GameModel.turnLabel = new JLabel("Waiting for initial piece selection...");
        GameModel.turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(GameModel.turnLabel);
        add(topPanel, BorderLayout.NORTH);

        
        JPanel boardPanel = new JPanel(new GridLayout(GameModel.ROWS, GameModel.COLS));
        add(boardPanel, BorderLayout.CENTER);

        
        String basePath = "Jungle King/Assets/";
        GameModel.landIcon = scaleImage(basePath + "Board/land.png", 100, 100);
        GameModel.lakeIcon = scaleImage(basePath + "Board/lake.png", 100, 100);
        GameModel.trapIcon = scaleImage(basePath + "Board/trap.png", 100, 100);
        GameModel.denIcon = scaleImage(basePath + "Board/den.png", 100, 100);
        GameModel.hiddenIcon = scaleImage(basePath + "Board/hidden.png", 100, 100);

        GameModel.grid = new JButton[GameModel.ROWS][GameModel.COLS];
        GameModel.pieces = new Piece[GameModel.ROWS][GameModel.COLS];
        initializeBoard(boardPanel);
        initializePieces();
        gameController.addPieceSelectionListeners();

        
        setFocusTraversalKeysEnabled(false);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gameController.handleKeyPress(e);
            }
        });
        setFocusable(true);
        requestFocus();
    }

    private void initializePieces() {
        
        GameModel.pieces = new Piece[GameModel.ROWS][GameModel.COLS];
        GameModel.originalPieces = new Piece[GameModel.ROWS][GameModel.COLS];

        
        GameModel.originalPieces[0][0] = new Lion(true);
        GameModel.originalPieces[0][2] = new Elephant(true);
        GameModel.originalPieces[1][1] = new Cat(true);
        GameModel.originalPieces[2][2] = new Wolf(true);
        GameModel.originalPieces[4][2] = new Leopard(true);
        GameModel.originalPieces[5][1] = new Dog(true);
        GameModel.originalPieces[6][0] = new Tiger(true);
        GameModel.originalPieces[6][2] = new Rat(true);
        
        
        GameModel.originalPieces[0][8] = new Tiger(false);
        GameModel.originalPieces[1][7] = new Dog(false);
        GameModel.originalPieces[2][6] = new Leopard(false);
        GameModel.originalPieces[0][6] = new Rat(false);
        GameModel.originalPieces[6][6] = new Elephant(false);
        GameModel.originalPieces[6][8] = new Lion(false);
        GameModel.originalPieces[5][7] = new Cat(false);
        GameModel.originalPieces[4][6] = new Wolf(false);

        
        java.util.List<Piece> allPieces = new java.util.ArrayList<>();
        for (int row = 0; row < GameModel.ROWS; row++) {
            for (int col = 0; col < GameModel.COLS; col++) {
                if (GameModel.originalPieces[row][col] != null) {
                    allPieces.add(GameModel.originalPieces[row][col]);
                }
            }
        }

        
        java.util.Collections.shuffle(allPieces);
        int pieceIndex = 0;
        for (int row = 0; row < GameModel.ROWS; row++) {
            for (int col = 0; col < GameModel.COLS; col++) {
                if (!isLake(row, col) && !isDen(row, col) && pieceIndex < allPieces.size()) {
                    GameModel.pieces[row][col] = allPieces.get(pieceIndex++);
                }
            }
        }

        updateBoardDisplayWithHiddenPieces();
    }

    private void initializeBoard(JPanel boardPanel) {
        for (int row = 0; row < GameModel.ROWS; row++) {
            for (int col = 0; col < GameModel.COLS; col++) {
                GameModel.grid[row][col] = new JButton();
                GameModel.grid[row][col].setIcon(getTileIcon(row, col));
                GameModel.grid[row][col].setBorderPainted(true); 
                GameModel.grid[row][col].setFocusPainted(false);
                GameModel.grid[row][col].setContentAreaFilled(false);
                boardPanel.add(GameModel.grid[row][col]);
            }
        }
    }

    private void updateBoardDisplayWithHiddenPieces() {
        for (int row = 0; row < GameModel.ROWS; row++) {
            for (int col = 0; col < GameModel.COLS; col++) {
                ImageIcon baseIcon = getTileIcon(row, col);
                if (GameModel.pieces[row][col] != null) {
                    GameModel.grid[row][col].setIcon(overlayIcons(baseIcon, GameModel.hiddenIcon));
                } else {
                    GameModel.grid[row][col].setIcon(baseIcon);
                }
            }
        }
    }

    public ImageIcon overlayIcons(ImageIcon backgroundIcon, ImageIcon foregroundIcon) {
        Image background = backgroundIcon.getImage();
        Image foreground = foregroundIcon.getImage();
        
        BufferedImage combined = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();
        
        g.drawImage(background, 0, 0, 100, 100, null);
        g.drawImage(foreground, 0, 0, 100, 100, null);
        g.dispose();
        
        return new ImageIcon(combined);
    }
    public ImageIcon getTileIcon(int row, int col) {
        if (isLake(row, col)) return GameModel.lakeIcon;
        if (isTrap(row, col)) return GameModel.trapIcon;
        if (isDen(row, col)) return GameModel.denIcon;
        return GameModel.landIcon;
    }

    private boolean isLake(int row, int col) {
        return gameController.isLake(row, col);
    }

    private boolean isTrap(int row, int col) {
        return gameController.isTrap(row, col);
    }

    private boolean isDen(int row, int col) {
        return gameController.isDen(row, col);
    }

    private ImageIcon scaleImage(String path, int width, int height) {
        try {
            String absolutePath = new java.io.File(path).getAbsolutePath();
            ImageIcon icon = new ImageIcon(absolutePath);
            
            if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                System.err.println("Failed to load image: " + absolutePath);
                return createFallbackIcon(width, height);
            }
            
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (Exception e) {
            System.err.println("Error loading image " + path + ": " + e.getMessage());
            return createFallbackIcon(width, height);
        }
    }

    private ImageIcon createFallbackIcon(int width, int height) {
        BufferedImage fallback = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = fallback.createGraphics();
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width-1, height-1);
        g.dispose();
        return new ImageIcon(fallback);
    }

    public void clearSelection() {
        gameController.selectedPiece = null;
        gameController.selectedRow = -1;
        gameController.selectedCol = -1;
        
        
        for (int r = 0; r < GameModel.ROWS; r++) {
            for (int c = 0; c < GameModel.COLS; c++) {
                GameModel.grid[r][c].setBorder(null);
            }
        }
        requestFocus(); 
    }

    public void updateBoardDisplay() {
        for (int row = 0; row < GameModel.ROWS; row++) {
            for (int col = 0; col < GameModel.COLS; col++) {
                ImageIcon baseIcon = getTileIcon(row, col);
                if (GameModel.pieces[row][col] != null) {
                    GameModel.grid[row][col].setIcon(overlayIcons(baseIcon, GameModel.pieces[row][col].getIcon()));
                } else {
                    GameModel.grid[row][col].setIcon(baseIcon);
                }
            }
        }
    }

    public void highlightSelectedPiece(int row, int col) {
        
        GameModel.grid[row][col].setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createLineBorder(Color.GREEN, 3)
        ));
    }

}
