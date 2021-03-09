package com.juguo.gushici.response;


import com.juguo.gushici.base.BaseResponse;

public class AddPayOrderResponse extends BaseResponse {

    private AddPayOrderInfo result;

    public AddPayOrderInfo getResult() {
        return result;
    }

    public void setResult(AddPayOrderInfo result) {
        this.result = result;
    }

    public class AddPayOrderInfo {

        private String orderId;
        private String payerType; // 支付商类型 WX/ALI
        private String signOrder;

        public String getSignOrder() {
            return signOrder;
        }

        public void setSignOrder(String signOrder) {
            this.signOrder = signOrder;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPayerType() {
            return payerType;
        }

        public void setPayerType(String payerType) {
            this.payerType = payerType;
        }
    }
}
