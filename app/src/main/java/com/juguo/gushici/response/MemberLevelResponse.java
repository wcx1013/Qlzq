package com.juguo.gushici.response;


import com.juguo.gushici.base.BaseResponse;

import java.util.List;

public class MemberLevelResponse extends BaseResponse {

    private List<MemberLevelInfo> list;

    public List<MemberLevelInfo> getList() {
        return list;
    }

    public void setList(List<MemberLevelInfo> list) {
        this.list = list;
    }

    public class MemberLevelInfo {

        private int price; // 真实价格
        private int originalPrice; // 原价费用
        private String code;// 永久：YJ  年度：ND  月度:YD
        private String goodId;
        private String id; // id

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoodId() {
            return goodId;
        }

        public void setGoodId(String goodId) {
            this.goodId = goodId;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(int originalPrice) {
            this.originalPrice = originalPrice;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
