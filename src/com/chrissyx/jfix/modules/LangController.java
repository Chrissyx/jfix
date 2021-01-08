package com.chrissyx.jfix.modules;

import com.chrissyx.jfix.modules.util.FileUtils;
import com.chrissyx.jfix.modules.util.ResourceUtils;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import org.slf4j.LoggerFactory;

/**
 * Manages translation and formatting of language strings.
 *
 * @author Chrissyx
 * @since 0.4
 */
public final class LangController implements Observer
{
    /**
     * Singleton instance of this class.
     */
    private static LangController langController = new LangController();

    /**
     * Handler / cache for localized strings.
     */
    private ResourceBundle resourceBundle;

    /**
     * Name of base resource file.
     */
    private final String resourceName = ResourceUtils.getPath("jfix-lang");

    /**
     * Loads localization based on detected user's preference.
     */
    private LangController()
    {
        ConfigController.getInstance().addObserver(this);
        try
        {
            this.resourceBundle = ResourceBundle.getBundle(this.resourceName, new Locale(ConfigController.getInstance().getCfgVal("language")));
        }
        catch(final MissingResourceException e)
        {
            LoggerFactory.getLogger(LangController.class).error("No localization file found!", e);
        }
        LoggerFactory.getLogger(LangController.class).info("Localization for '{}' loaded", this.resourceBundle.getLocale().getLanguage());
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return Instance of this class
     */
    public static LangController getInstance()
    {
        return LangController.langController;
    }

    /**
     * Returns list of supported locales.
     *
     * @return Locales usable for this controller
     */
    public String[] getLocales()
    {
        final LinkedList<String> localeList = new LinkedList<String>();
        for(final String curLocale : FileUtils.getPackageListing("resources", ".properties"))
            localeList.add(curLocale.replace("jfix-lang_", "")); //.replaceFirst("_", ""));
        return localeList.toArray(new String[0]);
    }

    /**
     * Returns a translated language string for stated key.
     *
     * @param key Identifier of translated string
     * @return Localized string or the key itself if identifier was not found
     */
    public String getString(final String key)
    {
        LoggerFactory.getLogger(LangController.class).debug("Returning string for key '{}'...", key);
        try
        {
            return this.resourceBundle.getString(key);
        }
        catch(final MissingResourceException e)
        {
            LoggerFactory.getLogger(LangController.class).warn("No localization for key '{}' found!", key);
        }
        return key;
    }

    /**
     * Returns a parametrized translated string for stated key and replaces given arguments.
     * The string has to define a placeholder with {@code {argumentIndex[,formatType[,formatStyle]]}},
     * e.g. {@code {0,number,integer}} to format a number as an integer and insert it at position 0.
     *
     * @param key Identifier of translated string
     * @param args Argument(s) to insert into translated string at predefined positions
     * @return Localized string or the key itself if identifier was not found
     */
    public String getString(final String key, final Object... args)
    {
        try
        {
            return MessageFormat.format(this.getString(key), args);
        }
        catch(final IllegalArgumentException e)
        {
            LoggerFactory.getLogger(LangController.class).warn("Invalid pattern in translated string or wrong type of arguments for key '{}'!", key);
        }
        return key;
    }

    /**
     * Sets new locale to use for string translations.
     *
     * @param newLocale New locale
     */
    public void setLocale(final Locale newLocale)
    {
        ResourceBundle.clearCache();
        try
        {
            this.resourceBundle = ResourceBundle.getBundle(this.resourceName, newLocale);
            if(newLocale.getLanguage().equals(this.resourceBundle.getLocale().getLanguage()))
                LoggerFactory.getLogger(LangController.class).debug("New locale set to '{}'", newLocale.getLanguage());
            else
                LoggerFactory.getLogger(LangController.class).warn("No localization file for '{}' found!", newLocale.getLanguage());
        }
        catch(final MissingResourceException e)
        {
            LoggerFactory.getLogger(LangController.class).error("No localization file found!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final Observable o, final Object arg)
    {
        this.setLocale(new Locale(ConfigController.getInstance().getCfgVal("language")));
    }
}
