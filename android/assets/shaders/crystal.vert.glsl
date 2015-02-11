// <custom-header>
    uniform vec3 u_baseColor;
    uniform vec4 u_foregroundColor;

    varying vec3 v_baseColor;
    varying vec4 v_foregroundColor;
// </custom-header>

// <custom-logic>
    v_baseColor = u_baseColor;

    v_foregroundColor = u_foregroundColor;
    v_foregroundColor.a = v_foregroundColor.a * (255.0/254.0);
// </custom-logic>