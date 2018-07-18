package board;

import board.ChessBoard.ChessSquare;
import chess.Team;
import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Set;

public interface ChessMen extends MenStances {

    public Set<AbstractMovement> getMovement();

    public String getMenName();

    public String getInfo();

    public Image getImg();

    public ChessBoard getChessBoard();

    Component getMenComponent();

    public ChessSquare getPosition();

    public void setPosition(ChessSquare position);

    public Team getTeam();

    public boolean hasMoved();

    public boolean isCaptured();

    public void setCaptured(boolean captured);

    public void setMoved(boolean moved);

    public void defineStances();

    public void showScope();

    public void capture();

    public void onClick();

    public void actOnClick(ChessSquare onSqr);

    public void moveTo(ChessSquare position);

    void locate();//TODO

    public static abstract class AbstractMovement {

        private int horizontalDisplacement;
        private int verticalDisplacement;
        private int step;
        private int defaultMove;
        private boolean captures;
        private ArrayList<ChessSquare> scope;

        protected AbstractMovement() {
            this(0, 0, 0);
        }

        protected AbstractMovement(int horizontalDisplacement, int verticalDisplacement) {
            this(horizontalDisplacement, verticalDisplacement, 8);
        }

        protected AbstractMovement(int horizontalDisplacement, int verticalDisplacement, int step) {
            this.horizontalDisplacement = horizontalDisplacement;
            this.verticalDisplacement = verticalDisplacement;
            this.step = step;
            defaultMove = MOVING;
            captures = true;
            scope = new ArrayList<>(step);
        }

        public int getHorizontalDisplacement() {
            return horizontalDisplacement;
        }

        protected void setHorizontalDisplacement(int horizontalDisplacement) {
            this.horizontalDisplacement = horizontalDisplacement;
        }

        public int getVerticalDisplacement() {
            return verticalDisplacement;
        }

        protected void setVerticalDisplacement(int verticalDisplacement) {
            this.verticalDisplacement = verticalDisplacement;
        }

        public int getStep() {
            return step;
        }

        protected void setStep(int step) {
            this.step = step;
        }

        public int getDefaultType() {
            return defaultMove;
        }

        protected void setDefaultType(int type) {
            this.defaultMove = type;
        }

        public boolean captures() {
            return captures;
        }

        protected void setCaptures(boolean captures) {
            this.captures = captures;
        }

        public ArrayList<ChessSquare> getScope() {
            return scope;
        }

        public ChessSquare[] getScopeAsArray() {
            ChessSquare[] sqrs = (ChessSquare[]) scope.toArray();
            return scope.toArray(sqrs);
        }

        protected void setScope(ArrayList<ChessSquare> scope) {
            this.scope = scope;
        }

        @Override
        public final String toString() {
            return "[horizontalDisplacement = " + horizontalDisplacement + ", verticalDisplacement = "
                    + verticalDisplacement + ", step = " + step + ", type = " + defaultMove + ", captures = " +
                    captures + "]";
        }

        protected abstract void assignScope(boolean realTime);

        protected abstract boolean isValid();
    }
}
