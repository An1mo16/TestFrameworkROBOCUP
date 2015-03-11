package sk.fiit.jim.agent.trajectory;

import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.annotation.data.Annotation;
import sk.fiit.jim.annotation.data.AnnotationManager;
import sk.fiit.jim.annotation.data.MoveValidator;
import sk.fiit.jim.annotation.data.XMLParser;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 * 
 *  TrajectoryPlanner.java
 *  This class calculates which moves must agent make in order to get from given start position and rotation
 *  to desired end position and rotation. These moves are ordered in queue which represents the trajectory.
 *  Basic idea of algorithm:
 *  1. agent rotates so he is facingthe final position
 *  2. agents walks to the final position
 *  3. agents rotates again to achieve the final rotation
 *  There can be also obstacles in agents trajectory. In order to bypass obstacles agent calculates around these obstacles.
 *  Trajectory is calculated with different deviations in order to calculate fastest trajectory possible.
 *  
 *@Title        Jim
 *@author       $Author: Roman Bilevic$
 */
public class TrajectoryPlanner {
	/**
	 * Represents fastest calculated trajectory.
	 */
	Trajectory trajectory = null;
	/**
	 * Contains all moves tagged as rotation.
	 */
	ArrayList<Annotation> rotation = new ArrayList<Annotation>();
	/**
	 * Contains all moves tagged as walk.
	 */
	ArrayList<Annotation> walk = new ArrayList<Annotation>();
	/**
	 * Contains all possible obstcles on trajectory.
	 */
	LinkedList<Vector3D> obstacles = new LinkedList<Vector3D>();
	/**
	 * Contains all calculated trajectories.
	 */
	LinkedList<Trajectory> trajectoryPlans = new LinkedList<Trajectory>();
	/**
	 * Queue of moves used to store partial trajectories calculated by rotate() and walk() methods.
	 */
	LinkedList<Annotation> moves = new LinkedList<Annotation>();     // zasobnik pohybov pre medzivypocty
	
	/**
	 * Starting position of agent.
	 */
	Vector3D startPosition;
	/**
	 * Starting rotation of agent.
	 */
	double startRotation;
	/**
	 * Final position of agent.
	 */
	Vector3D finalPosition;
	/**
	 * Final rotation of agent.
	 */
	double finalRotation;
	/**
	 * Actual position of agent during trajectory calculations.
	 */
	Vector3D actualPos = null;
	/**
	 * Actual rotation of agent during trajectory calculations.
	 */
	double actualPhi = 0;
	
	//povolene odchylky pre rotaciu
	public static final double ANGLE_DEVIATION_A = 0.5; // degree
	public static final double ANGLE_DEVIATION_B = 1.5;
	public static final double ANGLE_DEVIATION_C = 2.5;
	public static final double ANGLE_DEVIATION_D = 5;
	public static final double ANGLE_DEVIATION_E = 10;
	//povolena odchylka vo vzdialenosti o d ciela
	public static final double DISTANCE_DEVIATION = 0.1; // metre
	
	/**
	 * Annotation of the move that is standard for walking.
	 * Should be straight with minimal y-axis deviation, stable and fast.
	 */
	Annotation stdWalk;
	/**
	 * Stores the last move in the trajectory for purpose of compatibility
	 * testing of the next move.
	 */
	Annotation lastMove;	
	
