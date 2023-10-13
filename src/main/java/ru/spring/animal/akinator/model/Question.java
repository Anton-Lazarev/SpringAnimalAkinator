package ru.spring.animal.akinator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private int id;
    private String value;
    private int positiveAnswerID;
    private int negativeAnswerID;
}
