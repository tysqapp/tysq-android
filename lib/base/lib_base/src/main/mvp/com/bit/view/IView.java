package com.bit.view;

import com.bit.event.CloseEvent;
import com.bit.event.ReloadEvent;

/**
 * @author a2
 * @date 创建时间：2018/11/14
 * @description
 */
public interface IView {

    void showDialog();

    void hideDialog();

    /**
     * 进行重新刷新加载
     */
    void onReload(ReloadEvent reloadEvent);

    /**
     * 进行关闭
     */
    void onClose(CloseEvent closeEvent);

}
