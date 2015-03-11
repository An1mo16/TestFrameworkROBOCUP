package sk.fiit.jim.annotation.gui;

import java.io.File;
import javax.swing.filechooser.*;

public class MyFilter extends FileFilter {

	/**
	 * Metoda accept() sluzi na potvrdenie zobrazenie daneho suboru ci adresara
	 */
    public boolean accept(File f) {
    	
    	//adresare sa zobrazuju automaticky
        if (f.isDirectory()) {
            return true;
        }

        //zistime priponu daneho suboru
        String extension = getExtension(f);
        
        //ak ma subor priponu .jpg ci .jpeg, tak povolime jeho zobrazenie
        if (extension != null) {
            if ( extension.equals("xml")) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }
    
    public String getDescription() {
        return "XML";
    }
    
    /**
     * Gets the extension of specified file. 
     *
     * @param f
     * @return
     */
    public String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if ((i > 0) &&  (i < s.length() - 1)) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
