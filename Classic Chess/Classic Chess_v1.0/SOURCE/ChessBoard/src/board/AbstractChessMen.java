package board;

import board.ChessBoard.ChessSquare;
import static board.ChessBoard.getChessSquareAt;
import chess.Team;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.Timer;

public abstract class AbstractChessMen implements ChessMen {

    public static final int LIGHTSPEED = -1;
    public static final int SLOW = 40;
    public static final int MID = 10;
    public static final int FAST = 1;
    static int lastCaptured;
    public final ChessBoard chessBoard;
    private final Component men;
    private ChessSquare position;
    private final Team team;
    private boolean hasMoved = false;
    private boolean isCaptured = false;
    private Timer animator;
    private int speed = MID;
    private Image simpleMoveImg, protectingMoveImg, captureMoveImg, traitoringMoveImg;

    public AbstractChessMen(AbstractChessMen men) {
        this(men.chessBoard, men.position, men.team);
    }

    public AbstractChessMen(ChessBoard chessBoard, Team team) {
        this(chessBoard, null, team);
    }

    public AbstractChessMen(ChessBoard chessBoard, ChessSquare position, Team team) {
        this.chessBoard = chessBoard;
        this.position = position;
        this.team = team;
        initPics();
        men = new Component() {
            {
                setSize(getChessBoard().getSquareSize());
                if (AbstractChessMen.this.position != null) {
                    setLocation((getCol() - 1) * getWidth(), (getRow() - 1) * getHeight());
                }
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ChessMen chessMen = getChessBoard().getChessMen(getRow(), getCol());
                        if (chessMen != null) {
                            chessMen.onClick();
                        } else {
                            getPosition().onClick();
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        AbstractChessMen.this.chessBoard.onMouseEnter(AbstractChessMen.this);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        AbstractChessMen.this.chessBoard.onMouseExit(AbstractChessMen.this);
                    }
                });
            }

