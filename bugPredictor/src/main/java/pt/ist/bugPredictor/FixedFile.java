package pt.ist.bugPredictor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

import pt.ist.bugPredictor.Dataset;


public class FixedFile extends CodeFile {

	public final String ANSI_RESET = "\u001B[0m";
	public final String ANSI_YELLOW = "\u001B[33m";

	private Dataset dataset;
	private String buggyBranch;

	public FixedFile(String fileName, String filePath, String branch, String buggybranch) {
		setFileName(fileName);
		setFilePath(filePath);
		this.branch = branch;
		this.buggyBranch = buggybranch;
	} 

	@Override
	protected void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	protected void setFilePath(String filePath) { 
		this.filePath = filePath;
	}	

	@Override
	public void print() {
		System.out.println("--------- FIXED FILE ---------");
		printInfo();
		System.out.println("-----------------------------");
		System.out.println(" \n");
		this.printTokens();
	}

	public String getBuggyBranch() {
		return buggyBranch;
	}

}
