package telran.io.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;

class InputOutputTest {

	private static final String STREAM_FILE = "Stream-file";
	private static final String HELLO = "Hello";
	private static final String WRITER_FILE = "Writer-file";

	@AfterAll
	static void tearDown() throws IOException {
		Files.deleteIfExists(Path.of(STREAM_FILE));
		Files.deleteIfExists(Path.of(WRITER_FILE));
	}

	@Test

	void printStreamTest() throws Exception {
		try (PrintStream printStream = new PrintStream(STREAM_FILE)) {
			printStream.println(HELLO);
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(STREAM_FILE))) {
			assertEquals(HELLO, reader.readLine());
			assertNull(reader.readLine());
		}

	}

	@Test
	void printWriterTest() throws Exception {

		try (PrintWriter printWriter = new PrintWriter(WRITER_FILE)) {
			printWriter.println(HELLO);
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(WRITER_FILE))) {
			assertEquals(HELLO, reader.readLine());
			assertNull(reader.readLine());
		}

	}

	@Test
	void pathTest() {
		Path pathCurrent = Path.of(STREAM_FILE);
		System.out.printf("%s - %s\n", pathCurrent.toAbsolutePath().normalize(),
				Files.isDirectory(pathCurrent) ? "directory" : "file");
		pathCurrent = pathCurrent.toAbsolutePath().normalize();
		System.out.printf("count of levels is %d\n", pathCurrent.getNameCount());
	}

	@Test
	void printDirectoryTest() throws IOException {
		printDirectory("..", 3);
	}

	private void printDirectory(String dirPathStr, int depth) throws IOException {
		Path path = Path.of(dirPathStr).toAbsolutePath().normalize();
		int spacesPerLevel = 4;
		String directoryColor = "\u001B[34m";
		String fileColor = "\u001B[32m";
		String resetColor = "\u001B[0m";

		Files.walkFileTree(path, new HashSet<>(), depth, new FileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				int level = dir.getNameCount() - path.getNameCount();
				System.out.printf("%s%s%s%s - directory\n", " ".repeat(level * spacesPerLevel), directoryColor,
						dir.getFileName(), resetColor);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				int level = file.getNameCount() - path.getNameCount();
				System.out.printf("%s%s%s%s - file\n", " ".repeat(level * spacesPerLevel), fileColor,
						file.getFileName(), resetColor);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				int level = file.getNameCount() - path.getNameCount();
				System.err.printf("%sFailed to visit file: %s\n", " ".repeat(level * spacesPerLevel),
						file.getFileName());
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});

	}

}
