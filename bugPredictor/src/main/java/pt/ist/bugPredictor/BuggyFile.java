package pt.ist.bugPredictor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class BuggyFile extends CodeFile {
	private final String diffPath;

	public BuggyFile(String branch, String datasetName) {
		this.branch   = branch;
		this.diffPath = datasetName + "/.bugs-dot-jar/developer-patch.diff";
		parseDiff();
	}

	private void parseDiff() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(diffPath));
			String line = reader.readLine();
			System.out.println("diff line: ");
			System.out.println(line);
			for(String token: line.split(" ")) {
				if(token.contains("/")) {
					setFilePath(token);
					setFileName(filePath);
					break; 
				}
			}
			System.out.println("filePath: ");
			System.out.println(filePath);
			System.out.println("fileName: ");
			System.out.println(fileName);
			System.out.println("-----------------------------");
			System.out.println(" ");

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

		this.filePath = diffLine.substring(i, diffLine.length());
	}

	@Override
	protected void setFileName(String filePath) {
		String[] tokens = filePath.split("/");
		this.fileName = tokens[tokens.length-1];
	}

}
