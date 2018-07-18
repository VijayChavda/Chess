package chess;

import board.AbstractChessMen;
import board.Bishop;
import board.ChessBoard;
import board.ChessMen;
import board.King;
import board.Knight;
import board.Pawn;
import board.Queen;
import board.Rook;
import java.awt.Dimension;
import static java.lang.Class.forName;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;

public class ChessBoardImpl extends ChessBoard {

    private static final Logger LOG = Logger.getLogger(ChessBoardImpl.class.getName());
    public static final Rules RULES = new Rules();
    private Configuration configuration = new Configuration() {
        @Override
        public void updateValues() {
            for (ChessMen men : getChessMensAsArray()) {
                AbstractChessMen piece = (AbstractChessMen) men;
                int speed = piece instanceof King ? getSpeed(ChB_king)
                        : piece instanceof Queen ? getSpeed(ChB_queen)
                        : piece instanceof Pawn ? getSpeed(ChB_pawn)
                        : piece instanceof Knight ? getSpeed(ChB_knight)
                        : piece instanceof Bishop ? getSpeed(ChB_bishop)
                        : piece instanceof Rook ? getSpeed(ChB_rook)
                        : AbstractChessMen.LIGHTSPEED;
                piece.setAnimSpeed(speed);
            }
        }
    };
    private boolean saved = true;

    public ChessBoardImpl(Dimension squareSize) {
        super(squareSize);
    }

    public final Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void newGame() {
        super.newGame();
        saved = true;
    }

    @Override
    public void endGame() {
        String str = "Are you sure you wish to quit this game...?";
        str += (saved ? "" : "<br>The game is not saved!!");
        if (promptYesNo(str)) {
            super.endGame();
            showTip("Good Bye...", null);
            System.exit(0);
        }
    }

    @Override
    public boolean showLastMoves() {
        return configuration.CB_showLast.isSelected();
    }

    @Override
    public boolean showAnimations() {
        return configuration.CB_showAnim.isSelected();
    }

    @Override
    public boolean showTips() {
        return configuration.CB_showTips.isSelected();
    }

    @Override
    public boolean showValidMoves() {
        return configuration.CB_showValid.isSelected();
    }

    @Override
    public String getPlayer1Name() {
        return configuration.TF_p1Name.getText();
    }

    @Override
    public String getPlayer2Name() {
        return configuration.TF_p2Name.getText();
    }

    @Override
    public boolean isActive() {
        return super.isActive() && isReady();
    }

    @Override
    public boolean isReady() {
        boolean b = !configuration.isShowing() && !RULES.isShowing();
        return b;
    }

    @Override
    protected void changeTurn() {
        super.changeTurn();
        saved = false;
    }

    public boolean canUndo() {
        for (Iterator<Trajector> it = getTrajectors().iterator(); it.hasNext();) {
            Trajector t = it.next();
            return t.getRecord(getRecordIndex() - 1) != null;
        }
        return false;
    }

    public boolean canRedo() {
        for (Iterator<Trajector> it = getTrajectors().iterator(); it.hasNext();) {
            Trajector t = it.next();
            return t.getRecord(getRecordIndex() + 1) != null;
        }
        return false;
    }

    public boolean undoGame() {
        if (canUndo()) {
            try {
                revertToRecord(getRecordIndex() - 1);
                return true;
            } catch (RecordReversionException ex) {
                LOG.log(Level.SEVERE, "Could not revert to the record whose index was " + (getRecordIndex() - 1) + ".", ex);
                showMessage("Failed to undo the game... an unexpected RecordReversionException occured.");
                return false;
            }
        } else {
            showTip("Cannot undo the game further...!", null);
            return false;
        }
    }

    public boolean redoGame() {
        if (canRedo()) {
            try {
                revertToRecord(getRecordIndex() + 1);
                return true;
            } catch (RecordReversionException ex) {
                LOG.log(Level.SEVERE, "Could not revert to the record whose index was " + getRecordIndex() + ".", ex);
                showMessage("Failed to redo the game... an unexpected RecordReversionException occured.");
                return false;
            }
        } else {
            showTip("Cannot redo the game further...!", null);
            return false;
        }
    }

