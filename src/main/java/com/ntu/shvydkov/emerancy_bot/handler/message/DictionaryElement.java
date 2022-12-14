package com.ntu.shvydkov.emerancy_bot.handler.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryElement {
    private String kind;
    private List<NumberAndDescription> numberAndDescriptions;
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class NumberAndDescription{
    String number;
    String description;
}
