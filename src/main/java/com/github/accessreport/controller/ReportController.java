package com.github.accessreport.controller;

import com.github.accessreport.model.*;
import com.github.accessreport.service.ReportService;

import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    // REST endpoint to generate the access report
    @GetMapping("/access-report")
    public ApiResponse<AccessReport> getAccessReport() throws Exception {

        AccessReport report = reportService.generateReport();

        return new ApiResponse<>(
                "success",
                Instant.now().toString(),
                report
        );
    }

}