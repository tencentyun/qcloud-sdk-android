package com.tencent.qcloud.qcloudxml;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "TestBeana")
public class TestBean {
    @XmlElement(name = "Aab")
    public String aaa;
    public int bbb;

    public ITestBean2 iTestBean2;
    public ITestBean1 iTestBean1;

    public List<ITestBean2> iTestBean2List;

    @XmlBean
    public static class ITestBean1{
        public String iaaa;
    }

    @XmlBean
    public static class ITestBean2{
        public int ibbb;
    }
}
