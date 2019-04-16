package pt.ist.bugPredictor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;


public class FixedFile extends CodeFile {

	public FixedFile(String fileName, String datasetPath) {
		setFileName(fileName);
		setFilePath(datasetPath);
		branch = "master";
	} 

	@Override
	protected void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	protected void setFilePath(String path) {
		// TODO: search directory for file
	}
}
