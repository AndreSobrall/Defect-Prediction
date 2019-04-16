package pt.ist.bugPredictor;

import java.util.List;

public class CodeFile {
	private String fileName;
	private String filePath;
	private List<String> tokenVector;
	private int[]		 intVector;

	public CodeFile(String fileName, String filePath) {
		this.fileName = fileName;
		this.filePath = filePath;
	}
}