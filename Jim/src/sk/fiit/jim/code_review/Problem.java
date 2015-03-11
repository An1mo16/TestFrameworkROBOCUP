package sk.fiit.jim.code_review;

import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import sk.fiit.robocup.library.review.ReviewOk;

/**
 *  Problem.java
 *  
 *  Mark classes with this annotation in case a problem(s) was found during
 *  a code review.
 *
 *@Title	Jim
 *@author	marosurbanec
 */
@Target({TYPE, METHOD, FIELD, CONSTRUCTOR, LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
@ReviewOk
public @interface Problem {
	String[] value();
}