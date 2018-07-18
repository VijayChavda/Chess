package board;

import chess.ChessGame;
import chess.RecordReversionException;
import chess.Team;
import chess.Type;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class ChessBoard implements ChessGame {

    private final Container board;
    public final Dimension squareSize;
    private static final ChessSquare[] chessSquares = new ChessSquare[64];
    private final HashSet<ChessMen> chessMens = new HashSet<>(32);
    private Team turn;
    private ChessMen selectedMen;
    private final Component arrival, departure;
    private Icon iconImg;
    private Image boardImg, selectionImg, arrivalImg, departureImg;
    private HashSet<Trajector> trajectors;
    private int recordIndex = -1, noOfRecords = 0;
    private static final Logger LOG = Logger.getLogger(ChessBoard.class.
            getName());

    public ChessBoard(Dimension squareSize) {
        this.squareSize = squareSize;
        trajectors = new HashSet<>(1);
        board = new Container() {
            {
                setLayout(null);
                setSize(ChessBoard.this.squareSize.width * 8,
                        ChessBoard.this.squareSize.height * 8);
            }

            @Override
            public void paint(Graphics g) {
                g.drawImage(boardImg, 0, 0, board);
                for (ChessSquare square : chessSquares) {
                    Component c = square.getSquareComponent();
                    c.paint(g.create(c.getX(), c.getY(), c.getWidth(), c.
                            getHeight()));
                }
                for (Iterator<ChessMen> mens = chessMens.iterator(); mens.
                        hasNext();) {
                    ChessMen men = mens.next();
                    Component m = men.getMenComponent();
                    m.paint(g.create(m.getX(), m.getY(), m.getWidth(), m.getHeight()));
                }
                if (showLastMoves()) {
                    arrival.paint(g.create(arrival.getX(), arrival.getY(),
                            arrival.getWidth(), arrival.getHeight()));
                    departure.paint(g.create(departure.getX(), departure.getY(),
                            departure.getWidth(), departure.getHeight()));
                }
            }
        };
        arrival = new Component() {
            {
                setSize(ChessBoard.this.squareSize.width,
                        ChessBoard.this.squareSize.height);
                setVisible(false);
            }

            @Override
            public void paint(Graphics g) {
                if (arrival.isVisible()) {
                    g.drawImage(arrivalImg, 0, 0, this);
                }
            }
        };
        departure = new Component() {
            {
                setSize(ChessBoard.this.squareSize.width,
                        ChessBoard.this.squareSize.height);
                setVisible(false);
            }

            @Override
            public void paint(Graphics g) {
                if (departure.isVisible()) {
                    g.drawImage(departureImg, 0, 0, this);
                }
            }
        };
        turn = Team.WHITE;
        initPics();
        initChessComponents();
    }

    private void initPics() {
        selectionImg = getImgSrc("selectionImg.jpg");
        iconImg = new ImageIcon(getImgSrc("IconImg.gif"));
        arrivalImg = getImgSrc("arrival.gif");
        departureImg = getImgSrc("departure.gif");
    }

    private void initChessComponents() {
        int row = 1, col = 1;
        Image img = null;
        for (int i = 0; i < chessSquares.length; i++) {
            chessSquares[i] = new ChessSquare(row, col, img);
            board.add(chessSquares[i].square, 0);
            row = col == 8 ? row + 1 : row;
            col = col == 8 ? 1 : col + 1;
        }
    }

    private static void checkRowCol(int row, int col) {
        boolean wrongRow, wrongCol;
        wrongRow = row > 8 || row < 0;
        wrongCol = col > 8 || col < 0;
        if (wrongCol && wrongRow) {
            throw new IllegalArgumentException("Invalid value for a row = "
                    + row + "and col = " + col
                    + ". Row & column must range from 1 to 8");
        } else if (wrongRow) {
            throw new IllegalArgumentException("Invalid value for a row = "
                    + row + ". The row must range from 1 to 8");
        } else if (wrongCol) {
            throw new IllegalArgumentException("Invalid value for a col = "
                    + col + ". The row must range from 1 to 8");
        }
    }

    public void importChessBoard(Container parent) {
        parent.add(board);
    }

    public static Image getImgSrc(ClassLoader loader, String imgName,
            Dimension scale) {
        if (imgName == null) {
            return null;
        }
        Image img;
        try {
            img = ImageIO.read(loader.getResource("images/" + imgName));
            if (img != null) {
                return img.getScaledInstance(scale.width, scale.height,
                        Image.SCALE_SMOOTH);
            }
        } catch (IOException | IllegalArgumentException ex) {
            Logger.getLogger("Problem in loading Image.").log(Level.SEVERE,
                    "Could not load the Image whose provided name was \"{0}\".\n",
                    imgName);
        }
        return null;
    }

    public Image getImgSrc(String imgName) {
        return getImgSrc(ChessBoard.class.getClassLoader(), imgName, squareSize);
    }

    public Image getBoardImg() {
        return boardImg;
    }

    public Image getSelectionImg() {
        return selectionImg;
    }

    public Icon getIconImg() {
        return iconImg;
    }

    public Image getArrivalImg() {
        return arrivalImg;
    }

    public Image getDepartureImg() {
        return departureImg;
    }

    public void setSelectionImg(Image selectionImg) {
        this.selectionImg = selectionImg;
    }

    public void setIconImg(Icon iconImg) {
        this.iconImg = iconImg;
    }

    public void setArrivalImg(Image arrivalImg) {
        this.arrivalImg = arrivalImg;
    }

    public void setDepartureImg(Image departureImg) {
        this.departureImg = departureImg;
    }

    public void paintChessBoard(Graphics g) {
        board.paint(g);
    }

    public void repaintBoard() {
        board.repaint();
    }

    public Graphics getGraphic() {
        return board.getGraphics();
    }

    public Point getLocation() {
        return board.getLocation();
    }

    public void setLocation(int x, int y) {
        board.setLocation(x, y);
    }

    public Dimension getSize() {
        return board.getSize();
    }

    public Dimension getSquareSize() {
        return squareSize;
    }

    public Team getTurn() {
        return turn;
    }

    protected void setTurn(Team turn) {
        this.turn = turn;
    }

    public Team getOppositeTeam(Team team) {
        return Team.isWhite(team) ? Team.BLACK : Team.WHITE;
    }

    void turnChange() {
        changeTurn();
    }

    protected void changeTurn() {
        AbstractChessMen.lastCaptured++;
        Pawn.lastMoved++;
        turn = Team.isWhite(turn) ? Team.BLACK : Team.WHITE;
        getKing(turn).checkCheck();
        boolean endGame = false;
        boolean insuffMaterial = chkInsufficientMaterial();
        boolean staleMate = chkStaleMate();
        boolean noPawnMove = chkNoPawnMove(50);
        boolean noCapture = chkNoCapture(50);
        if (insuffMaterial) {
            showMessage(
                    "<html><center><font color = red size = 6><b>GAME DRAW</b></font>"
                    + "</center><br>" + "<font size = 3 color = black> The game cannot "
                    + "continue with limited pieces.<br>" + " There is insufficient material "
                    + "for an checkmate to occur.</html>");
            endGame = true;
        } else if (staleMate && getKing().checkCheck()) {
            String name = Team.isWhite(turn) ? getPlayer1Name()
                    : getPlayer2Name();
            showMessage("<html><center><font color = red size = 6><b>GAME OVER</b>"
                    + "</font></center><br>" + "<font size = 3 color = black> " + name
                    + " loses due to CHECKMATE!.</html>");
            endGame = true;
        } else if (staleMate) {
            String name = Team.isWhite(turn) ? getPlayer1Name()
                    : getPlayer2Name();
            showMessage("<html><center><font color = red size = 6><b>GAME DRAW</b>"
                    + "</font></center><br>" + "<font size = 3 color = black> The game has been"
                    + " drawn due to STALEMATE.<br> " + name + "'s" + " king has no possible"
                    + " move to play.</html>");
            endGame = true;
        } else if (noPawnMove) {
            showMessage(
                    "<html><center><font color = red size = 6><b>GAME DRAW</b></font>"
                    + "</center><br>" + "<font size = 3 color = black> The game has been drawn"
                    + " as no Pawn has moved for last 50 moves!.<br></html>");
            endGame = true;
        } else if (noCapture) {
            showMessage(
                    "<html><center><font color = red size = 6><b>GAME DRAW</b></font>"
                    + "</center><br>" + "<font size = 3 color = black> The game has been drawn"
                    + " as there has been no progress in last 50 moves!.<br></html>");
            endGame = true;
        }
        if (endGame) {
            end();
            if (promptYesNo("Do you wish to start a new game...?")) {
                newGame();
            }
            return;
        }
        removeGarbageRecords();
        fillRecords();
    }

    public ChessSquare[] getChessSquares() {
        return chessSquares;
    }

    public HashSet<ChessMen> getChessMens() {
        return chessMens;
    }

    public ChessMen[] getChessMensAsArray() {
        return chessMens.toArray(new ChessMen[1]);
    }

    public final King getKing() {
        return getKing(turn);
    }

    public final King getKing(Team team) {
        for (Iterator<ChessMen> mens = chessMens.iterator(); mens.hasNext();) {
            ChessMen men = mens.next();
            if (men instanceof King && men.getTeam() == team) {
                return (King) men;
            }
        }
        throw new NullPointerException("The King of " + team
                + " team does not exists...");
    }

    public final King getWhiteKing() {
        return getKing(Team.WHITE);
    }

    public final King getBlackKing() {
        return getKing(Team.BLACK);
    }

    public ChessMen getSelectedMen() {
        return selectedMen;
    }

    void setSelectedMen(ChessMen selectedMen) {
        this.selectedMen = selectedMen;
    }

    public Component getArrivalIndicator() {
        return arrival;
    }

    public Component getDepartureIndicator() {
        return departure;
    }

    public void setArrivalAt(int row, int col) {
        arrival.setLocation((col - 1) * squareSize.width, (row - 1)
                * squareSize.height);
    }

    public void setArrivalAt(ChessSquare sqr) {
        setArrivalAt(sqr.getRow(), sqr.getCol());
    }

    public void showIndicators() {
        if (!arrival.getLocation().equals(departure.getLocation())) {
            arrival.setVisible(true);
            departure.setVisible(true);
        }
    }

    public void hideIndicators() {
        arrival.setVisible(false);
        departure.setVisible(false);
    }

    public void forgetIndicators() {
        getArrivalIndicator().setLocation(-squareSize.width, -squareSize.height);
        getDepartureIndicator().setLocation(-squareSize.width,
                -squareSize.height);
        hideIndicators();
    }

    public void setDepartureAt(int row, int col) {
        departure.setLocation((col - 1) * squareSize.width, (row - 1)
                * squareSize.height);
    }

    public void setDepartureAt(ChessSquare sqr) {
        setDepartureAt(sqr.getRow(), sqr.getCol());
    }

    public Trajector getTrajectorOf(ChessMen men) {
        for (Iterator<Trajector> it = trajectors.iterator(); it.hasNext();) {
            Trajector traj = it.next();
            if (traj.getChessMen() == men) {
                return traj;
            }
        }
        LOG.log(Level.SEVERE, "Could not get a trajector of {0}", men instanceof Pawn);
        return null;
    }

    public Trajector[] getTrajectorsAsArray() {
        Trajector[] trajs = trajectors.toArray(new Trajector[1]);
        return trajs;
    }

    protected HashSet<Trajector> getTrajectors() {
        return trajectors;
    }

    public void setTrajectors(HashSet<Trajector> trajectors) {
        this.trajectors = trajectors;
    }

    public int getRecordIndex() {
        return recordIndex;
    }

    public void setRecordIndex(int recordIndex) {
        this.recordIndex = recordIndex;
    }

    public int getNoOfRecords() {
        return noOfRecords;
    }

    public void setNoOfRecords(int noOfRecords) {
        this.noOfRecords = noOfRecords;
    }

    public int getNoOFMen() {
        return chessMens.size();
    }

    public static ChessSquare getChessSquareAt(int row, int col) {
        checkRowCol(row, col);
        return getChessSquare(row, col);
    }

    static ChessSquare getChessSquare(int row, int col) {
        for (ChessSquare sqr : chessSquares) {
            if (sqr.row == row && sqr.col == col) {
                return sqr;
            }
        }
        return null;
    }

    public boolean isEmptySquare(ChessSquare square) {
        return getChessMenAt(square) == null;
    }

    public boolean isEmptySquare(int row, int col) {
        return getChessMenAt(row, col) == null;
    }

    public boolean areEmptySquares(ChessSquare... squares) {
        for (ChessSquare sqr : squares) {
            if (!isEmptySquare(sqr)) {
                return false;
            }
        }
        return true;
    }

    public ChessSquare getFirstEmptySquare() {
        for (ChessSquare sqr : getChessSquares()) {
            if (sqr.isEmpty()) {
                return sqr;
            }
        }
        return null;
    }

    public ChessMen getChessMenAt(Point pt) {
        for (Iterator<ChessMen> mens = chessMens.iterator(); mens.hasNext();) {
            synchronized (mens) {
                ChessMen men = mens.next();
                if (!men.isCaptured() && board.getComponentAt(pt).equals(men)) {
                    return men;
                }
            }
        }
        return null;
    }

    public ChessMen getChessMenAt(ChessSquare square) {
        return getChessMenAt(square.row, square.col);
    }

    ChessMen getChessMen(ChessSquare square) {
        return getChessMen(square.row, square.col);
    }

    public ChessMen getChessMenAt(int row, int col) {
        checkRowCol(row, col);
        return getChessMen(row, col);
    }

    ChessMen getChessMen(int row, int col) {
        for (Iterator<ChessMen> mens = chessMens.iterator(); mens.hasNext();) {
            synchronized (mens) {
                ChessMen men = mens.next();
                ChessSquare sqr = men.getPosition();
                if (!men.isCaptured() && sqr.row == row && sqr.col == col) {
                    return men;
                }
            }
        }
        return null;
    }

    protected void addChessMen(ChessMen piece) {
        addChessMen(piece, new Trajector(piece));
    }

    protected void addChessMen(ChessMen piece, Trajector newTrajector) {
        if (piece == null) {
            throw new NullPointerException(
                    "Null value not allowed for a ChessMen.");
        }
        if (chessMens.size() >= 64) {
            throw new ArrayIndexOutOfBoundsException(
                    "The ChessBoard is full... No more ChessMen cannot be added.");
        }
        board.add(piece.getMenComponent(), 0);
        chessMens.add(piece);
        if (newTrajector != null) {
            trajectors.add(newTrajector);
        }
    }

    public void removeChessMen(ChessMen piece) {
        if (piece != null) {
            board.remove(piece.getMenComponent());
        }
    }

    public void removeAllChessMen() {
        for (Iterator<ChessMen> it = chessMens.iterator(); it.hasNext();) {
            ChessMen men = it.next();
            board.remove(men.getMenComponent());
        }
        chessMens.clear();
    }

    protected void fillRecords() {
        recordIndex++;
        for (Iterator<Trajector> trajs = trajectors.iterator(); trajs.hasNext();) {
            Trajector t = trajs.next();
            t.removeRecord(recordIndex);
            t.addRecord(recordIndex);
        }
        noOfRecords++;
    }

    protected void removeGarbageRecords() {
        for (Trajector t : trajectors) {
            for (int i = recordIndex + 1; i <= noOfRecords; i++) {
                t.removeRecord(recordIndex + 1);
            }
        }
        noOfRecords = recordIndex;
    }

    protected void revertToRecord(int recordIndex) throws
            RecordReversionException {
        cleanChessBoard();
        forgetIndicators();
        for (Iterator<Trajector> trajs = trajectors.iterator(); trajs.hasNext();) {
            Trajector t = trajs.next();
            Record past = t.getRecord(recordIndex);
            ChessMen men = t.getChessMen();
            Pawn deadPawn = null;
            for (Iterator<Pawn> it = Pawn.getPromoted().iterator(); it.hasNext();) {
                Pawn pPawn = it.next();
                if (pPawn.getPosition() == men.getPosition()
                        && pPawn.getTeam() == men.getTeam()) {
                    deadPawn = pPawn;
                }
            }
            if (deadPawn != null && past == null) {
                addChessMen(deadPawn, getTrajectorOf(deadPawn));
                removeChessMen(men);
                chessMens.remove(men);
                trajectors.remove(getTrajectorOf(men));
                setTurn(recordIndex % 2 == 0 ? Team.WHITE : Team.BLACK);
                this.recordIndex = recordIndex;
                return;
            }
            if (past != null) {
                if (men.isCaptured() && !past.isCaptured) {
                    addChessMen(men, null);
                }
                men.setPosition(past.position);
                men.setCaptured(past.isCaptured);
                men.setMoved(past.hasMoved);
            } else {
                throw new RecordReversionException("Cannot revert to the record whose"
                        + " index was " + recordIndex + ". No record was present at that index.");
            }
            setTurn(recordIndex % 2 == 0 ? Team.WHITE : Team.BLACK);
            this.recordIndex = recordIndex;
        }
    }

    public boolean isActive() {
        return selectedMen != null;
    }

    public boolean isReady() {
        return true;
    }

    public void showAllLegalMoves() {
        if (!showValidMoves()) {
            return;
        }
        for (Iterator<ChessMen> it = chessMens.iterator(); it.hasNext();) {
            ChessMen mens = it.next();
            mens.showScope();
        }
    }

    public void cleanChessBoard() {
        for (ChessSquare square : chessSquares) {
            square.setImg(null);
        }
    }

    public void refreshStances() {
        for (ChessSquare square : chessSquares) {
            square.setStance(ChessMen.NONE);
        }
        selectedMen = null;
    }

    public void onMouseEnter(ChessSquare sqr) {
        //may be overriden...
    }

    public void onMouseEnter(ChessMen men) {
        //may be overriden...
    }

    public void onMouseExit(ChessSquare sqr) {
        //may be overriden...
    }

    public void onMouseExit(ChessMen men) {
        //may be overriden...
    }

    public final boolean chkInsufficientMaterial() {
        if (!getKing(turn).checkCheck()) {
            boolean onewN = false, onewB = false;
            boolean onebN = false, onebB = false;
            boolean wk = true, wkn = false, wkb = false;
            boolean bk = true, bkn = false, bkb = false;
            boolean wkbbkb;
            char wbType = 0, bbType = 0;
            for (ChessMen men : chessMens) {
                AbstractChessMen man = (AbstractChessMen) men;
                if (!man.isCaptured()) {
                    if (man instanceof Bishop) {
                        wkb = !wkb ? (!onewB && !wkn && man.isWhite()) : wkb;
                        bkb = !bkb ? (!onebB && !bkn && man.isBlack()) : bkb;
                        wbType = wbType != 0 ? (((man.getRow() % 2 != 0 && man.
                                getCol() % 2 != 0) || (man.getRow() % 2 == 0
                                && man.getCol() % 2 == 0)) && man.isWhite() ? 'w'
                                : 'b') : wbType;
                        bbType = bbType != 0 ? (((man.getRow() % 2 != 0 && man.
                                getCol() % 2 != 0) || (man.getRow() % 2 == 0
                                && man.getCol() % 2 == 0)) && man.isBlack() ? 'w'
                                : 'b') : bbType;
                        onewB = !onewB ? man.isWhite() : onewB;
                        onebB = !onebB ? man.isBlack() : onebB;
                    } else if (man instanceof Knight) {
                        wkn = !onewN && !wkb && man.isWhite();
                        bkn = !onebN && !bkb && man.isBlack();
                        onewN = man.isWhite();
                        onebN = man.isBlack();
                    } else if (man instanceof King == false) {
                        return false;
                    }
                }
            }
            wk = wk && !wkb && !wkn;
            bk = bk && !bkb && !bkn;
            wkbbkb = wkb && bkb && (bbType == wbType);
            return ((wk && bk) || (wk && bkn) || (bk && wkn) || (wk && bkb)
                    || (bk && wkb) || (wkbbkb));
        }
        return false;
    }

    public boolean chkStaleMate() {
        for (ChessMen men : chessMens) {
            AbstractChessMen chessMen = (AbstractChessMen) men;
            if (chessMen.isFriend()) {
                for (Iterator<ChessSquare> it = chessMen.getNetScope().
                        iterator(); it.hasNext();) {
                    ChessSquare sqr = it.next();
                    if (sqr.getStance() != MenStances.NONE && sqr.getStance()
                            != MenStances.OBSTRUCTED && sqr.getStance()
                            != MenStances.TRAITORING) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean chkNoPawnMove(int n) {
        return Pawn.lastMoved > n;
    }

    public boolean chkNoCapture(int n) {
        return AbstractChessMen.lastCaptured > n;
    }

    @Override
    public String toString() {
        return "[location = " + getLocation() + ", size = " + getSize()
                + ", noOfChessMens = " + chessMens.size() + "]";
    }

    @Override
    public void newGame() {
        Pawn.lastMoved = 0;
        recordIndex = -1;
        trajectors.clear();
        cleanChessBoard();
        forgetIndicators();
        removeAllChessMen();
        setTurn(Team.WHITE);
        (new Rook(this, getChessSquares()[0], Team.BLACK)).spawnPiece();
        (new Knight(this, getChessSquares()[1], Team.BLACK)).spawnPiece();
        (new Bishop(this, getChessSquares()[2], Team.BLACK)).spawnPiece();
        (new Queen(this, getChessSquares()[3], Team.BLACK)).spawnPiece();
        (new King(this, getChessSquares()[4], Team.BLACK)).spawnPiece();
        (new Bishop(this, getChessSquares()[5], Team.BLACK)).spawnPiece();
        (new Knight(this, getChessSquares()[6], Team.BLACK)).spawnPiece();
        (new Rook(this, getChessSquares()[7], Team.BLACK)).spawnPiece();
        (new Pawn(this, getChessSquares()[8], Team.BLACK)).spawnPiece();
        (new Pawn(this, getChessSquares()[9], Team.BLACK)).spawnPiece();
        (new Pawn(this, getChessSquares()[10], Team.BLACK)).spawnPiece();
        (new Pawn(this, getChessSquares()[11], Team.BLACK)).spawnPiece();
        (new Pawn(this, getChessSquares()[12], Team.BLACK)).spawnPiece();
        (new Pawn(this, getChessSquares()[13], Team.BLACK)).spawnPiece();
        (new Pawn(this, getChessSquares()[14], Team.BLACK)).spawnPiece();
        (new Pawn(this, getChessSquares()[15], Team.BLACK)).spawnPiece();
        (new Pawn(this, getChessSquares()[48], Team.WHITE)).spawnPiece();
        (new Pawn(this, getChessSquares()[49], Team.WHITE)).spawnPiece();
        (new Pawn(this, getChessSquares()[50], Team.WHITE)).spawnPiece();
        (new Pawn(this, getChessSquares()[51], Team.WHITE)).spawnPiece();
        (new Pawn(this, getChessSquares()[52], Team.WHITE)).spawnPiece();
        (new Pawn(this, getChessSquares()[53], Team.WHITE)).spawnPiece();
        (new Pawn(this, getChessSquares()[54], Team.WHITE)).spawnPiece();
        (new Pawn(this, getChessSquares()[55], Team.WHITE)).spawnPiece();
        (new Rook(this, getChessSquares()[56], Team.WHITE)).spawnPiece();
        (new Knight(this, getChessSquares()[57], Team.WHITE)).spawnPiece();
        (new Bishop(this, getChessSquares()[58], Team.WHITE)).spawnPiece();
        (new Queen(this, getChessSquares()[59], Team.WHITE)).spawnPiece();
        (new King(this, getChessSquares()[60], Team.WHITE)).spawnPiece();
        (new Bishop(this, getChessSquares()[61], Team.WHITE)).spawnPiece();
        (new Knight(this, getChessSquares()[62], Team.WHITE)).spawnPiece();
        (new Rook(this, getChessSquares()[63], Team.WHITE)).spawnPiece();
        fillRecords();
        repaintBoard();
    }

    @Override
    public void endGame() {
        end();
    }

    private void end() {
        recordIndex = -1;
        trajectors.clear();
        cleanChessBoard();
        forgetIndicators();
        removeAllChessMen();
        setTurn(Team.WHITE);
        repaintBoard();
    }

    @Override
    public void showTip(Object message, Image img) {
        if (showTips()) {
            message = ("<html><font size = 4><b>" + message + "</b></html>");
            showMessageDialog(board, message, "Chess",
                    JOptionPane.INFORMATION_MESSAGE, (img == null ? iconImg
                    : new ImageIcon(img)));
        }
        repaintBoard();
    }

    protected void showMessage(String msg) {
        msg = ("<html><b><font size = 3> " + msg + "</b></font></html>");
        showMessageDialog(board, msg, "Chess", JOptionPane.WARNING_MESSAGE,
                iconImg);
        repaintBoard();
    }

    @Override
    public boolean promptYesNo(String message) {
        message = ("<html><font size = 4><b>" + message + "</b></font></html>");
        return showConfirmDialog(null, message, "Chess",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    @Override
    public boolean showLastMoves() {
        return true;
    }

    @Override
    public boolean showAnimations() {
        return true;
    }

    @Override
    public boolean showValidMoves() {
        return true;
    }

    @Override
    public boolean showTips() {
        return true;
    }

    @Override
    public Type showPromotion(Team team) {
        Object[] choices = new Object[]{"Queen", "Rook", "Bishop", "Knight"};
        int pType = JOptionPane.showOptionDialog(board,
                "Select a promotion for your pawn...",
                "You can promote your pawn!!!", JOptionPane.OK_OPTION,
                JOptionPane.ERROR_MESSAGE, iconImg, choices, null);
        return pType == 1 ? Type.ROOK : pType == 2 ? Type.BISHOP : pType == 3
                ? Type.KNIGHT : Type.QUEEN;
    }

    @Override
    public String getPlayer1Name() {
        return "WHITE";
    }

    @Override
    public String getPlayer2Name() {
        return "BLACK";
    }

    public void setBoardImg(Image boardImg) {
        this.boardImg = boardImg;
    }

    public class ChessSquare {

        private int row, col;
        private int stance;
        private Image img;
        private final Component square;

        private ChessSquare(int row, int col, Image img) {
            this.row = row;
            this.col = col;
            this.img = img;
            square = new Component() {
                {
                    setLocation((ChessSquare.this.col - 1) * squareSize.width,
                            (ChessSquare.this.row - 1) * squareSize.height);
                    setSize(squareSize);
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            onClick();
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            onMouseEnter(ChessSquare.this);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            onMouseExit(ChessSquare.this);
                        }
                    });
                }

                @Override
                public void paint(Graphics g) {
                    g.drawImage(ChessSquare.this.img, 0, 0, this);
                }
            };
        }

        public void paint(Graphics g) {
            square.paint(g);
        }

        public void repaintSquare() {
            square.repaint();
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public int getStance() {
            return stance;
        }

        void setStance(int stance) {
            this.stance = stance;
        }

        public Image getImage() {
            return img;
        }

        public void setImg(Image img) {
            this.img = img;
            repaintSquare();
        }

        Component getSquareComponent() {
            return square;
        }

        public void addMouseListener(MouseListener ml) {
            square.addMouseListener(ml);
        }

        public void onClick() {
            if (isActive()) {
                cleanChessBoard();
                selectedMen.actOnClick(this);
            }
            showIndicators();
            refreshStances();
        }

        public boolean isEmpty() {
            if (getChessMen(this) == null) {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "[row = " + row + ", col = " + col + ", stance = " + stance + "]";
        }
    }

    public static class Trajector {

        private ChessMen hostMen;
        private ArrayList<Record> records;

        public Trajector(ChessMen hostMen) {
            this.hostMen = hostMen;
            records = new ArrayList<>(1);
        }

        public ChessMen getChessMen() {
            return hostMen;
        }

        public void setChessMen(ChessMen men) {
            this.hostMen = men;
        }

        public void addRecord(Record record) {
            records.add(record);
        }

        public void addRecords(ArrayList<Record> records) {
            this.records.addAll(records);
        }

        public void addRecord(int index) {
            Record record = new Record(
                    hostMen.getPosition(), hostMen.hasMoved(), hostMen.isCaptured());
            records.add(index, record);
        }

        public int getNoOfRec() {
            return records.size();
        }

        public void removeRecord(int index) {
            if (getRecord(index) != null) {
                records.remove(index);
            }
        }

        public void removeAllRecords() {
            records.clear();
        }

        public ArrayList<Record> getRecords() {
            return records;
        }

        public Record getRecord(int index) {
            if (index >= 0 && index < records.size()) {
                return records.get(index);
            }
            return null;
        }
    }

    public static class Record {

        private ChessBoard.ChessSquare position;
        private boolean hasMoved, isCaptured;

        public Record(ChessBoard.ChessSquare position, boolean hasMoved,
                boolean isCaptured) {
            this.position = position;
            this.hasMoved = hasMoved;
            this.isCaptured = isCaptured;
        }

        public ChessBoard.ChessSquare getPosition() {
            return position;
        }

        public boolean hasMoved() {
            return hasMoved;
        }

        public boolean isCaptured() {
            return isCaptured;
        }

        @Override
        public String toString() {
            int row = position.getRow(), col = position.getCol();
            int moved = hasMoved ? 1 : 0;
            int captured = isCaptured ? 1 : 0;
            return "[row = " + row + ", col = " + col + ", isMoved = " + moved
                    + ", isCaptured = " + captured + "]";
        }
    }
}