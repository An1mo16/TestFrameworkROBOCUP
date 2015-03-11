package sk.fiit.jim.annotation.data;

public class Joint {
	/**
	 * Joint name.
	 */
	String name;
	/**
	 * Joint deflection.
	 */
	double value;
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setValue(double value){
		this.value = value;
	}
	
	public String getName(){
		return this.name;
	}
	
	public double getValue(){
		return this.value;
	}
}
