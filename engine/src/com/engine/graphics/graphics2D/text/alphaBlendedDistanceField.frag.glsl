#ifdef GL_ES
    #define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

uniform sampler2D u_texture;

varying LOWP vec4 v_color;
varying LOWP vec4 v_shadowColor;
varying LOWP vec4 v_foregroundColor;

varying vec2  v_texCoord;
varying float v_thickness;
varying float v_smoothing;
varying float v_renderShadows;
varying vec2  v_shadowOffset;

void main()
{
    // Soft Edges
    float distance = texture2D(u_texture, v_texCoord).a;
    float alphaMask = smoothstep(v_thickness - v_smoothing, v_thickness + v_smoothing, distance);
    vec4 color = vec4(v_color.rgb, alphaMask);

    // Shadow
    if (v_renderShadows > 0.0)
    {
        float shadowDistance = texture2D(u_texture, v_texCoord + v_shadowOffset).a;
        float shadowMask = smoothstep(v_thickness - v_smoothing, v_thickness + v_smoothing, shadowDistance);
        vec4 shadowColor = vec4(v_shadowColor.rgb, v_shadowColor.a * shadowMask);
        color = mix(shadowColor, color, alphaMask);
    }

    color.a *= v_color.a;

    // AlphaBlending con el foregroundColor
    color.rgb = color.rgb * (1.0 - v_foregroundColor.a) + (v_foregroundColor.rgb * v_foregroundColor.a);

    gl_FragColor = color;
}