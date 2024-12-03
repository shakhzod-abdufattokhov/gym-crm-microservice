package com.gym.crm.service.client;

import com.gym.crm.model.dto.request.TrainerWorkload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "trainer-hours-service",
        url = "http://localhost:8222/api/v1/workload"
)
public interface WorkloadClient {

    @PostMapping
    ResponseEntity<?> processWorkload(@RequestBody TrainerWorkload workload, @RequestHeader("Authorization") String authorization);

}
