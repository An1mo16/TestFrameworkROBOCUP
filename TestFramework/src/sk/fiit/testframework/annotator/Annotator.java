/**
 * Name:    Annotator.java
 * Created: 26.11.2011
 * 
 * @author: Miroslav Bimbo
 */
package sk.fiit.testframework.annotator;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import sk.fiit.robocup.library.geometry.Circle;
import sk.fiit.robocup.library.geometry.MEC;
import sk.fiit.robocup.library.geometry.Vector2;
import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.testframework.annotator.serialization.Annotation;
import sk.fiit.testframework.annotator.serialization.Axis;
import sk.fiit.testframework.annotator.serialization.Values;
import sk.fiit.testframework.annotator.serialization.XMLcreator;
import sk.fiit.testframework.init.Implementation;
import sk.fiit.testframework.init.ImplementationFactory;
import sk.fiit.testframework.trainer.testsuite.ITestCaseObserver;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;

/**
 * Class is responsibile for creating of automatic
 * XML annotations of moves. 
 * 
 * @author Miro Bimbo
 * 
 * TODO: what to do when game over? 
 *
 */
public class Annotator implements ITestCaseObserver{


	private static double MIN_DISTANCE = 0.1; 		//minimal distance for ball movement recognition
	private static double BALL_Z_AXIS_DEFAULT = 0.05; 	//default value of ball z axis at start of annotating
	private static File XSD_FILE = new File("./annotation/annotation.xsd"); //annotation XSD source file
	
	private int loops; //number of tests for every initial ball position 
	private Circle initCircle; //initial circle of ball positions
	private String moveName; //name of move to be tested
	private File dest; //output annotation file
	private File annotationOutputDir; //output, output directory
	private Annotation annotation; //annotation representation in Java 

	//initial ball positions computed from initial circle
	private List<Vector3> initBallPositions
		= Collections.synchronizedList(new LinkedList<Vector3>());
	//test case results, represents informations collected in tests 
	private List<AnnotatorTestCaseResult> moveInfos 
		= Collections.synchronizedList(new LinkedList<AnnotatorTestCaseResult>());	

	private Logger logger = Logger.getLogger(getClass().getName());


	public Annotator(int loops, Circle circle,String moveName,File annotationOutputDir){
		this.moveName = moveName;
		this.loops = loops;
		this.initCircle = circle;
		this.initBallPositions.addAll(initPositionsFromCircle(circle));
		this.annotationOutputDir = annotationOutputDir; 
	}

	/**
	 * Testing player moves using TestCase 
	 * This method only inserts testCases into queue
	 * and starts testing.
	 *  
	 * Does not work, if there is no communication whether
	 * from Player to Framework or from Framework to Player. 
	 * Check configuration files
	 * 
	 * Does not work, if player is doing some move in loop.
	 * Check plan.rb
	 * 
	 * @param annotSource is source annotation file wrote by human
	 */
	public void annotate(){
		if(!XSD_FILE.isFile()){
			logger.severe("XSD file not exists, check constant XSD_FILE: "+XSD_FILE);
			return;
		}
		
		if(!annotationOutputDir.isDirectory()){
			logger.severe("Destination directory does not exists: "+annotationOutputDir);
			return;
		}
		
		dest = getDestFile(annotationOutputDir,moveName);
		
		//TODO: TEST CONNECTION WITH PLAYER
		//TEST MOVE EXISTANCE
		
		//PARSE
		/*try {
			annotation = XMLparser.parse(annotSource,XSD_FILE);
			logger.info("File succesfully parsed: "+annotSource);
		} catch (Exception e) {
			logger.severe("Cannot parse source annotation file: "+annotSource.getAbsolutePath());
			e.printStackTrace();
		}*/
		
		//if unable to parse file, create blank annotation
		if(annotation == null){
			annotation = new Annotation();
			annotation.setName(moveName);
			annotation.setNote("Annotation created automatically by annotator,"+
					" please do some human work there. (set note, type, lying in prediction, endstate)");
			logger.info("Created blank annotation.");
		}
		
		annotation.getPreconditions().setBallPosCircle(initCircle);

		//TODO:GET CHECKSUM FROM PLAYER
		if(!annotation.getName().equals(this.moveName)){
			logger.severe("Move name in source annotation is not same as move to test, annotator closed.");
			return;
		}
		
		//TEST
		test();
	}
	
	
	/**
	 * Find name for next annotation (+1 after biggest number)
	 * 
	 * @param dir annotations directory
	 * @param moveToTest name of move (also annotation without counter)
	 * @return new annotation filename 
	 * 
	 * */
	private static File getDestFile(File dir, String moveToTest) {
		int max = -1;
		//for each file in annotations directory
		for(String file: dir.list()){
			//if file is moveToTest-number.xml
			if (file.matches("^[a-zA-Z_0-9]{1,}[-]{1}[0-9]{1,}\\.xml$")){
				String[] s = file.substring(0, file.length() - 4).split("-");
				if(s[0].equals(moveToTest) && Integer.parseInt(s[1])>max){
					max=Integer.parseInt(s[1]);
				}
			}	
		}
		return new File(dir+"/"+moveToTest+"-"+(max+1)+".xml");
	}

