package cz.cuni.mff.kocur.agentGraphics;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cz.cuni.mff.kocur.base.GraphicResources;
import cz.cuni.mff.kocur.brain.Brain;
import cz.cuni.mff.kocur.decisions.Decision;
import cz.cuni.mff.kocur.graphics.ConstraintsBuilder;

public class ConsiderationsViewer extends JPanel implements ActionListener {

	/**
	 * Generated serial version id.
	 */
	private static final long serialVersionUID = 8606023145609355096L;

	/**
	 * Logger registered for our class.
	 */
	private static final Logger logger = LogManager.getLogger(ConsiderationsViewer.class);

	/**
	 * Panels that contains decisions. We store them using decisions as keys - so we
	 * can change them later.
	 */
	private HashMap<Decision, DecisionPanel> decisionPanels = new HashMap<>();

	/**
	 * Reference to brain we are displaying.
	 */
	private Brain brain = null;

	/**
	 * Grid bag constraints we use.
	 */
	private GridBagConstraints gbc;

	/**
	 * Button that reloads brain to its former state.
	 */
	private JButton reloadBrain = new JButton("Reload brain");

	/**
	 * Hides all the decision panels.
	 */
	private JButton hideAll = new JButton("Hide all");

	/**
	 * Shows all the decision panels.
	 */
	private JButton showAll = new JButton("Show all");

	/**
	 * Constructor that stores reference to brain it displyes.
	 * 
	 * @param brain
	 *            Brain.
	 */
	public ConsiderationsViewer(Brain brain) {
		this.brain = brain;

		this.setLayout(new GridBagLayout());

	}

	/**
	 * Builds the viewer.
	 */
	public void build() {
		gbc = ConstraintsBuilder.build().gridxy(0).weightxy(1, 0).fill(GridBagConstraints.BOTH)
				.anchor(GridBagConstraints.WEST).get();

		addButtons();

		// Add decisions
		LinkedList<Decision> decisions = brain.getDecisions();
		for (Decision d : decisions) {
			addDecision(d);
		}
		
		// Add label that separates decisions and void decisions
		JLabel separator = new JLabel("VOID DECISIONS");
		separator.setFont(new Font("Serif", Font.PLAIN, 20));
		this.add(separator, gbc);
		gbc.gridy++;
		
		// Add VOID decisions
		decisions = brain.getVoidDecisions();
		for (Decision d : decisions) {
			addDecision(d);
		}

		// Addd filler
		gbc.gridy++;
		gbc.weighty = 1;
		this.add(new JPanel(), gbc);
	}
	
	private void addDecision(Decision d) {
		DecisionPanel panel = new DecisionPanel(d);
		panel.build();
		panel.updateDecisions();

		decisionPanels.put(d, panel);

		this.add(panel, gbc);
		gbc.gridy++;
	}

	/**
	 * Adds all the buttons (reload etc.).
	 */
	private void addButtons() {
		reloadBrain.setIcon(GraphicResources.restartI);
		reloadBrain.addActionListener(this);

		hideAll.addActionListener(this);
		showAll.addActionListener(this);

		JPanel wrap = new JPanel(new GridBagLayout());
		GridBagConstraints _gbc = ConstraintsBuilder.build().gridxy(0).weightxy(0).fill(GridBagConstraints.NONE).get();

		wrap.add(reloadBrain, _gbc);
		_gbc.gridx++;

		// Filler
		_gbc.fill = GridBagConstraints.BOTH;
		_gbc.weightx = 1;
		wrap.add(new JPanel(), _gbc);

		_gbc.gridx++;
		_gbc.weightx = 0;
		_gbc.fill = GridBagConstraints.NONE;

		wrap.add(hideAll, _gbc);
		_gbc.gridx++;

		wrap.add(showAll, _gbc);

		this.add(wrap, gbc);
		gbc.gridy++;
	}

	/**
	 * Updates the viewed information.
	 */
	public void update() {
		LinkedList<Decision> decisions = brain.getAllDecisions();

		for (Decision d : decisions) {
			DecisionPanel dp = decisionPanels.get(d);
			if (dp == null) {
				logger.error("Unable to find decision in decision panels.");
			} else {
				dp.updateDecisions();
			}
		}
	}

	/**
	 * Called after the tab is focused.
	 */
	public void focused() {
		update();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == reloadBrain) {
			brain.reload();
			this.removeAll();
			this.build();
		} else if (e.getSource() == hideAll) {
			for (DecisionPanel d : decisionPanels.values()) {
				d.hideBody();
			}
		} else if (e.getSource() == showAll) {
			for (DecisionPanel d : decisionPanels.values()) {
				d.showBody();
			}
		}

	}

}
