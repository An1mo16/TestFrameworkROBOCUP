package sk.fiit.testframework.beta.ui;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import sk.fiit.robocup.library.geometry.Circle;
import sk.fiit.robocup.library.geometry.Point3D;
import sk.fiit.robocup.library.geometry.Vector2;
import sk.fiit.testframework.communication.agent.AgentData;
import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.AgentJim.TeamSide;
import sk.fiit.testframework.communication.robocupserver.RobocupServer;
import sk.fiit.testframework.communication.robocupserver.RobocupServerAddress;
import sk.fiit.testframework.init.Implementation;
import sk.fiit.testframework.init.ImplementationFactory;
import sk.fiit.testframework.monitor.AgentMonitorMessage;
import sk.fiit.testframework.monitor.IAgentMonitorListener;
import sk.fiit.testframework.monitor.RobocupMonitor;
import sk.fiit.testframework.trainer.testsuite.TestCase;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;
import sk.fiit.testframework.trainer.testsuite.testcases.WalkTestCase;
import sk.fiit.testframework.ui.MeasurableInformation;
import sk.fiit.testframework.ui.MonitorGUI;
import sk.fiit.testframework.ui.TestCaseList;
import sk.fiit.testframework.ui.UserInterface;
import sk.fiit.testframework.ui.TestCaseList.TestHolder;
import sk.fiit.testframework.worldrepresentation.ISimulationStateObserver;
import sk.fiit.testframework.worldrepresentation.models.SimulationState;
import sk.fiit.testframework.annotator.*;

import java.util.*;


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
public class MainFrame extends javax.swing.JFrame implements ISimulationStateObserver, UserInterface  {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5281830264128806859L;
	private JTabbedPane jTabbedPane1;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private JButton startButton;
	private JTabbedPane agentTabbedPane;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JRadioButton logRadioButton3;
	private JRadioButton logRadioButton2;
	private JRadioButton logRadioButton1;
	private ButtonGroup logLevelGroup;
	private JPanel logLevelPanel;
	private JTextArea logTextArea;
	private JRadioButton logRadioButton7;
	private JButton startTestButton;
	private JComboBox testCaseComboBox;
	private JLabel jLabel3;
	private JLabel ipLabel;
	private JButton annotateButton;
	private JLabel jLabel8;
	private JLabel jLabel7;
	private JTextField ballYTextField;
	private JTextField ballXTextField;
	private JRadioButton logRadioButton6;
	private JRadioButton logRadioButton5;
	private JRadioButton logRadioButton4;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane1;
	private JTextArea simulationTextArea;
	private JPanel infoPanel;
	private JTextField ballRadiusTextField;
	private JTextField loopField;
	private JTextField sourceField;
	private JTextField MovementField;
	private JLabel jLabel6;
	private JPanel ballPanel;
	private JLabel jLabel5;
	private JTextField ipField;
	private JPanel settingsPanel;
	private JButton connectButton;
	private JButton stopButton;
	private JPanel controlPanel;
	private JTextField agentNumberField;
	private JTextField ballPositionField;
	private JButton ballPositionButton;
	private JButton agentNumberButton;
	
	//NON gui members
	private RobocupMonitor monitor;
	private RobocupServer trainer = null;
    private RobocupServerAddress serverAddress;
    private MeasurableInformation atStart;
    private boolean firstUpdate = true;
    private boolean isStopped = false;
    private Logger masterLogger;
    private TestCaseResult annotationResult;


    
	/*public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame inst = new MainFrame(null);
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}*/
	
