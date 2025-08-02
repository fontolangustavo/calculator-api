package com.fontolan.calculator.unit.infrastructure.mapper;

import com.fontolan.calculator.domain.enums.UserStatus;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.infrastructure.dataprovider.entity.UserEntity;
import com.fontolan.calculator.infrastructure.mapper.UserEntityMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserEntityMapperTest {

    private final UserEntityMapper mapper = new UserEntityMapper();

    @Test
    void shouldConvertEntityToDomain() {
        UserEntity entity = new UserEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setUsername("testuser");
        entity.setPassword("secret");
        entity.setStatus(UserStatus.ACTIVE);
        entity.setBalance(new BigDecimal("100.00"));

        User user = mapper.toDomain(entity);

        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertEquals(new BigDecimal("100.00"), user.getBalance());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void shouldConvertDomainToEntity() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "testuser", "secret", UserStatus.ACTIVE, new BigDecimal("50.00"));

        UserEntity entity = mapper.toEntity(user);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("testuser", entity.getUsername());
        assertEquals("secret", entity.getPassword());
        assertEquals(UserStatus.ACTIVE, entity.getStatus());
        assertEquals(new BigDecimal("50.00"), entity.getBalance());
    }

    @Test
    void shouldReturnNullWhenDomainIsNull() {
        assertNull(mapper.toEntity(null));
    }
}
