package crazy.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Map;

/**
 * @inv <pre>
 *     getModels() != null
 *     forall Rank r : getModels().get(r) != null
 *     getShotsNb() >= 0
 *     getTimeDelta() >= 0
 *     !isFinished() ==> getTimeDelta() == 0 </pre>
 * @cons <pre>
 *     $ARGS$ Set<E> elements
 *     $PRE$  elements != null && elements.size() >= 2
 *     $POST$
 *         les 2+2 modèles de podium sont initialisés aléatoirement
 *         avec les éléments donnés </pre>
 */
public interface PodiumManager<E> {

    /**
     * Les dispositions des podiums sur la fenêtre :
     * <ul>
     *   <li> WORK_LEFT  : podium gauche de la configuration de travail ;</li>
     *   <li> WORK_RIGHT : podium droit de la configuration de travail ;</li>
     *   <li> GOAL_LEFT  : podium gauche de la configuration objectif ;</li>
     *   <li> GOAL_RIGHT : podium droit de la configuration objectif.</li>
     * </ul>
     */
    enum Rank { WORK_LEFT, WORK_RIGHT, GOAL_LEFT, GOAL_RIGHT }

    String PROP_LAST_ORDER = "lastOrder";
    String PROP_FINISHED = "finished";

    // REQUETES

    /**
     * Le dernier ordre donné.
     * Vaut null en début de partie.
     */
    Order getLastOrder();

    /**
     * Les quatre modèles de podium gérés par ce gestionnaire.
     */
    Map<Rank, PodiumModel<E>> getModels();

    /**
     * Tous les observateurs de changement de valeur de la propriété pName.
     */
    PropertyChangeListener[] getPropertyChangeListeners(String pName);
    
    /**
     * Le nombre d'ordres donnés au cours d'une partie.
     */
    int getShotsNb();

    /**
     * L'intervalle de temps entre le début d'une partie et la fin.
     * Vaut 0 tant que la partie n'est pas finie.
     */
    long getTimeDelta();

    /**
     * Tous les observateurs de changement de valeur de la propriété pName.
     */
    VetoableChangeListener[] getVetoableChangeListeners(String pName);

    /**
     * Indique si une partie en cours est finie.
     */
    boolean isFinished();

    // COMMANDES

    /**
     * Ajoute un PCL pour la propriété pName.
     * Ne fait rien si lnr a déjà été ajouté.
     * @pre <pre>
     *     pName != null && lnr != null </pre>
     * @post <pre>
     *     lnr a été ajouté à la liste des écouteurs de la propriété pName
     * </pre>
     */
    void addPropertyChangeListener(String pName, PropertyChangeListener lnr);

    /**
     * Ajoute un VCL pour la propriété pName.
     * Ne fait rien si lnr a déjà été ajouté.
     * @pre <pre>
     *     pName != null && lnr != null </pre>
     * @post <pre>
     *     lnr a été ajouté à la liste des écouteurs de la propriété pName
     * </pre>
     */
    void addVetoableChangeListener(String pName, VetoableChangeListener lnr);

    /**
     * Exécute l'ordre o sur ce gestionnaire.
     * Un changement de valeur pour la propriété lastOrder (si acceptable) est
     *  notifié, ainsi que pour la propriété finished (l'ancienne valeur
     *  de cette propriété est non null).
     * @pre <pre>
     *     o != null </pre>
     * @post <pre>
     *     les actions conformes à l'ordre o ont été exécutées sur les modèles
     *       gérés par ce gestionnaire </pre>
     * @throws
     *     PropertyVetoException si l'ordre o a été refusé
     */
    void executeOrder(Order o) throws PropertyVetoException;

    /**
     * Réinitialise ce gestionnaire.
     * Un changement de valeur pour la propriété finished est notifié
     *  (cette propriété passe de la valeur null à une valeur non null).
     * @post <pre>
     *     les 2+2 modèles de podium ont été régénérés aléatoirement
     *     getShotsNb() == 0
     *     getTimeDelta() == 0
     *     getLastOrder() == null </pre>
     */
    void reinit();

    /**
     * Retire un PCL pour la propriété pName.
     * Ne fait rien si lnr a déjà été retiré.
     * @pre <pre>
     *     lnr != null && pName != null</pre>
     * @post <pre>
     *     lnr a été retiré de la liste des écouteurs </pre>
     */
    void removePropertyChangeListener(String pName, PropertyChangeListener lnr);

    /**
     * Retire un VCL pour la propriété pName.
     * Ne fait rien si lnr a déjà été retiré.
     * @pre <pre>
     *     lnr != null && pName != null</pre>
     * @post <pre>
     *     lnr a été retiré de la liste des écouteurs </pre>
     */
    void removeVetoableChangeListener(String pName, VetoableChangeListener lnr);
}
