package com.ntu.shvydkov.emerancy_bot.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportEnvelop {
        private List<ReportEnvelopItem> items;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ReportEnvelopItem {
        private String userId;
        private Double lat;
        private Double lon;
        private String dangerLevel;
        private String localDateString;
}