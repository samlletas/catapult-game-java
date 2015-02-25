attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4  u_projTrans;
uniform float u_thickness;
uniform float u_smoothing;
uniform vec4  u_foregroundColor;
uniform float u_renderShadows;
uniform vec2  u_shadowOffset;
uniform vec4  u_shadowColor;

varying vec4  v_color;
varying vec2  v_texCoord;
varying float v_thickness;
varying float v_smoothing;
varying vec4  v_foregroundColor;
varying vec2  v_shadowOffset;
varying vec4  v_shadowColor;

void main()
{
    gl_Position = u_projTrans * a_position;

    v_color = a_color;
    v_texCoord = a_texCoord0;
    v_thickness = u_thickness;
    v_smoothing = u_smoothing;
    v_shadowOffset = u_shadowOffset;

    v_foregroundColor = u_foregroundColor;
    v_foregroundColor.a = v_foregroundColor.a * (255.0/254.0);

    v_shadowColor = u_shadowColor;
    v_shadowColor.a = v_shadowColor.a * (255.0/254.0);
}