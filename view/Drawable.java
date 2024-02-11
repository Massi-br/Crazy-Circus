package crazy.view;

import java.awt.Graphics;

public interface Drawable {

    /**
     * L'élément se dessine sur g en partant de (0, 0). 
     * @pre <pre>
     *     g != null && g.getClipBounds() != null </pre>
     * @post <pre>
     *     L'élément s'est dessiné sur le Rectangle g.getClipBounds()
     *       défini avant l'appel </pre>
     */
    void draw(Graphics g);
}
