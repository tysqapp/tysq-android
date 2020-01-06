package com.tysq.lib_creator_mvp.creator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.tysq.lib_creator_mvp.utils.ClzNameHelper;
import com.tysq.lib_creator_mvp.utils.JavaPencil;
import com.tysq.lib_creator_mvp.utils.MVPFactory;

import java.io.IOException;

import javax.lang.model.element.Modifier;

import dagger.Module;
import dagger.Provides;

/**
 * @author a2
 * @date 创建时间：2018/12/12
 * @description
 */
public class ModuleCreator {

    // 包名
    private final String pkgName;
    // 类名
    private final String clzName;
    // 存放路径
    private final String path;

    private static final String GET_VIEW = "get";

    public ModuleCreator(String path,
                         String pkgName) {
        this.path = path;
        this.clzName = ClzNameHelper.getInstance().getName(ClzNameHelper.MODULE);
        this.pkgName = pkgName;
    }

    public void creator() throws IOException {

        String viewName = ClzNameHelper.getInstance().getName(ClzNameHelper.VIEW);
        ClassName viewClz = ClassName.get(pkgName, viewName);

        FieldSpec view = FieldSpec.builder(viewClz, "mView")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build();

        // 构建构造函数
        MethodSpec constructorMethod = MethodSpec.constructorBuilder()  // 构建构造函数类型
                .addModifiers(Modifier.PUBLIC)      // public
                .addParameter(viewClz, "view")  // 添加 参数(XXXView view)
                .addStatement("this.mView = view")  // 添加语句： super(view)
                .build();

        // getXXXView
        MethodSpec getViewMethod = MethodSpec.methodBuilder(GET_VIEW + viewName)  // 构建构造函数类型
                .addAnnotation(Provides.class)    // 添加 Provides 注解：@Provides
                .addAnnotation(ClzNameHelper.getInstance().getClass(ClzNameHelper.SCOPE))    // 添加 作用域 注解：@ActivityScope
                .addModifiers(Modifier.PUBLIC)      // public
                .returns(viewClz)                   // 返回类型
                .addStatement("return this.mView")  // 添加语句： return this.mView;
                .build();

        // 生成接口类
        TypeSpec typeSpec = TypeSpec
                .classBuilder(clzName)      // 类名
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)  // 类型限定付
                .addAnnotation(Module.class)
                .addMethod(constructorMethod)          // 添加 构造 方法
                .addField(view)                        // 添加 成员 属性
                .addMethod(getViewMethod)              // 添加 getXXXView方法
                .build();

        JavaPencil.write(path, pkgName + MVPFactory.DI, typeSpec);
    }

}
