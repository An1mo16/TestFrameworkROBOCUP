/**
 * Name:    AgentMonitorFrame.java
 * Created: 11.4.2012
 * 
 * @author: Peto
 */
package sk.fiit.testframework.ui;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.IAgentManagerListener;
import sk.fiit.testframework.monitor.RobocupMonitor;
import sk.fiit.testframework.worldrepresentation.ISimulationStateObserver;
import sk.fiit.testframework.worldrepresentation.models.SimulationState;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/

/**
 * Window for monitoring an agents' position and rotation. One windows exists for each monitored
 * agent. The window is displayed on the user's demand by clicking a "Monitor" button in the
 * main GUI window.
 */
public class AgentMonitorFrame extends JFrame implements IAgentManagerListener, ISimulationStateObserver {
	private AgentJim agent;
	private MeasurableInformation atStart;
	private RobocupMonitor monitor;
	
	/**
	 * Whether this window's agent still exists. Once it disconnects, the updates will stop
	 */
	private boolean hasAgent = true;
	
	public AgentMonitorFrame(AgentJim agent, MeasurableInformation atStart, RobocupMonitor monitor) {
		createGUI();
		this.agent = agent;
		this.atStart = atStart;
		this.monitor = monitor;
		monitor.getSimulationState().registerObserver(this);
		setTitle(agent.toString() + " monitor");
		
		addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent arg0) { }
			public void windowClosed(WindowEvent arg0) { }
			public void windowClosing(WindowEvent arg0) { onWindowClosing(); }
			public void windowDeactivated(WindowEvent arg0) { }
			public void windowDeiconified(WindowEvent arg0) { }
			public void windowIconified(WindowEvent arg0) { }
			public void windowOpened(WindowEvent arg0) { }
		});
	}
	
	protected void onWindowClosing() {
		monitor.getSimulationState().unregisterObserver(this);
	}

	@Override
	public void update() {
		if (hasAgent) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					SimulationState ss = monitor.getSimulationState();
					MeasurableInformation now = new MeasurableInformation(ss, agent.getSceneGraphPlayer());
					MeasurableInformation dist = MeasurableInformation
							.createDistanceInformation(now, atStart);

					txtPosCurrent.setText(now.agentPosition.toString());
					txtPosDist.setText(dist.agentPosition.toString());
					txtPosDist2D.setText(String.format("%.3f", atStart.agentPosition.getXYDistanceFrom(now.agentPosition)));

					txtRotCurrent.setText(now.agentRotation.toString());
					txtRotDist.setText(dist.agentRotation.toString());
				}
			});
		}
	}

	@Override
	public void agentRemoved(AgentJim agent) {
		if (agent == this.agent) {
			setTitle("(dead) " + agent.toString() + " monitor");
			this.agent = null;
			hasAgent = false;
		}
	}
	
	
	public void createGUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("agent_name monitor");
		GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
		getContentPane().setLayout(thisLayout);
		this.setSize(334, 278);
		{
			jPanel1 = new JPanel();
			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1Layout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanel1Layout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 7};
			jPanel1Layout.columnWeights = new double[] {0.1, 0.1};
			jPanel1Layout.columnWidths = new int[] {7, 7};
			jPanel1.setLayout(jPanel1Layout);
			{
				JLabel lblPosition = new JLabel("Position");
				jPanel1.add(lblPosition, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				JLabel lblNow = new JLabel("Current");
				jPanel1.add(lblNow, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
			}
			{
				JLabel lblDistance = new JLabel("Distance");
				jPanel1.add(lblDistance, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
			}
			{
				JLabel lbldDistance = new JLabel("2D distance");
				jPanel1.add(lbldDistance, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
			}
			{
				JLabel lblRotation = new JLabel("Rotation");
				jPanel1.add(lblRotation, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				JLabel lblCurrent = new JLabel("Current");
				jPanel1.add(lblCurrent, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
			}
			{
				JLabel lblDistance_1 = new JLabel("Distance");
				jPanel1.add(lblDistance_1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
			}
			{
				txtPosCurrent = new JTextField();
				jPanel1.add(txtPosCurrent, new GridBagConstraints(1, 1, 1, 1, 2.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				txtPosCurrent.setEditable(false);
			}
			{
				txtPosDist = new JTextField();
				jPanel1.add(txtPosDist, new GridBagConstraints(1, 2, 1, 1, 2.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				txtPosDist.setEditable(false);
			}
			{
				txtPosDist2D = new JTextField();
				jPanel1.add(txtPosDist2D, new GridBagConstraints(1, 3, 1, 1, 2.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				txtPosDist2D.setEditable(false);
			}
			{
				txtRotCurrent = new JTextField();
				jPanel1.add(txtRotCurrent, new GridBagConstraints(1, 5, 1, 1, 2.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				txtRotCurrent.setEditable(false);
			}
			{
				txtRotDist = new JTextField();
				jPanel1.add(txtRotDist, new GridBagConstraints(1, 6, 1, 1, 2.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				txtRotDist.setEditable(false);
			}
		}
				thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(jPanel1, 0, 217, Short.MAX_VALUE)
					.addContainerGap());
				thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(jPanel1, 0, 314, Short.MAX_VALUE)
					.addContainerGap());
	}

	private static final long serialVersionUID = -4873775636134147362L;
	private JPanel jPanel1;
	private JTextField txtPosCurrent;
	private JTextField txtRotDist;
	private JTextField txtRotCurrent;
	private JTextField txtPosDist2D;
	private JTextField txtPosDist;

	//unused interface methods
	public void agentOutputLine(AgentJim agent, String line) { }
	public void agentAdded(AgentJim agent) { }
}
