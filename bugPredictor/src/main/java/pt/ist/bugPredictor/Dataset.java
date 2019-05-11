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

import pt.ist.bugPredictor.parser.Tokenizer;
import pt.ist.bugPredictor.parser.IntMapper;


public class Dataset {
	private String datasetName;
	private String datasetPath;
	private String currentBranch;
	private List<String> branches;

	private Map<String, List<CodeFile>> codeFiles;
	private IntMapper mapper;
	private Tokenizer tokenizer;
	
	private final String[] MASTER = {"master", "HEAD"}; // Aux String[]	

	public Dataset(String name, String path, IntMapper mapper) {
		this.datasetName = name;
		this.datasetPath = path;
		this.mapper 	 = mapper;
		this.tokenizer   = new Tokenizer(); 
		this.codeFiles 	 = new HashMap<String, List<CodeFile>>();
		try {
			currentBranch = getCurrentGitBranch();
			branches 	  = getDatasetGitBranches();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// TODO: Populate codeFiles
	// For each branch get buggy file
	// checkout to master
	// get the fixed file
	// store as entry in codeFiles using brnachName
	public void processCodeFiles() throws IOException, InterruptedException {
		
		// Process all buggy files first in order to only checkout to master once
		List<BuggyFile> buggies = processBuggyFiles();

		// Switch to the git master branch
		checkoutToMasterBranch();

		// For each buggy file, process the corresponding fixed file
		for(BuggyFile buggy : buggies) {
			FixedFile fixed = new FixedFile(buggy.getFileName(), buggy.getBranch(), this);
			addCodeFile(fixed, buggy);
		}
	}

	// For each git branch processes a BuggyFile: 
	public List<BuggyFile> processBuggyFiles() throws IOException, InterruptedException {
		List<BuggyFile> buggies = new ArrayList<BuggyFile>();
		for(String branch : branches)
			buggies.add(processBuggyFile(branch));
		return buggies;
	}

	// Process a BuggyFile: 
	// checkouts to corresponding branch and creates a buggyFile
	public BuggyFile processBuggyFile(String branch) throws IOException, InterruptedException {
		checkoutGitBranch(branch);
		BuggyFile buggy = new BuggyFile(branch, this.datasetPath);
		buggy.processContents(tokenizer, mapper);
		return buggy;
	}

	// Process a FixedFile: 
	// creates a FixedFile and processes contents
	public FixedFile processFixedFile(BuggyFile buggy, Dataset dataset) throws IOException, InterruptedException {
		FixedFile fixed = new FixedFile(buggy.getFileName(), buggy.getBranch(), dataset);
		fixed.processContents(tokenizer, mapper);
		return fixed;
	}

	// Add an entry to codeFiles Map
	// key: branchName, val: [fixed, buggy]
	public void addCodeFile(FixedFile fixed, BuggyFile buggy) {
		List<CodeFile> files = new ArrayList<CodeFile>();
		String branch = buggy.getBranch();
		files.add(fixed);
		files.add(buggy);
		codeFiles.put(branch,files);
	}

	// TODO: Remove
	// Only here to test entire pipeline
	public void processCodeFiles(String branch) throws IOException, InterruptedException {
		BuggyFile buggy = processBuggyFile(branch);
		checkoutToMasterBranch();
		FixedFile fixed = processFixedFile(buggy, this);
		addCodeFile(fixed, buggy);
	}

	public void writeCodeFiles() {
		for(String branch : codeFiles.keySet()) {
			for(CodeFile file : codeFiles.get(branch)) {
				try {
					mapper.writeCodeFile(this.datasetName, file); 
				} catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	/* ------------------------ */
	/* 	 	 Git Methods		*/
	/* ------------------------ */

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
    		System.out.println("Switched from branch \'" + currentBranch + "\' -> \'" + branchName + "\'");
    		currentBranch = branchName; 
    	}
	}


	public void checkoutToMasterBranch() throws IOException, InterruptedException {
			checkoutGitBranch(MASTER[0]);
	}



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

	public List<String> getGitBranches() {
		return branches;
	}

	public void printDataset() {
		printDatasetInfo();
		printFiles();
	}

	public void printDatasetInfo() {
		System.out.println("\n---------- DATASET Info ----------");
		System.out.println("Dataset Name: \t" + datasetName);
		System.out.println("Dataset Path: \t" + datasetPath);
		System.out.println("\n---------- GIT Info ----------");
		System.out.println("Current Branch: "+ currentBranch);
		System.out.println("Other Available Branches: " + branches.size());
		System.out.println("-------------------------------\n");
	}

	public void printFiles() {
		for(String branch : codeFiles.keySet()) {
			for(CodeFile file : codeFiles.get(branch)) {
				file.print();
			}
		}
	}

}
