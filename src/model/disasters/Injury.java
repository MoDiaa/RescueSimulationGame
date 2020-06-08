package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import model.people.Citizen;
import model.people.CitizenState;

public class Injury extends Disaster {

	public Injury(int startCycle, Citizen target) {
		super(startCycle, target);
	}

	@Override
	public void strike() throws DisasterException

	{
		
		if (((Citizen) this.getTarget()).getState()!=CitizenState.DECEASED)  {
				Citizen target = (Citizen) getTarget();
				target.setBloodLoss(target.getBloodLoss() + 30);
			
					super.strike();
				
			}
		else 
			throw new CitizenAlreadyDeadException(this,"already dead");
		
	}

	@Override
	public void cycleStep() {
		Citizen target = (Citizen) getTarget();
		target.setBloodLoss(target.getBloodLoss() + 10);

	}

}
