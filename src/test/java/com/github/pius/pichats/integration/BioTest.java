//package com.github.pius.pichats.integration;
//
//import com.github.pius.pichats.controller.PostController;
//import com.github.pius.pichats.controller.UserController;
//import com.github.pius.pichats.model.Bio;
//import com.github.pius.pichats.model.EmailVerification;
//import com.github.pius.pichats.model.Post;
//import com.github.pius.pichats.model.User;
//import com.github.pius.pichats.repository.BioRepository;
//import com.github.pius.pichats.repository.PostRepository;
//import com.github.pius.pichats.repository.UserRepository;
//import com.github.pius.pichats.security.JwtProvider;
//import org.hamcrest.core.Is;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.LocalDateTime;
//
//import static com.github.pius.pichats.integration.JsonString.jsonString;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class BioTest {
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @Autowired
//  private BioRepository bioRepository;
//
//  @Autowired
//  private UserRepository userRepository;
//
//  @Autowired
//  private UserController userController;
//
//  @Autowired
//  private JwtProvider jwtProvider;
//
//  @Autowired
//  private PasswordEncoder passwordEncoder;
//
//  @Test
//  public void contextLoadsPostController() {
//    assertThat(userController).isNotNull();
//  }
//
//  @Test
//  public void createUserBio() throws Exception {
//    User user = new User();
//    user.setPassword(passwordEncoder.encode("password"));
//    user.setFirstName("firstname");
//    user.setLastName("lastname");
//    user.setUsername("username");
//    user.setEmail("user@mail.com");
//    userRepository.save(user);
//
//    Bio bio = new Bio();
//    bio.setGender("male");
//    bio.setUser(user);
//
//    String token = jwtProvider.createToken(user.getEmail());
//
//    this.mockMvc
//      .perform(post("/bio").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(jsonString(bio)))
//      .andExpect(status().isCreated())
//      .andExpect(jsonPath("$.message", Is.is("A post has been made successfully by a user")))
//      .andExpect(jsonPath("$.status", Is.is("CREATED"))).andExpect(content().json("{}"));
//  }
//}
