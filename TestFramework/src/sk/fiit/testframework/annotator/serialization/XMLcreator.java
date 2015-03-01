package sk.fiit.testframework.annotator.serialization;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sk.fiit.robocup.library.geometry.Circle;

/** 
 * @author: Roman Bilevic
 */

public class XMLcreator {

	public static void serialize(String file, Annotation annotation) throws ParserConfigurationException, TransformerException{
		serialize(new File(file), annotation);
	}
	
	public static void serialize(File file, Annotation annotation) throws ParserConfigurationException, TransformerException{
		Document d = XMLcreator.createXML(annotation);		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		DOMSource source = new DOMSource(d);
		StreamResult result = new StreamResult(file);
		//StreamResult result = new StreamResult(new File(file + ".xml"));
		//StreamResult result = new StreamResult(System.out);
		t.transform(source, result);
	}
	
	public static Document createXML(Annotation annotation) throws ParserConfigurationException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document d = db.newDocument();
		
		// korenovy element annotation
		Element root = d.createElement("annotation");
		Attr schema = d.createAttribute("xmlns:xsi");
		schema.setValue("http://www.w3.org/2001/XMLSchema-instance");
		root.setAttributeNode(schema);
		Attr location = d.createAttribute("xsi:noNamespaceSchemaLocation");
		location.setValue("framework.xsd");
		root.setAttributeNode(location);
		d.appendChild(root);
		
		// nazov anotovaneho pohybu
		Element name = d.createElement("name");
		name.appendChild(d.createTextNode(annotation.getName()));
		root.appendChild(name);
		
		// popis pohybu
		Element description = d.createElement("description");
		root.appendChild(description);
		
		
		// doba vykonavania pohybu
		Element duration = d.createElement("duration");
		description.appendChild(duration);
		
		Element d_min = d.createElement("min");
		d_min.appendChild(d.createTextNode(Double.toString(annotation.duration.getMin())));
		duration.appendChild(d_min);
		
		Element d_max = d.createElement("max");
		d_max.appendChild(d.createTextNode(Double.toString(annotation.duration.getMax())));
		duration.appendChild(d_max);
		
		Element d_avg = d.createElement("avg");
		d_avg.appendChild(d.createTextNode(Double.toString(annotation.duration.getAvg())));
		duration.appendChild(d_avg);
		
		// otocenie robota
		if(annotation.rot == true){
			Element rotation = d.createElement("rotation");
			description.appendChild(rotation);
			
			// os X je nepovinna
			if((annotation.rotation.getX().getMin() != 0) || (annotation.rotation.getX().getMax() != 0) || (annotation.rotation.getX().getAvg() != 0)){
				Element x_axis = d.createElement("x-axis");
				rotation.appendChild(x_axis);
				
				Element min = d.createElement("min");
				min.appendChild(d.createTextNode(Double.toString(annotation.rotation.getX().getMin())));
				x_axis.appendChild(min);
				
				Element max = d.createElement("max");
				max.appendChild(d.createTextNode(Double.toString(annotation.rotation.getX().getMax())));
				x_axis.appendChild(max);
				
				Element avg = d.createElement("avg");
				avg.appendChild(d.createTextNode(Double.toString(annotation.rotation.getX().getAvg())));
				x_axis.appendChild(avg);
			}
			
			// os Y je nepovinna
			if((annotation.rotation.getY().getMin() != 0) || (annotation.rotation.getY().getMax() != 0) || (annotation.rotation.getY().getAvg() != 0)){
				Element y_axis = d.createElement("y-axis");
				rotation.appendChild(y_axis);
				
				Element min = d.createElement("min");
				min.appendChild(d.createTextNode(Double.toString(annotation.rotation.getY().getMin())));
				y_axis.appendChild(min);
				
				Element max = d.createElement("max");
				max.appendChild(d.createTextNode(Double.toString(annotation.rotation.getY().getMax())));
				y_axis.appendChild(max);
				
				Element avg = d.createElement("avg");
				avg.appendChild(d.createTextNode(Double.toString(annotation.rotation.getY().getAvg())));
				y_axis.appendChild(avg);
			}
			
			// os Z je povinna
			Element z_axis = d.createElement("z-axis");
			rotation.appendChild(z_axis);
			
			Element min = d.createElement("min");
			min.appendChild(d.createTextNode(Double.toString(annotation.rotation.getZ().getMin())));
			z_axis.appendChild(min);
			
			Element max = d.createElement("max");
			max.appendChild(d.createTextNode(Double.toString(annotation.rotation.getZ().getMax())));
			z_axis.appendChild(max);
			
			Element avg = d.createElement("avg");
			avg.appendChild(d.createTextNode(Double.toString(annotation.rotation.getZ().getAvg())));
			z_axis.appendChild(avg);
			
		}
		
