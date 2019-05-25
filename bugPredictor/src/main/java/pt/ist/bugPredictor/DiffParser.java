package pt.ist.bugPredictor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class DiffParser {
	private String datasetPath;
	private String diffPath;

	public DiffParser(String datasetPath) {
		this.datasetPath = datasetPath;
		this.diffPath 	 = datasetPath + "/.bugs-dot-jar/developer-patch.diff";
	}

	public List<String> parseDiff() {
		List<String> filePaths = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.diffPath));
			String line = reader.readLine();
			while(line != null) {
				if(isLineWithFilePath(line)) {
					String filePath = getFilePathFromLine(line);
					filePaths.add(filePath); 
				}
				line = reader.readLine();
			}

			reader.close();
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
		return filePaths;
	}

	private boolean isLineWithFilePath(String line) {
		return line.contains("diff --git");
	}

	private String getFilePathFromLine(String line)
	{
		for(String token : line.split(" ")) {
			if(token.contains("/")) {
				return getFilePath(token);
			}
		}
		return null;
	}

	private String getFilePath(String token) {
		int i;

		// removes beginning of path of diff
		// ex: a/<file-path>/ to <file-path>/
		for (i = 0; i < token.length(); i++) {
        	if (token.charAt(i) == '/') {
            	i++;
            	break;
        	}
    	}

		return datasetPath + "/" + token.substring(i, token.length());
	}

	

}