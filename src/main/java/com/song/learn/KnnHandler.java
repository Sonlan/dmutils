package com.song.learn;

import com.song.common.DMHandler;
import com.song.utils.jama.Matrix;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Song on 2016/9/30.
 */
public class KnnHandler implements DMHandler {
    private Matrix minVals;
    private Matrix maxVals;
    private Matrix ranges;

    public KnnHandler(Matrix dataSet){
        double [][] minMax = dataSet.getMinMax();
        this.minVals = new Matrix(minMax[0],1);
        this.maxVals = new Matrix(minMax[1],1);
        this.ranges = maxVals.minus(minVals);
    }
    /**
     * 归一化特征值
     * @param dataSet 特征集
     */
    public Matrix autoNorm(Matrix dataSet){
        double[][] norm = dataSet.getArray();
        for(int j=0;j<dataSet.getColumnDimension();j++){
            for(int i=0;i<norm.length;i++){
                norm[i][j] = (norm[i][j]-minVals.get(0,j))/ranges.get(0,j);
            }
        }
        return new Matrix(norm);
    }

    /**
     * K近邻算法
     * @param sample 待评估样本
     * @param dataSet 数据集
     * @param labels 数据集中，每行数据对应的类别
     * @param rate 将距离按由小至大排列，按比例选择固定数量的类别
     */
    public double classify(Matrix sample,Matrix dataSet,Matrix labels,double rate){
        //统计样本频率
        HashMap<Double,Integer> levels = new HashMap<Double, Integer>();
        //遍历类别，得出一共有几类
        for(int i=0;i<labels.getRowDimension();i++){
            if(!levels.containsKey(labels.get(i,0))) levels.put(labels.get(i,0),0);
        }
        //获得距离，并排序
        Matrix sortedDistance = sample.distance(dataSet).expand(labels,true).sort();
        //取前num个数据
        int num = (int)Math.ceil(sortedDistance.getRowDimension()*rate);
        for(int i=0;i<num;i++){
            levels.put(sortedDistance.get(i,1),levels.get(sortedDistance.get(i,1))+1);
        }
        //按频率排序
        double targetLevel = 0;
        int count = 0;
        for(double key:levels.keySet()){
            if(levels.get(key)>count) {
                count = levels.get(key);
                targetLevel = key;
            }
        }
        return targetLevel;
    }

    public static void main(String [] args){
        Random random = new Random();
        double [][] dataSet = new double[100][4];
        for(int i=0;i<100;i++){
            for(int j=0;j<4;j++){
                dataSet[i][j]=random.nextInt(10);
            }
        }
        double [] sample = {1,2,3,4};
        double [] lables = new double[100];
        for(int i=0;i<100;i++){
            lables[i]=i/10;
        }
        KnnHandler handler = new KnnHandler(new Matrix(dataSet));
        //handler.autoNorm(new Matrix(dataSet)).print(4,3);
        System.out.println(handler.classify(new Matrix(sample,1),new Matrix(dataSet),new Matrix(lables,1).transpose(),0.3));
    }
}
