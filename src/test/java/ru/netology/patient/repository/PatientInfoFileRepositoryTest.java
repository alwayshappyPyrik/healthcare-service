package ru.netology.patient.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatientInfoFileRepositoryTest {

    private PatientInfoFileRepository patientInfoFileRepository;
    private ObjectMapper mapper;
    private File repoFile;

    @BeforeAll
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        repoFile = new File("patients.txt");
        patientInfoFileRepository = new PatientInfoFileRepository(repoFile, mapper);
    }

    @Test
    void test_get_patient_by_id() {
        BigDecimal temperature = new BigDecimal(36.65);
        temperature = temperature.setScale(2, BigDecimal.ROUND_UP);
        PatientInfo patientInfoExpected = new PatientInfo("095d8e19-3754-46aa-96ab-139135e9dc2f", "Иван", "Петров", LocalDate.of(1980, 11, 26), new HealthInfo(temperature, new BloodPressure(120, 60)));

        String idPatient = "095d8e19-3754-46aa-96ab-139135e9dc2f";
        PatientInfo patientInfoActual = patientInfoFileRepository.getById(idPatient);

        Assertions.assertEquals(patientInfoExpected, patientInfoActual);
    }

    @Test
    void test_add_patient_in_file() {
        BigDecimal temperature = new BigDecimal(36.64999999999999857891452847979962825775146484375);

        Throwable patientIdActual = assertThrows(RuntimeException.class, () -> patientInfoFileRepository.add(new PatientInfo("095d8e19-3754-46aa-96ab-139135e9dc2f", "Иван", "Петров", LocalDate.of(1980, 11, 26), new HealthInfo(temperature, new BloodPressure(120, 60)))));

        Assertions.assertEquals("Patient already exists", patientIdActual.getMessage());
    }

    @Test
    void test_remove_patient_by_him_id() {
        String idPatient = "095d8e19-3754-46aa-96ab-139135e9dc2f";

        Throwable patientRemove = assertThrows(RuntimeException.class, () -> patientInfoFileRepository.remove(idPatient));

        Assertions.assertEquals("Not implemented", patientRemove.getMessage());
    }

    @Test
    void test_update_data_patint_info() {
        PatientInfo patientInfo = new PatientInfo("095d8e19-3754-46aa-96ab-139135e9dc2f", "Иван", "Петров", LocalDate.of(1980, 11, 26), new HealthInfo(new BigDecimal(36.65), new BloodPressure(120, 60)));

        Throwable patientUpdate = assertThrows(RuntimeException.class, () -> patientInfoFileRepository.update(patientInfo));

        Assertions.assertEquals("Not implemented", patientUpdate.getMessage());
    }
}