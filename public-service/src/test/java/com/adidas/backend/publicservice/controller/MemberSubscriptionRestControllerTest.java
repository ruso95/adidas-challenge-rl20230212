package com.adidas.backend.publicservice.controller;

import com.adidas.backend.publicservice.dto.external.SubscriptionRequestDTO;
import com.adidas.backend.publicservice.service.external.MembersService;
import com.adidas.backend.publicservice.util.exceptions.BadConfigException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = MemberSubscriptionRestController.class)
@Execution(ExecutionMode.SAME_THREAD)
class MemberSubscriptionRestControllerTest {

    @MockBean
    private static MembersService membersService;

    @Autowired
    private MemberSubscriptionRestController controller;

    private final String stringRequest = "email@test.es";
    private final SubscriptionRequestDTO dtoRequest = SubscriptionRequestDTO.builder().email("email@test.es").build();

    @Test
    void newSubscription_whenAllOk_thenOk() {
        // given
        String expectedBodyMsg = "All is OK";
        ResponseEntity<String> expectedResult = ResponseEntity.ok().body(expectedBodyMsg);

        // when
        when(membersService.newSubscription(any(SubscriptionRequestDTO.class))).thenReturn(expectedResult);

        // then
        ResponseEntity<String> actual = controller.newSubscription(stringRequest);

        // asserts
        assertEquals(expectedResult, actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expectedBodyMsg, actual.getBody());
    }

    @Test
    void newSubscription_whenResponseNull_thenServiceUnavailable() {
        when(membersService.newSubscription(any(SubscriptionRequestDTO.class))).thenReturn(null);

        ResponseEntity<String> actual = controller.newSubscription(stringRequest);

        assertNotNull(actual);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, actual.getStatusCode());
        assertNotNull(actual.getBody());
    }

    @Test
    void newSubscription_whenBadConfigException_thenInternalServiceError() {
        when(membersService.newSubscription(any(SubscriptionRequestDTO.class))).thenThrow(new BadConfigException("Bad configuration"));

        ResponseEntity<String> actual = controller.newSubscription(stringRequest);

        assertNotNull(actual);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        assertNotNull(actual.getBody());
    }


    @Test
    void newSubscriptionWithDTO_whenAllOk_thenOk() {
        // given
        String expectedBodyMsg = "All is OK";
        ResponseEntity<String> expectedResult = ResponseEntity.ok().body(expectedBodyMsg);

        // when
        when(membersService.newSubscription(any(SubscriptionRequestDTO.class))).thenReturn(expectedResult);

        // then
        ResponseEntity<String> actual = controller.newSubscription(dtoRequest);

        // asserts
        assertEquals(expectedResult, actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expectedBodyMsg, actual.getBody());
    }

    @Test
    void newSubscriptionWithDTO_whenResponseNull_thenServiceUnavailable() {
        when(membersService.newSubscription(any(SubscriptionRequestDTO.class))).thenReturn(null);

        ResponseEntity<String> actual = controller.newSubscription(dtoRequest);

        assertNotNull(actual);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, actual.getStatusCode());
        assertNotNull(actual.getBody());
    }

    @Test
    void newSubscriptionWithDTO_whenBadConfigException_thenInternalServiceError() {
        when(membersService.newSubscription(any(SubscriptionRequestDTO.class))).thenThrow(new BadConfigException("Bad configuration"));

        ResponseEntity<String> actual = controller.newSubscription(dtoRequest);

        assertNotNull(actual);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        assertNotNull(actual.getBody());
    }

    @Test
    void newSubscription_whenNull_thenStringAndDTOEqualResponse() {
        when(membersService.newSubscription(any(SubscriptionRequestDTO.class))).thenReturn(null);

        ResponseEntity<String> actualDto = controller.newSubscription(dtoRequest);
        ResponseEntity<String> actualString = controller.newSubscription(stringRequest);

        assertEquals(actualDto, actualString);
    }

    @Test
    void newSubscription_whenOk_thenStringAndDTOEqualResponse() {
        String expectedBodyMsg = "All is OK";
        ResponseEntity<String> expectedResult = ResponseEntity.ok().body(expectedBodyMsg);
        when(membersService.newSubscription(any(SubscriptionRequestDTO.class))).thenReturn(expectedResult);

        ResponseEntity<String> actualDto = controller.newSubscription(dtoRequest);
        ResponseEntity<String> actualString = controller.newSubscription(stringRequest);

        // response is equals
        assertEquals(actualDto, actualString);
        assertEquals(actualDto.getStatusCode(), actualString.getStatusCode());
        assertEquals(actualDto.getBody(), actualString.getBody());

        // response is expected
        assertEquals(expectedResult, actualDto);
        assertEquals(HttpStatus.OK, actualDto.getStatusCode());
        assertEquals(expectedBodyMsg, actualDto.getBody());
        assertEquals(expectedResult, actualString );
        assertEquals(HttpStatus.OK, actualString.getStatusCode());
        assertEquals(expectedBodyMsg, actualString.getBody());

    }

    @Test
    void fallbackSubscriptionMethod_whenCalled_thenServiceUnavailable() {
        ResponseEntity<String> actual = controller.fallbackSubscriptionMethod(new Exception("This is a test"));

        assertNotNull(actual);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, actual.getStatusCode());
        assertNotNull(actual.getBody());
    }
}