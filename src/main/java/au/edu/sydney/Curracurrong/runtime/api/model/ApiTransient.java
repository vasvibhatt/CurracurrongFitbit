package au.edu.sydney.Curracurrong.runtime.api.model;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiTransient {
}
