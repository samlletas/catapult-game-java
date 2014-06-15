// <custom-header>
uniform vec4 u_color;
varying vec4 v_color;

float calculate()
{
    return 0.0;
}
// </custom-header>

void main()
{
    // <custom-logic>
    v_color = u_color;
    // </custom-logic>
}