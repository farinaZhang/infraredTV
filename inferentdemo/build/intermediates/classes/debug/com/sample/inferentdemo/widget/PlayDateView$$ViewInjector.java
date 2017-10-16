// Generated code from Butter Knife. Do not modify!
package com.sample.inferentdemo.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PlayDateView$$ViewInjector {
  public static void inject(Finder finder, final com.sample.inferentdemo.widget.PlayDateView target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361866, "field 'mPlayTimeList'");
    target.mPlayTimeList = (android.support.v7.widget.RecyclerView) view;
    view = finder.findRequiredView(source, 2131361864, "field 'mDateTab'");
    target.mDateTab = (com.sample.inferentdemo.widget.HorizontalListView) view;
  }

  public static void reset(com.sample.inferentdemo.widget.PlayDateView target) {
    target.mPlayTimeList = null;
    target.mDateTab = null;
  }
}
