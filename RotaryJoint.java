package comp557.a1;

import javax.vecmath.Matrix4d;
import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;	
		
public class RotaryJoint extends GraphNode{
	//takes translation in the parent frame
	double[] tl;
	//axis of rotation
	double[] axis;
	//angle param
	DoubleParameter rad;
	public RotaryJoint (String name, double[] tl, double[] axis, double amin, double amax) {
		super(name);
		this.tl = tl;
		this.axis = axis;
		//min and max for angle parameter
		dofs.add(rad = new DoubleParameter(name + "rad", 0, amin, amax));
	}
	@Override
	public void display( GLAutoDrawable drawable, BasicPipeline pipeline ) {
		pipeline.push();
		pipeline.translate(tl[0],tl[1],tl[2]);
		pipeline.rotate(rad.getValue(),axis[0],axis[1],axis[2]);
		
		super.display( drawable, pipeline);//draw children with correct transformation	
		pipeline.pop();
	}
	
}
