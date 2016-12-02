package com.allen.send_message.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HoomSun on 2016/12/2.
 */

public class ImageFloder {

    /**
     //         * 图片的文件夹路径
     //         */
        private String dir;

        /**
         * 第一张图片的路径
         */
        private String firstImagePath;
        /**
         * 文件夹的名称
         */
        private String name;

        public List<ImageItem> images = new ArrayList<ImageItem>();

        public String getFirstImagePath() {
            return firstImagePath;
        }

        public void setFirstImagePath(String firstImagePath) {
            this.firstImagePath = firstImagePath;
        }
}
