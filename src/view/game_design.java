package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import model.units.FireTruck;

public class game_design extends JFrame {
	private JPanel myleftPanel;
	private static int amb=0;
	private static int evac=0;
	private static int gas=0;
	private static int fir=0;
	private static int dis=0;
	private JPanel myrightPanel;
	private JButton ambulance;
	private JButton firetruck;
	private JButton Evacuator;
	private JButton GasControlUnit;
	private JButton nextcycle;
	private JPanel myrightPanel2;
	private JPanel mycenterPanel;
	public JButton[] activeUnits = new JButton[5];
	private JLabel myleftLabel1;
    private JButton start = null;
	private JButton diseaseunit;
	private JButton gasunit;
	private JTextArea disasterinfo;
	private JLabel mycenterLabel;
	private JLabel background;
	private JTextArea simInfo;
	private JPanel rightdown;
	private JPanel right;
	private JTextArea currentcycle;
	private JTextArea casualties;
	public JTextArea getCurrentcycle() {
		return currentcycle;
	}

	public JTextArea getCasualties() {
		return casualties;
	}



	public static int getAmb() {
		return amb;
	}

	public static void setAmb(int amb) {
		game_design.amb = amb;
	}

	public static int getEvac() {
		return evac;
	}

	public static void setEvac(int evac) {
		game_design.evac = evac;
	}

	public static int getGas() {
		return gas;
	}

	public static void setGas(int gas) {
		game_design.gas = gas;
	}

	public static int getFir() {
		return fir;
	}

	public static void setFir(int fir) {
		game_design.fir = fir;
	}

	public static int getDis() {
		return dis;
	}

	public static void setDis(int dis) {
		game_design.dis = dis;
	}

	public JButton getFiretruck() {
		return firetruck;
	}

	public JButton getEvacuator() {
		return Evacuator;
	}

	public JButton getDiseaseunit() {
		return diseaseunit;
	}

	public JButton getGasunit() {
		return gasunit;
	}

	public JPanel getMyrightPanel2() {
		return myrightPanel2;
	}

	public JPanel getMyleftPanel() {
		return myleftPanel;
	}

	public JTextArea getSimInfo() {
		return simInfo;
	}

	public JPanel getMyrightPanel() {
		return myrightPanel;
	}

	public JLabel getMyleftLabel1() {
		return myleftLabel1;
	}

	public JLabel getMycenterLabel() {
		return mycenterLabel;
	}

	public JPanel getMycenterPanel() {
		return mycenterPanel;
	}

	public game_design() {
		setLayout(null);
		JFrame myview = new JFrame();
		disasterinfo = new JTextArea();
		myleftPanel = new JPanel();
		myrightPanel = new JPanel();
		myrightPanel2 = new JPanel();
		mycenterPanel = new JPanel();
		background = new JLabel();
		casualties=new JTextArea();
		currentcycle= new JTextArea();
		simInfo = new JTextArea();
		ambulance = new JButton("Ambulance");
		Evacuator = new JButton("Evacuator");
		firetruck = new JButton("Fire Truck");
		diseaseunit = new JButton("Disease Unit");
		gasunit = new JButton("Gas Unit");
		activeUnits[0]= ambulance;
		activeUnits[1]=Evacuator ;
		activeUnits[2]=firetruck ;
		activeUnits[3]= diseaseunit;
		activeUnits[4]=gasunit ;
		mycenterPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width - 800,
				Toolkit.getDefaultToolkit().getScreenSize().height));
		mycenterPanel.setLayout(new GridLayout(10, 10, 10, 10));
		myleftPanel.setPreferredSize(new Dimension(400, Toolkit.getDefaultToolkit().getScreenSize().height));
		mycenterPanel.setBackground(Color.DARK_GRAY);
		addbuttons();
		myrightPanel.setBackground(Color.DARK_GRAY);
		myleftPanel.setLayout(new GridLayout(5,1,10,10));
		casualties.setBackground(Color.BLACK);
		currentcycle.setBackground(Color.BLACK);
		myleftPanel.add(casualties);
		myleftPanel.setBackground(Color.DARK_GRAY);
		myleftPanel.add(currentcycle);
		casualties.setLineWrap(true);
		casualties.setWrapStyleWord(true);
		casualties.setEnabled(false);
		casualties.setEditable(false);
