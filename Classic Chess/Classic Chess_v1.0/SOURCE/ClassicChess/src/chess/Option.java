package chess;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class Option extends Component {

    private static int x;
    private static int gap;
    private static int noOfOptions = -1;
    private static Dimension size;
    private Image img;

    public Option(Image img) {
        this.img = img;
        initSelf();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, this);
    }

    public abstract void onClick();

    public static int getx() {
        return x;
    }

    public static int getGap() {
        return gap;
    }

    public int getNoOfOptions() {
        return noOfOptions;
    }

    public static Dimension getBound() {
        return size;
    }

    public Image getImg() {
        return img;
    }

    protected static void setX(int x) {
        Option.x = x;
    }

    protected static void setGap(int gap) {
        Option.gap = gap;
    }

    protected static void setBound(Dimension size) {
        Option.size = size;
    }

    protected void setImg(Image img) {
        this.img = img;
    }

    private void initSelf() {
        int y = ++noOfOptions * size.height + gap * (noOfOptions + 1);
        setLocation(x, y);
        setSize(size.width, size.height);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                    onClick();
            }
        });
    }
}
