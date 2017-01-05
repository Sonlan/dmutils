package com.song.learn;

import com.song.utils.jama.Matrix;
import com.song.utils.jatree.Node;

import java.io.*;
import java.util.*;

/**
 * Created by Song on 2017/1/3.
 * 决策树
 */
public class DectreeHandler {
    /**
     * 计算数据集的香农熵
     * @param dataSet 数据集（最后一列为分类信息）
     * @return 香农熵
     */
    public static double calcShannonEnt(Matrix dataSet){
        int m = dataSet.getRowDimension();
        int n = dataSet.getColumnDimension();
        double currentLabel = 0;
        double shannonEnt = 0;
        double rate = 0;
        HashMap<Double,Integer> labelCounts = new HashMap<Double, Integer>();
        //统计各类出现次数
        for(int i=0;i<m;i++){
            currentLabel = dataSet.get(i,n-1);
            if(!labelCounts.containsKey(currentLabel))
                labelCounts.put(currentLabel,0);
            labelCounts.put(currentLabel,labelCounts.get(currentLabel)+1);
        }
        //计算整体香农熵
        for(double key:labelCounts.keySet()){
            rate =labelCounts.get(key)/(float)m;
            shannonEnt -= rate*Math.log(rate)/Math.log(2);
        }
        return shannonEnt;
    }

    /**
     * 划分数据集（当第axis维数据等于value时，提取出该行数据（）去掉第axis维）
     * @param dataSet 数据集（最后一列为分类信息）
     * @param axis 待匹配列（从0开始）
     * @param value 待匹配列值
     * @return
     */
    public Matrix splitDataSet(Matrix dataSet,int axis,double value){
        Matrix retDataSet = new Matrix(0,dataSet.getColumnDimension()-1);
        Matrix temp = new Matrix(1,dataSet.getColumnDimension()-1);
        for(int i=0;i<dataSet.getRowDimension();i++){
            if(dataSet.get(i,axis)==value){
                int k = 0;
                for(int j=0;j<dataSet.getColumnDimension();j++){
                    if(j!=axis)
                        temp.set(0,k++,dataSet.get(i,j));
                }
                retDataSet = retDataSet.expand(temp,false);
            }
        }
        return retDataSet;
    }

    /**
     * 选择最好的数据集划分方式
     * @param dataSet  数据集（最后一列为分类信息）
     * @return 香农熵最小时（增益最大）的特征值序号
     */
    public int chooseBestFeatureToSplit(Matrix dataSet){
        //特征数
        int featureNums= dataSet.getColumnDimension()-1;
        //数据集的香农熵
        double baseEntropy = calcShannonEnt(dataSet);
        double bestInfoGain = 0.0;
        int bestFeature = -1;
        double newEntropy = 0.0;
        Set<Double> tempFeatureSet = new HashSet<Double>();
        for(int j=0;j<featureNums;j++){
            //取数据集中的第i列Set
            for(int i=0;i<dataSet.getRowDimension();i++){
                tempFeatureSet.add(dataSet.get(i,j));
            }
            Matrix subMatrix;
            double prob=0;
            double infoGain=0;
            newEntropy = 0.0;
            for(double val:tempFeatureSet){
                subMatrix = splitDataSet(dataSet,j,val);
                prob = subMatrix.getRowDimension()/(float)dataSet.getRowDimension();
                newEntropy += prob*calcShannonEnt(subMatrix);
            }
            infoGain = baseEntropy-newEntropy;
            if(infoGain>bestInfoGain){
                bestInfoGain = infoGain;
                bestFeature = j;
            }
        }
        return bestFeature;
    }

    /**
     * 返回出现次数最多的类
     * @param labels 每个样本所属的类矩阵
     * @return 出现次数最多的类
     */
    public double majorityCnt(Matrix labels){
        Map<Double,Integer> classCount = new HashMap<Double, Integer>();
        for(int i=0;i<labels.getRowDimension();i++){
            if(!classCount.containsKey(labels.get(i,0)))
                classCount.put(labels.get(i,0),0);
            classCount.put(labels.get(i,0),classCount.get(labels.get(i,0))+1);
        }
        int count =0;
        double label = -1;
        for(double key:classCount.keySet()){
            if(classCount.get(key)>count){
                count = classCount.get(key);
                label = key;
            }
        }
        return label;
    }

