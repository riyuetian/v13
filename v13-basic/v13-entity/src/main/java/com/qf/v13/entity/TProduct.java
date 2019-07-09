package com.qf.v13.entity;


import java.io.Serializable;

/**
 *实体类
 * 需要继承Serializable  序列化
 *
 *      1、将对象的状态保存在存储媒体中以便可以在以后重新创建出完全相同的副本；
        2、按值将对象从一个应用程序域发送至另一个应用程序域。

 序列化和反序列化。序列化是这个过程的第一部分，将数据分解成字节流，以便存储在文件中或在网络上传输。
 反序列化就是打开字节流并重构对象。对象序列化不仅要将基本数据类型转换成字节表示，有时还要恢复数据。恢复数据要求有恢复数据的对象实例


     实现serializable接口的作用是就是可以把对象存到字节流，然后可以恢复。
     所以你想如果你的对象没实现序列化怎么才能进行网络传输呢，要网络传输就得转为字节流，
     所以在分布式应用中，你就得实现序列化，如果你不需要分布式应用，那就没那个必要实现序列化。
 *
 */
public class TProduct implements Serializable{
    private Long id;

    private String name;

    private Long price;

    private Long salePrice;

    private String images;

    private String salePoint;

    private Long typeId;

    private String typeName;

    private Boolean flag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Long salePrice) {
        this.salePrice = salePrice;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images == null ? null : images.trim();
    }

    public String getSalePoint() {
        return salePoint;
    }

    public void setSalePoint(String salePoint) {
        this.salePoint = salePoint == null ? null : salePoint.trim();
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "TProduct{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", salePrice=" + salePrice +
                ", images='" + images + '\'' +
                ", salePoint='" + salePoint + '\'' +
                ", typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", flag=" + flag +
                '}';
    }
}