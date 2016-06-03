package teleop.tetrix;
import lejos.nxt.*;
import lejos.nxt.addon.tetrix.*;
import lejos.util.Delay;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class TeleopTetrix
{
	public static void main(String[] args)
	{
		int ds = 100;
		int pw = 50;

		// Create a new TetrixControllerFactory, which allows you to get
		// the actual controllers themselves.  Note that the controllers must
		// be connected to a sensor port (NOT a motor port!) on the NXT.
		TetrixControllerFactory cf = new TetrixControllerFactory(SensorPort.S1);
		
		// Now that you have the factory, get the controllers.  Since they
		// are connected in series, you MUST get the controllers in the correct
		// order, starting with the unit that is directly connected to the NXT,
		// and then moving out from there.  
		Delay.msDelay(ds);
		TetrixMotorController mc1 = cf.newMotorController();
		System.out.println("Motor Controler 1 Connected\n");
		Delay.msDelay(ds);
		TetrixMotorController mc2 = cf.newMotorController();
		System.out.println("Motor Controler 2 Connected\n");
		
		// We don't use the servo controller on this bot, but if you did you
		// would include a line like the one below.
		//TetrixServoController sc1 = cf.newServoController();

		// Now get the actual motors, based on the port from the controller (1 or 2)
		TetrixMotor frontLeft = mc1.getBasicMotor(TetrixMotorController.MOTOR_1);
		TetrixMotor backLeft = mc1.getBasicMotor(TetrixMotorController.MOTOR_2);
		TetrixMotor frontRight = mc2.getBasicMotor(TetrixMotorController.MOTOR_1);
		TetrixMotor backRight = mc2.getBasicMotor(TetrixMotorController.MOTOR_2);

		// Reverse right side drive motors
		Delay.msDelay(ds);
		frontLeft.setReverse(true);
		System.out.println("r\n");
		Delay.msDelay(ds);
		backLeft.setReverse(true);
		System.out.println("r\n");

		frontLeft.setPower(pw);
		backLeft.setPower(pw);
		frontRight.setPower(pw);
		backRight.setPower(pw);

		// Set up display
		if(glfwInit() != 1)
		{
			System.err.println("GLFW failed to initialize");
			System.exit(1);
		};
		
		long win = glfwCreateWindow(640, 480, "Window", 0, 0);
		
//		glfwShowWindow(win);		
//		glfwMakeContextCurrent(win);
//		GL.createCapabilities();
		
		int fl = 0;
		int bl = 0;
		int fr = 0;
		int br = 0;
		
		float x = 0;
		float y = 0;
		
		float xsize = 0;
		float ysize = 0;
		
		boolean[] buttons = new boolean[16];
		float[] axes = new float[6];
		
		int present = glfwJoystickPresent(GLFW_JOYSTICK_1); // Logitech F310 must be in D position not X		
		while(glfwWindowShouldClose(win) != 1) 
		{
			Delay.msDelay(50);

			glfwPollEvents();			

			// Read joystick 1 axes
			FloatBuffer joyAxes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
			int axesID = 0;
			while (joyAxes.hasRemaining())
			{
				axes[axesID] = joyAxes.get();
				if(axes[axesID] < 0.2 && axes[axesID] >-0.2)
				{
					axes[axesID] = 0;
				}
				axesID++;
			}
			
//				System.out.println("Left Stick X Axes:  " + axes[0]);
//				System.out.println("Left Stick Y Axes:  " + axes[1]);
//				System.out.println("Right Stick X Axes: " + axes[2]);
//				System.out.println("Right Stick Y Axes: " + axes[3]);
//				System.out.println("Left Trigger / L2:  " + axes[4]);
//				System.out.println("Right Trigger / R2: " + axes[5]);				
			
			
			// Read joystick 1 buttons
			ByteBuffer joyButtons = glfwGetJoystickButtons(GLFW_JOYSTICK_1);			
			int buttonID = 0;
			while (joyButtons.hasRemaining()) 
			{
			    int state = joyButtons.get();
			    buttons[buttonID] = (state == GLFW_PRESS);
			    buttonID++;
			}

//			x += 0.01f * axes[2];
//			y -= 0.01f * axes[3];
			
			// power is a percentage (0-100)
			if(buttons[8]) 
			{ 
				pw = pw<=0?0:pw-1; 
				frontLeft.setPower(pw);
				backLeft.setPower(pw);
				frontRight.setPower(pw);
				backRight.setPower(pw);	
			}
			else if(buttons[9]) 
			{ 
				pw = pw>=100?100:pw+1; 
				frontLeft.setPower(pw);
				backLeft.setPower(pw);
				frontRight.setPower(pw);
				backRight.setPower(pw);	
			}


			if(buttons[0]) { 
				System.out.println("left: " + pw);
				if(fl != -1) {
					frontLeft.backward();
					fl = -1;
				}
				if(bl != 1) {
					backLeft.forward();
					bl = 1;
				}
				if(fr != 1) {
					frontRight.forward();
					fr = 1;
				}
				if(br != -1) {
					backRight.backward();
					br = -1;
				}
			} else if(buttons[1]) { 
				System.out.println("back: "+ pw);
				if(fl != -1) {
					frontLeft.backward();
					fl = -1;
				}
				if(bl != -1) {
					backLeft.backward();
					bl = -1;
				}
				if(fr != -1) {
					frontRight.backward();
					fr = -1;
				}
				if(br != -1) {
					backRight.backward();
					br = -1;
				}
			} else if(buttons[2]) { 
				System.out.println("right: " + pw);
				if(fl != 1) {
					frontLeft.forward();
					fl = 1;
				}
				if(bl != -1) {
					backLeft.backward();
					bl = -1;
				}
				if(fr != -1) {
					frontRight.backward();
					fr = -1;
				}
				if(br != 1) {
					backRight.forward();
					br = 1;
				}
			} else if(buttons[3]) { 
				System.out.println("forward: " + pw);
				if(fl != 1) {
					frontLeft.forward();
					fl = 1;
				}
				if(bl != 1) {
					backLeft.forward();
					bl = 1;
				}
				if(fr != 1) {
					frontRight.forward();
					fr = 1;
				}
				if(br != 1) {
					backRight.forward();
					br = 1;
				}
			} else if(buttons[4]) { 
				System.out.println("spin left: " + pw);
				if(fl != -1) {
					frontLeft.backward();
					fl = -1;
				}
				if(bl != -1) {
					backLeft.backward();
					bl = -1;
				}
				if(fr != 1) {
					frontRight.forward();
					fr = 1;
				}
				if(br != 1) {
					backRight.forward();
					br = 1;
				}
			} else if(buttons[5]) { 
				System.out.println("spin right: " + pw);
				if(fl != 1) {
					frontLeft.forward();
					fl = 1;
				}
				if(bl != 1) {
					backLeft.forward();
					bl = 1;
				}
				if(fr != -1) {
					frontRight.backward();
					fr = 1;
				}
				if(br != -1) {
					backRight.backward();
					br = 1;
				}
			} else {
				System.out.println("float: " + pw);
				if(fl != 0) {
					frontLeft.flt();
					fl = 0;
				}
				if(bl != 0) {
					backLeft.flt();
					bl = 0;
				}
				if(fr != 0) {
					frontRight.flt();
					fr = 0;
				}
				if(br != 0) {
					backRight.flt();
					br = 0;
				}
			}
//			if(buttons[12]) { ysize += 0.001f; }
//			if(buttons[13]) { xsize += 0.001f; }
//			if(buttons[14]) { ysize -= 0.001f; }
//			if(buttons[15]) { xsize -= 0.001f; }
			
			/*
			if(glfwGetKey(win, GLFW_KEY_A) == GL_TRUE) { x -= 0.001f; }
			if(glfwGetKey(win, GLFW_KEY_D) == GL_TRUE) { x += 0.001f; }
			if(glfwGetKey(win, GLFW_KEY_W) == GL_TRUE) { y += 0.001f; }
			if(glfwGetKey(win, GLFW_KEY_S) == GL_TRUE) { y -= 0.001f; }

			if(glfwGetMouseButton(win, 0) == GL_TRUE) { xsize -= 0.002f; }
			if(glfwGetMouseButton(win, 1) == GL_TRUE) { xsize += 0.002f; }
			*/
			
//			glClear(GL_COLOR_BUFFER_BIT);  // set every pixel to black
//			
//			glBegin(GL_QUADS);
//			glColor4f(1, 0, 0, 0);
//			glVertex2f(-0.5f+x-xsize, 0.5f+y+ysize);
//			glColor4f(0, 1, 0, 0);			
//			glVertex2f(0.5f+x+xsize, 0.5f+y+ysize);
//			glColor4f(0, 0, 1, 0);
//			glVertex2f(0.5f+x+xsize, -0.5f+y-ysize);
//			glColor4f(1, 1, 1, 0);
//			glVertex2f(-0.5f+x-xsize, -0.5f+y-ysize);
//			glColor4f(1, 0, 0, 0);
//			glEnd();
//			
//			glfwSwapBuffers(win);
}
		
		glfwTerminate();
		
				
		Delay.msDelay(100);
		System.out.println("Done\n");

	}

}