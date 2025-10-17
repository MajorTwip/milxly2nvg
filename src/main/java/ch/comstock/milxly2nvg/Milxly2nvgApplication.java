package ch.comstock.milxly2nvg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.xml.bind.JAXBException;

@SpringBootApplication
public class Milxly2nvgApplication implements CommandLineRunner {

	private static Logger log = LoggerFactory.getLogger(Milxly2nvgApplication.class);

	public static void main(String[] args) {
		log.info("Starting Milxly2nvg Application");
		checkArgs(args);
		SpringApplication.run(Milxly2nvgApplication.class, args);
		log.info("Milxly2nvg Application terminated");
	}

	/**
	 * Check command line arguments
	 * 
	 * @param args
	 */
	private static void checkArgs(String[] args) {
		for (int i = 0; i < args.length; ++i) {
			log.info("args[{}]: {}", i, args[i]);
		}
	}

	@Override
	public void run(String... args) {
		Path currentRelativePath = Paths.get("");
		try {
			Files.walk(currentRelativePath, 1).filter(Files::isRegularFile)
					.filter(path -> path.toString().endsWith(".milxly")).forEach(path -> {
						if (path.getFileName().toString().endsWith(".milxly")) {
							log.debug("Found MILXLY file: {}", path);
							try {
								Converter.convert(path);
							} catch (JAXBException e) {
								log.error("JAXB Exception during conversion of file: {}", path, e);
								e.printStackTrace();
							}
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}