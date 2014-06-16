// <custom-header>
uniform float u_glowFactor;
varying float v_glowFactor;

varying vec3 v_originalPosition;
varying vec3 v_newPosition;

float calculate()
{
    return 0.0;
}
// </custom-header>

void main()
{
    // <custom-logic>
    v_originalPosition = (u_modelView * position).xyz;

    position += u_glowFactor * normal;

    v_newPosition = (u_modelView * position).xyz;

    v_glowFactor = u_glowFactor;
    // </custom-logic>
}