package vn.vsd.agro.domain.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Domain's query with created by is current user
 **/
@Retention(RetentionPolicy.RUNTIME)
public @interface CreatedOnly {

}
