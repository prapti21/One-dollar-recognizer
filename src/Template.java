package dollar;

import java.util.ArrayList;

public class Template {
	String name;
	ArrayList<Point> points;
	
	public Template(String name, ArrayList<Point> points) {
		if(name.length()==0 && points.size() == 0) {
			return;
		}
		this.name = name;
		this.points = Recognition.resampling(points, Board.N);
		double x = 0.0;
		double y = 0.0;
		for(int i=0; i<points.size(); i++) {
			Point pt = points.get(i);
			x +=pt.x;
			y += pt.y;
		}
		double qx = x / (double)points.size();
		double qy = y / (double)points.size();
		
		Point central = new Point(qx, qy);
		Point p = points.get(0);
		double angleValue = Math.atan2(central.y - p.y, central.x - p.x);
		this.points = Recognition.rotation(this.points, -1.0*angleValue);
		this.points = Recognition.scaling(this.points, Board.bound);
		this.points = Recognition.translation(this.points, new Point(0,0));
		
	}
}
