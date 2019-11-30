import java.awt.AWTException;
import java.awt.Button;
import java.awt.Color;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.w3c.dom.events.MouseEvent;

import javafx.scene.input.MouseButton;

import javax.swing.*;
public class MainClass extends JFrame implements KeyListener {
	
	JLabel lblVideo = new JLabel();
	
	ImageIcon videoIcon = new ImageIcon();
	
	boolean exit = false;
	
	Robot robot = null;
	
	public MainClass() {
		setSize(600, 400);
		setVisible(true);
		setLayout(null);
		
		addKeyListener(this);
		
		lblVideo.setBounds(0, 20, 600, 360);
		lblVideo.setBorder(BorderFactory.createLineBorder(Color.BLUE,2));
		add(lblVideo);
		
		JButton exitButton = new JButton("EXIT");
		exitButton.setBounds(250, 380, 100, 20);
		exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				exit = true;
			}
		});
		
		add(exitButton);
		
	}
	
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		MainClass mainClass = new MainClass();
		
		mainClass.playVideo();
		
	}
	
	public void playVideo() {
		VideoCapture videoCapture = new VideoCapture(0);
		
		
		if(!videoCapture.isOpened()) {
			videoCapture.open(0);
		}
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(!exit) {
			Mat frame = new Mat();
			
			videoCapture.read(frame);
			
			Core.flip(frame, frame, Core.ROTATE_180);
			
		    ///Imgproc.blur(frame, frame, new Size(3, 3));

			
			
			
			
			Mat grayScale = new Mat();
			
			Imgproc.cvtColor(frame, grayScale, Imgproc.COLOR_RGB2GRAY);
			
			CascadeClassifier handClassifier = new CascadeClassifier("C:\\Users\\padch\\OneDrive\\Documents\\HaarCascadesOCV\\" + "palm_v4.xml");
			
			MatOfRect hands = new MatOfRect();
			
			handClassifier.detectMultiScale2(grayScale, hands, new MatOfInt());
			
			for(Rect rect: hands.toArray()) {
				Imgproc.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height) , new Scalar(255, 0, 0),2);
				
				int x = (int) (1366/640) * ((rect.x + rect.x + rect.width)/2);
				int y = (int) (768/360)*((rect.y + rect.y + rect.height)/2);
				
				robot.mouseMove(x, y);

				
				
			
			}
			
			
			
			Image frameImage = HighGui.toBufferedImage(frame);
			
			ImageIcon frameIcon = new ImageIcon(frameImage);
			
			lblVideo.setIcon(frameIcon);
			
		}
		
		videoCapture.release();
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		System.exit(1);		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
 