	public MainFrame(RobocupServerAddress robocupAddress) {
		super();
		this.annotationResult = new TestCaseResult();
    	this.serverAddress = robocupAddress;
    	try {
    	monitor = RobocupMonitor.getMonitorInstance(serverAddress);
		trainer = RobocupServer.getServerInstance(serverAddress);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		initGUI();
		InitLogger();
	}
	
    public void registerMonitor() {
		monitor.getSimulationState().registerObserver(this);
    }
	
    /**
     * 
     * TODO: Replace with a brief purpose of method.
     *
     */
    private void InitLogger() {
    	masterLogger = Logger.getLogger("");
    	masterLogger.addHandler(new LogHandler(logTextArea));
    }
    
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				//TODO wrap with panel to limit size
				setLayout(null);
				jTabbedPane1 = new JTabbedPane();
				getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
				getContentPane().add(getJScrollPane2());
				jTabbedPane1.setSize(600, 400);
				jTabbedPane1.setBorder(BorderFactory.createTitledBorder(""));
				{
					jPanel1 = new JPanel();
					jTabbedPane1.addTab("Main", null, jPanel1, null);
				}
				{
					jPanel2 = new JPanel();
					jTabbedPane1.addTab("Annotation", null, jPanel2, null);
					jPanel2.setLayout(null);
					jPanel2.setPreferredSize(new java.awt.Dimension(400, 170));
				}
				{
					jPanel3 = new ComparingTab();
					jTabbedPane1.addTab("Comparing", null, jPanel3, null);
				}
			}
			{
				logLevelPanel = new JPanel();
				getContentPane().add(logLevelPanel);
				logLevelPanel.setLayout(null);
				logLevelPanel.setBounds(443, 406, 151, 266);
				logLevelPanel.setBorder(BorderFactory.createTitledBorder("Log level"));
				logLevelGroup = getLogLevelGroup();
				{
					logRadioButton1 = new JRadioButton();
					logLevelPanel.add(logRadioButton1);
					logRadioButton1.setBounds(15, 30, 125, 25);
					logRadioButton1.setText("Finest");
					logRadioButton1.addActionListener(new LogLevelWatcher());
					logRadioButton1.setName("logRadioButton1");
					logLevelGroup.add(logRadioButton1);
				}
				{
					logRadioButton2 = new JRadioButton();
					logLevelPanel.add(logRadioButton2);
					logLevelPanel.add(getLogRadioButton3());
					logLevelPanel.add(getLogRadioButton4());
					logLevelPanel.add(getLogRadioButton5());
					logLevelPanel.add(getLogRadioButton6());
					logLevelPanel.add(getLogRadioButton7());
					logRadioButton2.setBounds(15, 60, 125, 25);
					logRadioButton2.setText("Finer");
					logRadioButton2.addActionListener(new LogLevelWatcher());
					logRadioButton2.setName("logRadioButton2");
					logLevelGroup.add(logRadioButton2);
					logLevelGroup.add(getLogRadioButton4());
					logLevelGroup.add(getLogRadioButton5());
					logLevelGroup.add(getLogRadioButton6());
					logLevelGroup.add(getLogRadioButton7());
				}			
				{
					logRadioButton3 = getLogRadioButton3();
					logLevelGroup.add(logRadioButton3);
				}
			}
			{
				logTextArea = new JTextArea();
				getContentPane().add(logTextArea);
				logTextArea.setText("Welcome user");
				logTextArea.setBounds(10, 406, 427, 266);
				logTextArea.setBorder(BorderFactory.createTitledBorder(""));
				logTextArea.setWrapStyleWord(true);
				logTextArea.setLineWrap(true);
			}
			fillControlTab();
			fillSettingsTab();
					
