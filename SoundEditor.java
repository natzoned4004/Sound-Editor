
/*
 * Noah Alonso-Torres
 * Sound Editor
 * SoundEditor Class
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SoundEditor implements ActionListener{
	
	private JButton playButton		= new JButton("Play");
	private JButton stopButton 		= new JButton("Stop");
		private JButton resetButton 	= new JButton("Reset");
		private JButton maximizeButton 	= new JButton("Maximize");
	private JButton addEchoButton	= new JButton("Add Echo");
	private JButton swapChButton	= new JButton("Swap Channels");
		private JButton reverseButton	= new JButton("Reverse");
		private JButton quietButton	= new JButton("Quiet");
	private JButton fasterButton	= new JButton("Faster");
	private JButton fasterNHButton	= new JButton("Faster Not Higher");
		private JButton louderButton	= new JButton("Louder");
		private JButton distortButton	= new JButton("Distort");
		
		private String soundFile = "vivaldi.wav";
		Audio audio = new Audio(soundFile);

		
	public SoundEditor() {
		JFrame frame = new JFrame("Audio Editor");
		frame.setLayout(new GridLayout(6,2)); //ROWS, COLUMNS
		frame.add(playButton);
		frame.add(stopButton);
			frame.add(resetButton);
			frame.add(maximizeButton);
		frame.add(addEchoButton);
		frame.add(swapChButton);
			frame.add(reverseButton);
			frame.add(quietButton);
		frame.add(fasterButton);
		frame.add(fasterNHButton);
			frame.add(louderButton);
			frame.add(distortButton);
		
		playButton		.addActionListener(this);
		stopButton		.addActionListener(this);
			resetButton		.addActionListener(this);
			maximizeButton	.addActionListener(this);
		addEchoButton	.addActionListener(this);
		swapChButton	.addActionListener(this);
			reverseButton	.addActionListener(this);
			quietButton		.addActionListener(this);
		fasterButton	.addActionListener(this);
		fasterNHButton	.addActionListener(this);
			louderButton	.addActionListener(this);
			distortButton	.addActionListener(this);
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		System.out.println("History: ");
	}

	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == playButton) audio.play();
		if (event.getSource() == stopButton) audio.stop();
		if (event.getSource() == resetButton) reset();
		if (event.getSource() == maximizeButton) maximize();
		if (event.getSource() == addEchoButton) addEcho();
		if (event.getSource() == swapChButton) swapChButton();
		if (event.getSource() == reverseButton) reverse();
		if (event.getSource() == quietButton) quiet();
		if (event.getSource() == fasterButton) faster();
		if (event.getSource() == fasterNHButton) fasterNH();
		if (event.getSource() == louderButton) louder();
		if (event.getSource() == distortButton) distort();
	}
	
	public void reset() {
		Audio refresh = new Audio(soundFile);
		audio = refresh;
		System.out.println(" Reset");
	}
	
	public void maximize() {
		float[] left = audio.getLeftChannel();
		float[] right = audio.getRightChannel();
		float maximumLeft = left[0];
		float maximumRight = right[0];
		
		//SWEEP FOR MAX VALUE
			for (int i = 0; i < left.length; i++) {
				if (Math.abs(left[i]) > maximumLeft) maximumLeft = left[i];
			}
			for (int i = 0; i < right.length; i++){
				if (Math.abs(right[i]) > maximumRight) maximumRight = right[i];
			}
		
		//DIVIDE EACH ARRAY BY MAXIMUML/R REPLACE ARRAY
			for (int i = 0; i < left.length; i++)
				left[i] = left[i]/maximumLeft;
			for (int i = 0; i < right.length; i++)
				right[i] = right[i]/maximumRight;
		
		//SET BOUNDARIES -1 , 1
			for (int i = 0; i < left.length; i++)
				if (left[i] > 1) left[i] = 1;
			for (int i = 0; i < right.length; i++)
				if (right[i] > 1) right[i] = 1;
			for (int i = 0; i < left.length; i++)
				if (left[i] < -1) left[i] = -1;
			for (int i = 0; i < right.length; i++)
				if (right[i] < -1) right[i] = -1;

			audio.setLeftChannel(left);
			audio.setRightChannel(right);
			System.out.println(" Maximize");
			System.out.println(left.length);
	}
	
	public void addEcho() {
		float[] left = audio.getLeftChannel();
		float[] right = audio.getRightChannel();
		
		for(int i = 44100/8; i < left.length; i++)
			left[i] = (float).25*left[i] + (float).75*left[i-44100/8];
		for(int i = 44100/8; i < right.length; i++)
			right[i] = (float).25*right[i] + (float).75*right[i-44100/8];
		
		audio.setLeftChannel(left);
		audio.setRightChannel(right);
		System.out.println(" Echo");
	}
	
	public void swapChButton(){
		float[] left = audio.getLeftChannel();
			float[] temp = audio.getLeftChannel();
		float[] right = audio.getRightChannel();
		
		for(int i=0; i < left.length; i++) left[i] = right[i];
		for(int i=0; i < right.length; i++) right[i] = temp[i];
		
		audio.setLeftChannel(left);
		audio.setRightChannel(right);
		System.out.println(" Swap");
	}

	public void reverse() {
		float[] left = audio.getLeftChannel();
		float[] right = audio.getRightChannel();
		float[] tempLeft	= audio.getLeftChannel();
		float[] tempRight	= audio.getRightChannel();
		int sizeLeft = left.length-1;
		int sizeRight = right.length-1;
		
		//USED FOR OFF BY ONE DEBUG
		//System.out.println(sizeLeft + "\n" + sizeRight);
		//System.out.println(tempLeft.length + "\n" + tempRight.length);
		
		//SWEEP IN REVERSE AND STORE INTO TEMP ARRAYS
			for (int i = 0; i <= sizeLeft; i++)
				tempLeft[i] = left[sizeLeft - i];
			for (int i = 1; i <= sizeRight; i++)
				tempRight[i] = right[sizeRight - i];
			
		//SET CHANNELS AS TEMP ARRAYS
			audio.setLeftChannel(tempLeft);
			audio.setRightChannel(tempRight);
			
			System.out.println(" Reverse");
	}
	
	public void quiet() {
		float[] left = audio.getLeftChannel();
		float[] right = audio.getRightChannel();
		
		for(int i=0; i < left.length; i++) left[i] = left[i]/2;
		for(int i=0; i < right.length; i++) right[i] = right[i]/2;
		
		audio.setLeftChannel(left);
		audio.setRightChannel(right);
		
		System.out.println(" Quiet");
	}
	
	public void faster() {
		float[] left = audio.getLeftChannel();
		float[] right = audio.getRightChannel();
		float[] tempLeft	= new float[left.length];
		float[] tempRight	= new float[right.length];
		int nLeft = 0;
		int nRight = 0;
		String speed = JOptionPane.showInputDialog("Enter how many times faster you want it.");
		int multiple = Integer.parseInt(speed);

		//SWEEP. IF POSITION IS DIVISIBLE BY 3, STORE INTO NEW ARRAY
			for(int i=0; i < left.length; i++) {
				if ( 0 == i % multiple) {
					tempLeft[nLeft] = left[i];
					nLeft++;
				}
			}
			for(int i=0; i < right.length; i++) {
				if ( 0 == i % multiple) {
					tempRight[nRight] = right[i];
					nRight++;
				}
			}
		audio.setLeftChannel(tempLeft);
		audio.setRightChannel(tempRight);
		
		System.out.println(" Faster");
	}
	
	public void fasterNH() {
		float[] left = audio.getLeftChannel();
		float[] right = audio.getRightChannel();
		float[] tempLeft = new float[(left.length/2)];
		float[] tempRight = new float[(right.length/2)];
		int nL = 0;
		int nR = 0;
		
		for(int i=0; i < tempLeft.length; i++) {
			tempLeft[i] = left[nL];
			if (nL != 0 && nL%1000 == 0) nL += 1000;
			nL++;
		}
		for(int i=0; i < tempRight.length; i++) {
			tempRight[i] = right[nR];
			if (nR != 0 && nR%1000 == 0) nR += 1000;
			nR++;
		}
		
		audio.setLeftChannel(tempLeft);
		audio.setRightChannel(tempRight);
		System.out.println(" Faster Not Higher");

	}
	
	public void louder() {
		float[] left = audio.getLeftChannel();
		float[] right = audio.getRightChannel();
		
		for(int i=0; i < left.length; i++) {
			if (left[i] >= 0) left[i] = (float)(Math.pow(left[i], .8));
			else left[i] = (float)(-1*(Math.pow(Math.abs(left[i]), .8)));
		}
		for(int i=0; i < right.length; i++) {
			if (right[i] >= 0) right[i] = (float)(Math.pow(right[i], .8));
			else right[i] = (float)(-1*(Math.pow(Math.abs(right[i]), .8)));
		}
		
		audio.setLeftChannel(left);
		audio.setRightChannel(right);
		
		System.out.println(" Louder");
	}
	
	public void distort() {
		float[] left = audio.getLeftChannel();
		float[] right = audio.getRightChannel();
		
		for(int i=0; i < left.length; i++) left[i] = Math.abs(left[i]);
		for(int i=0; i < left.length; i++) right[i] = Math.abs(right[i]);
		
		audio.setLeftChannel(left);
		audio.setRightChannel(right);
		System.out.println(" Distort");
	}
}
