package sk.fiit.testframework.worldrepresentation;

import java.util.ArrayList;
import java.util.logging.Logger;

import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.AgentManager;
import sk.fiit.testframework.parsing.models.SceneGraphHeaderPart;
import sk.fiit.testframework.parsing.models.SceneGraphPart;
import sk.fiit.testframework.parsing.models.SceneGraphType;
import sk.fiit.testframework.parsing.models.nodes.Node;
import sk.fiit.testframework.parsing.models.nodes.NodeType;
import sk.fiit.testframework.parsing.models.nodes.NonLeafNode;
import sk.fiit.testframework.parsing.models.nodes.StaticMeshNode;
import sk.fiit.testframework.worldrepresentation.models.NaoBody;
import sk.fiit.testframework.worldrepresentation.models.Player;
import sk.fiit.testframework.worldrepresentation.models.Scene;
import sk.fiit.testframework.worldrepresentation.models.SceneGraphDescription;

class SceneUpdater {

	public void update(Scene scene, SceneGraphHeaderPart header,
			SceneGraphPart body) {

		SceneGraphDescription currentSceneDescription = scene.getDescription();

		updateSceneGraphDescription(scene, currentSceneDescription, body,
				isFullSceneGraph(header));
		updateBallPosition(scene, currentSceneDescription);
		updatePlayers(scene, currentSceneDescription);
	}

	private void updateSceneGraphDescription(Scene scene,
			SceneGraphDescription currentSceneDescription, SceneGraphPart body,
			boolean isFullSceneGraph) {
		if (isFullSceneGraph) {
			currentSceneDescription.setFullSceneGraph(body);

			ArrayList<Integer> ballPositions = new ArrayList<Integer>();
			findBallInfoPosition(body.getNodes(), ballPositions);

			ArrayList<Integer> playerPositions;
			playerPositions = findPlayerInfoPositions(body.getNodes());

			currentSceneDescription.setBallPosition(ballPositions);
			currentSceneDescription.setPlayerPositions(playerPositions);

			scene.getPlayers().clear();
		} else {
			// update full scene graph
			synchronizeNodes(body.getNodes(), currentSceneDescription
					.getFullSceneGraph().getNodes());
		}

	}

	private void synchronizeNodes(ArrayList<Node> updatedNodes,
			ArrayList<Node> currentNodes) {
		for (int i = 0; i < updatedNodes.size(); i++) {

			if (currentNodes.size() > i) {
				if (updatedNodes.get(i).getNodeType() != NodeType.Empty) {
					currentNodes.set(i, updatedNodes.get(i));
				}

				if (currentNodes.get(i).getNodeType() == NodeType.Transform) {
					synchronizeNodes(
							((NonLeafNode) updatedNodes.get(i)).getChildren(),
							((NonLeafNode) currentNodes.get(i)).getChildren());
				}
			}
		}
	}

	private boolean findBallInfoPosition(ArrayList<Node> currentNodes,
			ArrayList<Integer> ballPositions) {
		for (int i = 0; i < currentNodes.size(); i++) {
			Node n = currentNodes.get(i);
			if (n.getNodeType() == NodeType.StaticMesh) {
				StaticMeshNode smn = (StaticMeshNode) n;
				if (smn.getModel().contains("ball")) {
					ballPositions.add(i);
					return true;
				}
			}
			if (n.getNodeType() == NodeType.Transform) {
				if (findBallInfoPosition(((NonLeafNode) n).getChildren(),
						ballPositions)) {
					ballPositions.add(i);
					return true;
				}
			}
		}
		return false;
	}

	private ArrayList<Integer> findPlayerInfoPositions(ArrayList<Node> nodes) {
		ArrayList<Integer> result = new ArrayList<Integer>();

		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			if (n.getNodeType() == NodeType.Transform) {
				if (((NonLeafNode) n).getChildren().size() == 23) {
					result.add(i);
				}
			}
		}

		return result;
	}

	private void updateBallPosition(Scene scene,
			SceneGraphDescription currentSceneDescription) {
		ArrayList<Node> nodes = currentSceneDescription.getFullSceneGraph()
				.getNodes();
		Node ballNode = getNodeAtPosition(nodes,
				currentSceneDescription.getBallPosition());
		scene.setBallLocation(ballNode.getGlobalTransform().getTranslation());
	}

	private void updatePlayers(Scene scene,
			SceneGraphDescription currentSceneDescription) {
		ArrayList<Node> nodes = currentSceneDescription.getFullSceneGraph()
				.getNodes();

		int count = 0;
		for (Integer i : currentSceneDescription.getPlayerPositions()) {
			Node playerNode = nodes.get(i);

			Node torsoNode = ((NonLeafNode) playerNode).getChildren().get(0);
			Node headNode = ((NonLeafNode) playerNode).getChildren().get(2);

			// torso
			while (torsoNode.getNodeType() == NodeType.Transform) {
				torsoNode = ((NonLeafNode) torsoNode).getChildren().get(0);
			}

			// head
			while (headNode.getNodeType() == NodeType.Transform) {
				headNode = ((NonLeafNode) headNode).getChildren().get(0);
			}

			Player player;
			NaoBody body;

			if (scene.getPlayers().size() > count) {
				player = scene.getPlayers().get(count);
				body = (NaoBody) player.getBody();

			} else {
				body = new NaoBody();

				player = new Player();
				player.setBody(body);
				
				player.setOrder(count);
				AgentJim agent = AgentManager.getManager().getAgentByOrder(count);
				if (agent == null) {
					AgentManager.getManager().enqueueScenePlayer(player);
				}else{
					agent.setSceneGraphPlayer(count);
					player.setAssociatedAgent(agent);
				}
				
				scene.getPlayers().add(player);
			}
			body.getTorso().setTransformation(torsoNode.getGlobalTransform());
			body.getHead().setTransformation(headNode.getGlobalTransform());

			player.setOnGround(body.getHead().getTransformation().getValues()[14]<0.2);
			player.setStanding(body.getHead().getTransformation().getValues()[14]>0.5);
			
			count++;
		}
	}

	private boolean isFullSceneGraph(SceneGraphHeaderPart header) {
		return (header.getSceneGraphType() == SceneGraphType.RSG);
	}

	private Node getNodeAtPosition(ArrayList<Node> nodes,
			ArrayList<Integer> positions) {
		int currentIndex = positions.size() - 1;
		Node result = null;
		while (currentIndex >= 0) {
			result = nodes.get(positions.get(currentIndex));

			if (currentIndex > 0) {
				nodes = ((NonLeafNode) result).getChildren();
			}
			currentIndex--;
		}
		return result;
	}
}
