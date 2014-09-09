// <custom-header>

    const vec3 lightAbove = vec3(0.2, 0.0, -1.0);
    const vec3 lightBelow = vec3(-0.2, 0.5, -1.0);
    const vec3 diffuseColor = vec3(111.0 / 255.0, 102.0 / 255.0, 104.0 / 255.0);
    const vec3 specularColor = vec3(1.0, 1.0, 1.0);

// </custom-header>


// <custom-logic>

    float dot = max(dot(normalize(lightAbove), v_normal), 0.0);

    float diffuse = pow(1.0 - dot, 10.0);
    float specular = pow(dot, 10.0);

    color.xyz -= diffuseColor * diffuse * 0.2;
    color.xyz += specularColor * specular;



    dot = max(dot(normalize(lightBelow), v_normal), 0.0);

    diffuse = pow(1.0 - dot, 10.0);
    specular = pow(dot, 15.0);

    color.xyz -= diffuseColor * diffuse * 0.2;
    color.xyz += specularColor * specular;

// </custom-logic>