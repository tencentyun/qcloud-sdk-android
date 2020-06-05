package com.tencent.cos.xml.common;

/**
 * Created by bradyxiao on 2017/12/5.
 * 请求头 Range: bytes=x-x
 * <ul>
 *     <li>表示头500个字节：bytes=0-499 </li>
 *     <li>表示第二个500字节：bytes=500-999</li>
 *     <li>表示500字节以后的范围：bytes=500- </li>
 *     <li>表示最后500个字节：bytes=-500 :暂不支持</li>
 * </ul>
 *　　
 */

public class Range {
    private long start;
    private long end;
    public Range(long start, long end){
        this.start = start;
        this.end = end;
    }

    public Range(long start){
        this(start, -1);
    }

    public long getEnd() {
        return end;
    }

    public long getStart() {
        return start;
    }

    public String getRange(){
        return String.format("bytes=%s-%s", start, (end == -1 ?"": String.valueOf(end)));
    }

}
