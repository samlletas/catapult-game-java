package com.engine.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StringBuilder;

public abstract class CustomShader implements Shader
{
    protected long attributesMask;
    protected Camera camera;
    protected RenderContext context;

    protected String vertexShader;
    protected String fragmentShader;
    protected ShaderProgram program;

    protected Matrix4 modelView;
    protected Matrix4 modelViewProjection;
    protected UniformCollection uniforms;

    public CustomShader(long attributesMask)
    {
        this.attributesMask = attributesMask;
    }

    @Override
    public final void init()
    {
        String baseVertexShader = Gdx.files.classpath(
                "com/engine/shaders/custom.vert.glsl").readString();
        String baseFragmentShader = Gdx.files.classpath(
                "com/engine/shaders/custom.frag.glsl").readString();
        String flags = generateFlags();

        CustomShaderMixer mixer = new CustomShaderMixer();
        vertexShader = mixer.mix(baseVertexShader, getCustomVertexShader(),
                flags);
        fragmentShader = mixer.mix(baseFragmentShader, getCustomFragmentShader(),
                flags);

        program = new ShaderProgram(vertexShader, fragmentShader);

        if (program.isCompiled())
        {
            modelView = new Matrix4();
            modelViewProjection = new Matrix4();

            addUniforms();
            addCustomUniforms();

//            System.out.println(vertexShader);
//            System.out.println(fragmentShader);

            for(String u : program.getUniforms())
            {
                System.out.println(u);
            }

            uniforms.initialize(program);
        }
        else
        {
            throw new GdxRuntimeException(program.getLog());
        }
    }

    private void addUniforms()
    {
        uniforms = new UniformCollection();

        uniforms.add(new Uniform("u_modelView", new IUniformSetter()
        {
            @Override
            public void set(Uniform uniform, ShaderProgram program,
                            Renderable renderable)
            {
                program.setUniformMatrix(uniform.id, modelView);
            }
        }));

        uniforms.add(new Uniform("u_modelViewProjection", new IUniformSetter()
        {
            @Override
            public void set(Uniform uniform, ShaderProgram program,
                            Renderable renderable)
            {
                program.setUniformMatrix(uniform.id, modelViewProjection);
            }
        }));

        if ((attributesMask & VertexAttributes.Usage.TextureCoordinates) != 0)
        {
            uniforms.add(new Uniform("u_texture", new IUniformSetter()
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
    }

    private String generateFlags()
    {
        StringBuilder builder = new StringBuilder();

        if ((attributesMask & VertexAttributes.Usage.Normal) != 0)
        {
            builder.append("#define normalFlag\n");
        }
        if ((attributesMask & VertexAttributes.Usage.TextureCoordinates) != 0)
        {
            builder.append("#define textureFlag\n");
        }
        if ((attributesMask & VertexAttributes.Usage.BoneWeight) != 0)
        {
            builder.append("#define skinningFlag\n");
        }

        return builder.toString();
    }

    @Override
    public int compareTo(Shader other)
    {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance)
    {
        return true;
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
        uniforms.setGlobals(program);
    }

    @Override
    public void render(Renderable renderable)
    {
        modelView.set(camera.view);
        modelView.mul(renderable.worldTransform);

        modelViewProjection.set(camera.projection);
        modelViewProjection.mul(modelView);

        uniforms.setIndividuals(program, renderable);

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
        uniforms.dispose();
    }

    protected abstract String getCustomVertexShader();
    protected abstract String getCustomFragmentShader();
    protected abstract void addCustomUniforms();
}
