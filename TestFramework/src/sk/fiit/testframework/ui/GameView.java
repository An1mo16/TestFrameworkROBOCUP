/**
 * Name:    GameView.java
 * Created: 24.4.2012
 * 
 * @author: Peto
 */
package sk.fiit.testframework.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Logger;

import javax.swing.JPanel;

import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.robocup.library.geometry.Vector2;
import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.robocup.library.geometry.Vector3D;
import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.AgentJim.TeamSide;
import sk.fiit.testframework.communication.agent.AgentManager;
import sk.fiit.testframework.parsing.models.EnvironmentPart;
import sk.fiit.testframework.worldrepresentation.models.Player;
import sk.fiit.testframework.worldrepresentation.models.SimulationState;

/**
 * Provides a swing component that shows a graphical view of the game running on the server
 */
public class GameView extends JPanel {
	private final Color fieldColor = new Color(0x00BB00);
	private final Color leftColor = new Color(0x0000BB);
	private final Color rightColor = new Color(0xBB0000);
	private final Color leftWColor = new Color(0x4444EE);
	private final Color rightWColor = new Color(0xEE4444);
	private final double padding = 1.0;
	private final int minViewBallRadius = 3;
	private final int minViewAgentRadius = 4;
	private long lastUpdate = 0;
	private int updateInterval = 0;
	
	private SimulationState state;
	private Logger logger = Logger.getLogger("sk.fiit.testframework");
	private double fieldWidth, fieldHeight, vfRatio;
	int viewWidth, viewHeight, viewX = 0, viewY = 0;
	
	public enum WorldModelDrawing { ALL, NONE, SELECTED }
	private WorldModelDrawing wmDrawing = WorldModelDrawing.ALL;
	private AgentJim wmAgent = null;
	
	public void setWorldModelDrawing(WorldModelDrawing wmd) {
		wmDrawing = wmd;
	}
	
	public void setWMDrawingAgent(AgentJim agent) {
		wmAgent = agent;
	}
	
	public void setSimulationState(SimulationState s) {
		state = s;
	}
	
	private Vector2 realToPixel(double x, double y) {
		return new Vector2(viewX + viewWidth/2 + x*vfRatio, viewY + viewHeight/2 - y*vfRatio);
	}
	
	public void setUpdateInterval(int interval) {
		updateInterval = interval;
	}
	
