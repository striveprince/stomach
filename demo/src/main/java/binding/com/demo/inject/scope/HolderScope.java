package binding.com.demo.inject.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * project：cutv_ningbo
 * description：
 * create developer： admin
 * create time：9:34
 * modify developer：  admin
 * modify time：9:34
 * modify remark：
 *
 * @version 2.0
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface HolderScope {
}
