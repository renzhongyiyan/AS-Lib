package com.iyuba.core.iyumooc.teacher.bean;

import java.util.List;

/**
 * 作者：renzhy on 17/7/21 16:53
 * 邮箱：renzhongyigoo@gmail.com
 */
public class AnswerListBean {

    /**
     * result : 1
     * total : 1
     * question : {"questionid":"24621","question":"老师您好，我现在是个大二学生，英语笔试还不错，就是听力很差。初中才开始正式接触英语，但学校的英语学习基本是以应试考试为主，不太注重听力练习，所以导致现在听力很弱，跟学校外国留学生聊天也是很困难，所以现在很苦恼。老师，您有什么快速且有效提高英语听力的方法吗？在线等挺急的！","questiondetail":"","img":"","audio":"","uid":"4608260","username":"Harlann","imgsrc":"http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=4608260","answercount":"1","commentcount":"4","agreecount":"7","category1":"2","category2":"101","location":"null","app":"0","price":"100","createtime":"2017-07-13 11:45:48.0"}
     * answers : [{"answerid":"46440","answer":"英语听力本来就是需要多练多听才有效果的，胖子也不是一天就吃胖的，所有呢，英语学习没有捷径可以走，必须得下功夫，但是要选对方法会很有帮助的。最基础的是单词，所以单词要每天坚持记，对听力很有帮助，如果听力基础差点的话可以每天坚持听VOA慢速英语，还不错的话可以听VOA常速英语，听歌学英语也是轻松学英语的好选择。日积月累，相信你的英语一定会取得意想不到的效果！","type":"1","questionid":"24621","authorid":"4364550","authortype":"1","img":"null","audio":"null","username":"闫倩楠","imgsrc":"http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=4364550","answertime":"2017-07-13 13:45:48.0","agreecount":"0","timg":"image/2017/6/5/13/43/35/18cb05e5-7583-4950-aaf8-fa53233e55cc.jpg","zquestions":[],"total2":"0"}]
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
         * questionid : 24621
         * question : 老师您好，我现在是个大二学生，英语笔试还不错，就是听力很差。初中才开始正式接触英语，但学校的英语学习基本是以应试考试为主，不太注重听力练习，所以导致现在听力很弱，跟学校外国留学生聊天也是很困难，所以现在很苦恼。老师，您有什么快速且有效提高英语听力的方法吗？在线等挺急的！
         * questiondetail :
         * img :
         * audio :
         * uid : 4608260
         * username : Harlann
         * imgsrc : http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=4608260
         * answercount : 1
         * commentcount : 4
         * agreecount : 7
         * category1 : 2
         * category2 : 101
         * location : null
         * app : 0
         * price : 100
         * createtime : 2017-07-13 11:45:48.0
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
         * answerid : 46440
         * answer : 英语听力本来就是需要多练多听才有效果的，胖子也不是一天就吃胖的，所有呢，英语学习没有捷径可以走，必须得下功夫，但是要选对方法会很有帮助的。最基础的是单词，所以单词要每天坚持记，对听力很有帮助，如果听力基础差点的话可以每天坚持听VOA慢速英语，还不错的话可以听VOA常速英语，听歌学英语也是轻松学英语的好选择。日积月累，相信你的英语一定会取得意想不到的效果！
         * type : 1
         * questionid : 24621
         * authorid : 4364550
         * authortype : 1
         * img : null
         * audio : null
         * username : 闫倩楠
         * imgsrc : http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=4364550
         * answertime : 2017-07-13 13:45:48.0
         * agreecount : 0
         * timg : image/2017/6/5/13/43/35/18cb05e5-7583-4950-aaf8-fa53233e55cc.jpg
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
        private String imgsrc;
        private String answertime;
        private String agreecount;
        private String timg;
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

        public String getTimg() {
            return timg;
        }

        public void setTimg(String timg) {
            this.timg = timg;
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
