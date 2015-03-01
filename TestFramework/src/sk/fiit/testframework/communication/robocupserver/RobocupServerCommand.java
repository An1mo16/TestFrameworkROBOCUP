/**
 * Name:    TrainerCommand.java
 * Created: Feb 27, 2011
 * 
 * @author: relation
 */
package sk.fiit.testframework.communication.robocupserver;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import sk.fiit.robocup.library.annotations.TestCovered;
import sk.fiit.robocup.library.geometry.Point3D;

/**
 * Interface with aim to bridge
 * @author    relation
 */
@TestCovered
public abstract class RobocupServerCommand {

    /**
     *
     * This command start the soccer game, tossing a coin to select the team
     * that kicks off first.
     *
     * @author relation
     *
     */
    public static class KickOff extends RobocupServerCommand {

        /*
         * (non-Javadoc)
         *
         * @see sk.fiit.testframework.trainer.TrainerCommand#getCommand()
         */
        @Override
        public String getCommand() {
            return "(kickOff Left)";
        }
    }

    /**
	 * This command sets the current play mode. Possible play modes are given as strings in the play modes expression of the init expression the monitor receives when it connects. TODO: Check if @see   {@link sk.fiit.testframework.parsing.models.PlayMode}    corresponds to real play modes. Otherwise override their toString() method.
	 * @author    relation
	 */
    public static class PlayMode extends RobocupServerCommand {

        private sk.fiit.testframework.parsing.models.PlayMode playMode;

        public PlayMode(sk.fiit.testframework.parsing.models.PlayMode playMode) {
            this.playMode = playMode;
        }

        /*
         * (non-Javadoc)
         *
         * @see sk.fiit.testframework.trainer.TrainerCommand#getCommand()
         */
        @Override
        public String getCommand() {
            return String.format(Locale.US, "(playMode %s)", playMode);
        }
    }

    /**
	 * TODO: Replace with a brief purpose of class / interface.
	 * @author    ivan
	 */
    public static class BallPosition extends RobocupServerCommand {

        private Position position;

        public BallPosition(Point3D position) {
            this.position = new Position(position);
        }

        /*
         * (non-Javadoc)
         *
         * @see sk.fiit.testframework.trainer.TrainerCommand#getCommand()
         */
        @Override
        public String getCommand() {
            return String.format(Locale.US, "(ball %s)", position.getCommand());
        }
    }

    /**
	 * TODO: Replace with a brief purpose of class / interface.
	 * @author    ivan
	 */
    public static class BallVelocity extends RobocupServerCommand {

        private Velocity velocity;

        public BallVelocity(Point3D velocity) {
            this.velocity = new Velocity(velocity);
        }

        /*
         * (non-Javadoc)
         *
         * @see sk.fiit.testframework.trainer.TrainerCommand#getCommand()
         */
        @Override
        public String getCommand() {
            return String.format(Locale.US, "(ball %s)", velocity.getCommand());
        }
    }

    /**
	 * TODO: Replace with a brief purpose of class / interface.
	 * @author    ivan
	 */
    public static class Ball extends RobocupServerCommand {

  
        private Position position;
        private Velocity velocity;

        public Ball(Point3D position, Point3D velocity) {
            this.position = new Position(position);
            this.velocity = new Velocity(velocity);
        }

        @Override
        public String getCommand() {
            return String.format(Locale.US, "(ball %s%s)", position.getCommand(), velocity.getCommand());
        }
    }

    /**
     * Command used to drop ball at its current position and move all players
     * away by the free kick radius.
     *
     * @author relation
     */
    public static class DropBall extends RobocupServerCommand {

        /*
         * (non-Javadoc)
         *
         * @see sk.fiit.testframework.trainer.TrainerCommand#getCommand()
         */
        @Override
        public String getCommand() {
            return "(dropBall)";
        }
    }

    /**
	 * This expression sets the position and velocity of the given player on the field.
	 * @author    relation
	 */
    public static class Agent extends RobocupServerCommand {

        private Position position;
        private Velocity velocity;
        private String team;
        private int uniformNumber;

        public Agent(Point3D position, Point3D velocity, String team, int uniformNumber) {
            super();
            this.position = new Position(position);
            this.velocity = new Velocity(velocity);
            this.team = team;
            this.uniformNumber = uniformNumber;
        }

        /*
         * (non-Javadoc)
         *
         * @see sk.fiit.testframework.trainer.TrainerCommand#getCommand()
         */
        @Override
        public String getCommand() {
            return String.format(Locale.US, "(agent (team %1$s)(unum %2$d)%3$s)",
                    team, uniformNumber, position.getCommand());
        }
    }

    /**
	 * Generic command used mainly for debugging purposes.
	 * @author    relation
	 */
    public static class Generic extends RobocupServerCommand {

        private String command;

        public Generic(String command) {
            super();
            this.command = command;
        }

        /* (non-Javadoc)
         * @see sk.fiit.testframework.trainer.TrainerCommand#getCommand()
         */
        /**
		 * TODO: Replace with a brief purpose of method.
		 * @return
		 */
        @Override
        public String getCommand() {
            return command;
        }
    }

    /*
     * Non-public
     */
    /**
	 * TODO: Replace with a brief purpose of class / interface.
	 * @author    ivan
	 */
    static class Position extends RobocupServerCommand {

        private Point3D point3d;

        public Position(Point3D point3d) {
            super();
            this.point3d = point3d;
        }

        /*
         * (non-Javadoc)
         *
         * @see sk.fiit.testframework.trainer.TrainerCommand#getCommand()
         */
        @Override
        public String getCommand() {
            return String.format(Locale.US, "(pos %1$f %2$f %3$f)", point3d.x, point3d.y,
                    point3d.z);
        }
    }

    /**
	 * TODO: Replace with a brief purpose of class / interface.
	 * @author    ivan
	 */
    static class Velocity extends RobocupServerCommand {

        private Point3D velocity3d;

        public Velocity(Point3D velocity3d) {
            super();
            this.velocity3d = velocity3d;
        }

        /*
         * (non-Javadoc)
         *
         * @see sk.fiit.testframework.trainer.TrainerCommand#getCommand()
         */
        @Override
        public String getCommand() {
            return String.format(Locale.US, "(vel %1$f %2$f %3$f)", velocity3d.x,
                    velocity3d.y, velocity3d.z);
        }
    }

    public byte[] getAsBytes() {
        byte[] byteCommand = null;

        try {
            byteCommand = getCommand().getBytes("ASCII");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("This exception couldn't happen."
                    + ex.getMessage());
        }

        int commandLength = byteCommand.length;
        byte alignedCommand[] = new byte[byteCommand.length + 4];
        System.arraycopy(byteCommand, 0, alignedCommand, 4, byteCommand.length);
        alignedCommand[0] = (byte) (commandLength >> 24);
        alignedCommand[1] = (byte) ((commandLength << 8) >> 24);
        alignedCommand[2] = (byte) ((commandLength << 16) >> 24);
        alignedCommand[3] = (byte) ((commandLength << 24) >> 24);
        return alignedCommand;
    }

    public abstract String getCommand();
}
