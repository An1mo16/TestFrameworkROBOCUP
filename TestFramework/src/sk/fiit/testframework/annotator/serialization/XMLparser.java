package sk.fiit.testframework.annotator.serialization;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sk.fiit.robocup.library.geometry.Circle;
import sk.fiit.robocup.library.geometry.Vector2;
/** 
 * @author: Roman Bilevic
 */

public class XMLparser {
	
	public static Annotation parse(File file,File xsdFile) throws ParserConfigurationException, IOException, SAXException{
		Annotation annotation = new Annotation();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document d = db.parse(file);
		
		XMLparser.check(file,xsdFile);
		
		// element name
		annotation.name = d.getElementsByTagName("name").item(0).getTextContent();
		
		// element duration
		NodeList duration = d.getElementsByTagName("duration").item(0).getChildNodes();
		for(int i=0; i<duration.getLength(); i++){
			if(duration.item(i).getNodeName().compareTo("min") == 0){
				annotation.duration.setMin(new Double(duration.item(i).getTextContent()));
			}
			
			if(duration.item(i).getNodeName().compareTo("max") == 0){
				annotation.duration.setMax(new Double(duration.item(i).getTextContent()));
			}
			
			if(duration.item(i).getNodeName().compareTo("avg") == 0){
				annotation.duration.setAvg(new Double(duration.item(i).getTextContent()));
			}
		}
		
		// element rotation
		NodeList rotation = d.getElementsByTagName("rotation");
		if(rotation.getLength() > 0){
			annotation.rot = true;
			NodeList r_axis = rotation.item(0).getChildNodes();
			for(int i=0; i<r_axis.getLength(); i++){
				if(r_axis.item(i).getNodeName().compareTo("z-axis") == 0){
					NodeList z = r_axis.item(i).getChildNodes();
					double min = 0;
					double max = 0;
					double avg = 0;
					for(int j=0; j<z.getLength(); j++){
						if(z.item(j).getNodeName().compareTo("min") == 0){
							min = new Double(z.item(j).getTextContent());
						}
						
						if(z.item(j).getNodeName().compareTo("max") == 0){
							max = new Double(z.item(j).getTextContent());
						}
						
						if(z.item(j).getNodeName().compareTo("avg") == 0){
							avg = new Double(z.item(j).getTextContent());
						}
					}
					annotation.rotation.setZ(min, max, avg);
				}
			}
		}
		
		// element move
		NodeList move = d.getElementsByTagName("move");
		if(move.getLength() > 0){
			annotation.mov = true;
			NodeList m_axis = move.item(0).getChildNodes();
			for(int i=0; i<m_axis.getLength(); i++){
				if(m_axis.item(i).getNodeName().compareTo("x-axis") == 0){
					NodeList x = m_axis.item(i).getChildNodes();
					double min = 0;
					double max = 0;
					double avg = 0;
					for(int j=0; j<x.getLength(); j++){
						if(x.item(j).getNodeName().compareTo("min") == 0){
							min = new Double(x.item(j).getTextContent());
						}
						
						if(x.item(j).getNodeName().compareTo("max") == 0){
							max = new Double(x.item(j).getTextContent());
						}
						
						if(x.item(j).getNodeName().compareTo("avg") == 0){
							avg = new Double(x.item(j).getTextContent());
						}
					}
					annotation.move.setX(min, max, avg);
				}
				
				if(m_axis.item(i).getNodeName().compareTo("y-axis") == 0){
					NodeList y = m_axis.item(i).getChildNodes();
					double min = 0;
					double max = 0;
					double avg = 0;
					for(int j=0; j<y.getLength(); j++){
						if(y.item(j).getNodeName().compareTo("min") == 0){
							min = new Double(y.item(j).getTextContent());
						}
						
						if(y.item(j).getNodeName().compareTo("max") == 0){
							max = new Double(y.item(j).getTextContent());
						}
						
						if(y.item(j).getNodeName().compareTo("avg") == 0){
							avg = new Double(y.item(j).getTextContent());
						}
					}
					annotation.move.setY(min, max, avg);
				}
			}
		}
		
		// element fall
		annotation.fall = new Double(d.getElementsByTagName("fall").item(0).getTextContent());
		
		
		// element ball_distance, added by high5
		if(d.getElementsByTagName("ball_distance").getLength()>0){
			NodeList ballDistance = d.getElementsByTagName("ball_distance").item(0).getChildNodes();
			for(int i=0; i<ballDistance.getLength(); i++){
				if(ballDistance.item(i).getNodeName().compareTo("min") == 0){
					annotation.getBallMoveDistance().setMin(new Double(ballDistance.item(i).getTextContent()));
				}
				
				if(ballDistance.item(i).getNodeName().compareTo("max") == 0){
					annotation.getBallMoveDistance().setMax(new Double(ballDistance.item(i).getTextContent()));
				}
				
				if(ballDistance.item(i).getNodeName().compareTo("avg") == 0){
					annotation.getBallMoveDistance().setAvg(new Double(ballDistance.item(i).getTextContent()));
				}
			}
		}
		
		//element max_ball_distance_position, added by high5
		if(d.getElementsByTagName("max_ball_distance_position").getLength()>0){
			NodeList maxPos = d.getElementsByTagName("max_ball_distance_position").item(0).getChildNodes();
			for(int i=0; i<maxPos.getLength(); i++){
				if(maxPos.item(i).getNodeName().compareTo("x") == 0){
					annotation.getMaxBallDistancePosition().setX(new Double(maxPos.item(i).getTextContent()));
				}
				if(maxPos.item(i).getNodeName().compareTo("y") == 0){
					annotation.getMaxBallDistancePosition().setY(new Double(maxPos.item(i).getTextContent()));
				}
				if(maxPos.item(i).getNodeName().compareTo("z") == 0){
					annotation.getMaxBallDistancePosition().setZ(new Double(maxPos.item(i).getTextContent()));
				}
			}	
		}
		
		
		//element ball_move
		NodeList ballMove = d.getElementsByTagName("ball_move");
		if(ballMove.getLength() > 0){
			annotation.b_mov = true;
			NodeList b_axis = ballMove.item(0).getChildNodes();
			for(int i=0; i<b_axis.getLength(); i++){
				if(b_axis.item(i).getNodeName().compareTo("x-axis") == 0){
					NodeList x = b_axis.item(i).getChildNodes();
					double min = 0;
					double max = 0;
					double avg = 0;
					for(int j=0; j<x.getLength(); j++){
						if(x.item(j).getNodeName().compareTo("min") == 0){
							min = new Double(x.item(j).getTextContent());
						}
						
						if(x.item(j).getNodeName().compareTo("max") == 0){
							max = new Double(x.item(j).getTextContent());
						}
						
						if(x.item(j).getNodeName().compareTo("avg") == 0){
							avg = new Double(x.item(j).getTextContent());
						}
					}
					annotation.b_move.setX(min, max, avg);
				}
				
				if(b_axis.item(i).getNodeName().compareTo("y-axis") == 0){
					NodeList y = b_axis.item(i).getChildNodes();
					double min = 0;
					double max = 0;
					double avg = 0;
					for(int j=0; j<y.getLength(); j++){
						if(y.item(j).getNodeName().compareTo("min") == 0){
							min = new Double(y.item(j).getTextContent());
						}
						
						if(y.item(j).getNodeName().compareTo("max") == 0){
							max = new Double(y.item(j).getTextContent());
						}
						
						if(y.item(j).getNodeName().compareTo("avg") == 0){
							avg = new Double(y.item(j).getTextContent());
						}
					}
					annotation.b_move.setY(min, max, avg);
				}
			}
		}
		
		// element preconditions
		NodeList preconditions = d.getElementsByTagName("preconditions");
		if(preconditions.getLength() > 0){
			annotation.prec = true;
			NodeList precondition = preconditions.item(0).getChildNodes();
			for(int k=0; k<precondition.getLength(); k++){
				if(precondition.item(k).getNodeName().compareTo("lying") == 0){
					annotation.preconditions.lying = precondition.item(k).getTextContent();
				}
				
				if(precondition.item(k).getNodeName().compareTo("ball_position") == 0){
					annotation.preconditions.b_pos = true;
					NodeList ballPosition = precondition.item(k).getChildNodes();
					for(int i=0; i<ballPosition.getLength(); i++){
						if(ballPosition.item(i).getNodeName().compareTo("x-axis") == 0){
							NodeList x = ballPosition.item(i).getChildNodes();
							double min = 0;
							double max = 0;
							double avg = 0;
							for(int j=0; j<x.getLength(); j++){
								if(x.item(j).getNodeName().compareTo("min") == 0){
									min = new Double(x.item(j).getTextContent());
								}
								
								if(x.item(j).getNodeName().compareTo("max") == 0){
									max = new Double(x.item(j).getTextContent());
								}
								
								if(x.item(j).getNodeName().compareTo("avg") == 0){
									avg = new Double(x.item(j).getTextContent());
								}
							}
							annotation.preconditions.ball.setX(min, max, avg);
						}
						
						if(ballPosition.item(i).getNodeName().compareTo("y-axis") == 0){
							NodeList y = ballPosition.item(i).getChildNodes();
							double min = 0;
							double max = 0;
							double avg = 0;
							for(int j=0; j<y.getLength(); j++){
								if(y.item(j).getNodeName().compareTo("min") == 0){
									min = new Double(y.item(j).getTextContent());
								}
								
								if(y.item(j).getNodeName().compareTo("max") == 0){
									max = new Double(y.item(j).getTextContent());
								}
								
								if(y.item(j).getNodeName().compareTo("avg") == 0){
									avg = new Double(y.item(j).getTextContent());
								}
							}
							annotation.preconditions.ball.setY(min, max, avg);
						}
					}
				}
				
				if(precondition.item(k).getNodeName().equals("ball_positions_circle")){
					NodeList circle = precondition.item(k).getChildNodes();
					double x=0,y=0,r=0;
					for(int j=0;j<circle.getLength(); j++){
						String nodeName = circle.item(j).getNodeName();
						if(nodeName.equals("x")){ 
							x = new Double(circle.item(j).getTextContent()); 
						}
						if(nodeName.equals("y")){ 
							y = new Double(circle.item(j).getTextContent()); 
						}
						if(nodeName.equals("r")){ 
							r = new Double(circle.item(j).getTextContent()); 
						}
					}
					Circle c = new Circle(new Vector2(x,y),r);
					annotation.getPreconditions().setBallPosCircle(c);			
				}
				
				if(precondition.item(k).getNodeName().compareTo("joints") == 0){
					NodeList joints = precondition.item(k).getChildNodes();
					for(int i=0; i<joints.getLength(); i++){
						if(joints.item(i).hasChildNodes()){
							Joint joint = new Joint();
							joint.name = joints.item(i).getNodeName();
							joint.value = new Double(joints.item(i).getTextContent());
							annotation.preconditions.joints.add(joint);
						}
					}
				}
			}
		}
		
		// element end_state
		NodeList endState = d.getElementsByTagName("end_state");
		if(endState.getLength() > 0){
			NodeList es = endState.item(0).getChildNodes();
			for(int k=0; k<es.getLength(); k++){
				if(es.item(k).getNodeName().compareTo("lying") == 0){
					annotation.end.lying = es.item(k).getTextContent();
				}
				
				if(es.item(k).getNodeName().compareTo("ball_position") == 0){
					annotation.end.b_pos = true;
					NodeList ballPosition = es.item(k).getChildNodes();
					for(int i=0; i<ballPosition.getLength(); i++){
						if(ballPosition.item(i).getNodeName().compareTo("x-axis") == 0){
							NodeList x = ballPosition.item(i).getChildNodes();
							double min = 0;
							double max = 0;
							double avg = 0;
							for(int j=0; j<x.getLength(); j++){
								if(x.item(j).getNodeName().compareTo("min") == 0){
									min = new Double(x.item(j).getTextContent());
								}
								
								if(x.item(j).getNodeName().compareTo("max") == 0){
									max = new Double(x.item(j).getTextContent());
								}
								
								if(x.item(j).getNodeName().compareTo("avg") == 0){
									avg = new Double(x.item(j).getTextContent());
								}
							}
							annotation.end.ball.setX(min, max, avg);
						}
						
						if(ballPosition.item(i).getNodeName().compareTo("y-axis") == 0){
							NodeList y = ballPosition.item(i).getChildNodes();
							double min = 0;
							double max = 0;
							double avg = 0;
							for(int j=0; j<y.getLength(); j++){
								if(y.item(j).getNodeName().compareTo("min") == 0){
									min = new Double(y.item(j).getTextContent());
								}
								
								if(y.item(j).getNodeName().compareTo("max") == 0){
									max = new Double(y.item(j).getTextContent());
								}
								
								if(y.item(j).getNodeName().compareTo("avg") == 0){
									avg = new Double(y.item(j).getTextContent());
								}
							}
							annotation.end.ball.setY(min, max, avg);
						}
					}
				}
				
				if(es.item(k).getNodeName().equals("ball_positions_circle")){
					NodeList circle = es.item(k).getChildNodes();
					double x=0,y=0,r=0;
					for(int j=0;j<circle.getLength(); j++){
						String nodeName = circle.item(j).getNodeName();
						if(nodeName.equals("x")){ 
							x = new Double(circle.item(j).getTextContent()); 
						}
						if(nodeName.equals("y")){ 
							y = new Double(circle.item(j).getTextContent()); 
						}
						if(nodeName.equals("r")){ 
							r = new Double(circle.item(j).getTextContent()); 
						}
					}
					Circle c = new Circle(new Vector2(x,y),r);
					annotation.getEnd().setBallPosCircle(c);			
				}
				
				if(es.item(k).getNodeName().compareTo("joints") == 0){
					NodeList joints = es.item(k).getChildNodes();
					for(int i=0; i<joints.getLength(); i++){
						if(joints.item(i).hasChildNodes()){
							Joint joint = new Joint();
							joint.name = joints.item(i).getNodeName();
							joint.value = new Double(joints.item(i).getTextContent());
							annotation.end.joints.add(joint);
						}
					}
				}
			}
		}
		
		// element note
		NodeList note = d.getElementsByTagName("note");
		if(note.getLength() > 0){
			annotation.note = note.item(0).getTextContent();
		}
		
		//element checksum
		NodeList checksum = d.getElementsByTagName("checksum");
		if(checksum.getLength() > 0){
			annotation.checksum = checksum.item(0).getTextContent();
		}
		
		return annotation;
	}
	
	
	//edited by Miro Bimbo, return boolean
	private static boolean check(File checkFile,File xsdFile) throws SAXException{
		
		SchemaFactory fac = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema xsd = fac.newSchema(xsdFile);
		Validator validator = xsd.newValidator();
		Source source = new StreamSource(checkFile);
		try {
			validator.validate(source);
			return true;
		} catch (IOException e) {
			return false;
		}

	}
}
