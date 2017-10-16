// Generated code from Butter Knife. Do not modify!
package com.sample.inferentdemo.widget.searchview;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SearchView$$ViewInjector {
  public static void inject(Finder finder, final com.sample.inferentdemo.widget.searchview.SearchView target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361872, "field 'mSearchContentView'");
    target.mSearchContentView = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131361873, "field 'mHistoryEara'");
    target.mHistoryEara = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131361874, "field 'clearHistory'");
    target.clearHistory = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131361875, "field 'SearchlistView'");
    target.SearchlistView = (android.support.v7.widget.RecyclerView) view;
    view = finder.findRequiredView(source, 2131361876, "field 'TryListView'");
    target.TryListView = (android.support.v7.widget.RecyclerView) view;
  }

  public static void reset(com.sample.inferentdemo.widget.searchview.SearchView target) {
    target.mSearchContentView = null;
    target.mHistoryEara = null;
    target.clearHistory = null;
    target.SearchlistView = null;
    target.TryListView = null;
  }
}
