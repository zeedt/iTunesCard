package com.zeed;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import static javafx.application.ConditionalFeature.GRAPHICS;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class ItunescardApplicationTests {

	@Test
	public void contextLoads() throws IOException, WriterException {
		String qrCodeText = "000201010211021343143812705100415539923700023993520465365303NGN5403.0058032345917oklumide Adenrele6003oyo630451F7";
		String filePath = "/Users/syusuf/Desktop/JD.png";
		int size = 197;
		String fileType = "png";
		File qrFile = new File(filePath);
		createQRImage(qrFile, qrCodeText, size, fileType);
		System.out.println("DONE");
	}

	private static void createQRImage(File qrFile, String qrCodeText, int size, String fileType)
			throws WriterException, IOException {
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

		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, 427, 470);


		graphics.setColor(Color.DARK_GRAY);
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 17);
		graphics.setFont(font);
		FontMetrics metrics = graphics.getFontMetrics(font);
		// Determine the X coordinate for the text
		String text = "Scan Here To Pay";
		int x = (427 - metrics.stringWidth(text)) / 2;
		graphics.drawString(text, x,40);

		BufferedImage arrowImage = ImageIO.read(new File("/Users/syusuf/Downloads/red-arrow.png"));
		x = (427 - arrowImage.getWidth(null)) / 2;
		graphics.drawImage(arrowImage, x, 50, 30, 30, null);

		graphics.setColor(Color.BLUE);
		font = new Font(Font.SERIF, Font.BOLD, 20);
		graphics.setFont(font);
		metrics = graphics.getFontMetrics(font);
		// Determine the X coordinate for the text
		text = "CASH MONEY";
		x = (427 - metrics.stringWidth(text)) / 2;
		graphics.drawString(text, x,352);

		graphics.setColor(Color.darkGray);
		graphics.fillRect(0, 385,427,5);


		graphics.setColor(Color.BLUE);
		font = new Font(Font.SERIF, Font.BOLD, 20);
		graphics.setFont(font);
		metrics = graphics.getFontMetrics(font);
		// Determine the X coordinate for the text
		text = "mVisa ID";
		x = (212 - metrics.stringWidth(text)) / 2;
		graphics.drawString(text, x,420);
		text = "431438383231";
		x = (212 - metrics.stringWidth(text)) / 2;
		graphics.setColor(Color.BLACK);
		graphics.drawString(text, x,450);

		graphics.setColor(Color.BLUE);
		font = new Font(Font.SERIF, Font.BOLD, 20);
		graphics.setFont(font);
		metrics = graphics.getFontMetrics(font);
		// Determine the X coordinate for the text
		text = "MasterPass ID";
		x = (212 - metrics.stringWidth(text)) / 2;
		graphics.drawString(text, 212+x,420);
		text = "431438383231";
		x = (212 - metrics.stringWidth(text)) / 2;
		graphics.setColor(Color.BLACK);
		graphics.drawString(text, 212+x,450);



		graphics.setColor(Color.darkGray);
		graphics.fillRect(212,390,3,80);

		//QR image rendering
		graphics.setColor(Color.WHITE);
		graphics.fillRect(115, 98, matrixWidth, matrixWidth);

		// Paint and save the image using the ByteMatrix
		graphics.setColor(Color.BLACK);

		for (int i = 115; i < matrixWidth+115; i++) {
			for (int j = 98; j < matrixWidth+98; j++) {
				if (byteMatrix.get(i-115, j-98)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}

		ImageIO.write(image, fileType, qrFile);
	}

}
