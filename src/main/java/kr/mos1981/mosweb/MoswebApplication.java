package kr.mos1981.mosweb;

import kr.mos1981.mosweb.configuration.StorageProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MoswebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoswebApplication.class, args);
	}
	@Bean
	CommandLineRunner init(StorageProperties properties) {
		return (args) -> {
			Files.createDirectories(Paths.get(properties.getLocation()));
		};
	}

}
