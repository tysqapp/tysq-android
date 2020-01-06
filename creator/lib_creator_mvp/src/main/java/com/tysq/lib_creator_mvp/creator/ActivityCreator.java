package com.tysq.lib_creator_mvp.creator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.tysq.lib_creator_mvp.utils.ClzNameHelper;
import com.tysq.lib_creator_mvp.utils.JavaPencil;
import com.tysq.lib_creator_mvp.utils.MVPFactory;

import java.io.IOException;

import javax.lang.model.element.Modifier;

/**
 * @author a2
 * @date 创建时间：2018/12/12
 * @description
 */

public class ActivityCreator {

    // 包名
    private final String pkgName;
    // 类名
    private final String clzName;
    // 存放路径
    private final String path;

    private static final String GET_LAYOUT = "getLayout";
    private static final String INIT_INTENT = "initIntent";
    private static final String INIT_VIEW = "initView";
//    private static final String INIT_DATA = "initData";
    private static final String DAGGER_INJECT = "registerDagger";

    public static final String APP_COMPONENT_METHOD = "getAppComponent";

    public ActivityCreator(String path,
                           String pkgName) {
        this.path = path;
        this.clzName = ClzNameHelper.getInstance().getName(ClzNameHelper.ACTIVITY);
        this.pkgName = pkgName;
    }

    public void creator() throws IOException {

        ClassName intent = ClassName.get("android.content", "Intent");

        String viewName = ClzNameHelper.getInstance().getName(ClzNameHelper.VIEW);
        ClassName viewClz = ClassName.get(pkgName, viewName);

        MethodSpec getLayoutMethod = MethodSpec.methodBuilder(GET_LAYOUT)  // 构建构造函数类型
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)            // 添加 Override 注解：@Override
                .returns(int.class)
                .addStatement("return 0")            // 添加语句：return 0;
                .build();

        MethodSpec initIntentMethod = MethodSpec.methodBuilder(INIT_INTENT)  // 构建构造函数类型
                .addAnnotation(Override.class)            // 添加 Override 注解：@Override
                .addModifiers(Modifier.PROTECTED)         // 添加 限定符 protected
                .addParameter(intent, "intent")        // 添加 参数 (Intent intent)
                .build();

        MethodSpec initViewMethod = MethodSpec.methodBuilder(INIT_VIEW)  // 构建构造函数类型
                .addAnnotation(Override.class)            // 添加 Override 注解：@Override
                .addModifiers(Modifier.PROTECTED)         // 添加 限定符 protected
                .build();

//        MethodSpec initDataMethod = MethodSpec.methodBuilder(INIT_DATA)  // 构建构造函数类型
//                .addAnnotation(Override.class)            // 添加 Override 注解：@Override
//                .addModifiers(Modifier.PROTECTED)         // 添加 限定符 protected
//                .build();


        String moduleName = ClzNameHelper.getInstance().getName(ClzNameHelper.MODULE);
        ClassName moduleClz = ClassName.get(pkgName + MVPFactory.DI, moduleName);
        String moduleMethodName = moduleName.substring(0, 1).toLowerCase() + moduleName.substring(1);

        CodeBlock codeBlock = CodeBlock.builder()
                .add("Dagger$L", ClzNameHelper.getInstance().getName(ClzNameHelper.COMPONENT))
                .add(".builder()\r\n")
                .add(".appComponent($T.$L())\r\n", ClzNameHelper.getInstance().getAppClass(), APP_COMPONENT_METHOD)
                .add(".$L(new $T(this))\r\n", moduleMethodName, moduleClz)
                .add(".build()\r\n")
                .add(".inject(this);\r\n")
                .build();

        // dagger注入 函数
        MethodSpec daggerMethod = MethodSpec.methodBuilder(DAGGER_INJECT)  // 构建构造函数类型
                .addAnnotation(Override.class)            // 添加 Override 注解：@Override
                .addCode(codeBlock)
                .addModifiers(Modifier.PROTECTED)         // 添加 限定符 protected
                .build();

        // XXXPresenter
        String presenterName = ClzNameHelper.getInstance().getName(ClzNameHelper.PRESENTER);
        ClassName presenterClz = ClassName.get(pkgName, presenterName);

        // 生成接口类
        TypeSpec typeSpec = TypeSpec
                .classBuilder(clzName)      // 类名
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)  // 类型限定付
                .superclass(ParameterizedTypeName
                        .get(ClassName.get(ClzNameHelper.getInstance().getClass(ClzNameHelper.ACTIVITY)),
                                presenterClz))    // 继承JBaseFragment<XXXPresenter>类
                .addSuperinterface(viewClz)
                .addMethod(getLayoutMethod)         // 添加 getLayout 方法
                .addMethod(initIntentMethod)        // 添加 initIntent 方法
                .addMethod(initViewMethod)          // 添加 initView 方法
//                .addMethod(initDataMethod)          // 添加 initData 方法
                .addMethod(daggerMethod)            // 添加 registerDagger 方法
                .build();

        JavaPencil.write(path, pkgName, typeSpec);
    }

}
