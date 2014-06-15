package com.engine.shaders;

import com.badlogic.gdx.utils.StringBuilder;

public class CustomShaderMixer
{
    private static final String HEADER_TAG    = "// <custom-header>";
    private static final String HEADER_ENDTAG = "// </custom-header>";
    private static final String LOGIC_TAG     = "// <custom-logic>";
    private static final String LOGIC_ENDTAG  = "// </custom-logic>";

    public void mix(StringBuilder baseShader, String customShader)
    {
        copyFromCustomShader(baseShader, customShader, HEADER_TAG, HEADER_ENDTAG);
        copyFromCustomShader(baseShader, customShader, LOGIC_TAG, LOGIC_ENDTAG);
    }

    private void copyFromCustomShader(StringBuilder baseShader, String customShader,
                                      String tag, String endTag)
    {
        insertInto(baseShader, tag,
                customShader.substring(
                    customShader.indexOf(tag) + tag.length(),
                    customShader.indexOf(endTag) - 1)); // -1 para saltar \n
    }

    private void insertInto(StringBuilder baseShader, String tag, String code)
    {
        baseShader.insert(baseShader.indexOf(tag) + tag.length(), code);
    }
}