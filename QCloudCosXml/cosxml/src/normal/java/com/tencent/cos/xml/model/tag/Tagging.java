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

package com.tencent.cos.xml.model.tag;

import java.util.LinkedList;
import java.util.List;

/**
 * 标签集合
 */
public class Tagging {

    /**
     * 标签集合
     */
    public TagSet tagSet;

    public Tagging() {
        tagSet = new TagSet();
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }

        if (anObject instanceof Tagging) {
            Tagging anotherTagging = (Tagging) anObject;
            return this.tagSet.equals(anotherTagging.tagSet);
        }
        return false;
    }

    /**
     * 标签集合
     */
    public static class TagSet {

        /**
         * 标签集合, 最多支持50个标签
         */
        public List<Tag> tags;

        public TagSet() {
            tags = new LinkedList<>();
        }

        public void addTag(Tag tag) {
            tags.add(tag);
        }

        @Override
        public boolean equals(Object anObject) {
            if (this == anObject) {
                return true;
            }

            if (anObject instanceof TagSet) {
                TagSet anotherTagSet = (TagSet) anObject;
                int size = tags.size();
                if (size == anotherTagSet.tags.size()) {
                    int i = 0;
                    while (size-- != 0) {
                        if (!tags.get(i).equals(anotherTagSet.tags.get(i)))
                            return false;
                        i++;
                    }
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 标签
     */
    public static class Tag {
        /**
         * 标签键, 长度不超过128字节，支持英文字母、数字、空格、加号、减号、下划线、等号、点号、冒号、斜线
         */
        public String key;

        /**
         * 标签值, 长度不超过256字节，支持英文字母、数字、空格、加号、减号、下划线、等号、点号、冒号、斜线
         */
        public String value;

        public Tag(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public Tag() {
        }

        @Override
        public boolean equals(Object anObject) {
            if (this == anObject) {
                return true;
            }

            if (anObject instanceof Tag) {
                Tag anotherTag = (Tag) anObject;
                return this.key.equals(anotherTag.key) && this.value.equals(anotherTag.value);
            }
            return false;
        }
    }
}
