package com.example.demo.question;


import com.example.demo.DataNotFoundException;
import com.example.demo.answer.Answer;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.entity.User;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ModelMapper modelMapper;


    private Specification<Question> search(String kw) {
        return new Specification<Question>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, User> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, User> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    private QuestionDto of(Question question) {
        return modelMapper.map(question, QuestionDto.class);
    }

    private Question of(QuestionDto questionDto) {
        return modelMapper.map(questionDto, Question.class);
    }

    public Page<QuestionDto> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        Page<Question> questionList = this.questionRepository.findAll(spec, pageable);
        Page<QuestionDto> questionDtoList = questionList.map(q -> of(q));
        return questionDtoList;
    }

    public Page<QuestionDto> getListMain(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        Page<Question> questionList = this.questionRepository.findAll(spec, pageable);
        Page<QuestionDto> questionDtoList = questionList.map(q -> of(q));
        return questionDtoList;
    }

    public QuestionDto getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return of(question.get());
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public QuestionDto create(String subject, String content, UserDTO userDTO) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setSubject(subject);
        questionDto.setContent(content);
        questionDto.setCreateDate(LocalDateTime.now());
        questionDto.setAuthor(userDTO);
        Question question = of(questionDto);
        this.questionRepository.save(question);
        return questionDto;
    }

    public QuestionDto modify(QuestionDto questionDto, String subject, String content) {
        questionDto.setSubject(subject);
        questionDto.setContent(content);
        questionDto.setModifyDate(LocalDateTime.now());
        Question question = of(questionDto);
        this.questionRepository.save(question);
        return questionDto;
    }

    public void delete(QuestionDto questionDto) {
        this.questionRepository.delete(of(questionDto));
    }

    public QuestionDto vote(QuestionDto questionDto, UserDTO userDTO) {
        questionDto.getVoter().add(userDTO);
        this.questionRepository.save(of(questionDto));
        return questionDto;
    }

    public long getQuestionCount() {
        return questionRepository.count();
    }
}