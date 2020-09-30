package comp557.a1;

import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;

public class SphericalJoint extends GraphNode {
	DoubleParameter rx;
	DoubleParameter ry;
	DoubleParameter rz;
	double[] pos;
	public SphericalJoint(String name, double[] pos, double[] amin, double[] amax) {
		super(name);
		dofs.add( rx = new DoubleParameter( name+" rx", 0, amin[0], amax[0] ) );		
		dofs.add( ry = new DoubleParameter( name+" ry", 0, amin[1], amax[1] ) );
		dofs.add( rz = new DoubleParameter( name+" rz", 0, amin[2], amax[2] ) );
		this.pos = pos;
	}
	@Override
	public void display( GLAutoDrawable drawable, BasicPipeline pipeline ) {
		//calculate rotation coordinates and apply to pipeline
		pipeline.push();
		pipeline.rotate(rx.getValue(),pos[0],pos[1],pos[2]);
		pipeline.rotate(ry.getValue(),pos[0],pos[1],pos[2]);
		pipeline.rotate(rz.getValue(),pos[0],pos[1],pos[2]);
		
		super.display( drawable, pipeline);//draw children with correct transformation	
		pipeline.pop();
	}
}
