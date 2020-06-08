package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private WorldListener worldListener;

	public Unit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener = worldListener;
	}

	public void setWorldListener(WorldListener listener) {
		this.worldListener = listener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

	@Override
	public void respond(Rescuable r) throws Exception {
		if (r instanceof Citizen) {

			throw new IncompatibleTargetException(this, r, "this unit can not be targeted to a citizen");

		} else {

			if (canTreat(r)) {
				if (target != null && state == UnitState.TREATING)
					reactivateDisaster();
				finishRespond(r);
			} else
				throw new CannotTreatException(this, r, "this target can not be treated");

		}
	}
	public String targets() {
		if(this.getTarget()!=null) {
			if(this.getTarget() instanceof Citizen)
			return "Citizen at location :"+this.getLocation().getX()+","+this.getLocation().getY();
			else
				return "building at location :"+this.getLocation().getX()+","+this.getLocation().getY();

			
		}
		else {return "no Target";}
		
		
		
		
	}

	public String getpassinfo() {
		Evacuator x = (Evacuator) this;
		String info = "";
		if(x.getState()==UnitState.IDLE) {
			
				
				info = info +"no citizens inside"+"\n"
						;
			
		
		}
		
		if(x.getState()==UnitState.RESPONDING) {
			
				
				info = info +"no citizens inside"+"\n"
						;
			
		
		}
		
		
		if(x.getState()==UnitState.TREATING) {
		for (int i = 0; i < x.getPassengers().size(); i++) {
			Citizen c = (Citizen) x.getPassengers().get(i);
			if(c.getDisaster()!=null) {
			info = info + "location:" + c.getLocation().getX() + " " + c.getLocation().getY() + "\n" + "Citizen name;"
					+ c.getName() + "\n" + "Citizen age;" + c.getAge() + "\n" + "Citizen NationalID;"
					+ c.getNationalID() + "\n" + "HP:" + c.getHp() + "\n" + "BloodLoss" + c.getBloodLoss() + "\n"
					+ "Toxicity:" + c.getToxicity() + "\n" + "Citizen state:" + c.getState() + "\n"
					+ "disaster affecting the citizen:" + c.getDisaster().tostring() + "\n"
					+ "_______________" + "\n";}
			else {	info = info + "location:" + c.getLocation().getX() + " " + c.getLocation().getY() + "\n" + "Citizen name;"
					+ c.getName() + "\n" + "Citizen age;" + c.getAge() + "\n" + "Citizen NationalID;"
					+ c.getNationalID() + "\n" + "HP:" + c.getHp() + "\n" + "BloodLoss" + c.getBloodLoss() + "\n"
					+ "Toxicity:" + c.getToxicity() + "\n" + "Citizen state:" + c.getState() + "\n"
					+ "disaster affecting the citizen: no disaster on the citizen"  + "\n"
					+ "_______________" + "\n";}

		}
		}
		return info;
		
	}
	

	public void reactivateDisaster() {
		Disaster curr = target.getDisaster();
		curr.setActive(true);
	}

	public void finishRespond(Rescuable r) {
		target = r;
		state = UnitState.RESPONDING;
		Address t = r.getLocation();
		distanceToTarget = Math.abs(t.getX() - location.getX()) + Math.abs(t.getY() - location.getY());

	}

	public abstract boolean canTreat(Rescuable r) throws CannotTreatException;

	/*
	 * if (r instanceof Citizen) { if (((Citizen) r).getState() == CitizenState.SAFE
	 * || ((Citizen) r).getState() == CitizenState.DECEASED) {
	 * 
	 * return false;
	 * 
	 * } else return true; } else { if (this instanceof Evacuator) { if
	 * (((ResidentialBuilding) r).getFoundationDamage() == 100 ||
	 * ((ResidentialBuilding) r).getFoundationDamage() == 0) return false; } else
	 * return true;
	 * 
	 * if (this instanceof FireTruck) { if (((ResidentialBuilding)
	 * r).getFireDamage() == 100 || ((ResidentialBuilding) r).getFireDamage() == 0
	 * || ((ResidentialBuilding) r).getFoundationDamage() == 0) return false;
	 * 
	 * } else return true;
	 * 
	 * if (this instanceof GasControlUnit) { if (((ResidentialBuilding)
	 * r).getGasLevel() == 100 || ((ResidentialBuilding) r).getGasLevel() == 0 ||
	 * ((ResidentialBuilding) r).getFoundationDamage() == 0) return false;
	 * 
	 * } else return true; return true; }
	 */

	public abstract void treat();

	public void cycleStep() {
		if (state == UnitState.IDLE)
			return;
		if (distanceToTarget > 0) {
			distanceToTarget = distanceToTarget - stepsPerCycle;
			if (distanceToTarget <= 0) {
				distanceToTarget = 0;
				Address t = target.getLocation();
				worldListener.assignAddress(this, t.getX(), t.getY());
			}
		} else {
			state = UnitState.TREATING;
			treat();
		}
	}

	public void jobsDone() {
		target = null;
		state = UnitState.IDLE;

	}
}