package com.juguo.gushici.utils;

import java.util.List;

public class ListUtils {

    /**
     * List非空判断
     * @param list
     * @return
     */
    public static boolean isEmpty(List<?> list){

        if(list==null){
            return true;
        }
        if(list.size()==0){
            return true;
        }
        return false;
    }
}
