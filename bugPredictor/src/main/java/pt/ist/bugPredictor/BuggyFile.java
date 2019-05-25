package pt.ist.bugPredictor;

public class BuggyFile extends CodeFile {
	private String diffLine;

	public BuggyFile(String branch, String filePath) {
		this.branch   = branch;
		this.filePath = filePath;
		setFileName(filePath);
	}

	@Override
	protected void setFileName(String filePath) {
		String[] tokens = filePath.split("/");
		this.fileName = tokens[tokens.length-1];
	}

	@Override
	public void print() {
		System.out.println("--------- BUGGY FILE ---------");
		// System.out.println("diff line:" + diffLine);
		printInfo();
		System.out.println("-----------------------------");
		System.out.println(" \n");
		this.printTokens();
	}


}
