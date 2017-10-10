package com.iyuba.core.iyumooc.teacher.bean;

import java.util.List;

/**
 * 作者：renzhy on 17/7/8 17:50
 * 邮箱：renzhongyigoo@gmail.com
 */
public class AgreeListBean {

    /**
     * result : 1
     * total : 3
     * data : [{"uid":"4043799","username":"iyu_Molly","imgsrc":"http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=4043799"},{"uid":"4385179","username":"枯死的文艺青年","imgsrc":"http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=4385179"},{"uid":"4582787","username":"英语小白就是我","imgsrc":"http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=4582787"}]
     */

    private String result;
    private String total;
    private List<AgreeDataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<AgreeDataBean> getData() {
        return data;
    }

    public void setData(List<AgreeDataBean> data) {
        this.data = data;
    }

    public static class AgreeDataBean {
        /**
         * uid : 4043799
         * username : iyu_Molly
         * imgsrc : http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=4043799
         */

        private String uid;
        private String username;
        private String imgsrc;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getImgsrc() {
            return imgsrc;
        }

        public void setImgsrc(String imgsrc) {
            this.imgsrc = imgsrc;
        }
    }
}
