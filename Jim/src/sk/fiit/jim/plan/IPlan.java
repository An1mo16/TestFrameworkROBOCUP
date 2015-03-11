package sk.fiit.jim.plan;

public interface IPlan {
	public void run();
	public void control();
	public void replan();
	public boolean turned_to_goal();
	public boolean ball_front();
	public boolean ball_back();
	public boolean is_ball_mine();
	public boolean straight();
	public boolean near_ball();
	public double ball_unseen();
	
}
