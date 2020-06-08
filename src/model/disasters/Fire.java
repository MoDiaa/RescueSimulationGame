package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import model.infrastructure.ResidentialBuilding;

public class Fire extends Disaster {

	public Fire(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);

	}

	public void strike() throws DisasterException {
		
		if (((ResidentialBuilding) this.getTarget()).getStructuralIntegrity() >0){

				ResidentialBuilding target = (ResidentialBuilding) getTarget();
				target.setFireDamage(target.getFireDamage() + 10);
				
					super.strike();
				
			}
		else 
			throw new BuildingAlreadyCollapsedException(this,"already collapsed");
	}

	@Override
	public void cycleStep() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		target.setFireDamage(target.getFireDamage() + 10);

	}

}
