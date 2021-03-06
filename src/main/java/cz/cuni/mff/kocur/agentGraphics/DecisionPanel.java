package cz.cuni.mff.kocur.agentGraphics;

import java.awt.GridBagConstraints;
import java.util.HashMap;
import java.util.LinkedList;

import cz.cuni.mff.kocur.considerations.Consideration;
import cz.cuni.mff.kocur.decisions.Decision;
import cz.cuni.mff.kocur.graphics.ConstraintsBuilder;
import cz.cuni.mff.kocur.graphics.WindowedJPanel;

public class DecisionPanel extends WindowedJPanel {

	/**
	 * Generated serial version id.
	 */
	private static final long serialVersionUID = -827329011888625701L;

	/**
	 * Reference to decision.
	 */
	private Decision decision = null;

	/**
	 * Panels with considerations.
	 */
	private HashMap<Consideration, ConsiderationInfoPanel> considerationPanels = new HashMap<>();

	/**
	 * Creates decision panel for supplied decision.
	 * 
	 * @param decision
	 *            Decision for which we create the panel.
	 */
	public DecisionPanel(Decision decision) {
		this.decision = decision;
		this.hideCloseLabel();
	}

	/**
	 * Builds the decision panel.
	 */
	public void build() {
		GridBagConstraints _gbc = ConstraintsBuilder.build().gridxy(0).weightxy(1, 0).insets(5)
				.fill(GridBagConstraints.BOTH).get();

		LinkedList<Consideration> considerations = decision.getConsiderations();

		for (Consideration c : considerations) {
			ConsiderationInfoPanel panel = new ConsiderationInfoPanel(c);
			considerationPanels.put(c, panel);

			body.add(panel, _gbc);
			_gbc.gridy++;
		}

		this.changeTitle(decision.getReadableName());
		super.build();
	}

	@Override
	protected void close() {
		// Do nothing, we do not want to close
	}

	/**
	 * Updates the decisions.
	 */
	public void updateDecisions() {
		LinkedList<Consideration> considerations = decision.getConsiderations();

		for (Consideration c : considerations) {
			ConsiderationInfoPanel panel = considerationPanels.get(c);

			if (panel == null) {
				logger.error("Trying to update unknow consideration panel.");
				continue;
			}

			panel.update();
		}

		this.changeTitle(decision.getReadableName() + " - " + decision.getScore());
	}

}
