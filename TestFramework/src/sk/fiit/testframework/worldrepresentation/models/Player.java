package sk.fiit.testframework.worldrepresentation.models;

import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.testframework.communication.agent.AgentJim;

public class Player {

	private NaoBody body;
	private boolean isOnGround;
	private boolean isStanding;
	private AgentJim associatedAgent;
	private int order;

	public boolean isStanding() {
		return isStanding;
	}

	public void setStanding(boolean isStanding) {
		this.isStanding = isStanding;
	}

	public void setBody(NaoBody body) {
		this.body = body;
	}

	public NaoBody getBody() {
		return body;
	}

	public void setOnGround(boolean isOnGround) {
		this.isOnGround = isOnGround;
	}

	public boolean isOnGround() {
		return isOnGround;
	}
	
	public Vector3 getLocation() {
		return body.getTorso().getTranslation();
	}
	
	public Vector3 getRotation() {
		return body.getTorso().getRotation();
	}
	
	public double getNormalizedRotation() {
		return - getRotation().getZ();
	}

	public AgentJim getAssociatedAgent() {
		return associatedAgent;
	}

	public void setAssociatedAgent(AgentJim associatedAgent) {
		this.associatedAgent = associatedAgent;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