    public void loadGame(Connection conn) {
        if (!saved) {
            if (!promptYesNo("The current game is not saved... Do you still wish to load another game....?")) {
                return;
            }
        }
        getTrajectors().clear();
        removeAllChessMen();

        URL defaultRL = ChessMen.class.getResource("");
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            Statement stmt1 = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            Statement stmt3 = conn.createStatement();
            stmt1.executeUpdate("use classicchess;");
            ResultSet menSet = stmt1.executeQuery("select * from menTrajectory order by menTrajID;");
            while (menSet.next()) {
                int menTrajID = menSet.getInt("menTrajID");
                Team menTeam = menSet.getString("menTeam").charAt(0) == 'W' ? Team.WHITE : Team.BLACK;
                String menClassName = menSet.getString("menClassName");
                URL menURL = menSet.getURL("menClassURL");
                ClassLoader menLoader = new URLClassLoader(new URL[]{defaultRL, menURL});
                try {
                    Class<ChessMen> menType = (Class<ChessMen>) forName(menClassName, true, menLoader);
                    Constructor<ChessMen> menConstructor = menType.getConstructor(ChessBoard.class, Team.class);
                    ChessMen newMen = menConstructor.newInstance(this, menTeam);
                    ArrayList<Record> records = new ArrayList<>(1);
                    ResultSet menRecordSet = stmt2.executeQuery("select * from recorder where trajID = "
                            + menTrajID + " order by ID;");
                    while (menRecordSet.next()) {
                        int index = menRecordSet.getInt("ID");
                        int row = menRecordSet.getInt("row");
                        int col = menRecordSet.getInt("col");
                        boolean hasMoved = menRecordSet.getString("hasMoved").charAt(0) == 'T';
                        boolean isCaptured = menRecordSet.getString("isCaptured").charAt(0) == 'T';
                        boolean isActive = menRecordSet.getString("active").charAt(0) == 'T';
                        Record r = new Record(getChessSquareAt(row, col), hasMoved, isCaptured);
                        records.add(index, r);
                        ResultSet countSet = stmt3.executeQuery("select max(ID) as \"noOfRec\"from recorder;");
                        if (countSet.next()) {
                            int maxRI = countSet.getInt("noOfRec");
                            setNoOfRecords(maxRI + 1);
                        }
                        setRecordIndex(isActive ? index : getRecordIndex());
                    }
                    Trajector t = new Trajector(newMen);
                    t.addRecords(records);
                    getTrajectors().add(t);
                    getChessMens().add(newMen);
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException |
                        NoSuchMethodException | InvocationTargetException ex) {
                    LOG.log(Level.SEVERE, "Failed to load game properly... ChessMen(s) could not initialise.", ex);
                }
            }
        } catch (SQLException ex) {
            try {
                stmt.executeUpdate("rollback;");
            } catch (SQLException ex1) {
                //continue ahead...
            }
            LOG.log(Level.SEVERE, "Loading failed... An SQLException occured.", ex);
            showMessage("Failed to load the game... an SQLExceptionoccured.");
        }
        try {
            revertToRecord(getRecordIndex());
            for (ChessMen men : getChessMensAsArray()) {
                super.addChessMen(men, null);
            }
        } catch (RecordReversionException ex) {
            LOG.log(Level.SEVERE, "Lo", ex);
            showMessage("Failed to load the game... [Unexpected decoding error]");
            super.endGame();
        }
        repaintBoard();
        saved = true;
        showTip("Load sucessful...!", null);
    }

    public void saveGame(Connection conn) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("start transaction;");
            stmt.executeUpdate("create database if not exists classicchess;");
            stmt.executeUpdate("use classicchess;");
            stmt.executeUpdate("create table if not exists recorder("
                    + "ID int(5)," + ""
                    + "active char(1) not null check(active in('T', 'F')), "
                    + " row tinyint(1) not null,"
                    + " col tinyint(1) not null, "
                    + "hasMoved char(1) not null check(hasMoved in('T', 'F')),"
                    + " isCaptured char(1) not null check(isCaptured in('T', 'F')),"
                    + " trajID tinyint(2) references menTrajectory(menTrajID), "
                    + "primary key(ID, trajID)) engine = \"InnoDB\";");
            stmt.executeUpdate("create table if not exists menTrajectory("
                    + "menTrajID tinyint(2) primary key,"
                    + " menTeam char(1) not null check(menTeam in('W', 'F')),"
                    + " menClassName char(20) not null, "
                    + " menClassURL varchar(250)) engine = \"InnoDB\";");
            stmt.executeUpdate("delete from menTrajectory;");
            stmt.executeUpdate("delete from recorder;");
            int trajID = 0;
            for (Iterator<Trajector> it = getTrajectors().iterator(); it.hasNext();) {
                Trajector t = it.next();
                trajID++;
                ChessMen men = t.getChessMen();
                String tValues = trajID + ", \"" + men.getTeam().toString().charAt(0) + "\", \""
                        + men.getClass().getName() + "\", \"" + men.getClass().getClassLoader().getResource("") + "\"";
                stmt.executeUpdate("insert into menTrajectory values(" + tValues + ");");
                int ID = -1;
                for (Iterator<Record> records = t.getRecords().iterator(); records.hasNext();) {
                    ID++;
                    Record r = records.next();
                    String values = ID + ", \"" + (ID == getRecordIndex() ? 'T' : 'F') + "\", "
                            + r.getPosition().getRow() + ", " + r.getPosition().getCol() + ", \""
                            + (r.hasMoved() ? 'T' : 'F') + "\", \"" + (r.isCaptured() ? 'T' : 'F') + "\", " + trajID;
                    stmt.executeUpdate("insert into recorder values (" + values + ");");
                }
            }
            saved = true;
            showTip("Save sucessful", null);
        } catch (SQLException ex) {
            try {
                stmt.executeUpdate("rollback;");
            } catch (SQLException ex1) {
                //continue ahead...
            }
            LOG.log(Level.SEVERE, "Saving failed... An SQLException occured.", ex);
            showMessage("Failed to save the game... an SQLExceptionoccured.");
        }
    }

    private int getSpeed(JComboBox cb) {
        int speed = cb.getSelectedIndex();
        return !configuration.CB_showAnim.isSelected() ? AbstractChessMen.LIGHTSPEED
                : speed == 0 ? AbstractChessMen.SLOW
                : speed == 1 ? AbstractChessMen.MID
                : speed == 2 ? AbstractChessMen.FAST
                : AbstractChessMen.LIGHTSPEED;
    }
}