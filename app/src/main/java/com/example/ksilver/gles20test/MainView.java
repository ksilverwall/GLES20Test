package com.example.ksilver.gles20test;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainView extends GLSurfaceView {

    public MainView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new Renderer());
    }
    private class Renderer implements GLSurfaceView.Renderer {
        private int m_Program;
        private Buffer m_VertexBuffer;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            String vertexShaderSource = ""
                    + "attribute vec4 vPosition;"
                    + "void main(){"
                    + "    gl_Position = vPosition;"
                    + "}";
            String fragmentShaderSource = ""
                    + "void main(){"
                    + "    gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);"
                    + "}";

            int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
            int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
            GLES20.glShaderSource(vertexShader,vertexShaderSource);
            GLES20.glShaderSource(fragmentShader, fragmentShaderSource);
            GLES20.glCompileShader(vertexShader);
            GLES20.glCompileShader(fragmentShader);

            int program = GLES20.glCreateProgram();
            GLES20.glAttachShader(program,vertexShader);
            GLES20.glAttachShader(program,fragmentShader);
            GLES20.glLinkProgram(program);

            m_Program = program;

            float[] vertex = {
                    -0.5f, -0.5f, 0.0f, 0.0f,
                    0.5f, -0.5f, 0.0f, 0.0f,
                    0.0f, 0.5f, 0.0f, 0.0f,
            };
            FloatBuffer fb = ByteBuffer.allocateDirect(vertex.length * 4)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            fb.put(vertex);
            fb.position(0);
            m_VertexBuffer = fb;
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0,0,width,height);
        }
        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            GLES20.glUseProgram(m_Program);
            int position = GLES20.glGetAttribLocation(m_Program, "vPosition");

            GLES20.glEnableVertexAttribArray(position);
            GLES20.glVertexAttribPointer(position, 2, GLES20.GL_FLOAT, false, 0, m_VertexBuffer);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,3);
        }
    }
}