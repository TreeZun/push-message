package com.yx.entity;

public enum ErrCode {

    /**
     * 成功
     */
    Success(0){
        @Override
        public String toString() {
            return "成功";
        }
    },  
    /**
     * 失败
     */
    failed(-1){
        @Override
        public String toString() {
            return "失败";
        }
    },
    /**
     * 数据为空
     */
    empty(100002){
        @Override
        public String toString() {
            return "数据为空";
        }
    },

    /**
     * 保存数据失败
     */
    Save_Failed(100001){
        @Override
        public String toString() {
            return "保存失败";
        }
    },

    PARAMETER_WRONG(100013){
        @Override
        public String toString() {
            return "参数错误";
        }
    },

    EXIST_WRONG(100014){
        @Override
        public String toString() {
            return "已存在";
        }
    },

    LOGIN_FAIL(100015){
        @Override
        public String toString() {
            return "用户名或者密码错误";
        }
    },



    NO_POWER(100016){
        @Override
        public String toString() {
            return "无权限";
        }
    }

    ,    NO_LOGIIN(100017){
        @Override
        public String toString() {
            return "请登录";
        }
    },
    USER_DEVICE_WRONG(100018){
        @Override
        public String toString() {
            return "用户绑定设备错误！";
        }
    },
    USER_DEVICES_WRONG(100019){
        @Override
        public String toString() {
            return "主持人与议题中人员相同时，设备需选择同一个";
        }
    },
    DEVICE_USER_WRONG(100020){
        @Override
        public String toString() {
            return "主持人与议题中人员相同时，设备需选择同一个";
        }
    },
    USER_REPORT_WRONG(100021){
        @Override
        public String toString() {
            return "同一个会议，多个议题人员相同时，设备需选择同一个";
        }
    },
   ;



    private int errCode;

    private ErrCode(int errCode) {
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
