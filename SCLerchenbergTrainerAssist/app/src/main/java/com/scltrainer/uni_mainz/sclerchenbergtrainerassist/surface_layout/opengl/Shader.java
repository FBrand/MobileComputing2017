package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.opengl.GLES20.*;

/**
 * Created by Julian on 06.03.2017.
 * Klasse um eine  Shader zu Laden
 */

public class Shader {
    public final int id;


    private boolean compiled;

    public Shader(int type) {
        id = glCreateShader(type);
    }

    public Shader(int type, String source) {
        this(type);
        setShaderSource(source);
    }

    /**
     *
     * @param source
     */
    public void setShaderSource(String source) {
        glShaderSource(id, source);
        compiled = false;
    }

    /**
     * Compiled einen Shader
     * @throws ShaderCompileException
     */
    public void compile() throws ShaderCompileException {
        if (!compiled) {
            glCompileShader(id);

            int success[] = new int[1];
            glGetShaderiv(id, GL_COMPILE_STATUS, success, 0);
            compiled = success[0] == GL_TRUE;
            if (!compiled)
                throw new ShaderCompileException(glGetShaderInfoLog(id));
        }
    }

    /**
     * Lädt eine Shader aus den Resource Ordner
     * @param resId
     * @param context
     * @throws IOException
     */
    public void loadFromResource(int resId, Context context) throws IOException {
        InputStream inputstream = context.getResources().openRawResource(resId);

        InputStreamReader isr = new InputStreamReader(inputstream);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder text = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            text.append(line);
        }
        br.close();
        setShaderSource(text.toString());
    }

    public int getId() {
        return id;
    }

    public void delete() {
        glDeleteShader(id);
    }
}
