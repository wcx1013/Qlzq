package com.juguo.gushici.param;

public class AddWithRemovePlanParams {

    private PlanBean param;

    public PlanBean getParam() {
        return param;
    }

    public void setParam(PlanBean param) {
        this.param = param;
    }

    public static class PlanBean{

        private String addPoemIds;
        private String rmPoemIds;

        public String getAddPoemIds() {
            return addPoemIds;
        }

        public void setAddPoemIds(String addPoemIds) {
            this.addPoemIds = addPoemIds;
        }

        public String getRmPoemIds() {
            return rmPoemIds;
        }

        public void setRmPoemIds(String rmPoemIds) {
            this.rmPoemIds = rmPoemIds;
        }
    }
}
