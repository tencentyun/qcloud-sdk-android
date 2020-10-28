package com.tencent.qcloud.qcloudxml.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class XmlProcessor extends AbstractProcessor {
    private static final String TAG = "XmlProcessor";

    private Filer filer;
    public static Messager MESSAGER;
    public static Elements ELEMENT_UTILS;
    public static Types TYPE_UTILS;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        MESSAGER = processingEnv.getMessager();
        ELEMENT_UTILS = processingEnv.getElementUtils();
        TYPE_UTILS = processingEnv.getTypeUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(XmlBean.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(XmlBean.class);

        if(elementsAnnotatedWith==null || elementsAnnotatedWith.size()==0) return false;

        for (Element element : elementsAnnotatedWith) {
            if(element.getKind().isClass()){
                TypeElement classElement = (TypeElement) element;
                try {
                    generateCode(classElement);
                } catch (ProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private void generateCode(TypeElement classElement) throws ProcessingException {
        //生成XmlBean$$XmlAdapter类
        TypeSpec typeSpec = CodeCreator.generateCode(classElement);

        //获取包名
        String packageName = ELEMENT_UTILS.getPackageOf(classElement).getQualifiedName().toString();
        MESSAGER.printMessage(Diagnostic.Kind.NOTE, "packageName: " + packageName);
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .build();

        try {
            //写入java文件
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
