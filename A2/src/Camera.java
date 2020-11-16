package comp557.a2;
//Agnes Liu 260713093
import javax.swing.JPanel;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import mintools.parameters.DoubleParameter;
import mintools.parameters.Parameter;
import mintools.parameters.ParameterListener;
import mintools.parameters.Vec3Parameter;
import mintools.swing.VerticalFlowPanel;

/**
 * Camera class to be used both for viewing the scene, but also to draw the scene from 
 * a point light.
 */
public class Camera {

	Vec3Parameter position = new Vec3Parameter("position", 0, 0, 10 );
	Vec3Parameter lookat = new Vec3Parameter("look at", 0, 0, 0 );
	Vec3Parameter up = new Vec3Parameter("up", 0, 1, 0 );
	
    DoubleParameter near = new DoubleParameter( "near plane", 1, 0.1, 10 );    
    DoubleParameter far = new DoubleParameter( "far plane" , 40, 1, 100 );    
    DoubleParameter fovy = new DoubleParameter( "fovy degrees" , 27, 14, 67 );    	
	double aspR; 
    /** Viewing matrix to be used by the pipeline */
    Matrix4d V = new Matrix4d();
    /** Projection matrix to be used by the pipeline */
    Matrix4d P = new Matrix4d();
    
    public Camera() {
    	near.addParameterListener( new ParameterListener<Double>() {			
			@Override
			public void parameterChanged(Parameter<Double> parameter) {
				// Let's keep near and far from crossing!
				if ( near.getValue() >= far.getValue() ) {
					far.setValue( near.getValue() + 0.1 );
				}
			}
		});
    	far.addParameterListener( new ParameterListener<Double>() {
    		@Override
    		public void parameterChanged(Parameter<Double> parameter) {
				// Let's keep near and far from crossing!
    			if ( far.getValue() <= near.getValue() ) {
					near.setValue( far.getValue() - 0.1 );
				}
    		}
		});
    }
    
    /**
     * Update the projection and viewing matrices
     * We'll do this every time we draw, though we could choose to more efficiently do this only when parameters change.
     * @param width of display window (for aspect ratio)
     * @param height of display window (for aspect ratio)
     */
    public void updateMatrix( double width, double height ) {
    	
    	// TODO: Objective 2: Replace the default viewing matrix with one constructed from the parameters available in this class!
    	Vector3d w = new Vector3d(position.x-lookat.x,position.y-lookat.y,position.z-lookat.z);
    	w.normalize();
    	Vector3d u = new Vector3d(up.x,up.y,up.z);
    	u.cross(u,w);
    	u.normalize();
    	Vector3d v = new Vector3d();
    	v.cross(w,u);
    	Matrix4d tmp = new Matrix4d();
    	tmp.setColumn(0,u.x,u.y,u.z,0);
    	tmp.setColumn(1,v.x,v.y,v.z,0);
    	tmp.setColumn(2,w.x,w.y,w.z,0);
    	tmp.setColumn(3,position.x,position.y,position.z,1);
    	tmp.invert();
    	V.set(tmp);
//    	// TODO: Objective 3: Replace the default projection matrix with one constructed from the parameters available in this class!    
//    	//use fov, aspect ratio, near & far plane
    	aspR = width/height;
    	double n = near.getValue();
    	double f = far.getValue();
    	double t = Math.tan(Math.toRadians(fovy.getFloatValue())/2)*n;
    	double b = -t;
    	double r = t*aspR;
    	double l = -t*aspR;
    	P.setRow(0,2*n/(r-l),0,(r+l)/(r-l),0);
    	P.setRow(1,0,2*n/(t-b),(t+b)/(t-b),0);
    	P.setRow(2,0,0,(n+f)/(n-f),2*n*f/(n-f));
    	P.setRow(3,0,0,-1,0);
    }
    
    /**
     * @return controls for the camera
     */
    public JPanel getControls() {
        VerticalFlowPanel vfp = new VerticalFlowPanel();
        vfp.add( position );
        vfp.add( lookat );
        vfp.add( up );
        vfp.add( near.getControls() );
        vfp.add( far.getControls() );
        vfp.add( fovy.getControls() );
        return vfp.getPanel();
    }
	
}
