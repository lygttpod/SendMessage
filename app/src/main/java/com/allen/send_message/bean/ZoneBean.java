package com.allen.send_message.bean;

import java.util.List;

/**
 * Created by allen on 2016/12/2.
 * 大本营实体类
 */

public class ZoneBean {

    /**
     * error_code : 0
     * data : [{"name":"空间三","admin":{"id":"","name":"神秘用户","avatar":""}}]
     */

    private int error_code;
    private List<DataBean> data;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : 空间三
         * admin : {"id":"","name":"神秘用户","avatar":""}
         */

        private String name;
        private AdminBean admin;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public AdminBean getAdmin() {
            return admin;
        }

        public void setAdmin(AdminBean admin) {
            this.admin = admin;
        }

        public static class AdminBean {
            /**
             * id :
             * name : 神秘用户
             * avatar :
             */

            private String id;
            private String name;
            private String avatar;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }
}
