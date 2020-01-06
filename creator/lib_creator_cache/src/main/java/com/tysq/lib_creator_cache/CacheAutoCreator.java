package com.tysq.lib_creator_cache;

import com.google.gson.Gson;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.lang.model.element.Modifier;

/**
 * author       : frog
 * time         : 下午3:14
 * desc         :
 * version      : 1.3.0
 */
public class CacheAutoCreator {

//    public static void main(String[] args) throws IOException {
//        CacheAutoCreator.Builder builder = new CacheAutoCreator.Builder(CacheHelper.class);
//
//        builder.setClassName("Test");
//        builder.setModelClazz(User.class);
//        builder.setPkgPath("com.zinc.bit_creator");
//        builder.setSavePath("bit_creator/src/main/java");
//        builder.setSpName("test_cache");
//
//        builder.build().create();
//    }

    private static final String DEFAULT_PREFIX = "";
    private static final String DEFAULT_SUFFIX = "Cache";
    private static final String INSTANCE = "instance";
    private static final String SP_NAME = "SP_NAME";
    private static final String GSON_NAME = "mGson";
    private static final String SP_NAME_SUFFIX = "_cache";

    /**
     * 共享内存的名字
     */
    private String mSpName;
    /**
     * 保存路径
     */
    private String mSavePath;
    /**
     * 模型类
     */
    private Class mModelClazz;
    /**
     * 类名
     */
    private String mClassName;
    /**
     * 包路径
     */
    private String mPkgPath;
    /**
     * CacheHelper 的类
     */
    private Class mCacheHelperClazz;

    public static class Builder {

        private String mSpName;
        private String mSavePath;
        private Class mModelClazz;
        private String mClassName;
        private String mPkgPath;
        private String mSuffix;
        private String mPrefix;

        private Class mCacheHelperClazz;

        public Builder(Class cacheHelperClazz) {
            this.mCacheHelperClazz = cacheHelperClazz;
        }

        public Builder setSpName(String spName) {
            this.mSpName = spName;
            return this;
        }

        public Builder setSavePath(String savePath) {
            this.mSavePath = savePath;
            return this;
        }

        public Builder setModelClazz(Class modelClazz) {
            this.mModelClazz = modelClazz;
            return this;
        }

        public Builder setClassName(String className) {
            this.mClassName = className;
            return this;
        }

        public void setPkgPath(String pkgPath) {
            this.mPkgPath = pkgPath;
        }

        public void setSuffix(String suffix) {
            this.mSuffix = suffix;
        }

        public void setPrefix(String prefix) {
            this.mPrefix = prefix;
        }

        public CacheAutoCreator build() {
            return new CacheAutoCreator(this);
        }

    }

    private CacheAutoCreator(Builder builder) {

        this.mModelClazz = builder.mModelClazz;
        this.mSavePath = builder.mSavePath;
        this.mCacheHelperClazz = builder.mCacheHelperClazz;
        this.mPkgPath = builder.mPkgPath;

        String className;
        if (builder.mClassName == null || builder.mClassName.equals("")) {
            className = this.mModelClazz.getSimpleName().toLowerCase();
        } else {
            className = builder.mClassName;
        }

        className = upFirstWord(className);

        if (builder.mPrefix == null || builder.mPrefix.equals("")) {
            this.mClassName = DEFAULT_PREFIX + className;
        } else {
            this.mClassName = upFirstWord(builder.mPrefix) + className;
        }

        if (builder.mSuffix == null || builder.mSuffix.equals("")) {
            this.mClassName = className + DEFAULT_SUFFIX;
        } else {
            this.mClassName = className + upFirstWord(builder.mSuffix);
        }

        if (builder.mSpName == null || builder.mSpName.equals("")) {
            this.mSpName = this.mModelClazz.getSimpleName().toLowerCase() + SP_NAME_SUFFIX;
        } else {
            this.mSpName = builder.mSpName;
        }

    }

    private String upFirstWord(String content) {
        if (content.length() <= 0) {
            return "";
        } else {
            return content.substring(0, 1).toUpperCase() +
                    content.substring(1, content.length());
        }
    }

    /**
     * 开始创建
     */
    public void create() throws IOException {

        // private static volatile T instance;
        FieldSpec instanceField = FieldSpec.builder(mModelClazz, INSTANCE)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.VOLATILE)
                .build();

