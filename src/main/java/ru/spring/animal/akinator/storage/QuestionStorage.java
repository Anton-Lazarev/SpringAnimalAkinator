package ru.spring.animal.akinator.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.spring.animal.akinator.exceptions.QuestionNotFoundException;
import ru.spring.animal.akinator.model.Question;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Component
public class QuestionStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public QuestionStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        startFillingDB();
    }

    public int addQuestion(String question, int positiveAnswerID, int negativeAnswerID) {
        String insertQuery = "INSERT INTO questions (question, positive_answer_id, negative_answer_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statements = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            statements.setString(1, question);
            statements.setInt(2, positiveAnswerID);
            statements.setInt(3, negativeAnswerID);
            return statements;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void updateQuestion(int questionID,String question, int positiveAnswerID, int negativeAnswerID) {
        String sql = "UPDATE questions SET question = ?, positive_answer_id = ?, negative_answer_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, question, positiveAnswerID, negativeAnswerID, questionID);
    }

    public Question getQuestionByID(int id) {
        String sql = "SELECT * FROM questions WHERE id = ?";
        SqlRowSet questionRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (questionRowSet.next()) {
            return Question.builder()
                    .id(questionRowSet.getInt("id"))
                    .value(questionRowSet.getString("question"))
                    .positiveAnswerID(questionRowSet.getInt("positive_answer_id"))
                    .negativeAnswerID(questionRowSet.getInt("negative_answer_id"))
                    .build();
        } else {
            throw new QuestionNotFoundException("Question with ID " + id + " not presented in DB");
        }
    }

    public Question getQuestionByValue(String value) {
        String sql = "SELECT * FROM questions WHERE question = ?";
        SqlRowSet questionRowSet = jdbcTemplate.queryForRowSet(sql, value);
        if (questionRowSet.next()) {
            return Question.builder()
                    .id(questionRowSet.getInt("id"))
                    .value(questionRowSet.getString("question"))
                    .positiveAnswerID(questionRowSet.getInt("positive_answer_id"))
                    .negativeAnswerID(questionRowSet.getInt("negative_answer_id"))
                    .build();
        } else {
            throw new QuestionNotFoundException("Question '" + value + "' not presented in DB");
        }
    }

    private void startFillingDB() {
        String selectStartQuestion = "SELECT * FROM questions WHERE question = 'живет на суше'";
        SqlRowSet questionRow = jdbcTemplate.queryForRowSet(selectStartQuestion);
        if (!questionRow.next()) {
            addQuestion("кот", 0, 0);
            addQuestion("кит", 0, 0);
            addQuestion("живет на суше", 1, 2);
        }
    }
}
