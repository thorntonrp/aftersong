/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Robert P. Thornton
 */
@Configuration
@ComponentScan(basePackageClasses = MainApp.class)
public class MainConfiguration {
}
