package com.tysq.lib_creator_cache;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;

public class JavaPencil {

    public static void write(String path,
                             String pkgName,
                             TypeSpec typeSpec) throws IOException {
        // 生成java类
        JavaFile javaFile = JavaFile
                .builder(pkgName, typeSpec)
                .build();

        // 输出文件，注意这里不用再带类名
        javaFile.writeTo(new File(path));
    }

}
