package com.example.demo.controller;

import java.util.*;

import com.example.demo.domain.Accommodation;
import com.example.demo.domain.Booking;
import com.example.demo.repos.AccommodationRepository;
import com.example.demo.services.AccommodationService;
import com.example.demo.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller; // для управляющего класса
import org.springframework.ui.Model; //интерфейс для взаимодействия колнтроолера и конфигуратора и для добавления элементов в веб интерфейс
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView; //метод для указания html страниц которые подвязаны  с контроллером

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

@Controller
public class AppController {
    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private AccommodationService accommodationService;


    @GetMapping("/")
    public String viewHomePageAdmin(Model model, @Param("keyword") String keyword) {

        List<Accommodation> accommodation = accommodationService.listAll(keyword);

        model.addAttribute("accommodation", accommodation);
        model.addAttribute("keyword", keyword);

        return "index";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("accommodation", new Accommodation());
        return "new_booking";
    }

    @PostMapping("/save")
    public String saveAccommodation(@ModelAttribute("accommodation") Accommodation accommodation) {
        accommodationService.save(accommodation);
        return "redirect:/";
    }

    @RequestMapping("/edit/{id}") //для редактировнаия студентов
    public ModelAndView showEditAccommodationForm(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit_accommodation");
        Accommodation accommodation = accommodationService.get(id);
        mav.addObject("accommodation", accommodation);
        return mav;
    }
//
//    @DeleteMapping("/accommodation/{id}")
//    @ResponseBody
//    public String deleteAccommodation(@PathVariable Long id) {
//        accommodationService.delete(id);
//        return "Accommodation with ID " + id + " has been deleted";
//    }


    @RequestMapping("/delete/{id}") //для удаления студента
    public String deleteAccommodation(@PathVariable(name="id") Long id){
        accommodationService.delete(id);
        return "redirect:/";
    }
}
