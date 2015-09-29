package com.annotation.compiler;

import com.annotation.api.StaticLauncher;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static com.annotation.compiler.Utils.getPackageName;
import static com.squareup.javapoet.JavaFile.builder;

public class AnnotationProcessor extends AbstractProcessor {

    private static final String ANNOTATION = "@"+ StaticLauncher.class.getSimpleName();

    private final Messager messager = new Messager();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager.init(processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(StaticLauncher.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {

        for(Element annotatedElement : roundEnv.getElementsAnnotatedWith(StaticLauncher.class)){
            TypeElement annotatedClass = (TypeElement) annotatedElement;
            if(!isValidClass(annotatedClass)){
                return true;
            }

            try{
                generateCode(annotatedClass);
            }catch (UnnamedPackageException | IOException e){
                messager.error(annotatedElement, "Couldn't generate class for %s : %s", annotatedClass, e.getMessage());
            }
        }

        return false;
    }

    private void generateCode(TypeElement annotatedClass)
            throws UnnamedPackageException, IOException {
        String packageName = getPackageName(processingEnv.getElementUtils(), annotatedClass);

        CodeGenerator codeGenerator = new CodeGenerator(annotatedClass);
        TypeSpec generatedClass = codeGenerator.generateClass();

        JavaFile javaFile = builder(packageName, generatedClass).build();
        javaFile.writeTo(processingEnv.getFiler());
    }

    private boolean isValidClass(TypeElement annotatedClass) {
        ClassValidator classValidator = new ClassValidator(annotatedClass);

        if(!classValidator.isPublic()){
            messager.error(annotatedClass, "Classes annotated with %s must be public", ANNOTATION);
            return false;
        }

        if(classValidator.isAbstract()){
            messager.error(annotatedClass, "Classes annotated with %s must not be abstract", ANNOTATION);
            return false;
        }

        return true;
    }

}
