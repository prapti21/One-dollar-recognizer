package dollar;

public class Score {
	String name;
	double score;
	
	public Score(String name, double score) {
		this.name = name;
		this.score = score;
	}
	
	@Override
	public String toString(){
		return "Recognized: "+ name + "; Score: " + (1-score);
	}
}
