package dollar;

import java.util.ArrayList;
import java.util.List;
/*To resample, rotate, scale, transform, recognize and calculate the score */
public class Recognition {
	
	public static Template recognizingFile(String user_name, List<Template> randomTemplates, Template c) {
		double best_value = Double.MAX_VALUE;
		int index_template = Integer.MIN_VALUE;
		int i=0;
		int template_size = randomTemplates.size();
		Template template_min = null;
		while(i < template_size) {
			double angle_length = 0.0;
			Template random_temp = randomTemplates.get(i);
			ArrayList<Point> points = random_temp.points;
			angle_length = supporter(c.points, -1.0*Board.theta, Board.theta, Board.dtheta, random_temp);
			if(angle_length < best_value) {
				best_value = angle_length;
				index_template = i;
			}
			i++;
		}
		template_min = index_template == Integer.MIN_VALUE ? c : randomTemplates.get(index_template);

		return template_min;
		
	}
	/*To calculate the score and returns the best_value matched score */
	public static Score scoreFinder(String username, List<Template> templates, ArrayList<Point> points) {
		double best_value = Double.MAX_VALUE;
		int index_template = Integer.MIN_VALUE;
		int i=0;
		int template_size = templates.size();
		while(i < template_size) {
			double angle_length = 0.0;
			angle_length = supporter(points, -1.0*Board.theta, Board.theta, Board.dtheta, templates.get(i));
			if(angle_length < best_value) {
				best_value = angle_length;
				index_template = i;
			}
			i++;
		}
		if(index_template == Integer.MIN_VALUE) {
			return new Score("Not properly recognizable", 0.0);
		}
		double score_value = (1.0 - best_value) / Board.half;
		return new Score(templates.get(index_template).name, score_value*10);
	}
	

