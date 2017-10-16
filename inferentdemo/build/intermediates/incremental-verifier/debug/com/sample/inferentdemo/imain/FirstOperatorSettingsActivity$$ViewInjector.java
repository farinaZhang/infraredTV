// Generated code from Butter Knife. Do not modify!
package com.sample.inferentdemo.imain;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class FirstOperatorSettingsActivity$$ViewInjector {
  public static void inject(Finder finder, final com.sample.inferentdemo.imain.FirstOperatorSettingsActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361801, "field 'mIcnGo'");
    target.mIcnGo = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131361799, "field 'mIcnDongfang'");
    target.mIcnDongfang = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131361800, "field 'mIcnBaushitong'");
    target.mIcnBaushitong = (android.widget.ImageView) view;
  }

  public static void reset(com.sample.inferentdemo.imain.FirstOperatorSettingsActivity target) {
    target.mIcnGo = null;
    target.mIcnDongfang = null;
    target.mIcnBaushitong = null;
  }
}
