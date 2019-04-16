package pt.ist.bugPredictor.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class BuggyFile {
	private final String diffPath;
	private String filePath;
	private String fileName;

	public BuggyFile(String datasetPath) {
		diffPath = datasetPath + ".bugs-dot-jar/developer-patch.diff";
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
					filePath = getFilePath(token);
					fileName = getFileName(filePath);
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

	private String getFilePath(String diffLine) {
		int i;
		for (i = 0; i < diffLine.length(); i++) {
        	if (diffLine.charAt(i) == '/') {
            	i++;
            	break;
        	}
    	}
		return diffLine.substring(i, diffLine.length());
	}

	private String getFileName(String filePath) {
		String[] tokens = filePath.split("/");
		return tokens[tokens.length-1];
	}

	public String filePath() {
		return filePath;
	}

	public String fileName() {
		return fileName;
	}

}
