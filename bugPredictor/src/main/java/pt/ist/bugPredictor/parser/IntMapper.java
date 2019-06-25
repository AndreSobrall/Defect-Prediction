package pt.ist.bugPredictor.parser;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import pt.ist.bugPredictor.CodeFile;
import pt.ist.bugPredictor.FixedFile;

public class IntMapper {

	public final String ANSI_RESET = "\u001B[0m";
	public final String ANSI_RED   = "\u001B[31m";
	public final String ANSI_GREEN = "\u001B[32m";

	/* ------------------------ */
	/*		Data Structures		*/
	/* ------------------------ */

	private Map<String, Integer> freq_mapping;
	private Map<String, Integer> less_mapping;
	private Integer max_file_size;
	private int id;
	private final String OUTPUT_FOLDER = "./output/";

	public IntMapper() {
		this.id = 1;
		this.max_file_size = 0;
		setFreqMapping();
		setLessMapping();
	}

	/* -------------------- */
	/*		Class Logic		*/
	/* -------------------- */

	public List<Integer> getIntArray(List<String> tokens) {
		
		List<Integer> int_array = new ArrayList<Integer>();
		
		for(String token : tokens) {
			int id = getTokenId(token);
			// UNCOMENT TO PRINT <token>:<id>
			// System.out.print(token + ":" +  id + "\n");
			int_array.add(id);
		}

		// updates max file size, if needed.
		setMaxFileSize(int_array.size());
		
		return int_array;
	} 

	// Gets id for string token
	public int getTokenId(String token) {

		// First it verifies if it is a frequent token
		if(freq_mapping.containsKey(token))
			return freq_mapping.get(token);

		// Else if it's the first time seeing this token, assign an id to it.
		else if(!less_mapping.containsKey(token)) 
			addToMap(less_mapping, token);
		
		return less_mapping.get(token);
	}

	// Increases readability, increments id.
	private void inc_id() {
		this.id++;
	}

	// Sets the maximum size of a file that was mapped
	private void setMaxFileSize(int newMax) {
		if(newMax > max_file_size)
			this.max_file_size = newMax;
	}

	// Stores tokens that are regarded as frequent.
	// Right now, these are Java Keywords. ex:"if", "while", "for"
	// Frequent Tokens:
	// 		IfStatement 
	// 		WhileStatement 
	// 		DoStatement
	// 		ForStatement
	//      ForEachStmt
	// 		AssertStatement 
	// 		BreakStatement 
	// 		ContinueStatement 
	// 		ReturnStatement 
	// 		ThrowStatement 
	// 		SynchronizedStatement 
	// 		TryStatement
	// 		SwitchStatement 
	// 		SwitchEntry
	// 		CatchClause 

	// 	(ver NodeIterator para + info.)
	// 		MethodDeclaration 
	// 		ClassDeclaration 
	// 		ConstructorDeclaration
	// 		EnumDeclaration  
	// 		MethodInvocation  
	// 		SuperMethodInvocation  


	private void setFreqMapping() {
		String[] freq_tokens = {"<if>", "<while>", "<do>", "<for>", "<foreach>","<assert>", 
								"<break>", "<continue>", "<return>", "<throw>", "<synchronized>", 
								"<try>", "<switch>", "<case>", "<catch>", "<method-declaration>",
								"<class-declaration>", "<constructor-declaration>", "<enum-declaration>",
								"<method-invocation>", "<super>", "<field>"};

		this.freq_mapping = new HashMap<String,Integer>();

		for(int i = 0; i < freq_tokens.length; i++) 
			addToMap(freq_mapping, freq_tokens[i]);
	}

	// These are the tokens that are regarded as less frequent.
	// These are the variables that vary between classes.
	// Less Frequent:
	// 		FormalParameter
	// 		BasicType 
	// 		PackageDeclaration 
	// 		InterfaceDeclaration 
	// 		CatchClauseParameter 
	// 		ClassDeclaration 
	// 		MethodInvocation 
	// 		SuperMethodInvocation 
	// 		MemberReference 
	// 		SuperMemberReference 
	// 		ConstructorDeclaration 
	// 		ReferenceType 
	// 		MethodDeclaration 
	// 		VariableDeclarator 
	// 		StatementExpression 
	// 		TryResource CatchClause 
	// 		CatchClauseParameter 
	// 		SwitchStatementCase 
	// 		ForControl 
	// 		EnhancedForControl
	private void setLessMapping() { 
		this.less_mapping = new HashMap<String, Integer>();
	}

