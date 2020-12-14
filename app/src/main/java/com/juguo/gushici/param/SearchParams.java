package com.juguo.gushici.param;

public class SearchParams {

    private SearchInfo param;

    public SearchInfo getParam() {
        return param;
    }

    public void setParam(SearchInfo param) {
        this.param = param;
    }

    public static class SearchInfo {

        private String searchKey;//搜索诗词名称或作者名称

        public String getSearchKey() {
            return searchKey;
        }

        public void setSearchKey(String searchKey) {
            this.searchKey = searchKey;
        }
    }
}