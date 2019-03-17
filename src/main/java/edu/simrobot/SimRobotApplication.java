package edu.simrobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SimRobotApplication{

    public static void main(String[] args) {
        SpringApplication.run(SimRobotApplication.class, args);
    }

}

