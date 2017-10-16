// Generated code from Butter Knife. Do not modify!
package com.sample.inferentdemo.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainSearchResultView$$ViewInjector {
  public static void inject(Finder finder, final com.sample.inferentdemo.widget.MainSearchResultView target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361860, "field 'mPlayingTab'");
    target.mPlayingTab = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131361861, "field 'mPlayingTime'");
    target.mPlayingTime = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131361862, "field 'mPlayRecyleView'");
    target.mPlayRecyleView = (android.support.v7.widget.RecyclerView) view;
    view = finder.findRequiredView(source, 2131361863, "field 'mSubSearchPlayDate'");
    target.mSubSearchPlayDate = (com.sample.inferentdemo.widget.PlayDateViewExt) view;
  }

  public static void reset(com.sample.inferentdemo.widget.MainSearchResultView target) {
    target.mPlayingTab = null;
    target.mPlayingTime = null;
    target.mPlayRecyleView = null;
    target.mSubSearchPlayDate = null;
  }
}
