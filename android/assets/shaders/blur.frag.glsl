#ifdef GL_ES
    #define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
varying highp vec2 blurCoordinates[5];

void main()
{
    //gl_FragColor = v_color * texture2D(u_texture, v_texCoords);

    lowp vec4 sum = vec4(0.0);
    sum += texture2D(u_texture, blurCoordinates[0]) * 0.204164;
    sum += texture2D(u_texture, blurCoordinates[1]) * 0.304005;
    sum += texture2D(u_texture, blurCoordinates[2]) * 0.304005;
    sum += texture2D(u_texture, blurCoordinates[3]) * 0.093913;
    sum += texture2D(u_texture, blurCoordinates[4]) * 0.093913;
    gl_FragColor = sum;
}
