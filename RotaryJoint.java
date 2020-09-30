package comp557.a1;

import javax.vecmath.Matrix4d;


import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;	
		
public class RotaryJoint extends GraphNode{
	//takes translation in the parent frame
	DoubleParameter tx;
	DoubleParameter ty;
	DoubleParameter tz;
	//axis of rotation
	DoubleParameter raxis;
	
	public RotaryJoint (String name, double amin, double amax) {
		super(name);
		//min and max for angle parameter
		dofs.add(raxis = new DoubleParameter(name + "raxis", 0, amin, amax));
	}
	@Override
	public void display( GLAutoDrawable drawable, BasicPipeline pipeline ) {
		pipeline.translate(tx.getFloatValue(),ty.getFloatValue(),tz.getFloatValue());
		pipeline.push();//do i need to call push here??
		//get the radian by rx,ry,rz?
		pipeline.rotate(rx.getFloatValue(),ry.getFloatValue(),rz.getFloatValue());
		pipeline.push();
		
		super.display( drawable, pipeline);//draw children with correct transformation	
		pipeline.pop();
	}
	
}
