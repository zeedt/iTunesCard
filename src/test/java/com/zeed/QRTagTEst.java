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
public class QRTagTEst {

    int paddinLeft = 18;
    int size = 105;

    @Test
    public void contextLoads() throws IOException, WriterException, DocumentException {
        String qrCodeText = "0002010102110213431438491783604155399237001308315204653653035665802NG5925EZIDONYE UGOCHUKWU JOSHUA6011Lagos State63042C83";
        String filePath = "/Users/syusuf/Desktop/JD5.png";
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
        int totalWidth = 275;
        BufferedImage image = new BufferedImage(totalWidth, 420, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        int qrCodeContainerWidth = 230;

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        fillRectangle(graphics, 0, 0, totalWidth, 420, Color.DARK_GRAY);
        fillRectangle(graphics, paddinLeft, 18, qrCodeContainerWidth, 41, Color.WHITE);

        drawHeader(graphics, totalWidth);

        fillRectangle(graphics, paddinLeft, 71, qrCodeContainerWidth, 207, Color.WHITE);

        drawCenterDetails(graphics, totalWidth, qrCodeContainerWidth, byteMatrix);

        Font font = new Font(Font.SERIF, Font.PLAIN, 10);
        FontMetrics metrics = graphics.getFontMetrics(font);
        String text = "Services provided by";
        int xOffset = (totalWidth - metrics.stringWidth(text)) / 2;
        writeText(graphics,xOffset,395, font, Color.white, text);
        BufferedImage logoImage = ImageIO.read(new File("/Users/syusuf/Downloads/interswitch.png"));
        int logoWidth = 94;
        int logoHeight = 22;
        int imageXOffset = (totalWidth - logoWidth) / 2;
//
        appendLogo(graphics, logoImage, imageXOffset, 400, logoWidth, logoHeight);
        ImageIO.write(image, fileType, qrFile);
        createPdfFromImage(image);

    }

    private void drawCenterDetails(Graphics2D graphics, int totalWidth, int qrCodeContainerWidth, BitMatrix byteMatrix) throws IOException {
        Font font = new Font(Font.SERIF, Font.PLAIN, 15);
        FontMetrics metrics = graphics.getFontMetrics(font);
        String text = "Scan Here To Pay";
        int xOffset = (totalWidth - metrics.stringWidth(text)) / 2;
        writeText(graphics,xOffset,86, font, Color.BLUE, text);

        BufferedImage arrowImage = ImageIO.read(new File("/Users/syusuf/Downloads/red-arrow.png"));
        int arrowImageXOffset = (totalWidth - 10) / 2;
        drawImage(graphics, arrowImage, arrowImageXOffset, 94, 10, 10);
        drawQRImageCode(graphics, byteMatrix, size, 79, 124);
        drawMerchantNameAndQRDetails(graphics, "EZEDUONOYE UGOCHUKWU", totalWidth);

        BufferedImage logoImage = ImageIO.read(new File("/Users/syusuf/Downloads/fbn-logo.png"));
        int logoWidth = 75;
        int logoHeight = 19;
        fillRectangle(graphics, paddinLeft, 332, qrCodeContainerWidth, 41, Color.white);
        int imageXOffset = (totalWidth - logoWidth) / 2;
        appendLogo(graphics, logoImage, imageXOffset, 340, logoWidth, logoHeight);
    }

    private void drawHeader(Graphics2D graphics, int totalWidth) throws IOException {

        BufferedImage verveLogo = ImageIO.read(new File("/Users/syusuf/Downloads/verve.png"));
        drawImage(graphics, verveLogo, 34, 26, 45,18 );

        BufferedImage visaLogo = ImageIO.read(new File("/Users/syusuf/Downloads/visa.png"));
        drawImage(graphics, visaLogo, 117, 26, 45,18 );

        BufferedImage masterpassLogo = ImageIO.read(new File("/Users/syusuf/Downloads/masterpass.png"));
        drawImage(graphics, masterpassLogo, 196, 26, 45,18 );
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
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 15);
        graphics.setFont(font);
        FontMetrics metrics = graphics.getFontMetrics(font);
        int merchantNameXOffset = (totalWidth - metrics.stringWidth(merchantName)) / 2;
        writeText(graphics, merchantNameXOffset, 257, font, Color.BLUE, merchantName);

        fillRectangle(graphics,paddinLeft, 283,114,41,Color.WHITE);

        font = new Font(Font.SERIF, Font.BOLD, 10);
        metrics = graphics.getFontMetrics(font);
        String text = "mVisa ID";
        int x = paddinLeft + ((114 - metrics.stringWidth(text)) / 2);
        writeText(graphics, x, 300, font, Color.BLUE, text);

        text = "431438383231";
        x = paddinLeft + ((114 - metrics.stringWidth(text)) / 2);
        writeText(graphics, x, 315, font, Color.BLACK, text);

        fillRectangle(graphics,133, 283,114,41,Color.WHITE);

        font = new Font(Font.SERIF, Font.BOLD, 10);
        metrics = graphics.getFontMetrics(font);
        text = "MasterPass ID";
        x = paddinLeft + ((114 - metrics.stringWidth(text)) / 2);
        writeText(graphics, 114+x, 300, font, Color.BLUE, text);

        text = "431438383231";
        x = paddinLeft + ((114 - metrics.stringWidth(text)) / 2);
        writeText(graphics, 114+x, 315, font, Color.BLACK, text);

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
