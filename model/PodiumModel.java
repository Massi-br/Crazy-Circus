package crazy.model;

import javax.swing.event.ChangeListener;

/**
 * Une structure linéaire bornée à laquelle on peut ajouter ou supprimer le
 *  sommet, supprimer la base, et dont on peut consulter tous les éléments.
 * @inv <pre>
 *     0 <= capacity()
 *     0 <= size() <= capacity()
 *     similar(m) <==>
 *             m.size() == this.size()
 *             && m.capacity() == this.capacity()
 *             && forall i, 0 <= i < size() :
 *                    m.elementAt(i).equals(elementAt(i))
 *     top() == elementAt(size() - 1)
 *     bottom() == elementAt(0)
 *     forall i, 0 <= i < size() : elementAt(i) != null
 *     forall i, size() <= i < capacity() : elementAt(i) == null </pre>
 * @cons <pre>
 * $ARGS$ List<E> init, int capacity
 * $PRE$
 *     init != null
 *     capacity >= init.size()
 *     forall i, 0 <= i < init.size() : init.get(i) != null
 * $POST$
 *     capacity() == capacity
 *     size() == init.size()
 *     forall i, 0 <= i < size() : elementAt(i) == init.get(i) </pre>
 */
public interface PodiumModel<E> {

    // REQUETES

    /**
     * L'élément à la base de ce modèle.
     * @pre <pre>
     *     size() > 0 </pre>
     */
    E bottom();

    /**
     * La capacité du modèle, c'est-à-dire le nombre maximal d'éléments
     *  qu'il peut contenir.
     */
    int capacity();

    /**
     * Le ième élément du modèle.
     * Les éléments aux positions entre 0 et size() - 1 sont tous non null.
     * Il n'y a pas d'élément aux positions suivantes (retourne null).
     * @pre <pre>
     *     0 <= i < capacity() </pre>
     */
    E elementAt(int i);

    /**
     * Les observateurs du modèle.
     */
    ChangeListener[] getChangeListeners();

    /**
     * Indique si ce modèle est similaire à that.
     * @pre <pre>
     *     that != null </pre>
     */
    boolean similar(PodiumModel<E> that);

    /**
     * Le nombre d'éléments actuellement dans le modèle.
     */
    int size();

    /**
     * L'élément au sommet de ce modèle.
     * @pre <pre>
     *     size() > 0 </pre>
     */
    E top();

    // COMMANDES

    /**
     * Ajoute un ChangeListener au modèle.
     * @pre <pre>
     *     listener != null </pre>
     * @post <pre>
     *     getChangeListeners() contient listener </pre>
     */
    void addChangeListener(ChangeListener cl);

    /**
     * Ajoute un élément au sommet, avec notification de changement d'état.
     * @pre <pre>
     *     elem != null
     *     size() < capacity() </pre>
     * @post <pre>
     *     size() == old size() + 1
     *     forall i, 0 <= i < size() - 1 :
     *         elementAt(i) == old elementAt(i)
     *     elementAt(size() - 1) == elem </pre>
     */
    void addTop(E elem);

    /**
     * Retire l'élément à la base, avec notification de changement d'état.
     * @pre <pre>
     *     size() > 0 </pre>
     * @post <pre>
     *     size() == old size() - 1
     *     forall i, 0 <= i < size() :
     *         elementAt(i) == old elementAt(i + 1) </pre>
     */
    void removeBottom();

    /**
     * Retire un ChangeListener du modèle.
     * @pre <pre>
     *     listener != null </pre>
     * @post <pre>
     *     getChangeListeners() ne contient pas listener </pre>
     */
    void removeChangeListener(ChangeListener cl);

    /**
     * Retire l'élément au sommet, avec notification de changement d'état.
     * @pre <pre>
     *     size() > 0 </pre>
     * @post <pre>
     *     size() == old size() - 1
     *     forall i, 0 <= i < size() :
     *         elementAt(i) == old elementAt(i) </pre>
     */
    void removeTop();
}
