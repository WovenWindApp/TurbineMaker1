package edu.umich.turbinemaker1.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class PartsContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Part> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Part> ITEM_MAP = new HashMap<>();

    // CREATE ITEMS HERE
    static {
        // Create Part items
        Part blades = new Part("Blades", "", "BLADES DETAILS HERE");
        Part structure = new Part("Structure", "", "STRUCTURE DETAILS HERE");
        Part output = new Part("Output", "", "OUTPUT DETAILS HERE");
        Part location = new Part("Location", "", "LOCATION DETAILS HERE");

        // Add items
        addItem(blades);
        addItem(structure);
        addItem(output);
        addItem(location);
    }

    private static void addItem(Part item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class Part {
        public final String id;
        public final String content;
        public final String details;

        public Part(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
