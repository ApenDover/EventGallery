package GUI.Gallery.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class EmptyChecker {

    public static boolean isStringListValid(List<String> stringList) {
        for (String s : stringList) {
            if (StringUtils.isBlank(s)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isObjectListValid(List<Object> objectList) {
        for (Object o : objectList) {
            if (Objects.isNull(o)) {
                return false;
            }
        }
        return true;
    }

}
