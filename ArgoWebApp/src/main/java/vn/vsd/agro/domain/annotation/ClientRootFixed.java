package vn.vsd.agro.domain.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Domain's client always is root
 **/
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientRootFixed {

}
