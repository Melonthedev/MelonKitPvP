package wtf.melonthedev.melonkitpvp;

import java.util.Objects;

public class KitPvpSettings {

    private KitPvpSettings() {} //Utility class

    public static void initSettings() {
        initSetting("defaultkillbonus", 3, 3);
        initSetting("restockonkill", 3, 1);
        initSetting("breakableblocks", 3, 1);
        initSetting("neutralplayers", 3, 3);
        initSetting("mapreset", 5, 3);

        if (KitPvP.getMultipleChoiceSetting("templatemap") == null)
            KitPvP.setMultipleChoiceSetting("templatemap", "none");
        if (KitPvP.getMultipleChoiceSetting("kitpvpmap") == null)
            KitPvP.setMultipleChoiceSetting("kitpvpmap", "world");
        if (!Main.getPlugin().getConfig().contains("settings.enableshields")) KitPvP.setSetting("enableshields", true);
    }

    public static void initSetting(String setting, int maxOptionValue, int defaultValue) {
        if (KitPvP.getMultipleChoiceSetting(setting) == null || (!isOptionValueValid(setting, maxOptionValue)))
            KitPvP.setMultipleChoiceSetting(setting, String.valueOf(defaultValue));
    }

    public static boolean isOptionValueValid(String value, int maxOptionValues) {
        for (int i = 1; i <= maxOptionValues; i++)
            if (Objects.equals(value, String.valueOf(i))) return true;
        return false;
    }
}
