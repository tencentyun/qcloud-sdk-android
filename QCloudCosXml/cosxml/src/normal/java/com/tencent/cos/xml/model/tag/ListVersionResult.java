package com.tencent.cos.xml.model.tag;

import java.util.List;

public class ListVersionResult {

    public String name;

    public String prefix;

    public String keyMarker;

    public String versionIdMarker;

    public String encodingType;

    public int maxKeys;

    public boolean isTruncated;

    public String delimiter;

    public String nextKeyMarker;

    public String nextVersionIdMarker;

    public List<CommonPrefixes> commonPrefixes;

    public List<Version> versions;

    public List<DeleteMarker> deleteMarkers;

    public static class CommonPrefixes {

        public String prefix;
    }


    public static class Version {

        public String key;

        public String versionID;

        public boolean isLatest;

        public String lastModified;

        public String etag;

        public long size;

        public String storageClass;

        public Owner owner;
    }

    public static class DeleteMarker {

        public String key;

        public String versionId;

        public boolean isLatest;

        public String lastModified;

        public Owner owner;
    }

    public static class Owner {

        public String id;

        public String displayName;
    }

}
