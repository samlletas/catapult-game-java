package com.engine.graphics.shaders.shaders3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StringBuilder;
import com.engine.graphics.shaders.IUniformSetter;
import com.engine.graphics.shaders.Uniform;
import com.engine.graphics.shaders.UniformCollection;

public abstract class Custom3DShader implements Shader
{
    private static final String SKINNING_LOGIC_TAG = "// <skinning-logic>";
    private static final Matrix4 IDENTITY_MATRIX = new Matrix4().idt();
    private static StringBuilder vertexBuilder = new StringBuilder();
    private static StringBuilder fragmentBuilder = new StringBuilder();
    private static StringBuilder tempBuilder = new StringBuilder();
    private static String baseVertexShader;
    private static String baseFragmentShader;

    private Renderable renderable;

    protected long attributesMask;
    protected Camera camera;
    protected RenderContext context;
    protected ShaderProgram program;

    protected Matrix4 modelView;
    protected Matrix4 modelViewProjection;
    protected UniformCollection globalUniforms;
    protected UniformCollection localUniforms;

    protected int numBones;

    public Custom3DShader(Renderable renderable)
    {
        this.renderable = renderable;
        this.attributesMask = renderable.mesh.getVertexAttributes().getMask();

        if (renderable.bones != null)
        {
            this.numBones = renderable.bones.length;
        }
        else
        {
            this.numBones = 0;
        }
    }

    @Override
    public final void init()
    {
        clearStringBuilders();
        generateVertexShaderFlags();
        generateFragmentShaderFlags();
        generateSkinningVariables();
        loadBaseShaderCode();
        generateSkinningLogic();

        Custom3DShaderMixer mixer = new Custom3DShaderMixer();

        mixer.mix(vertexBuilder, getCustomVertexShader());
        mixer.mix(fragmentBuilder, getCustomFragmentShader());

//        System.out.print("---VERTEX SHADER---\n\n");
//        System.out.println(vertexBuilder);
//        System.out.print("---FRAGMENT SHADER---\n\n");
//        System.out.println(fragmentBuilder);

        program = new ShaderProgram(vertexBuilder.toString(), fragmentBuilder.toString());

        if (program.isCompiled())
        {
            modelView = new Matrix4();
            modelViewProjection = new Matrix4();

            globalUniforms = new UniformCollection();
            localUniforms = new UniformCollection();

            addBaseUniforms();
            addCustomGlobalUniforms();
            addCustomLocalUniforms();

//            for(String u : program.getUniforms())
//            {
//                System.out.println(u);
//            }

            globalUniforms.initialize(program);
            localUniforms.initialize(program);
        }
        else
        {
            throw new GdxRuntimeException(program.getLog());
        }
    }

    private static void clearStringBuilders()
    {
        vertexBuilder.replace(0, vertexBuilder.length(), "");
        fragmentBuilder.replace(0, fragmentBuilder.length(), "");
        tempBuilder.replace(0, tempBuilder.length(), "");
    }

    private boolean hasVertexAttribute(long usage)
    {
        return (attributesMask & usage) == usage;
    }

    private boolean hasSkinning()
    {
        return hasVertexAttribute(VertexAttributes.Usage.BoneWeight) &&
                numBones > 0;
    }

    private void generateVertexShaderFlags()
    {
        if (hasVertexAttribute(VertexAttributes.Usage.Normal))
        {
            vertexBuilder.append("#define normalFlag\n");
        }
        if (hasVertexAttribute(VertexAttributes.Usage.TextureCoordinates))
        {
            vertexBuilder.append("#define textureFlag\n");
        }
        if (hasSkinning())
        {
            vertexBuilder.append("#define boneWeightsFlag\n");
            vertexBuilder.append("#define numBones ");
            vertexBuilder.append(numBones);
            vertexBuilder.append("\n\n");
        }
    }

    private void generateFragmentShaderFlags()
    {
        if (hasVertexAttribute(VertexAttributes.Usage.Normal))
        {
            fragmentBuilder.append("#define normalFlag\n");
        }
        if (hasVertexAttribute(VertexAttributes.Usage.TextureCoordinates))
        {
            fragmentBuilder.append("#define textureFlag\n");
        }
    }

    private void loadBaseShaderCode()
    {
        if (baseVertexShader == null)
        {
            baseVertexShader = Gdx.files.classpath(
                    "com/engine/graphics/shaders/shaders3d/custom3d.vert.glsl").readString();
        }
        if (baseFragmentShader == null)
        {
            baseFragmentShader = Gdx.files.classpath(
                    "com/engine/graphics/shaders/shaders3d/custom3d.frag.glsl").readString();
        }

        vertexBuilder.append(baseVertexShader);
        fragmentBuilder.append(baseFragmentShader);
    }

    private void generateSkinningVariables()
    {
        if (hasSkinning())
        {
            for(VertexAttribute attribute : renderable.mesh.getVertexAttributes())
            {
                if (attribute.usage == VertexAttributes.Usage.BoneWeight &&
                        attribute.unit < numBones)
                {
                    // Atributo
                    vertexBuilder.append("attribute vec2 a_boneWeight");
                    vertexBuilder.append(attribute.unit);
                    vertexBuilder.append(";\n");

                    // Uniforme
                    vertexBuilder.append("uniform mat4 u_bone");
                    vertexBuilder.append(attribute.unit);
                    vertexBuilder.append(";\n");
                }
            }

            vertexBuilder.append("\n");
        }
    }