casualties.setFont(new Font("alien encounters",Font.ROMAN_BASELINE , 26));
casualties.setText("Number of people dead=0");
currentcycle.setLineWrap(true);
currentcycle.setWrapStyleWord(true);
currentcycle.setEnabled(false);
currentcycle.setEditable(false);
currentcycle.setFont(new Font("alien encounters",Font.ROMAN_BASELINE , 26));
currentcycle.setText("Cycle:0");
		
		

		simInfo.setLineWrap(true);
		simInfo.setWrapStyleWord(true);
		simInfo.setEnabled(false);
		simInfo.setEditable(false);
		simInfo.setBackground(Color.BLACK);
		simInfo.setFont(simInfo.getFont().deriveFont(Font.ROMAN_BASELINE, 15f));
		JScrollPane scroll = new JScrollPane(simInfo);
		myleftPanel.add(scroll, BorderLayout.NORTH);
		disasterinfo.setLineWrap(true);
		disasterinfo.setWrapStyleWord(true);
		disasterinfo.setEnabled(false);
		disasterinfo.setEditable(false);
		disasterinfo.setBackground(Color.BLACK);
		disasterinfo.setFont(disasterinfo.getFont().deriveFont(Font.ROMAN_BASELINE, 15f));
		JScrollPane scroll2 = new JScrollPane(disasterinfo);
		myleftPanel.add(scroll2);
		nextcycle = new JButton("next cycle");
		nextcycle.setIcon(new ImageIcon("joystick.png"));
		nextcycle.setFont(nextcycle.getFont().deriveFont(Font.ROMAN_BASELINE, 24f));
		myleftPanel.add(nextcycle, BorderLayout.SOUTH);
		
		myview.add(mycenterPanel, BorderLayout.CENTER);
		myview.add(myrightPanel, BorderLayout.EAST);
		
		myview.setExtendedState(JFrame.MAXIMIZED_BOTH);
		myview.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myview.add(myleftPanel, BorderLayout.WEST);
myrightPanel2.setLayout(new GridLayout(5,1));
myrightPanel2.add(ambulance);
myrightPanel2.add(diseaseunit);
myrightPanel2.add(Evacuator);
myrightPanel2.add(firetruck);
myrightPanel2.add(gasunit);
Evacuator.setIcon(new ImageIcon("PoliceCar_Blue.png"));
firetruck.setIcon(new ImageIcon("FireTruck_Red.png"));
ambulance.setIcon(new ImageIcon("Ambulance_Red.png"));
diseaseunit.setIcon(new ImageIcon("police-car (1).png"));
gasunit.setIcon(new ImageIcon("tank-truck.png"));
		myrightPanel.add(myrightPanel2);
		myview.setVisible(true);
	}

	public JButton[] getActiveUnits() {
		return activeUnits;
	}

	public JTextArea getDisasterinfo() {
		return disasterinfo;
	}

	public JButton getAmbulance() {
		return ambulance;
	}

	public JButton getStart() {
		return start;
	}

	public void addbuttons() {

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (i == 0 && j == 0) {
					this.addCenter();
				} else {
					JButton new1 = new JButton(i + " " + j);
					new1.setSize(30, 30);
					new1.setVisible(true);
					new1.setBackground(Color.white);
					new1.setIcon(new ImageIcon("blueSheet.png"));

					this.mycenterPanel.add(new1);
				}
			}

		}

	}

	public void addCenter() {
		start = new JButton(0 + " " + 0);
		start.setSize(30, 30);
		start.setVisible(true);
		start.setBackground(Color.WHITE);
		start.setIcon(new ImageIcon("base (1).png"));
		this.mycenterPanel.add(start);

	}
	
}