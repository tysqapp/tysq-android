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
public class FragmentCreator {

    // 包名
    private final String pkgName;
    // 类名
    private final String clzName;
    // 存放路径
    private final String path;

    private static final String CREATE_FRAGMENT_VIEW = "onCreateFragmentView";
    private static final String INIT_VIEW = "initView";
    private static final String DAGGER_INJECT = "registerDagger";

    public static final String APP_COMPONENT_METHOD = "getAppComponent";

    public FragmentCreator(String path,
                           String pkgName) {
        this.path = path;
        this.clzName = ClzNameHelper.getInstance().getName(ClzNameHelper.FRAGMENT);
        this.pkgName = pkgName;
    }

    public void creator() throws IOException {

        ClassName layoutInflater = ClassName.get("android.view", "LayoutInflater");
        ClassName viewGroup = ClassName.get("android.view", "ViewGroup");
        ClassName bundle = ClassName.get("android.os", "Bundle");
        ClassName view = ClassName.get("android.view", "View");

        String viewName = ClzNameHelper.getInstance().getName(ClzNameHelper.VIEW);
        ClassName viewClz = ClassName.get(pkgName, viewName);

        // onCreateFragmentView 函数
        MethodSpec createMethod = MethodSpec.methodBuilder(CREATE_FRAGMENT_VIEW)  // 构建构造函数类型
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)            // 添加 Inject 注解：@Inject
                .addParameter(layoutInflater, "inflater")  // 添加 参数(layoutInflater inflater)
                .addParameter(viewGroup, "container")  // 添加 参数(ViewGroup container)
                .addParameter(bundle, "savedInstanceState")  // 添加 参数(Bundle savedInstanceState)
                .returns(view)
                .addStatement("return null")            // 添加语句： super(view)
                .build();

        // initView 函数
        MethodSpec initViewMethod = MethodSpec.methodBuilder(INIT_VIEW)  // 构建构造函数类型
                .addAnnotation(Override.class)            // 添加 Override 注解：@Override
                .addModifiers(Modifier.PROTECTED)         // 添加 限定符 protected
                .addParameter(view, "view")        // 添加 参数   (View view)
                .build();

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
                        .get(ClassName.get(ClzNameHelper.getInstance().getClass(ClzNameHelper.FRAGMENT)),
                                presenterClz))  // 继承JBaseFragment<XXXPresenter>类
                .addSuperinterface(viewClz)
                .addMethod(createMethod)          // 添加 onCreateFragmentView 方法
                .addMethod(initViewMethod)        // 添加 initView 方法
                .addMethod(daggerMethod)          // 添加 registerDagger 方法
                .build();

        JavaPencil.write(path, pkgName, typeSpec);
    }

}
