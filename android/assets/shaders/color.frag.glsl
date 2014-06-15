// <custom-header>
varying vec4 v_color;

float someTest()
{
    return 1.0;
}
// </custom-header>

void main()
{
    // <custom-logic>
    color = v_color;
    // </custom-logic>
}