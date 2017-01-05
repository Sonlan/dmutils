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

    /**
     * 获得灰度图的灰度值矩阵（最低八位，rgb值都相等）
     * @param bimg
     * @return
     * @throws IOException
     */
    public static double[][] getMatrix(BufferedImage bimg) throws IOException{
        double [][] data = new double[bimg.getHeight()][bimg.getWidth()];
        for(int i=0;i<bimg.getHeight();i++){
            for(int j=0;j<bimg.getWidth();j++){
                data[i][j]=bimg.getRGB(j,i)&0xFF;
//                System.out.printf("%x\t",data[i][j]);
            }
 //           System.out.println();
        }
        return data;
    }

    /**
     * 获得灰度图片
     * @param bimg
     * @return
     * @throws IOException
     */
    public static BufferedImage getGreyImg(BufferedImage bimg) throws IOException{
        BufferedImage bimg_grey = new BufferedImage(bimg.getWidth(),bimg.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
        for(int i=0;i<bimg.getWidth();i++){
            for(int j=0;j<bimg.getHeight();j++){
                bimg_grey.setRGB(i,j,bimg.getRGB(i,j));
            }
        }
        return bimg_grey;
    }

    /**
     * 获得二值化图片
     * @param bimg
     * @return
     * @throws IOException
     */
    public static BufferedImage getBinaryImg(BufferedImage bimg) throws IOException{
        BufferedImage bimg_bin= new BufferedImage(bimg.getWidth(),bimg.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
        for(int i=0;i<bimg.getWidth();i++){
            for(int j=0;j<bimg.getHeight();j++){
                bimg_bin.setRGB(i,j,bimg.getRGB(i,j));
            }
        }
        return bimg_bin;
    }

    /**
     * 手动生成灰度图片
     * @param pixels
     * @throws IOException
     */
    public static BufferedImage genarateGreyImg(double[][] pixels) throws IOException{
        BufferedImage bimg = new BufferedImage(pixels[0].length,pixels.length,BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = bimg.getRaster();
        for(int j=0;j<pixels.length;j++){
            for(int i=0;i<pixels[0].length;i++){
                raster.setPixel(i,j,new double[]{pixels[j][i]});
            }
        }
        return bimg;
       // ImageIO.write(bimg,"jpg",new File(outputPath));
    }

    /**
     * 图片尺寸裁剪
     * @param sourceImage
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage zoom(BufferedImage sourceImage , int width , int height){
        BufferedImage zoomImage = new BufferedImage(width, height, sourceImage.getType());
        Image image = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        Graphics gc = zoomImage.getGraphics();
        gc.setColor(Color.WHITE);
        gc.drawImage(image , 0, 0, null);
        return zoomImage;
    }

    public static void main(String [] args) throws IOException{
        BufferedImage sourceImg =  ImageIO.read(new File("E:\\a.jpg"));
        BufferedImage zoomImg = zoom(sourceImg,100,100);
        getMatrix(getGreyImg(zoomImg));
    }
}
