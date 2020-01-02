package com.zeed;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import org.codehaus.groovy.util.StringUtil;
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
public class QRIDCardTEst {


    @Test
    public void contextLoads() throws IOException, WriterException, DocumentException {
        String qrCodeText = "0002010102110213431438491783604155399237001308315204653653035665802NG5925EZIDONYE UGOCHUKWU JOSHUA6011Lagos State63042C83";
        String filePath = "/Users/syusuf/Desktop/JD3.png";
        int size = 120;
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
        int matrixWidth = byteMatrix.getWidth();
        int totalWidth = 191;
        BufferedImage image = new BufferedImage(totalWidth, 275, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        int qrCodeContainerWidth = 126;

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        fillRectangle(graphics, 0, 0, totalWidth, 275, Color.DARK_GRAY);

        fillRectangle(graphics, 30, 16, qrCodeContainerWidth, 42, Color.WHITE);

        BufferedImage verveLogo = ImageIO.read(new File("/Users/syusuf/Downloads/verve.png"));
        drawImage(graphics, verveLogo, 35, 26, 32,25 );

        BufferedImage visaLogo = ImageIO.read(new File("/Users/syusuf/Downloads/visa.png"));
        drawImage(graphics, visaLogo, 77, 26, 32,25 );

        BufferedImage masterpassLogo = ImageIO.read(new File("/Users/syusuf/Downloads/masterpass.png"));
        drawImage(graphics, masterpassLogo, 119, 26, 32,25 );

        fillRectangle(graphics, 30, 66, qrCodeContainerWidth, 140, Color.WHITE);

        Font font = new Font(Font.SERIF, Font.PLAIN, 10);
        FontMetrics metrics = graphics.getFontMetrics(font);
        String text = "Scan Here To Pay";
        int xOffset = (totalWidth - metrics.stringWidth(text)) / 2;
        writeText(graphics,xOffset,81, font, Color.BLUE, text);

        BufferedImage arrowImage = ImageIO.read(new File("/Users/syusuf/Downloads/red-arrow.png"));
        int arrowImageXOffset = (totalWidth - 10) / 2;
        drawImage(graphics, arrowImage, arrowImageXOffset, 84, 8, 8);
        drawQRImageCode(graphics, byteMatrix, size, 33, 76);
        drawMerchantNameAndQRDetails(graphics, "EZEDUONOYE UGOCHUKWU", totalWidth);

        BufferedImage logoImage = ImageIO.read(new File("/Users/syusuf/Downloads/fbn-logo.png"));
        int logoWidth = 80;
        int logoHeight = 25;
        fillRectangle(graphics, 30, 211, qrCodeContainerWidth, logoHeight+3, Color.white);
        int imageXOffset = (totalWidth - 80) / 2;
        appendFBNLogo(graphics, logoImage, imageXOffset, 214, logoWidth, logoHeight);


        font = new Font(Font.SERIF, Font.PLAIN, 10);
        metrics = graphics.getFontMetrics(font);
        text = "Services provided by";
        xOffset = (totalWidth - metrics.stringWidth(text)) / 2;
        writeText(graphics,xOffset,250, font, Color.white, text);

        logoImage = ImageIO.read(new File("/Users/syusuf/Downloads/interswitch.png"));
        logoWidth = 100;
        logoHeight = 18;
        imageXOffset = (totalWidth - logoWidth) / 2;

        appendFBNLogo(graphics, logoImage, imageXOffset, 252, logoWidth, logoHeight);
        ImageIO.write(image, fileType, qrFile);
        createPdfFromImage(image);

    }

    private void appendFBNLogo(Graphics graphics, BufferedImage image, int imageXOffset, int imageYOffset, int width, int height) {

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
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 8);
        graphics.setFont(font);
        FontMetrics metrics = graphics.getFontMetrics(font);
        int merchantNameXOffset = (totalWidth - metrics.stringWidth(merchantName)) / 2;
        writeText(graphics, merchantNameXOffset, 192, font, Color.BLACK, merchantName);

        font = new Font(Font.MONOSPACED, Font.PLAIN, 8);
        metrics = graphics.getFontMetrics(font);
        String mVisaId = "431438383231";
        String mVisaText = "merchantId : " + mVisaId;
        int x = (totalWidth - metrics.stringWidth(mVisaText)) / 2;
        writeText(graphics, x, 200, font, Color.BLUE, mVisaText);

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
