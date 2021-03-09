package com.juguo.gushici.response;


import com.juguo.gushici.base.BaseResponse;

public class QueryOrderResponse extends BaseResponse {

    private QueryOrderInfo result;

    public QueryOrderInfo getResult() {
        return result;
    }

    public void setResult(QueryOrderInfo result) {
        this.result = result;
    }

    public class QueryOrderInfo {

//        ("0","等待付款"),
//        ("1","交易关闭"),//超时/关闭/退款
//        ("2","交易成功"), //可退款
//        ("3","交易完成"),

        private String orderId;
        private String orderStatus;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }
    }
}
