// <custom-header>
varying float v_glowFactor;
varying vec3 v_originalPosition;
varying vec3 v_newPosition;

float someTest()
{
    return 1.0;
}
// </custom-header>

void main()
{
    // <custom-logic>
    float d = distance(v_originalPosition.xy, v_newPosition.xy);

    color.a = 0.5;
    color = vec4(vec3(v_normal), 1.0);


    vec3 camera = vec3(0.0, 0.0, 1.0);

    float c = dot(v_normal, camera);
    //c = pow(c, 3.0);
    color = vec4(vec3(c), 1.0);

    // </custom-logic>
}