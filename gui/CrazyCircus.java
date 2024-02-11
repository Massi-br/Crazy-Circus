package crazy.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import crazy.model.Order;
import crazy.model.PodiumManager;
import crazy.model.PodiumModel;
import crazy.model.StdPodiumManager;
import crazy.model.PodiumManager.Rank;
import crazy.view.Drawable;
import crazy.view.Podium;

public class CrazyCircus<E extends Drawable> {

    // ATTRIBUTS

    private PodiumManager<E> manager;
    
    private final JFrame frame;
    private final Map<Rank, Podium<E>> allPodiums;
    private final Map<Order, JButton> commandButtons;
    private final JButton restart;
    private final JTextArea output;
    private final JCheckBox soAllower;

    // CONSTRUCTEURS

    public CrazyCircus(Set<E> drawables) {
        // MODELE
        manager = new StdPodiumManager<E>(drawables);
        // VUE
        frame = new JFrame("Crazy Circus");
        commandButtons = buildCommandButtons();
        restart = new JButton("Nouvelle Partie");
        output = buildOutput();
        soAllower = new JCheckBox("Autoriser SO");
        allPodiums = buildAllPodiums();
        placeComponents();
        // CONTROLEUR
        connectControllers();
    }

    // COMMANDES

    public void display() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // OUTILS

    private JTextArea buildOutput() {
        final int outRowsNb = 4;
        final int outColsNb = 10;

        JTextArea out = new JTextArea(outRowsNb, outColsNb);
        out.setEditable(false);
        out.setLineWrap(true);
        return out;
    }
    
    private Map<Order, JButton> buildCommandButtons() {
        Map<Order, JButton> cmds = new EnumMap<Order, JButton>(Order.class);
        for (Order o : Order.values()) {
            JButton b = new JButton(o.label());
            b.setName(o.name());
            cmds.put(o, b);
        }
        return cmds;
    }
    
    private Map<Rank, Podium<E>> buildAllPodiums() {
        Map<Rank, PodiumModel<E>> models = manager.getModels();
        Map<Rank, Podium<E>> podiums = new EnumMap<Rank, Podium<E>>(Rank.class);
        for (Rank r : Rank.values()) {
            podiums.put(r, new Podium<E>(models.get(r)));
        }
        return podiums;
    }

    private void placeComponents() {
        JPanel p = new JPanel(new BorderLayout());
        { //--
            JPanel q = new JPanel(new GridLayout(0, 1));
            { //--
                for (Order o : Order.values()) {
                    q.add(commandButtons.get(o));
                }
                JPanel r = new JPanel();
                { //--
                    r.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    r.add(soAllower);
                } //--
                q.add(r);
                q.add(restart);
            } //--
            p.add(q, BorderLayout.NORTH);
        } //--
        frame.add(p, BorderLayout.EAST);

        p = new JPanel(new BorderLayout());
        { //--
            JPanel q = new JPanel(new GridLayout(1, 0));
            { //--
                for (Rank r : Rank.values()) {
                    q.add(allPodiums.get(r));
                    if (r == Rank.WORK_RIGHT) {
                        q.add(new JLabel(""));
                    }
                }
            } //--
            p.add(q, BorderLayout.CENTER);
            q = new JPanel(new GridLayout(1, 0));
            { //--
                q.add(createMinorLabel("Travail"));
                q.add(new JLabel(""));
                q.add(createMinorLabel("Objectif"));
            } //--
            p.add(q, BorderLayout.SOUTH);
        } //--
        frame.add(p, BorderLayout.WEST);

        frame.add(new JScrollPane(output), BorderLayout.SOUTH);
    }

    private JLabel createMinorLabel(String text) {
        JLabel result = new JLabel(text);
        result.setHorizontalAlignment(SwingConstants.CENTER);
        return result;
    }

    private void connectControllers() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton b = ((JButton) e.getSource());
                Order o = Order.valueOf(b.getName());
                manager.executeOrder(o);
            }
        };
        for (Order o : Order.values()) {
            commandButtons.get(o).addActionListener(al);
        }

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.reinit();
            }
        });
    }
}
