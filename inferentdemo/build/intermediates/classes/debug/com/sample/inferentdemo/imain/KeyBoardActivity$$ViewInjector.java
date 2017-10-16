// Generated code from Butter Knife. Do not modify!
package com.sample.inferentdemo.imain;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class KeyBoardActivity$$ViewInjector {
  public static void inject(Finder finder, final com.sample.inferentdemo.imain.KeyBoardActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361835, "field 'mTxtInput'");
    target.mTxtInput = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131361836, "field 'mRecycleKeyboard'");
    target.mRecycleKeyboard = (android.support.v7.widget.RecyclerView) view;
    view = finder.findRequiredView(source, 2131361819, "field 'mImgClose'");
    target.mImgClose = (android.widget.ImageView) view;
  }

  public static void reset(com.sample.inferentdemo.imain.KeyBoardActivity target) {
    target.mTxtInput = null;
    target.mRecycleKeyboard = null;
    target.mImgClose = null;
  }
}
