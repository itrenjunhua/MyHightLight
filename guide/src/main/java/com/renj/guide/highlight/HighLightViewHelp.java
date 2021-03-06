package com.renj.guide.highlight;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * ======================================================================
 * <p/>
 * 作者：Renj
 * <p/>
 * 创建时间：2016-08-02    17:18
 * <p/>
 * 描述：操作引导工具帮助类——高亮显示部分控件类型帮助类
 * <p/>
 * 修订历史：
 * <p/>
 * ======================================================================
 */
public class HighLightViewHelp {
    private List<HighLightViewPage> highLightViewPages;
    private OnHighLightViewRemoveListener onHighLightViewRemoveListener;

    /**
     * 设置移除高亮监听
     */
    public void setOnHighLightViewRemoveListener(OnHighLightViewRemoveListener onHighLightViewRemoveListener) {
        this.onHighLightViewRemoveListener = onHighLightViewRemoveListener;
    }

    /**
     * 构造函数
     */
    public HighLightViewHelp() {
        highLightViewPages = new ArrayList<>();
    }

    /**
     * 增加一个高亮的布局。调用 {@link #showHighLightView()} 方法开始显示
     *
     * @param rHighLightPageParams {@link RHighLightPageParams} 对象
     * @param rHighLightViewParams {@link RHighLightViewParams} 对象
     */
    public HighLightViewHelp addHighLightView(final @NonNull RHighLightPageParams rHighLightPageParams,
                                              final @NonNull RHighLightViewParams rHighLightViewParams) {
        checkHighLightPageParams(rHighLightPageParams);
        checkHighLightViewParams(rHighLightViewParams);

        HighLightViewPage highLightViewPage = new HighLightViewPage(rHighLightPageParams);
        highLightViewPage.addHighLight(rHighLightViewParams);
        highLightViewPage.setOnRemoveViewListener(new HighLightViewPage.OnRemoveViewListener() {
            @Override
            public void onRemove(HighLightViewPage highLightViewPage) {
                highLightViewPages.remove(highLightViewPage);
                // 回调移除方法
                callBackRemoveListener(highLightViewPages.isEmpty(), highLightViewPages);
                if (rHighLightPageParams.autoShowNext)
                    showNext();
            }
        });
        highLightViewPages.add(highLightViewPage);
        return this;
    }


    /**
     * 增加一个高亮的布局，一个界面需要有多个地方高亮时调用。调用 {@link #showHighLightView()} 方法开始显示
     *
     * @param rHighLightPageParams   {@link RHighLightPageParams} 对象
     * @param rHighLightBgParamsList {@link RHighLightViewParams} 对象集合
     * @return {@link HighLightViewHelp} 类对象
     */
    public HighLightViewHelp addHighLightView(final @NonNull RHighLightPageParams rHighLightPageParams,
                                              final @NonNull List<RHighLightViewParams> rHighLightBgParamsList) {
        checkHighLightPageParams(rHighLightPageParams);

        if (rHighLightBgParamsList == null) {
            throw new IllegalArgumentException("Params rHighLightBgParamsList is null!");
        }
        if (rHighLightBgParamsList.isEmpty()) return this;

        HighLightViewPage highLightViewPage = new HighLightViewPage(rHighLightPageParams);
        for (RHighLightViewParams rHighLightViewParams : rHighLightBgParamsList) {
            checkHighLightViewParams(rHighLightViewParams);
            highLightViewPage.addHighLight(rHighLightViewParams);
        }
        highLightViewPage.setOnRemoveViewListener(new HighLightViewPage.OnRemoveViewListener() {
            @Override
            public void onRemove(HighLightViewPage highLightViewPage) {
                highLightViewPages.remove(highLightViewPage);
                // 回调移除方法
                callBackRemoveListener(highLightViewPages.isEmpty(), highLightViewPages);
                if (rHighLightPageParams.autoShowNext) {
                    showNext();
                }
            }
        });
        highLightViewPages.add(highLightViewPage);
        return this;
    }

    /**
     * 显示下一个高亮布局
     */
    private void showNext() {
        // 如果有没有移除的高亮布局，就先移除
        if (highLightViewPages.isEmpty()) return;
        highLightViewPages.get(0).remove();

        if (highLightViewPages.isEmpty()) return;
        highLightViewPages.get(0).show();
    }

