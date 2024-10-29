package edu.usb.argos.ASTProcessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AstProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AstProcessorApplication.class, args);

		String testFilePath = "src/main/java/edu/usb/argos/ASTProcessor/ExampleClass.java";

		JavaParserExample parserExample = new JavaParserExample();

		List<String> methodNames = parserExample.getMethodNames(testFilePath);

		System.out.println("Names of methods found:");
		methodNames.forEach(System.out::println);
	}
}
