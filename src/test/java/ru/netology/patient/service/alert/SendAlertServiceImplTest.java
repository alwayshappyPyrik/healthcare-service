package ru.netology.patient.service.alert;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SendAlertServiceImplTest {

    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeAll
    void setUpStreams() {
        System.setOut(new PrintStream(output));
    }

    @Test
    void send() {
        SendAlertServiceImpl sendAlertService = new SendAlertServiceImpl();
        String message = String.format("Warning, patient with id: 095d8e19-3754-46aa-96ab-139135e9dc2f, need help");
        sendAlertService.send(message);

        Assertions.assertEquals("Warning, patient with id: 095d8e19-3754-46aa-96ab-139135e9dc2f, need help\r\n", output.toString());
    }

    @AfterAll
    void cleanUpStreams() {
        System.setOut(null);
    }
}