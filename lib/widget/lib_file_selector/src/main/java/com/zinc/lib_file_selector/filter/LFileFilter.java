package com.zinc.lib_file_selector.filter;

import java.io.File;
import java.io.FileFilter;

public class LFileFilter implements FileFilter {
    private String[] mTypes;

    public LFileFilter(String[] types) {
        this.mTypes = types;
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        if (mTypes != null && mTypes.length > 0) {
            for (int i = 0; i < mTypes.length; i++) {
                if (file.getName().endsWith(mTypes[i].toLowerCase()) || file.getName().endsWith(mTypes[i].toUpperCase())) {
                    return true;
                }
            }
        }else {
            return true;
        }
        return false;
    }
}
