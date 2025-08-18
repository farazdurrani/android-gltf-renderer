package com.faraz.libgdx;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

/**
 * See: http://blog.xoppa.com/3d-frustum-culling-with-libgdx
 *
 * @author Xoppa
 */
public class Main2 implements ApplicationListener {
    public static final String[] fileExtension = {"glb", "gltf"};
    public static final String filename = "Grinnell_Lake." + fileExtension[0];
    private final String data = "/home/faraz/Android/code-workspace/libgdx-tutorial/assets/data/" + filename;
    private final Vector3 position = new Vector3();
    protected PerspectiveCamera cam;
    protected CameraInputController camController;
    protected ModelBatch modelBatch;
    protected AssetManager assets;
    protected Array<ModelInstance> instances = new Array<>();
    protected Environment environment;
    protected boolean loading;
    protected Stage stage;
    protected Label label;
    protected BitmapFont font;
    protected StringBuilder stringBuilder;
    private int visibleCount;
    private DirectionalLightEx light;

    @Override
    public void create() {
        stage = new Stage();
        font = new BitmapFont();
        label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
        stage.addActor(label);
        stringBuilder = new StringBuilder();

        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        environment.add(light);

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 6500f, 0f);
        cam.lookAt(0, 0, 0);
        cam.near = 0.1f;
        cam.far = 30000f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        assets = new AssetManager();
        if (data.endsWith(".gltf")) {
            assets.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());
        } else {
            assets.setLoader(SceneAsset.class, data, new GLBAssetLoader());
        }
        assets.load(data, SceneAsset.class);
        loading = true;
    }

    private void doneLoading() {
        Model model = assets.get(data, SceneAsset.class).scene.model;
        for (int i = 0; i < model.nodes.size; i++) {
            String id = model.nodes.get(i).id;
//            GameObject instance = new GameObject(model, id, true);
            instances.add(new ModelInstance(model, id));
        }
        loading = false;
    }

    @Override
    public void render() {
        if (loading && assets.update())
            doneLoading();
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        visibleCount = 0;
        for (final ModelInstance instance : instances) {
            modelBatch.render(instance, environment);
            visibleCount++;
        }

        modelBatch.end();

        stringBuilder.setLength(0);
        stringBuilder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
        stringBuilder.append(" Visible: ").append(visibleCount);
        label.setText(stringBuilder);
        stage.draw();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

//    public static class GameObject extends ModelInstance {
//        private final static BoundingBox bounds = new BoundingBox();
//        public final Vector3 center = new Vector3();
//        public final Vector3 dimensions = new Vector3();
//        public final float radius;
//
//        public GameObject(Model model, String rootNode, boolean mergeTransform) {
//            super(model, rootNode, mergeTransform);
//            calculateBoundingBox(bounds);
//            bounds.getCenter(center);
//            bounds.getDimensions(dimensions);
//            radius = dimensions.len() / 2f;
//        }
//    }
}
