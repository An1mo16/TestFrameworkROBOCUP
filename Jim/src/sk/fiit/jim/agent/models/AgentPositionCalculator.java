package sk.fiit.jim.agent.models;

import java.util.Map.Entry;

import static sk.fiit.jim.log.LogType.AGENT_MODEL;
import sk.fiit.jim.agent.parsing.ParsedData;
import sk.fiit.jim.log.Log;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *  AgentPositionCalculator.java
 *  
 *  Calculates an approximation of agent's current position, based on the
 *  flags seen. Class assumes that agent's rotations are correctly calculated.
 *  
 *  Calculating is done by normalizing and reversing a given flag position.
 *  When this percieved relative position is added to the known position
 *  of the flag, a correct approximation of our position should be calculated.
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
public class AgentPositionCalculator{
	private final AgentModel agent;

	/**
	 * Sets current AgentModel.
	 * @param agent
	 */
	public AgentPositionCalculator(AgentModel agent){
		this.agent = agent;
	}
	
	/**
	 * Updates agent's position in current AgentModel from
	 * last received data from server by calculation using fixed
	 * objects(flags) and agents rotation and position.
	 *
	 * @param data
	 */
	public void updatePosition(ParsedData data){
		if(data.fixedObjects == null || data.fixedObjects.size() < 1) {
			return;
		}
		
		Vector3D accumulator = Vector3D.cartesian(0.0, 0.0, 0.0);
		
		for(Entry<FixedObject, Vector3D> flag : data.fixedObjects.entrySet()){
			Vector3D absolute = flag.getKey().getAbsolutePosition();
			Vector3D seen = flag.getValue();
			Vector3D ourPosition = seen.rotateOverX(agent.rotationX).rotateOverY(agent.rotationY).rotateOverZ(agent.rotationZ).negate();
			ourPosition = ourPosition.add(absolute);
			accumulator = accumulator.add(ourPosition);
		}
		Vector3D pos = accumulator.divide(data.fixedObjects.size());
		if (Math.abs(pos.getXYDistanceFrom(agent.position)) < 1) {
			agent.position = pos;
		}
		
//		agent.lastPositions.add(agent.position);
//		if (agent.position.subtract(agent.lastPositions.))
//		if (agent.lastPositions.size() == 21) {
//			agent.lastPositions.remove();
//		}
//		this.Regress();
		Log.log(AGENT_MODEL, "My position: [%.2f,%.2f,%.2f]", agent.position.getX(), agent.position.getY(), agent.position.getZ());
	}
	
	public void regress() {
		int MAXN = 1000;
        //pocitanie regresie pre X
		int n = 0;
        double[] x = new double[MAXN];
        double[] y = new double[MAXN];
		double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
		for (Vector3D pos : agent.lastPositions) {
			x[n] = pos.getX();
			y[n] = n;
			sumx += x[n];
			sumx2 += x[n] * x[n];
			sumy += y[n];
			n++;
		}
		double xbar = sumx / n;
		double ybar = sumy / n;

		// second pass: compute summary statistics
		double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
		for (int i = 0; i < n; i++) {
			xxbar += (x[i] - xbar) * (x[i] - xbar);
			yybar += (y[i] - ybar) * (y[i] - ybar);
			xybar += (x[i] - xbar) * (y[i] - ybar);
		}
		double beta1 = xybar / xxbar;
		double beta0 = ybar - beta1 * xbar;
		double regresX =  beta1*(n-1)+beta0;
		//pocitanie regresie pre y
		n = 0;
        double[] x1 = new double[MAXN];
        double[] y1 = new double[MAXN];
		sumx = 0.0;
		sumy = 0.0;
		sumx2 = 0.0;
		for (Vector3D pos : agent.lastPositions) {
			x1[n] = pos.getY();
			y1[n] = n;
			sumx += x1[n];
			sumx2 += x1[n] * x1[n];
			sumy += y1[n];
			n++;
		}
		xbar = sumx / n;
		ybar = sumy / n;

		// second pass: compute summary statistics
		xxbar = 0.0;
		yybar = 0.0;
		xybar = 0.0;
		for (int i = 0; i < n; i++) {
			xxbar += (x1[i] - xbar) * (x1[i] - xbar);
			yybar += (y1[i] - ybar) * (y1[i] - ybar);
			xybar += (x1[i] - xbar) * (y1[i] - ybar);
		}
		beta1 = xybar / xxbar;
		beta0 = ybar - beta1 * xbar;
		double regresY =  beta1*(n-1)+beta0;
		// set results
//		System.out.println("y   = " + beta1 + " * x + " + beta0);
		agent.position = agent.position.setX(regresX).setY(regresY);

//		// analyze results
//		int df = n - 2;
//		double rss = 0.0; // residual sum of squares
//		double ssr = 0.0; // regression sum of squares
//		for (int i = 0; i < n; i++) {
//			double fit = beta1 * x[i] + beta0;
//			rss += (fit - y[i]) * (fit - y[i]);
//			ssr += (fit - ybar) * (fit - ybar);
//		}
//		double R2 = ssr / yybar;
//		double svar = rss / df;
//		double svar1 = svar / xxbar;
//		double svar0 = svar / n + xbar * xbar * svar1;
//		System.out.println("R^2                 = " + R2);
//		System.out.println("std error of beta_1 = " + Math.sqrt(svar1));
//		System.out.println("std error of beta_0 = " + Math.sqrt(svar0));
//		svar0 = svar * sumx2 / (n * xxbar);
//		System.out.println("std error of beta_0 = " + Math.sqrt(svar0));
//
//		System.out.println("SSTO = " + yybar);
//		System.out.println("SSE  = " + rss);
//		System.out.println("SSR  = " + ssr);
	}
}