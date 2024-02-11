package crazy.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.Contract;

public class StdPodiumManager<E> implements PodiumManager<E> {

    // ATTRIBUTS

    private int shotsNb;
    private long time;
    private long delta;
    private Order lastOrder;
    
    private final Set<E> elements;
    private EnumMap<Rank, PodiumModel<E>> podiumModels;
    private final PropertyChangeSupport pcs;
    
    // CONSTRUCTEURS

    /**
     * @pre <pre>
     *     drawables != null
     *     drawables.size() >= 2 </pre>
     */
    public StdPodiumManager(Set<E> elems) {
        Contract.checkCondition(elems != null);
        Contract.checkCondition(elems.size() >= 2);

        elements = new HashSet<E>(elems);
		this.pcs = new PropertyChangeSupport(this);
        podiumModels = new EnumMap<Rank, PodiumModel<E>>(Rank.class);
        changePodiumModels();
        internalReinit();
    }

    // REQUETES

    @Override
    public Order getLastOrder() {
    	pcs.firePropertyChange(PROP_LAST_ORDER, null, lastOrder);
        return lastOrder;
    }

    @Override
    public Map<Rank, PodiumModel<E>> getModels() {
        return podiumModels.clone();
    }

    @Override
    public int getShotsNb() {
        return shotsNb;
    }

    @Override
    public long getTimeDelta() {
        return delta;
    }

    @Override
    public boolean isFinished() {
        PodiumModel<E> workLeft = podiumModels.get(Rank.WORK_LEFT);
        PodiumModel<E> workRight = podiumModels.get(Rank.WORK_RIGHT);
        PodiumModel<E> goalLeft = podiumModels.get(Rank.GOAL_LEFT);
        PodiumModel<E> goalRight = podiumModels.get(Rank.GOAL_RIGHT);
        boolean b =workLeft.similar(goalLeft) && workRight.similar(goalRight);
        pcs.firePropertyChange(PROP_FINISHED, null, b );
        return b;
    }

    // COMMANDES

    @Override
    public void executeOrder(Order o) throws PropertyVetoException {
        Contract.checkCondition(o != null);

        switch (o) {
            case LO:
                sendTop(Rank.WORK_LEFT, Rank.WORK_RIGHT);
                break;
            case KI:
                sendTop(Rank.WORK_RIGHT, Rank.WORK_LEFT);
                break;
            case MA:
                cycle(Rank.WORK_LEFT);
                break;
            case NI:
                cycle(Rank.WORK_RIGHT);
                break;
            case SO:
                exchangeTops();
                break;
            default:
                throw new AssertionError();
        }

        shotsNb += 1;
        lastOrder = o;
        if (isFinished()) {
            delta = System.currentTimeMillis() - time;
        }
    }

    @Override
    public void reinit() {
        podiumModels.clear();
        changePodiumModels();
        internalReinit();
    }

    // OUTILS

    private void exchangeTops() {
        PodiumModel<E> left = podiumModels.get(Rank.WORK_LEFT);
        PodiumModel<E> right = podiumModels.get(Rank.WORK_RIGHT);
        if (left.size() > 0 && right.size() > 0) {
            E elem = left.top();
            left.removeTop();
            left.addTop(right.top());
            right.removeTop();
            right.addTop(elem);
        }
    }

    private void cycle(Rank r) {
        PodiumModel<E> m = podiumModels.get(r);
        if (m.size() > 0) {
            E elem = m.bottom();
            m.removeBottom();
            m.addTop(elem);
        }
    }

    private void sendTop(Rank from, Rank to) {
        PodiumModel<E> f = podiumModels.get(from);
        PodiumModel<E> t = podiumModels.get(to);
        if (f.size() > 0 && t.size() < t.capacity()) {
            E elem = f.top();
            f.removeTop();
            t.addTop(elem);
        }
    }

    private void internalReinit() {
        shotsNb = 0;
        delta = 0;
        time = System.currentTimeMillis();
    }

    /**
     * Construit les 4 séquences d'éléments de E, puis les 4 modèles de podiums
     *  basés sur ces séquences.
     * La concaténation des deux premières séquences est une permutation des
     *  éléments de drawables.
     * La concaténation des deux dernières séquences est aussi une permutation
     *  des éléments de drawables.
     * Il se peut que les permutations soient identiques.
     */
    private void changePodiumModels() {
        int capacity = elements.size();
        
        List<List<E>> population = createRandomElements();
        podiumModels.put(Rank.WORK_LEFT,
                new StdPodiumModel<E>(population.get(0), capacity));
        podiumModels.put(Rank.WORK_RIGHT,
                new StdPodiumModel<E>(population.get(1), capacity));
        
        population = createRandomElements();
        podiumModels.put(Rank.GOAL_LEFT,
                new StdPodiumModel<E>(population.get(0), capacity));
        podiumModels.put(Rank.GOAL_RIGHT,
                new StdPodiumModel<E>(population.get(1), capacity));
    }

    /**
     * Construit une séquence de deux séquences aléatoires d'éléments de E, à
     *  partir de l'ensemble drawables, un peu comme on distribue des cartes :
     *  - on commence par mélanger les cartes,
     *  - puis on les distribue au hasard, une par une, en deux tas (de tailles
     *  pas forcément égales donc).
     */
    private List<List<E>> createRandomElements() {
        final double ratio = 0.5;
        final int n = elements.size();
        List<E> elementStock = new LinkedList<E>(elements);
        
        /* On mélange les éléments de elementStock en les vidant dans
         *  shuffledElements.
         * C'est de complexité quadratique en temps mais on s'en fout ici.
         * Rq : une meilleure version utiliserait un ListIterator pour
         *      supprimer dans elementStock.
         */
        List<E> shuffledElements = new ArrayList<E>(n);
        for (int i = n; i > 0; i--) {
            int k = ((int) (Math.random() * i));
            shuffledElements.add(elementStock.get(k));
            elementStock.remove(k);
        }
        
        /* On distribue au hasard les éléments de shuffledElements entre
         *  leftElements et rightElements (les éléments de gauche, et ceux
         *  de droite).
         */
        List<E> leftElements = new ArrayList<E>(elements.size());
        List<E> rightElements = new ArrayList<E>(elements.size());
        for (E e : shuffledElements) {
            if (Math.random() < ratio) {
                leftElements.add(e);
            } else {
                rightElements.add(e);
            }
        }
        
        List<List<E>> result = new ArrayList<List<E>>(2);
        result.add(leftElements);
        result.add(rightElements);
        return result;
    }

	@Override
	public PropertyChangeListener[] getPropertyChangeListeners(String pName) {
		Contract.checkCondition(pName != null);
		return pcs.getPropertyChangeListeners(pName);
	}

	@Override
	public VetoableChangeListener[] getVetoableChangeListeners(String pName) {
		return null;
	}

	@Override
	public void addPropertyChangeListener(String pName, PropertyChangeListener lnr) {
		Contract.checkCondition(pName != null);
		pcs.addPropertyChangeListener(pName ,lnr);
	}

	@Override
	public void addVetoableChangeListener(String pName, VetoableChangeListener lnr) {
		
	}

	@Override
	public void removePropertyChangeListener(String pName, PropertyChangeListener lnr) {
		Contract.checkCondition(pName != null);
		
		pcs.removePropertyChangeListener(pName, lnr);
	}

	@Override
	public void removeVetoableChangeListener(String pName, VetoableChangeListener lnr) {
		// TODO Auto-generated method stub
		
	}
}
