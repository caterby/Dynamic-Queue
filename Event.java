/*Jie Liu Netid:jxl164830 */
public class Event {
	   public double time;
	   public int type;
	   public int priority;
	   public Event next;
	   Event(){};
	   public Event(double x, int y, int p){
		   this.time=x;
		   this.type=y;
		   this.priority=p;
	   }
	}
