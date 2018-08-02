package org.mapstruct;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 原注解是RetentionPolicy.CLASS无法在运行期获取注解信息
 *
 * @author lhf
 * @date 2018/8/1
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MappingTarget {
}
