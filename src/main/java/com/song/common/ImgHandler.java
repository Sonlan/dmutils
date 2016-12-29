package com.song.common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

/**
 * Created by Song on 2016/12/29.
 * 用于读取Image文件
 */
public final class ImgHandler {


    public static int[][] getMatrix(String path) throws IOException{
        BufferedImage bimg = ImageIO.read(new File(path));
        int [][] data = new int[bimg.getWidth()][bimg.getHeight()];
        for(int i=0;i<bimg.getWidth();i++){
            for(int j=0;j<bimg.getHeight();j++){
                data[i][j]=bimg.getRGB(i,j);
                System.out.printf("%d\t",data[i][j]);
            }
            System.out.println();
        }
        return data;
    }

    public void getGreyImg(String inputPath,String outputPath) throws IOException{
        BufferedImage bimg = ImageIO.read(new File(inputPath));
        BufferedImage bimg_grey = new BufferedImage(bimg.getWidth(),bimg.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
        for(int i=0;i<bimg.getWidth();i++){
            for(int j=0;j<bimg.getHeight();j++){
                bimg_grey.setRGB(i,j,bimg.getRGB(i,j));
            }
        }
        ImageIO.write(bimg_grey,"jpg",new File(outputPath));
    }

    public void getBinaryImg(String inputPath,String outputPath) throws IOException{
        BufferedImage bimg = ImageIO.read(new File(inputPath));
        BufferedImage bimg_bin= new BufferedImage(bimg.getWidth(),bimg.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
        for(int i=0;i<bimg.getWidth();i++){
            for(int j=0;j<bimg.getHeight();j++){
                bimg_bin.setRGB(i,j,bimg.getRGB(i,j));
            }
        }
        ImageIO.write(bimg_bin,"jpg",new File(outputPath));
    }

    public static void test(String inputPath,String outputPath) throws IOException{
        BufferedImage bimg = ImageIO.read(new File(inputPath));
        BufferedImage bimg_grey = new BufferedImage(bimg.getWidth(),bimg.getHeight(),BufferedImage.TYPE_INT_ARGB);
        int rgb,r,g,b,grey;
        for(int i=0;i<bimg.getWidth();i++){
            for(int j=0;j<bimg.getHeight();j++){
                rgb = bimg.getRGB(i,j);
                r = (rgb>>16)&0xFF;
                g = (rgb>>8)&0xFF;
                b = rgb&0xFF;
                grey = (r+g+b)/3;
                bimg_grey.setRGB(i,j,rgb);
            }
        }
        ImageIO.write(bimg_grey,"jpg",new File(outputPath));
    }
    public static void main(String [] args) throws IOException{
        test("E:\\a.jpg","E:\\test.jpg");
    }
}
