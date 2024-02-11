package crazy.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import crazy.view.Drawable;
import util.Contract;

public enum AnimalColor implements Drawable {
    LION(Color.ORANGE), ELEPHANT(Color.GRAY), BEAR(Color.WHITE),
    MOUSE(Color.GREEN), BULL(Color.RED), PANTHER(Color.PINK),
    TIGER(Color.CYAN), HORSE(Color.YELLOW), COW(Color.MAGENTA),
    CAT(Color.BLUE);

    private Color color;

    AnimalColor(Color c) {
        color = c;
    }

    @Override
    public void draw(Graphics g) {
        Contract.checkCondition(g != null);
        Rectangle r = g.getClipBounds();
        Contract.checkCondition(r != null);
        
        // bordure
        g.setColor(Color.DARK_GRAY);
        g.drawRect(0, 0, r.width - 1, r.height - 1);
        // intérieur
        g.setColor(color);
        g.fillRect(1, 1, r.width - 2, r.height - 2);
    }
}
