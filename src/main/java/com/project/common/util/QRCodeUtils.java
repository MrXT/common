package com.project.common.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.project.common.exception.BusinessException;

/**
 * ClassName: QRCodeUtils <br/>
 * Function: 二维码生成工具. <br/>
 * @author WJK
 * @version 2014-12
 */
public class QRCodeUtils {

    private static final int QRCODE_SIDE_LENGTH = CommonUtils.readResource("qrcode.side.length") == null ? 150 : Integer.parseInt(CommonUtils
        .readResource("qrcode.side.length"));// 默认二维码边距

    private static final int QRCODE_LOGO_LENGTH = (int) (QRCODE_SIDE_LENGTH * 0.25);// logo边距约为二维码边距的25%

    // private static final String QRCODE_LOGO_PATH =
    // System.getProperty("user.dir")+CommonUtils.readResource("qrcode.logo.path");//二维码logo路径
    private static final String DEFAULT_CHARACTER = "UTF-8";

    private static final String DEFAULT_FORMAT = "png";// png无损、jpg有损

    /**
     * 生成二维码图片对象
     * @param content 二维码扫描内容
     * @param deleteWhite 是否去白边
     */
    public static BufferedImage createMatrix(String content) {
        return createMatrix(content, QRCODE_SIDE_LENGTH, null, false);
    }

    /**
     * 生成二维码图片对象
     * @param content 二维码扫描内容
     */
    public static BufferedImage createMatrix(String content, boolean deleteWhite) {
        return createMatrix(content, QRCODE_SIDE_LENGTH, null, deleteWhite);
    }

    /**
     * 生成二维码图片对象
     * @param content 二维码扫描内容
     * @param sideLength 二维码边距
     */
    public static BufferedImage createMatrix(String content, int sideLength) {
        return createMatrix(content, sideLength, null, false);
    }

    /**
     * 生成二维码图片对象
     * @param content 二维码扫描内容
     * @param logoFile logo图片文件对象
     */
    public static BufferedImage createMatrix(String content, File logoFile) {
        return createMatrix(content, QRCODE_SIDE_LENGTH, null, false);
    }

    /**
     * 生成二维码图片对象
     * @param content 二维码扫描内容
     * @param sideLength 二维码边距
     * @param logoFile logo图片文件对象
     * @param deleteWhite 是否去白边
     */
    public static BufferedImage createMatrix(String content, int sideLength, String logoPath, boolean deleteWhite) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 设置二维码的默认编码
        hints.put(EncodeHintType.CHARACTER_SET, QRCodeUtils.DEFAULT_CHARACTER);
        // 设置二维码的纠错级别——这里选择最高H级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 二维码边框宽度
        hints.put(EncodeHintType.MARGIN, 0);

        BitMatrix matrix = null;
        try {
            matrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, sideLength, sideLength, hints);
            if (deleteWhite) {
                matrix = deleteWhite(matrix);
            }
        } catch (WriterException e) {
            throw new BusinessException(e);
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
        if (logoPath != null) {
            addLogo(image, null);
        }
        return image;
    }

    /**
     * 生成二维码图片
     * @param content 二维码扫描内容
     * @param filePath 文件路径
     */
    public static boolean writeToFile(String content, String filePath) {
        BufferedImage image = createMatrix(content);
        return writeToFile(image, filePath);
    }

    /**
     * 生成二维码图片
     * @param content 二维码扫描内容
     * @param filePath 文件路径
     */
    public static boolean writeToFile(BufferedImage image, String filePath) {
        try {
            File logoFile = new File(filePath);
            return ImageIO.write(image, DEFAULT_FORMAT, logoFile);
        } catch (IOException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 生成输出流
     * @param content 二维码扫描内容
     * @param stream 输出流
     */
    public static boolean writeToStream(String content, OutputStream stream) {
        BufferedImage image = createMatrix(content);
        return writeToStream(image, stream);
    }

    /**
     * 生成输出流
     * @param image 二维码图片对象
     * @param stream 输出流
     */
    public static boolean writeToStream(BufferedImage image, OutputStream stream) {
        try {
            return ImageIO.write(image, DEFAULT_FORMAT, stream);
        } catch (IOException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 去白边(PDF_418和QR_CODE有效)
     * @param matrix
     */
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

    /**
     * 给二维码图片添加Logo
     * @param image
     * @param filePath
     */
    private static void addLogo(BufferedImage image, String filePath) {
        try {
            File logoFile = new File(filePath);
            if (logoFile == null || !logoFile.isFile() || !logoFile.exists()) {
                throw new BusinessException("没有找到logo图片,路径:" + logoFile.getAbsolutePath());
            }
            // 读取二维码图片，并构建绘图对象
            Graphics2D g = image.createGraphics();
            // 读取Logo图片
            BufferedImage logoImage = ImageIO.read(logoFile);
            int widthLogo = QRCODE_LOGO_LENGTH;
            int heightLogo = QRCODE_LOGO_LENGTH;
            // 计算图片放置位置
            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - heightLogo) / 2;
            // 开始绘制图片
            g.drawImage(logoImage, x, y, widthLogo, heightLogo, null);
            g.drawRoundRect(x, y, widthLogo, heightLogo, 0, 0);
            g.drawRect(x, y, widthLogo, heightLogo);
            g.dispose();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public static void main(String[] args) {
        String content = "http://121.42.30.179/resources/appCheckOut.apk";
        // BufferedImage bufferedImage = QRCodeUtils.createMatrix(content);
        // System.out.println(QRCodeUtils.writeToFile(bufferedImage,
        // "C:/Users/Administrator/Desktop/二维码.jpg"));
        System.out.println(QRCodeUtils.writeToFile(content, "C:/Users/Administrator/Desktop/二维码.png"));
    }
}