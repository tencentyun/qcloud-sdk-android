# QCloudXml

使用注解处理为Java模型类生成xml序列化器/反序列化器（解析器）

### Gradle引入

```
    implementation project(':xmlAnnoation')
    implementation project(':xmlCore')
    annotationProcessor project(':xmlCompiler')
```

### 项目说明

- xmlAnnoation：定义注解
- xmlCore：给外部提供XML序列化与反序列化的方法
- xmlCompiler：编译时生成Java模型类的Xml Adapter

### 使用说明
1. 按照XML规则定义Java模型类并添加注解
```
<ListAllMyBucketsResult>
    <Owner>
        <ID>string</ID>
        <DisplayName>string</DisplayName>
    </Owner>
    <Buckets>
        <Bucket>
            <Name>string</Name>
            <Location>Enum</Location>
            <CreationDate>date</CreationDate>
        </Bucket>
        <Bucket>
            <Name>string</Name>
            <Location>Enum</Location>
            <CreationDate>date</CreationDate>
        </Bucket>
    </Buckets>
</ListAllMyBucketsResult>
```

```
@XmlBean(name = "ListAllMyBucketsResult")
public class ListAllMyBuckets {
    /**
     * 存储桶持有者信息
     */
    public Owner owner;
    /**
     * 存储桶列表
     */
    public List<Bucket> buckets;

    /**
     * 存储桶持有者
     */
    @XmlBean
    public static class Owner{
        /**
         * 存储桶持有者的完整 ID
         */
        @XmlElement(name = "ID")
        public String id;
        /**
         * 存储桶持有者的名字
         */
        @XmlElement(name = "DisplayName")
        public String disPlayName;
    }

    /**
     * 存储桶
     */
    @XmlBean
    public static class Bucket{
        /**
         * 存储桶的名称
         */
        public String name;
        /**
         * 存储桶所在地域
         */
        public String location;
        /**
         * 存储桶的创建时间，为 ISO8601 格式，例如2019-05-24T10:56:40Z
         */
        @XmlElement(name = "CreationDate")
        public String createDate;
        public String type;
    }
}
```

2. 反序列化
```
    ListAllMyBuckets listAllMyBuckets = QCloudXml.fromXml(inputStream, ListAllMyBuckets.class);
```

3. 序列化
```
    String xmlStr = QCloudXml.toXml(listAllMyBuckets);
```

### 映射规则
默认规则：
XML节点名---类名
XML节点名---字段名首字母大写

可以通过注解的name属性进行映射自定义，比如：
@XmlElement(name = "CreationDate")