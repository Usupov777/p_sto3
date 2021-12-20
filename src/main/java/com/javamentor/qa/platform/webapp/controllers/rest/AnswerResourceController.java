package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@Api("get  list answerdto by questionId")
@RequestMapping("api/user/question")
public class AnswerResourceController {

    private final AnswerDtoService answerDtoService;

    @Autowired
    public AnswerResourceController(AnswerDtoService answerDtoService) {
        this.answerDtoService = answerDtoService;
    }


    @GetMapping("/{questionId}/answer")
    @ApiOperation(
            value = "Returns List of AnswerDtos corresponding questionId",
            notes = "Returns HTTP 404 if the questionId is not found")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Valid List of AnswerDtos found"),
            @ApiResponse(code = 404, message = "Answers with id not found")})
    public ResponseEntity<?> getAllAnswerByQuestionId(@PathVariable("questionId") Long id) {
        List<AnswerDto> answerDtos = answerDtoService.getAllAnswersByQuestionId(id);

        return  answerDtos.isEmpty()?
                new ResponseEntity<>("Answers with id " + id + " not found!", HttpStatus.NOT_FOUND):
                new ResponseEntity<>(answerDtos, HttpStatus.OK);
    }

}