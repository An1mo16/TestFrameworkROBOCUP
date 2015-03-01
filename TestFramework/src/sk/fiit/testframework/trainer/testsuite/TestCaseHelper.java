/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.fiit.testframework.trainer.testsuite;

import sk.fiit.robocup.library.annotations.UnderConstruction;
import sk.fiit.robocup.library.geometry.Point3D;
import sk.fiit.robocup.library.geometry.Vector3;

/**
 *
 * @author relation
 */
public class TestCaseHelper {

    public static Point3D getBaseGoaliePosition() {
        return new Point3D(-5, 0, 0.4);
    }

    public static Point3D getBaseBallPosInGoalieKick()  {
        return new Point3D(-2, 0, 0.2);
    }

    @UnderConstruction
    public static boolean isBallInGoal(Vector3 ballPos) {
        return true;
    }

}
