package com.itbulls.springdemo.utils;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class LogFileCleaner {

	@Value("${logging.file.name:logs/my-app.log}") // default if not specified
    private String logFilePath;
	
    @PostConstruct
    public void cleanLogFile() {
        try {
            FileWriter writer = new FileWriter(logFilePath, false);
            writer.write(""); 
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