		// pohyb robota
		if(annotation.mov == true){
			Element move = d.createElement("move");
			description.appendChild(move);
						
			// os X je povinna
			Element x_axis = d.createElement("x-axis");
			move.appendChild(x_axis);
			
			Element x_min = d.createElement("min");
			x_min.appendChild(d.createTextNode(Double.toString(annotation.move.getX().getMin())));
			x_axis.appendChild(x_min);
			
			Element x_max = d.createElement("max");
			x_max.appendChild(d.createTextNode(Double.toString(annotation.move.getX().getMax())));
			x_axis.appendChild(x_max);
			
			Element x_avg = d.createElement("avg");
			x_avg.appendChild(d.createTextNode(Double.toString(annotation.move.getX().getAvg())));
			x_axis.appendChild(x_avg);			
			
			// os Y je povinna
			Element y_axis = d.createElement("y-axis");
			move.appendChild(y_axis);
			
			Element y_min = d.createElement("min");
			y_min.appendChild(d.createTextNode(Double.toString(annotation.move.getY().getMin())));
			y_axis.appendChild(y_min);
			
			Element y_max = d.createElement("max");
			y_max.appendChild(d.createTextNode(Double.toString(annotation.move.getY().getMax())));
			y_axis.appendChild(y_max);
			
			Element y_avg = d.createElement("avg");
			y_avg.appendChild(d.createTextNode(Double.toString(annotation.move.getY().getAvg())));
			y_axis.appendChild(y_avg);
			
			// os Z je nepovinna
			if((annotation.move.getZ().getMin() != 0) || (annotation.move.getZ().getMax() != 0) || (annotation.move.getZ().getAvg() != 0)){
				Element z_axis = d.createElement("z-axis");
				move.appendChild(z_axis);
				
				Element z_min = d.createElement("min");
				z_min.appendChild(d.createTextNode(Double.toString(annotation.move.getZ().getMin())));
				z_axis.appendChild(z_min);
				
				Element z_max = d.createElement("max");
				z_max.appendChild(d.createTextNode(Double.toString(annotation.move.getZ().getMax())));
				z_axis.appendChild(z_max);
				
				Element z_avg = d.createElement("avg");
				z_avg.appendChild(d.createTextNode(Double.toString(annotation.move.getZ().getAvg())));
				z_axis.appendChild(z_avg);
			}
		}
		
		// pravdepodobnost padu
		Element fall = d.createElement("fall");
		fall.appendChild(d.createTextNode(Double.toString(annotation.fall)));
		description.appendChild(fall);
		
		//posun_lopty, added by High5
		Element ballDistance = d.createElement("ball_distance");
		description.appendChild(ballDistance);
		
		Element min = d.createElement("min");
		min.appendChild(d.createTextNode(Double.toString(annotation.getBallMoveDistance().getMin())));
		ballDistance.appendChild(min);
		
		Element max = d.createElement("max");
		max.appendChild(d.createTextNode(Double.toString(annotation.getBallMoveDistance().getMax())));
		ballDistance.appendChild(max);
		
