package com.tysq.ty_android.net.rx;

import com.tysq.ty_android.config.NetCode;
import com.tysq.ty_android.net.exception.ServerException;

import common.RespData;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * author       : frog
 * time         : 2019/4/23 上午9:49
 * desc         : rx 拆壳 解析器
 * version      : 1.3.0
 */

public class RxParser {

    /**
     * 拆壳
     *
     * @param <T>
     * @return
     */
    public static <T> SingleTransformer<RespData<T>, T> handleSingleDataResult() {
        return new SingleTransformer<RespData<T>, T>() {
            @Override
            public SingleSource<T> apply(Single<RespData<T>> upstream) {
                return upstream
                        .map(new TransToData<T>())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }

        };
    }

    /**
     * 拆壳
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<RespData<T>, T> handleObservableDataResult() {
        return new ObservableTransformer<RespData<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<RespData<T>> upstream) {
                return upstream
                        .map(new TransToData<T>())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 不拆壳
     *
     * @param <T>
     * @return
     */
    public static <T> SingleTransformer<RespData<T>, RespData<T>> handleSingleToResult() {
        return new SingleTransformer<RespData<T>, RespData<T>>() {
            @Override
            public SingleSource<RespData<T>> apply(Single<RespData<T>> upstream) {
                return upstream
                        .map(new TransToResult<T>())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }

        };
    }

    /**
     * 拆壳
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<RespData<T>, RespData<T>> handleObservableToResult() {
        return new ObservableTransformer<RespData<T>, RespData<T>>() {
            @Override
            public ObservableSource<RespData<T>> apply(Observable<RespData<T>> upstream) {
                return upstream
                        .map(new TransToResult<T>())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private static class TransToData<T> implements Function<RespData<T>, T> {

        @Override
        public T apply(RespData<T> tResult) throws Exception {
            if (tResult.getStatus() == 0
                    || tResult.getStatus() == NetCode.CODE_INVALIDATE
                    || tResult.getStatus() == NetCode.ARTICLE_REMOVE_ERROR_CODE
                    || tResult.getStatus() == NetCode.NO_PERMISSION_CODE
                    || tResult.getStatus() == NetCode.GRADE_NO_ENOUGH_CODE
                    || tResult.getStatus() == NetCode.COIN_NO_ENOUGH_CODE
                    || tResult.getStatus() == NetCode.GRADE_NO_ENOUGH_PERMISSION_CODE) {
                if (tResult.getData() == null) {
                    return (T) new Object();
                } else {
                    return tResult.getData();
                }
            } else {
                throw new ServerException(tResult.getStatus(), tResult.getReason());
            }
        }
    }

    private static class TransToResult<T> implements Function<RespData<T>, RespData<T>> {

        @Override
        public RespData<T> apply(RespData<T> tResult) throws Exception {

            if (tResult.getStatus() == 0
                    || tResult.getStatus() == NetCode.CODE_INVALIDATE
                    || tResult.getStatus() == NetCode.ARTICLE_REMOVE_ERROR_CODE
                    || tResult.getStatus() == NetCode.NO_PERMISSION_CODE
                    || tResult.getStatus() == NetCode.GRADE_NO_ENOUGH_CODE
                    || tResult.getStatus() == NetCode.COIN_NO_ENOUGH_CODE
                    || tResult.getStatus() == NetCode.GRADE_NO_ENOUGH_PERMISSION_CODE) {
                return tResult;
            } else {
                throw new ServerException(tResult.getStatus(), tResult.getReason());
            }

        }
    }

}
