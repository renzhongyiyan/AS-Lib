package com.iyuba.core.iyumooc.teacher.bean;

import java.util.List;

/**
 * 作者：renzhy on 17/7/20 20:38
 * 邮箱：renzhongyigoo@gmail.com
 */
public class CommentListBean {

    /**
     * result : 1
     * total : 3
     * question : {"questionid":"24502","question":"请问老师如何利用英语头条这个APP提高英语阅读能力？","questiondetail":"","img":"","audio":"","uid":"4582787","username":"英语小白就是我","imgsrc":"http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=4582787","answercount":"1","commentcount":"3","agreecount":"5","category1":"3","category2":"117","location":"null","app":"0","price":"10","createtime":"2017-07-07 14:57:50.0"}
     * answers : [{"answerid":"46581","answer":"多看英语头条啦","type":"1","questionid":"24502","authorid":"2551464","authortype":"2","img":"null","audio":"null","username":"zy温暖","vip":"0","imgsrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","answertime":"2017-07-18 10:40:38.0","agreecount":"0","zquestions":[],"total2":"0"},{"answerid":"46265","answer":"老师回答的非常好！此乃传道授业解惑也！谢谢老师","type":"1","questionid":"24502","authorid":"4385179","authortype":"2","img":"null","audio":"null","username":"枯死的文艺青年","vip":"1","imgsrc":"http://static1.iyuba.com/uc_server/head/2017/5/26/16/50/45/f59ba9f6-6c4f-45b6-a53a-17f3fd11eae1-m.jpg","answertime":"2017-07-08 11:56:25.0","agreecount":"0","zquestions":[],"total2":"0"},{"answerid":"46224","answer":"个人觉得，还是靠积累吧","type":"1","questionid":"24502","authorid":"4323432","authortype":"2","img":"null","audio":"null","username":"失去的灵魂","vip":"1","imgsrc":"http://static1.iyuba.com/uc_server/head/2017/4/4/19/33/26/ca50b5d2-ba94-4956-949f-d05d06adfdba-m.jpg","answertime":"2017-07-08 09:25:47.0","agreecount":"0","zquestions":[],"total2":"0"}]
     */

    private String result;
    private String total;
    private QuestionBean question;
    private List<AnswersBean> answers;

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

    public QuestionBean getQuestion() {
        return question;
    }

    public void setQuestion(QuestionBean question) {
        this.question = question;
    }

    public List<AnswersBean> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswersBean> answers) {
        this.answers = answers;
    }

    public static class QuestionBean {
        /**
         * questionid : 24502
         * question : 请问老师如何利用英语头条这个APP提高英语阅读能力？
         * questiondetail :
         * img :
         * audio :
         * uid : 4582787
         * username : 英语小白就是我
         * imgsrc : http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=4582787
         * answercount : 1
         * commentcount : 3
         * agreecount : 5
         * category1 : 3
         * category2 : 117
         * location : null
         * app : 0
         * price : 10
         * createtime : 2017-07-07 14:57:50.0
         */

        private String questionid;
        private String question;
        private String questiondetail;
        private String img;
        private String audio;
        private String uid;
        private String username;
        private String imgsrc;
        private String answercount;
        private String commentcount;
        private String agreecount;
        private String category1;
        private String category2;
        private String location;
        private String app;
        private String price;
        private String createtime;

        public String getQuestionid() {
            return questionid;
        }

        public void setQuestionid(String questionid) {
            this.questionid = questionid;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getQuestiondetail() {
            return questiondetail;
        }

        public void setQuestiondetail(String questiondetail) {
            this.questiondetail = questiondetail;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

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

        public String getAnswercount() {
            return answercount;
        }

        public void setAnswercount(String answercount) {
            this.answercount = answercount;
        }

        public String getCommentcount() {
            return commentcount;
        }

        public void setCommentcount(String commentcount) {
            this.commentcount = commentcount;
        }

        public String getAgreecount() {
            return agreecount;
        }

        public void setAgreecount(String agreecount) {
            this.agreecount = agreecount;
        }

        public String getCategory1() {
            return category1;
        }

        public void setCategory1(String category1) {
            this.category1 = category1;
        }

        public String getCategory2() {
            return category2;
        }

        public void setCategory2(String category2) {
            this.category2 = category2;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }
    }

    public static class AnswersBean {
        /**
         * answerid : 46581
         * answer : 多看英语头条啦
         * type : 1
         * questionid : 24502
         * authorid : 2551464
         * authortype : 2
         * img : null
         * audio : null
         * username : zy温暖
         * vip : 0
         * imgsrc : http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg
         * answertime : 2017-07-18 10:40:38.0
         * agreecount : 0
         * zquestions : []
         * total2 : 0
         */

        private String answerid;
        private String answer;
        private String type;
        private String questionid;
        private String authorid;
        private String authortype;
        private String img;
        private String audio;
        private String username;
        private String vip;
        private String imgsrc;
        private String answertime;
        private String agreecount;
        private String total2;
        private List<?> zquestions;

        public String getAnswerid() {
            return answerid;
        }

        public void setAnswerid(String answerid) {
            this.answerid = answerid;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getQuestionid() {
            return questionid;
        }

        public void setQuestionid(String questionid) {
            this.questionid = questionid;
        }

        public String getAuthorid() {
            return authorid;
        }

        public void setAuthorid(String authorid) {
            this.authorid = authorid;
        }

        public String getAuthortype() {
            return authortype;
        }

        public void setAuthortype(String authortype) {
            this.authortype = authortype;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getImgsrc() {
            return imgsrc;
        }

        public void setImgsrc(String imgsrc) {
            this.imgsrc = imgsrc;
        }

        public String getAnswertime() {
            return answertime;
        }

        public void setAnswertime(String answertime) {
            this.answertime = answertime;
        }

        public String getAgreecount() {
            return agreecount;
        }

        public void setAgreecount(String agreecount) {
            this.agreecount = agreecount;
        }

        public String getTotal2() {
            return total2;
        }

        public void setTotal2(String total2) {
            this.total2 = total2;
        }

        public List<?> getZquestions() {
            return zquestions;
        }

        public void setZquestions(List<?> zquestions) {
            this.zquestions = zquestions;
        }
    }
}
