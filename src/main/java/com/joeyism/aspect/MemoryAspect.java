package com.joeyism.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.joeyism.app.LogMemory;

@Aspect
public class MemoryAspect {

	@Pointcut("within(@Memory *)")
	public void withinMemory() {}
	
	@Pointcut("execution(* * (..))")
	public void allMethods(){}
	
	@Before("execution(* *(..))")
	public Object aroundAllMethods(ProceedingJoinPoint joinPoint) throws Throwable{
		System.out.println("aspecting");
		LogMemory.inMb();
		return joinPoint.proceed();
	}
	
}
