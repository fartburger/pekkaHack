package com.fartburger.fartcheat.config;

import com.fartburger.fartcheat.util.Utils;
import com.fartburger.fartcheat.config.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.lang.reflect.Field;


@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public enum SettingType {
    DOUBLE(double.class, (setting, inputField, defaultValue) -> {
        Utils.throwIfAnyEquals("Min, max and precision need to be defined", -1, setting.min(), setting.max(), setting.precision());
        return new DoubleSetting.Builder(inputField.getDouble(defaultValue)).min(setting.min()).max(setting.max()).precision(setting.precision());
    }),
    INT(int.class, (setting, inputField, defaultValue) -> {
        Utils.throwIfAnyEquals("Min and max need to be defined", -1, setting.min(), setting.max());
        return new DoubleSetting.Builder(inputField.getDouble(defaultValue)).min(setting.min()).max(setting.max()).precision(0);
    }),
    BOOLEAN(boolean.class, (setting, inputField, defaultValue) -> new BooleanSetting.Builder(inputField.getBoolean(defaultValue))),
    RANGE(RangeSetting.Range.class, (setting, inputField, declaringClass) -> {
        Utils.throwIfAnyEquals("Min, max and precision need to be defined", -1, setting.min(), setting.max(), setting.precision());
        double minA = setting.min();
        double maxA = setting.max();
        double minB = setting.upperMin();
        double maxB = setting.upperMax();
        if (minB == -1) {
            minB = minA;
        }
        if (maxB == -1) {
            maxB = maxA;
        }
        return new RangeSetting.Builder((RangeSetting.Range) inputField.get(declaringClass)).lowerMin(minA)
                .lowerMax(maxA)
                .upperMin(minB)
                .upperMax(maxB)
                .precision(setting.precision());
    });
    @Getter
    final Class<?> acceptedType;
    @Getter
    final SettingProvider<?> provider;

    interface SettingProvider<T extends SettingBase.Builder<?, ?, ?>> {
        default T getExtern(Setting setting, Field inputField, Object declaringClass) throws Exception {
            return get(setting, inputField, declaringClass);
        }

        T get(Setting setting, Field inputField, Object declaringClass) throws Exception;
    }
}
