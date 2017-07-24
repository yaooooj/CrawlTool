package com.example.coustomtoolbar.Bean;

import java.util.List;

/**
 * Created by yaojian on 2017/7/21.
 */

public class PictureCategory {
    private String showapi_res_code;
    private String showapi_res_error;
    private AllCategory showapi_res_body;

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

    public AllCategory getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(AllCategory showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }
    public class AllCategory {
        private String ret_code;
        private List<CategoryList> list;

        public String getRet_code() {
            return ret_code;
        }

        public void setRet_code(String ret_code) {
            this.ret_code = ret_code;
        }

        public List<CategoryList> getList() {
            return list;
        }

        public void setList(List<CategoryList> list) {
            this.list = list;
        }

        public  class CategoryList{
            private String name;
            private List<ConcreteCategory> list;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<ConcreteCategory> getList() {
                return list;
            }

            public void setList(List<ConcreteCategory> list) {
                this.list = list;
            }

            public  class ConcreteCategory{
                private String id;
                private String name;

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
            }
        }
    }

}
