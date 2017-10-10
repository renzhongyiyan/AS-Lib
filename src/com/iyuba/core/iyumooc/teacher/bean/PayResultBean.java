package com.iyuba.core.iyumooc.teacher.bean;

import android.support.annotation.Keep;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 作者：renzhy on 17/7/22 17:28
 * 邮箱：renzhongyigoo@gmail.com
 */
@Keep
@Root(name = "response", strict = false)
public class PayResultBean {
    @Element(required = false)
    public String result;
    @Element(required = false)
    public String msg;
    @Element(required = false)
    public String amount;
}