    /**
     * 创建决策树
     * @param dataSet 数据集（最后一列为类）
     * @param featurelabels 各列特征名
     * @param labelStr 类名
     * @return 决策树
     */
    public Node createTree(Matrix dataSet,String [] featurelabels,String [] labelStr) {
        double[] classList = new double[dataSet.getRowDimension()];
        for (int i = 0; i < dataSet.getRowDimension(); i++) {
            classList[i] = dataSet.get(i, dataSet.getColumnDimension() - 1);
        }

        int num = 0;
        for (double cla : classList) {
            if (cla == classList[0]) num++;
        }
        if (num == classList.length) {
            Node node = new Node();
            node.element=labelStr[(int)classList[0]];
            return node;
        }  //若为同一类，则直接返回该类

        if(dataSet.getColumnDimension()==1) {
            Node node = new Node();
            node.element=majorityCnt(new Matrix(classList,1).transpose());
            return node;
        }

        double bestFeature = chooseBestFeatureToSplit(dataSet);
        String bestFeatureLabel = featurelabels[(int)bestFeature];

        Node root = new Node();
        root.element = bestFeatureLabel;

        String [] subLabels = del(featurelabels,bestFeatureLabel);
        Set<Double> uniqFeatureVals = new HashSet<Double>();
        for(int i=0;i<dataSet.getRowDimension();i++){
            uniqFeatureVals.add(dataSet.get(i,(int)bestFeature));
        }

        Map<Object,Node> child = new HashMap<Object, Node>();
        for(double val:uniqFeatureVals){
            child.put(val,createTree(splitDataSet(dataSet,(int)bestFeature,val),subLabels,labelStr));
        }
        root.child=child;
        return root;
    }

    /**
     * 从labels数组中删除元素val
     * @param labels
     * @param val
     * @return 新的数组
     */
    private String[] del(String [] labels,String val){
        String [] newLabels = new String[labels.length-1];
        int k=0;
        for(int i=0;i<labels.length && k<labels.length-1;i++){
            if(!labels[i].equals(val))
                newLabels[k++]=labels[i];
        }
        return newLabels;
    }

    /**
     * 决策树分类调用接口
     * @param tree 调用createTree得到的决策树根节点
     * @param featureLabels 特征集名称
     * @param sample 待分类样本
     * @return
     */
    public String classify(Node tree,String [] featureLabels,Matrix sample){
        while ((null != tree) && (null != tree.child)){
            try {
                System.out.println(tree.element);
                tree = tree.child.get(sample.get(0,getIndex(featureLabels,(String) tree.element)));
            }catch (Exception e){
                e.printStackTrace();
                return "Class Not Find";
            }
        }
        if(null == tree) return "Class Not Find";
        return (String) tree.element;
    }

    /**
     * 从String数组中获取对应值的下标
     * @param labels
     * @param val
     * @return
     */
    private int getIndex(String [] labels,String val){
        for(int i=0;i<labels.length;i++){
            if(val.equals(labels[i]))
                return i;
        }
        return -1;
    }

    public static void main(String [] args) throws Exception{
        DectreeHandler handler = new DectreeHandler();
        double [][] data = {
                {1,1,1,1,1,1,1},
                {2,2,2,2,2,2,2},
                {3,3,3,3,3,3,3},
                {1,1,4,2,3,3,1},
                {4,1,5,4,2,1,2},
                {1,2,6,2,1,2,6},
                {4,2,7,4,3,5,4},
                {1,2,8,3,3,3,4},
                {2,12,9,5,2,4,5},
                {1,2,3,10,8,6,5}
        };
        Matrix dataSet = new Matrix(data);
        double [] labels = {1,1,1,2,2,3,3,3,3,0};
        dataSet = dataSet.expand(new Matrix(labels,1).transpose(),true);
        int bestFeature = handler.chooseBestFeatureToSplit(dataSet);
        System.out.println(bestFeature);
        dataSet.print(dataSet.getColumnDimension(),3);
        String [] featurelabels = {"特征A","特征B","‘特征C","特征D","特征E","特征F","特征G"};
        String [] labelStr = {"类A","类B","类C","类D"};
        Node root = handler.createTree(dataSet,featurelabels,labelStr);

        //序列化存储
/*        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("E:\\dectree.txt")));
        oos.writeObject(root);
        oos.flush();
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("E:\\dectree.txt")));
        Node tree = (Node) ois.readObject();*/

        double [] sample = new double[]{1,1,3,10,3,3,4};
        String className = handler.classify(root,featurelabels,new Matrix(sample,1));
        System.out.println(className);
    }
}
