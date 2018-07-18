package board;

import board.ChessBoard.ChessSquare;
import chess.Team;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class Bishop extends AbstractChessMen {

    private Image wBishopImg, bBishopImg;

    {
        wBishopImg = chessBoard.getImgSrc("wBishop.gif");
        bBishopImg = chessBoard.getImgSrc("bBishop.gif");
    }

    public Bishop(Bishop bishop) {
        super(bishop);
    }

    public Bishop(ChessBoard chessBoard, Team team) {
        super(chessBoard, team);
    }

    public Bishop(ChessBoard chessBoard, ChessSquare position, Team team) {
        super(chessBoard, position, team);
    }

    @Override
    public Set<AbstractMovement> getMovement() {
        Movement upR = new Movement(1, -1);
        Movement downR = new Movement(1, 1);
        Movement upL = new Movement(-1, -1);
        Movement downL = new Movement(-1, 1);
        Set<AbstractMovement> moves = new HashSet(10);
        moves.addAll(Arrays.asList(new Movement[]{upR, downR, upL, downL}));
        return moves;
    }

    @Override
    public Image getImg() {
        return isWhite() ? wBishopImg : bBishopImg;
    }

    @Override
    public String getMenName() {
        return "Bishop";
    }

    @Override
    public String getInfo() {
        return "Bishops can only be moved diagonally, as far as required.";
    }
}