    /**
     * 显示高亮布局，点击之后自动显示下一个高亮视图，如果有上一个高亮没有移除，会自动移除掉
     *
     * @return 当前显示的高亮View
     */
    public void showHighLightView() {
        showNext();
    }

    /**
     * 移除指定的高亮Page，默认会同时清除其他的高亮Page，{@link #removeHighLightView(boolean)}
     *
     * @see #removeHighLightView(boolean)
     * @see #skipAllHighLightView()
     */
    public void removeHighLightView() {
        removeHighLightView(true);
    }

    /**
     * 移除指定的高亮Page，并设置是否需要移除其他的高亮Page。<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 如果移除（clearOtherCoverView值传true），那么该页面就不会在显示高亮Page了，除非再次添加和显示<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 如果不移除（false）并且后面还有，那么可以继续调用 {@link #showHighLightView()} 方法显示。
     *
     * @param clearOtherHighLightView 是否清除其他的高亮Page
     * @see #removeHighLightView()
     * @see #skipAllHighLightView()
     */
    public void removeHighLightView(boolean clearOtherHighLightView) {
        if (highLightViewPages.isEmpty()) return;
        highLightViewPages.get(0).remove();

        if (clearOtherHighLightView) {
            skipAllHighLightView();
            callBackRemoveListener(false, highLightViewPages);
        } else {
            callBackRemoveListener(highLightViewPages.isEmpty(), highLightViewPages);
        }
    }

    /**
     * 移除后面的高亮Page/跳过后面所有的高亮Page
     *
     * @see #removeHighLightView()
     * @see #removeHighLightView(boolean)
     */
    public void skipAllHighLightView() {
        highLightViewPages.clear();
    }

    /**
     * 回调方法
     *
     * @param hasHighLight           是否还有已添加，但未显示的高亮
     * @param notShownHighLightViews 未显示的高亮信息集合，如果 hasHighLight 为false，集合元素为空
     */
    private void callBackRemoveListener(boolean hasHighLight, List<HighLightViewPage> notShownHighLightViews) {
        if (onHighLightViewRemoveListener != null) {
            List<HighLightPageInfo> result = new ArrayList<>();
            for (HighLightViewPage notShownHighLightView : notShownHighLightViews) {
                result.add(new HighLightPageInfo(notShownHighLightView.rHighLightPageParams, notShownHighLightView.highLightViewParams));
            }
            onHighLightViewRemoveListener.onRemoveHighLightView(hasHighLight, result);
        }
    }

    private void checkHighLightPageParams(RHighLightPageParams rHighLightPageParams) {
        if (rHighLightPageParams == null) {
            throw new IllegalArgumentException("addHighLightView() params rHighLightPageParams is null!");
        }
    }

    private void checkHighLightViewParams(RHighLightViewParams rHighLightViewParams) {
        if (rHighLightViewParams == null) {
            throw new IllegalArgumentException("addHighLightView() params rHighLightViewParams is null!");
        }
        if (rHighLightViewParams.onPosCallback == null) {
            throw new IllegalArgumentException("Couldn't find the OnPosCallback." +
                    "Call the RHighLightViewParams#setOnPosCallback(OnPosCallback) method.");
        }

        if (rHighLightViewParams.decorLayoutId == -1 && rHighLightViewParams.decorLayoutView == null) {
            throw new IllegalArgumentException("Params Exception: No decorative layout information is highlighted!");
        }
    }

    /**
     * 遮罩移除监听
     */
    public interface OnHighLightViewRemoveListener {
        /**
         * 高亮移除监听，如果是手动调用 {@link #skipAllHighLightView()} 方法，不会回调该方法
         *
         * @param hasHighLight           是否还有已添加，但未显示的高亮
         * @param notShownHighLightViews 未显示的高亮信息集合，如果 hasHighLight 为false，集合元素为空
         */
        void onRemoveHighLightView(boolean hasHighLight, List<HighLightPageInfo> notShownHighLightViews);
    }

    public static class HighLightPageInfo {
        public RHighLightPageParams rHighLightPageParams;
        public List<RHighLightViewParams> highLightViewParams;

        HighLightPageInfo(RHighLightPageParams rHighLightPageParams, List<RHighLightViewParams> highLightViewParams) {
            this.rHighLightPageParams = rHighLightPageParams;
            this.highLightViewParams = highLightViewParams;
        }
    }
}