		Element avg = d.createElement("avg");
		avg.appendChild(d.createTextNode(Double.toString(annotation.getBallMoveDistance().getAvg())));
		ballDistance.appendChild(avg);
		
		
		// inicialna poloha lopty, pri ktorej bol dosiahnuty najdlhsi kop, added by High5
		if(annotation.isMaxPos()){
			Element maxPos = d.createElement("max_ball_distance_position");
			description.appendChild(maxPos);
			
			Element x = d.createElement("x");
			x.appendChild(d.createTextNode(Double.toString(annotation.getMaxBallDistancePosition().getX())));
			maxPos.appendChild(x);
			
			Element y = d.createElement("y");
			y.appendChild(d.createTextNode(Double.toString(annotation.getMaxBallDistancePosition().getY())));
			maxPos.appendChild(y);
			
			Element z = d.createElement("z");
			z.appendChild(d.createTextNode(Double.toString(annotation.getMaxBallDistancePosition().getZ())));
			maxPos.appendChild(z);
		}
		
		
		
		// pohyb lopty
		if(annotation.b_mov == true){
			Element b_move = d.createElement("ball_move");
			description.appendChild(b_move);
			
			// os X je povinna
			Element x_axis = d.createElement("x-axis");
			b_move.appendChild(x_axis);
			
			Element x_min = d.createElement("min");
			x_min.appendChild(d.createTextNode(Double.toString(annotation.b_move.getX().getMin())));
			x_axis.appendChild(x_min);
			
			Element x_max = d.createElement("max");
			x_max.appendChild(d.createTextNode(Double.toString(annotation.b_move.getX().getMax())));
			x_axis.appendChild(x_max);
			
			Element x_avg = d.createElement("avg");
			x_avg.appendChild(d.createTextNode(Double.toString(annotation.b_move.getX().getAvg())));
			x_axis.appendChild(x_avg);			
			
			// os Y je povinna
			Element y_axis = d.createElement("y-axis");
			b_move.appendChild(y_axis);
			
			Element y_min = d.createElement("min");
			y_min.appendChild(d.createTextNode(Double.toString(annotation.b_move.getY().getMin())));
			y_axis.appendChild(y_min);
			
			Element y_max = d.createElement("max");
			y_max.appendChild(d.createTextNode(Double.toString(annotation.b_move.getY().getMax())));
			y_axis.appendChild(y_max);
			
			Element y_avg = d.createElement("avg");
			y_avg.appendChild(d.createTextNode(Double.toString(annotation.b_move.getY().getAvg())));
			y_axis.appendChild(y_avg);
			
			// os Z je nepovinna
			if((annotation.move.getZ().getMin() != 0) || (annotation.move.getZ().getMax() != 0) || (annotation.move.getZ().getAvg() != 0)){
				Element z_axis = d.createElement("z-axis");
				b_move.appendChild(z_axis);
				
				Element z_min = d.createElement("min");
				z_min.appendChild(d.createTextNode(Double.toString(annotation.b_move.getZ().getMin())));
				z_axis.appendChild(z_min);
				
				Element z_max = d.createElement("max");
				z_max.appendChild(d.createTextNode(Double.toString(annotation.b_move.getZ().getMax())));
				z_axis.appendChild(z_max);
				
				Element z_avg = d.createElement("avg");
				z_avg.appendChild(d.createTextNode(Double.toString(annotation.b_move.getZ().getAvg())));
				z_axis.appendChild(z_avg);
			}
		}
		
