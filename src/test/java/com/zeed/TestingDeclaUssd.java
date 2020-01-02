package com.zeed;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class TestingDeclaUssd {


    public static void main (String[] args) throws IOException, DocumentException, FontFormatException {

//        String fName = "/Users/syusuf/Downloads/frutiger-lt-45-light.ttf";
        String fName = "/Users/syusuf/Downloads/Frutiger LT Std 45 Light Bold.otf";

        InputStream is = new FileInputStream(fName);

        Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.BOLD, 70f);

        BufferedImage bufferedImage = ImageIO.read(new File("/Users/syusuf/Desktop/decal-template.png"));
        String filePath = "/Users/syusuf/Desktop/mydecal.png";
        String fileType = "png";
        bufferedImage.createGraphics();
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();

//         font = font.deriveFont(Font.TRUETYPE_FONT, 70f);
//        Font font = new Font(Font.DIALOG, Font.BOLD, 70);

        FontMetrics metrics = graphics.getFontMetrics(font);
        String text = "*894*89400075*Amount#";
        int xOffset = (bufferedImage.getWidth() - metrics.stringWidth(text)) / 2;
        writeText(graphics,xOffset,900, font, Color.BLACK, text);

        text = "*402*89400075*Amount#";
        xOffset = (bufferedImage.getWidth() - metrics.stringWidth(text)) / 2;
        writeText(graphics,xOffset,1150, font, Color.BLACK, text);


        text = "UQ RESTAURANT";
        xOffset = (bufferedImage.getWidth() - metrics.stringWidth(text)) / 2;
        String nameFontFile = "/Users/syusuf/Downloads/GOTHIC.TTF";

        InputStream nameFontInputStream = new FileInputStream(nameFontFile);

        Font nameFont = Font.createFont(Font.TRUETYPE_FONT, nameFontInputStream).deriveFont(Font.PLAIN, 70f);
//        font = font.deriveFont(Font.PLAIN, 70f);
//        font = new Font(Font.DIALOG, Font.PLAIN, 70);
        writeText(graphics,xOffset,1465, nameFont, Color.BLACK, text);

        text = "89400075";
        xOffset = (bufferedImage.getWidth() - metrics.stringWidth(text)) / 2;
        font = font.deriveFont(Font.BOLD, 50f);
//        font = new Font(Font.SANS_SERIF, Font.BOLD, 50);
        writeText(graphics,xOffset + 100,1540, font, Color.BLACK, text);

        ImageIO.write(bufferedImage, fileType, new File(filePath));

        createPdfFromImage(bufferedImage, 0.8f);


    }

    private static void writeText(Graphics graphics, int xOffset, int yOffset, Font font, Color color, String text) {
        graphics.setColor(color);
        graphics.setFont(font);
        graphics.drawString(text, xOffset, yOffset);
    }


    private static void createPdfFromImage(BufferedImage image, float ratio) throws IOException, DocumentException {
        Document document = new Document();
        String dest = "/Users/syusuf/Desktop/ussd-pdf-decal.pdf";
        PdfWriter.getInstance(document, new FileOutputStream(new File(dest)));
        document.open();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);

        com.itextpdf.text.Image image1 = com.itextpdf.text.Image.getInstance(outputStream.toByteArray());
        image1.setXYRatio(ratio);
        image1.scaleToFit(PageSize.A4.getWidth()*ratio, PageSize.A4.getHeight()*ratio);
        float x = (PageSize.A4.getWidth() - image1.getScaledWidth()) / 2;
        float y = (PageSize.A4.getHeight() - image1.getScaledHeight()) / 2;
        image1.setAbsolutePosition(x, y);
        document.add(image1);
        document.close();
//        servletResponse.flushBuffer();
    }
}
