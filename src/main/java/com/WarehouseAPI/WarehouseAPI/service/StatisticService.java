package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.dto.ImportStatistic;
import com.WarehouseAPI.WarehouseAPI.repository.StatisticRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticService implements IStatisticService {


    @Autowired
    private StatisticRepository statisticRepository;

    @Override
    public Map<String, Integer> getGroupedImportStatistics(LocalDateTime startDate, LocalDateTime endDate, ChronoUnit unit) {
        List<ImportStatistic> statistics = statisticRepository.findImportStatistics(startDate, endDate);

        return statistics.stream()
                .collect(Collectors.groupingBy(
                        stat -> formatDateByUnit(stat.getImportDate(), unit),
                        Collectors.summingInt(ImportStatistic::getTotalProducts)
                ));
    }

    @Override
    public String formatDateByUnit(LocalDateTime date, ChronoUnit unit) {
        switch (unit) {
            case DAYS:
                return date.toLocalDate().toString();
            case WEEKS:
                return date.getYear() + "-W" + date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
            case MONTHS:
                return date.getYear() + "-" + date.getMonthValue();
            case YEARS:
                return String.valueOf(date.getYear());
            default:
                throw new IllegalArgumentException("Unsupported unit: " + unit);
        }
    }
}
