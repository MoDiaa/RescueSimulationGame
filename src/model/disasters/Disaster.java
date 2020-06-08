package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Disaster implements Simulatable {
	private int startCycle;
	private Rescuable target;
	private boolean active;

	public Disaster(int startCycle, Rescuable target) {
		this.startCycle = startCycle;
		this.target = target;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getStartCycle() {
		return startCycle;
	}

	public Rescuable getTarget() {
		return target;
	}


	public void strike() throws  DisasterException {
		if(this.getTarget() instanceof ResidentialBuilding) {
			ResidentialBuilding x=(ResidentialBuilding)this.getTarget();
			if(x.getDisaster() instanceof Collapse)
				throw new BuildingAlreadyCollapsedException(this);
		}
		if(this.getTarget() instanceof Citizen) {
			Citizen x= (Citizen) this.getTarget();
			if(x.getState()==CitizenState.DECEASED)
				throw new CitizenAlreadyDeadException(this);
		}
		target.struckBy(this);
		active = true;
	}


	public boolean citizennotdead(Rescuable r) throws CitizenAlreadyDeadException {
		Citizen c = (Citizen) r;
		if (((Citizen) this.target).getState() == CitizenState.DECEASED) {
			throw new CitizenAlreadyDeadException(this,
					"the citizen is already dead no disaster can be striked on the citizen");
		}
		else {return true;}

	}
	public String tostring () {
		

		if(this instanceof Collapse)
			return "Collapse";
		if(this instanceof Injury)
			return "Injury";
		if(this instanceof Infection)
			return "Infection";
		if(this instanceof GasLeak)
			return "GasLeak";
		if(this instanceof Fire)
			return "Fire" ;
		else
			return "no Disaster";
	}
	
	public boolean buildingnotcollapsed(Rescuable r) throws BuildingAlreadyCollapsedException {
		ResidentialBuilding c = (ResidentialBuilding) r;
		if (((ResidentialBuilding) this.target).getFoundationDamage()==0 ) {
			throw new BuildingAlreadyCollapsedException(this,
					"the Building is already Collapsed no disaster can be striked on the citizen");
		}
		else {return true;}

	}
}
