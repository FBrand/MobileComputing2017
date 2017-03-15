
precision mediump int;
precision mediump float;
varying vec2 texcoord;
uniform sampler2D tex;
uniform vec4 color;
uniform float transparency;
void main() {
    vec4 t = texture2D(tex, texcoord);
    gl_FragColor = vec4(t.rgb*(1.0-color.a) + color.rgb * color.a, transparency*t.a);
}

