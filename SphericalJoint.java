package comp557.a1;

import com.jogamp.opengl.GLAutoDrawable;
import javax.vecmath.Tuple3d;
import mintools.parameters.DoubleParameter;

public class SphericalJoint extends GraphNode {
	DoubleParameter rx;
	DoubleParameter ry;
	DoubleParameter rz;
	Tuple3d position;
	public SphericalJoint(String name) {
		super(name);
	}
	public void setPosition(Tuple3d t) {
		this.position = t;
	}
	public void setX(double min, double max) {
		dofs.add( rx = new DoubleParameter(" rx", 0, min, max ));//( name+" rx", 0, min, max ) );
	}
	public void setY(double min, double max) {
		dofs.add( ry = new DoubleParameter(" ry", 0, min, max ));//( name+" ry", 0, min, max ) );
	}
	public void setZ(double min, double max) {
		dofs.add( rz = new DoubleParameter(" rz", 0, min, max ));//( name+" rz", 0, min, max ) );
	}
	@Override
	public void display( GLAutoDrawable drawable, BasicPipeline pipeline ) {
		pipeline.translate(position.x, position.y, position.z);
		pipeline.rotate(rx.getValue(),1,0,0);
		pipeline.rotate(ry.getValue(),0,1,0);
		pipeline.rotate(rz.getValue(),0,0,1);
		
		super.display( drawable, pipeline);//draw children with correct transformation	
	}
}
