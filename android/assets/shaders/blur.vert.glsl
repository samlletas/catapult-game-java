attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

varying vec4 v_color;
varying vec2 v_texCoords;

const float texelWidthOffset = 1.0 / 800.0;
const float texelHeightOffset = 1.0 / 480.0;

varying vec2 blurCoordinates[5];

void main()
{
    v_color = a_color;
    v_color.a = v_color.a * (256.0/255.0);
    v_texCoords = a_texCoord0;
    gl_Position =  u_projTrans * a_position;

    vec2 inputTextureCoordinate = a_texCoord0;
    vec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);

    blurCoordinates[0] = inputTextureCoordinate.xy;
    blurCoordinates[1] = inputTextureCoordinate.xy + singleStepOffset * 1.407333;
    blurCoordinates[2] = inputTextureCoordinate.xy - singleStepOffset * 1.407333;
    blurCoordinates[3] = inputTextureCoordinate.xy + singleStepOffset * 3.294215;
    blurCoordinates[4] = inputTextureCoordinate.xy - singleStepOffset * 3.294215;
}