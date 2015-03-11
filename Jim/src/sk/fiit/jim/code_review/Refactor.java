package sk.fiit.jim.code_review;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Refactor.java
 *  
 *  Mark class or method with this annotation if a part of the system
 *  requires a refactoring due to an identified smell.
 *
 *@Title	Jim
 *@author	marosurbanec
 */
@Retention(value=RetentionPolicy.SOURCE)
@Target(value={ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Refactor {
	String smell();
	String solution();
}