package com.allen.send_message.bean;

/**
 * Created by lenovo on 2016/12/3.
 */

public class SendMessageBean {

    /**
     * content : 测试发布动态
     * zone_id : 3
     * images : https://www.baidu.com/img/baidu_jgylogo3.gif;http://www.mamicode.com/img/logo.png
     * addr : 天安门广场
     * xy : 39.9707080078125,116.445867784288
     */

    private String content;
    private int zone_id;
    private String images;
    private String addr;
    private String xy;
    private int error_code;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getZone_id() {
        return zone_id;
    }

    public void setZone_id(int zone_id) {
        this.zone_id = zone_id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getXy() {
        return xy;
    }

    public void setXy(String xy) {
        this.xy = xy;
    }

    @Override
    public String toString() {
        return "SendMessageBean{" +
                "content='" + content + '\'' +
                ", zone_id=" + zone_id +
                ", images='" + images + '\'' +
                ", addr='" + addr + '\'' +
                ", xy='" + xy + '\'' +
                '}';
    }
}
