/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.cos.xml.transfer;

/**
 * 传输相关配置
 */

public class TransferConfig {

    /**
     * 启用分块复制的最小对象大小
     */
    protected long divisionForCopy;
    /**
     * 分块复制时的分块大小
     */
    protected long sliceSizeForCopy;
    /**
     * 启用分块上传的最小对象大小
     */
    protected long divisionForUpload;
    /**
     * 分块上传时的分块大小
     */
    protected long sliceSizeForUpload;

    private boolean forceSimpleUpload;

    TransferConfig(Builder builder){
        this.divisionForCopy = builder.divisionForCopy;
        this.sliceSizeForCopy = builder.sliceSizeForCopy;
        this.divisionForUpload = builder.divisionForUpload;
        this.sliceSizeForUpload = builder.sliceSizeForUpload;
        this.forceSimpleUpload = builder.forceSimpleUpload;
    }

    /**
     * 获取启用分块复制的最小对象大小
     * @return 启用分块复制的最小对象大小
     */
    public long getDivisionForCopy() {
        return divisionForCopy;
    }

    public boolean isForceSimpleUpload() {
        return forceSimpleUpload;
    }

    public static class Builder{
        private long divisionForCopy = 5242880; // 5M
        private long sliceSizeForCopy = 5242880; //5M
        private long divisionForUpload = 2097152; //2M
        private long sliceSizeForUpload = 1048576; // 1M
        private boolean forceSimpleUpload = false;

        public Builder(){

        }

        /**
         * 设置启用分块复制的最小对象大小
         * @param division 启用分块复制的最小对象大小
         */
        public Builder setDividsionForCopy(long division){
            if(division > 0){
                this.divisionForCopy = division;
            }
            return this;
        }

        /**
         * 设置启用分块上传的最小对象大小
         * @param division 启用分块上传的最小对象大小
         */
        public Builder setDivisionForUpload(long division){
            if(division > 0){
                this.divisionForUpload = division;
            }
            return this;
        }

        /**
         * 设置分块复制时的分块大小
         * @param sliceSize 分块复制时的分块大小
         */
        public Builder setSliceSizeForCopy(long sliceSize){
            if(sliceSize > 0){
                this.sliceSizeForCopy = sliceSize;
            }
            return this;
        }

        /**
         * 设置分块上传时的分块大小
         * @param sliceSizee 分块上传时的分块大小
         */
        public Builder setSliceSizeForUpload(long sliceSizee){
            if(sliceSizee > 0){
                this.sliceSizeForUpload = sliceSizee;
            }
            return this;
        }

        public Builder setForceSimpleUpload(boolean forceSimpleUpload) {
            this.forceSimpleUpload = forceSimpleUpload;
            return this;
        }

        public TransferConfig build(){
            return new TransferConfig(this);
        }
    }
}