			pack();
			this.setSize(600, 700);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}

	/**
	 * TODO: Replace with a brief purpose of method.
	 * 
	 */
	private void fillControlTab() {
		
		controlPanel = new JPanel();
		controlPanel.setLayout(null);
		controlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Controller"));
		controlPanel.setBounds(283, 77, 278, 250);
		jPanel1.add(controlPanel);
		{
			settingsPanel = new JPanel();
			jPanel1.add(settingsPanel);
			settingsPanel.setLayout(null);
			settingsPanel.setBounds(283, 6, 278, 65);
			settingsPanel.setBorder(BorderFactory.createTitledBorder("Settings"));
			{
				ipField = new JTextField();
				settingsPanel.add(ipField);
				settingsPanel.add(getIpLabel());
				ipField.setText("196.168.56.101");
				ipField.setBounds(37, 28, 229, 22);
			}
		}
		{
			agentTabbedPane = new JTabbedPane();
			jPanel1.add(agentTabbedPane);
			agentTabbedPane.setBounds(6, 6, 277, 330);
			agentTabbedPane.addTab("Jim info", null, getInfoPanel(), null);
		}
		jPanel1.setLayout(null);
		startButton = new JButton();
		startButton.setBounds(170, 20, 100, 25);
		startButton.setText("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        firstUpdate = true;
		        isStopped = false;
		        stopButton.requestFocusInWindow();

		        //Implementation impl = ImplementationFactory.getImplementationInstance();
				//impl.enqueueTestCase(testCase, null);
		        Thread testThread = new Thread(TestCaseList.testCaseList.get(
						testCaseComboBox.getSelectedIndex()
						).runnable);
		        testThread.start();
				
				TestCaseList.isRunning = testThread;
			}
		});
		controlPanel.add(startButton);
		
		connectButton = new JButton();
		connectButton.setBounds(170, 50, 100, 25);
		connectButton.setText("Connect");
		controlPanel.add(connectButton);
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				registerMonitor();
			}
		});

		stopButton = new JButton();
		stopButton.setBounds(170, 80, 100, 25);
		stopButton.setText("Stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        startButton.requestFocusInWindow();
		        TestCaseList.isRunning.stop();
		        isStopped = true;
			}
		});
		controlPanel.add(stopButton);

		ballPositionField = new JTextField();
		ballPositionField.setText("0.0.0.4");
		ballPositionField.setBounds(12, 166, 132, 24);
		controlPanel.add(ballPositionField);
		
		agentNumberField = new JTextField();
		agentNumberField.setBounds(12, 126, 33, 24);
		agentNumberField.setText("1");
		controlPanel.add(agentNumberField);
		
		ballPositionButton = new JButton();
		ballPositionButton.setBounds(12, 196, 132, 24);
		ballPositionButton.setText("Set Ball");
		controlPanel.add(ballPositionButton);
		ballPositionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        try {
		            trainer.setBallPosition(getPoint3D(ballPositionField.getText()));
		        } catch (IOException ex) {
		            Logger.getLogger(MonitorGUI.class.getName()).log(Level.SEVERE, null, ex);
		        }
			}
		});

		agentNumberButton = new JButton();
		agentNumberButton.setBounds(45, 127, 99, 24);
		agentNumberButton.setText("Add Agent");
		controlPanel.add(agentNumberButton);
		controlPanel.add(getJLabel3());
		controlPanel.add(getTestTypeComboBox());
		controlPanel.add(getStartTestButton());
		agentNumberButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AgentJim aj = null;
		        
		        aj = new AgentJim(new AgentData(1, TeamSide.LEFT, "JimTeam"), "localhost", 3070);
		        //TODO JimTeam from some variable
		        try {
		            trainer.setAgentPosition(aj.getAgentData(), getPoint3D(agentNumberField.getText()));
		        } catch (Exception ex) {
		            Logger.getLogger(MonitorGUI.class.getName()).log(Level.SEVERE, null, ex);
		        }
			}
		});
	}
	
	private void fillTestCaseComboBox() {
		testCaseComboBox.removeAllItems();
		for(TestHolder th : TestCaseList.testCaseList) {
			testCaseComboBox.addItem(th.name);
		}
	}
	
	private void fillSettingsTab() {
		jPanel2.setPreferredSize(new java.awt.Dimension(501, 170));
		jPanel2.add(getJLabel1());
		jPanel2.add(getJLabel2());
		jPanel2.add(getJLabel5());
		jPanel2.add(getBallPanel());
		jPanel2.add(getMovementField());
		jPanel2.add(getSourceField());
		jPanel2.add(getLoopField());
		jPanel2.add(getAnnotateButton());
	}
	
	private ButtonGroup getLogLevelGroup() {
		if(logLevelGroup == null) {
			logLevelGroup = new ButtonGroup();
		}
		return logLevelGroup;
	}
	
	private JRadioButton getLogRadioButton3() {
		if(logRadioButton3 == null) {
			logRadioButton3 = new JRadioButton();
			logRadioButton3.setName("logRadioButton3");
			logRadioButton3.setText("Fine");
			logRadioButton3.setBounds(15, 90, 125, 25);
			logRadioButton3.addActionListener(new LogLevelWatcher());
		}
		return logRadioButton3;
	}
	
	private JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("Movement XML Source");
			jLabel1.setBounds(20, 22, 194, 24);
		}
		return jLabel1;
	}
	
	private JLabel getJLabel2() {
		if(jLabel2 == null) {
			jLabel2 = new JLabel();
			jLabel2.setText("XML Annotation Destination folder");
			jLabel2.setBounds(20, 48, 224, 15);
		}
		return jLabel2;
	}

	private JLabel getJLabel5() {
		if(jLabel5 == null) {
			jLabel5 = new JLabel();
			jLabel5.setText("Number of Loops");
			jLabel5.setBounds(20, 128, 131, 15);
		}
		return jLabel5;
	}
	
	private JPanel getBallPanel() {
		if(ballPanel == null) {
			ballPanel = new JPanel();
			ballPanel.setBounds(20, 167, 237, 135);
			ballPanel.setLayout(null);
			ballPanel.setBorder(BorderFactory.createTitledBorder("Ball"));
			ballPanel.add(getJLabel6());
			ballPanel.add(getCenterField());
			ballPanel.add(getBallXTextField());
			ballPanel.add(getBallYTextField());
			ballPanel.add(getJLabel7());
			ballPanel.add(getJLabel8());
		}
		return ballPanel;
	}
	
	private JLabel getJLabel6() {
		if(jLabel6 == null) {
			jLabel6 = new JLabel();
			jLabel6.setText("Circle Radius");
			jLabel6.setBounds(12, 103, 116, 15);
		}
		return jLabel6;
	}

	private JTextField getMovementField() {
		if(MovementField == null) {
			MovementField = new JTextField();
			MovementField.setText("hello");
			MovementField.setBounds(243, 23, 172, 22);
		}
		return MovementField;
	}
	
	private JTextField getSourceField() {
		if(sourceField == null) {
			sourceField = new JTextField();
			sourceField.setText("Aloha");
			sourceField.setBounds(243, 44, 172, 22);
		}
		return sourceField;
	}

	private JTextField getLoopField() {
		if(loopField == null) {
			loopField = new JTextField();
			loopField.setText("412");
			loopField.setBounds(243, 124, 86, 22);
		}
		return loopField;
	}
	
	private JTextField getCenterField() {
		if(ballRadiusTextField == null) {
			ballRadiusTextField = new JTextField();
			ballRadiusTextField.setText("Velky");
			ballRadiusTextField.setBounds(140, 99, 85, 22);
		}
		return ballRadiusTextField;
	}

	/* (non-Javadoc)
	 * @see sk.fiit.testframework.worldrepresentation.ISimulationStateObserver#update()
	 */
	@Override
	public void update() {
		if (isStopped) {
            return;
        }

        if (firstUpdate) {
            atStart = new MeasurableInformation(monitor.getSimulationState());
            firstUpdate = false;
        }

        EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SimulationState ss = monitor.getSimulationState();
				MeasurableInformation now = new MeasurableInformation(ss);
		        MeasurableInformation dist = MeasurableInformation.createDistanceInformation(now, atStart);

		        StringBuilder sbInfo = new StringBuilder();	//hungarian notation? wtf
		        sbInfo.append("General\n=======\n");
		        sbInfo.append("Time start: ").append(format(atStart.time)).append("\n");
		        sbInfo.append("Time now: ").append(format(now.time)).append("\n");
		        sbInfo.append("Time passed: ").append(format(dist.time)).append("\n\n");

		        sbInfo.append("Ball\n======\n");
		        sbInfo.append("At start: ").append(atStart.ballPosition.toString()).append("\n");
		        sbInfo.append("Now: ").append(now.ballPosition.toString()).append("\n");
		        sbInfo.append("Dist: ").append(dist.ballPosition.toString()).append("\n");
		        sbInfo.append("2D Dist: ").append(atStart.ballPosition.getXYDistanceFrom(now.ballPosition)).append("\n\n");

		        sbInfo.append("Agent\n======\n");
		        sbInfo.append("At start: ").append(atStart.agentPosition.toString()).append("\n");
		        sbInfo.append("Now: ").append(now.agentPosition.toString()).append("\n");
		        sbInfo.append("Dist: ").append(dist.agentPosition.toString()).append("\n");
		        sbInfo.append("2D Dist: ").append(atStart.agentPosition.getXYDistanceFrom(now.agentPosition)).append("\n");

		        sbInfo.append("Rotation\n======\n");
		        sbInfo.append("At start: ").append(atStart.agentRotation.toString()).append("\n");
		        sbInfo.append("Now: ").append(now.agentRotation.toString()).append("\n");
		        sbInfo.append("Dist: ").append(dist.agentRotation.toString()).append("\n");

		        simulationTextArea.setText(sbInfo.toString());
			}
        });	
	}
	
    private String format(double d) {
        return String.format("%.3f", d);
    }
    
    private Point3D getPoint3D(String text) {
        String[] split = ballPositionField.getText().split(":");
        if (split.length != 3) {
            System.out.println("split fail");
        }
        double x = new Double(split[0]).doubleValue();
        double y = new Double(split[1]).doubleValue();
        double z = new Double(split[2]).doubleValue();
        return new Point3D(x, y, z);
    }

	/* (non-Javadoc)
	 * @see sk.fiit.testframework.trainer.testsuite.ITestCaseObserver#testFinished(sk.fiit.testframework.trainer.testsuite.TestCaseResult)
	 */
	@Override
	public void testFinished(TestCaseResult result) {
	}

	/* (non-Javadoc)
	 * @see sk.fiit.testframework.ui.UserInterface#start()
	 */
	@Override
	public void start() {
		this.setVisible(true);
		
	}

	/* (non-Javadoc)
	 * @see sk.fiit.testframework.ui.UserInterface#shoudExitOnEmptyQueue()
	 */
	@Override
	public boolean shoudExitOnEmptyQueue() {
		return false;
	}
	
	private JPanel getInfoPanel() {
		if(infoPanel == null) {
			infoPanel = new JPanel();
			infoPanel.setPreferredSize(new java.awt.Dimension(193, 268));
			infoPanel.add(getJScrollPane1());
		}
		return infoPanel;
	}
	
	private JTextArea getSimulationTextArea() {
		if(simulationTextArea == null) {
			simulationTextArea = new JTextArea();
			simulationTextArea.setText("Ready for action");
			simulationTextArea.setPreferredSize(new java.awt.Dimension(182, 263));
			simulationTextArea.setEditable(false);
			simulationTextArea.setLineWrap(true);
		}
		return simulationTextArea;
	}
	
	private JScrollPane getJScrollPane1() {
		if(jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setPreferredSize(new java.awt.Dimension(247, 277));
			jScrollPane1.setViewportView(getSimulationTextArea());
		}
		return jScrollPane1;
	}
	
	private JScrollPane getJScrollPane2() {
		if(jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setBounds(8, 406, 432, 266);
		}
		return jScrollPane2;
	}
	
	private JRadioButton getLogRadioButton4() {
		if(logRadioButton4 == null) {
			logRadioButton4 = new JRadioButton();
			logRadioButton4.setText("Config");
			logRadioButton4.setBounds(15, 120, 125, 25);
			logRadioButton4.addActionListener(new LogLevelWatcher());
			logRadioButton4.setName("logRadioButton4");
		}
		return logRadioButton4;
	}
	
	private JRadioButton getLogRadioButton5() {
		if(logRadioButton5 == null) {
			logRadioButton5 = new JRadioButton();
			logRadioButton5.setText("Info");
			logRadioButton5.setBounds(15, 150, 125, 25);
			logRadioButton5.addActionListener(new LogLevelWatcher());
			logRadioButton5.setName("logRadioButton5");
		}
		return logRadioButton5;
	}
	
	private JRadioButton getLogRadioButton6() {
		if(logRadioButton6 == null) {
			logRadioButton6 = new JRadioButton();
			logRadioButton6.setText("Warning");
			logRadioButton6.setBounds(15, 180, 125, 25);
			logRadioButton6.addActionListener(new LogLevelWatcher());
			logRadioButton6.setName("logRadioButton6");
		}
		return logRadioButton6;
	}
	
	private JRadioButton getLogRadioButton7() {
		if(logRadioButton7 == null) {
			logRadioButton7 = new JRadioButton();
			logRadioButton7.setText("Severe");
			logRadioButton7.addActionListener(new LogLevelWatcher());
			logRadioButton7.setBounds(15, 210, 125, 25);
			logRadioButton7.setName("logRadioButton7");
		}
		return logRadioButton7;
	}
	
	private JTextField getBallXTextField() {
		if(ballXTextField == null) {
			ballXTextField = new JTextField();
			ballXTextField.setText("x");
			ballXTextField.setBounds(140, 28, 85, 28);
		}
		return ballXTextField;
	}
	
	private JTextField getBallYTextField() {
		if(ballYTextField == null) {
			ballYTextField = new JTextField();
			ballYTextField.setText("y");
			ballYTextField.setBounds(140, 62, 85, 28);
		}
		return ballYTextField;
	}
	
	private JLabel getJLabel7() {
		if(jLabel7 == null) {
			jLabel7 = new JLabel();
			jLabel7.setText("X Coordinate");
			jLabel7.setBounds(12, 34, 81, 16);
		}
		return jLabel7;
	}
	
	private JLabel getJLabel8() {
		if(jLabel8 == null) {
			jLabel8 = new JLabel();
			jLabel8.setText("Y Coordinate");
			jLabel8.setBounds(12, 68, 81, 16);
		}
		return jLabel8;
	}
	
	private JButton getAnnotateButton() {
		if(annotateButton == null) {
			annotateButton = new JButton();
			annotateButton.setText("Annotate");
			annotateButton.setBounds(365, 293, 101, 29);
			annotateButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					//create instance of annotator
					int loops;
					Circle circle;
					try
					{
						//parse input from user 
						loops = Integer.parseInt(loopField.getText());
						double x = Double.parseDouble(ballXTextField.getText());
						double y = Double.parseDouble(ballYTextField.getText());
						double radius = Double.parseDouble(ballRadiusTextField.getText());
						Vector2 v = new Vector2(x, y);
						circle  = new Circle(v, radius);
					}catch(NumberFormatException ex) {
						//TODO error message
						return;
					}
					Annotator annotator = new Annotator(loops, circle, "Kick_Move", new File(sourceField.getText()));
					
					//and try to annotate
					annotator.annotate();
				}
			});
		}
		return annotateButton;
	}
	
	private JLabel getIpLabel() {
		if(ipLabel == null) {
			ipLabel = new JLabel();
			ipLabel.setText("IP:");
			ipLabel.setBounds(13, 31, 24, 17);
		}
		return ipLabel;
	}
	
	private JLabel getJLabel3() {
		if(jLabel3 == null) {
			jLabel3 = new JLabel();
			jLabel3.setText("Test Selection");
			jLabel3.setBounds(12, 23, 88, 16);
		}
		return jLabel3;
	}
	
	private JComboBox getTestTypeComboBox() {
		if(testCaseComboBox == null) {
			testCaseComboBox = new JComboBox();
			for(int i = 0; i < TestCaseList.testCaseList.size(); i++) {
				testCaseComboBox.addItem(TestCaseList.testCaseList.get(i).name);
			}
			testCaseComboBox.setBounds(12, 49, 132, 27);
			testCaseComboBox.setEditable(true);
			testCaseComboBox.setVerifyInputWhenFocusTarget(false);
		}
		return testCaseComboBox;
	}
	
	private JButton getStartTestButton() {
		if(startTestButton == null) {
			startTestButton = new JButton();
			startTestButton.setText("Start Test");
			startTestButton.setBounds(12, 82, 132, 29);
			startTestButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					TestCaseList.testCaseList.get(testCaseComboBox.getSelectedIndex()).runnable.run();
				}
			});
		}
		return startTestButton;
	}


	
//root folder annotation
}
