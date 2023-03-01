package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MessageController {

    Logger logger = LoggerFactory.getLogger(MessageController.class);

    int i = 0;
    long lastCallTime = 0l;
    long timeDifference = 0l;

    @GetMapping("/message")
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 2000, multiplier = 2))
    public String getMessage(){
        timeDifference = System.currentTimeMillis() - lastCallTime;
        logger.info(++i + " call Inside MessageController getMessage method. timeDifference : "+timeDifference);
        RestTemplate rt = new RestTemplate();
        lastCallTime = System.currentTimeMillis();
        rt.getForObject("http://localhost:9090/demo", Object.class);
        return "Message received successfully.";
    }

    @Recover
    public String getRecoveryMessage(){
        logger.info(" call Inside MessageController getRecoveryMessage method.");
        return "Recovery Message";
    }
}