	/**
	 * Testing player moves using TestCase 
	 * This function only insert testCases into queue
	 */
	private void test(){
		Implementation impl = ImplementationFactory.getImplementationInstance();
		try {
			for(Vector3 initBall: this.initBallPositions){
				for(int i=0;i<loops;i++){
					AnnotatorTestCase testCase =new AnnotatorTestCase(initBall,moveName);
					impl.enqueueTestCase(testCase, this);
				}	
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while testcase processing. " + e);
		}
	}	
	
	//TODO: od Boba, sort by Private/Protected/Public 
	/**
	 *  Create new annotation from original annotation
	 *  using new info from testing
	 */
	private void annotateAfterTest() {
		
		Annotation newAnnot = remakeAnot(annotation,moveInfos);
		
		try {
			XMLcreator.serialize(dest, newAnnot);
		} catch (ParserConfigurationException e) {
			logger.severe("cannot create XML: " + e.getMessage());
			e.printStackTrace();
			return;
		} catch (TransformerException e) {
			logger.severe("cannot create XML: " + e.getMessage());
			e.printStackTrace();
			return;
		} finally{
			moveInfos.clear();
		}
		logger.info("File"+dest.getAbsolutePath()+" succesfully created ");
	}
	
	/** 
	 * method called after each testCase
	 * @throws Exception 
	 */
	public void testFinished(TestCaseResult result){
		AnnotatorTestCaseResult anotResult = (AnnotatorTestCaseResult) result;
		
		moveInfos.add(anotResult);
		
		logger.info("Test "+moveInfos.size()+" of "+loops*initBallPositions.size()+" finished");
		
		//if last test is finished 
		if(moveInfos.size() == loops*initBallPositions.size()){
			annotateAfterTest();
		}
	}

	/**
	 * Count final Annotation from resulted Annotations 
	 */
	private static Annotation remakeAnot(Annotation annotation,
			List<AnnotatorTestCaseResult> results) {
		
		if (results.size()<1){
			return annotation;
		}
		
		computePlayerMove(annotation, results);
		computePlayerFall(annotation, results);
		computeBallMove(annotation, results);
		computeMoveDuration(annotation, results);
		computePlayerRotation(annotation, results);
		computeBallPrecondicions(annotation, results);
		
		computeBallDistance(annotation, results);
		computeEndStateBallPositionsCircle(annotation, results);
		
		computeBallDispersion(annotation, results);
		
		return annotation;
	}

	/**
	 * Computes several 3D points on borders of circle 
	 * and one in the center of circle
	 * 
	 * @param initCircle
	 * @return
	 */
	private List<Vector3> initPositionsFromCircle(
			Circle initCircle) {
		
		List<Vector3> pos = new LinkedList<Vector3>();
		
		double x = initCircle.getCenter().getX();
		double y = initCircle.getCenter().getY();
		double z = BALL_Z_AXIS_DEFAULT;
		double r = initCircle.getRadius();
		
		pos.add(new Vector3(x,y,z));
		pos.add(new Vector3(x+r,y,z)); 		
		pos.add(new Vector3(x-r,y,z));
		pos.add(new Vector3(x,y+r,z));
		pos.add(new Vector3(x,y-r,z));
		pos.add(new Vector3(x+Math.cos(Math.PI/4)*r,y+Math.cos(Math.PI/4)*r,z));
		pos.add(new Vector3(x+Math.cos(Math.PI/4)*r,y-Math.cos(Math.PI/4)*r,z));
		pos.add(new Vector3(x-Math.cos(Math.PI/4)*r,y+Math.cos(Math.PI/4)*r,z));
		pos.add(new Vector3(x-Math.cos(Math.PI/4)*r,y-Math.cos(Math.PI/4)*r,z));		
		
		return pos;
	}
	
	private static void computeBallDispersion(Annotation annotation,
			List<AnnotatorTestCaseResult> results) {
		Vector3 endBallPos;
		Values val = new Values();
		double sum = 0;
		for (AnnotatorTestCaseResult res : results){
			endBallPos = res.getInitBallPosition().addition(res.getBallMove());
			double degree = 90-Math.toDegrees(Math.atan2(endBallPos.getX(),endBallPos.getY()));
			sum = sum + degree;
			if(degree>val.getMax()){
				val.setMax(degree);
			} else if(degree<val.getMin()){
				val.setMin(degree);
			}
			//System.out.println();
		}
		val.setAvg(sum/results.size());
		annotation.setBallDisperion(val);
		
	}
	
	private static void computeEndStateBallPositionsCircle(
			Annotation annotation, List<AnnotatorTestCaseResult> results) {
		
		List<Vector2> points = new LinkedList<Vector2>();
		
		for(AnnotatorTestCaseResult res : results){	
			
			Vector3 moved = res.getBallMove(); //how much did ball moved from its initial position?
			Vector3 started = res.getInitBallPosition(); //what is initial ball position?
			Vector3 absolute = started.addition(moved); //what is absolute final position of ball?
			points.add(new Vector2(absolute.getX(),absolute.getY()));
		}
		
		annotation.getEnd().setBallPosCircle(MEC.minEnclosingCircle(points));
		
	}

	private static void computeBallDistance(Annotation annotation,
			List<AnnotatorTestCaseResult> results) {
	
		AnnotatorTestCaseResult firstResult = results.get(0); 
		//buggy?
		//double firstDistance = firstResult.getInitBallPosition().getXYDistanceFrom(firstResult.getBallMove());
		double firstDistance = firstResult.getBallMove().getXYDistanceFrom(new Vector3(0,0,0));
			
		double min=firstDistance;
		double max=firstDistance;
		double distSum=0;
		Vector3 maxPos = new Vector3();
		
		for(AnnotatorTestCaseResult res: results){
			//double dist = res.getInitBallPosition().getXYDistanceFrom(res.getBallMove());
			double dist = res.getBallMove().getXYDistanceFrom(new Vector3(0,0,0));
			if(dist >= max){
				max = dist;
				annotation.setMaxPos(true);
				maxPos = res.getInitBallPosition(); 
				}
			if(dist < min){min = dist;}
			distSum = distSum+dist;
		}
		annotation.getBallMoveDistance().setMin(min);
		annotation.getBallMoveDistance().setMax(max);
		annotation.getBallMoveDistance().setAvg(distSum/results.size());
		
		annotation.setMaxBallDistancePosition(maxPos);
		
		
	}

	// isnt there some better representation than rectagle?
	private static void computeBallPrecondicions(Annotation annotation,
			List<AnnotatorTestCaseResult> results) {
		double xmin=annotation.getPreconditions().getBall().getX().getMin();
		double xmax=annotation.getPreconditions().getBall().getX().getMax();
		double ymin=annotation.getPreconditions().getBall().getY().getMin();
		double ymax=annotation.getPreconditions().getBall().getY().getMax();
		double xavg=annotation.getPreconditions().getBall().getX().getAvg();
		double yavg=annotation.getPreconditions().getBall().getY().getAvg();
		boolean someResults = false;
		for(AnnotatorTestCaseResult res: results){
			//if ball moved more than minimal distance
			if(res.getBallMove().getXYDistanceFrom(new Vector3(0,0,0)) > MIN_DISTANCE){
				someResults = true;
				if(res.getInitBallPosition().getX() > xmax){xmax = res.getInitBallPosition().getX();}
				if(res.getInitBallPosition().getX() < xmin){xmin = res.getInitBallPosition().getX();}
				if(res.getInitBallPosition().getY() > ymax){ymax = res.getInitBallPosition().getY();}
				if(res.getInitBallPosition().getY() < ymin){ymin = res.getInitBallPosition().getY();}
			}
			annotation.setPrec(someResults);
			annotation.getPreconditions().getBall().setX(xmin, xmax, xavg);
			annotation.getPreconditions().getBall().setY(ymin, ymax, yavg);
		}
		
	}

	private static void computePlayerRotation(Annotation anot,
			List<AnnotatorTestCaseResult> results) {
		
		double firstX = results.get(0).getRotation().getX()*180/Math.PI;
		double firstY = results.get(0).getRotation().getY()*180/Math.PI;
		double firstZ = results.get(0).getRotation().getZ()*180/Math.PI;
		if(firstX>180){firstX=firstX-360;}
		if(firstX<-180){firstX=firstX+360;}
		if(firstY>180){firstY=firstY-360;}
		if(firstY<-180){firstY=firstY+360;}
		if(firstZ>180){firstZ=firstZ-360;}
		if(firstZ<-180){firstZ=firstZ+360;}
		double xmin=firstX,xmax=firstX;
		double ymin=firstY,ymax=firstY;
		double zmin=firstZ,zmax=firstZ;
		double xsum=0,ysum=0,zsum=0;
		for(AnnotatorTestCaseResult res: results){
			//Rad to Degree
			double x = res.getRotation().getX()*180/Math.PI;
			double y = res.getRotation().getY()*180/Math.PI;
			double z = res.getRotation().getZ()*180/Math.PI;
			// beacuse of desired nubers in <-180,180>
			if(x>180){x=x-360;}
			if(x<-180){x=x+360;}
			if(y>180){y=y-360;}
			if(y<-180){y=y+360;}
			if(z>180){z=z-360;}
			if(z<-180){z=z+360;}
			// get min max sum
			if(x < xmin){xmin = x;}
			if(x > xmax){xmax = x;}
			if(y < ymin){ymin = y;}
			if(y > ymax){ymax = y;}
			if(z < zmin){zmin = z;}
			if(z > zmax){zmax = z;}
			xsum = xsum+x;
			ysum = ysum+y;
			zsum = zsum+z;
		}
		
		Axis rot = new Axis();
		rot.setX(xmin, xmax, xsum/results.size());
		rot.setY(ymin, ymax, ysum/results.size());
		rot.setZ(zmin, zmax, zsum/results.size());
		anot.setRot(true);
		anot.setRotation(rot);
		
	}

	private static void computeMoveDuration(Annotation anot,
			List<AnnotatorTestCaseResult> results) {
		
		double durationSum = 0;
		double max = results.get(0).getDuration();
		double min = results.get(0).getDuration();
		for(AnnotatorTestCaseResult res: results){
			durationSum = durationSum+res.getDuration();
			if(res.getDuration()> max){max = res.getDuration();}
			if(res.getDuration()< min){min = res.getDuration();}
		}
		
		Values duration = new Values();
		duration.setMax(max);
		duration.setMin(min);
		duration.setAvg(durationSum/results.size());
		anot.setDuration(duration);
	}

	private static void computeBallMove(Annotation anot,
			List<AnnotatorTestCaseResult> results) {
		
		double firstX = results.get(0).getBallMove().getX();
		double firstY = results.get(0).getBallMove().getY();
		double firstZ = results.get(0).getBallMove().getZ();
		double xmin=firstX,xmax=firstX;
		double ymin=firstY,ymax=firstY;
		double zmin=firstZ,zmax=firstZ;
		double xsum=0,ysum=0,zsum=0;
		for(AnnotatorTestCaseResult res: results){
			if(res.getBallMove().getX() < xmin){xmin = res.getBallMove().getX();}
			if(res.getBallMove().getX() > xmax){xmax = res.getBallMove().getX();}
			if(res.getBallMove().getY() < ymin){ymin = res.getBallMove().getY();}
			if(res.getBallMove().getY() > ymax){ymax = res.getBallMove().getY();}
			if(res.getBallMove().getZ() < zmin){zmin = res.getBallMove().getZ();}
			if(res.getBallMove().getZ() > zmax){zmax = res.getBallMove().getZ();}
			xsum = xsum+res.getBallMove().getX();
			ysum = ysum+res.getBallMove().getY();
			zsum = zsum+res.getBallMove().getZ();
		}
		
		Axis move = new Axis();
		move.setX(xmin, xmax, xsum/results.size());
		move.setY(ymin, ymax, ysum/results.size());
		move.setZ(zmin, zmax, zsum/results.size());
		anot.setB_mov(true);
		anot.setB_move(move);
		
	}

	private static void computePlayerFall(Annotation anot,
			List<AnnotatorTestCaseResult> results) {
		if (results.size()<1){
			return;
		}
		int falls = 0;
		for(AnnotatorTestCaseResult res: results){
			if (res.isFall()){
				falls++;
			}
		}
		anot.setFall(falls/results.size()*100);
	}

	private static void computePlayerMove(Annotation anot,
			List<AnnotatorTestCaseResult> results) {
		if (results.size()<1){
			return;
		}
		double firstX = results.get(0).getMove().getX();
		double firstY = results.get(0).getMove().getY();
		double firstZ = results.get(0).getMove().getZ();
		double xmin=firstX,xmax=firstX;
		double ymin=firstY,ymax=firstY;
		double zmin=firstZ,zmax=firstZ;
		double xsum=0,ysum=0,zsum=0;
		for(AnnotatorTestCaseResult res: results){
			if(res.getMove().getX() < xmin){xmin = res.getMove().getX();}
			if(res.getMove().getX() > xmax){xmax = res.getMove().getX();}
			if(res.getMove().getY() < ymin){ymin = res.getMove().getY();}
			if(res.getMove().getY() > ymax){ymax = res.getMove().getY();}
			if(res.getMove().getZ() < zmin){zmin = res.getMove().getZ();}
			if(res.getMove().getZ() > zmax){zmax = res.getMove().getZ();}
			xsum = xsum+res.getMove().getX();
			ysum = ysum+res.getMove().getY();
			zsum = zsum+res.getMove().getZ();
		}
		
		Axis move = new Axis();
		move.setX(xmin, xmax, xsum/results.size());
		move.setY(ymin, ymax, ysum/results.size());
		move.setZ(zmin, zmax, zsum/results.size());
		anot.setMov(true);
		anot.setMove(move);
	}
	
}
