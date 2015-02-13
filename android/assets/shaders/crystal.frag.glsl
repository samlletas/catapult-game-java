// <custom-header>
    varying vec3 v_baseColor;
    varying vec4 v_foregroundColor;

    const vec3 specularColor = vec3(0.9);
    const vec3 shadowColorDifference = vec3(0.1);
    const vec3 lightAbove = vec3(0.2, 0.0, -1.0);
    const vec3 lightBelow = vec3(-0.2, 0.5, -1.0);
// </custom-header>

// <custom-logic>
    color.xyz = v_baseColor;

    // Luz de arriba
    float dotResult = max(dot(normalize(lightAbove), v_normal), 0.0);
    float shadow = pow(1.0 - dotResult, 15.0);
    float specular = pow(dotResult, 8.0);

    color.xyz -= shadowColorDifference * shadow;
    color.xyz += specularColor * specular;


    // Luz de abajo
    dotResult = max(dot(normalize(lightBelow), v_normal), 0.0);
    shadow = pow(1.0 - dotResult, 15.0);
    specular = pow(dotResult, 8.0);

    color.xyz -= shadowColorDifference * shadow;
    color.xyz += specularColor * specular;


    // Aplicación de alpha blending con el foregroundColor
    color.rgb = color.rgb * (1.0 - v_foregroundColor.a) + (v_foregroundColor.rgb * v_foregroundColor.a);

// </custom-logic>