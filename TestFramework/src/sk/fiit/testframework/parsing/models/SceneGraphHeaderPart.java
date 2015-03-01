package sk.fiit.testframework.parsing.models;

public class SceneGraphHeaderPart {

	private SceneGraphType sceneGraphType;
	private int sceneGraphMainVersion = -1;
	private int sceneGraphSubVersion = -1;
	public void setSceneGraphType(SceneGraphType sceneGraphType) {
		this.sceneGraphType = sceneGraphType;
	}
	public SceneGraphType getSceneGraphType() {
		return sceneGraphType;
	}
	public void setSceneGraphMainVersion(int sceneGraphMainVersion) {
		this.sceneGraphMainVersion = sceneGraphMainVersion;
	}
	public int getSceneGraphMainVersion() {
		return sceneGraphMainVersion;
	}
	public void setSceneGraphSubVersion(int sceneGraphSubVersion) {
		this.sceneGraphSubVersion = sceneGraphSubVersion;
	}
	public int getSceneGraphSubVersion() {
		return sceneGraphSubVersion;
	}
	
}
