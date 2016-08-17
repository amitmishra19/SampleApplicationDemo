package com.example.codingtest.beans;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Pojo class for facts json response
 */

public class FactsDataModel {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("rows")
    @Expose
    private List<Fact> factsList = new ArrayList<Fact>();

    /**
     *
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     *     The rows
     */
    public List<Fact> getRows() {
        return factsList;
    }

    /**
     *
     * @param factses
     *     The factses
     */
    public void setRows(List<Fact> factses) {
        this.factsList = factses;
    }

}
