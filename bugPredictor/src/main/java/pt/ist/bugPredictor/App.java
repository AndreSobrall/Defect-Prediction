package pt.ist.bugPredictor;

import java.util.Map;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import pt.ist.bugPredictor.Dataset;
import pt.ist.bugPredictor.parser.IntMapper;

public class App 
{	
	Map<String, Dataset> datasets;

	public App() {
		createDatasets();		
	}

	private void createDatasets() {
		final String[] datasetNames = { "accumulo", "camel", "commons-math", 
										"flink", "jackrabbit-oak", "logging-log4j2", 
										"maven", "wicket"};

		this.datasets = new HashMap<String, Dataset>();


		if (Files.exists(Paths.get("../bugs-dot-jar/"))) {
	
			// for all datasets		
			for(String datasetName : datasetNames) {
				
				String datasetpath = "../bugs-dot-jar/" + datasetName;

				// if dataset exists
				if(Files.exists(Paths.get(datasetpath)))
					this.datasets.put(datasetName, new Dataset(datasetName, datasetpath));
				else
					System.out.println("Dataset \""+datasetName+"\" does not exist.");
			}
		} 
		// if bugs.jar does not exist
		else {
			System.out.println("Dataset \"bugs-dot-jar\" does not exist in \"../bugs-dot-jar/\".");
		}
	}


	// Process all branches of all available datasets
	private void processAll(){
		try {
    		for(String datasetName : datasets.keySet()) {
    			
    			printHeader(datasetName);
	        	
	        	Dataset dataset = this.datasets.get(datasetName);
	        	
	        	// Tokenize code files
	        	dataset.processCodeFiles();

	        	// Map them to int's and output to *.txt file
	        	dataset.writeCodeFiles(); 
	        }

		} catch(Exception e) {
			e.printStackTrace();
		}    	
	}

	// Process all branches of a given dataset
	private void processDataset(String datasetName) {
		try {
    		
    		printHeader(datasetName);
	        Dataset dataset = this.datasets.get(datasetName);
	       
	        // Tokenize code files
	        dataset.processCodeFiles();

	        // Map them to int's and output to *.txt file
	        dataset.writeCodeFiles(); 

	    } catch(Exception e) {
			e.printStackTrace();
		}    	
	}

	// Process a branch of a given dataset
	private void processBranch(String datasetName, String branch) {
		try {
    		
    		printHeader(datasetName);
	        Dataset dataset = this.datasets.get(datasetName);
	        //dataset.printDatasetInfo();

	        // Tokenize code files
	        dataset.processCodeFiles(branch);

	        dataset.printFiles();

	        // Map them to int's and output to *.txt file
	        dataset.writeCodeFiles(); 

	    } catch(Exception e) {
			e.printStackTrace();
		}    	
	}

	// Aux print to help info user on different dataset processing 
	private void printHeader(String datasetName) {
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++++");
		// all lined up correctly
		if(datasetName.length() == 8)
			System.out.println("++\t       " + datasetName + "\t\t\t++");
		else if(datasetName.length() < 8)
			System.out.println("++\t\t" + datasetName + "\t\t\t++");
		else
			System.out.println("++\t    " + datasetName + "\t\t++");

    	System.out.println("++++++++++++++++++++++++++++++++++++++++++");
	}


    public static void main( String[] args )
    {	
    	App app = new App();
    	// app.processBranch("accumulo", "bugs-dot-jar_ACCUMULO-218_15476a0d");
    	// app.processDataset("accumulo");
    	// app.processDataset("camel");
    	// app.processDataset("commons-math"); 
		// app.processDataset("flink"); 
		app.processDataset("jackrabbit-oak");
		app.processDataset("logging-log4j2");
		app.processDataset("maven");
		app.processDataset("wicket");
    	// app.processAll();
	}
}
