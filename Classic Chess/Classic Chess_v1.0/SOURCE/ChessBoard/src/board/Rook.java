package board;

import board.ChessBoard.ChessSquare;
import chess.Team;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class Rook extends AbstractChessMen {

    private Image wRookImg, bRookImg;

    {
        wRookImg = chessBoard.getImgSrc("wRook.gif");
        bRookImg = chessBoard.getImgSrc("bRook.gif");
    }

    public Rook(Rook rook) {
        super(rook);
    }

    public Rook(ChessBoard chessBoard, Team team) {
        super(chessBoard, team);
    }

    public Rook(ChessBoard chessBoard, ChessSquare position, Team team) {
        super(chessBoard, position, team);
    }

    @Override
    public Set<AbstractMovement> getMovement() {
        Movement up = new Movement(0, -1);
        Movement down = new Movement(0, 1);
        Movement left = new Movement(1, 0);
        Movement right = new Movement(-1, 0);
        Set<AbstractMovement> moves = new HashSet(10);
        moves.addAll(Arrays.asList(new Movement[]{up, down, left, right}));
        return moves;
    }

    @Override
    public Image getImg() {
        return isWhite() ? wRookImg : bRookImg;
    }

    @Override
    public String getMenName() {
        return "Rook";
    }

    @Override
    public String getInfo() {
        return "Rooks can only be moved horizontally or vertically, as far as required.";
    }
}
