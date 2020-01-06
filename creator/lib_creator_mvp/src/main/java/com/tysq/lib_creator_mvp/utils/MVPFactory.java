package com.tysq.lib_creator_mvp.utils;

import com.tysq.lib_creator_mvp.creator.ActivityCreator;
import com.tysq.lib_creator_mvp.creator.ComponentCreator;
import com.tysq.lib_creator_mvp.creator.FragmentCreator;
import com.tysq.lib_creator_mvp.creator.ModuleCreator;
import com.tysq.lib_creator_mvp.creator.PresenterCreator;
import com.tysq.lib_creator_mvp.creator.ViewCreator;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * MVP 的 工厂
 */
public class MVPFactory {

    private final String preFixName;
    private final String path;
    private final String pkgName;
    private final boolean isFragment;

    public static final String DI = ".di";

    public MVPFactory(String preFixName,
                      String path,
                      String pkgName,
                      boolean isFragment) {
        this.preFixName = preFixName;
        this.path = path;
        this.pkgName = pkgName
                + "."
                + preFixName.substring(0, 1).toLowerCase()
                + preFixName.substring(1, preFixName.length());
        this.isFragment = isFragment;
    }

    public void launch() throws IOException {
        // 初始化所有的
        ClzNameHelper.getInstance().init(preFixName);

        // 检测文件是否存在
        String absPath = path + "/" + pkgName.replace(".", "/");
        Map<String, String> typeName = ClzNameHelper.getInstance().getTypeName();
        for (Map.Entry<String, String> entry : typeName.entrySet()) {
            File file = new File(absPath, entry.getValue() + ".java");
            if (file.exists()) {
                throw new RuntimeException(entry.getValue() + " 类已经存在，请删除后在创建，以免覆盖");
            }
        }

        new ViewCreator(path, pkgName).create();
        new PresenterCreator(path, pkgName).create();
        if (isFragment) {
            new FragmentCreator(path, pkgName).creator();
        } else {
            new ActivityCreator(path, pkgName).creator();
        }
        new ModuleCreator(path, pkgName).creator();
        new ComponentCreator(path, pkgName, isFragment).creator();

        System.out.println("创建完成");
        System.out.println("请到以下路径查看：");
        System.out.println(absPath);
        System.out.println("本次创建文件有如下：");
        for (Map.Entry<String, String> entry : typeName.entrySet()) {
            File file = new File(absPath, entry.getValue() + ".java");
            System.out.println(file.getName());
        }
    }

}
