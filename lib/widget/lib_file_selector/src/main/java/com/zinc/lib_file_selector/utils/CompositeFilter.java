package com.zinc.lib_file_selector.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;

public class CompositeFilter implements FileFilter, Serializable {

    private ArrayList<FileFilter> mFilters;

    public CompositeFilter(ArrayList<FileFilter> filters) {
        mFilters = filters;
    }

    @Override
    public boolean accept(File f) {
        for (FileFilter filter : mFilters) {
            if (!filter.accept(f)) {
                return false;
            }
        }

        return true;
    }
}
