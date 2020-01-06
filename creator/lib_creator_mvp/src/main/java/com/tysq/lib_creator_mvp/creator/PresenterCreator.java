package com.tysq.lib_creator_mvp.creator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.tysq.lib_creator_mvp.utils.ClzNameHelper;
import com.tysq.lib_creator_mvp.utils.JavaPencil;

import java.io.IOException;

import javax.inject.Inject;
import javax.lang.model.element.Modifier;

/**
 * @author a2
 * @date 创建时间：2018/12/12
 * @description
 */
public class PresenterCreator {

    // 包名
    private final String pkgName;
    // 类名
    private final String clzName;
    // 存放路径
    private final String path;

    public PresenterCreator(String path,
                            String pkgName) {
        this.path = path;
        this.clzName = ClzNameHelper.getInstance().getName(ClzNameHelper.PRESENTER);
        this.pkgName = pkgName;
    }

    public void create() throws IOException {
        String viewName = ClzNameHelper.getInstance().getName(ClzNameHelper.VIEW);
        ClassName viewClz = ClassName.get(pkgName, viewName);

        // 构建构造函数
        MethodSpec constructorMethod = MethodSpec.constructorBuilder()  // 构建构造函数类型
                .addModifiers(Modifier.PUBLIC)      // public
                .addAnnotation(Inject.class)    // 添加 Inject 注解：@Inject
                .addParameter(viewClz, "view")  // 添加 参数(XXXView view)
                .addStatement("super(view)")  // 添加语句： super(view)
                .build();

        // 生成接口类
        TypeSpec typeSpec = TypeSpec
                .classBuilder(clzName)      // 类名
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)  // 类型限定付
                .superclass(ParameterizedTypeName
                        .get(ClassName.get(ClzNameHelper.getInstance().getClass(ClzNameHelper.PRESENTER)),
                                viewClz))  // 继承BasePresenter类
                .addMethod(constructorMethod)
                .build();

        JavaPencil.write(path, pkgName, typeSpec);

    }

}
