package ru.netology.patient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

import ru.netology.patient.entity.*;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

public class Main {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        File repoFile = new File("patients.txt");
        PatientInfoRepository patientInfoRepository = new PatientInfoFileRepository(repoFile, mapper);

//        String id1 = patientInfoRepository.add(
//            new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
//                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 60)))
//        );
//
//        String id2 = patientInfoRepository.add(
//            new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
//                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)))
//        );

        SendAlertService alertService = new SendAlertServiceImpl();
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);

        //run service
        BloodPressure currentPressure = new BloodPressure(120, 60);
        medicalService.checkBloodPressure("095d8e19-3754-46aa-96ab-139135e9dc2f", currentPressure);

        BigDecimal currentTemperature = new BigDecimal("39.9");
        medicalService.checkTemperature("095d8e19-3754-46aa-96ab-139135e9dc2f", currentTemperature);
    }
}
