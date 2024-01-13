package newaimod.properties;

import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class PropertyManager {
    public static final Logger logger = LogManager.getLogger(PropertyManager.class.getName());

    private static final PropertyManager instance = new PropertyManager();


    private static final Properties myProperties = new Properties();
    public static final String ENABLE = "enable";
    public boolean enabled;

    private PropertyManager() {}

    public static PropertyManager getInstance() {
        return instance;
    }

    public void initializeProperties() {
        myProperties.setProperty(ENABLE, "FALSE");

        try {
            SpireConfig config = new SpireConfig("NewAIMod", "NewAIConfig", myProperties); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enabled = config.getBool(ENABLE);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


    public ModPanel getSettingsPanel() {
        ModPanel settingsPanel = new ModPanel();

        ModLabeledToggleButton enableAutoCombatButton = new ModLabeledToggleButton("Enable automatic combat.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enabled, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {
                },
                (button) -> {
                    enabled = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("NewAIMod", "NewAIConfig", myProperties);
                        config.setBool(ENABLE, enabled);
                        config.save();
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                });
        settingsPanel.addUIElement(enableAutoCombatButton);

        return settingsPanel;
    }

}
