package com.juguo.gushici.bean;

import java.util.List;

public class AddPayOrderBean {

    private AddPayOrderInfo param;

    public AddPayOrderInfo getParam() {
        return param;
    }

    public void setParam(AddPayOrderInfo param) {
        this.param = param;
    }

    public static class AddPayOrderInfo {

        private String subject; // 交易标题
        private String body; // 交易商品描述
        private String userId; // 用户id
        private int amount; // 交易金额
        private String currencyType; // 交易币种
        private String recAccount; // 收款账号
        private String channel; // app渠道
        private List<GoodsListInfo> goodsList;

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public List<GoodsListInfo> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsListInfo> goodsList) {
            this.goodsList = goodsList;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCurrencyType() {
            return currencyType;
        }

        public void setCurrencyType(String currencyType) {
            this.currencyType = currencyType;
        }

        public String getRecAccount() {
            return recAccount;
        }

        public void setRecAccount(String recAccount) {
            this.recAccount = recAccount;
        }

    }

    public static class GoodsListInfo {

        private int price;
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public GoodsListInfo(int price, String id) {
            this.price = price;
            this.id = id;
        }
    }
}
