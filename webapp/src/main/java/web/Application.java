package web;

import crest.siamese.helpers.MyUtils;
import crest.siamese.main.Siamese;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Date;

@SpringBootApplication
public class Application extends SpringBootServletInitializer{
    private static String configFile;
    private static Siamese siamese = null;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Siamese siamese() {
        if (siamese == null)
            startSiamese();
        return siamese;
    }

    public static void startSiamese() {
//        String[] overridingParams = processCommandLine(args);
        String[] overridingParams = {"", "", ""};
        if (configFile == null) {
            System.out.println("Couldn't find the config file. Use the default one at ./config.properties");
            configFile = "config.properties"; // TODO: change how config being loaded , external is better
        }
        Date startDate = MyUtils.getCurrentTime();
        siamese = new Siamese(configFile, overridingParams);
        siamese.startup();
    }
}