	public void requestUpdate() {
		if (System.currentTimeMillis() > lastUpdate + updateInterval)
			repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		lastUpdate = System.currentTimeMillis();
		if (state == null)
			return;
		
		//get the dimensions for the view
		EnvironmentPart envInfo = state.getEnvironmentInfo();
		double ratio = (envInfo.getFieldLength()+2*padding) / (envInfo.getFieldWidth()+2*padding);
		fieldWidth = envInfo.getFieldLength() + 2*padding;
		fieldHeight = envInfo.getFieldWidth() + 2*padding;
		g.setColor(fieldColor);
		double viewRatio = (double)getWidth()/ getHeight();
		if (ratio > viewRatio) {
			viewWidth = getWidth();
			viewHeight = (int)(getWidth() / ratio);
			viewX = 0;
			viewY = getHeight()/2 - viewHeight/2;
		}else{
			viewHeight = getHeight();
			viewWidth = (int)(getHeight() * ratio);
			viewY = 0;
			viewX = getWidth()/2 - viewWidth/2;
		}
		vfRatio = viewWidth / fieldWidth;
		int viewPadding = (int)(padding * vfRatio);
		
		//field, borders and center line
		g.fillRect(viewX, viewY, viewWidth, viewHeight);
		g.setColor(Color.black);
		g.drawRect(viewX+viewPadding, viewY+viewPadding, viewWidth-2*viewPadding, viewHeight-2*viewPadding);
		g.drawLine(getWidth()/2, viewY+viewPadding, getWidth()/2, viewY + viewHeight - viewPadding);
		
		//branky
		int viewGoalWidth = (int)(envInfo.getGoalDepth() * vfRatio);
		int viewGoalHeight = (int)(envInfo.getGoalWidth() * vfRatio);
		g.drawRect(viewX+viewPadding-viewGoalWidth, viewY + viewHeight/2 - viewGoalHeight/2,
				viewGoalWidth, viewGoalHeight);
		
		g.drawRect(viewX+viewWidth-viewPadding, viewY+viewHeight/2 - viewGoalHeight/2,
				viewGoalWidth, viewGoalHeight);
		
		//lopta
		g.setColor(Color.white);
		Vector3 ballPos = state.getScene().getBallLocation();
		Vector2 ballView = realToPixel(ballPos.getX(), ballPos.getY());
		int ballViewRadius = Math.max((int)(envInfo.getBallRadius() * vfRatio), minViewBallRadius);
		g.fillOval((int)ballView.getX()-ballViewRadius, (int)ballView.getY()-ballViewRadius,
				ballViewRadius*2, ballViewRadius*2);
		
		//vykreslenie agentov
		int agentViewRadius = Math.max((int)(envInfo.getAgentRadius()*vfRatio), minViewAgentRadius);
		for (int i=0; i<AgentManager.getManager().getAgentCount(); i++) {
			AgentJim agent = AgentManager.getManager().getAgentByOrder(i);
			int playerId = agent.getSceneGraphPlayer();
			Player player = state.getScene().getPlayers().get(playerId);
			
			//pozicia agenta podla monitorovacieho rozhrania
			Vector3 playerPos = player.getLocation();
			Vector2 playerView = realToPixel(playerPos.getX(), playerPos.getY());
			g.setColor(agent.getAgentData().getTeam() == TeamSide.LEFT ? leftColor : rightColor);
			g.drawOval((int)playerView.getX()-agentViewRadius, (int)playerView.getY()-agentViewRadius,
					agentViewRadius*2, agentViewRadius*2);
			//rotacia hraca podla monitorovacieho rozhrania (nejak nefunguje)
			//poznamka k optimalizacii: toto asi pocita dost vela nepotrebnych veci (sinusov a pod.)
			Vector3 v = player.getRotation();
			double rotZ = - v.getZ();
			/*Vector3D playerRot = Vector3D.fromVector3(v).setR(agentViewRadius);*/
			Vector3D plRotVector = Vector3D.spherical(agentViewRadius, rotZ, 0);
			g.drawLine((int)playerView.getX(), (int)playerView.getY(),
					(int)(playerView.getX() + plRotVector.getX()), (int)(playerView.getY() - plRotVector.getY()));
			/*g.drawLine((int)playerView.getX(), (int)playerView.getY(), 
					(int)(playerView.getX() + playerRot.getZ()), (int)(playerView.getY() + playerRot.getY()));*/
			
			//udaje z modelu sveta sa vykreslia len pre agentov podla nastavenia
			if (wmDrawing == WorldModelDrawing.ALL || (wmDrawing == WorldModelDrawing.SELECTED && wmAgent == agent)) {
				g.setColor(agent.getAgentData().getTeam() == TeamSide.LEFT ? leftWColor : rightWColor);
				WorldModel model = agent.getWorldModel();
				if (model != null) {
					//agentova pozicia podla jeho world modelu
					Vector3D wmPos = model.getAgentModel().getPosition();
					Vector2 wmView = realToPixel(wmPos.getX(), wmPos.getY());
					g.drawOval((int)wmView.getX()-agentViewRadius, (int)wmView.getY()-agentViewRadius,
							agentViewRadius*2, agentViewRadius*2);
					
					//agentova rotacia podla jeho world modelu
					double wmRot = model.getAgentModel().getRotationZ();
					Vector3D wmRotVector = Vector3D.spherical(agentViewRadius, wmRot, 0);
					g.drawLine((int)wmView.getX(), (int)wmView.getY(),
							(int)(wmView.getX() + wmRotVector.getX()), (int)(wmView.getY() - wmRotVector.getY()));
					
					//pozicia lopty podla agentovho world modelu
					Vector3D wmBallPos = model.getBall().getPosition();
					Vector2 wmBallView = realToPixel(wmBallPos.getX(), wmBallPos.getY());
					g.fillOval((int)wmBallView.getX()-ballViewRadius, (int)wmBallView.getY()-ballViewRadius,
							ballViewRadius*2, ballViewRadius*2);
				}
			}
		}
	}
}
