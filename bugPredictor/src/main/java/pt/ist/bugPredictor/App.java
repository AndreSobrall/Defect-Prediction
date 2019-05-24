package pt.ist.bugPredictor;

import java.util.Map;
import java.util.HashMap;
import pt.ist.bugPredictor.Dataset;
import pt.ist.bugPredictor.parser.IntMapper;

public class App 
{	
	Map<String, Dataset> datasets;
	IntMapper mapper;

	public App() {
		this.mapper = new IntMapper();
		createDatasets();		
	}

	private void createDatasets() {
		final String[] datasetNames = { "accumulo", "camel", "commons-math", 
										"flink", "jackrabbit-oak", "logging-log4j2", 
										"maven", "wicket"};

		this.datasets = new HashMap<String, Dataset>();

		// for all datasets		
		for(String datasetName : datasetNames)
			this.datasets.put(datasetName, new Dataset(datasetName, "../bugs-dot-jar/" + datasetName, this.mapper));
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
    	// app.processBranch("accumulo", "bugs-dot-jar_ACCUMULO-2390_28294266");
    	// app.processDataset("accumulo");
    	app.processAll();
	}
}
