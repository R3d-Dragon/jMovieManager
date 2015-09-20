/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.xml;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotations for the XML parser
 * 
 * @author Bryan Beck
 * @since 30.08.2012
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface MyXMLAnnotation {
    
    /**
     * if the method should be parsed or not
     * @return true the method must not be parsed <br/> false otherwise
     */
    boolean isTransient() default false;
    
    /**
     * if the method field can be null or not
     * @return true if the method isn't allowed to return null values
     */
    boolean isNotNull() default false;
}
