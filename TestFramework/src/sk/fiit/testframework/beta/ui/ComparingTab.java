/**
 * Name:    ComparingTab.java
 * Created: Mar 24, 2012
 * 
 * @author: tomasbolecek
 */
package sk.fiit.testframework.beta.ui;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.robocup.library.geometry.Vector3D;
import sk.fiit.testframework.monitor.AgentMonitor;
import sk.fiit.testframework.monitor.AgentMonitorMessage;
import sk.fiit.testframework.monitor.IAgentMonitorListener;
import sk.fiit.testframework.monitor.RobocupMonitor;
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
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author tomasbolecek
 *
 */
public class ComparingTab extends JPanel implements IAgentMonitorListener{

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JTabbedPane TabbedPane;
	private JPanel BallPanel;
	private JPanel playersPanel;
	private JPanel fromPlayerPanel;
	private JPanel fromServerPanel;
	private JPanel differencesPanel;
	private JPanel fromPlayerPanel1;
	private JPanel fromServerPanel1;
	private JPanel differencesPanel1;
	private JLabel jLabel1;
	private JLabel jLabel11;
	private JLabel jLabel111;
	private JLabel jLabel2;
	private JLabel jLabel21;
	private JLabel jLabel3;
	private JLabel jLabel31;
	private JLabel jLabel4;
	private JLabel jLabel41;
	private JLabel jLabel5;
	private JLabel jLabel51;
	private JLabel jLabel6;
	private JLabel jLabel61;
	private JLabel jLabel71;
	private JLabel jLabel81;
	private JComboBox playerViewComboBox;
	private JComboBox playerView2ComboBox;
	private JComboBox playerMonitoringComboBox;
	private JTextField axisZTextField;
	private JTextField axisYTextField;
	private JTextField axisXTextField;
	private JTextField axisZTextField2;
	private JTextField axisYTextField2;
	private JTextField axisXTextField2;
	private JTextField axisZTextField3;
	private JTextField axisYTextField3;
	private JTextField axisXTextField3;
	private JTextField angleTextField;
	private JTextField speedTextField;
	private JTextField angleTextField2;
	private JTextField speedTextField2;
	private JTextField angleTextField3;
	private JTextField speedTextField3;
	private JTextField axisZTextField1;
	private JTextField axisYTextField1;
	private JTextField axisXTextField1;
	private JTextField axisZTextField21;
	private JTextField axisYTextField21;
	private JTextField axisXTextField21;
	private JTextField axisZTextField31;
	private JTextField axisYTextField31;
	private JTextField axisXTextField31;
	private JTextField rotationZTextField1;
	private JTextField rotationYTextField1;
	private JTextField rotationXTextField1;
	private JTextField rotationZTextField21;
	private JTextField rotationYTextField21;
	private JTextField rotationXTextField21;
	private JTextField rotationZTextField31;
	private JTextField rotationYTextField31;
	private JTextField rotationXTextField31;
	private JTextField velocityTextField1;
	private JTextField velocityTextField21;
	private JTextField velocityTextField31;



	


	public ComparingTab() {
		initGUI();
	}
	
