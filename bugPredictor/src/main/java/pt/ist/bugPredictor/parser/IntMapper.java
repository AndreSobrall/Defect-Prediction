package pt.ist.bugPredictor.parser;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import pt.ist.bugPredictor.CodeFile;

public class IntMapper {

	/* ------------------------ */
	/*		Data Structures		*/
	/* ------------------------ */

	private Map<String, Integer> mapping;
	private int id;
	private final String OUTPUT_FOLDER = "./output/";
	private final String DELIMITER = "*";


	public IntMapper() {
		this.mapping = new HashMap<String, Integer>();
		this.id = 1;
	}

	/* -------------------- */
	/*		Class Logic		*/
	/* -------------------- */

	public List<Integer> getIntArray(List<String> tokens) {
		List<Integer> int_array = new ArrayList<Integer>();
		for(String token : tokens)
			int_array.add(getTokenId(token));
		return int_array;
	} 

	// Gets id for string token
	public int getTokenId(String token) {

		// if first time seeing this token, assign an id to it.
		if(!mapping.containsKey(token)) {
			mapping.put(token, id);
			inc_id(); // update id to an unused one.
		}

		// return integer id
		return mapping.get(token);
	}

	// Increases readability, increments id.
	private void inc_id() {
		this.id++;
	}



	/* -------------------- */
	/*		File Writer		*/
	/* -------------------- */

	public void writeCodeFile(String datasetName, CodeFile file) throws IOException {

		String fileName = datasetName + DELIMITER + file.getBranch() + DELIMITER + file.getFileName();
		String path = OUTPUT_FOLDER + fileName;
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		
		List<Integer> int_array = file.getintMap();
		for(int i=0; i  < int_array.size(); i++)
    		writer.write(int_array.get(i).toString() + " ");
     
    	writer.close();
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
		System.out.println("Printing the id's associated with each string");
		for(String token : mapping.keySet()) {
			if(token.length() <= 4)
				System.out.println("\""+token+"\" \t\t\t-> " + mapping.get(token));
			else if(token.length() <= 10)
				System.out.println("\""+token+"\" \t\t-> " + mapping.get(token));
			else
				System.out.println("\""+token+"\" \t-> " + mapping.get(token));
		}
	}
}