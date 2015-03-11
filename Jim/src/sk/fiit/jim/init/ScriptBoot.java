package sk.fiit.jim.init;

import sk.fiit.robocup.library.init.Script;

/**
 * <p>Used to execute the ruby boot script. It's a script used to reload all other ruby scripts,
 * which will in effect reset all the settings to values from scripts/config/settings.rb
 * and cause a reset of the planner.</p>
 * <p>It's mainly used to apply changes made to ruby scripts without restarting the agent.</p> 
 *
 *@Title	Jim
 *@author	marosurbanec
 */

//TODO since rewriting from Ruby to Java, this is no more needed
public class ScriptBoot{
	/**
	 * 
	 * Executes the boot script, which will reload all other ruby scripts. See class description
	 * for details. 
	 *
	 */
	public static void boot(){
		Script.createScriptFrom("./scripts/boot.rb").execute();
	}
}