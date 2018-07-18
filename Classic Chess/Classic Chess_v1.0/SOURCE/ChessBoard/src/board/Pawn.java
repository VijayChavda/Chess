package board;

import board.ChessBoard.ChessSquare;
import board.ChessBoard.Record;
import board.ChessBoard.Trajector;
import chess.Team;
import chess.Type;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class Pawn extends AbstractChessMen implements PawnStances {

    static private ArrayList<Pawn> promoteds = new ArrayList<>(16);
    static int lastMoved = 0;
    private Image wPawnImg, bPawnImg, enPassantMoveImg;

    {
        wPawnImg = chessBoard.getImgSrc("wPawn.gif");
        bPawnImg = chessBoard.getImgSrc("bPawn.gif");
        enPassantMoveImg = chessBoard.getImgSrc("enPassantImg.jpg");
    }

    public Pawn(Pawn pawn) {
        super(pawn);
    }

    public Pawn(ChessBoard chessBoard, Team team) {
        super(chessBoard, team);
    }

    public Pawn(ChessBoard chessBoard, ChessSquare position, Team team) {
        super(chessBoard, position, team);
    }

    public static ArrayList<Pawn> getPromoted(){
        return promoteds;
    }

    @Override
    public void moveTo(ChessSquare destination) {
        lastMoved = 0;
        super.moveTo(destination);
//        chessBoard.showMessage("");//TODO for promotion...?
        boolean isHighestRank = (getRow() == 1 && isWhite()) || (getRow() == 8 && isBlack());
        if (isHighestRank) {
            Type newType = chessBoard.showPromotion(getTeam());
            chessBoard.removeChessMen(this);
            chessBoard.getChessMens().remove(this);
            promoteds.add(this);
            AbstractChessMen men;
            switch (newType) {
                case ROOK:
                    men = new Rook(chessBoard, destination, getTeam());
                    break;
                case BISHOP:
                    men = new Bishop(chessBoard, destination, getTeam());
                    break;
                case KNIGHT:
                    men = new Knight(chessBoard, destination, getTeam());
                    break;
                default:
                    men = new Queen(chessBoard, destination, getTeam());
            }
            Trajector oldTraj = chessBoard.getTrajectorOf(this);
            Trajector newTrajector = new Trajector(men);
            for (int i = 0; i <= chessBoard.getRecordIndex()+1; i++) {
                newTrajector.addRecord(null);
            }
            chessBoard.addChessMen(men, newTrajector);
        }
    }

    @Override
    public void actOnClick(ChessSquare onSqr) {
        super.actOnClick(onSqr);
        if (onSqr.getStance() == EN_PASSANT) {
            int rl = onSqr.getCol() > getPosition().getCol() ? 1 : -1;
            int c = getPosition().getCol() + rl;
            ChessMen enPassantHost = chessBoard.getChessMenAt(getRow(), c);
            enPassantHost.capture();
            moveTo(onSqr);
        }
    }

    @Override
    public Set<AbstractMovement> getMovement() {
        final int left = -1;
        final int right = 1;
        final int dy = (Team.isWhite(getTeam()) ? left : right);
        Movement captureL = new Movement(left, dy, 1) {
            @Override
            protected boolean isValid() {
                ChessMen hostMen = chessBoard.getChessMen(getRow() + dy, getCol() - 1);
                return super.isValid() && hostMen != null && getTeam() != hostMen.getTeam();
            }
        };
        Movement captureR = new Movement(right, dy, 1) {
            @Override
            protected boolean isValid() {
                ChessMen hostMen = chessBoard.getChessMen(getRow() + dy, getCol() + 1);
                return super.isValid() && hostMen != null && getTeam() != hostMen.getTeam();
            }
        };
        Movement forward = new Movement(0, dy, hasMoved() ? 1 : 2);
        Movement enPassantL = new Movement(left, dy, 1) {
            @Override
            protected boolean isValid() {
                int index = Pawn.super.chessBoard.getRecordIndex();
                ChessMen enmy = chessBoard.getChessMen((isWhite() ? 4 : 5), getCol() + left);
                if (enmy != null && enmy instanceof Pawn && (isWhite() ? getRow() == 4 : getRow() == 5)) {
                    Trajector traj = chessBoard.getTrajectorOf(enmy);
                    Record lastRec = traj.getRecord(index - 1);
                    return (enmy.hasMoved() && lastRec != null && !lastRec.hasMoved());
                }
                return false;
            }
        };
        Movement enPassantR = new Movement(right, dy, 1) {
            @Override
            protected boolean isValid() {
                int index = Pawn.super.chessBoard.getRecordIndex();
                ChessMen enmy = chessBoard.getChessMen((isWhite() ? 4 : 5), getCol() + right);
                if (enmy != null && enmy instanceof Pawn && (isWhite() ? getRow() == 4 : getRow() == 5)) {
                    Trajector traj = chessBoard.getTrajectorOf(enmy);
                    Record lastRec = traj.getRecord(index - 1);
                    return (enmy.hasMoved() && lastRec != null && !lastRec.hasMoved());
                }
                return false;
            }
        };
        enPassantL.setDefaultType(EN_PASSANT);
        enPassantR.setDefaultType(EN_PASSANT);
        captureL.setDefaultType(CAPTURING);
        captureR.setDefaultType(CAPTURING);
        forward.setCaptures(false);
        Set<AbstractMovement> moves = new HashSet(10);
        moves.addAll(Arrays.asList(new Movement[]{captureR, captureL, forward, enPassantL, enPassantR}));
        return moves;
    }

    @Override
    public void showScope() {
        super.showScope();
        for (ChessSquare sqr : chessBoard.getChessSquares()) {
            if (sqr.getStance() == EN_PASSANT) {
                sqr.setImg(enPassantMoveImg);
            }
        }
    }

    @Override
    public Image getImg() {
        return isWhite() ? wPawnImg : bPawnImg;
    }

    @Override
    public String getMenName() {
        return "Pawn";
    }

    @Override
    public String getInfo() {
        return "Pawns can only move forward one step, or two if they havent moved yet,<br>"
                + " and can capture pieces in one step in forward-diagonal direction only.<br>";
    }
}
