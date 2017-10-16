// Generated code from Butter Knife. Do not modify!
package com.sample.inferentdemo.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainShowView$$ViewInjector {
  public static void inject(Finder finder, final com.sample.inferentdemo.widget.MainShowView target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361864, "field 'mTitleTabHListView'");
    target.mTitleTabHListView = (com.sample.inferentdemo.widget.HorizontalListView) view;
    view = finder.findRequiredView(source, 2131361862, "field 'mPlayRecyleView'");
    target.mPlayRecyleView = (android.support.v7.widget.RecyclerView) view;
  }

  public static void reset(com.sample.inferentdemo.widget.MainShowView target) {
    target.mTitleTabHListView = null;
    target.mPlayRecyleView = null;
  }
}
