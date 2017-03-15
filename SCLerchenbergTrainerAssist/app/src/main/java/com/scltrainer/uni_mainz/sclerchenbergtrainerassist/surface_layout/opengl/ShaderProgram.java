package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.*;

/**
 * Created by Julian on 06.03.2017.
 */

public class ShaderProgram {

    public final int id;

    public String name;

    private List<Shader> attachedShaders = new ArrayList<>();


    private boolean linked;

    /**
     *
     */
    public ShaderProgram() {
        id = glCreateProgram();
        name = "ShaderProgram"+id;
    }

    /**
     *
     */
    public ShaderProgram(String name) {
        id = glCreateProgram();
        this.name = name;
    }

    public void attachShader(Shader... shaders) {
        for (Shader shader : shaders) {
            glAttachShader(id, shader.id);
            attachedShaders.add(shader);
        }
        if (shaders.length > 0)
            linked = false;
    }

    public void compile() throws ShaderCompileException {
        for (Shader shader : attachedShaders)
            shader.compile();
    }

    public void link() throws ProgramLinkException {
        if (!linked) {
            glLinkProgram(id);
            int success[] = new int[1];
            glGetProgramiv(id, GL_LINK_STATUS, success, 0);
            linked = (success[0] == GL_TRUE);
            if (!linked)
                throw new ProgramLinkException(glGetProgramInfoLog(id));
        }
    }

    public void compileAndLink() throws ShaderCompileException, ProgramLinkException {
        compile();
        link();
    }

    public void bind() {
        if (!linked)
            throw new ProgramNotLinkedException(name);
        glUseProgram(id);
    }

    public void unBind() {
        glUseProgram(0);
    }

    public boolean isLinked() {
        return linked;
    }

    public void delete() {
        glDeleteProgram(id);
    }

    public int getId() {
        return id;
    }
}
