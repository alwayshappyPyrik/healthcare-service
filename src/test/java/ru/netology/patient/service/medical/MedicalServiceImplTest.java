package ru.netology.patient.service.medical;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;


class MedicalServiceImplTest {

    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(output));
    }

    @Test
    void test_check_out_message_if_blood_pressure_not_normal() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById("095d8e19-3754-46aa-96ab-139135e9dc2f"))
                .thenReturn(new PatientInfo("095d8e19-3754-46aa-96ab-139135e9dc2f", "Иван", "Петров", LocalDate.of(1980, 11, 26), new HealthInfo(new BigDecimal(36.65), new BloodPressure(120, 80))));

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure("095d8e19-3754-46aa-96ab-139135e9dc2f", new BloodPressure(200, 120));
        String message = String.format("Warning, patient with id: 095d8e19-3754-46aa-96ab-139135e9dc2f, need help");
        Mockito.verify(sendAlertService, Mockito.only()).send(message);
        Mockito.verify(sendAlertService).send(argumentCaptor.capture());

        Assertions.assertEquals("Warning, patient with id: 095d8e19-3754-46aa-96ab-139135e9dc2f, need help", argumentCaptor.getValue());
        Assertions.assertEquals("Warning, patient with id: 095d8e19-3754-46aa-96ab-139135e9dc2f, need help", output.toString());
    }

    @Test
    void test_check_out_message_if_temperature_not_normal() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById("095d8e19-3754-46aa-96ab-139135e9dc2f"))
                .thenReturn(new PatientInfo("095d8e19-3754-46aa-96ab-139135e9dc2f", "Иван", "Петров", LocalDate.of(1980, 11, 26), new HealthInfo(new BigDecimal(43.0), new BloodPressure(120, 80))));

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        BigDecimal currentTemperature = new BigDecimal(40.0);
        medicalService.checkTemperature("095d8e19-3754-46aa-96ab-139135e9dc2f", currentTemperature);
        String message = String.format("Warning, patient with id: 095d8e19-3754-46aa-96ab-139135e9dc2f, need help");
        Mockito.verify(sendAlertService, Mockito.only()).send(message);
        Mockito.verify(sendAlertService).send(argumentCaptor.capture());

        Assertions.assertEquals("Warning, patient with id: 095d8e19-3754-46aa-96ab-139135e9dc2f, need help", argumentCaptor.getValue());
        Assertions.assertEquals("Warning, patient with id: 095d8e19-3754-46aa-96ab-139135e9dc2f, need help", output.toString());
    }

    @Test
    void test_not_message_if_health_normal() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById("095d8e19-3754-46aa-96ab-139135e9dc2f"))
                .thenReturn(new PatientInfo("095d8e19-3754-46aa-96ab-139135e9dc2f", "Иван", "Петров", LocalDate.of(1980, 11, 26), new HealthInfo(new BigDecimal(36.6), new BloodPressure(120, 60))));

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure("095d8e19-3754-46aa-96ab-139135e9dc2f", new BloodPressure(120, 60));
        BigDecimal currentTemperature = new BigDecimal(36.6);
        medicalService.checkTemperature("095d8e19-3754-46aa-96ab-139135e9dc2f", currentTemperature);

        String message = String.format("Warning, patient with id: 095d8e19-3754-46aa-96ab-139135e9dc2f, need help");
        Mockito.verify(sendAlertService, Mockito.never()).send(message);

        Assertions.assertEquals("", output.toString());
    }

    @AfterEach
    public void cleanUpStreams() {
        System.setOut(null);
    }

}