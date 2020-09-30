package comp557.a1;

import javax.vecmath.Matrix4d;


import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;

public class FreeJoint extends GraphNode {

	DoubleParameter tx;
	DoubleParameter ty;
	DoubleParameter tz;
	DoubleParameter rx;
	DoubleParameter ry;
	DoubleParameter rz;
		
	public FreeJoint( String name ) {
		super(name);
		dofs.add( tx = new DoubleParameter( name+" tx", 0, -2, 2 ) );		
		dofs.add( ty = new DoubleParameter( name+" ty", 0, -2, 2 ) );
		dofs.add( tz = new DoubleParameter( name+" tz", 0, -2, 2 ) );
		dofs.add( rx = new DoubleParameter( name+" rx", 0, -180, 180 ) );		
		dofs.add( ry = new DoubleParameter( name+" ry", 0, -180, 180 ) );
		dofs.add( rz = new DoubleParameter( name+" rz", 0, -180, 180 ) );
	}
	
	@Override
	public void display( GLAutoDrawable drawable, BasicPipeline pipeline ) {
		pipeline.translate(tx.getFloatValue(),ty.getFloatValue(),tz.getFloatValue());
		pipeline.push();//do i need to call push here??
		//get the radian by rx,ry,rz?
		pipeline.rotate(rx.getFloatValue(),ry.getFloatValue(),rz.getFloatValue());
		pipeline.push();
		// TODO: Objective 3: Freejoint, transformations must be applied before drawing children
		
		super.display( drawable, pipeline);//draw children with correct transformation	
		pipeline.pop();
	}	
}

