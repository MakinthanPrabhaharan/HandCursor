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
	
	double screenWidth = 0;  //change to your screen width
	double screenHeight = 0;//change to your screen height
	
	//this label will hold the imageicon to display the video from teh camera
	JLabel lblVideo = new JLabel();
	
	//imageicon that will hold the frame captured by the camera
	ImageIcon videoIcon = new ImageIcon();
	
	//a boolean that says whether or not to stop the program
	boolean exit = false;
	
	//declaring a null Robot object which is gonna be used to control the mouse
	Robot robot = null;
	
	//constructor for the GUI
	public MainClass() {
		//setting the parameters and laying out the components
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
	
	//main method
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		MainClass mainClass = new MainClass();
		
		mainClass.playVideo();
		
	}
	
	//method that basically does everything
	public void playVideo() {
		
		//initializing a new videocapture object which represents the webcam
		VideoCapture videoCapture = new VideoCapture(0);
		
		//checking to see if it is opened already
		if(!videoCapture.isOpened()) {
			videoCapture.open(0);
		}
		
		//initializing the Robot object we declared earlier
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//while the user does not press the exit button
		while(!exit) {
			
			//using this Mat to store the frame captured by the webcam
			Mat frame = new Mat();
			
			//storing the frame captured by the webcam into the frame Mat
			videoCapture.read(frame);
			
			//horizontally flipping the frame
			Core.flip(frame, frame, Core.ROTATE_180);
			
		   	//initializng another Mat that will store the grayscale version of the captured frame
			Mat grayScale = new Mat();
			
			//converting the frame to gray and storing it in the grayScale Mat
			Imgproc.cvtColor(frame, grayScale, Imgproc.COLOR_RGB2GRAY);
			
			//cascade classifier to be used to detect the palm of your hand in the image
			CascadeClassifier handClassifier = new CascadeClassifier("C:\\Users\\padch\\OneDrive\\Documents\\HaarCascadesOCV\\" + "palm_v4.xml");
			
			/*hands MatOfRect will essentially be a matrix of type Rect where each Rect represents the bounding box of the hands in the image*/
			MatOfRect hands = new MatOfRect();
			
			//this will detect the hands in the grayscale image and store the boundig boxes of the hands in the MatOfRect
			handClassifier.detectMultiScale2(grayScale, hands, new MatOfInt());
			
			//looping through each rect in the MatOfRect 
			for(Rect rect: hands.toArray()) {
				
				//drawing an actual rectangle on the image of where the hands are
				Imgproc.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height) , new Scalar(255, 0, 0),2);
				
				//calculating the x and y coordinates of where the mouse should be
				int x = (int) (screenWidth/screenHeight) * ((rect.x + rect.x + rect.width)/2);
				int y = (int) (768/360)*((rect.y + rect.y + rect.height)/2);
				
				//telling the robot to move the mouse at that location
				robot.mouseMove(x, y);

				
				
			
			}
			
			
			//displaying the frame image on the label
			Image frameImage = HighGui.toBufferedImage(frame);
			ImageIcon frameIcon = new ImageIcon(frameImage);
			lblVideo.setIcon(frameIcon);
			
		}
		
		//releasing the videoCapture object when all is finished
		videoCapture.release();
		
		
	}

	
	//stops the program when any key is pressed
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
 
