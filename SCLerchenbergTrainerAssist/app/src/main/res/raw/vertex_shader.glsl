
precision mediump int;
precision mediump float;
uniform mat4 mvpMat;
uniform float pointSize;
attribute vec2 a_position;
attribute vec2 a_texcoord;
varying vec2 texcoord;
void main() {
    texcoord = a_texcoord;
    gl_Position = mvpMat* vec4(a_position,0,1);
    gl_PointSize = pointSize;
}