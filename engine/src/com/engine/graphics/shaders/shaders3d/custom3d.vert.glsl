attribute vec3 a_position;
varying vec3 v_position;

#ifdef normalFlag
    attribute vec3 a_normal;
    uniform mat3 u_normalMatrix;
    varying vec3 v_normal;
#endif

#ifdef textureFlag
    attribute vec2 a_texCoord0;
    varying vec2 v_texCoord0;
#endif

#if defined(numBones) && defined(boneWeightsFlag)
    #if (numBones > 0)
        #define skinningFlag
    #endif
#endif

uniform mat4 u_modelView;
uniform mat4 u_modelViewProjection;

// <custom-header>

#ifdef skinningFlag
    // <skinning-logic>
#endif

void main()
{
    #ifdef skinningFlag
        mat4 skinning = processSkinning();
        vec4 position = skinning * vec4(a_position, 1.0);
    #else
        vec4 position = vec4(a_position, 1.0);
    #endif

    #ifdef normalFlag
        #ifdef skinningFlag
            vec3 normal = normalize((u_modelView * skinning * vec4(a_normal, 0.0)).xyz);
        #else
            vec3 normal = normalize(u_normalMatrix * a_normal);
        #endif
    #endif

    // <custom-logic>

    // Aplicación de transformaciones
    gl_Position = u_modelViewProjection * position;

    // Asignación de varyings
    v_position = (u_modelView * position).xyz;

    #ifdef normalFlag
        v_normal = normal;
    #endif

    #ifdef textureFlag
        v_texCoord0 = a_texCoord0;
    #endif
    // Fin asignación de varyings
}