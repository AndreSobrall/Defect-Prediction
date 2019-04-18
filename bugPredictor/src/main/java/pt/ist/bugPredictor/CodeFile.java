package pt.ist.bugPredictor;

import java.util.List;
import pt.ist.bugPredictor.parser.Tokenizer;
import pt.ist.bugPredictor.parser.IntMapper;

public abstract class CodeFile {
	protected String  fileName;
	protected String  filePath;
	protected String  branch;
	private List<String>  tokenVector;
	private List<Integer> intMap;

	// Tokenizes file and maps to int vector
	public void processContents(Tokenizer tokenizer, IntMapper mapper) {
		setTokenVector(tokenizer);
		setMapping(mapper);
	}

	// Calls parser to vectorize file
	private void setTokenVector(Tokenizer tokenizer) {
		this.tokenVector = tokenizer.execute(filePath);
	}

	// Calls mapper to get int vector
	private void setMapping(IntMapper mapper) {
		this.intMap = mapper.getIntArray(tokenVector);
	}

	protected abstract void setFilePath(String path);
	protected abstract void setFileName(String name);

	public String getFilePath() {
		return filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public String getBranch() {
		return branch;
	}	

	public List<Integer> getintMap() {
		return intMap;
	}

	public List<String>  getTokenVector() {
		return tokenVector;
	}

}