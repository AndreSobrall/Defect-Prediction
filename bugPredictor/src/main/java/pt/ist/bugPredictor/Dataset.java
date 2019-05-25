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
	public final String ANSI_RESET = "\u001B[0m";
	public final String ANSI_RED   = "\u001B[31m";
	public final String ANSI_GREEN = "\u001B[32m";

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

	// Processes each pair of buggy and fixed files of the Dataset
	public void processCodeFiles() throws IOException, InterruptedException {
		for(String branch : branches)
			processCodeFiles(branch);
	}

	public void processCodeFiles(String branch) throws IOException, InterruptedException {
		
		// Reads diff file to learn the file path's of files that were changed
		DiffParser diff = new DiffParser(this.datasetPath);
		checkoutGitBranch(branch);
		List<String> filePaths = diff.parseDiff();

		List<BuggyFile> buggies = new ArrayList<BuggyFile>();

		// This way has the necessary number of "git checkouts" 
		// -> only once for x number of files
		for(String filePath : filePaths) {
			// Reads and tokenizes buggy file
			BuggyFile buggy = processBuggyFile(branch, filePath);
			System.out.println("buggyFilePath: " + ANSI_RED + buggy.getFilePath() + ANSI_RESET);
			buggies.add(buggy);
		}
		
		// Here is the same logic, in the fixed branch.
		// Adds each fixed, buggy pair to data structure
		for(BuggyFile buggy : buggies) {
			// Reads and tokenizes fixed file
			FixedFile fixed = processFixedFile(buggy);
			System.out.println("FixedFilePath: " + ANSI_GREEN + fixed.getFilePath() + ANSI_RESET);
			addCodeFile(fixed, buggy);
		}
		
	}

	// Process a BuggyFile: 
	// checkouts to corresponding branch and creates a buggyFile
	public BuggyFile processBuggyFile(String branch, String filePath) throws IOException, InterruptedException {
		checkoutGitBranch(branch);
		BuggyFile buggy = new BuggyFile(branch, filePath);
		buggy.processContents(tokenizer, mapper);
		return buggy;
	}

	// Process a FixedFile: 
	// creates a FixedFile and processes contents
	public FixedFile processFixedFile(BuggyFile buggy) throws IOException, InterruptedException {
		String fixedBranch = getFixedBranch(buggy.getBranch());
		checkoutGitBranch(fixedBranch);
		FixedFile fixed = new FixedFile(buggy.getFileName(), buggy.getFilePath(), fixedBranch, buggy.getBranch());
		fixed.processContents(tokenizer, mapper);
		return fixed;
	}

	// Add an entry to codeFiles Map
	// key: branchName, val: [fixed, buggy]
	public void addCodeFile(FixedFile fixed, BuggyFile buggy) {
		List<CodeFile> files;
		String branch = buggy.getBranch();
		
		// There is already an entry for this branch, update
		if(codeFiles.containsKey(branch)) {
			files = codeFiles.get(branch);
			files.add(fixed);
			files.add(buggy);
		}
		// first time adding entries for this branch
		else {
			files = new ArrayList<CodeFile>();
			files.add(fixed);
			files.add(buggy);
			codeFiles.put(branch,files);
		}
		
	}

	// From buggyBranch extract the fixed branch
	public String getFixedBranch(String buggyBranch) {
		String tokens[] = buggyBranch.split("_");
		return tokens[2];
	}

	// Outputs dataset files into corresponding mappings in ".txt" format
	public void writeCodeFiles() {
		
		// Writes each token vector mapping in a ".txt" file
		for(String branch : codeFiles.keySet()) {
			for(CodeFile file : codeFiles.get(branch)) {
				try {
					mapper.writeCodeFile(this.datasetName, file); 
				} catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		// Writes the maximum file size analyzed in a ".txt" file
		// Helps python ML script define the shape of the dataset
		try {
			mapper.writeMaxFileSize(); 
		} catch(Exception e) {
			System.out.println(e.getMessage());
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
