package com.WarehouseAPI.WarehouseAPI.service.interfaces;

import com.WarehouseAPI.WarehouseAPI.repository.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public interface IStatisticService {
    public Map<String, Integer> getGroupedImportStatistics(LocalDateTime startDate, LocalDateTime endDate, ChronoUnit unit);
    public String formatDateByUnit(LocalDateTime date, ChronoUnit unit);
}
