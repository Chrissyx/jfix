package com.chrissyx.jfix.modules;

import com.chrissyx.jfix.common.error.JFixError;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Properties;

import org.slf4j.LoggerFactory;

/**
 * Manages configuration values.
 *
 * @author Chrissyx
 * @since 0.2
 */
public final class ConfigController extends Observable
{
    /**
     * Singleton instance of this class.
     */
    private static ConfigController configController = new ConfigController();

    /**
     * Properties handler.
     */
    private final Properties properties;

    /**
     * Name of XML configuration file.
     */
    private final String configFile;

    /**
     * Creates properties handler.
     */
    private ConfigController()
    {
        this.properties = new Properties();
        this.configFile = System.getProperty("user.dir") + File.separator + "jfix-config.xml";
        LoggerFactory.getLogger(ConfigController.class).debug("Loading settings from {}...", this.configFile);
        try
        {
            this.properties.loadFromXML(new FileInputStream(this.configFile));
            LoggerFactory.getLogger(ConfigController.class).info("Loaded {} settings!", this.properties.size());
        }
        catch(final FileNotFoundException e)
        {
            LoggerFactory.getLogger(ConfigController.class).warn("Config XML file not found, loading defaults...", e);
            this.setCfgVal("port", "21");
            this.setCfgVal("localStartDir", System.getProperty("user.dir"));
            this.setCfgVal("remoteStartDir", "/");
            this.setCfgVal("plugIn", "SiteUtime");
            this.setCfgVal("recursive", "false");
            this.setCfgVal("language", "en_US");
        }
        catch(final IOException e)
        {
            new JFixError("Cannot access config XML file!", e).error(ConfigController.class);
        }
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return Instance of this class
     */
    public static ConfigController getInstance()
    {
        return ConfigController.configController;
    }

    /**
     * Returns a single configuration value.
     *
     * @param key Identifier of config value
     * @return Requested config value <b>or empty string</b>
     * @see #hasCfgVal(java.lang.String)
     */
    public String getCfgVal(final String key)
    {
        LoggerFactory.getLogger(LangController.class).debug("Returning config value for key '{}'...", key);
        return this.properties.getProperty(key, "");
    }

    /**
     * Returns the stated configuration identifier is known.
     *
     * @param key Identifier of config value
     * @return Requested config value exists
     */
    public boolean hasCfgVal(final String key)
    {
        return this.properties.containsKey(key);
    }

    /**
     * Sets a single configuration value.
     *
     * @param key Identifier to access the value
     * @param value Configuration entry
     */
    public void setCfgVal(final String key, final String value)
    {
        this.properties.setProperty(key, value);
    }

    /**
     * Saves configuration values to file and notifies observing controllers.
     *
     * @throws JFixError If saving failed
     */
    public void save() throws JFixError
    {
        LoggerFactory.getLogger(ConfigController.class).debug("Saving settings...");
        try
        {
            this.properties.storeToXML(new FileOutputStream(this.configFile), null);
        }
        catch(final IOException e)
        {
            throw new JFixError("Cannot save settings to file!", e);
        }
        LoggerFactory.getLogger(ConfigController.class).info("Settings saved!");
        //Notify regged controllers about new config
        this.setChanged();
        this.notifyObservers();
    }
}
