package telran.io;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CodeCommentsSeparation {

	public static void main(String[] args) throws IOException {
		// args[0] - file path for file containing both Java class code and comments
		// args[1] - result file with only code
		// args[2] - result file with only comments
		// example of args[0] "src/telran/io/test/InputOutputTest.java"
		// TODO
		// from one file containing code and comment to create two files
		// one with only comments and second with only code
		if (args.length < 3) {
			System.out.println("should be 3 arguments");
			return;
		}

		String inputFilePath = args[0];
		String codeOutputFilePath = args[1];
		String commentsOutputFilePath = args[2];
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
				PrintWriter codeWriter = new PrintWriter(new FileWriter(codeOutputFilePath));
				PrintWriter commentsWriter = new PrintWriter(new FileWriter(commentsOutputFilePath))) {

			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().startsWith("//")) {
					commentsWriter.println(line);
				} else {
					codeWriter.println(line);
				}
			}
		}
	}
}