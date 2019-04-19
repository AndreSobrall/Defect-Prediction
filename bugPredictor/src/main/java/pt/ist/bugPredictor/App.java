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


    public static void main( String[] args )
    {	
    	App app = new App();

    	try {
        	Dataset dataset = app.datasets.get("accumulo");
        	dataset.processCodeFiles("bugs-dot-jar_ACCUMULO-1183_cfbf5999");
        	dataset.writeCodeFiles();
        	// dataset.printDataset();

		} catch(Exception e) {
			e.printStackTrace();
		}

		// TODO: write mapper to file
		// mapper.writeToFile(...)
    }
}
