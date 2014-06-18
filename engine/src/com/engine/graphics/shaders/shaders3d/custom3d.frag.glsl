#ifdef GL_ES
    precision mediump float;
#endif

varying vec3 v_position;

#ifdef normalFlag
    varying vec3 v_normal;
#endif

#ifdef textureFlag
    varying vec2 v_texCoord0;
    uniform sampler2D u_texture;
#endif

// <custom-header>

void main()
{
    #ifdef textureFlag
        vec4 color = texture2D(u_texture, v_texCoord0);
        color.a = 1.0;
    #else
        vec4 color = vec4(1.0, 1.0, 1.0, 1.0);
    #endif

    // <custom-logic>

    gl_FragColor = color;
}

