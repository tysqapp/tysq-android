package cache;

import com.bit.cache.AppCache;
import com.tysq.lib_creator_cache.CacheAutoCreator;

import java.io.IOException;

/**
 * author       : frog
 * time         : 2019/3/29 下午5:31
 * desc         :
 * version      : 1.3.0
 */
public class CacheCreator {

    public static void main(String[] args) throws IOException {
        CacheAutoCreator.Builder builder = new CacheAutoCreator.Builder(AppCache.class);

        builder.setModelClazz(Net.class);

        builder.setPkgPath("com.tysq.ty_android.local.sp");
        builder.setSavePath("app/src/main/java");

        builder.build().create();
    }

}
