package com.sample.inferentdemo.widget.searchview;

/**
 * Created by Carson_Ho on 17/8/10.
 */

public interface ICallBack {
    //type:0:频道，1：具体节目，2：节目大分类：例如【电视剧】【电视节目】【电影】【体育】 ，3：节目小分类：例如【动作】【战争】【谍战】【革命】【喜剧等】
    void SearchAciton(String string,int type);

}
