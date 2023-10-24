package GUI.Gallery.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

@UtilityClass
public class EmptyChecker {

    public boolean isStringListValid(List<String> stringList) {
        for (String s : stringList) {
            if (StringUtils.isBlank(s)) {
                return false;
            }
        }
        return true;
    }

    public boolean isObjectListValid(List<Object> objectList) {
        for (Object o : objectList) {
            if (Objects.isNull(o)) {
                return false;
            }
        }
        return true;
    }

}
