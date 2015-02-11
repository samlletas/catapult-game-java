attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform vec4 u_foregroundColor;

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec4 v_foregroundColor;

void main()
{
    gl_Position = u_projTrans * a_position;

    v_color = a_color;
    v_color.a = v_color.a * (255.0/254.0);

    v_texCoords = a_texCoord0;

    v_foregroundColor = u_foregroundColor;
    v_foregroundColor.a = v_foregroundColor.a * (255.0/254.0);
}