package dev.umang.userauthservice.controllers;

import dev.umang.userauthservice.dtos.LoginRequestDTO;
import dev.umang.userauthservice.dtos.SignupRequestDTO;
import dev.umang.userauthservice.dtos.UserDTO;
import dev.umang.userauthservice.dtos.ValidateTokenDto;
import dev.umang.userauthservice.models.User;
import dev.umang.userauthservice.pojos.UserToken;
import dev.umang.userauthservice.services.IAuthService;
import org.antlr.v4.runtime.misc.MultiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    /*
    bunch of APIs
    1. /signup
        -Type: POST
        -Request type
            -name
            -email
            -password
        -Return type
            -ResponseEntity
            -body: UserDTO
                -UserDTO
                    -name, email
            -200 if registration is success
            -error code if it fails
         -why ResponseEntity<UserDTO> instead of ResponseEntity<User>?
    2. login
        -Type: POST
        -Return type
            -In response, headers
            -ResponseEntity<UserDTO>
                -headers(JWT)
            -name, email, roles
         -Request type
            -email, password

    /login GET
    -you're generating a token as a result of this call and the name
    suggests that you are only feteching some info
    -{
        "username":
        "password":
      }
      Request body / payload
     */

    @Autowired
    private IAuthService authService;


    @PostMapping("/signup")
    ResponseEntity<UserDTO> signup(@RequestBody SignupRequestDTO signupRequestDTO){
        System.out.println("Signup API called with name: " + signupRequestDTO.getName() + ", email: " + signupRequestDTO.getEmail());
        try {
            User user = authService.signup(signupRequestDTO.getName(),
                    signupRequestDTO.getEmail(),
                    signupRequestDTO.getPassword());

            UserDTO userDTO = user.convertToUserDTO();

            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);

        }catch (Exception e){
            return null;
        }

    }

    @PostMapping("/login")
    ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        try{
            UserToken userToken = authService.login(loginRequestDTO.getEmail(),
                    loginRequestDTO.getPassword());
            /*
            How do we return the token in response?
            We will add token into headers and we can easily set headers in ResponseEntity

            MultiValueMap is used for representing headers and the key names here
            should be key against which we will add this token?

            The token i want to send back to client should in form of what in frontend??
            ---Cookies
             */

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            /*
            cookie, token
            cookie, session id
            cookie, something else...
             */
            headers.add(HttpHeaders.COOKIE, userToken.getToken());

            HttpHeaders httpHeaders = new HttpHeaders(headers);

            return new ResponseEntity<>(userToken.getUser().convertToUserDTO(),
                    httpHeaders,
                    HttpStatus.OK);

        } catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            //return null;
        }

        /*
        Input : Token
        Output: boolean
        Type: POST because we will send token in request body


         */




    }
    @PostMapping("/validateToken")
    public ResponseEntity<String> validateToken(@RequestBody ValidateTokenDto validateTokenDto) {
        Boolean result = authService.validateToken(validateTokenDto.getToken());

        if(result == false) {
            return new ResponseEntity<>("Please login again, Inconvenience Regretted", HttpStatus.FORBIDDEN);
            //throw new RuntimeException("Please login again, Inconvenience Regretted");
        }else{
            return new ResponseEntity<>("Token is valid", HttpStatus.OK);
        }
    }





    /*
    Write a controller advice to catch and handle exceptions.
     */
}
