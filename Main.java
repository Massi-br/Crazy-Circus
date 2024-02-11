package crazy;

import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

import crazy.gui.AnimalColor;
import crazy.gui.CrazyCircus;

/**
 * L'argument attendu dans main() est un entier positif qui indique le nombre
 *  d'animaux à prendre en compte.
 * S'il n'y a pas d'argument ou s'il est erroné (pas un nombre) c'est
 *  DEFAULT_ANIMALS_NB qui est pris en compte.
 * Si le paramètre donné est trop petit, c'est MIN_ANIMALS_NB qui est pris en
 *  compte.
 * Si le paramètre donné est trop grand, c'est MAX_ANIMALS_NB qui est pris en
 *  compte.
 */
public final class Main {

    private static final AnimalColor[] VALUES = AnimalColor.values();
    
    private static final int MIN_ANIMALS_NB = 2;
    private static final int DEFAULT_ANIMALS_NB = 3;
    private static final int MAX_ANIMALS_NB = VALUES.length;
    
    private Main() {
        // rien
    }

    public static void main(String[] args) {
        int n = n(args);
        final Set<AnimalColor> animals = animals(n);
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CrazyCircus<AnimalColor>(animals).display();
            }
        });
    }
    
    private static Set<AnimalColor> animals(int n) {
        Set<AnimalColor> s = new HashSet<AnimalColor>();
        for (int i = 0; i < n; i++) {
            s.add(VALUES[i]);
        }
        return s;
    }
    
    private static int n(String[] args) {
        int n = -1;
        if (args.length == 0) {
            n = DEFAULT_ANIMALS_NB;
        } else {
            try {
                n = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                n = DEFAULT_ANIMALS_NB;
            } finally {
                if (n < MIN_ANIMALS_NB) {
                    n = MIN_ANIMALS_NB;
                } else if (n > MAX_ANIMALS_NB) {
                    n = MAX_ANIMALS_NB;
                }
            }
        }
        return n;
    }
}
