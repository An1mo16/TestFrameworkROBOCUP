package sk.fiit.testframework.parsing;

import sk.fiit.testframework.parsing.models.*;

public class SceneGraphHeaderPartParser extends SExpressionParser {

	public SceneGraphHeaderPart parse(String sceneGraphHeaderPart) {
		SceneGraphHeaderPart result = new SceneGraphHeaderPart();
		
		String[] sExpression = processSExpression(sceneGraphHeaderPart);
		result.setSceneGraphType(SceneGraphType.valueOf(sExpression[0]));
		result.setSceneGraphMainVersion(Integer.parseInt(sExpression[1]));
		result.setSceneGraphSubVersion(Integer.parseInt(sExpression[2]));
		
		return result;
	}
}
