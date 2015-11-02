package com.joeyism.aspect;

import java.lang.annotation.*;

import org.springframework.stereotype.Component;

@Component
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Memory {

}
