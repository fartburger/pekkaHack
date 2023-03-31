package com.fartburger.fartcheat.config;

import java.util.List;
import java.util.function.Consumer;

public class StringSetting extends SettingBase<String> {

    public StringSetting(String defaultValue, String name, String description, List<Consumer<String>> onChange) {
        super(defaultValue, name, description, onChange);
    }

    @Override
    public String parse(String value) {
        return value;
    }

    @Override
    public String getType() {
        return "string";
    }

    public static class Builder extends SettingBase.Builder<StringSetting.Builder, String, StringSetting> {

        public Builder(String defaultValue) {
            super(defaultValue);
        }

        @Override
        public StringSetting get() {
            return new StringSetting(defaultValue, name, description, changed);
        }
    }
}
