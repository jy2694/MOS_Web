package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {

    private AssignmentRepository assignmentRepository;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository){
        this.assignmentRepository = assignmentRepository;
    }
}
