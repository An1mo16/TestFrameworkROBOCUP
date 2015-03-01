package sk.fiit.testframework.annotator.serialization;

/** 
 * @author: Roman Bilevic
 */

public class Axis {
	Values x_axis = new Values();
	Values y_axis = new Values();
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

	@Override
	public String toString() {
		return "Axis [x_axis=" + x_axis + ", y_axis=" + y_axis + ", z_axis="
				+ z_axis + "]";
	}
	
}
