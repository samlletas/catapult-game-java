#ifdef GL_ES
    #define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

uniform sampler2D u_texture;

varying LOWP vec4 v_color;
varying vec2 v_texCoord;
varying float v_thickness;
varying float v_smoothing;

void main()
{
    float distance = texture2D(u_texture, v_texCoord).a;
    float alpha = smoothstep(v_thickness - v_smoothing, v_thickness + v_smoothing, distance) * v_color.a;

    gl_FragColor = vec4(v_color.rgb, alpha);
}