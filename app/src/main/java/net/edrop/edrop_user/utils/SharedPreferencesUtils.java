package net.edrop.edrop_user.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtils {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences mPreference;
    private static SharedPreferences.Editor editor;
    private Context mContext;

    public SharedPreferencesUtils(Context context, String name) {
        this.mContext = context;
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferences getPreference(Context context) {
        if (mPreference == null)
            mPreference = PreferenceManager
                    .getDefaultSharedPreferences(context);
        return mPreference;
    }

    public SharedPreferences.Editor getEditor() {
        if (editor != null)
            return editor;
        else
            return null;
    }

    /**
     * 写入String类型键值对
     *
     * @param key
     * @param vlaues
     * @return
     */
    public boolean putString(String key, String vlaues) {
        editor.putString(key, vlaues);
        boolean result = editor.commit();
        return result;
    }

    /**
     * 写入long
     *
     * @param key
     * @param values
     * @return
     */
    public boolean putLong(String key, Long values) {
        editor.putLong(key, values);
        boolean result = editor.commit();
        return result;
    }

    /**
     * 写入int
     *
     * @param key
     * @param values
     * @return
     */
    public boolean putInt(String key, int values) {
        editor.putInt(key, values);
        boolean result = editor.commit();
        return result;
    }

    /**
     * 写入boolean
     *
     * @param key
     * @param values
     * @return
     */
    public boolean putBoolean(String key, boolean values) {
        editor.putBoolean(key, values);
        boolean result = editor.commit();
        return result;
    }

    /**
     * 写入float
     *
     * @param key
     * @param values
     * @return
     */
    public boolean putFloat(String key, float values) {
        editor.putFloat(key, values);
        boolean result = editor.commit();
        return result;
    }

    /**
     * 移除某个值
     *
     * @param key
     * @return
     */
    public boolean removeValues(String key) {
        editor.remove(key);
        boolean result = editor.commit();
        return result;
    }

    /**
     * 清理
     *
     * @return
     */
    public boolean clear() {
        editor.clear();
        return editor.commit();
    }

    /**
     * 获取String，默认值
     *
     * @param key
     * @param defaultValues
     * @return
     */
    public String getString(String key, String defaultValues) {
        return sharedPreferences.getString(key, defaultValues);
    }

    /**
     * 获取long
     *
     * @param key
     * @return
     */
    public long getLoing(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    /**
     * 获取int值
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    /**
     * 获取boolean
     *
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * 获取float值
     *
     * @param key
     * @return
     */
    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

    public static void setInteger(Context context, String name, int value) {
        getPreference(context).edit().putInt(name, value).commit();
    }
    public static int getInteger(Context context, String name, int default_i) {
        return getPreference(context).getInt(name, default_i);
    }

    /**
     * 设置字符串类型的配置
     */
    public static void setString(Context context, String name, String value) {
        getPreference(context).edit().putString(name, value).commit();
    }

    public static String getString(Context context, String name) {
        return getPreference(context).getString(name, null);
    }

    /**
     * 获取字符串类型的配置
     */
    public static String getString(Context context, String name, String defalt) {
        return getPreference(context).getString(name, defalt);
    }

    /**
     * 获取boolean类型的配置
     *
     * @param context
     * @param name
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Context context, String name,
                                     boolean defaultValue) {
        return getPreference(context).getBoolean(name, defaultValue);
    }

    /**
     * 设置boolean类型的配置
     *
     * @param context
     * @param name
     * @param value
     */
    public static void setBoolean(Context context, String name, boolean value) {
        getPreference(context).edit().putBoolean(name, value).commit();
    }

    public static void setFloat(Context context, String name, Float value) {
        getPreference(context).edit().putFloat(name, value).commit();
    }

    public static Float getFloat(Context context, String name, Float value) {
        return getPreference(context).getFloat(name, 0);
    }

    public static void setLong(Context context, String name, Long value) {
        getPreference(context).edit().putLong(name, value).commit();
    }

    public static Long getLong(Context context, String name, Long defaultValue) {
        return getPreference(context).getLong(name, defaultValue);
    }
}
