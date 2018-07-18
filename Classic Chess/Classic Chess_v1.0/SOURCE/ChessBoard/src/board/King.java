package board;

import board.ChessBoard.ChessSquare;
import static board.ChessBoard.getChessSquare;
import chess.Team;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.util.Arrays.asList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.Timer;

public final class King extends AbstractChessMen implements KingStances {

    boolean check = false;
    private Timer checkPainter;
    private Image wKingImg, bKingImg, checkImg, castlingMoveImg;

    {
        wKingImg = chessBoard.getImgSrc("wKing.gif");
        bKingImg = chessBoard.getImgSrc("bKing.gif");
        checkImg = chessBoard.getImgSrc("checkImg.jpg");
        castlingMoveImg = chessBoard.getImgSrc("castlingImg.jpg");
        checkPainter = new Timer(21, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                {
                    if (checkCheck()) {
                        boolean toPaint = getPosition().getImage() == null;
                        if (toPaint) {
                            getPosition().setImg(checkImg);
                        }
                    } else {
                        getPosition().setImg(getPosition().getImage());
                    }
                }
            }
        });
        checkPainter.start();
    }

    public King(King king) {
        super(king);
    }

    public King(ChessBoard chessBoard, Team team) {
        super(chessBoard, team);
    }

    public King(ChessBoard chessBoard, ChessSquare position, Team team) {
        super(chessBoard, position, team);
    }

    public boolean checkCheck() {
        try {
            for (Iterator<ChessMen> it = chessBoard.getChessMens().iterator(); it.hasNext();) {
                ChessMen men = it.next();
                if (men.getTeam() != getTeam()) {
                    for (Iterator<AbstractMovement> moves = men.getMovement().iterator();
                            moves.hasNext();) {
                        AbstractMovement move = moves.next();
                        if (move.captures()) {
                            move.assignScope(false);
                        }
                        if (check) {
                            check = false;
                            return true;
                        }
                    }
                }
            }
        } catch (ConcurrentModificationException e) {
            //never mind...
        }
        return false;
    }

    @Override
    public void actOnClick(ChessSquare onSqr) {
        super.actOnClick(onSqr);
        if (onSqr.getStance() == CASTLING) {
            boolean right = onSqr.getCol() == 7;
            Rook tryRook;
            tryRook = (Rook) chessBoard.getChessMenAt(getRow(), getCol() + (right ? 3 : -4));
            moveTo(onSqr);
            tryRook.setPosition(getRow(), getCol() + (right ? 1 : -1));
        }
    }

    @Override
    public Set<AbstractMovement> getMovement() {
        Set<AbstractMovement> moves = new HashSet(10);
        Movement up = new Movement(0, -1, 1);
        Movement down = new Movement(0, 1, 1);
        Movement left = new Movement(1, 0, 1);
        Movement right = new Movement(-1, 0, 1);
        Movement upR = new Movement(1, -1, 1);
        Movement downR = new Movement(1, 1, 1);
        Movement upL = new Movement(-1, -1, 1);
        Movement downL = new Movement(-1, 1, 1);
        Movement castlingL = new Movement(-2, 0, 1) {
            @Override
            public boolean isValid() {
                ChessSquare[] left = new ChessSquare[2];
                ChessMen tryRook = chessBoard.getChessMen(getRow(), getCol() - 4);
                Rook rook;
                if (tryRook instanceof Rook && !hasMoved()) {
                    rook = (Rook) tryRook;
                } else {
                    return false;
                }
                final ChessSquare origPosition = getPosition();
                boolean notThrtnd = !checkCheck();

                left[0] = getChessSquare(origPosition.getRow(), origPosition.getCol() - 1);
                setPosition(left[0]);
                notThrtnd = notThrtnd && !checkCheck();

                left[1] = getChessSquare(origPosition.getRow(), origPosition.getCol() - 2);
                setPosition(left[1]);
                notThrtnd = notThrtnd && !checkCheck();

                setPosition(origPosition);
                return notThrtnd && !rook.hasMoved() && chessBoard.areEmptySquares(left);
            }
        };
        Movement castlingR = new Movement(2, 0, 1) {
            @Override
            public boolean isValid() {
                ChessSquare[] right = new ChessSquare[2];
                ChessMen tryRook = chessBoard.getChessMen(getRow(), getCol() + 3);
                Rook rook;
                if (tryRook instanceof Rook && !hasMoved()) {
                    rook = (Rook) tryRook;
                } else {
                    return false;
                }
                final ChessSquare origPosition = getPosition();
                boolean notThrtnd = !checkCheck();

                right[0] = getChessSquare(origPosition.getRow(), origPosition.getCol() + 1);
                setPosition(right[0]);
                notThrtnd = notThrtnd && !checkCheck();

                right[1] = getChessSquare(origPosition.getRow(), origPosition.getCol() + 2);
                setPosition(right[1]);
                notThrtnd = notThrtnd && !checkCheck();

                setPosition(origPosition);
                return notThrtnd && !rook.hasMoved() && chessBoard.areEmptySquares(right);
            }
        };
        castlingL.setDefaultType(CASTLING);
        castlingL.setCaptures(false);
        castlingR.setDefaultType(CASTLING);
        castlingR.setCaptures(false);
        moves.addAll(asList(new Movement[]{
            up, down, left, right, upR, downR, upL, downL, castlingR, castlingL}));
        return moves;
    }

    @Override
    public Image getImg() {
        return isWhite() ? wKingImg : bKingImg;
    }

    @Override
    public String getMenName() {
        return "King";
    }

    @Override
    public String getInfo() {
        return "King can only move one step, but in any of the eight directions.";
    }
}
