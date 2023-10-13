package ru.spring.animal.akinator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spring.animal.akinator.model.Question;
import ru.spring.animal.akinator.storage.QuestionStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class SpringAkinatorService {
    private final BufferedReader cmdReader;
    private final QuestionStorage storage;

    @Autowired
    public SpringAkinatorService(QuestionStorage storage) {
        this.storage = storage;
        this.cmdReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void startGame() {
        try {
            int startQuestionID = storage.getQuestionByValue("живет на суше").getId();
            System.out.println("Хотите поиграем в угадайку про животных? (да/нет)");
            String answer = cmdReader.readLine();
            while (!answer.equals("нет")) {
                System.out.println("Загадай животное, а я попробую угадать...");
                ask(storage.getQuestionByID(startQuestionID));
                System.out.println("Хотите сыграть ещё? (да/нет)");
                answer = cmdReader.readLine();
            }
            System.out.println("Спасибо, что поиграли со мной! ;)");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void ask(Question question) throws IOException {
        System.out.println("Это животное " + question.getValue() + "? (да/нет)");
        String cmdInput = cmdReader.readLine();
        if (question.getPositiveAnswerID() == 0 && question.getNegativeAnswerID() == 0) {
            if (cmdInput.equals("да")) {
                System.out.println("Ваше животное - " + question.getValue());
            } else {
                String currentAnimal = question.getValue();
                System.out.println("Какое животное вы загадали?");
                String newAnimal = cmdReader.readLine();
                System.out.println("Чем оно отличается от " + currentAnimal + "?");
                String newQuestion = cmdReader.readLine();

                int newAnimalID = storage.addQuestion(newAnimal, 0, 0);
                int newTaleAnimalID = storage.addQuestion(currentAnimal, 0, 0);
                storage.updateQuestion(question.getId(), newQuestion, newAnimalID, newTaleAnimalID);

                System.out.println("В этот раз у меня не получилось выиграть, зато я узнал что то новое");
            }
        } else {
            if (cmdInput.equals("да")) {
                ask(storage.getQuestionByID(question.getPositiveAnswerID()));
            } else {
                ask(storage.getQuestionByID(question.getNegativeAnswerID()));
            }
        }
    }
}
