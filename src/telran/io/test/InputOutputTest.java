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
	// Testing printStream class
	// creates printStream object and connects it to file with name STREAM_FILE

	void printStreamTest() throws Exception {
		// try resources block allows automatic closing after
		try (PrintStream printStream = new PrintStream(STREAM_FILE)) {
			printStream.println(HELLO);
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(STREAM_FILE))) {
			assertEquals(HELLO, reader.readLine());
			assertNull(reader.readLine());
		}

	}

	@Test
	// The same test as for PrintStream but for PrintWriter
	// The difference between PrintStream and PrintWriter is that
	// PrintStream puts line immediately while Print PrintWriter puts line into a
	// buffer
	// The buffer flushing is happening after closing
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
//		System.out.printf("%s - path \".\"\n",pathCurrent);
//		System.out.printf("%s - normalized path \".\"\n",pathCurrent.normalize());
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

		Files.walkFileTree(path, new HashSet<>(), depth==-1?Integer.MAX_VALUE:depth, new FileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				printIO(dir, path, null);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				printIO(file, path, null);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				printIO(file, path, exc);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

				printIO(dir, path, exc);
				return FileVisitResult.CONTINUE;
			}

			private static void printIO(Path path, Path basePath, IOException exc) {
				String type = Files.isDirectory(path) ? "directory" : "file";
				int spacesPerLevel = 4;
				int level = getLevel(path, basePath);

				if (exc == null) {
					System.out.printf("%s%s - %s\n", " ".repeat(level * spacesPerLevel), path.getFileName(), type);
				} else {
					System.err.printf("%sFailed to visit %s: %s\n", " ".repeat(level * spacesPerLevel), type,
							path.getFileName());
				}
			}

			private static int getLevel(Path path, Path basePath) {
				return path.getNameCount() - basePath.getNameCount();
			}
		});

	}

}
