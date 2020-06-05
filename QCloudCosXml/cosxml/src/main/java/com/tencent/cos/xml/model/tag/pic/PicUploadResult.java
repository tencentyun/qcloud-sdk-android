package com.tencent.cos.xml.model.tag.pic;

import java.util.List;

public class PicUploadResult {

    private PicOriginalInfo originalInfo;

    private List<PicObject> processResults;

    public PicUploadResult(PicOriginalInfo originalInfo, List<PicObject> processResults) {
        this.originalInfo = originalInfo;
        this.processResults = processResults;
    }

    public List<PicObject> getProcessResults() {
        return processResults;
    }

    public PicOriginalInfo getOriginalInfo() {
        return originalInfo;
    }
}
