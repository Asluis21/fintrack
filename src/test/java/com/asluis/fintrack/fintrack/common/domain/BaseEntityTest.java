package com.asluis.fintrack.fintrack.common.domain;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BaseEntityTest {

    static class SampleEntity extends BaseEntity {
    }

    static class OtherEntity extends BaseEntity {
    }

    private static void setId(BaseEntity entity, Long id) {
        try {
            Field field = BaseEntity.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void isEqualToItself() {
        SampleEntity entity = new SampleEntity();

        assertThat(entity).isEqualTo(entity);
    }

    @Test
    void sameClassAndSameId_areEqual() {
        SampleEntity a = new SampleEntity();
        SampleEntity b = new SampleEntity();
        setId(a, 1L);
        setId(b, 1L);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void sameClassButDifferentId_areNotEqual() {
        SampleEntity a = new SampleEntity();
        SampleEntity b = new SampleEntity();
        setId(a, 1L);
        setId(b, 2L);

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void differentEffectiveClass_areNotEqual() {
        SampleEntity a = new SampleEntity();
        OtherEntity b = new OtherEntity();
        setId(a, 1L);
        setId(b, 1L);

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void transientEntities_areNeverEqual() {
        SampleEntity a = new SampleEntity();
        SampleEntity b = new SampleEntity();

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void neverEqualToNullOrDifferentType() {
        SampleEntity a = new SampleEntity();
        setId(a, 1L);

        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("not an entity");
    }
}
