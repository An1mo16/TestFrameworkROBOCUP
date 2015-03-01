/**
 * Name:    Data.java
 * Created: Dec 9, 2011
 * 
 * @author: ivan
 */
package sk.fiit.testframework.communication.agent;

import sk.fiit.testframework.communication.agent.AgentJim.TeamSide;

public class AgentData {
    
    private int uniformNumber;
    private TeamSide teamSide;
    private String teamName;

    public AgentData(int uniformNumber, TeamSide teamSide,String teamName) {
        this.uniformNumber = uniformNumber;
        this.teamSide = teamSide;
        this.teamName = teamName;
    }

    public TeamSide getTeam() {
        return teamSide;
    }
    
    public int getUniformNumber() {
        return uniformNumber;
    }

	public String getTeamName() {
		return teamName;
	}
    
    
    
}