package com.abc.lib_multi_download.downloadStrategy;

import com.abc.lib_multi_download.exception.DownloadException;
import com.abc.lib_log.JLogUtils;

/**
 * author       : frog
 * time         : 2019-10-14 11:05
 * desc         :
 * version      :
 */
public interface IStrategy {

    void stop(JLogUtils log);

    boolean run() throws DownloadException;

}
