package com.annotation.compiler;

import javax.lang.model.element.TypeElement;

/**
 * Created by SungGeun on 2015-09-29.
 */
class UnnamedPackageException extends Exception {

    public UnnamedPackageException(TypeElement typeElement) {
        super("The package of " + typeElement.getSimpleName() + " is unnamed");
    }
}
