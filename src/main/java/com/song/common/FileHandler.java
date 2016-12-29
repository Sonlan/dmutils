package com.song.common;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Song on 2016/9/30.
 * 根据文件格式，读取样本数据
 * 支持xls文件，txt文件，以及mysql数据库
 */
public class FileHandler {

    public static Map<String,Object> read(String filePath, String ext)throws IOException{
        File file = new File(filePath);
        String [][] originData = null;
        if(ext.equals("xls")){
            originData =  ExcelHandler.getData(file,0);
        }

        Map<String,Object> map = new HashMap<String, Object>();
        if(null != originData && 0!= originData.length){
            map.put("labels",originData[0]);
            String [][] data1 = new String[originData.length-1][];
            System.arraycopy(originData,1,data1,0,originData.length-1);
            map.put("data",data1);
        }
        return map;
    }
}
