package com.example.demo.user.controller;
import com.example.demo.place.dto.PlaceDto;
import com.example.demo.place.entity.Place;
import com.example.demo.place.service.PlaceService;
import com.example.demo.question.QuestionRepository;
import com.example.demo.rental.dto.RentalDto;
import com.example.demo.rental.entity.Rental;
import com.example.demo.rental.entity.RentalCreateForm;
import com.example.demo.rental.repository.RentalRepository;
import com.example.demo.rental.service.RentalService;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserCreateForm;
import com.example.demo.user.entity.UserUpdateForm;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PlaceService placeService;
    private final RentalService rentalService;
    private final UserRepository userRepository;

    private final QuestionRepository questionRepository;

    private final RentalRepository rentalrepository;

    @GetMapping("/login")
    public String loginForm(){return "index.html";}

    @GetMapping("/join")
    public String join(UserCreateForm userCreateForm){return "join.html";}

    @PostMapping("/joining")
    public String join(@Valid UserCreateForm userCreateForm,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "join.html";
        }
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "join.html";
        }
        try{
            userService.userdb(userCreateForm.getUsername(),userCreateForm.getUserid(),userCreateForm.getPassword1(),userCreateForm.getPhone(),userCreateForm.getEmail());
        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "join.html";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "join.html";
        }
        return "redirect:/";
    }

    @GetMapping("/update")
    public String userudpateForm(UserUpdateForm userUpdateForm, Principal principal, Model model){
        String loginid= principal.getName();
        UserDTO userDTO=this.userService.getUserDto(loginid);

        model.addAttribute("updateUser",userDTO);
        userUpdateForm.setUsername(userDTO.getUsername());
        userUpdateForm.setEmail(userDTO.getUseremail());
        userUpdateForm.setPhone(userDTO.getUserphone());
        userUpdateForm.setUserid(userDTO.getUserid());
        return "MyPage.html";
    }

    @PostMapping("/update")
    public String userupdateForm(@Valid UserUpdateForm userUpdateForm,Principal principal,Model model){
        String loginid= principal.getName();
        UserDTO userDTO=userService.getUserDto(loginid);

        model.addAttribute("updateUser",userDTO);
        this.userService.modify(userDTO,userUpdateForm.getUsername(),userUpdateForm.getPhone(),userUpdateForm.getEmail());
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String userdelete(Principal principal,Model model){
        String loginid= principal.getName();
        UserDTO userDTO=this.userService.getUserDto(loginid);

        User user = this.userService.getUser(loginid);
        List<Rental> rentals = rentalService.findRentalsByUser(user);

        if(rentals != null){
            for(int i =0; i < rentals.size(); i++){
                Rental rental = rentals.get(i);
                this.rentalService.delete(rental);
            }
        }

        model.addAttribute("deleteUser",userDTO);

        this.userService.delete(userDTO);
        return "redirect:/";
    }

    @RequestMapping("/home")
    public String usermain(Principal principal){
        String loginid= principal.getName();
        UserDTO userDto=userService.getUserDto(loginid);

        if(userDto.getRole().getRoleid().equals("1")){
            return "redirect:/adminmainpage";
        }
        return "home.html";
    }

    @GetMapping("/adminmainpage")
    public String adminmain(Principal principal,Model model){
        String loginid= principal.getName();
        UserDTO userDto=userService.getUserDto(loginid);

        long usercount = userRepository.count();
        long questioncount=questionRepository.count();
        long rentalcount=rentalrepository.count();

        model.addAttribute("userCount", usercount);
        model.addAttribute("questionCount", questioncount);
        model.addAttribute("rentalCount", rentalcount);

        if(userDto.getRole().getRoleid().equals("1"))
            return "adminmainpage.html";

        return loginid;
    }

    @GetMapping("/user")
    public String usermain2(RentalCreateForm rentalCreateForm, Principal principal, Model model){
        String loginid= principal.getName();
        UserDTO userDto=userService.getUserDto(loginid);
        List<Place> places=placeService.findPlaces();
        model.addAttribute("places",places);

        return "user.html";
    }

    @GetMapping("/admin/userinfo")
    public String userinfo(Model model){
        List<User> users=userService.findUsers();
        model.addAttribute("users",users);
        return "userinfo.html";
    }

    @GetMapping("/admin/userinfo/delete/{userid}")
    public String Admintouserdelete(@PathVariable("userid") String userid){
        UserDTO userDTO=this.userService.getUserDto(userid);
        User user = this.userService.getUser(userid);
        List<Rental> rentals = rentalService.findRentalsByUser(user);

        if(rentals != null){
            for(int i =0; i < rentals.size(); i++){
                Rental rental = rentals.get(i);
                this.rentalService.delete(rental);
            }
        }
        this.userService.delete(userDTO);
        return "redirect:/adminmainpage";
    }
    @GetMapping("/admin/updateuserinfo/{userid}")
    public String adminuserupdate(UserUpdateForm userUpdateForm,@PathVariable("userid") String userid,Model model){
        UserDTO userDTO=this.userService.getUserDto(userid);
        model.addAttribute("updateUsers",userDTO);
        return "adminuserupdate.html";
    }
    @PostMapping("/admin/updateuserinfo/{userid}")
    public String adminuserupdating(@Valid UserUpdateForm userUpdateForm,@PathVariable("userid") String userid,Model model){
        UserDTO userDTO = this.userService.getUserDto(userid);
        model.addAttribute("updateUsers",userDTO);
        this.userService.modify(userDTO, userUpdateForm.getUsername(),userUpdateForm.getPhone(),userUpdateForm.getEmail());
        return "redirect:/adminmainpage";
    }
}