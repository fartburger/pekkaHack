package com.fartburger.fartcheat.config;

import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import java.util.function.Consumer;

public class BooleanSetting extends SettingBase<Boolean> {

    public BooleanSetting(Boolean defaultValue, String name, String description, List<Consumer<Boolean>> onChanged) {
        super(defaultValue, name, description, onChanged);
    }

    @Override
    @SneakyThrows
    public void serialize(DataOutputStream stream) {
        stream.writeBoolean(getValue());
    }

    @Override
    public String getType() {
        return "boolean";
    }

    @Override
    @SneakyThrows
    public void deserialize(DataInputStream stream) {
        this.setValue(stream.readBoolean());
    }

    @Override
    public Boolean parse(String value) {
        return (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1"));
    }

    public static class Builder extends SettingBase.Builder<BooleanSetting.Builder, Boolean, BooleanSetting> {

        public Builder(Boolean defaultValue) {
            super(defaultValue);
        }

        @Override
        public BooleanSetting get() {
            return new BooleanSetting(defaultValue, name, description, changed);
        }
    }
}
