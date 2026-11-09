package io.github.GoosePatrolGame.utils;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * Utility class for generating texture atlases from individual image files.
 * 
 * Uses LibGDX's TexturePacker to process images from an input directory and
 * generate an atlas file alongside its associated texture image.
 */
public class TexturePackerTool {
    /**
     * Entry point for running the texture packing process.
     * 
     * Defines the input directory, output directory and name of the 
     * generated atlas before passing them to TexturePacker.
     * 
     * @param args Command-line arguments, not used.
     */
    public static void main(String[] args) {
        // Directory containing the original, unprocessed image files.
        String inputDir = "assets_raw/objects";

        // Directory where the generated atlas and texture image will be saved.
        String outputDir = "assets/graphics";

        // Base name used for the generated atlas and texture files.
        String packFileName = "objects";

        // Packs the images from the input directory into a texture atlas.
        TexturePacker.process(inputDir, outputDir, packFileName);
    }
}
