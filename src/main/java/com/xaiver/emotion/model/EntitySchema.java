package com.xaiver.emotion.model;

import com.xaiver.emotion.enums.AccessModifierEnum;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

@Data
public class EntitySchema {

    private AccessModifierEnum accessModifier;

    private String name;

    private List<PropSchema> props;

    @Data
    public static class PropSchema {
        private AccessModifierEnum accessModifier;
        private Class javaClass;
        private String name;
    }

    public boolean isInit(){
        return Objects.nonNull(name) && Objects.nonNull(props);
    }
}
