package br.com.semeru;

import br.com.semeru.config.FileStorageConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageConfig.class})
public class Startup {
    public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);

        /*BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);
        String result = bCryptPasswordEncoder.encode("admin123");
        System.out.println("My hash " + result);*/
    }
}
