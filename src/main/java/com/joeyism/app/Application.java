package com.joeyism.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.joeyism.aspect.MemoryAspect;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
    	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("META-INF/Spring-Customer.xml");
    	MemoryAspect memoryAspect = (MemoryAspect) applicationContext.getBean("memoryAspect");
        SpringApplication.run(Application.class, args);
        LogMemory.inMb();
        ((ClassPathXmlApplicationContext) applicationContext).close();
    }
    
}
