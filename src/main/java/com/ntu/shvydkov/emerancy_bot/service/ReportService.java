package com.ntu.shvydkov.emerancy_bot.service;

import com.ntu.shvydkov.emerancy_bot.domain.Report;
import com.ntu.shvydkov.emerancy_bot.domain.ReportState;
import com.ntu.shvydkov.emerancy_bot.repo.ReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.InternalServerErrorException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReportService {
    @Autowired
    private ReportRepo reportRepo;

    public List<Report> findAllReportsByUserName(String username) {
        return reportRepo.findAllByUserUsername(username);
    }

    public List<Report> findAll() {
        return (List<Report>) reportRepo.findAll();
    }

    public Report updateStatus(UUID id, ReportState state) {
        Optional<Report> report = reportRepo.findById(id);
        if (report.isPresent()) {
            Report reportToUpdate = report.get();
            reportToUpdate.setReportState(state);
            return reportRepo.save(reportToUpdate);
        }
        throw new InternalServerErrorException("No report were found!");
    }
}
