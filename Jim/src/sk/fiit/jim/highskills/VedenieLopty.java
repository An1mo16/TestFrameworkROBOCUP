package sk.fiit.jim.highskills;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.robocup.library.geometry.Circle;
import sk.fiit.robocup.library.geometry.Line2D;
import sk.fiit.robocup.library.geometry.Vector2;
import sk.fiit.robocup.library.geometry.Vector3D;

/*
 * 
 * Reimplemented from Ruby to Java by Michal Blanarik
 */
public class VedenieLopty extends HighSkill{
	
	private double radius = 1; 
	
	public HighSkill pickHighSkill() {
		AgentInfo agentInfo = AgentInfo.getInstance();
		if(agentInfo.ballUnseen() > 5) {
			return new Localize(
					new LambdaCallable() {
						public boolean call() {
							return ! WorldModel.getInstance().isSeeBall() || AgentModel.getInstance().isOnGround();
						}
					}
					); 
		}							  
		return pickWalk();  
	}
	
	public HighSkill pickWalk(){
		AgentInfo agentInfo = AgentInfo.getInstance();
		WorldModel worldModel = WorldModel.getInstance();
		AgentModel agentModel = AgentModel.getInstance();
		
		//Smell #SmellType(NeverUsed) | Michal-PC/Michal 2013-12-10T21:29:13.9300000Z 
		//relative positions
		Vector3D ballRelPos = agentInfo.ballControlPosition();
		
		//absolute positions
		Vector3D ballAbsPos = worldModel.getBall().getPosition();
                // FIXME: Preco 11.5? Nemalo by byt 10.5 podla starej? V novej 15.
		Vector3D goalAbsPos = Vector3D.cartesian(11.5, 0, 0);
		Vector3D agentAbsPos = agentModel.getPosition();
		
		//circle around ball in radius
		Circle circleAroundBall = new Circle(new Vector2(ballAbsPos.getX(), ballAbsPos.getY()), radius);
		
		//line between ball and target
		Line2D line = new Line2D(ballAbsPos.getX(), ballAbsPos.getY(), goalAbsPos.getX(), goalAbsPos.getY());
		
		//orthogonal line
		Line2D ortLine = new Line2D(ballAbsPos.getX(), ballAbsPos.getY(), line.getNormalVector());
		
		//points of intersection between lines (ball, target) and circle(ball, radius)
		//there have to be always two points as a result for each line
		List<Vector2> pointsAroundBallOnSides = ortLine.getCircleIntersection(circleAroundBall);
		List<Vector2> pointsAroundBallOnWay = line.getCircleIntersection(circleAroundBall);
		
		Vector3D leftPoint = Vector3D.cartesian(getHighestY(pointsAroundBallOnSides), 0);
		Vector3D rightPoint = Vector3D.cartesian(getLowestY(pointsAroundBallOnSides), 0);
		Vector3D frontPoint = Vector3D.cartesian(getLowestX(pointsAroundBallOnWay), 0);
		
		Vector3D targetToGo;
		if(ortLine.solveGeneralEqation(agentAbsPos.getX(), agentAbsPos.getY()) < 2) {
			//'ball is behind me'
			if(line.solveGeneralEqation(agentAbsPos.getX(), agentAbsPos.getY()) > 0) {
				targetToGo = leftPoint;
			} //'I have to go left around ball'
			else {
				targetToGo = rightPoint;
			}			
		}
		else { //'ball is in front of me, I have to go to front point'
			targetToGo = frontPoint;
		}
		
		return new WalkOld(targetToGo);
	}
	
	private Vector2 getHighestY(List<Vector2> points){
		if(points.get(0).getY()>points.get(1).getY()) {
			return points.get(0);
		}	
		return points.get(1);
	}
	
	private Vector2 getLowestY(List<Vector2> points){
		if(points.get(0).getY()>points.get(1).getY()) {
			return points.get(1);
		}	
		return points.get(0);
	}
	
	private Vector2 getLowestX(List<Vector2> points){
		if(points.get(0).getX()>points.get(1).getX()) {
			return points.get(1);
		}	
		return points.get(0);
	}
	
	@Override
	public LowSkill pickLowSkill() {	
		//Bug #Task() #Solver() #Priority() | Michal-PC/Michal 2013-12-10T21:26:43.8230000Z 
		
		return null;
	}

	@Override
	public void checkProgress() throws Exception {
		// TODO Auto-generated method stub		
	}

	
	//Smell #SmellType(NeverUsed) | Michal-PC/Michal 2013-12-10T21:27:10.0750000Z 
	private boolean seeBall(){
		WorldModel worldModel = WorldModel.getInstance();
		if(worldModel.getBall().notSeenLongTime() < 5) {
			return true;
		}
		return false;
	}
	
}
