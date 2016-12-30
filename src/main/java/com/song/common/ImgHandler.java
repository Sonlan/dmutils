package com.song.common;

import com.song.utils.Calculator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Song on 2016/12/29.
 * 用于读取Image文件
 */
public final class ImgHandler {


    public static int[][] getMatrix(String path) throws IOException{
        BufferedImage bimg = ImageIO.read(new File(path));
        int [][] data = new int[bimg.getHeight()][bimg.getWidth()];
        for(int i=0;i<bimg.getHeight();i++){
            for(int j=0;j<bimg.getWidth();j++){
                data[i][j]=bimg.getRGB(j,i)&0xFF;
              //  System.out.printf("%d\t",data[i][j]);
            }
           // System.out.println();
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
        BufferedImage bimg_grey = new BufferedImage(bimg.getWidth(),bimg.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = bimg_grey.getRaster();
        int rgb,r,g,b,grey;
        Random random = new Random();
        for(int i=0;i<bimg.getWidth();i++){
            for(int j=0;j<bimg.getHeight();j++){
                rgb = bimg.getRGB(i,j);
                r = (rgb>>16)&0xFF;
                g = (rgb>>8)&0xFF;
                b = rgb&0xFF;
                //grey = Math.abs(random.nextInt(255));
                grey = (r+g+b)/3;
                if(grey>=255) grey=255;
                if(grey<=0) grey=0;
                raster.setPixel(i,j,new int[]{grey});
            }
        }
        ImageIO.write(bimg_grey,"jpg",new File(outputPath));
    }

    public static void genarateGreyImg(float[][] pixels,String outputPath) throws IOException{
        BufferedImage bimg = new BufferedImage(pixels[0].length,pixels.length,BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = bimg.getRaster();
        for(int j=0;j<pixels.length;j++){
            for(int i=0;i<pixels[0].length;i++){
                raster.setPixel(i,j,new float[]{pixels[j][i]});
            }
        }
        ImageIO.write(bimg,"jpg",new File(outputPath));
    }
    public static void main(String [] args) throws IOException{
//        test("E:\\a.jpg","E:\\c.jpg");
        float [][] src2 = {{-1,0,-1},{0,4,0},{-1,0,-1}};
        int [][] src1 = getMatrix("E:\\c.jpg");
        float [][] res = Calculator.conv(src1,src2,1);
        for(int i=0;i<res.length;i++){
            for(int j=0;j<res[0].length;j++){
                if(res[i][j]<=60) res[i][j]=0;
                else res[i][j] = 255;
            }
        }
        genarateGreyImg(res,"E:\\test.jpg");
    }
}
