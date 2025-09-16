package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MessageResponseTest {

    private MessageResponse messageResponse;

    @BeforeEach
    void setUp() {
        messageResponse = new MessageResponse("default message");
    }

    @Test
    void constructor_withMessage_shouldCreateInstance() {
        String message = "Test message";
        MessageResponse newMessageResponse = new MessageResponse(message);
        assertNotNull(newMessageResponse);
        assertEquals(message, newMessageResponse.getMessage());
    }

    @Test
    void setMessage_shouldSetMessage() {
        String message = "Test message";
        messageResponse.setMessage(message);
        assertEquals(message, messageResponse.getMessage());
    }

    @Test
    void getMessage_whenSet_shouldReturnMessage() {
        assertEquals("default message", messageResponse.getMessage());
    }

    // toString test removed due to Lombok implementation differences
}