        // private final static String SP_NAME = SP_NAME;
        FieldSpec spNameField = FieldSpec.builder(String.class, SP_NAME)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$S", mSpName)
                .build();

        // private final static mGson = new Gson();
        FieldSpec gsonField = FieldSpec.builder(Gson.class, GSON_NAME)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("new $T()", Gson.class)
                .build();

        MethodSpec getDefaultMethod = createGetDefaultMethod();
        MethodSpec refreshMethod = createRefreshMethod();
        MethodSpec removeMethod = createRemoveMethod();
        MethodSpec isEmptyMethod = createIsEmptyMethod();
        MethodSpec saveMethod = createSaveMethod();

        TypeSpec cache = TypeSpec.classBuilder(mClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(instanceField)
                .addField(spNameField)
                .addField(gsonField)
                .addMethod(getDefaultMethod)
                .addMethod(refreshMethod)
                .addMethod(removeMethod)
                .addMethod(isEmptyMethod)
                .addMethod(saveMethod)
                .build();

        JavaPencil.write(mSavePath, mPkgPath, cache);

    }

    /**
     * <pre>
     * public static T getDefault() {
     *    if (instance == null) {
     *      synchronized (T.class) {
     *         if (instance == null) {
     *              refresh();
     *         }
     *      }
     *    }
     *    return instance;
     * }
     * </pre>
     */
    private MethodSpec createGetDefaultMethod() {

        return MethodSpec.methodBuilder("getDefault")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(mModelClazz)
                .beginControlFlow("if (" + INSTANCE + " == null)")
                .beginControlFlow("synchronized ($T.class)", mModelClazz)
                .beginControlFlow("if (" + INSTANCE + " == null)")
                .addStatement("refresh()")
                .endControlFlow()
                .endControlFlow()
                .endControlFlow()
                .addStatement("return " + INSTANCE)
                .build();

    }

    /**
     * <pre>
     * public static void refresh() {
     *    synchronized (T.class) {
     *       String json = CacheHelper.getInstance().getString(SP_NAME);
     *       if (TextUtils.isEmpty(json)) {
     *          instance = null;
     *       } else {
     *          instance = (T) new Gson().fromJson(json, T.class);
     *       }
     *    }
     * }
     * </pre>
     */
    private MethodSpec createRefreshMethod() {
        return MethodSpec.methodBuilder("refresh")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .beginControlFlow("synchronized ($T.class)", mModelClazz)
                .addStatement("String json = $T.getInstance().getString(SP_NAME)",
                        mCacheHelperClazz)
                .beginControlFlow("if (json == null || json.equals(\"\"))")
                .addStatement(INSTANCE + " = null")
                .endControlFlow()
                .beginControlFlow("else")
                .addStatement(INSTANCE + " = ($T) " + GSON_NAME + ".fromJson(json, $T.class)",
                        mModelClazz, mModelClazz)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * <pre>
     * public static void remove() {
     *     CacheHelper.getInstance().remove(SP_NAME);
     *     instance = null;
     * }
     * </pre>
     */
    private MethodSpec createRemoveMethod() {

        return MethodSpec.methodBuilder("remove")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addStatement("$T.getInstance().remove(SP_NAME)", mCacheHelperClazz)
                .addStatement(INSTANCE + "= null")
                .build();

    }

    /**
     * <pre>
     * public static boolean isEmpty() {
     *     return getDefault() == null;
     * }
     * </pre>
     */
    private MethodSpec createIsEmptyMethod() {
        return MethodSpec.methodBuilder("isEmpty")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(boolean.class)
                .addStatement("return getDefault() == null")
                .build();
    }

    /**
     * <pre>
     * public static void save(T model) {
     *     instance = model;
     *     String json = new Gson().toJson(model);
     *     CacheHelper.getInstance().setCache(SP_NAME, json);
     * }
     * </pre>
     */
    private MethodSpec createSaveMethod() {
        return MethodSpec.methodBuilder("save")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(mModelClazz, "model")
                .addStatement(INSTANCE + " = model")
                .addStatement("String json = " + GSON_NAME + ".toJson(model)")
                .addStatement("$T.getInstance().setCache(SP_NAME, json)", mCacheHelperClazz)
                .build();
    }

}