		// predpodmienky
		if(annotation.prec == true){
			Element preconditions = d.createElement("preconditions");
			root.appendChild(preconditions);
			
			// lezi robot ?
			Element lying = d.createElement("lying");
			lying.appendChild(d.createTextNode(annotation.preconditions.lying));
			preconditions.appendChild(lying);
			
			// vzdialenost lopty od hraca
			if(annotation.preconditions.b_pos == true){
				Element b_pos = d.createElement("ball_position");
				preconditions.appendChild(b_pos);
				
				// os X je povinna
				Element x_axis = d.createElement("x-axis");
				b_pos.appendChild(x_axis);
				
				Element x_min = d.createElement("min");
				x_min.appendChild(d.createTextNode(Double.toString(annotation.preconditions.ball.getX().getMin())));
				x_axis.appendChild(x_min);
				
				Element x_max = d.createElement("max");
				x_max.appendChild(d.createTextNode(Double.toString(annotation.preconditions.ball.getX().getMax())));
				x_axis.appendChild(x_max);
				
				Element x_avg = d.createElement("avg");
				x_avg.appendChild(d.createTextNode(Double.toString(annotation.preconditions.ball.getX().getAvg())));
				x_axis.appendChild(x_avg);			
				
				// os Y je povinna
				Element y_axis = d.createElement("y-axis");
				b_pos.appendChild(y_axis);
				
				Element y_min = d.createElement("min");
				y_min.appendChild(d.createTextNode(Double.toString(annotation.preconditions.ball.getY().getMin())));
				y_axis.appendChild(y_min);
				
				Element y_max = d.createElement("max");
				y_max.appendChild(d.createTextNode(Double.toString(annotation.preconditions.ball.getY().getMax())));
				y_axis.appendChild(y_max);
				
				Element y_avg = d.createElement("avg");
				y_avg.appendChild(d.createTextNode(Double.toString(annotation.preconditions.ball.getY().getAvg())));
				y_axis.appendChild(y_avg);
				
				// os Z je nepovinna
				if((annotation.preconditions.ball.getZ().getMin() != 0) || (annotation.preconditions.ball.getZ().getMax() != 0) || (annotation.preconditions.ball.getZ().getAvg() != 0)){
					Element z_axis = d.createElement("z-axis");
					b_pos.appendChild(z_axis);
					
					Element z_min = d.createElement("min");
					z_min.appendChild(d.createTextNode(Double.toString(annotation.preconditions.ball.getZ().getMin())));
					z_axis.appendChild(z_min);
					
					Element z_max = d.createElement("max");
					z_max.appendChild(d.createTextNode(Double.toString(annotation.preconditions.ball.getZ().getMax())));
					z_axis.appendChild(z_max);
					
					Element z_avg = d.createElement("avg");
					z_avg.appendChild(d.createTextNode(Double.toString(annotation.preconditions.ball.getZ().getAvg())));
					z_axis.appendChild(z_avg);
				}
			}
			
			//circle, added by High5
			Circle circle = annotation.getPreconditions().getBallPosCircle();
			if(circle.getRadius()>0){
				Element circleElem = d.createElement("ball_positions_circle");
				preconditions.appendChild(circleElem);
				
				Element x = d.createElement("x");
				x.appendChild(d.createTextNode(Double.toString(circle.getCenter().getX())));
				circleElem.appendChild(x);
				
				Element y = d.createElement("y");
				y.appendChild(d.createTextNode(Double.toString(circle.getCenter().getY())));
				circleElem.appendChild(y);
				
				Element r = d.createElement("r");
				r.appendChild(d.createTextNode(Double.toString(circle.getRadius())));
				circleElem.appendChild(r);
			}
			
			// uhly klbov
			if(annotation.preconditions.joints.size() > 0){
				Element joints = d.createElement("joints");
				preconditions.appendChild(joints);
				
				for(int i=0; i<annotation.preconditions.joints.size(); i++){
					if(annotation.preconditions.joints.get(i).name != null){
						Element joint = d.createElement(annotation.preconditions.joints.get(i).name);
						joint.appendChild(d.createTextNode(Double.toString(annotation.preconditions.joints.get(i).value)));
						joints.appendChild(joint);
					}
				}
			}
		}
		
		// koncovy stav
		Element end = d.createElement("end_state");
		root.appendChild(end);
		
		// lezi robot ?
		Element lying = d.createElement("lying");
		lying.appendChild(d.createTextNode(annotation.end.lying));
		end.appendChild(lying);
		
