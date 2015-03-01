package sk.fiit.testframework.annotator.serialization;

/** 
 * @author: Roman Bilevic
 */

public class Values {
	double min = 0;
	double max = 0;
	double avg = 0;
	
	public void setMin(double min){
		this.min = min;
	}
	
	public void setMax(double max){
		this.max = max;
	}
	
	public void setAvg(double avg){
		this.avg = avg;
	}
	
	public double getMin(){
		return this.min;
	}
	
	public double getMax(){
		return this.max;
	}
	
	public double getAvg(){
		return this.avg;
	}

	@Override
	public String toString() {
		return "Values [min=" + min + ", max=" + max + ", avg=" + avg + "]";
	}
	
	
}
