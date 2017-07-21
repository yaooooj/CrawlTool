package com.example.coustomtoolbar.Bean;

import java.util.List;

/**
 * Created by yaojian on 2017/7/21.
 */

public class PictureCategory {
    private String showapi_res_code;
    private String showapi_res_error;
    private String showapi_res_body;
    private String ret_code;
    private List<AllCategory> list;

    public String getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(String showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public String getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(String showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public List<AllCategory> getList() {
        return list;
    }

    public void setList(List<AllCategory> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PictureCategory{" +
                "showapi_res_code='" + showapi_res_code + '\'' +
                ", showapi_res_error='" + showapi_res_error + '\'' +
                ", showapi_res_body='" + showapi_res_body + '\'' +
                ", ret_code='" + ret_code + '\'' +
                ", list=" + list +
                '}';
    }
}
