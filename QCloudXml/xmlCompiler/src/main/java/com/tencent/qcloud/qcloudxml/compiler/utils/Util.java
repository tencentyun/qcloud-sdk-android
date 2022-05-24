package com.tencent.qcloud.qcloudxml.compiler.utils;

import static com.tencent.qcloud.qcloudxml.compiler.XmlProcessor.ELEMENT_UTILS;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;
import com.tencent.qcloud.qcloudxml.compiler.ProcessingException;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;

public class Util {
    private static final String TAG = "Util";

    /**
     * 使字符串首字母小写
     *
     * @param originalStr 原始字符串
     * @return 首字母小写的新字符串
     */
    public static String initialsLowerCase(String originalStr) {
        if (originalStr == null || originalStr.length() <= 0) return originalStr;

        if (originalStr.length() == 1) {
            return originalStr.toLowerCase(Locale.ROOT);
        } else {
            String initials = originalStr.substring(0, 1).toLowerCase(Locale.ROOT);
            return initials + originalStr.substring(1);
        }
    }

    /**
     * 使字符串首字母大写
     *
     * @param originalStr 原始字符串
     * @return 首字母大写的新字符串
     */
    public static String initialsUpperCase(String originalStr) {
        if (originalStr == null || originalStr.length() <= 0) return originalStr;

        if (originalStr.length() == 1) {
            return originalStr.toUpperCase(Locale.ROOT);
        } else {
            String initials = originalStr.substring(0, 1).toUpperCase(Locale.ROOT);
            return initials + originalStr.substring(1);
        }
    }

    /**
     * 获取Element全路径（主要用于获取内部类路径 比如：TestBean$ITestBean）
     */
    public static String getElementFullName(Element element) {
        StringBuilder sb = new StringBuilder(element.getSimpleName());
        Element enclosingElement = element.getEnclosingElement();
        while (enclosingElement != null && enclosingElement.getKind().isClass()) {
            sb.insert(0, element.getEnclosingElement().getSimpleName() + "$");
            enclosingElement = enclosingElement.getEnclosingElement();
        }
        return sb.toString();
    }

    /**
     * 获取XML映射类 节点名称
     * @param element XML映射类
     * @return 节点名称 默认为类名，可以通过XmlBean name指定
     */
    public static String getTypeElementName(TypeElement element) {
        if (element.getAnnotation(XmlBean.class) != null && !"".equals(element.getAnnotation(XmlBean.class).name())) {
            return element.getAnnotation(XmlBean.class).name();
        } else {
            return element.getSimpleName().toString();
        }
    }

    /**
     * 获取XML映射成员 节点名称
     * @param element XML映射成员
     * @return 节点名称 默认为成员字段名且首字母大写，可以通过XmlElement name指定
     */
    public static String getElementName(Element element) {
        if (element.getAnnotation(XmlElement.class) != null && !"".equals(element.getAnnotation(XmlElement.class).name())) {
            return element.getAnnotation(XmlElement.class).name();
        } else {
            return Util.initialsUpperCase(element.getSimpleName().toString());
        }
    }

    /**
     * 获取List节点是否忽略
     * @param element List节点
     * @return List节点是否忽略
     */
    public static boolean ignoreListNote(Element element) {
        if (element.getAnnotation(XmlElement.class) != null) {
            return element.getAnnotation(XmlElement.class).ignoreListNote();
        } else {
            return false;
        }
    }

    /**
     * 获取Element名字是否忽略
     * @param element Element节点
     * @return Element名字是否忽略
     */
    public static boolean ignoreName(Element element) {
        if (element.getAnnotation(XmlElement.class) != null) {
            return element.getAnnotation(XmlElement.class).ignoreName();
        } else {
            return false;
        }
    }

    /**
     * Element值为0时是否忽略
     * @param element Element节点
     * @return Element值为0时是否忽略
     */
    public static boolean ignoreZero(Element element) {
        if (element.getAnnotation(XmlElement.class) != null) {
            return element.getAnnotation(XmlElement.class).ignoreZero();
        } else {
            return false;
        }
    }

    /**
     * 获取Element是否是平铺的List节点
     * @param element Element节点
     * @return Element是否是平铺的List节点
     */
    public static boolean flatListNote(Element element) {
        if (element.getAnnotation(XmlElement.class) != null) {
            return element.getAnnotation(XmlElement.class).flatListNote();
        } else {
            return false;
        }
    }

    /**
     * 获取List泛型类型
     * @param listVariableElement List Element
     * @return 泛型类型
     * @throws ProcessingException 注解处理器异常
     */
    public static TypeMirror getGenericTypeFromList(Element listVariableElement) throws ProcessingException {
        if (listVariableElement.asType().getKind() != TypeKind.DECLARED) {
            throw new ProcessingException(listVariableElement, "Element must be of type java.util.List");
        }

        DeclaredType typeMirror = (DeclaredType) listVariableElement.asType();

        switch (typeMirror.getTypeArguments().size()){
            case 0:
                return ELEMENT_UTILS.getTypeElement("java.lang.Object").asType();
            case 1:
                if (typeMirror.getTypeArguments().get(0).getKind() == TypeKind.WILDCARD) {
                    WildcardType wildCardMirror = (WildcardType) typeMirror.getTypeArguments().get(0);
                    if(wildCardMirror.getExtendsBound()!=null){
                        return wildCardMirror.getExtendsBound();
                    } else if(wildCardMirror.getSuperBound()!=null){
                        return wildCardMirror.getSuperBound();
                    } else {
                        return ELEMENT_UTILS.getTypeElement("java.lang.Object").asType();
                    }
                } else {
                    return typeMirror.getTypeArguments().get(0);
                }

            default:
                throw new ProcessingException(listVariableElement,
                        "Seems that you have annotated a List with more than one generic argument? How is this possible?");
        }
    }

    private static HashMap<Name, Element> toHashMap(List<? extends Element> elements) {
        HashMap<Name, Element> hashMap = new HashMap<>(elements.size());
        for (Element element : elements) {
            hashMap.put(element.getSimpleName(), element);
        }
        return hashMap;
    }
}