	/*To perform resampling of the points */
	public static ArrayList<Point> resampling(ArrayList<Point> points, int max){
		double distance_value = 0.0;
		for(int i=1; i<points.size(); i++) {
			Point p = points.get(i-1);
			Point q = points.get(i);
			double d_x = q.x - p.x;
			double d_y = q.y - p.y;
			distance_value += Math.sqrt(d_x*d_x + d_y*d_y);
		}
		double t = distance_value;
		double index = t / (double)(max-1);
		double best_value = 0.0;
		ArrayList<Point> pts2 = new ArrayList<>();
		Point pt = points.get(0);
		pts2.add(pt);
		int i=1;
		while(i < points.size()) {
			Point p = points.get(i-1);
			Point r = points.get(i);
			double d_x = r.x - p.x;
			double d_y = r.y - p.y;
			double d =  Math.sqrt(d_x*d_x + d_y*d_y);
			if((best_value + d) >= index) {
				double x = 0.0;
				double y = 0.0;
				x = (double)p.x + ((index-best_value)/d) * (r.x - p.x);
				y = (double)p.y + ((index-best_value)/d) * (r.y - p.y);
				Point q = new Point(x, y);
				pts2.add(q);
				points.add(i, q);
				best_value = 0.0;
			}
			else {
				best_value += d;
			}
			i++;
		}
		if(pts2.size() == max-1) {
			pts2.add(points.get(points.size()-1));
			
		}
		return pts2;
	}
	/*To perform rotation of the points */
	public static ArrayList<Point> rotation(ArrayList<Point> points, double d_x){
		double x = 0.0;
		double y = 0.0;
		for(int i=0; i<points.size(); i++) {
			Point pt = points.get(i);
			x +=pt.x;
			y += pt.y;
		}
		double qx = x / (double)points.size();
		double qy = y / (double)points.size();
		
		Point center = new Point(qx, qy);
		double cosine_Value = Math.cos(d_x);
		double sine_Value = Math.sin(d_x);
		ArrayList<Point> pts2 = new ArrayList<>();
		
		int i=0;
		while( i < points.size()) {
			double x_2 = 0.0;
			double y_2 = 0.0;
			Point pnt = points.get(i);
			x_2 = (pnt.x - center.x) * cosine_Value - (pnt.y - center.y) * sine_Value + center.x;
			y_2 = (pnt.x - center.x) * sine_Value + (pnt.y - center.y) * cosine_Value + center.y;
			Point p2 = new Point(x_2,y_2);
			pts2.add(p2);
			i++;
		}
		return pts2;
	}
	/*To perform scaling of the points */
	public static ArrayList<Point> scaling(ArrayList<Point> points, double max) {
		double x_1 = Double.MAX_VALUE, x_2 = Double.MIN_VALUE, y_1 = Double.MAX_VALUE, y_2 = Double.MIN_VALUE;
		for (int i = 0; i < points.size(); i++) {
			x_1 = Math.min(x_1, points.get(i).x);
			y_1 = Math.min(y_1, points.get(i).y);
			x_2 = Math.max(x_2, points.get(i).x);
			y_2 = Math.max(y_2, points.get(i).y);
		}
		double width = 0.0;
		double height = 0.0;
		width= x_2 - x_1;
		height = y_2 - y_1;
		ArrayList<Point> pts2 = new ArrayList<>();
		int i=0;
		while(i < points.size()) {
			Point p = points.get(i);
			double x = 0.0;
			double y = 0.0;
			x = (double)p.x * (max / width);
			y = (double)p.y * (max / height);
			Point pt = new Point(x,y);
			pts2.add(pt);
			i++;
		}
		return pts2;
	}
	/*To translate the points */
	public static ArrayList<Point> translation(ArrayList<Point> points, Point pt){
		double x = 0.0;
		double y = 0.0;
		for(int i=0; i<points.size(); i++) {
			Point p = points.get(i);
			x +=p.x;
			y += p.y;
		}
		double qx = x / (double)points.size();
		double qy = y / (double)points.size();
		
		Point center = new Point(qx, qy);
		ArrayList<Point> pts2 = new ArrayList<>();
		int i=0;
		while(i < points.size()) {
			Point pnt2 = points.get(i);
			double x_1 = pnt2.x + pt.x - center.x;
			double y_1 =pnt2.y + pt.y - center.y;
			Point pt3 = new Point(x_1, y_1);
			pts2.add(pt3);
			i++;
		}
		return pts2;
	}
	/*To calculate the angle_length we use the supporter method */
	public static double supporter(ArrayList<Point> points, double value1, double value2, double val3, Template temp) {
		double x_1 = Board.phi * value1 + (1.0 - Board.phi) * value2;
		ArrayList<Point> pts2 = new ArrayList<>();
		pts2 = rotation(points, x_1);
		double d = 0.0;
		for(int i=0; i<pts2.size(); i++) {
			Point p = pts2.get(i);
			Point q = temp.points.get(i);
			double d_x = q.x - p.x;
			double d_y = q.y - p.y;
			d += Math.sqrt(d_x*d_x + d_y*d_y);
		}
		double v_1 = d / (double)pts2.size();
		double x_2 = (1.0 - Board.phi) * value1 + Board.phi * value2;
		pts2 = rotation(points, x_2);
		d = 0.0;
		for(int i=0; i<pts2.size(); i++) {
			Point p = pts2.get(i);
			Point q = temp.points.get(i);
			double d_x = q.x - p.x;
			double d_y = q.y - p.y;
			d += Math.sqrt(d_x*d_x + d_y*d_y);
		}
		double v_2= d / (double)pts2.size();
		while (Math.abs(value2- value1) > val3)
		{
			if (v_1 < v_2) {
				value2 = x_2;
				x_2 = x_1;
				v_2 = v_1;
				x_1 = Board.phi * value1 + (1.0 - Board.phi) * value2;
				pts2 = rotation(points, x_1);
				d = 0.0;
				for(int i=0; i<pts2.size(); i++) {
					Point p = pts2.get(i);
					Point q = temp.points.get(i);
					double d_x = q.x - p.x;
					double d_y = q.y - p.y;
					d += Math.sqrt(d_x*d_x + d_y*d_y);
				}
				v_1 = d / (double)pts2.size();
			} else {
				value1 = x_1;
				x_1 = x_2;
				v_1 = v_2;
				x_2 = (1.0 - Board.phi) * value1 + Board.phi * value2;
				pts2 = rotation(pts2, x_2);
				d = 0.0;
				for(int i=0; i<pts2.size(); i++) {
					Point p = pts2.get(i);
					Point q = temp.points.get(i);
					double d_x = q.x - p.x;
					double d_y = q.y - p.y;
					d += Math.sqrt(d_x*d_x + d_y*d_y);
				}
				v_2 = d / (double)pts2.size();
			}
		}
		return Math.min(v_1, v_2);
	}
	
	
}
