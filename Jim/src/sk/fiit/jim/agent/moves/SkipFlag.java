package sk.fiit.jim.agent.moves;

/**
 *  SkipFlag.java
 *  
 *  A flag that may be used in moves' XML, denoting skipping a phase
 *  if such a flag has been set to true. Internally, it is nothing more
 *  than a wrapper around a String
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
public class SkipFlag {
	private final String name;

	/**
	 * Creates new SkipFlag with specified name.
	 * 
	 * @param name
	 */
	public SkipFlag(String name){
		this.name = name;
	}
	
	/**
	 * Returns the name of SkipFlag.  
	 *
	 * @return
	 */
	public String getName(){
		return name;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkipFlag other = (SkipFlag) obj;
		if (name == null){
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return "SkipFlag: " + name;
	}	
}