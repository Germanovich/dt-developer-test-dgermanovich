package com.vizor.test.util;

import com.vizor.test.resource.ConfigurationManager;

public class Cash {

    private String searchText;
    private Integer pageNumber = Integer.parseInt(ConfigurationManager.getProperty("app.startingPageNumber"));

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
