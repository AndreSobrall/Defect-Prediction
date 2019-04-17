package pt.ist.bugPredictor;

import java.util.Map;
import java.util.HashMap;

import pt.ist.bugPredictor.Dataset;
import pt.ist.bugPredictor.BuggyFile;
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
		for(String datasetName : datasetNames) {
			datasets.put(datasetName, new Dataset(datasetName, path_to_datasets + datasetName));
		}

	}


    public static void main( String[] args )
    {	
    	App app = new App();

    	//----------------
    	// EX: TokenParser
        // System.out.println( "Hello World!" );
        // TokenVisitor parser = new TokenVisitor();
        // parser.exec();

    	try {
        	Dataset dataset = app.datasets.get("accumulo");
        	dataset.printDataset();
			// dataset.processCodeFiles();
        	dataset.processCodeFiles("bugs-dot-jar_ACCUMULO-1183_cfbf5999");

		} catch(Exception e) {
			e.printStackTrace();
		}

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
