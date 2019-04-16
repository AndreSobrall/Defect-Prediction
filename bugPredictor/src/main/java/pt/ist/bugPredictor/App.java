package pt.ist.bugPredictor;

import java.util.Map;
import java.util.HashMap;

import pt.ist.bugPredictor.Dataset;
import pt.ist.bugPredictor.parser.BuggyFile;
import pt.ist.bugPredictor.parser.TokenVisitor;
import pt.ist.bugPredictor.parser.OutputMap;


public class App 
{	
	Map<String, Dataset> datasets;

	public App() {
		final String[] datasetNames = { "accumulo", "camel", "commons-math", 
										"flink", "jackrabbit-oak", "logging-log4j2", 
										"maven", "wicket"};
		final String path_to_datasets = "../bugs-dot-jar/";
		
		datasets = new HashMap<String, Dataset>();
		
		// for all datasets		
		// for(String datasetName : this.datasetNames) {
		// 	datasets.put(datasetName, new Dataset(prefix + datasetName + "/"));
		// }

		datasets.put("accumulo", new Dataset("accumulo", path_to_datasets + "accumulo" + "/"));
	}


    public static void main( String[] args )
    {	
    	App app = new App();

    	//----------------
    	// EX: TokenParser
        // System.out.println( "Hello World!" );
        // TokenVisitor parser = new TokenVisitor();
        // parser.exec();

        // TODO:
        // Files have vector tokens and corresponding mappings
        // maybe Abstract File notion?
        Dataset d = app.datasets.get("accumulo");
        d.printDataset();
    	

    	//----------------
        // EX: Mapping
        // OutputMap mapping = new OutputMap();
        // mapping.addToken("ola");
        // mapping.addToken("andre");
        // mapping.addToken("luis");
        // mapping.addToken("carolina");
        // mapping.addToken("fender");
        // mapping.addToken("defectPrediction");
        // mapping.addToken("exec");
        // mapping.addToken("leonidas");
        // mapping.addToken("allrighty");
        // mapping.addToken("theyams");
        // mapping.addToken("indierocku");
        // mapping.addToken("theyams");
        // mapping.addToken("ola");
        // mapping.addToken("andre");
        // mapping.addToken("ola");
        // mapping.addToken("andre");
        // mapping.addToken("luis");
        // mapping.addToken("carolina");
        // mapping.addToken("fender");
        // mapping.addToken("defectPrediction");
        // mapping.addToken("exec");
        // mapping.addToken("leonidas");
        // mapping.addToken("allrighty");
        // mapping.addToken("theyams");
        // mapping.addToken("indierocku");
        // mapping.addToken("theyams");
        // mapping.addToken("ola");
        // mapping.addToken("andre");
        // mapping.printMapping();
     //    mapping.printIntArray();
     //    try {
     //    	mapping.writeToFile("HelloWorld.txt"); 
    	// } catch(Exception e) {
    	// 	System.out.println(e.getMessage());
    	// }
    }
}
