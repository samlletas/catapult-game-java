package com.engine.shaders;

import com.badlogic.gdx.utils.StringBuilder;

public class CustomShaderMixer
{
    private static final String FIELDS_TAG       = "// <extra-fields>";
    private static final String FIELDS_ENDTAG    = "// </extra-fields>";
    private static final String FUNCTIONS_TAG    = "// <extra-functions>";
    private static final String FUNCTIONS_ENDTAG = "// </extra-functions>";
    private static final String LOGIC_TAG        = "// <extra-logic>";
    private static final String LOGIC_ENDTAG     = "// </extra-logic>";

    private StringBuilder builder = new StringBuilder();

    public String mix(String baseShader, String customShader, String flags)
    {
        builder.replace(0, builder.length(), "");
        builder.append(flags);
        builder.append(baseShader);

        insertCode(customShader, FIELDS_TAG, FIELDS_ENDTAG);
        insertCode(customShader, FUNCTIONS_TAG, FUNCTIONS_ENDTAG);
        insertCode(customShader, LOGIC_TAG, LOGIC_ENDTAG);

        return builder.toString();
    }

    private void insertCode(String customShader, String tag, String endTag)
    {
        builder.insert(
                builder.indexOf(tag) + tag.length(),
                customShader.substring(
                        customShader.indexOf(tag) + tag.length(),
                        customShader.indexOf(endTag) - 1)); // -1 para saltar \n
    }
}
