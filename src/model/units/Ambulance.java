package model.units;

import static org.junit.Assume.assumeNoException;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {

	public Ambulance(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getBloodLoss() > 0) {
			target.setBloodLoss(target.getBloodLoss() - getTreatmentAmount());
			if (target.getBloodLoss() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getBloodLoss() == 0)

			heal();

	}

	public void respond(Rescuable r) throws Exception {

		if (r instanceof ResidentialBuilding) {

			throw new IncompatibleTargetException(this, r, "this unit can not be targeted to a building");
		}

		else {

			if (this.canTreat(r) == true) {
				if (getTarget() != null && ((Citizen) getTarget()).getBloodLoss() > 0
						&& getState() == UnitState.TREATING)
					reactivateDisaster();
				finishRespond(r);
			} else {
				throw new CannotTreatException(this, r, "this unit can not be treated");
			}

		}
	}

	@Override
	public boolean canTreat(Rescuable r) throws CannotTreatException {
		
			if (((Citizen) r).getState() == CitizenState.SAFE || ((Citizen) r).getBloodLoss()==0)
				return false;

			else
				return true;
		
		
	}
}
