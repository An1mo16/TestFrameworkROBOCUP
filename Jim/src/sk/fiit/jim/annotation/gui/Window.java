package sk.fiit.jim.annotation.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import sk.fiit.jim.annotation.data.*;

@SuppressWarnings("serial")
public class Window extends javax.swing.JFrame {

    public Window() {
        initComponents();
    }

    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        moves = new ArrayList<Annotation>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Search");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter(){
        	public void mouseClicked(final java.awt.event.MouseEvent evt){
        		search();
        	}
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jButton2.setText("Check");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter(){
        	public void mouseClicked(final java.awt.event.MouseEvent evt){
        		check();
        	}
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }
    
 // prehladavanie diskoveho priestoru
    private void search(){
    	final JFileChooser chooser = new JFileChooser();
    	chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.addChoosableFileFilter(new MyFilter());
        chooser.setAcceptAllFileFilterUsed(false);
    	int returnVal = chooser.showOpenDialog(new JFrame());
    	
    	if (returnVal == JFileChooser.APPROVE_OPTION) {
    		Annotation move;
			try {
				move = XMLParser.parse(chooser.getSelectedFile());
				moves.add(move);
//				try {
//					XMLcreator.serialize("pokus", move);
//					XMLparser.check(new File("pokus.xml"));
//				} catch (TransformerException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				jTextArea1.append(moves.size() + ". " + move.getName() + "\n");
			} catch (ParserConfigurationException e) {
				jTextArea2.append(e.getLocalizedMessage() + "\n");
				e.printStackTrace();
			} catch (SAXException e) {
				jTextArea2.append(e.getLocalizedMessage() + "\n");
				e.printStackTrace();
			} catch (IOException e) {
				jTextArea2.append(e.getLocalizedMessage() + "\n");
				e.printStackTrace();
			} 
    	}
    }
    
    // kontrola spravnej nadvaznosti pohybov
    private void check(){
    	MoveValidator mv = new MoveValidator();
    	mv.setMoves(moves);
    	mv.sequenceCheckFull();
    	if(mv.getValidity() == true){
    		jTextArea2.append("Move serialization is OK!\n");
    	}
    	else{
    		jTextArea2.append(mv.getReport());
    	}
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    ArrayList<Annotation> moves;
}
