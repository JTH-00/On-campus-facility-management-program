package com.example.demo.answer;

import com.example.demo.question.QuestionDto;
import com.example.demo.question.QuestionService;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
        QuestionDto questionDto = this.questionService.getQuestion(id);
        UserDTO userDTO = this.userService.getUserDto(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", questionDto);
            return "freeboard_detail";
        }
        AnswerDto answerDto = this.answerService.create(questionDto,
                answerForm.getContent(), userDTO);
        return String.format("redirect:/question/freeboard/detail/%s#answer_%s",
                answerDto.getQuestion().getId(), answerDto.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal,Model model) {
        AnswerDto answerDto = this.answerService.getAnswer(id);
        UserDTO userDto = this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)
        model.addAttribute("roleid", userDto.getRole().getRoleid());
        if (!answerDto.getAuthor().getUserid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerForm.setContent(answerDto.getContent());
        return "answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }
        AnswerDto answerDto = this.answerService.getAnswer(id);
        if (!answerDto.getAuthor().getUserid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(answerDto, answerForm.getContent());
        return String.format("redirect:/question/freeboard/detail/%s#answer_%s",
                answerDto.getQuestion().getId(), answerDto.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id,Model model) {
        UserDTO userDto = this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)
        model.addAttribute("roleid", userDto.getRole().getRoleid());
        AnswerDto answerDto = this.answerService.getAnswer(id);
        if (!answerDto.getAuthor().getUserid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answerService.delete(answerDto);
        return String.format("redirect:/question/freeboard/detail/%s", answerDto.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id,Model model) {
        AnswerDto answerDto = this.answerService.getAnswer(id);
        UserDTO userDTO = this.userService.getUserDto(principal.getName());
        this.answerService.vote(answerDto, userDTO);
        return String.format("redirect:/question/freeboard/detail/%s#answer_%s",
                answerDto.getQuestion().getId(), answerDto.getId());
    }
}