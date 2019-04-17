package pt.ist.bugPredictor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import pt.ist.bugPredictor.Dataset;


public class Dataset {
	private String datasetName;
	private String datasetPath;
	private String currentBranch;
	private List<String> branches;

	// Aux String[]
	private final String[] MASTER = {"master", "HEAD"};
	
	// TODO: Populate
	// key: branchName, val: tuple<release, master> Each Object with token vector and int array.
	private Map<String, List<CodeFile>> codeFiles;

	public Dataset(String name, String path) {
		this.datasetName = name;
		this.datasetPath = path;
		try {
			currentBranch = getCurrentGitBranch();
			branches 	  = getDatasetGitBranches();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String getCurrentGitBranch() throws IOException, InterruptedException {
    	String cmd = "git rev-parse --abbrev-ref HEAD";
    	Process process = Runtime.getRuntime().exec(cmd, null, new File(this.datasetPath));
    	process.waitFor();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	String currentBranch = reader.readLine();
    	if(currentBranch.equals(MASTER[1]))
    		currentBranch = MASTER[0];
    	return currentBranch;
	}

	private List<String> getDatasetGitBranches() throws IOException, InterruptedException {
		
		List<String> gitBranches = new ArrayList<String>();
		
		// List branches
		String cmd = "git branch -a", line;
		Process process = Runtime.getRuntime().exec(cmd, null, new File(this.datasetPath));
    	process.waitFor();

    	// Read and Store interesting branches
    	BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	Pattern pattern = Pattern.compile("bugs-dot-jar_.*");

    	while ((line = reader.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);

			if (matcher.find()){
			    // System.out.println("Matched portion:\n" + matcher.group(0));
			    String gitBranch = matcher.group(0);

			    if(!gitBranches.contains(gitBranch))
			    	gitBranches.add(gitBranch);
			} 
        }

        return gitBranches;
	}

	public void checkoutGitBranch(String branchName) throws IOException, InterruptedException {
		if(!currentBranch.equals(branchName)) {
			String cmd = "git checkout " + branchName;
			Process process = Runtime.getRuntime().exec(cmd, null, new File(this.datasetPath));
    		process.waitFor();
    		System.out.println("Switched from branch " + currentBranch + " -> " + branchName);
    		currentBranch = branchName; 
    	}
	}

	public void checkoutToMasterBranch() throws IOException, InterruptedException {
			checkoutGitBranch(MASTER[0]);
	}

	// TODO: Populate codeFiles
	// For each branch get buggy file
	// checkout to master
	// get the fixed file
	// store as entry in codeFiles using brnachName
	// ...........
	// Begin by doing it only for the current branch


	/* ------------------------ */
	/* 	 	 Aux Methods		*/
	/* ------------------------ */

	public String getDatasetName() {
		return datasetName;
	}

	public String getDatasetPath() {
		return datasetPath;
	}

	public String getCurrGitBranch() {
		return currentBranch;
	}

	public void printDataset() {
		System.out.println("\n---------- DATASET Info ----------");
		System.out.println("Dataset Name: \t" + datasetName);
		System.out.println("Dataset Path: \t" + datasetPath);
		System.out.println("\n---------- GIT Info ----------");
		System.out.println("Current Branch: "+ currentBranch);
		System.out.println("Other Available Branches: " + branches.size());
		System.out.println("-------------------------------\n");
	}

}
