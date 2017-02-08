package com.project.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import com.project.common.exception.ServerException;
/**
 * 验证码工具
 * @author WUJK
 * @version 2012-03
 */
public class ValidateCodeUtil {
    // 输出图片类型
    public final static String IMAGE_TYPE = "png";
    // 干扰线条数
    private final static int DISTURB_COUNT = 10;
    // 字体大小
    private final static int FONT_SIZE = 15;
    // 验证码字符集
    private final static String DEFAULT_CHARACTERS = "ABCDEFGHIJKLMNOPQSRTUVXYZ0123456789";//W显示出来很不好看，这里去掉了
    // 默认宽度
	private final static int DEFAULT_WIDTH = 60;
	// 默认高度
	private final static int DEFAULT_HEIGHT = 26;
	//验证码个数
	private final static int DEFAULT_CODE_COUNT = 4;

	/** 生成验证码字符串 */
    public static String createRandomCode() {
        return createRandomCode(DEFAULT_CODE_COUNT);
    }
    
    /** 
     * 生成验证码字符串 
     * @param codeCount 验证码个数
     */
    public static String createRandomCode(int codeCount) {
        Random random = new Random();
        String vcode = "";
        String code = null;
        for (int i = 0; i < codeCount; i++) {
            code = String.valueOf(DEFAULT_CHARACTERS.charAt(random.nextInt(DEFAULT_CHARACTERS.length())));
            vcode += code;
        }
        return vcode;
    }
    
    /** 
     * 生成验证码图片
     * @param vcode 验证码字符串
     */
    public static BufferedImage createImage(String vcode) {
        return createImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, vcode);
    }
	
    /** 
     * 生成验证码图片
     * @param width  宽度
     * @param height 高度
     * @param vcode  验证码字符串
     */
	public static BufferedImage createImage(int width,int height,String vcode) {
		 // 在内存中创建图象
		 BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		 // 获取图形上下文
		 Graphics g = image.getGraphics();
		 //生成随机类
		 Random random = new Random();
		 // 设定背景色
		 g.setColor(createRandColor(200, 250));
		 g.fillRect(0, 0, width, height);
		 //设定字体
		 g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));
		 // 随机产生干扰线，使图象中的认证码不易被其它程序探测到
		 g.setColor(createRandColor(160, 200));//干扰线的颜色
		 for (int i = 0; i < DISTURB_COUNT; i++) {
    		 int x1 = random.nextInt(width);
    		 int y1 = random.nextInt(height);
    		 int x2 = random.nextInt(width);
    		 int y2 = random.nextInt(height);
    		 g.drawLine(x1, y1, x2, y2);
		 }
		 // 取随机产生的认证码
		 for (int i = 0; i < vcode.length(); i++) {
    		 // 将认证码显示到图象中
    		 g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
    		 //调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
    		 g.drawString(String.valueOf(vcode.charAt(i)), 15*i+3, height-7);//改变验证码显示的高度和宽度后显示位置可能会需要通过这里进行调节，以获得最佳显示效果
		 }
		 // 图象生效
		 g.dispose();

		 return image;
	}
	
	/** 将验证码图片写入输出流 */
    public static void output(String vcode, OutputStream os) {
        try{
            ImageIO.write(createImage(vcode), IMAGE_TYPE, os);
        } catch(IOException e) {
            throw new ServerException(e.getMessage());
        } finally {
            IoUtils.closeQuietly(os);
        }
    }

	/** 给定范围获得随机颜色 */
    private static Color createRandColor(int fc, int bc) { 
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}
