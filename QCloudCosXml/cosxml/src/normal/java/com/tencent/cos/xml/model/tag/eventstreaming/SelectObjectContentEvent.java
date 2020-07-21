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

package com.tencent.cos.xml.model.tag.eventstreaming;

import java.nio.ByteBuffer;

public class SelectObjectContentEvent {

    /**
     * The Record Event.
     */
    public static class RecordsEvent extends SelectObjectContentEvent {
        private ByteBuffer payload;

        /**
         * The byte array of partial, one or more result records.
         */
        public ByteBuffer getPayload() {
            return payload;
        }

        /**
         * The byte array of partial, one or more result records.
         */
        public void setPayload(ByteBuffer payload) {
            this.payload = payload;
        }

        /**
         * The byte array of partial, one or more result records.
         */
        public RecordsEvent withPayload(ByteBuffer payload) {
            setPayload(payload);
            return this;
        }

    }

    /**
     * The Stats Event.
     */
    public static class StatsEvent extends SelectObjectContentEvent {
        private Stats details;

        /**
         * The Stats event details.
         */
        public Stats getDetails() {
            return details;
        }

        /**
         * The Stats event details.
         */
        public void setDetails(Stats details) {
            this.details = details;
        }

        /**
         * The Stats event details.
         */
        public StatsEvent withDetails(Stats details) {
            setDetails(details);
            return this;
        }
    }

    /**
     * The Progress Event.
     */
    public static class ProgressEvent extends SelectObjectContentEvent {

        private Progress details;

        /**
         * The Progress event details.
         */
        public Progress getDetails() {
            return details;
        }

        /**
         * The Progress event details.
         */
        public void setDetails(Progress details) {
            this.details = details;
        }

        /**
         * The Progress event details.
         */
        public ProgressEvent withDetails(Progress details) {
            setDetails(details);
            return this;
        }

    }

    /**
     * The Continuation Event.
     */
    public static class ContinuationEvent extends SelectObjectContentEvent {

    }

    /**
     * The End Event.
     */
    public static class EndEvent extends SelectObjectContentEvent {

    }

    @Override
    public SelectObjectContentEvent clone() {
        try {
            return (SelectObjectContentEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
