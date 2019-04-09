package pt.ist.bugPredictor.parser;

public class ParserDTO {
	int line;
	String name;

	public ParserDTO(int line, String name) {
		this.line = line;
		this.name = name;
	}

	public String toString() {
		return "["+line+"]: " + name;
	}

}