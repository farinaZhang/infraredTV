// Generated code from Butter Knife. Do not modify!
package com.sample.inferentdemo.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PlayDateRecycleView$$ViewInjector {
  public static void inject(Finder finder, final com.sample.inferentdemo.widget.PlayDateRecycleView target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361866, "field 'mPlayTimeList'");
    target.mPlayTimeList = (android.support.v7.widget.RecyclerView) view;
    view = finder.findRequiredView(source, 2131361865, "field 'mTextViewInfo'");
    target.mTextViewInfo = (android.widget.TextView) view;
  }

  public static void reset(com.sample.inferentdemo.widget.PlayDateRecycleView target) {
    target.mPlayTimeList = null;
    target.mTextViewInfo = null;
  }
}
