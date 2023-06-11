package newaimod.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashMap;

import static newaimod.NewAIMod.*;

public class TextureLoader {
    private static final HashMap<String, Texture> textures = new HashMap<>();

    /**
     * @param filePath - String path to the texture you want to load relative to resources,
     * Example: resourcePath("missing.png")
     * @return <b>com.badlogic.gdx.graphics.Texture</b> - The texture from the path provided
     */
    public static Texture getTexture(final String filePath) {
        return getTexture(filePath, true);
    }
    public static Texture getTexture(final String filePath, boolean linear) {
        if (textures.get(filePath) == null) {
            try {
                loadTexture(filePath, linear);
            } catch (GdxRuntimeException e) {
                try
                {
                    return getTexture(resourcePath("missing.png"), false);
                }
                catch (GdxRuntimeException ex) {
                    logger.info("missing.png is missing!");
                    return null;
                }
            }
        }
        return textures.get(filePath);
    }

    private static void loadTexture(final String textureString, boolean linearFilter) throws GdxRuntimeException {
        Texture texture = new Texture(textureString);
        if (linearFilter)
        {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        else
        {
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
        logger.info("Loaded texture " + textureString);
        textures.put(textureString, texture);
    }

}