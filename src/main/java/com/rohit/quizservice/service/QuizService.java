package com.rohit.quizservice.service;



import com.rohit.quizservice.dao.QuizDao;
import com.rohit.quizservice.feign.QuizInterface;
import com.rohit.quizservice.model.QuestionWrapper;
import com.rohit.quizservice.model.Quiz;
import com.rohit.quizservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizService {
    @Autowired

    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Integer> questions=quizInterface.getQuestionsForQuiz(category,numQ).getBody();

        Quiz quiz=new Quiz();

        quiz.setTitle(title);
        quiz.setQuestionIds(questions);

        quizDao.save(quiz);


       return new ResponseEntity<>("Success", HttpStatus.OK);

    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(int id) {
       Quiz quiz =quizDao.findById(id).get();

       List<Integer> questionIds=quiz.getQuestionIds();
       ResponseEntity<List<QuestionWrapper>> questions=quizInterface.getQuestionsFromId(questionIds);



       return questions;
    }


    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {

        ResponseEntity<Integer> score=quizInterface.getScore(responses);

        return score;
    }
}
