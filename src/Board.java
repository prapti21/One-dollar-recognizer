package dollar;

        import java.awt.BasicStroke;
	import java.awt.Color;
	import java.awt.Dimension;
	import java.awt.Graphics;
	import java.awt.Graphics2D;
	import java.awt.Shape;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.awt.event.MouseAdapter;
	import java.awt.event.MouseEvent;
	import java.awt.geom.Path2D;
	import java.io.BufferedWriter;
	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintWriter;
        import static java.lang.System.exit;
	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.Collections;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Random;
        import java.util.Scanner;
        import java.util.TreeMap;

        import javax.swing.JButton;
	import javax.swing.JComponent;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JOptionPane;
	import javax.swing.JPanel;
	import javax.swing.JTextField;
	import javax.swing.SwingUtilities;
        import javax.xml.XMLConstants;
        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;

        import org.w3c.dom.Document;
        import org.w3c.dom.Element;
        import org.w3c.dom.Node;
        import org.w3c.dom.NodeList;


	public class Board extends JPanel {


      private static ArrayList<Point> xy_coordinatesTemp = new ArrayList<>();


	    public static double theta = Math.toRadians(45.0);
	    public static double dtheta = Math.toRadians(2.0);
	    public static double bound = 250.0;
	    public static int N = 64;

	    public static double dgonal = Math.sqrt(Math.pow(bound, 2) + Math.pow(bound, 2));
	    public static double half = 0.5*dgonal;
	    public static double phi = 0.5 * (Math.sqrt(5.0)-1.0);

	    private static ArrayList<Point> xy_coordinates = new ArrayList<Point>();
	    private static ArrayList<Point> initial = new ArrayList<>();
	    public static ArrayList<Template> models = new ArrayList<Template>();
	    public static ArrayList<String> model_name = new ArrayList<>();
	    public static ArrayList<Integer> txy_coordinates = new ArrayList<>();
	    public static ArrayList<String> properties = new ArrayList<>();
	    //public static ArrayList<Score> scoreList = new ArrayList<>();
	    public static HashMap<String, List<Template>> data_set = new HashMap<>();
	    public static TreeMap<String, List<String>> recordofUsers = new TreeMap<>();
	    public static ArrayList<String> usernameList = new ArrayList<>();
	    public static TreeMap<String, Double> accuracy_rate = new TreeMap<>();
	    public static ArrayList<Template> rand_records;
	    public static ArrayList<Template> prototypeList = new ArrayList<>();
	    public static HashMap<Template, List<Template>> prototypes = new HashMap<>();
	    public static Template protype = null;
	    public static Template beginning = null;

	    public static ArrayList<Template> modelset = new ArrayList<>();


	    private static File directory;


	    public Board() {

	    }


	    public static ArrayList<Point> preRecognize(ArrayList<Point> xy_coordinates){
			ArrayList<Point> newxy_coordinates = xy_coordinates;
			newxy_coordinates = Recognition.resampling(newxy_coordinates, N);
			double x = 0.0;
			double y = 0.0;
			for(int i=0; i<xy_coordinates.size(); i++) {
				Point pt = xy_coordinates.get(i);
				x +=pt.x;
				y += pt.y;
			}
			double qx = x / (double)xy_coordinates.size();
			double qy = y / (double)xy_coordinates.size();

			Point central = new Point(qx, qy);
			Point p = xy_coordinates.get(0);
			double angleValue = Math.atan2(central.y - p.y, central.x - p.x);
			newxy_coordinates = Recognition.rotation(newxy_coordinates, -1.0*angleValue);
			newxy_coordinates = Recognition.scaling(newxy_coordinates, bound);
			newxy_coordinates = Recognition.translation(newxy_coordinates, new Point(0,0));
			return newxy_coordinates;
		}


	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {

					/* To read the data from the xml_log files downloaded in our project folder */
	                Board area = new Board();
	                directory = new File("s02/medium");
	                recordofUsers.put("s02", new ArrayList<String>());
	                readmodelsFile("s02", directory);
	                directory = new File("s03/medium");
	                recordofUsers.put("s03", new ArrayList<String>());
	                readmodelsFile("s03", directory);
	                directory = new File("s04/medium");
	                recordofUsers.put("s04", new ArrayList<String>());
	                readmodelsFile("s04",directory);
	                directory = new File("s05/medium");
	                recordofUsers.put("s05", new ArrayList<String>());
	                readmodelsFile("s05",directory);
	                directory = new File("s06/medium");
	                recordofUsers.put("s06", new ArrayList<String>());
	                readmodelsFile("s06",directory);
	                directory = new File("s07/medium");
	                recordofUsers.put("s01", new ArrayList<String>());
	                readmodelsFile("s01",directory);
	                /*directory = new File("s08/medium");
	                recordofUsers.put("s08", new ArrayList<String>());
	                readmodelsFile("s08",directory);
	                directory = new File("s09/medium");
	                recordofUsers.put("s09", new ArrayList<String>());
	                readmodelsFile("s09",directory);
	                directory = new File("s10/medium");
	                recordofUsers.put("s10", new ArrayList<String>());
	                readmodelsFile("s10",directory);
	                directory = new File("s11/medium");
	                recordofUsers.put("s11", new ArrayList<String>());
	                readmodelsFile("s11",directory);*/

				//Names of templates
		    		model_name.add("arrow");
		    		model_name.add("caret");
		    		model_name.add("check");
		    		model_name.add("circle");
		    		model_name.add("delete");
		    		model_name.add("left curly braces");
		    		model_name.add("left square braces");
		    		model_name.add("pig tail");
		    		model_name.add("zig-zag");
		    		model_name.add("rectangle");
		    		model_name.add("right curly braces");
		    		model_name.add("right square braces");
		    		model_name.add("star");
		    		model_name.add("triangle");
		    		model_name.add("v");
		    		model_name.add("x");


		    		try {
						FileWriter scripter = new FileWriter("Output.txt");
						BufferedWriter bfr_object = new BufferedWriter(scripter);
						//To print the format of the output file 
						bfr_object.write("Recognition Log: Sadhvi Thula Prapti Akolkar// $1 // xml_logs // USER-DEPENDENT-RANDOM-100");
						bfr_object.newLine();
						bfr_object.write("User  GestureType  RandomIteration  #ofTrainingExamples  TotalSizeOfTrainingSet   TrainingSetContents   Candidate RecoResulttypeOfGesture correctIncorrect    BestScore    BestLabel  N or 50 Best list");
                                                bfr_object.newLine();
						loopDataset(bfr_object);
						bfr_object.newLine();
						bfr_object.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

		    		System.out.println("Finished");

	            }
	        });
	    }

			//function to pick randon templates for training set
	    public static void pickRandModels(String user, int idx, int e, List<Template> Emodels){
	    	List<Template> sample = Emodels;
	    	if(idx == 1) {
	    		Random rand = new Random();
	    		int index = rand.nextInt(sample.size());
	    		protype = sample.get(index);
	    		prototypeList.add(protype);
					//remove already picked template
	    		sample.remove(index);
	    	}
	    	if(!prototypes.containsKey(protype)) {
	    		prototypes.put(protype, new ArrayList<>());
	    	}
	    	if(prototypes.size() == 1) {
	    		beginning = protype;
	    	}
                // randomly chhosing template gestures
	    	if(prototypes.get(beginning).isEmpty()) {
	    		for(int i=0; i<e; i++) {
                            boolean b = false;
		    		for(String nameOfFile : data_set.keySet()) {
                                    if(nameOfFile.contains(user)) {
                                    List<Template> list = data_set.get(nameOfFile);
		    				if(!b){
                                                for(Template tem : list){
                                        String s1 = protype.name.substring(0, protype.name.length()-2);
                    		String s2 = tem.name.substring(0, tem.name.length()-2);
                    		if(s1.equals(s2) ){
                          prototypes.get(beginning).add(tem);
                          b = true;
                          break;
                      	}
                  		}
                }
            	else{
                    Random rand = new Random();
                    int index = rand.nextInt(list.size());
                    Template r = list.get(index);
                    if(!r.name.equals(protype.name)) {
		    	prototypes.get(beginning).add(r);
                    }
                }
		    			}
		    		}
	    		}
	   	}

	    for(Template c : prototypes.keySet()) {
	    	prototypes.put(c, prototypes.get(beginning));
	    }


	  }


	// method where the testing and training of templates begins for each user
	public static void loopDataset(BufferedWriter bfr_object) {
	    System.out.println("started");
            //loop for users
         for(String person : recordofUsers.keySet()) {
         //String person = "s02";
	    	System.out.println("-------------------");
	    	System.out.println("User: " + person);
		double megaCount = 0.0;
                    //loop for e
                for(int e=1; e<=9; e++) {
                    System.out.println("e: " + e);
		    prototypeList = new ArrayList<>();
                    prototypes = new HashMap<>();
                    HashMap<String, Integer> map_cnt = new HashMap<>();
                    //loop for number of tests
                    for(int i=1; i<=100; i++) {

                        System.out.println("i: " + i);
                        List<Template> Emodels= new ArrayList<>();
                        rand_records = new ArrayList<>();
                        //picking random templates for training
                        for(String nameOfFile : data_set.keySet()) {
                            if(nameOfFile.contains(person)) {
                                Emodels = data_set.get(nameOfFile);
                                pickRandModels(person, i,e, Emodels);

	    		}
                    }

                    TreeMap<String, Integer> rightOrWrong = new TreeMap<>();
                    // template testing
                    for(Template c : prototypes.keySet()) {
						/*To call the recognition logic */
	    					Template bst = Recognition.recognizingFile(person, prototypes.get(c),c);

	    					String bstLabel = bst.name.substring(0, bst.name.length()-2);
	    					String candLabel = c.name.substring(0, c.name.length()-2);
	    					if(!map_cnt.containsKey(candLabel)) {
	    						map_cnt.put(candLabel, 0);
	    					}
	    					Score bstScore = null;
	    					if(bstLabel.equals(candLabel)) {
		    					map_cnt.put(candLabel, map_cnt.getOrDefault(candLabel, 0)+1);
	    						rightOrWrong.put(c.name, 1);

	    					}
	    					else {
	    						rightOrWrong.put(c.name, 0);
	    					}

	    					try {/*To write the best matched gesture name and score */
	    						String typeOfGesture = c.name.substring(0, c.name.length()-2);
	    						StringBuilder sb = new StringBuilder();
	    						String modifiedCandName = ""+person+"-"+c.name;
	    						String modifiedCandScoreName = ""+person+"-"+bst.name;
	    						sb.append("\"");
	    						sb.append("{");
	    						for(Template r : prototypes.get(c)) {
	    							sb.append(person+"-"+r.name);
	    							sb.append(" ");
	    						}
	    						sb.deleteCharAt(sb.length()-1);
	    						sb.append("}");
	    						sb.append("\"");
	    						StringBuilder nbstSb = new StringBuilder();
	    						nbstSb.append("\"");
	    						nbstSb.append("{");
                                                        Score scr=new Score(bst.name, 0.6);
	    						for(Template rand: prototypes.get(c)) {

	    							Score score = Recognition.scoreFinder(person, prototypes.get(c), rand.points);
	    							if(rand.name.equals(bst.name)) {
	    								bstScore = score;
	    							}


	    							String mod = ""+person+"-"+ score.name + "-" + String.format("%.6f",score.score);
	    							nbstSb.append(mod);
	    							nbstSb.append(" ");
	    						}

                                                        if(bstScore == null){
                                                            bstScore = scr;
                                                        }
	    						nbstSb.deleteCharAt(nbstSb.length()-1);
	    						nbstSb.append("}");
	    						nbstSb.append("\"");
								bfr_object.write(""+ person +"  "+ typeOfGesture + "    "+ i + "    "+ e + "    "+ "9"+ "   "+sb.toString()+ "  "+ modifiedCandName+ "  "+bst.name.substring(0, bst.name.length()-2)+ " "+rightOrWrong.get(c.name)+ "   "+String.format("%.6f",bstScore.score)+ "   "+modifiedCandScoreName+ "  "+nbstSb.toString());
								bfr_object.newLine();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
	    				}


	    				prototypes.put(beginning, new ArrayList<>());

					}




					double result = 0.0;
	    			for(Template c : prototypes.keySet()) {
	    				String cand = c.name.substring(0, c.name.length()-2);
	    				result += map_cnt.get(cand);
	    			}
	    			result /= 100; //CHANGE TO 100
	    			megaCount += result;

	    		}
	    		accuracy_rate.put(person, megaCount);
	    		try {
					bfr_object.newLine();
					bfr_object.newLine();
					bfr_object.write("Total Average Accuracy:"+"    "+ (double)megaCount/9);
					bfr_object.newLine();
					bfr_object.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}

	    }
        }
		/*To read folders present inside each user folder */
            public static void readmodelsFile(String root, File directory){
		File[] filesList = directory.listFiles();
		ArrayList<String> nms = new ArrayList<>();
		for(int i=0; i<filesList.length; i++) {
                    if(filesList[i].isFile()) {
                    String NAMEFILE = directory.toString() + "/"+filesList[i].getName();
                    String key_map = directory.toString()+"/"+filesList[i].getName().substring(0,filesList[i].getName().length()-6);
			if(!data_set.containsKey(key_map)) {
                            data_set.put(key_map, new ArrayList<>());

                            modelset = new ArrayList<>();
                        }
			nms.add(filesList[i].getName());
			readmodelsFileWithin(NAMEFILE);
			data_set.put(key_map, modelset);

                    }
		}
		recordofUsers.put(root, nms);
            }
          /*To read the content wrtiien inside the file */
          public static void readmodelsFileWithin(String NAMEFILE) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {

		          dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

		          DocumentBuilder db = dbf.newDocumentBuilder();

		          Document doc = db.parse(new File(NAMEFILE));

		          doc.getDocumentElement().normalize();

		          Element element = doc.getDocumentElement();
                  String name = element.getAttribute("Name");
                  String sub = element.getAttribute("Subject");
                  String speed = element.getAttribute("Speed");
                  String N = element.getAttribute("Number");
                  String numPts = element.getAttribute("NumPts");
                  String time = element.getAttribute("Millseconds");
                  String appName = element.getAttribute("AppName");
                  String appVer = element.getAttribute("AppVer");
                  String date = element.getAttribute("Date");

                  properties.add(name);
                  properties.add(sub);
                  properties.add(speed);
                  properties.add(N);
                  properties.add(numPts);
                  properties.add(time);
                  properties.add(appName);
                  properties.add(appVer);
                  properties.add(date);

		          NodeList list = doc.getElementsByTagName("Point");

		          for (int temp = 0; temp < list.getLength(); temp++) {

		              Node node = list.item(temp);

		              if (node.getNodeType() == Node.ELEMENT_NODE) {

		                  Element e2 = (Element) node;

		                  String x = e2.getAttribute("X");
		                  String y = e2.getAttribute("Y");
		                  String t = e2.getAttribute("T");
		                  Point point = new Point(Integer.parseInt(x), Integer.parseInt(y));
		                  initial.add(point);
		                  txy_coordinates.add(Integer.parseInt(t));

		              }
		          }

		          Template t = new Template(name, initial);
		          modelset.add(t);

		      } catch (Exception e) {
		          e.printStackTrace();
		      }
		}



	}
