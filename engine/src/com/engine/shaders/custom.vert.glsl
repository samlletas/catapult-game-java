attribute vec3 a_position;
varying vec3 v_position;

#ifdef normalFlag
    attribute vec3 a_normal;
    varying vec3 v_normal;
#endif

#ifdef textureFlag
    attribute vec2 a_texCoord0;
    varying vec2 v_texCoord0;
#endif

#ifdef skinningFlag
    #ifdef skinningFlag0
        attribute vec2 skinningFlag0;
    #endif
#endif

uniform mat4 u_modelView;
uniform mat4 u_modelViewProjection;

// <extra-fields>

// <extra-functions>

void main()
{
    #ifdef skinnkingFlag
    #endif

    #ifdef normalFlag
    #endif

    #ifdef textureFlag
        v_texCoord0 = a_texCoord0;
    #endif

    vec4 position = u_modelViewProjection * vec4(a_position, 1.0);

    // <extra-logic>

    v_position = (u_modelView * vec4(a_position, 1.0)).xyz;
}