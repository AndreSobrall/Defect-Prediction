package pt.ist.bugPredictor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class BuggyFile extends CodeFile {
	private final String diffPath;

	public BuggyFile(String branch, String datasetPath) {
		this.branch   = branch;
		this.diffPath = datasetPath + "/.bugs-dot-jar/developer-patch.diff";
		this.filePath = datasetPath;
		parseDiff();
	}

	private void parseDiff() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(diffPath));
			String line = reader.readLine();
			for(String token: line.split(" ")) {
				if(token.contains("/")) {
					setFilePath(token);
					setFileName(filePath);
					break; 
				}
			}

			printDebugInfo(line);

			reader.close();
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	protected void setFilePath(String diffLine) {
		int i;
		for (i = 0; i < diffLine.length(); i++) {
        	if (diffLine.charAt(i) == '/') {
            	i++;
            	break;
        	}
    	}
		this.filePath += "/" + diffLine.substring(i, diffLine.length());
	}

	@Override
	protected void setFileName(String filePath) {
		String[] tokens = filePath.split("/");
		this.fileName = tokens[tokens.length-1];
	}

	public void printDebugInfo(String diffLine) {
		// System.out.println("diff line:" + diffLine);
		System.out.println("--------- BUGGY FILE ---------");
		System.out.println("filePath: \t" + this.filePath);
		System.out.println("fileName: \t" + this.fileName);
		System.out.println("-----------------------------");
		System.out.println(" ");
	}

}
