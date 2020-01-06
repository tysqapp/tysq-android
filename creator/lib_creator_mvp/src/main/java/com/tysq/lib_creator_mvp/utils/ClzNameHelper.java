package com.tysq.lib_creator_mvp.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author a2
 * @date 创建时间：2018/12/12
 * @description
 */
public class ClzNameHelper {

    private static ClzNameHelper instance;

    private ClzNameHelper() {
    }

    public static ClzNameHelper getInstance() {
        if (instance == null) {
            synchronized (ClzNameHelper.class) {
                if (instance == null) {
                    instance = new ClzNameHelper();
                }
            }
        }
        return instance;
    }

    private String clzNamePre;
    private Class applicationClazz;

    public static final String VIEW = "View";
    public static final String PRESENTER = "Presenter";
    public static final String ACTIVITY = "Activity";
    public static final String FRAGMENT = "Fragment";
    public static final String MODULE = "Module";
    public static final String COMPONENT = "Component";
    public static final String SCOPE = "SCOPE";

    private final Map<String, String> typeName = new HashMap<>();

    private final Map<String, Class> typeClass = new HashMap<>();

    private String prefixName;

    public void init(String prefixName) {
        this.prefixName = prefixName;
        clzNamePre = prefixName.substring(0, 1).toUpperCase() + prefixName.substring(1);

        typeName.put(VIEW, clzNamePre + VIEW);
        typeName.put(PRESENTER, clzNamePre + PRESENTER);
        typeName.put(ACTIVITY, clzNamePre + ACTIVITY);
        typeName.put(FRAGMENT, clzNamePre + FRAGMENT);
        typeName.put(MODULE, clzNamePre + MODULE);
        typeName.put(COMPONENT, clzNamePre + COMPONENT);

    }

    public String getName(String type) {
        return typeName.get(type);
    }

    Map<String, String> getTypeName() {
        return typeName;
    }

    /**
     * 添加对应的基类
     *
     * @param type
     * @param clz
     */
    public ClzNameHelper putClass(String type, Class clz) {
        typeClass.put(type, clz);
        return this;
    }

    public Class getClass(String type) {
        return typeClass.get(type);
    }

    public String getPrefixName() {
        return prefixName;
    }

    /**
     * 设置application的class
     *
     * @param applicationClazz
     */
    public void setAppClass(Class applicationClazz) {
        this.applicationClazz = applicationClazz;
    }

    public Class getAppClass() {
        return applicationClazz;
    }
}
