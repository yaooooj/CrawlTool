package com.example.coustomtoolbar.Bean;

import java.util.List;

/**
 * Created by SEELE on 2017/7/26.
 */

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
