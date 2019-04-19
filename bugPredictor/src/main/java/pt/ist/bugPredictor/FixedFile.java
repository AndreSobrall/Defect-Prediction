package pt.ist.bugPredictor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

import pt.ist.bugPredictor.Dataset;


public class FixedFile extends CodeFile {
	private Dataset dataset;

	public FixedFile(String fileName, Dataset dataset) {
		setFileName(fileName);
		setDataset(dataset);
		setFilePath();
		branch = "master";
	} 

	@Override
	protected void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	protected void setFilePath(String filePath) { 
		this.filePath = filePath;
	}

	private void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}
	
	private void setFilePath() {
		try {
			// switches do master branch if needed
			this.dataset.checkoutToMasterBranch();

			// searchs dataset directory for fixed file
			String cmd = "find "+ this.dataset.getDatasetPath() + " -iname " + this.fileName;
	    	Process process = Runtime.getRuntime().exec(cmd, null, new File("."));
	    	process.waitFor();

	    	BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    	// TODO: assert this is the case.
	    	// I'm assuming first line read from find is the correct one.
	    	setFilePath(reader.readLine()); 

	    	String line; int i = 0;
    		while ((line = reader.readLine()) != null) { 
    			if(i == 0)
    				System.out.println("Other FixedFile possibilites: ");
    			System.out.println(line);
    			i++;
	    	}

	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	}

	@Override
	public void print() {
		System.out.println("--------- FIXED FILE ---------");
		printInfo();
		System.out.println("-----------------------------");
		System.out.println(" ");
	}

}
