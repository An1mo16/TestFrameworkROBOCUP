package sk.fiit.jim;

import java.util.List;

import sk.fiit.jim.agent.Planner;
/**
 *  ExecutionTimeMonitor.aj
 *  
 *  Frees {@link Communication} class from managing performance measurements
 *  necessary to decide whether a major garbage collect can be launched.
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
public aspect ExecutionTimeMonitor{
	public pointcut loopStart():
		within(sk.fiit.jim.agent.communication.Communication) && target(sk.fiit.jim.agent.parsing.Parser) && call(* parse(String));
	
	public pointcut messageSending():
		within(sk.fiit.jim.agent.communication.Communication) && withincode(private void mainLoop()) && call(* transmit(String));
	
	before(): loopStart(){
		Planner.loopStart = System.nanoTime();
	}
	
	after() : messageSending(){
		Planner.runGarbageCollectIfEnoughTime();
	}
}