	private void initGUI() {
		try {
			setLayout(null);
			{
				this.setPreferredSize(new java.awt.Dimension(530, 360));
				{
					TabbedPane = new JTabbedPane();
					this.add(TabbedPane);
					TabbedPane.setBounds(12, 18, 497, 340);
					{
						BallPanel = new JPanel();
						TabbedPane.addTab("Ball", null, BallPanel, null);
						BallPanel.setLayout(null);
						BallPanel.setPreferredSize(new java.awt.Dimension(500, 299));
						
					
						{
							ComboBoxModel playerViewComboBoxModel = 
									new DefaultComboBoxModel(
											new String[] { "Player 1", "Player 2" });
							playerViewComboBox = new JComboBox();
							BallPanel.add(playerViewComboBox);
							playerViewComboBox.setModel(playerViewComboBoxModel);
							playerViewComboBox.setBounds(91, 21, 110, 27);
							playerViewComboBox.addItemListener(new ItemListener() {
								@Override
								public void itemStateChanged(ItemEvent arg0) {
									AgentMonitor.removeMessageListener(ComparingTab.this);
									AgentMonitor.setMessageListener(1, "ANDROIDS", ComparingTab.this, AgentMonitorMessage.TYPE_WORLD_MODEL);
								}
							});
						}
						{
							jLabel1 = new JLabel();
							BallPanel.add(jLabel1);
							jLabel1.setText("Origin Player");
							jLabel1.setBounds(6, 25, 80, 16);
						}
						{
							jLabel2 = new JLabel();
							BallPanel.add(jLabel2);
							jLabel2.setText("Axis X");
							jLabel2.setBounds(10, 90, 50, 20);
						}
						{
							jLabel3 = new JLabel();
							BallPanel.add(jLabel3);
							jLabel3.setText("Axis Y");
							jLabel3.setBounds(10, 130, 50, 20);
						}
						{
							jLabel4 = new JLabel();
							BallPanel.add(jLabel4);
							jLabel4.setText("Axis Z");
							jLabel4.setBounds(10, 170, 50, 20);
						}
						{
							jLabel5 = new JLabel();
							BallPanel.add(jLabel5);
							jLabel5.setText("Speed");
							jLabel5.setBounds(10, 210, 50, 20);
						}
						{
							jLabel6 = new JLabel();
							BallPanel.add(jLabel6);
							jLabel6.setText("Angle");
							jLabel6.setBounds(10, 250, 50, 20);
						}
						{
							fromServerPanel = new JPanel();
							BallPanel.add(fromServerPanel);
							fromServerPanel.setBounds(91, 54, 110, 234);
							fromServerPanel.setBorder(BorderFactory.createTitledBorder(null, "From Server", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
							fromServerPanel.setOpaque(false);
							fromServerPanel.setAlignmentX(5.0f);
							{
								axisXTextField = new JTextField();
								fromServerPanel.add(axisXTextField);
								axisXTextField.setText("0");
								axisXTextField.setBounds(68, 69, 59, 28);
								axisXTextField.setPreferredSize(new java.awt.Dimension(70, 35));
								axisXTextField.setEditable(false);
								axisXTextField.setSize(60, 30);
								axisXTextField.setMinimumSize(new java.awt.Dimension(20, 28));
							}
							{
								axisYTextField = new JTextField();
								fromServerPanel.add(axisYTextField);
								axisYTextField.setText("0");
								axisYTextField.setBounds(68, 109, 22, 28);
								axisYTextField.setPreferredSize(new java.awt.Dimension(70, 35));
								axisYTextField.setEditable(false);
							}
							{
								axisZTextField = new JTextField();
								fromServerPanel.add(axisZTextField);
								axisZTextField.setText("0");
								axisZTextField.setBounds(68, 145, 22, 28);
								axisZTextField.setPreferredSize(new java.awt.Dimension(70, 35));
								axisZTextField.setEditable(false);
							}
							{
								speedTextField = new JTextField();
								fromServerPanel.add(speedTextField);
								speedTextField.setText("0");
								speedTextField.setBounds(71, 180, 84, 28);
								speedTextField.setPreferredSize(new java.awt.Dimension(70, 35));
								speedTextField.setEditable(false);
							}
							{
								angleTextField = new JTextField();
								fromServerPanel.add(angleTextField);
								angleTextField.setText("0");
								angleTextField.setBounds(71, 208, 58, 28);
								angleTextField.setPreferredSize(new java.awt.Dimension(70, 35));
								angleTextField.setEditable(false);
							}
						}
						{
							fromPlayerPanel = new JPanel();
							BallPanel.add(fromPlayerPanel);
							fromPlayerPanel.setBounds(220, 54, 110, 234);
							fromPlayerPanel.setBorder(BorderFactory.createTitledBorder(null, "From Player", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
							fromPlayerPanel.setOpaque(false);
							{
								axisXTextField2 = new JTextField();
								fromPlayerPanel.add(axisXTextField2);
								axisXTextField2.setText("0");
								axisXTextField2.setBounds(68, 69, 59, 28);
								axisXTextField2.setPreferredSize(new java.awt.Dimension(70, 35));
								axisXTextField2.setEditable(false);
							}
							{
								axisYTextField2 = new JTextField();
								fromPlayerPanel.add(axisYTextField2);
								axisYTextField2.setText("0");
								axisYTextField2.setBounds(68, 109, 22, 28);
								axisYTextField2.setPreferredSize(new java.awt.Dimension(70, 35));
								axisYTextField2.setEditable(false);
							}
							{
								axisZTextField2 = new JTextField();
								fromPlayerPanel.add(axisZTextField2);
								axisZTextField2.setText("0");
								axisZTextField2.setBounds(68, 145, 22, 28);
								axisZTextField2.setPreferredSize(new java.awt.Dimension(70, 35));
								axisZTextField2.setEditable(false);
							}
							{
								speedTextField2 = new JTextField();
								fromPlayerPanel.add(speedTextField2);
								speedTextField2.setText("0");
								speedTextField2.setBounds(71, 180, 84, 28);
								speedTextField2.setPreferredSize(new java.awt.Dimension(70, 35));
								speedTextField2.setEditable(false);
							}
							{
								angleTextField2 = new JTextField();
								fromPlayerPanel.add(angleTextField2);
								angleTextField2.setText("0");
								angleTextField2.setBounds(71, 208, 58, 28);
								angleTextField2.setPreferredSize(new java.awt.Dimension(70, 35));
								angleTextField2.setEditable(false);
							}
							{
								differencesPanel = new JPanel();
								BallPanel.add(differencesPanel);
								differencesPanel.setBounds(350, 54, 110, 234);
								differencesPanel.setBorder(BorderFactory.createTitledBorder(null, "Differences", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
								differencesPanel.setOpaque(false);
								{
									axisXTextField3 = new JTextField();
									differencesPanel.add(axisXTextField3);
									axisXTextField3.setText("0");
									axisXTextField3.setBounds(68, 69, 59, 28);
									axisXTextField3.setPreferredSize(new java.awt.Dimension(70, 35));
									axisXTextField3.setEditable(false);
								}
								{
									axisYTextField3 = new JTextField();
									differencesPanel.add(axisYTextField3);
									axisYTextField3.setText("0");
									axisYTextField3.setBounds(68, 109, 22, 28);
									axisYTextField3.setPreferredSize(new java.awt.Dimension(70, 35));
									axisYTextField3.setEditable(false);
								}
								{
									axisZTextField3 = new JTextField();
									differencesPanel.add(axisZTextField3);
									axisZTextField3.setText("0");
									axisZTextField3.setBounds(68, 145, 22, 28);
									axisZTextField3.setPreferredSize(new java.awt.Dimension(70, 35));
									axisZTextField3.setEditable(false);
								}
								{
									speedTextField3 = new JTextField();
									differencesPanel.add(speedTextField3);
									speedTextField3.setText("0");
									speedTextField3.setBounds(71, 180, 84, 28);
									speedTextField3.setPreferredSize(new java.awt.Dimension(70, 35));
									speedTextField3.setEditable(false);
								}
								{
									angleTextField3 = new JTextField();
									differencesPanel.add(angleTextField3);
									angleTextField3.setText("0");
									angleTextField3.setBounds(71, 208, 58, 28);
									angleTextField3.setPreferredSize(new java.awt.Dimension(70, 35));
									angleTextField3.setEditable(false);
								}
							}
						

						}
					}
					//{
						playersPanel = new JPanel();
						TabbedPane.addTab("Players", null, playersPanel, null);
						playersPanel.setLayout(null);
						playersPanel.setPreferredSize(new java.awt.Dimension(476, 235));
						{
							jLabel11 = new JLabel();
							playersPanel.add(jLabel11);
							jLabel11.setText("Origin Player");
							jLabel11.setBounds(6, 20, 80, 16);
						}
						{
							jLabel111 = new JLabel();
							playersPanel.add(jLabel111);
							jLabel111.setText("Monitoring Player");
							jLabel111.setBounds(220, 20, 120, 16);
						}
						{
							jLabel21 = new JLabel();
							playersPanel.add(jLabel21);
							jLabel21.setText("Axis X");
							jLabel21.setBounds(10, 70, 80, 20);
						}
						{
							jLabel31 = new JLabel();
							playersPanel.add(jLabel31);
							jLabel31.setText("Axis Y");
							jLabel31.setBounds(10, 100, 80, 20);
						}
						{
							jLabel41 = new JLabel();
							playersPanel.add(jLabel41);
							jLabel41.setText("Axis Z");
							jLabel41.setBounds(10, 130, 80, 20);
						}
						{
							jLabel51 = new JLabel();
							playersPanel.add(jLabel51);
							jLabel51.setText("Rotation X");
							jLabel51.setBounds(10, 160, 80, 20);
						}
						{
							jLabel61 = new JLabel();
							playersPanel.add(jLabel61);
							jLabel61.setText("Rotation Y");
							jLabel61.setBounds(10, 190, 80, 20);
						}
						{
							jLabel71 = new JLabel();
							playersPanel.add(jLabel71);
							jLabel71.setText("Rotation Z");
							jLabel71.setBounds(10, 220, 80, 20);
						}
						{
							jLabel81 = new JLabel();
							playersPanel.add(jLabel81);
							jLabel81.setText("Velocity");
							jLabel81.setBounds(10, 250, 80, 20);
						}
						{
							fromServerPanel1 = new JPanel();
							playersPanel.add(fromServerPanel1);
							fromServerPanel1.setBounds(90, 40, 110, 250);
							fromServerPanel1.setBorder(BorderFactory.createTitledBorder(null, "From Server", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
							fromServerPanel1.setOpaque(false);
							fromServerPanel1.setAlignmentX(5.0f);
							{
								axisXTextField1 = new JTextField();
								fromServerPanel1.add(axisXTextField1);
								axisXTextField1.setText("0");
								axisXTextField1.setBounds(65, 65, 60, 25);
								axisXTextField1.setPreferredSize(new java.awt.Dimension(70, 25));
								axisXTextField1.setEditable(false);
								axisXTextField1.setSize(60, 30);
								axisXTextField1.setMinimumSize(new java.awt.Dimension(20, 28));
							}
							{
								axisYTextField1 = new JTextField();
								fromServerPanel1.add(axisYTextField1);
								axisYTextField1.setText("0");
								axisYTextField1.setBounds(68, 109, 22, 28);
								axisYTextField1.setPreferredSize(new java.awt.Dimension(70, 25));
								axisYTextField1.setEditable(false);
							}
							{
								axisZTextField1 = new JTextField();
								fromServerPanel1.add(axisZTextField1);
								axisZTextField1.setText("0");
								axisZTextField1.setBounds(68, 145, 22, 28);
								axisZTextField1.setPreferredSize(new java.awt.Dimension(70, 25));
								axisZTextField1.setEditable(false);
							}
							{
								rotationXTextField1 = new JTextField();
								fromServerPanel1.add(rotationXTextField1);
								rotationXTextField1.setText("0");
								rotationXTextField1.setBounds(68, 69, 59, 28);
								rotationXTextField1.setPreferredSize(new java.awt.Dimension(70, 25));
								rotationXTextField1.setEditable(false);
								rotationXTextField1.setSize(60, 30);
								rotationXTextField1.setMinimumSize(new java.awt.Dimension(20, 28));
							}
							{
								rotationYTextField1 = new JTextField();
								fromServerPanel1.add(rotationYTextField1);
								rotationYTextField1.setText("0");
								rotationYTextField1.setBounds(68, 109, 22, 28);
								rotationYTextField1.setPreferredSize(new java.awt.Dimension(70, 25));
								rotationYTextField1.setEditable(false);
							}
							{
								rotationZTextField1 = new JTextField();
								fromServerPanel1.add(rotationZTextField1);
								rotationZTextField1.setText("0");
								rotationZTextField1.setBounds(68, 145, 22, 28);
								rotationZTextField1.setPreferredSize(new java.awt.Dimension(70, 25));
								rotationZTextField1.setEditable(false);
							}
							{
								velocityTextField1 = new JTextField();
								fromServerPanel1.add(velocityTextField1);
								velocityTextField1.setText("0");
								velocityTextField1.setBounds(68, 145, 22, 28);
								velocityTextField1.setPreferredSize(new java.awt.Dimension(70, 25));
								velocityTextField1.setEditable(false);
							}

						}
						{
							fromPlayerPanel1 = new JPanel();
							playersPanel.add(fromPlayerPanel1);
							fromPlayerPanel1.setBounds(220, 40, 110, 250);
							fromPlayerPanel1.setBorder(BorderFactory.createTitledBorder(null, "From Player", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
							fromPlayerPanel1.setOpaque(false);
							fromPlayerPanel1.setAlignmentX(5.0f);
							{
								axisXTextField21 = new JTextField();
								fromPlayerPanel1.add(axisXTextField21);
								axisXTextField21.setText("0");
								axisXTextField21.setBounds(65, 65, 60, 25);
								axisXTextField21.setPreferredSize(new java.awt.Dimension(70, 25));
								axisXTextField21.setEditable(false);
								axisXTextField21.setSize(60, 30);
								axisXTextField21.setMinimumSize(new java.awt.Dimension(20, 28));
							}
							{
								axisYTextField21 = new JTextField();
								fromPlayerPanel1.add(axisYTextField21);
								axisYTextField21.setText("0");
								axisYTextField21.setBounds(68, 109, 22, 28);
								axisYTextField21.setPreferredSize(new java.awt.Dimension(70, 25));
								axisYTextField21.setEditable(false);
							}
							{
								axisZTextField21 = new JTextField();
								fromPlayerPanel1.add(axisZTextField21);
								axisZTextField21.setText("0");
								axisZTextField21.setBounds(68, 145, 22, 28);
								axisZTextField21.setPreferredSize(new java.awt.Dimension(70, 25));
								axisZTextField21.setEditable(false);
							}
							{
								rotationXTextField21 = new JTextField();
								fromPlayerPanel1.add(rotationXTextField21);
								rotationXTextField21.setText("0");
								rotationXTextField21.setBounds(68, 69, 59, 28);
								rotationXTextField21.setPreferredSize(new java.awt.Dimension(70, 25));
								rotationXTextField21.setEditable(false);
								rotationXTextField21.setSize(60, 30);
								rotationXTextField21.setMinimumSize(new java.awt.Dimension(20, 28));
							}
							{
								rotationYTextField21 = new JTextField();
								fromPlayerPanel1.add(rotationYTextField21);
								rotationYTextField21.setText("0");
								rotationYTextField21.setBounds(68, 109, 22, 28);
								rotationYTextField21.setPreferredSize(new java.awt.Dimension(70, 25));
								rotationYTextField21.setEditable(false);
							}
							{
								rotationZTextField21 = new JTextField();
								fromPlayerPanel1.add(rotationZTextField21);
								rotationZTextField21.setText("0");
								rotationZTextField21.setBounds(68, 145, 22, 28);
								rotationZTextField21.setPreferredSize(new java.awt.Dimension(70, 25));
								rotationZTextField21.setEditable(false);
							}
							{
								velocityTextField21 = new JTextField();
								fromPlayerPanel1.add(velocityTextField21);
								velocityTextField21.setText("0");
								velocityTextField21.setBounds(68, 145, 22, 28);
								velocityTextField21.setPreferredSize(new java.awt.Dimension(70, 25));
								velocityTextField21.setEditable(false);
							}

						}
						{
							differencesPanel1 = new JPanel();
							playersPanel.add(differencesPanel1);
							differencesPanel1.setBounds(350, 40, 110, 250);
							differencesPanel1.setBorder(BorderFactory.createTitledBorder(null, "Differences", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
							differencesPanel1.setOpaque(false);
							differencesPanel1.setAlignmentX(5.0f);
							{
								axisXTextField31 = new JTextField();
								differencesPanel1.add(axisXTextField31);
								axisXTextField31.setText("0");
								axisXTextField31.setBounds(65, 65, 60, 25);
								axisXTextField31.setPreferredSize(new java.awt.Dimension(70, 25));
								axisXTextField31.setEditable(false);
								axisXTextField31.setSize(60, 30);
								axisXTextField31.setMinimumSize(new java.awt.Dimension(20, 28));
							}
							{
								axisYTextField31 = new JTextField();
								differencesPanel1.add(axisYTextField31);
								axisYTextField31.setText("0");
								axisYTextField31.setBounds(68, 109, 22, 28);
								axisYTextField31.setPreferredSize(new java.awt.Dimension(70, 25));
								axisYTextField31.setEditable(false);
							}
							{
								axisZTextField31 = new JTextField();
								differencesPanel1.add(axisZTextField31);
								axisZTextField31.setText("0");
								axisZTextField31.setBounds(68, 145, 22, 28);
								axisZTextField31.setPreferredSize(new java.awt.Dimension(70, 25));
								axisZTextField31.setEditable(false);
							}
							{
								rotationXTextField31 = new JTextField();
								differencesPanel1.add(rotationXTextField31);
								rotationXTextField31.setText("0");
								rotationXTextField31.setBounds(68, 69, 59, 28);
								rotationXTextField31.setPreferredSize(new java.awt.Dimension(70, 25));
								rotationXTextField31.setEditable(false);
								rotationXTextField31.setSize(60, 30);
								rotationXTextField31.setMinimumSize(new java.awt.Dimension(20, 28));
							}
							{
								rotationYTextField31 = new JTextField();
								differencesPanel1.add(rotationYTextField31);
								rotationYTextField31.setText("0");
								rotationYTextField31.setBounds(68, 109, 22, 28);
								rotationYTextField31.setPreferredSize(new java.awt.Dimension(70, 25));
								rotationYTextField31.setEditable(false);
							}
							{
								rotationZTextField31 = new JTextField();
								differencesPanel1.add(rotationZTextField31);
								rotationZTextField31.setText("0");
								rotationZTextField31.setBounds(68, 145, 22, 28);
								rotationZTextField31.setPreferredSize(new java.awt.Dimension(70, 25));
								rotationZTextField31.setEditable(false);
							}
							{
								velocityTextField31 = new JTextField();
								differencesPanel1.add(velocityTextField31);
								velocityTextField31.setText("0");
								velocityTextField31.setBounds(68, 145, 22, 28);
								velocityTextField31.setPreferredSize(new java.awt.Dimension(70, 25));
								velocityTextField31.setEditable(false);
							}

						}
						{
							ComboBoxModel playerView2ComboBoxModel = 
									new DefaultComboBoxModel(
											new String[] { "Player 1", "Player 2" });
							playerView2ComboBox = new JComboBox();
							playersPanel.add(playerView2ComboBox);
							playerView2ComboBox.setModel(playerView2ComboBoxModel);
							playerView2ComboBox.setBounds(90, 15, 110, 25);
							playerView2ComboBox.addItemListener(new ItemListener() {
								@Override
								public void itemStateChanged(ItemEvent arg0) {
									AgentMonitor.removeMessageListener(ComparingTab.this);
									AgentMonitor.setMessageListener(1, "ANDROIDS", ComparingTab.this, AgentMonitorMessage.TYPE_WORLD_MODEL);
								}
							});
						}
						{
							ComboBoxModel playerMonitoringComboBoxModel = 
									new DefaultComboBoxModel(
											new String[] { "Player 1", "Player 2" });
							playerMonitoringComboBox = new JComboBox();
							playersPanel.add(playerMonitoringComboBox);
							playerMonitoringComboBox.setModel(playerMonitoringComboBoxModel);
							playerMonitoringComboBox.setBounds(350, 15, 110, 25);
							playerMonitoringComboBox.addItemListener(new ItemListener() {
								@Override
								public void itemStateChanged(ItemEvent arg0) {
									AgentMonitor.removeMessageListener(ComparingTab.this);
									AgentMonitor.setMessageListener(1, "ANDROIDS", ComparingTab.this, AgentMonitorMessage.TYPE_WORLD_MODEL);
								}
							});
						}
					
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void SetServerInput(Vector3 position, Vector3 direction) 
	{
		axisXTextField.setText(position.getX() + "");
		axisYTextField.setText(position.getY() + "");
		axisXTextField.setText(position.getZ() + "");
//		angleTextField.setText(direction.getTheta() + "");
//		speedTextField.setText(direction.getR() + "");
		CalculateDiff();
	}

	public void SetPlayerInput(Vector3D position, Vector3D direction) 
	{
		axisXTextField2.setText(position.getX() + "");
		axisYTextField2.setText(position.getY() + "");
		axisXTextField2.setText(position.getZ() + "");
		angleTextField2.setText(direction.getTheta() + "");
		speedTextField2.setText(direction.getR() + "");
		CalculateDiff();
	}
	
	public void SetPlayerNames(String[] names) {
		
	}
	
	public int GetSelectedPlayerNameIndex() {
		return playerViewComboBox.getSelectedIndex();
	}
	
	public String GetSelelectedPlayerName() {
		return playerViewComboBox.getSelectedItem().toString();
	}
	
	private void CalculateDiff() {
		//calculate differences
		double diffX = Double.parseDouble(axisXTextField.getText()) - Double.parseDouble(axisXTextField2.getText());
		double diffY = Double.parseDouble(axisYTextField.getText()) - Double.parseDouble(axisYTextField2.getText());
		double diffZ = Double.parseDouble(axisZTextField.getText()) - Double.parseDouble(axisZTextField2.getText());
		double angle = Double.parseDouble(angleTextField.getText()) - Double.parseDouble(angleTextField2.getText());
		double speed = Double.parseDouble(speedTextField.getText()) - Double.parseDouble(speedTextField2.getText());
		
		
		//print the differences
		axisXTextField3.setText(diffX + "");
		axisYTextField3.setText(diffY + "");
		axisZTextField3.setText(diffZ + "");
		angleTextField3.setText(angle + "");
		speedTextField3.setText(speed + "");
	}
	
	/* (non-Javadoc)
	 * @see sk.fiit.testframework.monitor.IAgentMonitorListener#receivedMessage(int, java.lang.String, sk.fiit.testframework.monitor.AgentMonitorMessage)
	 * poslany model sveta od hraca (vykonat update UI)
	 */
	@Override
	public void receivedMessage(int uniform, String team, AgentMonitorMessage message) {
		AgentMonitorMessage.WorldModel modelMessage = (sk.fiit.testframework.monitor.AgentMonitorMessage.WorldModel) message;
		sk.fiit.jim.agent.models.WorldModel model = modelMessage.model;
		
		Vector3D position = model.getBall().getPosition();
		SetPlayerInput(position, position);
		
		SimulationState state = RobocupMonitor.getMonitorInstance().getSimulationState();
		Vector3 serverBallInfo = state.getScene().getBallLocation();
		SetServerInput(serverBallInfo, serverBallInfo);
		
	}
	
}
