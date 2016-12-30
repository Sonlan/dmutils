package com.song.utils;

/**
 * Created by Song on 2016/12/30.
 * 计算工具类
 */
public class Calculator {
    /**
     * 卷积操作
     * @param src1
     * @param src2
     * @param step
     * @return
     */
    public static float[][] conv(int [][] src1,float [][] src2,int step){
        int w1 = src1[0].length;
        int h1 = src1.length;

        int w2 = src2[0].length;
        int h2 = src2.length;

        if(0>=step || w1<w2 || h1<h2 || 0!= (w1-w2)%step || 0!= (h1-h2)%step ){
            //抛出异常
            return null;
        }

        int w3 = (w1-w2)/step+1;
        int h3 = (h1-h2)/step+1;
        float [][] res = new float[h3][w3];
        float temp = 0f;

        for(int i=0;i<h3;i++){
            for(int j=0;j<w3;j++){
                temp=0;
                for(int m=0;m<h2;m++){
                    for(int n=0;n<w2;n++){
                            temp += src2[m][n] * src1[i * step + m][j * step + n];
                    }
                }
                    res[i][j] = temp;
                System.out.print(res[i][j]+"\t");
            }
            System.out.println("");
        }
        return res;
    }
}
