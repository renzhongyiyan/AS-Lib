package com.iyuba.core.me.sqlite.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/4.
 */

public class RankUser implements Serializable {

    private String uid = "";
    private String wpm = "";
    private String name = "";
    private String cnt = "";
    private String words = "";
    private String ranking = "";
    private String sort = "";
    private String imgSrc = "";

    public RankUser() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWpm() {
        return wpm;
    }

    public void setWpm(String wpm) {
        this.wpm = wpm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    @Override
    public String toString() {
        return "RankUser [uid=" + uid + ", wpm=" + wpm + ", name=" + name + ", cnt="
                + cnt + ", words=" + words + ", ranking=" + ranking
                + ", sort=" + sort + ", imgSrc=" + imgSrc + "]";
    }
}
