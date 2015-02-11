attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4  u_projTrans;
uniform float u_thickness;
uniform float u_smoothing;

varying vec4  v_color;
varying vec2  v_texCoord;
varying float v_thickness;
varying float v_smoothing;

void main()
{
    gl_Position = u_projTrans * a_position;

    v_color = a_color;
    v_texCoord = a_texCoord0;
    v_thickness = u_thickness;
    v_smoothing = u_smoothing;
}