package com.allen.send_message.bean;

/**
 * Created by lenovo on 2016/12/3.
 */

public class UpPhotoBean {

    /**
     * attachment_id : 4409
     * name : 搜索.png
     * msg : ok
     * path : http://static.clouderwork.com/explore/78/a9/d07674b3-c716-493e-8b28-bbed030f10b4.png
     * size : 408615
     * error_code : 0
     * file_path : http://static.clouderwork.com/explore/78/a9/d07674b3-c716-493e-8b28-bbed030f10b4.png
     * md5 : 78a9a2dea7abc20ff5eff131c58b61be
     */

    private int attachment_id;
    private String name;
    private String msg;
    private String path;
    private int size;
    private int error_code;
    private String file_path;
    private String md5;

    public int getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(int attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Override
    public String toString() {
        return "UpPhotoBean{" +
                "attachment_id=" + attachment_id +
                ", name='" + name + '\'' +
                ", msg='" + msg + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", error_code=" + error_code +
                ", file_path='" + file_path + '\'' +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
