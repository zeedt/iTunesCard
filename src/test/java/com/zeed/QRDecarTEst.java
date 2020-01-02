package com.zeed;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

@RunWith(SpringRunner.class)
public class QRDecarTEst {

    int paddinLeft = 68;
    int size = 197;

    @Test
    public void contextLoads() throws IOException, WriterException, DocumentException {
        String qrCodeText = "0002010102110213431438491783604155399237001308315204653653035665802NG5925EZIDONYE UGOCHUKWU JOSHUA6011Lagos State63042C83";
        String filePath = "/Users/syusuf/Desktop/JD4.png";
        String fileType = "png";
        File qrFile = new File(filePath);
        createQRImage(qrFile, qrCodeText, size, fileType);
    }

    private void createQRImage(File qrFile, String qrCodeText, int size, String fileType)
            throws WriterException, IOException, DocumentException {
        // Create the ByteMatrix for the QR-Code that encodes the given String
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
        // Make the BufferedImage that are to hold the QRCode
        int totalWidth = 560;
        BufferedImage image = new BufferedImage(totalWidth, 790, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        int qrCodeContainerWidth = 431;

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        fillRectangle(graphics, 0, 0, totalWidth, 790, Color.DARK_GRAY);
        fillRectangle(graphics, paddinLeft, 34, qrCodeContainerWidth, 76, Color.WHITE);

        drawHeader(graphics, totalWidth);

        fillRectangle(graphics, paddinLeft, 120, qrCodeContainerWidth, 397, Color.WHITE);

        drawCenterDetails(graphics, totalWidth, qrCodeContainerWidth, byteMatrix);

        Font font = new Font(Font.SERIF, Font.PLAIN, 15);
        FontMetrics metrics = graphics.getFontMetrics(font);
        String text = "Services provided by";
        int xOffset = (totalWidth - metrics.stringWidth(text)) / 2;
        writeText(graphics,xOffset,737, font, Color.white, text);
        BufferedImage logoImage = ImageIO.read(new File("/Users/syusuf/Downloads/interswitch.png"));
        int logoWidth = 188;
        int logoHeight = 45;
        int imageXOffset = (totalWidth - logoWidth) / 2;

        appendLogo(graphics, logoImage, imageXOffset, 750, logoWidth, logoHeight);
        ImageIO.write(image, fileType, qrFile);
        createPdfFromImage(image);

    }

    private void drawCenterDetails(Graphics2D graphics, int totalWidth, int qrCodeContainerWidth, BitMatrix byteMatrix) throws IOException {
        Font font = new Font(Font.SERIF, Font.PLAIN, 20);
        FontMetrics metrics = graphics.getFontMetrics(font);
        String text = "Scan Here To Pay";
        int xOffset = (totalWidth - metrics.stringWidth(text)) / 2;
        writeText(graphics,xOffset,158, font, Color.BLUE, text);

        BufferedImage arrowImage = ImageIO.read(new File("/Users/syusuf/Downloads/red-arrow.png"));
        int arrowImageXOffset = (totalWidth - 10) / 2;
        drawImage(graphics, arrowImage, arrowImageXOffset, 177, 30, 30);
        drawQRImageCode(graphics, byteMatrix, size, 185, 234);
        drawMerchantNameAndQRDetails(graphics, "EZEDUONOYE UGOCHUKWU", totalWidth);

        BufferedImage logoImage = ImageIO.read(new File("/Users/syusuf/Downloads/fbn-logo.png"));
        int logoWidth = 151;
        int logoHeight = 37;
        fillRectangle(graphics, paddinLeft, 623, qrCodeContainerWidth, 76, Color.white);
        int imageXOffset = (totalWidth - logoWidth) / 2;
        appendLogo(graphics, logoImage, imageXOffset, 635, logoWidth, logoHeight);
    }

    private void drawHeader(Graphics2D graphics, int totalWidth) throws IOException {

        BufferedImage verveLogo = ImageIO.read(new File("/Users/syusuf/Downloads/verve.png"));
        drawImage(graphics, verveLogo, 99, 53, 68,38 );

        BufferedImage visaLogo = ImageIO.read(new File("/Users/syusuf/Downloads/visa.png"));
        drawImage(graphics, visaLogo, 251, 53, 68,38 );

        BufferedImage masterpassLogo = ImageIO.read(new File("/Users/syusuf/Downloads/masterpass.png"));
        drawImage(graphics, masterpassLogo, 403, 53, 68,38 );
    }

    private void appendLogo(Graphics graphics, BufferedImage image, int imageXOffset, int imageYOffset, int width, int height) {

        drawImage(graphics, image, imageXOffset,imageYOffset, width, height );
    }

    private void createPdfFromImage(BufferedImage image) throws IOException, DocumentException {
        String dest = "/Users/syusuf/Downloads/addingImage.pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(new File(dest)));
        document.open();
        com.itextpdf.text.Image image1 = com.itextpdf.text.Image.getInstance("/Users/syusuf/Desktop/JD2.png");
        float x = (PageSize.A4.getWidth() - image1.getScaledWidth()) / 2;
        float y = (PageSize.A4.getHeight() - image1.getScaledHeight()) / 2;
        image1.setAbsolutePosition(x, y);
        document.add(image1);
        document.close();
    }


    private void drawMerchantNameAndQRDetails(Graphics2D graphics, String merchantName, int totalWidth) {
        merchantName = (StringUtils.isEmpty(merchantName)) ? "" : merchantName.substring(0,20);
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 20);
        graphics.setFont(font);
        FontMetrics metrics = graphics.getFontMetrics(font);
        int merchantNameXOffset = (totalWidth - metrics.stringWidth(merchantName)) / 2;
        writeText(graphics, merchantNameXOffset, 487, font, Color.BLUE, merchantName);

        fillRectangle(graphics,paddinLeft, 525,212,68,Color.WHITE);

        font = new Font(Font.SERIF, Font.BOLD, 20);
        metrics = graphics.getFontMetrics(font);
        String text = "mVisa ID";
        int x = paddinLeft + ((212 - metrics.stringWidth(text)) / 2);
        writeText(graphics, x, 550, font, Color.BLUE, text);

        text = "431438383231";
        x = paddinLeft + ((212 - metrics.stringWidth(text)) / 2);
        writeText(graphics, x, 575, font, Color.BLACK, text);

        fillRectangle(graphics,287, 524,212,68,Color.WHITE);

        font = new Font(Font.SERIF, Font.BOLD, 20);
        metrics = graphics.getFontMetrics(font);
        text = "MasterPass ID";
        x = paddinLeft + ((212 - metrics.stringWidth(text)) / 2);
        writeText(graphics, 212+x, 550, font, Color.BLUE, text);

        text = "431438383231";
        x = paddinLeft + ((212 - metrics.stringWidth(text)) / 2);
        writeText(graphics, 212+x, 575, font, Color.BLACK, text);

    }

    private void drawQRImageCode(Graphics2D graphics, BitMatrix bitMatrix, int qrSize, int xOffset, int yOffset) {

        graphics.setColor(Color.BLACK);

        for (int i = xOffset; i < qrSize+xOffset; i++) {
            for (int j = yOffset; j < qrSize+yOffset; j++) {
                if (bitMatrix.get(i-xOffset, j-yOffset)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
    }

    private void fillRectangle(Graphics graphics, int xOffset, int yOffset, int width, int height, Color color) {
        graphics.setColor(color);
        graphics.fillRect(xOffset, yOffset, width, height);
    }

    private void writeText(Graphics graphics, int xOffset, int yOffset, Font font, Color color, String text) {
        graphics.setColor(color);
        graphics.setFont(font);
        graphics.drawString(text, xOffset, yOffset);
    }

    private void drawImage(Graphics graphics, BufferedImage image, int xOffset, int yOffset, int width, int height) {
        graphics.drawImage(image, xOffset, yOffset, width, height, null);
    }

}
