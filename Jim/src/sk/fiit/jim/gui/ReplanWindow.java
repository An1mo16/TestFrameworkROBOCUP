package sk.fiit.jim.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import static sk.fiit.jim.log.LogType.GUI;

import sk.fiit.jim.code_review.Problem;
import sk.fiit.jim.code_review.UnderConstruction;
import sk.fiit.jim.init.ScriptBoot;
import sk.fiit.jim.init.SkillsFromXmlLoader;
import sk.fiit.jim.log.Log;

/**
 *  ReplanWindow.java
 *  
 *@Title	Jim
 *@author	jdrahos
 */
@UnderConstruction(todo="agent's position & rotation, depicted on a graphical canvas.")
@Problem({"No user feedback after pressing a button", "no doc"})
public class ReplanWindow extends JFrame{
	private static final long serialVersionUID = -1864765528411817982L;
	JButton replan = new JButton("Replan");
	JButton reload = new JButton("XML Reload");
	JButton both = new JButton("XML + Scripts reload");
	
	public ReplanWindow() {
		super("JIM control center");
		initGui();
		registerListeners();
	}

	/**
	 * Sets visibility of GUI to true, so the window will be visible. 
	 *
	 */
	public void makeVisible(){
		this.setVisible(true);
	}

	private void initGui(){
		Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 15);
		replan.setFont(defaultFont);
		both.setFont(defaultFont);
		reload.setFont(defaultFont);
		this.setLayout(new BorderLayout());
		this.add(replan, BorderLayout.NORTH);
		this.add(reload, BorderLayout.CENTER);
		this.add(both, BorderLayout.SOUTH);
		this.pack();
		//this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); This close operation just close window, process still run on background.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void registerListeners(){
		replan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				reloadScripts();
			}
		});
		
		reload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				reloadXml(); 
			}
		});
		both.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				reloadXml();
				reloadScripts();
			}
		});
	}
	
	private void reloadScripts(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				Log.log(GUI, "Script reload request from GUI");
				ScriptBoot.boot();
			}
		});
	}

	private void reloadXml(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				try {
					Log.log(GUI, "XML reload request from GUI");
					new SkillsFromXmlLoader(new File("./moves")).load();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(ReplanWindow.this, "Unable to reload XML, consult logs for details");
				}
			}
		});
	}
}