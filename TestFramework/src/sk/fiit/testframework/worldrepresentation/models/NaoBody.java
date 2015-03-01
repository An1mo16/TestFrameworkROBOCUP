package sk.fiit.testframework.worldrepresentation.models;

public class NaoBody {
	
	private BodyPart torso;
	private BodyPart head;

	public NaoBody()
	{
		this.torso= new BodyPart();
		this.setHead(new BodyPart());
	}
	
	public void setTorso(BodyPart torso) {
		this.torso = torso;
	}

	public BodyPart getTorso() {
		return torso;
	}

	public void setHead(BodyPart head) {
		this.head = head;
	}

	public BodyPart getHead() {
		return head;
	}
}
