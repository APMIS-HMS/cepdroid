package ng.apmis.apmismobile.utilities;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import ng.apmis.apmismobile.annotations.Exclude;

public class AnnotationExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}