            @Override
            public void paint(Graphics g) {
                if (!isCaptured()) {
                    g.drawImage(getImg(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
    }

    private void initPics() {
        simpleMoveImg = chessBoard.getImgSrc("simpleMoveImg.jpg");
        protectingMoveImg = chessBoard.getImgSrc("protectingMoveImg.jpg");
        captureMoveImg = chessBoard.getImgSrc("capturingImg.jpg");
        traitoringMoveImg = chessBoard.getImgSrc("threateningImg.jpg");
    }

    public void addMouseListener(MouseListener ml) {
        men.addMouseListener(ml);
    }

    public Image getSimpleMoveImg() {
        return simpleMoveImg;
    }

    public Image getProtectingMoveImg() {
        return protectingMoveImg;
    }

    public Image getCapturingMoveImg() {
        return captureMoveImg;
    }

    public Image getTraitoringImg() {
        return traitoringMoveImg;
    }

    public void setSimpleMoveImg(Image simpleMoveImg) {
        this.simpleMoveImg = simpleMoveImg;
    }

    public void setProtectingMoveImg(Image protectingMoveImg) {
        this.protectingMoveImg = protectingMoveImg;
    }

    public void setCaptureMoveImg(Image captureMoveImg) {
        this.captureMoveImg = captureMoveImg;
    }

    public void setTraitoringMoveImg(Image traitoringMoveImg) {
        this.traitoringMoveImg = traitoringMoveImg;
    }

    @Override
    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    @Override
    public Component getMenComponent() {
        return men;
    }

    public int getRow() {
        if (position == null) {
            throw new NullPointerException("The position of this ChessMen is not defined.");
        }
        return position.getRow();
    }

    public int getCol() {
        if (position == null) {
            throw new NullPointerException("The position of this ChessMen is not defined.");
        }
        return position.getCol();
    }

    @Override
    public ChessSquare getPosition() {
        return position;
    }

    @Override
    public void setPosition(ChessSquare position) {
        this.position = position;
        locate();
    }

    public void setPosition(int row, int col) {
        setPosition(ChessBoard.getChessSquareAt(row, col));
    }

    @Override
    public Team getTeam() {
        return team;
    }

    public Team getOpponentTeam() {
        return Team.getOpponentTeam(team);
    }

    public boolean isWhite() {
        return Team.isWhite(team);
    }

    public boolean isBlack() {
        return Team.isBlack(team);
    }

    public int getAnimSpeed() {
        return speed;
    }

    public void setAnimSpeed(int speed) {
        this.speed = speed;
    }

    public Movement[] getMovementAsArray() {
        return getMovement().toArray(new Movement[1]);
    }

    @Override
    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public boolean isCaptured() {
        return isCaptured;
    }

    public Timer getAnimator() {
        return animator;
    }

    public boolean isFriend() {
        return team == chessBoard.getTurn();
    }

    public boolean isSelected() {
        return equals(chessBoard.getSelectedMen());
    }

    public void spawnPiece() {
        chessBoard.addChessMen(this);
    }

    @Override
    public void showScope() {
        for (ChessSquare sqr : chessBoard.getChessSquares()) {
            if (sqr.getStance() == ChessMen.MOVING) {
                sqr.setImg(
                        chessBoard.getKing().checkCheck() ? protectingMoveImg : simpleMoveImg);
            }
            if (sqr.getStance() == ChessMen.CAPTURING) {
                sqr.setImg(captureMoveImg);
            }
            if (sqr.getStance() == ChessMen.TRAITORING) {
                sqr.setImg(traitoringMoveImg);
            }
            if (sqr.getStance() == King.CASTLING) {
                sqr.setImg(chessBoard.getImgSrc("castlingImg.jpg"));
            }
        }
    }

    @Override
    public void capture() {
        lastCaptured = 0;
        isCaptured = true;
        chessBoard.removeChessMen(this);
    }

    @Override
    public void setCaptured(boolean captured) {
        this.isCaptured = captured;
    }

    @Override
    public void setMoved(boolean moved) {
        this.hasMoved = moved;
    }

    public ArrayList<ChessSquare> getNetScope() {
        ArrayList<ChessSquare> sqrsScope = new ArrayList<>(1);
        for (Iterator<AbstractMovement> it = getMovement().iterator(); it.hasNext();) {
            AbstractMovement move = it.next();
            move.assignScope(true);
            sqrsScope.addAll(move.getScope());
        }
        return sqrsScope;
    }

    @Override
    public void defineStances() {
        if (getMovement() == null) {
            throw new NullPointerException("No movements is assigned for this ChessMen");
        }
        Set<ChessSquare> netScope = new HashSet<>(64);
        for (Iterator<AbstractMovement> mvs = getMovement().iterator(); mvs.hasNext();) {
            AbstractMovement move = mvs.next();
            move.assignScope(true);
            netScope.addAll(move.getScope());
        }
    }

    @Override
    public final void onClick() {
        if (!chessBoard.isReady()) {
            return;
        }
        chessBoard.cleanChessBoard();
        chessBoard.hideIndicators();
        if (isFriend()) {
            if (!isSelected()) {
                chessBoard.refreshStances();
                chessBoard.setSelectedMen(this);
                defineStances();
                chessBoard.showAllLegalMoves();
            } else {
                chessBoard.showIndicators();
                chessBoard.setSelectedMen(null);
                chessBoard.refreshStances();
                chessBoard.cleanChessBoard();
            }
            if (isSelected()) {
                position.setImg(chessBoard.getSelectionImg());
            }
        } else {
            if (chessBoard.isActive()) {
                actOnClick(position);
                position.onClick();
            } else {
                String pName = Team.isWhite(chessBoard.getTurn())
                        ? chessBoard.getPlayer1Name() : chessBoard.getPlayer2Name();
                chessBoard.showTip("Its " + pName + "'s turn.", null);
                chessBoard.showIndicators();
            }
        }
    }

    @Override
    public void actOnClick(ChessSquare onSqr) {
        int stance = onSqr.getStance();
        switch (stance) {
            case NONE:
                String info = getInfo();
                if (position != onSqr) {
                    chessBoard.showTip("This is not a legal move for a " + getMenName() + ".<br></b><font size = 3>" + info, getImg());
                }
                break;
            case OBSTRUCTED:
                chessBoard.showTip("The path is obstructed...<br></b> This " + getMenName()
                        + " cannot move there..", getImg());
                break;
            case MOVING:
                moveTo(onSqr);
                break;
            case CAPTURING:
                if (position != onSqr) {
                    moveTo(onSqr);
                } else {
                    capture();
                }
                break;
            case TRAITORING:
                if (chessBoard.getKing().checkCheck()
                        && chessBoard.getSelectedMen() instanceof King == false) {
                    chessBoard.showTip("This is an illegal move...<br></b>"
                            + "You cannot move your piece there, as your king is in check.", null);
                } else {
                    chessBoard.showTip("This is an illegal move...<br></b>"
                            + "You cannot move your piece there, or your king will be in check.", null);
                }
                break;
        }
    }

    @Override
    public void moveTo(final ChessSquare dest) {
        final ChessSquare departure = getPosition();
        chessBoard.setArrivalAt(dest);
        chessBoard.setDepartureAt(departure);
        animator = new Timer(speed, new ActionListener() {
            Point departurePoint = men.getLocation();
            int i = 0;
            double animW = (dest.getCol() - getCol()) * chessBoard.squareSize.width;
            double animH = (dest.getRow() - getRow()) * chessBoard.squareSize.height;

            @Override
            public void actionPerformed(ActionEvent e) {
                ++i;
                if (chessBoard.showAnimations() && !men.getLocation().equals(
                        dest.getSquareComponent().getLocation()) && speed != LIGHTSPEED) {
                    men.setLocation(departurePoint.x + (int) (animW * i / 10),
                            departurePoint.y + (int) (animH * i / 10));
                } else {
                    position = dest;
                    hasMoved = true;
                    isCaptured = false;
                    locate();
                    chessBoard.showIndicators();
                    chessBoard.cleanChessBoard();
                    chessBoard.refreshStances();
                    chessBoard.turnChange();
                    animator.stop();
                }
            }
        });
        animator.start();
    }

    @Override
    public String toString() {
        return "[position = " + position + ", team = " + team + ", hasMoved = "
                + hasMoved + ", isCaptured = " + isCaptured + "]";
    }

    @Override
    public void locate() {
        men.setLocation((getCol() - 1) * men.getWidth(), (getRow() - 1) * men.getHeight());
    }

    public class Movement extends AbstractMovement {

        public Movement() {
            super();
        }

        public Movement(int horizontalDisplacement, int verticalDisplacement) {
            super(horizontalDisplacement, verticalDisplacement);
        }

        public Movement(int horizontalDisplacement, int verticalDisplacement, int step) {
            super(horizontalDisplacement, verticalDisplacement, step);
        }

        @Override
        protected void assignScope(boolean realTime) {
            ArrayList<ChessSquare> scope = new ArrayList<>(getStep());
            final ChessSquare origPosition = position;
            boolean blocked = false;
            if (isValid()) {
                int n = 1;
                while (n <= getStep()) {
                    int r = getRow() + getVerticalDisplacement() * n;
                    int c = getCol() + getHorizontalDisplacement() * n;
                    if (r <= 0 || r > 8 || c <= 0 || c > 8) {
                        break;
                    }
                    ChessSquare hostSqr = getChessSquareAt(r, c);
                    ChessMen hostMen = chessBoard.getChessMen(hostSqr);
                    if (blocked) {
                        if (realTime) {
                            hostSqr.setStance(OBSTRUCTED);
                            scope.add(hostSqr);
                            n++;
                            continue;
                        } else {
                            break;
                        }
                    }
                    if (!hostSqr.isEmpty()) {
                        blocked = true;
                        if (hostMen.getTeam() != team) {
                            if (captures()) {
                                if (hostMen instanceof King) {
                                    ((King) hostMen).check = true;
                                    if (!realTime) {
                                        return;
                                    }
                                } else if (realTime) {
                                    hostSqr.setStance(CAPTURING);
                                }
                            }
                        } else {
                            continue;
                        }
                    } else if (realTime) {
                        hostSqr.setStance(getDefaultType());
                    }
                    if (realTime) {
                        if (!hostSqr.isEmpty()) {
                            hostMen.setCaptured(true);
                        }
                        position = hostSqr;
                        locate();
                        King king = chessBoard.getKing(team);
                        if (king.checkCheck()) {
                            hostSqr.setStance(TRAITORING);
                        }
                        if (hostMen != null) {
                            hostMen.setCaptured(false);
                        }
                        position = origPosition;
                        locate();
                        scope.add(hostSqr);
                    }
                    n++;
                }
            }
            setScope(scope);
        }

        @Override
        protected boolean isValid() {
            return !isCaptured;
        }
    }
}