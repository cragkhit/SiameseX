package com.siamesex.standalone;

import crest.siamese.helpers.MyUtils;
import crest.siamese.main.Siamese;
import echotest.EchoTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class StandaloneApplication {


	// Chaiyong's Siamese java project
	private static String configFile;
	private static Siamese siamese = null;

	private static EchoTest echoTest = null;

	public static void main(String[] args) {

		SpringApplication.run(StandaloneApplication.class, args);
	}

	@Bean
	public Siamese siamese() {
		if (siamese == null)
			startSiamese();
		return siamese;
	}

	@Bean
	public EchoTest echoTest()
	{
		if(echoTest == null)
		{
			echoTest = new EchoTest();
		}
		return echoTest;
	}

	// function to create bean of Siamese Java app
	public static void startSiamese() {
//        String[] overridingParams = processCommandLine(args);
		String[] overridingParams = {"", "", ""};
		if (configFile == null) {
			System.out.println("Couldn't find the config file. Use the default one at ./config.properties \n TODO: currently, " +
					"the config file is hardcode to the main of Siamese class");
			configFile = "config.properties"; // TODO: currently, the config file is hardcode to the main of Siamese class
		}
		Date startDate = MyUtils.getCurrentTime();
		siamese = new Siamese(configFile, overridingParams);
		siamese.startup();
	}

}
