attribute vec2 a_position;
attribute float a_opacity;

uniform mat4 u_projTrans;
uniform vec4 u_color;
uniform vec4 u_foregroundColor;

varying vec4 v_color;
varying vec4 v_foregroundColor;

void main()
{
    v_color = u_color;
    v_color.a = a_opacity;

    gl_Position = u_projTrans * vec4(a_position.xy, 0.0, 1.0);

    v_foregroundColor = u_foregroundColor;
    v_foregroundColor.a = v_foregroundColor.a * (255.0/254.0);
}