    private void generateSkinningLogic()
    {
        if (hasSkinning())
        {
            // Función getBoneTransform
            tempBuilder.append("\nmat4 getBoneTransform(in int index)\n{");

            for (int i = 0; i < numBones; i++)
            {
                if (i == 0)
                {
                    tempBuilder.append("if (index == 0) ");
                }
                else if (i < numBones - 1)
                {
                    tempBuilder.append("else if (index == ");
                    tempBuilder.append(i);
                    tempBuilder.append(") ");
                }
                else
                {
                    tempBuilder.append("else ");
                }

                tempBuilder.append("{ return u_bone");
                tempBuilder.append(i);
                tempBuilder.append("; }\n");
            }

            tempBuilder.append("\n}");
            // Fin función getBoneTransform

            // Transformaciones de skinning
            tempBuilder.append("mat4 processSkinning()\n{\nmat4 skinning = mat4(0.0);\n\n");

            for (int i = 0; i < numBones; i++)
            {
                tempBuilder.append("skinning += (a_boneWeight");
                tempBuilder.append(i);
                tempBuilder.append(".y) * getBoneTransform(int(a_boneWeight");
                tempBuilder.append(i);
                tempBuilder.append(".x));\n");
            }

            tempBuilder.append("\nreturn skinning;\n}");
            // Fin de transformaciones de skinning

            vertexBuilder.insert(
                    vertexBuilder.indexOf(SKINNING_LOGIC_TAG) + SKINNING_LOGIC_TAG.length(),
                    tempBuilder);
        }
    }

    private void addBaseUniforms()
    {
        // Matriz ModelView
        localUniforms.add(new Uniform("u_modelView", new IUniformSetter()
        {
            @Override
            public void set(Uniform uniform, ShaderProgram program,
                            Renderable renderable)
            {
                program.setUniformMatrix(uniform.id, modelView);
            }
        }));

        // Matriz ModelViewProjection
        localUniforms.add(new Uniform("u_modelViewProjection", new IUniformSetter()
        {
            @Override
            public void set(Uniform uniform, ShaderProgram program,
                            Renderable renderable)
            {
                program.setUniformMatrix(uniform.id, modelViewProjection);
            }
        }));

        // Textura Difusa
        if (hasVertexAttribute(VertexAttributes.Usage.TextureCoordinates))
        {
            localUniforms.add(new Uniform("u_texture", new IUniformSetter()
            {
                @Override
                public void set(Uniform uniform, ShaderProgram program,
                                Renderable renderable)
                {
                    TextureAttribute textureAttribute =
                            (TextureAttribute)renderable.material.get(
                                    TextureAttribute.Diffuse);

                    program.setUniformi(
                            uniform.id,
                            context.textureBinder.bind(
                                    textureAttribute.textureDescription));
                }
            }));
        }

        // Skinning
        if (hasSkinning())
        {
            for(int i = 0; i < numBones; i++)
            {
                localUniforms.add(new Uniform("u_bone" + i, new BoneTransformSetter(i)));
            }
        }
    }

    @Override
    public int compareTo(Shader other)
    {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance)
    {
        return attributesMask == instance.mesh.getVertexAttributes().getMask() &&
                !((numBones > 0 && (instance.bones == null || instance.bones.length != numBones)));
    }

    @Override
    public void begin(Camera camera, RenderContext context)
    {
        context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        context.setDepthTest(GL20.GL_LEQUAL);
        context.setCullFace(GL20.GL_BACK);

        this.camera = camera;
        this.context = context;

        program.begin();
        globalUniforms.setUniforms(program, null);
    }

    @Override
    public void render(Renderable renderable)
    {
        modelView.set(camera.view);
        modelView.mul(renderable.worldTransform);

        modelViewProjection.set(camera.projection);
        modelViewProjection.mul(modelView);

        localUniforms.setUniforms(program, renderable);

        renderable.mesh.render(
                program,
                renderable.primitiveType,
                renderable.meshPartOffset,
                renderable.meshPartSize);
    }

    @Override
    public void end()
    {
        program.end();
    }

    @Override
    public void dispose()
    {
        program.dispose();
        globalUniforms.dispose();
        localUniforms.dispose();
    }

    protected abstract String getCustomVertexShader();
    protected abstract String getCustomFragmentShader();
    protected abstract void addCustomGlobalUniforms();
    protected abstract void addCustomLocalUniforms();

    class BoneTransformSetter implements IUniformSetter
    {
        private int boneTransformIndex;

        public BoneTransformSetter(int boneTransformIndex)
        {
            this.boneTransformIndex = boneTransformIndex;
        }

        @Override
        public void set(Uniform uniform, ShaderProgram program, Renderable renderable)
        {
            if (renderable.bones == null || renderable.bones[boneTransformIndex] == null)
            {
                program.setUniformMatrix(uniform.id, IDENTITY_MATRIX);
            }
            else
            {
                program.setUniformMatrix(uniform.id, renderable.bones[boneTransformIndex]);
            }
        }
    }
}
