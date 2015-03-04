package com.smashthestack;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Image;

public class LeapListener extends com.leapmotion.leap.Listener{
	Image leftImage;
	Image rightImage;
	boolean ready;
	
	public LeapListener(){
		this.leftImage = new Image();
		this.rightImage = new Image();
	}

	@Override
	public void onConnect(Controller controller) {
		// TODO Auto-generated method stub
		//super.onConnect(controller);
		System.out.println("Leap Connected");
		controller.setPolicy(Controller.PolicyFlag.POLICY_IMAGES);
	}

	@Override
	public void onFrame(Controller controller) {
		// TODO Auto-generated method stub
		super.onFrame(controller);
		if(controller.frame().isValid()){
			this.ready = true;
			this.leftImage = controller.frame().images().get(0);
			this.rightImage = controller.frame().images().get(1);
		} else {
			this.ready = false;
		}
		System.out.println("Ready:" + this.ready);
	}
	
	public Image getLeftImage(){
		return this.leftImage;
	}
	
	public Image getRightImage(){
		return this.rightImage;
	}
	
	public boolean isReady(){
		return this.ready;
	}
	
	
	

}
