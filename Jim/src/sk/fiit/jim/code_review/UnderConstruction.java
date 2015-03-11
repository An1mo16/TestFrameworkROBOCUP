package sk.fiit.jim.code_review;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import sk.fiit.robocup.library.review.ReviewOk;

/**
 *  UnderConstruction.java
 *  
 *  Annotations or classes marked with this annotation are not to be judged
 *  by a code review.
 *
 *@Title	Jim
 *@author	marosurbanec
 */
@Retention(value=RetentionPolicy.SOURCE)
@Target(value={ElementType.TYPE})
@ReviewOk
public @interface UnderConstruction {
	String[] todo();
}