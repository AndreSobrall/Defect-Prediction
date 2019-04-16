package pt.ist.bugPredictor;

import java.util.List;

public abstract class CodeFile {
	protected String  fileName;
	protected String  filePath;
	protected String  branch;
	private List<String>  tokenVector;
	private List<Integer> mapping;

	// TODO: call parser to vectorize file
	protected void setTokenVector(List<String> tokenVector) {
		this.tokenVector = tokenVector;
	}

	// TODO: call mapping to get int vector
	protected void setMapping(List<Integer> mapping) {
		this.mapping = mapping;
	}

	protected abstract void setFilePath(String path);
	protected abstract void setFileName(String name);

	public String filePath() {
		return filePath;
	}

	public String fileName() {
		return fileName;
	}

	public List<Integer> getMapping() {
		return mapping;
	}

	public List<String>  getTokenVector() {
		return tokenVector;
	}

}