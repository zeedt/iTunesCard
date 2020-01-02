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
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.scenario.effect.ImageData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

@RunWith(SpringRunner.class)
public class QRTest {


    @Test
    public void contextLoads() throws IOException, WriterException, DocumentException {
        String qrCodeText = "0002010102110213431438491783604155399237001308315204653653035665802NG5925EZIDONYE UGOCHUKWU JOSHUA6011Lagos State63042C83";
        String filePath = "/Users/syusuf/Desktop/JD2.png";
        int size = 197;
        String fileType = "png";
        File qrFile = new File(filePath);
        createQRImage(qrFile, qrCodeText, size, fileType);
        System.out.println("DONE");
        saveResizeImage();
    }

    private void saveResizeImage() throws IOException {
        String filePath = "/Users/syusuf/Downloads/QR IMAGE.png";
        BufferedImage qrImage = ImageIO.read(new File(filePath));
        BufferedImage image = new BufferedImage(211, 326, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.drawImage(qrImage, 0,0, 211, 326,null);
        ImageIO.write(image, "png", new File("/Users/syusuf/Downloads/RESIZED QR IMAGE.png"));
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
        BufferedImage image = new BufferedImage(427, 470, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        fillRectangle(graphics, 0, 0, 427, 470, Color.WHITE);

        drawImageBeforeQrCode(graphics);
        drawQRImageCode(graphics, byteMatrix, size);
        drawMerchantNameAndQRDetails(graphics);

        fillRectangle(graphics,212,390,3,80, Color.darkGray);

        ImageIO.write(image, fileType, qrFile);

        createPdfFromImage(image);

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


    private void drawMerchantNameAndQRDetails(Graphics2D graphics) {
        Font font = new Font(Font.SERIF, Font.BOLD, 20);
        graphics.setFont(font);
        FontMetrics metrics = graphics.getFontMetrics(font);
        String text = "EZIDONYE UGOCHUKWU JOSHUA";
        int merchantNameXOffset = (427 - metrics.stringWidth(text)) / 2;
        writeText(graphics, merchantNameXOffset, 352, font, Color.BLUE, text);

        fillRectangle(graphics, 0, 385, 427,5, Color.darkGray);

        font = new Font(Font.SERIF, Font.BOLD, 20);
        metrics = graphics.getFontMetrics(font);
        text = "mVisa ID";
        int x = (212 - metrics.stringWidth(text)) / 2;
        writeText(graphics, x, 420, font, Color.BLUE, text);

        text = "431438383231";
        x = (212 - metrics.stringWidth(text)) / 2;
        writeText(graphics, x, 450, font, Color.BLACK, text);

        font = new Font(Font.SERIF, Font.BOLD, 20);
        metrics = graphics.getFontMetrics(font);
        text = "MasterPass ID";
        x = (212 - metrics.stringWidth(text)) / 2;
        writeText(graphics, 212+x, 420, font, Color.BLUE, text);

        text = "431438383231";
        x = (212 - metrics.stringWidth(text)) / 2;
        writeText(graphics, 212+x, 450, font, Color.BLACK, text);


    }

    private void drawQRImageCode(Graphics2D graphics, BitMatrix bitMatrix, int qrSize) {
        //QR image rendering
        fillRectangle(graphics, 115, 98, qrSize, qrSize, Color.WHITE);

        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);

        for (int i = 115; i < qrSize+115; i++) {
            for (int j = 98; j < qrSize+98; j++) {
                if (bitMatrix.get(i-115, j-98)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
    }

    private void drawImageBeforeQrCode(Graphics graphics) throws IOException {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 17);
        graphics.setFont(font);
        FontMetrics metrics = graphics.getFontMetrics(font);
        String scanToPayText = "Scan Here To Pay";
        int scanToPayXOffset = (427 - metrics.stringWidth(scanToPayText)) / 2;
        writeText(graphics, scanToPayXOffset, 40, font, Color.DARK_GRAY, scanToPayText );

        BufferedImage arrowImage = ImageIO.read(new File("/Users/syusuf/Downloads/red-arrow.png"));
        int arrowImageXOffset = (427 - arrowImage.getWidth(null)) / 2;
        drawImage(graphics, arrowImage, arrowImageXOffset, 50, 30, 30);

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
