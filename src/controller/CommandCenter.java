package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import exceptions.CannotTreatException;
import exceptions.DisasterException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import model.units.UnitState;
import simulation.Rescuable;
import simulation.Simulator;
import view.game_design;

public class CommandCenter implements SOSListener {
	private JButton[][] buttonarray = new JButton[10][10];
	private JButton selectedUnitb = new JButton();
	private Unit selectedUnit = null;
	private ArrayList<Citizen> koftacitizen = new ArrayList<Citizen>();
	private int xpos;
	private int ypos;
	private Simulator engine;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private int countcycle = 1;
	@SuppressWarnings("unused")
	private ArrayList<Unit> emergencyUnits;

	public CommandCenter() throws Exception {
		engine = new Simulator(this);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		emergencyUnits = engine.getEmergencyUnits();

	}

	@Override
	public void receiveSOSCall(Rescuable r) {

		if (r instanceof ResidentialBuilding) {

			if (!visibleBuildings.contains(r))
				visibleBuildings.add((ResidentialBuilding) r);

		} else {

			if (!visibleCitizens.contains(r))
				visibleCitizens.add((Citizen) r);
		}

	}

	public String disastersAffectingCitizens(Citizen x) {
		String s = "Disasters affecting Citizen : ";
		if (x.getDisaster() == null) {
			s += " No Disasters Affecting Citizen";
		} else {
			if (x.getDisaster() instanceof Injury)
				s += "Injury ";
			if (x.getDisaster() instanceof Infection)
				s += "Inection ";
		}
		s += "\n";
		return s;
	}

