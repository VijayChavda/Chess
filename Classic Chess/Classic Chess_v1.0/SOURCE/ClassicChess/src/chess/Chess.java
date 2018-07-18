package chess;

import board.AbstractChessMen;
import board.ChessBoard.ChessSquare;
import static board.ChessBoard.getImgSrc;
import board.ChessMen;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Chess extends Container {

    public static final Dimension INITBOUNDS = GraphicsEnvironment.getLocalGraphicsEnvironment().
            getDefaultScreenDevice().getDefaultConfiguration().getBounds().getSize();
    public static final Dimension SQUARE_BOUND = new Dimension(
            (int) (0.060546875 * INITBOUNDS.width),
            (int) (0.0911458333333333 * INITBOUNDS.height));
    public static final Rectangle BOARD_BOUNDS = new Rectangle(
            (int) (0.2607421875 * INITBOUNDS.width),
            (int) (0.1380208333333333 * INITBOUNDS.height),
            SQUARE_BOUND.width * 8, SQUARE_BOUND.height * 8);
    public final static Dimension BORDER_BOUND = new Dimension(
            (int) (SQUARE_BOUND.width * 1.290322580645161),
            (int) (SQUARE_BOUND.height * 1.285714285714285));
    public static final int MARBLE_BOARD = 0, OAK_BOARD = 1, GLASS_BOARD = 2;
    private static final Logger LOG = Logger.getLogger(Chess.class.getName());
    private final ClassLoader imgLoader;
    private final ChessBoardImpl chessBoard;
    private final Option toggler, newGame, loadGame, saveGame, undoGame,
            redoGame, showRules, config, exitGame;
    private final Component border;
    private ArrayList<AbstractChessMen> deadMens;
    private Image borderImg;
    private Image boardImg, oakBoard, marbleBoard, glassBoard;
    private Image bgImg, oakBg, marbleBg, glassBg;
    private Image oakToggle, marbleToggle, glassToggle;
    private Image newGameImg, loadGameImg, saveGameImg, eUndoGameImg, dUndoGameImg,
            eRedoGameImg, dRedoGameImg, showRulesImg, configurationImg, exitGameImg;

    static {
        int width = (int) (0.068359375 * INITBOUNDS.width);
        int height = (int) (0.1 * INITBOUNDS.height);
//        int height = (int) (0.0911458333333333 * INITBOUNDS.height);//oldest
        Option.setBound(new Dimension(width, height));
        Option.setGap((int) (0.0111111111111111 * INITBOUNDS.height));
//        Option.setGap((int) (0.015625 * INITBOUNDS.height));//oldest
        Option.setX((int) (0.92 * INITBOUNDS.width));
    }

    public Chess() {
        chessBoard = new ChessBoardImpl(SQUARE_BOUND) {
            {
                setBoardImg(marbleBoard);
                setLocation(BOARD_BOUNDS.getLocation().x, BOARD_BOUNDS.getLocation().y);
                importChessBoard(Chess.this);
            }

            @Override
            public void onMouseEnter(ChessMen chessMen) {
                AbstractChessMen men = (AbstractChessMen) chessMen;
                border.setLocation(
                        BOARD_BOUNDS.x + ((men.getCol() - 1) * SQUARE_BOUND.width
                        - (border.getWidth() - SQUARE_BOUND.width) / 2),
                        BOARD_BOUNDS.y + ((men.getRow() - 1) * SQUARE_BOUND.height
                        - (border.getHeight() - SQUARE_BOUND.height) / 2));
                border.setVisible(true);
            }

            @Override
            public void onMouseEnter(ChessSquare sqr) {
                border.setLocation(
                        BOARD_BOUNDS.x + ((sqr.getCol() - 1) * SQUARE_BOUND.width
                        - (border.getWidth() - SQUARE_BOUND.width) / 2),
                        BOARD_BOUNDS.y + ((sqr.getRow() - 1) * SQUARE_BOUND.height
                        - (border.getHeight() - SQUARE_BOUND.height) / 2));
                border.setVisible(true);
            }

            @Override
            public void onMouseExit(ChessSquare sqr) {
                border.setVisible(false);
            }

            @Override
            public void onMouseExit(ChessMen men) {
                border.setVisible(false);
            }

            @Override
            protected void changeTurn() {
                super.changeTurn();
                refreshUR();
            }
        };
        chessBoard.newGame();
        deadMens = new ArrayList<>(chessBoard.getNoOFMen());
        new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        }).start();
    }

    {
        {
            setBounds(0, 0, INITBOUNDS.width, INITBOUNDS.height);
            setLayout(null);
        }

        {
            imgLoader = getClass().getClassLoader();
            oakBg = getImgSrc(imgLoader, "oak.jpg", INITBOUNDS.getSize());
            bgImg = marbleBg = getImgSrc(imgLoader, "marble.jpg", INITBOUNDS.getSize());
            glassBg = getImgSrc(imgLoader, "glass.jpg", INITBOUNDS.getSize());

            oakToggle = getImgSrc(imgLoader, "oak_board.jpg", Option.getBound());
            marbleToggle = getImgSrc(imgLoader, "marble_board.jpg", Option.getBound());
            glassToggle = getImgSrc(imgLoader, "glass_board.jpg", Option.getBound());

            oakBoard = getImgSrc(imgLoader, "oak_board.jpg", Chess.BOARD_BOUNDS.getSize());
            marbleBoard = getImgSrc(imgLoader, "marble_board.jpg", Chess.BOARD_BOUNDS.getSize());
            glassBoard = getImgSrc(imgLoader, "glass_board.jpg", Chess.BOARD_BOUNDS.getSize());
            boardImg = oakBoard;

            borderImg = getImgSrc(imgLoader, "borderImg.gif", BORDER_BOUND);

            newGameImg = getImgSrc(imgLoader, "newGameImg.gif", Option.getBound());
            loadGameImg = getImgSrc(imgLoader, "loadGameImg.gif", Option.getBound());
            saveGameImg = getImgSrc(imgLoader, "saveGameImg.gif", Option.getBound());
            eUndoGameImg = getImgSrc(imgLoader, "eUndoGameImg.gif", Option.getBound());
            dUndoGameImg = getImgSrc(imgLoader, "dUndoGameImg.gif", Option.getBound());
            eRedoGameImg = getImgSrc(imgLoader, "eRedoGameImg.gif", Option.getBound());
            dRedoGameImg = getImgSrc(imgLoader, "dRedoGameImg.gif", Option.getBound());
            showRulesImg = getImgSrc(imgLoader, "showRulesImg.gif", Option.getBound());
            configurationImg = getImgSrc(imgLoader, "configurationImg.gif", Option.getBound());
            exitGameImg = getImgSrc(imgLoader, "exitGameImg.gif", Option.getBound());
        }

        {
            border = new Component() {
                {
                    setSize(BORDER_BOUND.width, BORDER_BOUND.height);
                    setVisible(false);
                }

                @Override
                public void paint(Graphics g) {
                    if (border.isVisible() && chessBoard.getConfiguration().CB_showBorder.isSelected()) {
                        g.drawImage(borderImg, 0, 0, this);
                    }
                }
            };

            toggler = new Option(oakToggle) {
                int state = OAK_BOARD;

                @Override
                public void paint(Graphics g) {
                    g.setColor(Color.DARK_GRAY);
                    Rectangle r = g.getClipBounds();
                    g.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                    g.drawImage(getImg(), 4, 4, Option.getBound().width - 8, Option.getBound().height - 8, this);
                }

                @Override
                public void onClick() {
                    bgImg = state == MARBLE_BOARD ? marbleBg : state == OAK_BOARD ? oakBg : glassBg;
                    boardImg = state == MARBLE_BOARD ? marbleBoard
                            : state == OAK_BOARD ? oakBoard : glassBoard;
                    chessBoard.setBoardImg(boardImg);
                    setImg(state == MARBLE_BOARD ? oakToggle
                            : state == OAK_BOARD ? glassToggle : marbleToggle);
                    state = state == GLASS_BOARD ? MARBLE_BOARD : state + 1;
                    Chess.this.repaint();
                }
            };

            newGame = new Option(newGameImg) {
                @Override
                public void onClick() {
                    if (chessBoard.isReady()) {
                        chessBoard.newGame();
                        deadMens.clear();
                        refreshUR();
                    }
                }
            };

            loadGame = new Option(loadGameImg) {
                @Override
                public void onClick() {
                    if (chessBoard.isReady()) {
                        chessBoard.loadGame(getAConnection());
                        deadMens.clear();
                        refreshUR();
                    }
                }
            };

            saveGame = new Option(saveGameImg) {
                @Override
                public void onClick() {
                    if (chessBoard.isReady()) {
                        chessBoard.saveGame(getAConnection());
                    }
                }
            };

            undoGame = new Option(dUndoGameImg) {
                @Override
                public void onClick() {
                    if (chessBoard.isReady()) {
                        chessBoard.undoGame();
                        refreshUR();
                    }
                }
            };

            redoGame = new Option(dRedoGameImg) {
                @Override
                public void onClick() {
                    if (chessBoard.isReady()) {
                        chessBoard.redoGame();
                        refreshUR();
                    }
                }
            };

            config = new Option(configurationImg) {
                @Override
                public void onClick() {
                    if (chessBoard.isReady()) {
                        chessBoard.getConfiguration().setVisible(true);
                    }
                }
            };

            showRules = new Option(showRulesImg) {
                @Override
                public void onClick() {
                    if (chessBoard.isReady()) {
                        ChessBoardImpl.RULES.setVisible(true);
                    }
                }
            };

            exitGame = new Option(exitGameImg) {
                @Override
                public void onClick() {
                    if (chessBoard.isReady()) {
                        chessBoard.endGame();
                    }
                }
            };

            add(border);
            add(toggler);
            add(newGame);
            add(loadGame);
            add(saveGame);
            add(undoGame);
            add(redoGame);
            add(config);
            add(showRules);
            add(exitGame);
        }
    }
    final static int INIT_YW = (int) (0.0065104166666667 * INITBOUNDS.height);
    final static int INIT_YB = INIT_YW;
    final static int INIT_XW = (int) (0.01953125 * INITBOUNDS.width);
    final static int INIT_XB = (int) (0.0830078125 * INITBOUNDS.width);
    final static int W = (int) (0.0559895833333333 * INITBOUNDS.height);
    final static int H = (int) (0.0546875 * INITBOUNDS.height);

    @Override
    public void paint(Graphics g) {
        g.drawImage(bgImg, 0, 0, this);
        chessBoard.paintChessBoard(g.create(
                BOARD_BOUNDS.x, BOARD_BOUNDS.y, BOARD_BOUNDS.width, BOARD_BOUNDS.height));
        paint(g, border, toggler, newGame, loadGame, saveGame, undoGame,
                redoGame, showRules, config, exitGame);
        if (deadMens != null && chessBoard.getConfiguration().CB_showCaptured.isSelected()) {
            int yw = INIT_YW, yb = yw;
            for (AbstractChessMen deadMen : deadMens) {
                int x = deadMen.isWhite() ? INIT_XW : INIT_XB;
                int y = deadMen.isWhite() ? yw : yb;
                g.drawImage(deadMen.getImg(), x, y, W, H, this);
                y += H + INIT_YW;
                y = y > INITBOUNDS.height ? 5 : y;
                yw = deadMen.isWhite() ? y : yw;
                yb = deadMen.isBlack() ? y : yb;
            }
        }
    }

    private void paint(Graphics g, Component... components) {
        for (Component c : components) {
            c.paint(g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight()));
        }
    }

    private void refreshUR() {
        if (chessBoard.canUndo()) {
            undoGame.setImg(eUndoGameImg);
        } else {
            undoGame.setImg(dUndoGameImg);
        }
        if (chessBoard.canRedo()) {
            redoGame.setImg(eRedoGameImg);
        } else {
            redoGame.setImg(dRedoGameImg);
        }
        chessBoard.cleanChessBoard();
        reInitDeads();
        repaint();
    }

    private void reInitDeads() {
        for (ChessMen men : chessBoard.getChessMens()) {
            AbstractChessMen piece = (AbstractChessMen) men;
            if (piece.isCaptured()) {
                if (!deadMens.contains(piece)) {
                    deadMens.add(piece);
                    Chess.this.add(piece.getMenComponent());
                    validate();
                }
            } else if (deadMens.contains(piece)) {
                deadMens.remove(piece);
            }
        }
    }

    private Connection getAConnection() {
        Connection conn = null;
        String password = JOptionPane.showInputDialog(null, "Enter your local sql database password.", "Enter password", JOptionPane.QUESTION_MESSAGE);
        try {
            Class.forName("java.sql.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/", "root", password);
        } catch (ClassNotFoundException | SQLException ex) {
            LOG.log(Level.SEVERE, "Failed to connect to database... Connot save or load games using SQL.\n", ex);
            JOptionPane.showMessageDialog(null, "Could not connect to local database...",
                    "Database Connectivity problem.", JOptionPane.ERROR_MESSAGE);
        }
        return conn;
    }
}