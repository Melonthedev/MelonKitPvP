package wtf.melonthedev.melonkitpvp;

import java.util.Objects;

public class KitPvpSettings {

    private KitPvpSettings() {} //Utility class - das braucht man nicht zu instanziieren weil die methoden static sind und kein objekt sein sollten

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
            KitPvP.setMultipleChoiceSetting(setting, String.valueOf(defaultValue)); //Gutes Beispiel fürs Integer zu String converten mit String.valueOf()
    }

    public static boolean isOptionValueValid(String value, int maxOptionValues) { // IK das kann man besser machen
        for (int i = 1; i <= maxOptionValues; i++)
            if (Objects.equals(value, String.valueOf(i))) return true;
        return false;
    }
}
