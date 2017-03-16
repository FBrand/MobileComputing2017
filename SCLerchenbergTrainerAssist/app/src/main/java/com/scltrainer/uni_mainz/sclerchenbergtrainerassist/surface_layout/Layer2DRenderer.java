package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout;

import static android.opengl.GLES20.*;
import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.Util.ARROW_SIZE_INCH;
import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.Util.MAX_ZOOM;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.R;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.Layer;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.LayerGroup;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.Path;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.ProgramLinkException;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.Shader;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.ShaderCompileException;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.ShaderProgram;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.ShaderProgramInfo;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector4f;


import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;


public class Layer2DRenderer extends RendererBase {

    public static final String TEXTURE_NAME = "tex";
    public static final String TEXCOORD_NAME = "a_texcoord";
    public static final String POSITION_NAME = "a_position";
    public static final String MVP_MAT_NAME = "mvpMat";
    public static final String COLOR_NAME = "color";
    public static final String TRANSPARENCY_NAME = "transparency";
    public static final String POINT_SIZE_NAME = "pointSize";


    private ShaderProgram program;
    private ShaderProgramInfo programInfo;
    private LayerGroup layers = new LayerGroup();

    private Matrix4f projection = new Matrix4f();
    private Matrix4f view = new Matrix4f();
    private Quaternionf rot = new Quaternionf();

    private Vector2f centerBounds = new Vector2f();
    private Vector2f worldBounds = new Vector2f();


    private Context context;

    public Layer2DRenderer(Context context) {
        this.context = context;
    }

    private void updateView() {
        view.identity();
        //if (orientation == HORIZONTAL)
         //   view.rotate(-(float)Math.PI/2.0f, 0, 0, 1);
        view.scale(zoom, zoom, 1);
        view.rotate(rot);
        view.translate(-center.x, -center.y, 0);
    }

    private void updateArrowScale() {
        Path.arrowScale = Util.pixelDensity.x*ARROW_SIZE_INCH/(zoom*width);
    }

    private void setCenter(Vector2f c) {
        c.x = Math.max(c.x, -centerBounds.x);
        c.y = Math.max(c.y, -centerBounds.y);
        c.x = Math.min(c.x, centerBounds.x);
        c.y = Math.min(c.y, centerBounds.y);
        center = c;
        Log.e("Center", center.x + ", " + center.y);
    }

    private void updateCenterBounds() {
        centerBounds.x = worldBounds.x - worldBounds.x/zoom;
        centerBounds.y = worldBounds.y - worldBounds.y/zoom;
    }

    public Vector2f worldCoords(Vector2f ndc) {
        Matrix4f PV = new Matrix4f(projection);
        PV.mul(view);
        PV.invert();
        Vector4f result = new Vector4f();
        PV.transform(ndc.x, ndc.y, -1, 1, result);
        return new Vector2f(result.x, result.y);
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }
    public void removeLayer(Layer layer) {
        layers.remove(layer);
    }
    public void clearLayer() {
        layers.clear();
    }

    public void rotate(float angle, float x, float y, float z) {
        Quaternionf q = new Quaternionf();
        q.fromAxisAngleDeg(x,y,z,angle);
        rot.premul(q);
        updateView();
    }

    @Override
    public void translate(Vector2f t) {
        setCenter(new Vector2f(center).add(t));
        updateView();
    }

    @Override
    public void zoom(float amount, Vector2f zoomCenter) {
        amount = Math.min(Math.max(amount, 1.0f/zoom), MAX_ZOOM/zoom);
        zoom *= amount;
        Vector2f c = new Vector2f(center);
        setCenter(zoomCenter.add(c.sub(zoomCenter).mul(1.0f/amount)));
        updateArrowScale();
        updateView();
        updateCenterBounds();
    }

    public void onCreateGL(EGLConfig config) {
        // Set the background frame color
        int colorCode = ContextCompat.getColor(context, R.color.colorPrimary);
        float r = Color.red(colorCode)/255f;
        float g = Color.green(colorCode)/255f;
        float b = Color.blue(colorCode)/255f;
        glClearColor(r, g, b, 1.0f);


        // build the shader program
        program = new ShaderProgram();
        try {
            Shader vs = new Shader(GL_VERTEX_SHADER);
            vs.loadFromResource(R.raw.vertex_shader, context);
            Shader fs = new Shader(GL_FRAGMENT_SHADER);
            fs.loadFromResource(R.raw.fragment_shader, context);
            program.attachShader(vs, fs);
            program.compileAndLink();
        } catch (ShaderCompileException | ProgramLinkException |IOException e) {
            Log.e("SHADER", e.getMessage());
        }
        program.bind();
        programInfo = new ShaderProgramInfo()
            .putLocation(POSITION_NAME, glGetAttribLocation(program.getId(), POSITION_NAME))
            .putLocation(TEXCOORD_NAME, glGetAttribLocation(program.getId(), TEXCOORD_NAME))
            .putLocation(TEXTURE_NAME, glGetUniformLocation(program.getId(), TEXTURE_NAME))
            .putLocation(MVP_MAT_NAME, glGetUniformLocation(program.getId(), MVP_MAT_NAME))
            .putLocation(COLOR_NAME, glGetUniformLocation(program.getId(), COLOR_NAME))
            .putLocation(TRANSPARENCY_NAME, glGetUniformLocation(program.getId(), TRANSPARENCY_NAME))
            .putLocation(POINT_SIZE_NAME, glGetUniformLocation(program.getId(), POINT_SIZE_NAME));

        // we want to blend layers
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST); // no depth testing z=0 for all layers

    }

    @Override
    public void onDrawGL() {
        // Redraw background color
        glClear(GL_COLOR_BUFFER_BIT);

        Matrix4f PV = new Matrix4f(projection);
        PV.mul(view);

        layers.draw(PV, new Matrix4f(), programInfo);
    }

    @Override
    public void onResizeGL(int width, int height) {
        super.onResizeGL(width, height);
        glViewport(0, 0, width, height);
        projection.identity();

        Log.e("SIZE", width +", " + height);
        updateWorldBounds(1, 1/aspect);
        /*
        if (height > width)
            projection.ortho(-1, 1, -1/aspect, 1/aspect, -1, 1);
        else
            projection.ortho(-aspect, aspect, -1, 1, -1, 1);
        */
        updateArrowScale();
    }

    public void updateWorldBounds(float sizeX, float sizeY) {
        projection.identity();
        assert height > width;
        float y = Math.max(sizeX/aspect, sizeY);
        float x = y*aspect;
        projection.ortho(-x, x, -y, y, -1, 1);
        worldBounds.x = x;
        worldBounds.y = y;
        updateView();
        updateCenterBounds();
    }

    public Vector2f getWorldBounds() {
        return worldBounds;
    }

    public ShaderProgramInfo getProgramInfo() {
        return programInfo;
    }
}
