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
			dataset.checkoutToMasterBranch();

			// searchs dataset directory for file
			String cmd = "mdfind -onlyin " + dataset.getDatasetPath() + " -name " + fileName;
	    	Process process = Runtime.getRuntime().exec(cmd, null, new File(dataset.getDatasetPath()));
	    	process.waitFor();

	    	// TODO: make sure it always retrieve correct file
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    	setFilePath(reader.readLine()); 

	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
}