	/**
	 * Constructs the object of this class and calls all methods needed to compute the optimal trajectory.
	 * 
	 * @param startPosition
	 * @param startRotation
	 * @param finalPosition
	 * @param finalRotation
	 * @param obstacles
	 */
	public TrajectoryPlanner(Vector3D startPosition, double startRotation, Vector3D finalPosition, double finalRotation, LinkedList<Vector3D> obstacles){
		setStart(startPosition, startRotation);
		setFinal(finalPosition, finalRotation);
		
		//z AnnotationManager, ktory ma v pamati ulozene anotacie k pohybom sa vyberu vsetky pohyby typu "rotate" a "walk"
		//a utriedia sa podla ich hlavneho atributu 
		List<Annotation> annotations = new LinkedList<Annotation>(AnnotationManager.getInstance().getAllAnnotations());
		for(Annotation a : annotations){
			if(LowSkills.exists(a.getName())){
				List<String> types = LowSkills.get(a.getName()).getType();
				for(String type : types){
					//"walk" sa utrieduje podla dlzky pohybu pozdlz osi x
					if(type.compareTo("walk") == 0){
						int k = 0;
						while((k<this.walk.size()) && (this.walk.get(k).getMove().getX().getAvg() > a.getMove().getX().getAvg())){
							k++;
						}
						this.walk.add(k, a);
					}
					//"rotation" sa utrieduje podla velkosti otocenia v stupnoch
					if(type.compareTo("rotation") == 0){
						int k = 0;
						while((k<this.rotation.size()) && (this.rotation.get(k).getRotation().getZ().getAvg() > a.getRotation().getZ().getAvg())){
							k++;
						}
						this.rotation.add(k, a);
					}
				}
			}
		}
		
		//vytvoria sa trajektorie pre kazdu povolenu odchylku
		trajectoryPlans.add(new Trajectory(ANGLE_DEVIATION_A));
		trajectoryPlans.add(new Trajectory(ANGLE_DEVIATION_B));
		trajectoryPlans.add(new Trajectory(ANGLE_DEVIATION_C));
		trajectoryPlans.add(new Trajectory(ANGLE_DEVIATION_D));
		trajectoryPlans.add(new Trajectory(ANGLE_DEVIATION_E));
		
		//pridaju sa prekazky
		this.obstacles = new LinkedList<Vector3D>(obstacles);
		
		//priradi sa anotacia ku standardnej chodzi
		File stdWalkFile = new File("./moves/annotations/walk_fine_fast2-0.xml");
		try {
			this.stdWalk = XMLParser.parse(stdWalkFile);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//vykonava sa planovanie trajektorie pre vsetky odchylky
		for(Trajectory t: trajectoryPlans){
			this.lastMove = null;
			this.actualPos = this.startPosition;
			this.actualPhi = this.startRotation;
			plan(t);
			//vypis trvania vykonavania trajektorie - testovacie ucely
			System.out.println(t.getDeviation() +" deviation: " + t.getTime());
		}
		
		//vyber najrychlejsej trajektorie
		trajectory = pickBestTrajectory();
		
		//vypis trajektorie - testovacie ucely
		trajectory.print();
	}
	
	/**
	 * 
	 * Sets start position and rotation of the agent. 
	 *
	 * @param position
	 * @param rotation
	 */
	private void setStart(Vector3D position, double rotation){
		this.startPosition = position;
		this.startRotation = rotation;
	}
	
	/**
	 * 
	 * Sets final position and rotation of the agent. 
	 *
	 * @param position
	 * @param rotation
	 */
	private void setFinal(Vector3D position, double rotation){
		this.finalPosition = position;
		this.finalRotation = rotation;
	}
	
	/**
	 * 
	 * Plans the trajectory using rotations and walk moves, bypassing obstacles.
	 * Deviation is determined in given trajectory as parameter.
	 *
	 * @param trajectory to fill with moves
	 */
	private void plan(Trajectory t){
		
		// vypocet vektora z aktualnej pozicie ku koncovej pozicii
		Vector3D newVector = finalPosition.subtract(actualPos);
		// rotacia smerom na finalnu poziciu
		double phi = newVector.getPhi();
		// vzdialenost k finalnej pozicii
		double distance = newVector.getR();
		
		// planovanie trajektorie sa vykona iba pokial je vzdialenost of finalnej pozicie
		// vacsia ako povolena odchylka trajektorie 
		if(distance > DISTANCE_DEVIATION){
			// vypocet uhlu, o ktory sa ma agent otocit aby smeroval k finalnej pozicii
			double firstAngle = phi - actualPhi;
			
			// normalizacia uhlu do intervalu (-PI,PI)
			// aby sa agent otacal o mensi uhol (napr nie o 300 stupnov ale o -60 stupnov)
			if(firstAngle < -PI){
				firstAngle = 2*PI + firstAngle;
			}
			if(firstAngle > PI){
				firstAngle = firstAngle - 2*PI;
			}
			
			// odchylka natocenia agenta o pozadovaneho natocenia
			double angleDeviation = firstAngle;
			
			// pokial je odchylka natocenia vacsia ako je povolena odchylka trajektorie
			// tak sa vypocitaju potrebne pohyby na pozadovane otocenie
			if(Math.toDegrees(abs(firstAngle)) > t.getDeviation()){
				angleDeviation = rotate(firstAngle, t.getDeviation());
			}
			
			// ak je blizko ciela, tak dokonci trajektoriu
			if(distance < 5*(stdWalk.getMove().getX().getAvg())){
				// pridanie vypocitanych rotacii do trajektorie
				t.addList(moves);
				
				// vypocet pohybov potrebnych na presun na finalnu poziciu
				walk(distance);
				t.addList(moves);
				
				// vypocet uhla potrebneho na finalne natocenie
				double lastAngle = finalRotation - phi;
				if(lastAngle < -PI){
					lastAngle = 2*PI + lastAngle;
				}
				if(lastAngle > PI){
					lastAngle = lastAngle - 2*PI;
				}
				
				// finalne natocenie
				rotate(lastAngle, 1);
				t.addList(moves);
			}
			// pokial sa agent nachadza daleko od finalnej pozicie
			// vypocita sa trajektoria iba pre 3/4 vzdialenosti od finalnej pozicie
			// a nasledne sa prepocita, ci agent stale smeruje k finalnej pozicie a 
			// ak nie , tak sa jeho kurz (rotacia) upravi
			else{
				// vypocet krokov potrebnych na prejdenie 3/4 vzdialenosti k finalnej pozicii
				int walkNum = (int) ((distance/stdWalk.getMove().getX().getAvg()) * 0.75);
				// prepocet prejdenej vzdialenosti podla krokov
				double walkDistance = walkNum * stdWalk.getMove().getX().getAvg();
				// aktualna rotacia agenta sa rovna pozadovanej rotacii - odchylka, ktora vznikla pri vypoctoch
				double rot = phi - angleDeviation;
				
				// vypocet suradnic, kam sa agent pri ciastocnom presune dostane
				double newX = actualPos.getX() + (Math.cos(rot + (PI/2))*walkDistance);
				double newY = actualPos.getY() + (Math.sin(rot + (PI/2))*walkDistance);
				
				// ak sa na konci presunu budu finalne suradnice nachadzat v radiuse agenta, tak sa ignoruju prekazky, ktore
				// lezia na tychto suradniciach = agent sa musi dostat na furadnice aj keby na nich bol iny agent
				boolean isEnd = false;
				if(new Ellipse2D.Double(newX, newY, Obstacles.ROBOT_RADIUS, Obstacles.ROBOT_RADIUS).contains(finalPosition.getX(), finalPosition.getY())){
					isEnd = true;
				}
				
				// kontrola, ci sa na trajektorii nenachadza prekazka
				Vector3D check = Obstacles.checkIntersection(actualPos, Vector3D.cartesian(newX, newY, 0), obstacles, isEnd);
				// ak nie, tak sa vytvori trajektoria ciastocneho presunu
				if(check == null){
					// pridanie vypocitanych rotacii do trajektorie
					t.addList(moves);
					
					// pridanie chodze
					while(walkNum > 0){
						t.addMove(stdWalk);
						walkNum--;
					}
					
					// nastavenie posledneho pohybu v trajektorii
					this.lastMove = stdWalk;
					// nastavenieaktualnych suradnic na konci ciastocnej trajektorie
					this.actualPos = Vector3D.cartesian(newX, newY, 0);
					// nastavenie aktualneho natocenia na konci ciastocnej trajektorie
					this.actualPhi = rot;
					
					// rekurzivne volanie planovania od konca ciastocnej trajektorie ku koncovym suradniciam
					plan(t);
				}
				else{
					// pokial sa vo vypocitanej trajektorii nachdza prekazka, tak
					// sa vypocita bod obchadzania tejto pprekazky
					Vector3D bypassPoint = findBypassPoint(actualPos, rot, check);
					
					// pokial sa takyto bod najde, tak sa vypocita trajktoria presunu k tomuto bodu
					if(bypassPoint != null){
						// presun na miesto obchadzania prebieha rovnako ako v prvej casti tohto algoritmu
						// najprv sa vypocita vektor k bodu obchadzania
						Vector3D bypassVector = bypassPoint.subtract(actualPos);
						double bypassPhi = bypassVector.getPhi();
						double bypassDistance = bypassVector.getR();
						// vypocita sa uhol, o ktory sa ma agent otocit
						double bypassAngle = bypassPhi - actualPhi;
						if(bypassAngle < -PI){
							bypassAngle = 2*PI + bypassAngle;
						}
						if(bypassAngle > PI){
							bypassAngle = bypassAngle - 2*PI;
						}
						// vypocet trajektorie otacania
						if(Math.toDegrees(abs(bypassAngle)) > t.getDeviation()){
							bypassAngle = rotate(bypassAngle, t.getDeviation());
						}
						t.addList(moves);
						// presun na miesto obchadzania
						// smer chodze sa nekoriguje, pretoze miesto obchadzania je len priblizne
						walk(bypassDistance);
						// vypocet presneho miesta, kam sa agent pri obchadzani dostane
						double bypassX = actualPos.getX() + (Math.cos(bypassPhi-bypassAngle + (PI/2))*getDistance());
						double bypassY = actualPos.getY() + (Math.sin(bypassPhi-bypassAngle + (PI/2))*getDistance());
						t.addList(moves);
						// miesto obchadzania sa stane aktualnym miestom
						this.actualPos = Vector3D.cartesian(bypassX, bypassY, 0);
						this.actualPhi = bypassPhi-bypassAngle;
						System.out.println("BYPASSING[" + bypassX + "; " + bypassY + "; 0] phi = " + (bypassPhi-bypassAngle));
						plan(t);
					}
					else{
						// ak je agent obklopeny prekazkami, tak nemoze vypocitat ani trajektoriu
						System.out.println("Cannot bypass !!!");
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * Adjusts the angle and calls rotateQueue() method. 
	 *
	 * @param angle to rotate
	 * @param acceptable deviation
	 * @return angle deviation 
	 */
	private double rotate(double angle, double deviation){
		ArrayList<Annotation> list = new ArrayList<Annotation>();
		
		//uhol na vstupe je v radianoch a uhly anotacii su v stupnoch
		angle=Math.toDegrees(angle);
		//rotacia v anotaciach ma opacne znamienko
		angle = angle * (-1);
		
		// vola sa vypocet samotnej trajektorie otacania
		angle = rotateQueue(angle, list, deviation);
		
		if(list.size() > 0){
			this.lastMove = list.get(list.size()-1);
		}
		this.moves = new LinkedList<Annotation>(list);
		
		return Math.toRadians(angle);

	}
	
	/**
	 * 
	 * Calculates the trajectory of walking. Creates queue of 'walk' moves. 
	 *
	 * @param distance
	 */
	private void walk(double distance){
		ArrayList<Annotation> list = new ArrayList<Annotation>();
		double stdMove = stdWalk.getMove().getX().getAvg();
		
		// kym je neprejdena vzdialenost vacsia ako dlzka standarneho kroku, tak sa do trajektorie pridava standardny krok
		while(distance > stdMove){
			list.add(stdWalk);
			// neprejdena vzdialenost sa skrati o dlzku standardneho kroku
			distance -= stdMove;
		}
		
		// hlada sa krok, po ktoreho vykonani bude neprejdena vzdialenost v ramci prijatelnej odchylky
		// skusaju sa vsetky kroky v zozname 'walk', v poradi od najvacsieho k najmensiemu
		while(abs(distance) > DISTANCE_DEVIATION){
			int i = 0;
			// urci sa prepokladana neprejdena vzdialenost po vykonani prveho kroku zoznamu (moze byt aj zaporna, zaujima nas absolutna hodnota)
			double possibleDistance = distance - this.walk.get(0).getMove().getX().getAvg();
			// dalej sa hlada, ci po vykonani inych krokov nebude neprejdena vzdialenost mensia (hlada sa vhodnejsi krok)
			while((i+1<this.walk.size()) && (abs(distance - this.walk.get(i+1).getMove().getX().getAvg()) < abs(possibleDistance))){
				possibleDistance = distance - this.walk.get(i+1).getMove().getX().getAvg();
				i++;
			}
			
			// overi sa, ci vybrany krok moze nadvazovat na uz vypocitanu trajektoriu v tejto metode
			if(list.size() != 0){
				if(MoveValidator.sequenceCheck(list.get(list.size()-1), this.walk.get(i)) == true){
					list.add(this.walk.get(i));
				}
				else{
					//TODO: premysliet, ako sa bude riesit zla nadvaznost pohybov
					// docasne riesenie
					i++;
					while(MoveValidator.sequenceCheck(list.get(list.size()-1), this.walk.get(i))){
						i++;
					}
					list.add(this.walk.get(i));
				}
			}
			// ak je toto prvy krok v trajetorii vypocitanej v tejto metode, tak sa overi
			// nadvaznost na posledny krok celkovej pocitanej trajektorie
			else{
				if(this.lastMove != null){
					if(MoveValidator.sequenceCheck(this.lastMove, this.walk.get(i)) == true){
						list.add(this.walk.get(i));
					}
					else{
						//TODO: premysliet, ako sa bude riesit zla nadvaznost pohybov
						i++;
						while(MoveValidator.sequenceCheck(this.lastMove, this.walk.get(i))){
							i++;
						}
						list.add(this.walk.get(i));
					}
				}
				// ak je toto prvy krok v celkovej trajektorii , tak sa jednoducho prida
				else{
					list.add(this.walk.get(i));
				}
			}
			// neprejdena vzdialenost sa skrati o tento krok
			distance = possibleDistance;
		}
		
		// po vypocte trajektorie presunu sa urci posledny krok v celkovej trajektorii
		// a trajektoria sa ulozi do zasubniku priebeznej vypocitanej subtrajektorie
		if(list.size() != 0){
			this.lastMove = list.get(list.size()-1);
			this.moves = new LinkedList<Annotation>(list);
		}
	}
	
	/**
	 * 
	 * Calculates the trajectory of rotating. Creates queue of 'rotate' moves. 
	 *
	 * @param distance
	 */
	private double rotateQueue(double angle, ArrayList<Annotation> list, double deviation){	
		// vypocet trajektorie otacania prebieha pokym nie je uhlova odchylka mensia ako povolena odchylka
		while(abs(angle) > deviation){
//			if(angle > 0){
				int i = 0;
				// hlada sa rotacny pohyb, po ktoreho vykonani bude zvyskova rotacia co najmensia
				double possibleAngle = angle - this.rotation.get(0).getRotation().getZ().getAvg();
				while((i+1<this.rotation.size()) && (abs(angle - this.rotation.get(i+1).getRotation().getZ().getAvg())<abs(possibleAngle))){
					possibleAngle = angle - this.rotation.get(i+1).getRotation().getZ().getAvg();
					i++;
				}
				
				// overi sa nadvaznost pohybu na posledne vykonane pohyby rovnako ako v metode walk()
				if(list.size() != 0){
					if(MoveValidator.sequenceCheck(list.get(list.size()-1), this.rotation.get(i)) == true){
						list.add(this.rotation.get(i));
					}
					else{
						//TODO: premysliet, ako sa bude riesit zla nadvaznost pohybov
						// docasne riesenie
						i++;
						while(MoveValidator.sequenceCheck(list.get(list.size()-1), this.rotation.get(i))){
							i++;
						}
						list.add(this.rotation.get(i));
					}
				}
				else{
					if(this.lastMove != null){
						if(MoveValidator.sequenceCheck(this.lastMove, this.rotation.get(i)) == true){
							list.add(this.rotation.get(i));
						}
						else{
							//TODO: premysliet, ako sa bude riesit zla nadvaznost pohybov
							i++;
							while(MoveValidator.sequenceCheck(this.lastMove, this.rotation.get(i))){
								i++;
							}
							list.add(this.rotation.get(i));
						}
					}
					else{
						list.add(this.rotation.get(i));
					}
				}
				angle = possibleAngle;
//			}
//			else{
//				int i = this.rotation.size()-1;
//			}
		}
		
		return angle;
	}
	
	/**
	 * 
	 * Sums the move durations of all trajectories and returns the one with shortest duration. 
	 *
	 * @return trajectory
	 */
	public Trajectory pickBestTrajectory(){
		Trajectory fastest = null;
		for(Trajectory t : trajectoryPlans){
			if(fastest == null){
				fastest = t;
			}
			else{
				// pri vybere najlepsej trajetorie sa neberu do uvahy trajektorie s nulovym casom
				// tie vznikli nemoznostou obyst prekazku
				if((fastest.getTime() > t.getTime()) && (t.getTime() != 0)){
					fastest = t;
				}
			}
		}
		
		return fastest;
	}
	
	/**
	 * 
	 * Returns the fastest calculated trajectory. 
	 *
	 * @return trajectory
	 */
	public Trajectory getTrajectory(){
		return trajectory;
	}
	
	/**
	 * 
	 * Sums the walk length of all moves of operational trajectory returning distance to by walked. 
	 *
	 * @return distance of subtrajectory
	 */
	public double getDistance(){
		double distance = 0;
		for(Annotation a : moves){
			distance += a.getMove().getX().getAvg();
		}
		return distance;
	}
	
	/**
	 * 
	 * Finds bypass point of given obstacle considering also other obstacles.
	 *
	 * @param position of agent
	 * @param rotation of agent facing obstacle
	 * @param obstacle
	 * @return
	 */
	private Vector3D findBypassPoint(Vector3D position, double rotation, Vector3D obstacle){
		Vector3D bypassPoint = null;
		HashSet<Vector3D> consideredObstacles = new HashSet<Vector3D>();
		LinkedList<Vector3D> uncheckedObstacles = new LinkedList<Vector3D>();
		boolean ok = true;
		
		// hladanie obchadzania sa vykonava, pokym nie je najdeny bod obchodania, alebo 
		// neboli skontrolovane vsetky prekazky a aj tak nebol najdeny bod obchadzania
		while(ok == true){
			// prekazka je oznacena ako uvazovana aby sa vyradila z cyklov vypoctu
			consideredObstacles.add(obstacle);
			
			// najde sa bod obchadzania prekazky zlava
			Vector3D bp1 = Obstacles.findLeftBypassPoint(rotation + (PI/2), obstacle);
			// overi sa, ci sa na trajektorii k bodu obchadzania nenachadza prekazka
			Vector3D o1 = Obstacles.checkIntersection(position, bp1, obstacles, false);
			// ak sa nenachadza, tak sa overi, ci bod obchadzania nelezi  mimo ihriska
			// a ak nie, tak sa tento bod vrati ako zvoleny bod obchadzania
			if(o1 == null){
				if(Obstacles.checkInField(bp1) == true){
					bypassPoint = bp1;
					break;
				}
			}
			// ak sa na trajektorii k bodu obchadzania nachadza dalsia prekazka, tak sa
			// tato nova prekazka oznaci ako nepreskumana a v dalsich vypoctoch sa ju budeme snazit obist
			else{
				if(consideredObstacles.contains(o1) == false){
					uncheckedObstacles.add(o1);
				}
			}
			
			// vypocety sa opakuju, ale teraz sa hlada bod obchadzania sprava
			Vector3D bp2 = Obstacles.findRightBypassPoint(rotation + (PI/2), obstacle);
			Vector3D o2 = Obstacles.checkIntersection(position, bp2, obstacles, false);
			if(o2 == null){
				if(Obstacles.checkInField(bp2) == true){
					bypassPoint = bp2;
					break;
				}
			}
			else{
				if(consideredObstacles.contains(o2) == false){
					uncheckedObstacles.add(o2);
				}
			}
			
			// ak nebol najdeny bod obchadzania a uz nie su k dispozicii obchadzatelne prekazky, tak
			// vypocet vrati prazny bod obchadzania
			if(uncheckedObstacles.isEmpty() == true){
				ok = false;
			}
			// ak su  este prekazky , ktore sa neuvazovali pri obchadzani, tak sa
			// v dalsom cykle vyberie prva prekazka zo zoznamu a odstrani sa z tohto zoznamu
			// taktiez sa upravi rotacia agenta aby bol otoceny na tuto prekazku pre potreby
			// zistovania bodov obchadzania
			else{
				obstacle = uncheckedObstacles.getFirst();
				uncheckedObstacles.removeFirst();
				rotation = obstacle.subtract(position).getPhi();
			}
		}
		
		return bypassPoint;
	}
}
