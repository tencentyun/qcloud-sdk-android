package com.tencent.cos.xml.model.tag;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by rickenwang on 2019-12-05.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
public class Tagging {

    public TagSet tagSet;

    public Tagging() {
        tagSet = new TagSet();
    }

    public static class TagSet {

        public List<Tag> tags;

        public TagSet() {
            tags = new LinkedList<>();
        }

        public void addTag(Tag tag) {
            tags.add(tag);
        }
    }

    public static class Tag {

        public String key;

        public String value;

        public Tag(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public Tag() {}
    }
}
