package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.service.CandidateService;
import ru.job4j.dreamjob.service.CityService;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ru.job4j.dreamjob.util.Session.setSession;

@ThreadSafe
@Controller
public class CandidateController {
    private final CityService cityService;
    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService, CityService cityService) {
        this.candidateService = candidateService;
        this.cityService = cityService;
    }

    @GetMapping("/candidates")
    public String candidates(Model model, HttpSession session) {
        setSession(model, session);
        model.addAttribute("candidates", candidateService.findAll());
        return "candidates";
    }

    @GetMapping("/formAddCandidate")
    public String addCandidate(Model model, HttpSession session) {
        setSession(model, session);
        model.addAttribute("cities", cityService.getAllCities());
        return "addCandidate";
    }

    @PostMapping("/createCandidate")
    public String createPost(@ModelAttribute Candidate candidate, @RequestParam("file") MultipartFile file) throws IOException {
        candidate.setPhoto(file.getBytes());
        candidate.setCity(cityService.findById(candidate.getCity().getId()));
        candidateService.addCandidate(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("/formUpdateCandidate/{candidateId}")
    public String formUpdateCandidate(Model model, @PathVariable("candidateId") int id, HttpSession session) {
        setSession(model, session);
        model.addAttribute("candidate", candidateService.findById(id));
        model.addAttribute("cities", cityService.getAllCities());
        return "updateCandidate";
    }

    @PostMapping("/updateCandidate")
    public String updatePost(@ModelAttribute Candidate candidate, @RequestParam("file") MultipartFile file) throws IOException {
        candidate.setPhoto(file.getBytes());
        candidate.setCity(cityService.findById(candidate.getCity().getId()));
        candidateService.update(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("/photoCandidate/{candidateId}")
    public ResponseEntity<Resource> download(@PathVariable("candidateId") Integer candidateId) {
        Candidate candidate = candidateService.findById(candidateId);
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(candidate.getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(candidate.getPhoto()));
    }
}
