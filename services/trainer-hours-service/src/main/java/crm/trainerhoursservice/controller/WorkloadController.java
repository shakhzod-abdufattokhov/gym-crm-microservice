package crm.trainerhoursservice.controller;

import crm.trainerhoursservice.model.TrainerWorkload;
import crm.trainerhoursservice.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workload")
@RequiredArgsConstructor
public class WorkloadController {

    private final WorkloadService service;

    @PostMapping
    public ResponseEntity<?> processWorkload(@RequestBody TrainerWorkload workload, @RequestHeader("Authorization") String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Authorization header");
        }
        service.handleWorkload(workload);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/summary/{trainerUsername}")
    public ResponseEntity<?> summary(@PathVariable String trainerUsername, @RequestHeader("Authorization") String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Authorization header");
        }
        return ResponseEntity.ok(service.summarize(trainerUsername));
    }

}
