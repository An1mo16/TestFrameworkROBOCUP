package sk.fiit.jim.agent.models;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.parsing.ParsedData;
import sk.fiit.jim.agent.parsing.PlayerData;
import sk.fiit.jim.agent.parsing.PlayerData.PlayerPartsNames;
import sk.fiit.robocup.library.geometry.Vector3D;
import sk.fiit.robocup.library.geometry.Vector3DTest;

/**
 * 
 * @author TomasNemecek
 * @author gitmen
 * 
 *         Testing TacticalInfo methods.
 */
public class TacticalInfoTest {

	private static List<PlayerData> testedData;
	private static WorldModel worldModel;
	private static int PLAYERID = 0;
	
	@BeforeClass
	public static void initTest() {
		
		testedData = new ArrayList<PlayerData>();
		AgentInfo info = AgentInfo.getInstance();
		info.team="team";
		info.playerId = PLAYERID;
		
		
		ParsedData data = new ParsedData();
		
		for(int i = 1 ; i < 4 ; i++){
			PlayerData playerdata = addPlayer(new Double(i));
			data.otherplayers.add(playerdata);
			testedData.add(playerdata);
		}
		data.ballRelativePosition = Vector3D.ZERO_VECTOR;
		worldModel = WorldModel.getInstance();
		worldModel.processNewServerMessage(data);
	}

	@Test
	public void testIsBallNearestToMe() {
		TacticalInfo tacticalInfo = new TacticalInfo();
		setAgentPosition(2);
		assertEquals(myTestBallDistance(PLAYERID), tacticalInfo.isBallNearestToMe());
		setAgentPosition(0);
		assertEquals(myTestBallDistance(PLAYERID), tacticalInfo.isBallNearestToMe());
	}
        
        private void setAgentPosition(int i) {
		AgentModel.getInstance().position= Vector3D.cartesian(i, i * i, i + 10);
	}

		@Test
	public void testisOnPosition() {
		TacticalInfo tacticalInfo = new TacticalInfo();
                AgentInfo agentInfo = AgentInfo.getInstance();
                AgentModel agentModel = AgentModel.getInstance();
                agentInfo.team = "test";
                
                agentInfo.playerId = 3;     
                tacticalInfo.setMyFormPosition();
                agentModel.setPosition(Vector3D.cartesian(4.1, -0.3, 0));   
                assertEquals(true, tacticalInfo.isOnPosition());
                
                agentInfo.playerId = 3;     
                tacticalInfo.setMyFormPosition();
                agentModel.setPosition(Vector3D.cartesian(4.49, 0, 0));   
                assertEquals(true, tacticalInfo.isOnPosition());
                
                agentInfo.playerId = 3;     
                tacticalInfo.setMyFormPosition();
                agentModel.setPosition(Vector3D.cartesian(4.5, 0, 0)); 
                assertEquals(false, tacticalInfo.isOnPosition());
                
                agentInfo.playerId = 3;     
                tacticalInfo.setMyFormPosition();
                agentModel.setPosition(Vector3D.cartesian(2, 1, 0)); 
                assertEquals(false, tacticalInfo.isOnPosition());
	}

	private Object myTestBallDistance(int playerId) {
		List<Player> players = worldModel.getTeamPlayers();
		players.addAll(worldModel.getOpponentPlayers());

		Player minPlayer = players.get(0);
		// get minimal distance between ball and some other player
		for (Player p : players) {
			if (p.getDistanceFromBall() < minPlayer.getDistanceFromBall()) {
				minPlayer = p;
			}
		}

		double myDistance = DistanceHelper.computeDistanceBetweenObjects(
									WorldModel.getInstance().getBall().getPosition(), 
									AgentModel.getInstance().getPosition());

		return myDistance < minPlayer.getDistanceFromBall();
	}

	private static PlayerData addPlayer(double position) {

		PlayerData playerData = new PlayerData();
		playerData.id = new Double(position).toString().substring(0,1);
		if(new Integer(playerData.id) % 2 == 0){
			playerData.team = "team";
		}
		else{
			playerData.team = "team1";
		}
		Vector3D relativeposition = Vector3D.cartesian(position, position*position, position+10);
		playerData.bodyParts.put(PlayerPartsNames.llowerarm, relativeposition);
		playerData.bodyParts.put(PlayerPartsNames.rlowerarm, relativeposition);
		playerData.bodyParts.put(PlayerPartsNames.lfoot, relativeposition);
		playerData.bodyParts.put(PlayerPartsNames.rfoot, relativeposition);
		playerData.bodyParts.put(PlayerPartsNames.head, relativeposition);

		return playerData;
	}
}
