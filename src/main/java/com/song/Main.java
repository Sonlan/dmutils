package com.song;

import com.song.common.FileHandler;

import javax.imageio.ImageIO;
import java.util.Map;

/**
 * Created by Song on 2016/9/30.
 */
public class Main {
    public static void main(String [] args){
        try {
            String [] labels = null;
            String [][] data = null;
            Map<String,Object> map = FileHandler.read("test.xls","xls");
            if(null != map && (!map.isEmpty())){
                labels = (String [])map.get("labels");
                data = (String [][])map.get("data");
            }
            System.out.println("");
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }


}
