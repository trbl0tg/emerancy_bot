package com.ntu.shvydkov.emerancy_bot.service;

import com.ntu.shvydkov.emerancy_bot.domain.Report;
import com.ntu.shvydkov.emerancy_bot.repo.ReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ReportRepo reportRepo;

    public List<Report> findAllReportsByUserName(String username) {
        return reportRepo.findAllByUserUsername(username);
    }
}
