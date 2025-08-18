package com.fontolan.calculator.unit.infrastructure.external.randomorg;

import com.fontolan.calculator.infrastructure.external.randomorg.RandomOrgFeign;
import com.fontolan.calculator.infrastructure.external.randomorg.RandomOrgRandomStringAdapter;
import com.fontolan.calculator.infrastructure.external.randomorg.exception.RandomOrgException;
import com.fontolan.calculator.infrastructure.external.randomorg.request.RandomOrgRequest;
import com.fontolan.calculator.infrastructure.external.randomorg.response.RandomOrgResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RandomOrgRandomStringAdapterTest {

    private RandomOrgFeign feign;
    private RandomOrgRandomStringAdapter adapter;

    @BeforeEach
    void setUp() {
        feign = mock(RandomOrgFeign.class);
        adapter = new RandomOrgRandomStringAdapter(feign, "MY-KEY");
    }

    @Test
    void shouldBuildRequestAndReturnFirstString() throws Exception {
        var response = mock(RandomOrgResponse.class, RETURNS_DEEP_STUBS);
        when(response.getError()).thenReturn(null);
        when(response.getResult().getRandom().getData()).thenReturn(List.of("abcDEF123"));
        when(feign.invoke(any())).thenReturn(response);

        String out = adapter.getRandomString(12);

        assertThat(out).isEqualTo("abcDEF123");

        ArgumentCaptor<RandomOrgRequest> captor = ArgumentCaptor.forClass(RandomOrgRequest.class);
        verify(feign).invoke(captor.capture());
        RandomOrgRequest req = captor.getValue();

        assertThat(readField(req, "method")).isEqualTo("generateStrings");
        assertThat(readField(req, "id")).isEqualTo(1);

        Object params = readField(req, "params");
        assertThat(readField(params, "apiKey")).isEqualTo("MY-KEY");
        assertThat(readField(params, "n")).isEqualTo(1);
        assertThat(readField(params, "length")).isEqualTo(12);
        assertThat(readField(params, "characters")).isEqualTo("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertThat(readField(params, "replacement")).isEqualTo(true);
    }

    @Test
    void shouldThrowRandomOrgExceptionWhenErrorPresent() {
        var response = mock(RandomOrgResponse.class, RETURNS_DEEP_STUBS);
        when(response.getError().getCode()).thenReturn(42);
        when(response.getError().getMessage()).thenReturn("Rate limit");
        when(feign.invoke(any())).thenReturn(response);

        assertThatThrownBy(() -> adapter.getRandomString(8))
                .isInstanceOf(RandomOrgException.class)
                .hasMessageContaining("Rate limit");
    }

    @Test
    void shouldThrowWhenResultIsNull() {
        var response = mock(RandomOrgResponse.class);
        when(response.getError()).thenReturn(null);
        when(response.getResult()).thenReturn(null);
        when(feign.invoke(any())).thenReturn(response);

        assertThatThrownBy(() -> adapter.getRandomString(8))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid response from Random.org");
    }

    @Test
    void shouldThrowWhenRandomIsNull() {
        var response = mock(RandomOrgResponse.class, RETURNS_DEEP_STUBS);
        when(response.getError()).thenReturn(null);
        when(response.getResult().getRandom()).thenReturn(null);
        when(feign.invoke(any())).thenReturn(response);

        assertThatThrownBy(() -> adapter.getRandomString(8))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid response from Random.org");
    }

    @Test
    void shouldThrowWhenDataIsNull() {
        var response = mock(RandomOrgResponse.class, RETURNS_DEEP_STUBS);
        when(response.getError()).thenReturn(null);
        when(response.getResult().getRandom().getData()).thenReturn(null);
        when(feign.invoke(any())).thenReturn(response);

        assertThatThrownBy(() -> adapter.getRandomString(8))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid response from Random.org");
    }

    @Test
    void shouldThrowWhenDataIsEmpty() {
        var response = mock(RandomOrgResponse.class, RETURNS_DEEP_STUBS);
        when(response.getError()).thenReturn(null);
        when(response.getResult().getRandom().getData()).thenReturn(List.of());
        when(feign.invoke(any())).thenReturn(response);

        assertThatThrownBy(() -> adapter.getRandomString(8))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("did not return any strings");
    }

    private static Object readField(Object target, String name) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.get(target);
    }
}