		// vzdialenost lopty od hraca
		if(annotation.end.b_pos == true){
			Element b_pos = d.createElement("ball_position");
			end.appendChild(b_pos);
			
			// os X je povinna
			Element x_axis = d.createElement("x-axis");
			b_pos.appendChild(x_axis);
			
			Element x_min = d.createElement("min");
			x_min.appendChild(d.createTextNode(Double.toString(annotation.end.ball.getX().getMin())));
			x_axis.appendChild(x_min);
			
			Element x_max = d.createElement("max");
			x_max.appendChild(d.createTextNode(Double.toString(annotation.end.ball.getX().getMax())));
			x_axis.appendChild(x_max);
			
			Element x_avg = d.createElement("avg");
			x_avg.appendChild(d.createTextNode(Double.toString(annotation.end.ball.getX().getAvg())));
			x_axis.appendChild(x_avg);			
			
			// os Y je povinna
			Element y_axis = d.createElement("y-axis");
			b_pos.appendChild(y_axis);
			
			Element y_min = d.createElement("min");
			y_min.appendChild(d.createTextNode(Double.toString(annotation.end.ball.getY().getMin())));
			y_axis.appendChild(y_min);
			
			Element y_max = d.createElement("max");
			y_max.appendChild(d.createTextNode(Double.toString(annotation.end.ball.getY().getMax())));
			y_axis.appendChild(y_max);
			
			Element y_avg = d.createElement("avg");
			y_avg.appendChild(d.createTextNode(Double.toString(annotation.end.ball.getY().getAvg())));
			y_axis.appendChild(y_avg);
			
			// os Z je nepovinna
			if((annotation.end.ball.getZ().getMin() != 0) || (annotation.end.ball.getZ().getMax() != 0) || (annotation.end.ball.getZ().getAvg() != 0)){
				Element z_axis = d.createElement("z-axis");
				b_pos.appendChild(z_axis);
				
				Element z_min = d.createElement("min");
				z_min.appendChild(d.createTextNode(Double.toString(annotation.end.ball.getZ().getMin())));
				z_axis.appendChild(z_min);
				
				Element z_max = d.createElement("max");
				z_max.appendChild(d.createTextNode(Double.toString(annotation.end.ball.getZ().getMax())));
				z_axis.appendChild(z_max);
				
				Element z_avg = d.createElement("avg");
				z_avg.appendChild(d.createTextNode(Double.toString(annotation.end.ball.getZ().getAvg())));
				z_axis.appendChild(z_avg);
			}
		}
		
		//circle, added by High5
		Circle circle = annotation.getEnd().getBallPosCircle();
		if(circle.getRadius()>0){
			Element circleElem = d.createElement("ball_positions_circle");
			end.appendChild(circleElem);
			
			Element x = d.createElement("x");
			x.appendChild(d.createTextNode(Double.toString(circle.getCenter().getX())));
			circleElem.appendChild(x);
			
			Element y = d.createElement("y");
			y.appendChild(d.createTextNode(Double.toString(circle.getCenter().getY())));
			circleElem.appendChild(y);
			
			Element r = d.createElement("r");
			r.appendChild(d.createTextNode(Double.toString(circle.getRadius())));
			circleElem.appendChild(r);
		}
		
		// uhly klbov
		if(annotation.end.joints.size() > 0){
			Element joints = d.createElement("joints");
			end.appendChild(joints);
			
			for(int i=0; i<annotation.end.joints.size(); i++){
				if(annotation.end.joints.get(i).name != null){
					Element joint = d.createElement(annotation.end.joints.get(i).name);
					joint.appendChild(d.createTextNode(Double.toString(annotation.end.joints.get(i).value)));
					joints.appendChild(joint);
				}
			}
		}
		
		if(annotation.note != null){
			Element note = d.createElement("note");
			note.appendChild(d.createTextNode(annotation.note));
			root.appendChild(note);
		}
		
		if(annotation.checksum != null){
			Element checksum = d.createElement("checksum");
			checksum.appendChild(d.createTextNode(annotation.checksum));
			root.appendChild(checksum);
		}
		
		return d;
	}
}
