package com.example.anbo.checkbooktesting.checkbookInterface;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Anbo on 10/9/2015.
 */
public interface Entry {
    UUID getUUID();

    Calendar getDate();

    double getCost();

    List<String> getTags();

    String getNote();

}
