// Generated code from Butter Knife. Do not modify!
package com.sample.inferentdemo.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PlayDateViewExt$$ViewInjector {
  public static void inject(Finder finder, final com.sample.inferentdemo.widget.PlayDateViewExt target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361868, "field 'mViewPager'");
    target.mViewPager = (android.support.v4.view.ViewPager) view;
    view = finder.findRequiredView(source, 2131361867, "field 'mslideTabView'");
    target.mslideTabView = (com.sample.inferentdemo.widget.SlideTabView) view;
  }

  public static void reset(com.sample.inferentdemo.widget.PlayDateViewExt target) {
    target.mViewPager = null;
    target.mslideTabView = null;
  }
}
