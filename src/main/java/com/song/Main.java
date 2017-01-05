package com.song;

import com.song.common.FileHandler;
import com.song.common.ImgHandler;
import com.song.learn.KnnHandler;
import com.song.utils.jama.Matrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Song on 2016/9/30.
 */
public class Main {
    public static void main(String [] args) throws IOException{
        Matrix dataSet = new Matrix(0,100);
        Matrix labels = new Matrix(0,1);
        Matrix temp = new Matrix(1,1);
        for(int i = 0;i<20;i++){
            try {
                BufferedImage img = ImageIO.read(new File("E:\\imgdata\\cat\\" + i + ".jpg"));
                Matrix pixels = new Matrix(ImgHandler.getMatrix(ImgHandler.zoom(img, 100, 100)));
                dataSet = dataSet.expand(pixels, false);
                temp.set(0,0,1);
                labels = labels.expand(temp,false);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        for(int i = 0;i<20;i++){
            try {
                BufferedImage img = ImageIO.read(new File("E:\\imgdata\\dog\\"+i+".jpg"));
                Matrix pixels = new Matrix(ImgHandler.getMatrix(ImgHandler.zoom(img,100,100)));
                dataSet=dataSet.expand(pixels,false);
                temp.set(0,0,2);
                labels = labels.expand(temp,false);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        for(int i = 0;i<20;i++){
            try {
                BufferedImage img = ImageIO.read(new File("E:\\imgdata\\butterfly\\"+i+".jpg"));
                Matrix pixels = new Matrix(ImgHandler.getMatrix(ImgHandler.zoom(img,100,100)));
                dataSet=dataSet.expand(pixels,false);
                temp.set(0,0,3);
                labels = labels.expand(temp,false);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        for(int i = 0;i<20;i++){
            try {
                BufferedImage img = ImageIO.read(new File("E:\\imgdata\\dog\\"+i+".jpg"));
                Matrix pixels = new Matrix(ImgHandler.getMatrix(ImgHandler.zoom(img,100,100)));
                dataSet=dataSet.expand(pixels,false);
                temp.set(0,0,4);
                labels = labels.expand(temp,false);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        BufferedImage img = ImageIO.read(new File("E:\\imgdata\\cat\\"+10+".jpg"));
        Matrix sample = new Matrix(ImgHandler.getMatrix(ImgHandler.zoom(img,100,100)));
        KnnHandler handler = new KnnHandler(dataSet);
        System.out.println(handler.classify(sample,dataSet,labels,0.2));
    }


}
