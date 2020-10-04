package comp557.a1;
import comp557.a1.geom.Cube;


import comp557.a1.geom.Sphere;
import comp557.a1.geom.Quad;
import comp557.a1.geom.SimpleAxis;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import mintools.parameters.DoubleParameter;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3d;

public class Geometry extends GraphNode{
	/*leaves for scene graph
	These geometry nodes will not likely have any parameters to interactively adjust their appearance, 
	but feel free to excentere whatever you like, e.g., material colour
*/
	Tuple3d center;
	Tuple3d color;
	Tuple3d scale;
	String shape;
	public Geometry (String name, String sh) {
		super(name);
		this.shape = sh;
	}
	public void setCentre(Tuple3d p) {
		this.center = p;
	}
	public void setScale(Tuple3d s) {
		this.scale = s;
	}public void setColor(Tuple3d c){
		this.color = c;
	}
	public void display (GLAutoDrawable drawable, BasicPipeline pipeline) {
		GL4 gl = drawable.getGL().getGL4();
		gl.glUniform3f(pipeline.kdID,(float)(color.x),(float)(color.y),(float)(color.z));
		pipeline.translate(center.x,center.y,center.z);
		pipeline.scale(scale.x,scale.y,scale.z);
		if (shape.equals("box")) {
			Cube.draw(drawable, pipeline);
		}else if (shape.equals("sphere")) {
			Sphere.draw(drawable,pipeline);
		}
		super.display( drawable, pipeline);//draw children with correct transformation	
	}

}
