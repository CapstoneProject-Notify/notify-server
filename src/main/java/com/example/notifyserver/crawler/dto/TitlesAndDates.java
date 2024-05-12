package com.example.notifyserver.crawler.dto;

import java.util.Date;
import java.util.List;

public record TitlesAndDates(List<String> titles, List<Date> dates) {
}
