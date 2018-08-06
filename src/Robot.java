//__________________________________________________________________________
//_______________________ © Nader Trabelsi - May 2017 ______________________
//__________________________________________________________________________


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.GL2;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import static com.jogamp.opengl.GL.*;  // GL constants
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import com.jogamp.opengl.util.gl2.GLUT;




public class Robot extends GLCanvas implements GLEventListener {
    // Define constants for the top-level container
    private static String TITLE = "Robot";  // window's title
    private static final int CANVAS_WIDTH = 640;  // width of the drawable
    private static final int CANVAS_HEIGHT = 480; // height of the drawable
    private static final int FPS = 60; // animator's target frames per second
    
    
    /** The entry main() method to setup the top-level container and animator
     * @param args */
    public static void main(String[] args) {
        // Run the GUI codes in the event-dispatching thread for thread safety
        SwingUtilities.invokeLater(() -> {
            // Create the OpenGL rendering canvas
            GLCanvas canvas = new Robot();
            canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
            
            // Create a animator that drives canvas' display() at the specified FPS.
            final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
            
            // Create the top-level container
            final JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame
            frame.getContentPane().add(canvas);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    // Use a dedicate thread to run the stop() to ensure that the
                    // animator stops before program exits.
                    new Thread() {
                        @Override
                        public void run() {
                            if (animator.isStarted()) animator.stop();
                            System.exit(0);
                        }
                    }.start();
                }
            });
            frame.setTitle(TITLE);
            frame.pack();
            frame.setVisible(true);
            frame.setResizable(true);
            //  animator.start(); // start the animation loop
        });
    }
    
    // Setup OpenGL Graphics Renderer
    
    private GLU glu;  // for the GL Utility
    private GLUT glut; // for the GLUT Utility
    
    /** Constructor to setup the GUI for this Component */
    public Robot() {
        this.addGLEventListener(this);
        
    }
    
    // ------ Implement methods declared in GLEventListener ------
    
    /**
     * Called back immediately after the OpenGL context is initialized. Can be used
     * to perform one-time initialization. Run only once.
     * @param drawable
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        
        GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
        glu = new GLU();                         // get GL Utilities
        //gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearColor(0.3f, 0.3f, 0.3f, 0.0f);
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glEnable(GL_LINE_SMOOTH);
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
        
        // ----- Your OpenGL initialization code here -----
    }
    
    /**
     * Call-back handler for window re-size event. Also called when the drawable is
     * first set to visible.
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        glut = new GLUT();
        
        if (height == 0) height = 1;   // prevent divide by zero
        float aspect = (float)width / height;
        
        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);
        
        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar
        
        glu.gluLookAt(5,5 , 15, 0, 0, 0, 0,1, 0);
        
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }
    
    /**
     * Called back by the animator to perform rendering.
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        
        
        
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
        
        gl.glPushMatrix(); // All
        
        gl.glTranslatef(0,-1,0);
        gl.glScalef(1.5f,1,0.5f);
        gl.glColor3f(0,0,1);Cube(drawable);
        gl.glPushMatrix(); // Lower torso
        
        gl.glTranslatef(-0.5f,-2.0f,0);
        gl.glScalef(0.3f,1,1);
        gl.glColor3f(0,0.75f,1);Cube(drawable); // Left thigh
        
        gl.glTranslatef(0,-2.0f,0);
        gl.glColor3f(0,0.3f,1);Cube(drawable); // Lower leg
        
        gl.glTranslatef(0,-1.7f,0);
        gl.glScalef(1.2f,0.3f,1.2f);
        gl.glColor3f(0,0.75f,1);Cube(drawable); // Foot
        
        // Now back to the Lower torso node
        gl.glPopMatrix();
        
        gl.glTranslatef(0.5f,-2.0f,0);
        gl.glScalef(0.3f,1,1);
        gl.glColor3f(1,0.75f,0);Cube(drawable); // Right thigh
        
        gl.glTranslatef(0,-2.0f,0);
        gl.glColor3f(1,0.3f,0);Cube(drawable); // Lower leg
        
        gl.glTranslatef(0,-1.7f,0);
        gl.glScalef(1.2f,0.3f,1.2f);
        gl.glColor3f(1,0.75f,0);Cube(drawable); // Foot
        
        
        // Then we return to the tree root
        gl.glPopMatrix();
        
        gl.glTranslatef(0,1,0);
        gl.glScalef(1.5f,1.5f,0.5f);
        gl.glColor3f(1,0,0);Cube(drawable);
        gl.glPushMatrix(); // Upper torso
        
        gl.glTranslatef(0,2.f,0);
        gl.glScalef(1,1,3);
        gl.glColor3f(0,1,0);
        glut.glutSolidSphere(0.99 , 60, 60); // Head
        
        // Going up to the the Upper torso ...
        gl.glPopMatrix();gl.glPushMatrix();
        
        gl.glTranslatef(-2,0.5f,0);
        gl.glRotatef(45.0f,0,0,1);
        gl.glScalef(1,0.25f,0.8f);
        gl.glTranslatef(0,-2.3f,0);
        gl.glColor3f(1,1,0);Cube(drawable); // Left arm
        
        gl.glTranslatef(-1.4f,0,0);
        gl.glScalef(0.6f,0.8f,0.8f);
        gl.glRotatef(20.f, 0,0,1);
        gl.glTranslatef(0,-0.5f,0);
        gl.glColor3f(1,0.5f,0);Cube(drawable); // Lower arm
        
        gl.glTranslatef(-1.4f,0,0);
        gl.glScalef(1.11f,3.33f,3.13f);
        gl.glColor3f(0.9f,0.9f,0.9f);
        glut.glutSolidSphere(0.7 , 60, 60);// Hand
        
        // To the Upper torso again ... but for a last time
        gl.glPopMatrix();
        
        gl.glTranslatef(2,0.5f,0);
        gl.glRotatef(-45.0f,0,0,1);
        gl.glScalef(1,0.25f,0.8f);
        gl.glTranslatef(0,-2.3f,0);
        gl.glColor3f(1,1,0);Cube(drawable); // Right arm
        
        gl.glTranslatef(1.4f,0,0);
        gl.glScalef(0.6f,0.8f,0.8f);
        gl.glRotatef(-20.f, 0,0,1);
        gl.glTranslatef(0,-0.5f,0);
        gl.glColor3f(1,0.5f,0);Cube(drawable); // Lower arm
        
        gl.glTranslatef(1.4f,0,0);
        gl.glScalef(1.11f,3.33f,3.13f);
        gl.glColor3f(0.9f,0.9f,0.9f);
        glut.glutSolidSphere(0.7 , 60, 60);// Hand
        
        

    }
    
    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) { }
    
    public void Cube(GLAutoDrawable drawbale){
        GL2 gl=drawbale.getGL().getGL2();
        
        // Front
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex3f(-1,-1,1);
        gl.glVertex3f(1,-1,1);
        gl.glVertex3f(1,1,1);
        gl.glVertex3f(-1,1,1);
        gl.glEnd();
        
        // Back
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex3f(-1,-1,-1);
        gl.glVertex3f(1,-1,-1);
        gl.glVertex3f(1,1,-1);
        gl.glVertex3f(-1,1,-1);
        gl.glEnd();
        
        // Bottom
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex3f(-1,-1,-1);
        gl.glVertex3f(1,-1,-1);
        gl.glVertex3f(1,-1,1);
        gl.glVertex3f(-1,-1,1);
        gl.glEnd();
        
        // Up
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex3f(-1,1,-1);
        gl.glVertex3f(1,1,-1);
        gl.glVertex3f(1,1,1);
        gl.glVertex3f(-1,1,1);
        gl.glEnd();
        
        // Right
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex3f(1,-1,1);
        gl.glVertex3f(1,-1,-1);
        gl.glVertex3f(1,1,-1);
        gl.glVertex3f(1,1,1);
        gl.glEnd();
        
        // Left
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex3f(-1,-1,1);
        gl.glVertex3f(-1,-1,-1);
        gl.glVertex3f(-1,1,-1);
        gl.glVertex3f(-1,1,1);
        gl.glEnd();
    }
}