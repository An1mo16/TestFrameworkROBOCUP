/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.fiit.testframework.trainer.testsuite;

/**
 *
 * @author relation
 */
public class TestCaseResult {

    private double fitness;

    public TestCaseResult(){}
    
    public TestCaseResult(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        return String.valueOf(fitness);
    }

}
