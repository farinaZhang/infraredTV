// Generated code from Butter Knife. Do not modify!
package com.sample.inferentdemo.imain;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainSetingsActivity$$ViewInjector {
  public static void inject(Finder finder, final com.sample.inferentdemo.imain.MainSetingsActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361802, "field 'mIcnClose'");
    target.mIcnClose = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131361803, "field 'mShakeSettings'");
    target.mShakeSettings = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131361804, "field 'mVolumnSettings'");
    target.mVolumnSettings = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131361805, "field 'mOperatorSettngs'");
    target.mOperatorSettngs = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131361806, "field 'mAboatSettings'");
    target.mAboatSettings = (android.widget.ImageView) view;
  }

  public static void reset(com.sample.inferentdemo.imain.MainSetingsActivity target) {
    target.mIcnClose = null;
    target.mShakeSettings = null;
    target.mVolumnSettings = null;
    target.mOperatorSettngs = null;
    target.mAboatSettings = null;
  }
}
