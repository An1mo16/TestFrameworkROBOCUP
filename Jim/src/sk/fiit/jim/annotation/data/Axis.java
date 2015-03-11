package sk.fiit.jim.annotation.data;


public class Axis {
	/**
	 * X-axis values.
	 */
	Values x_axis = new Values();
	/**
	 * Y-axis values.
	 */
	Values y_axis = new Values();
	/**
	 * Z-axis values.
	 */
	Values z_axis = new Values();
	
	public void setX(double min, double max, double avg){
		this.x_axis.setMin(min);
		this.x_axis.setMax(max);
		this.x_axis.setAvg(avg);
	}
	
	public void setY(double min, double max, double avg){
		this.y_axis.setMin(min);
		this.y_axis.setMax(max);
		this.y_axis.setAvg(avg);
	}
	
	public void setZ(double min, double max, double avg){
		this.z_axis.setMin(min);
		this.z_axis.setMax(max);
		this.z_axis.setAvg(avg);
	}
	
	public Values getX(){
		return this.x_axis;
	}
	
	public Values getY(){
		return this.y_axis;
	}
	
	public Values getZ(){
		return this.z_axis;
	}
}