	// Assign id to token
	private void addToMap(Map<String, Integer> m, String token) {
		m.put(token, new Integer(this.id));
		inc_id(); // update id to an unused one.
	}


	/* -------------------- */
	/*		File Writer		*/
	/* -------------------- */

	public void writeCodeFile(String datasetName, CodeFile file) {

		String fileName, branch, simpleFileName;
		
		// Removes ".java" from file name
		simpleFileName = file.getFileName().substring(0, file.getFileName().length() - 5); 

		// Defines file name as "GitBranch * simpleFileName.txt"
		if(file instanceof FixedFile) 
			fileName = "fixed-"+simpleFileName+".txt";
		else
			fileName = "buggy-"+simpleFileName+".txt";

		// Stores in output/datasetName/guitBranch
		if(file instanceof FixedFile) {
			FixedFile ff = (FixedFile) file;
			branch = ff.getBuggyBranch(); 
		}
		else
			branch = file.getBranch();

		// Sets the path to the dir
		String dirPath = OUTPUT_FOLDER + datasetName + "/" + branch;
		
		// If folders to dirpath don't exist, they are created
		File directory = new File(dirPath);
    	if (!directory.exists())
        	directory.mkdirs(); 
      	
      	String filePath = dirPath + "/" + fileName;

        // Actually writes to file
		writeFile(fileName, filePath, file.getintMap());
	}

	// Writes Max FileSize to "./output/max_size.txt"
	public void writeDatasetMetadata() { 		
		String fileName =  "max_size.txt";
		String filePath = OUTPUT_FOLDER + fileName;
		List<Integer> content = new ArrayList<Integer>();
		content.add(this.max_file_size); // max sequence
		content.add(this.id);			 // number of unique features
		writeFile(fileName, filePath, content);
	}

	private void writeFile(String fileName, String filePath, List<Integer> content) 
	{
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));    
			for(int i=0; i  < content.size(); i++)
    			writer.write(content.get(i).toString() + " ");
    		writer.close();
			System.out.print(ANSI_GREEN + "[SUCCESS WRITING]:" + ANSI_RESET);
		} catch(IOException e) {
			System.out.print(ANSI_RED + "[ERROR WRITING]:" + ANSI_RESET + "\n");
			System.out.println(e.getMessage());
		}
		System.out.print(" \'"+fileName+"\'\t->\tdir: \'"+filePath+"\' \n");
	}


	/* ---------------------------- */
	/*		Printing Functions		*/
	/* ---------------------------- */

	public void printIntArray(List<Integer> int_array) {
		int loc = 0;
		int section_size = 10;
		int sections = int_array.size()/section_size;
		int leftover = int_array.size()%section_size;

		System.out.println("Printing Array of Integers:");
		// System.out.println("sections: " + sections + " leftover: " + leftover);
		System.out.println("[");

		for(int i = 0; i < sections; i++) {
			System.out.print("  ");
			for(int j = 0; j < section_size; j++) {
				loc = i*section_size + j;
				System.out.print(" " + int_array.get(loc) + ",");
			}
			System.out.println(""); //newline
		}

		// Go to next power of section size
		loc++;
		System.out.print("  ");
		//System.out.println("leftovers:  loc = " + loc);
		for(int w = loc; w < loc + leftover; w++) {
			System.out.print(" " + int_array.get(w) + ","); 
		}
		System.out.println("");
		System.out.println("]");
		System.out.println("");
	}

	public void printMapping() {
		System.out.println("Printing the id's associated with each string\n Only less_mapping:");
		for(String token : less_mapping.keySet()) {
			if(token.length() <= 4)
				System.out.println("\""+token+"\" \t\t\t-> " + less_mapping.get(token));
			else if(token.length() <= 10)
				System.out.println("\""+token+"\" \t\t-> " + less_mapping.get(token));
			else
				System.out.println("\""+token+"\" \t-> " + less_mapping.get(token));
		}
	}

}