#ifdef GL_ES
    #define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
varying vec4 v_foregroundColor;

uniform sampler2D u_texture;

void main()
{
    vec4 color = v_color;

    // Aplicación de alpha blending con el foregroundColor
    color.rgb = color.rgb * (1.0 - v_foregroundColor.a) + (v_foregroundColor.rgb * v_foregroundColor.a);

    gl_FragColor = color;
}