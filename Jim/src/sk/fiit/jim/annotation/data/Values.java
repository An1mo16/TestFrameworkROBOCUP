package sk.fiit.jim.annotation.data;

/** 
 * Class with maximal, minimal and average value of axis used in annotations.
 * 
 * @author: Roman Bilevic
 */

public class Values {
	/**
	 * Minimal value.
	 */
	double min = 0;
	/**
	 * Maximal value.
	 */
	double max = 0;
	/**
	 * Average value.
	 */
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