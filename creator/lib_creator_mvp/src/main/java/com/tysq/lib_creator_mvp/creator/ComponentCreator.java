package com.tysq.lib_creator_mvp.creator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.tysq.lib_creator_mvp.utils.ClzNameHelper;
import com.tysq.lib_creator_mvp.utils.JavaPencil;
import com.tysq.lib_creator_mvp.utils.MVPFactory;

import java.io.IOException;

import javax.lang.model.element.Modifier;

import dagger.Component;

/**
 * @author a2
 * @date 创建时间：2018/12/12
 * @description
 */
public class ComponentCreator {

    // 包名
    private final String pkgName;
    // 类名
    private final String clzName;
    // 存放路径
    private final String path;
    private boolean isFragment;

    private static final String INJECT = "inject";
    private static final String DEPENDENCIES = "dependencies";
    private static final String MODULES = "modules";

    public ComponentCreator(String path,
                            String pkgName,
                            boolean isFragment) {
        this.path = path;
        this.clzName = ClzNameHelper.getInstance().getName(ClzNameHelper.COMPONENT);
        this.pkgName = pkgName;
        this.isFragment = isFragment;
    }

    public void creator() throws IOException {

        String viewName;
        if (isFragment) {
            viewName = ClzNameHelper.getInstance().getName(ClzNameHelper.FRAGMENT);
        } else {
            viewName = ClzNameHelper.getInstance().getName(ClzNameHelper.ACTIVITY);
        }
        ClassName viewClz = ClassName.get(pkgName, viewName);

        String moduleName = ClzNameHelper.getInstance().getName(ClzNameHelper.MODULE);
        ClassName moduleClz = ClassName.get(pkgName + MVPFactory.DI, moduleName);

        MethodSpec injectMethod = MethodSpec.methodBuilder(INJECT)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(viewClz, viewName.substring(0, 1).toLowerCase() + viewName.substring(1))
                .build();

        AnnotationSpec componentAnnotation = AnnotationSpec
                .builder(Component.class)
                .addMember(MODULES, "{$T.class}", moduleClz)
                .addMember(DEPENDENCIES, "$T.class", ClzNameHelper.getInstance().getClass(ClzNameHelper.COMPONENT))
                .build();

        TypeSpec typeSpec = TypeSpec
                .interfaceBuilder(clzName)        // 类名
                .addModifiers(Modifier.PUBLIC)    // 类型限定付
                .addAnnotation(ClzNameHelper.getInstance().getClass(ClzNameHelper.SCOPE))
                .addAnnotation(componentAnnotation)
                .addMethod(injectMethod)          // 添加 injectXXX 方法
                .build();

        JavaPencil.write(path, pkgName + MVPFactory.DI, typeSpec);
    }

}
