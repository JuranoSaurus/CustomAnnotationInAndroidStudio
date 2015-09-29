package com.annotation.compiler;

import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by SungGeun on 2015-09-29.
 */
final class ClassValidator {

    private final TypeElement annotatedClass;
    private final Set<Modifier> modifiers;

    ClassValidator(TypeElement annotatedClass) {
        this.annotatedClass = annotatedClass;
        this.modifiers = annotatedClass.getModifiers();
    }

    boolean isPublic(){
        return modifiers.contains(Modifier.PUBLIC);
    }

    boolean isAbstract(){
        return modifiers.contains(Modifier.ABSTRACT);
    }

}
