package com.ntu.shvydkov.emerancy_bot.controller;

import com.ntu.shvydkov.emerancy_bot.domain.Report;
import com.ntu.shvydkov.emerancy_bot.domain.ReportState;
import com.ntu.shvydkov.emerancy_bot.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports-management")
public class ReportVeriificationController {
    @Autowired
    private ReportService reportService;

    @GetMapping
    public List<Report> getAllReports() {
        return reportService.findAll();
    }

    @PutMapping("/{reportId}")
    public Report setNewStatus(
            @PathVariable String reportId,
            @RequestParam String newState
    ) {
        return reportService.updateStatus(UUID.fromString(reportId), ReportState.valueOf(newState));
    }
}
