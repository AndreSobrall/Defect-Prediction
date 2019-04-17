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

			// searchs dataset directory for file
			String cmd = "find "+ this.dataset.getDatasetPath() + " -iname " + this.fileName;
	    	Process process = Runtime.getRuntime().exec(cmd, null, new File("."));
	    	process.waitFor();

	    	// TODO: make sure it always retrieve correct file
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    	setFilePath(reader.readLine());

	    	String line; int i = 0;
    		while ((line = reader.readLine()) != null) { 
    			if(i == 0)
    				System.out.println("Other possibilites: ");
    			System.out.println(line);
    			i++;
	    	}
	    	printDebugInfo();

	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	}

	public void printDebugInfo() {
		// System.out.println("diff line:" + diffLine);
		System.out.println("--------- FIXED FILE ---------");
		System.out.println("filePath: \t" + this.filePath);
		System.out.println("fileName: \t" + this.fileName);
		System.out.println("-----------------------------");
		System.out.println(" ");
	}


}
