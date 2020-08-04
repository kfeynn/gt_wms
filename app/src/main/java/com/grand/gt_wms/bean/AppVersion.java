package com.grand.gt_wms.bean;

/**
 * Created by Administrator on 2018/9/6.
 */

public class AppVersion {
        private String content;
        private int versionCode;
        private String versionName;
        public void setContent(String content) {
            this.content = content;
        }
        public String getContent() {
            return content;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }
        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }
        public String getVersionName() {
            return versionName;
        }

}
