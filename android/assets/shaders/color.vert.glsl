// <extra-fields>
uniform vec4 u_color;
varying vec4 v_color;
// </extra-fields>

// <extra-functions>
float calculate()
{
    return 0.0;
}
// </extra-functions>

void main()
{
    // <extra-logic>
    gl_Position = position;
    v_color = u_color;
    // </extra-logic>
}