package sk.fiit.jim.annotation.data;

import sk.fiit.jim.annotation.gui.Window;

public class Main {

	public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Window().setVisible(true);
            }
        });
    }
}