	public void setcitizen(game_design gameview) {
		JPanel centerpanel = gameview.getMycenterPanel();

		JTextArea citizeninfo = gameview.getSimInfo();
		int c = 0;
		int l = 0;
		for (int i = 0; i < visibleCitizens.size(); i++) {
			Citizen r = visibleCitizens.get(i);
			JButton x = this.buttonarray[visibleCitizens.get(i).getLocation().getX()][visibleCitizens.get(i)
					.getLocation().getY()];
			if ((visibleCitizens.get(i).getDisaster()) instanceof Infection)
				x.setIcon(new ImageIcon("virus.png"));
			if ((visibleCitizens.get(i).getDisaster()) instanceof Injury)
				x.setIcon(new ImageIcon("murder.png"));
			if(((r.getBloodLoss()>=50 && r.getDisaster().isActive()==true) ||(r.getToxicity()>=50 && r.getDisaster().isActive()==true)) && r.getState()==CitizenState.IN_TROUBLE )
			{
				
				ImageIcon image3=new ImageIcon("hmootyanas.jpg");
                JOptionPane.showMessageDialog(null,"hamoot ya nas "+"\n"+"at location"+r.getLocation().getX()+","+r.getLocation().getY()+").",null, JOptionPane.PLAIN_MESSAGE, image3);
				
				
			}
			if ((visibleCitizens.get(i).getState() == CitizenState.DECEASED))
				x.setIcon(new ImageIcon("deceased.png"));
			if ((visibleCitizens.get(i).getState() == CitizenState.RESCUED))
				x.setIcon(new ImageIcon("student.png"));
			

			x.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (selectedUnit != null) {
						try {
							selectedUnit.respond(r);
							selectedUnit = null;
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
							selectedUnit = null;

						}
					}

					citizeninfo.setText("location:" + r.getLocation().getX() + " " + r.getLocation().getY() + "\n"

							+ "Citizens info:" + "\n" + "Name : " + r.getName() + "\n" + "Age : " + r.getAge() + "\n"
							+ "HP : " + r.getHp() + "\n" + "Blood Loss : " + r.getBloodLoss() + "\n" + "Toxicity : "
							+ r.getToxicity() + "\n" + "Citizen State : " + r.getState() + "\n"
							+ disastersAffectingCitizens(r));

				}
			});
			for (int k = 0; k < 10; k++) {
				c = l;
				for (int k2 = 0; k2 < 10; k2++) {
					if (k + k2 + c < 100 && ((JButton) centerpanel.getComponent(k + k2 + c)).getText() == x.getText()) {

						centerpanel.add(x, k + k2 + c);
					}
					l = k + k2 + c;

				}

			}

		}

	}

	public void setbuildingdisaster(game_design gameview) {
		JPanel centerpanel = gameview.getMycenterPanel();

		JTextArea buildinginfo = gameview.getSimInfo();
		int c = 0;
		int l = 0;
		for (int i = 0; i < visibleBuildings.size(); i++) {
			ArrayList<Citizen> h = visibleBuildings.get(i).getOccupants();
			ResidentialBuilding r = visibleBuildings.get(i);
			JButton x = this.buttonarray[visibleBuildings.get(i).getLocation().getX()][visibleBuildings.get(i)
					.getLocation().getY()];

			if ((visibleBuildings.get(i).getDisaster()) instanceof GasLeak
					&& visibleBuildings.get(i).getFoundationDamage() == 0)
				x.setIcon(new ImageIcon("gasleakedbuilding.jpg"));
			if ((visibleBuildings.get(i).getDisaster()) instanceof Fire
					&& visibleBuildings.get(i).getFoundationDamage() == 0)
				x.setIcon(new ImageIcon("firedbuilding.jpg"));
			if (visibleBuildings.get(i).getFoundationDamage() > 0) {
				x.setIcon(new ImageIcon("collapsedbuilding.jpg"));
			}
			if (r.getDisaster().isActive() == false) {
				x.setIcon(new ImageIcon("normalbuilding.jpg"));
			}
			if (r.getStructuralIntegrity() <= 0) {
				x.setIcon(new ImageIcon("structure integrity.jpg"));

			}

			x.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (selectedUnit != null) {
						try {
							selectedUnit.respond(r);
							if (selectedUnit instanceof Ambulance)
								gameview.setAmb(gameview.getAmb() - 1);
							if (selectedUnit instanceof Evacuator)
								gameview.setEvac(gameview.getEvac() - 1);
							if (selectedUnit instanceof GasControlUnit)
								gameview.setGas(gameview.getGas() - 1);
							if (selectedUnit instanceof DiseaseControlUnit)
								gameview.setDis(gameview.getDis() - 1);
							if (selectedUnit instanceof FireTruck)
								gameview.setFir(gameview.getFir() - 1);
							selectedUnit = null;
						} catch (Exception e1) {

							JOptionPane.showMessageDialog(null, e1.getMessage());
							selectedUnit = null;

						}
					}
					buildinginfo.setText("location:" + r.getLocation().getX() + " " + r.getLocation().getY() + "\n"
							+ "structural integrity:" + r.getStructuralIntegrity() + "\n" + "FireDamage"
							+ r.getFireDamage() + "\n" + "GasLevel:" + r.getGasLevel() + "\n" + "foundation damage:"
							+ r.getFoundationDamage() + "\n" + "number of occupants:" + r.getOccupants().size() + "\n"
							+ "Citizens info:" + citizeninfo(h) + "\n" + " disaster affecting the building:"
							+ r.getDisaster().tostring());

				}
			});
			for (int k = 0; k < 10; k++) {
				c = l;
				for (int k2 = 0; k2 < 10; k2++) {
					if (k + k2 + c < 100 && ((JButton) centerpanel.getComponent(k + k2 + c)).getText() == x.getText()) {

						centerpanel.add(x, k + k2 + c);
					}
					l = k + k2 + c;

				}

			}

		}

	}

	public static String citizeninfo(ArrayList<Citizen> c) {
		String info = "";
		for (int i = 0; i < c.size(); i++) {
			Citizen c1 = c.get(i);
			String disastermessage = "";
			if (c1.getDisaster() == null) {
				disastermessage = "no Disaster";
			} else {
				disastermessage = c1.getDisaster().tostring();
			}
			info = info + "location:" + c1.getLocation().getX() + " " + c1.getLocation().getY() + "\n" + "Citizen name;"
					+ c1.getName() + "\n" + "Citizen age;" + c1.getAge() + "\n" + "Citizen NationalID;"
					+ c1.getNationalID() + "\n" + "HP:" + c1.getHp() + "\n" + "BloodLoss" + c1.getBloodLoss() + "\n"
					+ "Toxicity:" + c1.getToxicity() + "\n" + "Citizen state:" + c1.getState() + "\n"
					+ "disaster affecting the citizen:" + disastermessage + "\n"
					+ "_________________________________________" + "\n";

		}
		return info;

	}

	public void setunits(game_design gameview) {

		JTextArea Unitsinfo = gameview.getSimInfo();

		for (int i = 0; i < emergencyUnits.size(); i++) {
			Unit r = emergencyUnits.get(i);
			addUnits(gameview, r, Unitsinfo);

		}

	}

	public void addUnits(game_design gameview, Unit unit, JTextArea Unitsinfo) {

		if (unit instanceof Evacuator) {

			if (unit.getState() == UnitState.IDLE) {
				gameview.getEvacuator().setBackground(Color.GREEN);

			}
			if (unit.getState() == UnitState.RESPONDING) {
				gameview.getEvacuator().setBackground(Color.RED);

			}
			if (unit.getState() == UnitState.TREATING) {
				gameview.getEvacuator().setBackground(Color.BLUE);

			}
			
			if (unit.getState() == UnitState.TREATING && unit.getLocation().getX() == 0
					&& unit.getLocation().getY() == 0) {
				gameview.getEvacuator().setBackground(Color.BLUE);
				for (Citizen c : ((Evacuator) unit).getPassengers())
					koftacitizen.add(c);

			}
			gameview.getEvacuator().addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					Unitsinfo.setText("location:" + unit.getLocation().getX() + " " + unit.getLocation().getY() + "\n"

							+ "Units Info : " + "\n" + "ID : " + unit.getUnitID() + "\n" + "Type : " + "Evacuator" + "\n"
							+ "Steps Per Cycle : " + unit.getStepsPerCycle() + "\n" + "Target : "+unit.targets() + "\n"
							+ "Unit State : " + unit.getState() + "\n" + "Number Of Passengers inside:"
							+ ((Evacuator) unit).getPassengers().size() + "\n" + "Passengers inside info:"
							+ unit.getpassinfo());

				}
			});
			// Evac.setIcon(new ImageIcon("evacuator.jpg"));

		}

		if (unit instanceof Ambulance) {

			if (unit.getState() == UnitState.IDLE) {
				gameview.getAmbulance().setBackground(Color.GREEN);
			}
			if (unit.getState() == UnitState.RESPONDING) {
				gameview.getAmbulance().setBackground(Color.RED);
			}
			if (unit.getState() == UnitState.TREATING) {
				gameview.getAmbulance().setBackground(Color.BLUE);
			}

			gameview.getAmbulance().addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					Unitsinfo.setText("location:" + unit.getLocation().getX() + " " + unit.getLocation().getY() + "\n"

							+ "Units info:" + "\n" + "ID : " + unit.getUnitID() + "\n" + "Type : " + "Ambulance"
							+ "\n" + "Steps Per Cycle : " + unit.getStepsPerCycle() + "\n" + "Target : "
							+ unit.targets() + "\n" + "Unit State : " + unit.getState() + "\n");

				}
			});
		}

		if (unit instanceof FireTruck) {
			if (unit.getState() == UnitState.IDLE) {
				gameview.getFiretruck().setBackground(Color.GREEN);
			}
			if (unit.getState() == UnitState.RESPONDING) {
				gameview.getFiretruck().setBackground(Color.RED);
			}
			if (unit.getState() == UnitState.TREATING) {
				gameview.getFiretruck().setBackground(Color.BLUE);
			}
			// fire.setIcon(new ImageIcon("FireTruck_Red.png"));

			gameview.getFiretruck().addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					Unitsinfo.setText("location:" + unit.getLocation().getX() + " " + unit.getLocation().getY() + "\n"

							+ "Units info:" + "\n" + "ID : " + unit.getUnitID() + "\n" + "Type : " + "Fire Truck"
							+ "\n" + "Steps Per Cycle : " + unit.getStepsPerCycle() + "\n" + "Target : "
							+ unit.targets() + "\n" + "Unit State : " + unit.getState() + "\n");

				}
			});
		}

		if (unit instanceof GasControlUnit)

		{
			if (unit.getState() == UnitState.IDLE) {
				gameview.getGasunit().setBackground(Color.GREEN);
			}
			if (unit.getState() == UnitState.RESPONDING) {
				gameview.getGasunit().setBackground(Color.RED);
			}
			if (unit.getState() == UnitState.TREATING) {
				gameview.getGasunit().setBackground(Color.BLUE);
			}
			// fire.setIcon(new ImageIcon("FireTruck_Red.png"));

			gameview.getGasunit().addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					Unitsinfo.setText("location:" + unit.getLocation().getX() + " " + unit.getLocation().getY() + "\n"

							+ "Units info:" + "\n" + "ID : " + unit.getUnitID() + "\n" + "Type : " + "Fire Truck"
							+ "\n" + "Steps Per Cycle : " + unit.getStepsPerCycle() + "\n" + "Target : "
							+ unit.targets() + "\n" + "Unit State : " + unit.getState() + "\n");

				}
			});
		}
		if (unit instanceof DiseaseControlUnit) {
			if (unit.getState() == UnitState.IDLE) {
				gameview.getDiseaseunit().setBackground(Color.GREEN);
			}
			if (unit.getState() == UnitState.RESPONDING) {
				gameview.getDiseaseunit().setBackground(Color.RED);
			}
			if (unit.getState() == UnitState.TREATING) {
				gameview.getDiseaseunit().setBackground(Color.BLUE);
			}
			// fire.setIcon(new ImageIcon("FireTruck_Red.png"));

			gameview.getDiseaseunit().addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					Unitsinfo.setText("location:" + unit.getLocation().getX() + " " + unit.getLocation().getY() + "\n"

							+ "Units info:" + "\n" + "Name : " + unit.getUnitID() + "\n" + "Type : " + "Fire Truck"
							+ "\n" + "Steps Per Cycle : " + unit.getStepsPerCycle() + "\n" + "Target : "
							+ unit.targets() + "\n" + "Unit State : " + unit.getState() + "\n");

				}
			});
		}

	}

	public String disastersInfo() {
		String s = "";
		for (ResidentialBuilding x : visibleBuildings) {
			if (x.getDisaster() != null) {
				s += "Disaster : ";
				Disaster d = x.getDisaster();
				if (d instanceof Fire) {
					s += "Fire";
				}
				if (d instanceof GasLeak) {
					s += "Gas Leak";
				}
				if (d instanceof Collapse) {
					s += "Collapse";
				}

				s += " at building x=" + x.getLocation().getX() + " y= " + x.getLocation().getY() + "\n";
			}
		}
		for (Citizen c : visibleCitizens) {
			if (c.getDisaster() != null) {
				Disaster x = c.getDisaster();
				s += "Disaster : ";
				if (x instanceof Infection)
					s += "Infection";
				if (x instanceof Injury)
					s += "Injury";

				s += " at x = " + c.getLocation().getX() + " y = " + c.getLocation().getY() + "\n";

			}
		}
		return s;
	}

	public void nextcycle(game_design gameview) {
		JButton nextcycle = (JButton) gameview.getMyleftPanel().getComponent(4);
		gameview.getStart().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JTextArea info = gameview.getSimInfo();
				info.setText("");

				for (Citizen c : koftacitizen) {
					if (c.getLocation().getX() == 0 && c.getLocation().getY() == 0) {
						info.setText("\n" + "Citizen with id" + c.getNationalID() + "is here" + "\n" + info.getText());

					}
				}
				info.setText("\n" + "________" + info.getText());

				for (Unit c : emergencyUnits) {
					if (c.getLocation().getX() == 0 && c.getLocation().getY() == 0) {
						info.setText("Unit with ID" + c.getUnitID() + "Is here" + "\n" + info.getText());

					}

				}
				// info.setFont(new Font("alien encounters",Font.BOLD , 18));

			}

		});

		nextcycle.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				selectedUnit = null;

				try {

					engine.nextCycle();

					int l = 0;
					for (Citizen c : visibleCitizens) {
						if (c.getState() == CitizenState.DECEASED) {
							l++;

						}
					}
					for (ResidentialBuilding b : visibleBuildings) {
						for (Citizen c : b.getOccupants()) {
							if (c.getState() == CitizenState.DECEASED) {
								l++;
							}

						}

					}
					gameview.getCasualties().setText("Number of people dead=" + l);
					gameview.getCurrentcycle().setText("Cycle:" + countcycle);
					setbuildingdisaster(gameview);
					JTextArea info = gameview.getDisasterinfo();
					String P = "" + "\n" + info.getText();
					String s = "";
					for (ResidentialBuilding x : visibleBuildings) {
						if (x.getDisaster().isActive() == true) {

							Disaster d = x.getDisaster();
							if (d.getStartCycle() == countcycle && d instanceof Fire) {
								s += "Disaster : ";
								s += "Fire";
								s += " at building x=" + x.getLocation().getX() + " y= " + x.getLocation().getY()
										+ "\n";
							}
							if (d instanceof GasLeak && d.getStartCycle() == countcycle) {
								s += "Disaster : ";
								s += "Gas Leak";
								s += " at building x=" + x.getLocation().getX() + " y= " + x.getLocation().getY()
										+ "\n";
							}
							if (d instanceof Collapse && d.getStartCycle() == countcycle) {
								s += "Disaster : ";
								s += "Collapse";
								s += " at building x=" + x.getLocation().getX() + " y= " + x.getLocation().getY()
										+ "\n";
							}

						}

					}
					for (Citizen c : visibleCitizens) {
						if (c.getDisaster() != null) {
							Disaster x = c.getDisaster();

							if (x instanceof Infection && x.getStartCycle() == countcycle) {
								s += "Disaster : ";
								s += "Infection";
								s += " at x = " + c.getLocation().getX() + " y = " + c.getLocation().getY() + "\n";
							}
							if (x instanceof Injury && x.getStartCycle() == countcycle) {
								s += "Disaster : ";
								s += "Injury";
								s += " at x = " + c.getLocation().getX() + " y = " + c.getLocation().getY() + "\n";

							}

						}
					}
					if (engine.checkGameOver()) {
						
					ImageIcon	image2=new ImageIcon("game-over.png");
						JOptionPane.showMessageDialog(null, "Game Over", "See U Soon", 
							    JOptionPane.PLAIN_MESSAGE, image2);
						

						System.exit(0);
					}
					gameview.validate();
					info.setText("cycle:" + countcycle + "\n" + s + "\n" + P);
					countcycle++;
					setcitizen(gameview);
					setunits(gameview);

				} catch (DisasterException e1) {

					System.out.println(e1.getMessage());
				}
			}

		});
	}

	public JButton[][] getButtonarray() {
		return buttonarray;
	}

	public void buttonsgetter(game_design gameview) {
		int p = 0;
		int c = 0;
		int z = 0;
		JPanel panel = gameview.getMycenterPanel();
		for (int i = 0; i < 10; i++) {
			c = p;
			System.out.println();
			for (int j = 0; j < 10; j++) {

				if (j + c < 100) {
					this.buttonarray[i][j] = (JButton) panel.getComponent(j + c);

				}

				p = 10 + c;

			}

		}

	}

	public void commandbutton(JButton[][] buttonarray) {

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {

			}

		}

	}

	public void toggle(game_design gameview) {
		JButton x1 = gameview.getActiveUnits()[0];
		JButton x2 = gameview.getActiveUnits()[1];
		JButton x3 = gameview.getActiveUnits()[2];
		JButton x4 = gameview.getActiveUnits()[3];
		JButton x5 = gameview.getActiveUnits()[4];

		x1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedUnitb = x1;

				for (Unit x : emergencyUnits) {
					if (x instanceof Ambulance) {
						selectedUnit = x;
					}
				}

			}
		});
		x2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedUnitb = x2;
				for (Unit x : emergencyUnits) {
					if (x instanceof Evacuator) {
						selectedUnit = x;
					}
				}
			}
		});
		x3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedUnitb = x3;
				for (Unit x : emergencyUnits) {
					if (x instanceof FireTruck) {
						selectedUnit = x;
					}

				}

			}
		});
		x4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedUnitb = x4;
				for (Unit x : emergencyUnits) {
					if (x instanceof DiseaseControlUnit) {
						selectedUnit = x;
					}

				}
				System.out.println(selectedUnit);

			}
		});
		x5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedUnitb = x5;
				for (Unit x : emergencyUnits) {
					if (x instanceof GasControlUnit) {
						selectedUnit = x;
					}

				}

			}
		});

	}

	public void countUnits(game_design gameview) {
		for (Unit x : emergencyUnits) {
			if (x instanceof Ambulance)
				gameview.setAmb(gameview.getAmb() + 1);
			if (x instanceof Evacuator)
				gameview.setEvac(gameview.getAmb() + 1);
			if (x instanceof GasControlUnit)
				gameview.setGas(gameview.getGas() + 1);
			if (x instanceof DiseaseControlUnit)
				gameview.setDis(gameview.getDis() + 1);
		}
	}

	public static void main(String[] args) throws Exception {
		CommandCenter newcommandcenter = new CommandCenter();
		game_design gameview = new game_design();

		newcommandcenter.buttonsgetter(gameview);
		newcommandcenter.setunits(gameview);
		newcommandcenter.nextcycle(gameview);
		newcommandcenter.toggle(gameview);

	}

	static int caus;

	public void causalities() {
		for (ResidentialBuilding r : visibleBuildings) {
			for (Citizen c : r.getOccupants()) {
				if (c.getState() == CitizenState.DECEASED)
					caus++;
			}
		}
		for (Citizen c : visibleCitizens) {
			if (c.getState() == CitizenState.DECEASED)
				caus++;
		}
	}

}