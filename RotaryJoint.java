package comp557.a1;

import javax.vecmath.Matrix4d;
import javax.vecmath.Tuple3d;
import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;	
		
public class RotaryJoint extends GraphNode{
	//takes translation in the parent frame
	Tuple3d position;
	//axis of rotation
	Tuple3d axis;
	//angle param
	DoubleParameter rotate;
//	joint.setPosition( getTuple3dAttr(dataNode,"position") );
//	joint.setAxis( getTuple3dAttr(dataNode,"axis") );
	public RotaryJoint(String name) {
		super (name);
	}
	public void setPosition(Tuple3d t) {
		this.position = t;
	}
	public void setAxis(Tuple3d t) {
		this.axis = t;
	}
	public void setMinMax(Tuple3d t) {
		dofs.add(rotate = new DoubleParameter(name + "rotate", 0, 0, t.x));
	}
	@Override
	public void display( GLAutoDrawable drawable, BasicPipeline pipeline ) {
		pipeline.translate(position.x,position.y, position.z);
		pipeline.rotate(rotate.getValue(),axis.x,axis.y,axis.z);
		
		super.display( drawable, pipeline);//draw children with correct transformation	
	}
	
}
