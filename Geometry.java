package comp557.a1;
import comp557.a1.geom.Cube;
import comp557.a1.geom.Sphere;
import comp557.a1.geom.Quad;
import comp557.a1.geom.SimpleAxis;
import com.jogamp.opengl.GLAutoDrawable;
import mintools.parameters.DoubleParameter;

public class Geometry extends GraphNode{
	/*leaves for scene graph
	These geometry nodes will not likely have any parameters to interactively adjust their appearance, 
	but feel free to expose whatever you like, e.g., material colour
*/
	double[] pos;
	double[] color;
	double [] scale;
	String shape;
	public Geometry (String name, double[] pos, double[] color, double[]scale, String shape) {
		super(name);
		this.pos = pos;
		this.color = color;
		this.scale = scale;
		this.shape = shape;
	}
	public void display (GLAutoDrawable drawable, BasicPipeline pipeline) {
		pipeline.push();
		if (shape.equals("box")) {
			Cube.draw(drawable, pipeline);
		}else if (shape.equals("Sphere")) {
			Sphere.draw(drawable,pipeline);
		}
		
	}
	/* You will likely also want to specify 
	material parameters for the lighting computations of the geometry. See the kdID member variable 
	in the BasicPipeline and note that you can use this (and other GLSL shader inputs) with glUniform 
	calls to set the material parameters.*/
}
