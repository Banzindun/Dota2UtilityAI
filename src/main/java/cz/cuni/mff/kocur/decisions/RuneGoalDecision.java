package cz.cuni.mff.kocur.decisions;

import cz.cuni.mff.kocur.base.Location;
import cz.cuni.mff.kocur.influence.ExtendedAgentContext;
import cz.cuni.mff.kocur.influence.GoalLayer;
import cz.cuni.mff.kocur.server.AgentCommand;
import kocur.lina.agent.LayeredAgentContext;

/**
 * Decision that sets rune as our goal.
 * 
 * @author kocur
 *
 */
public class RuneGoalDecision extends Decision {

	public RuneGoalDecision() {

	}

	@Override
	public AgentCommand execute() {
		Location target = context.getTarget().getLocation();
		if (target == null)
			return null;

		// Set the creep to goal layer as our goal.
		((GoalLayer) context.getBotContext().getLayer(LayeredAgentContext.GOAL)).setGoal(target);

		// Set the last time of this decision
		super.execute();

		// Return null, we are a void decision
		return null;
	}

	@Override
	public void updateContext(ExtendedAgentContext bc) {
		// We will need a location of the most distant creep, as he defines the front of
		// our lane.
		Location botBounty = bc.getMyJungle().getBotBounty();
		context.setTarget(new Target(botBounty));
	}

	@Override
	public void presetContext(ExtendedAgentContext bc) {
		// We just set the context of the bot to our decision context.
		context.setBotContext(bc);
		context.setSource(bc.getHero());
	}

}
