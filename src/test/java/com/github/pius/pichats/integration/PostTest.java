//package com.github.pius.pichats.integration;
//
//import com.github.pius.pichats.controller.PostController;
//import com.github.pius.pichats.model.Post;
//import com.github.pius.pichats.model.User;
//import com.github.pius.pichats.repository.PostRepository;
//import org.hamcrest.core.Is;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static com.github.pius.pichats.integration.JsonString.jsonString;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class PostTest {
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @Autowired
//  private PostRepository postRepository;
//
//  @Autowired
//  private PostController postController;
//
//  @Autowired
//  private UserDetails userDetails;
//
//  @Test
//  public void contextLoadsPostController() {
//    assertThat(postController).isNotNull();
//  }
//
//  @Test
//  public void makeAPost() throws Exception {
////    User user = userDetails.createUser("user@mail.com");
//    Post post = new Post();
//    post.setPost("new post.jpg");
//    post.setCaption("nice picture");
//    post.setUrl("url.com");
//    postRepository.save(post);
//
//    String token = userDetails.getLoggedInToken("user@mail.com");
//
//    this.mockMvc
//      .perform(post("/post").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(jsonString(post)))
//      .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
//      .andExpect(jsonPath("$.message", Is.is("A post has been made successfully by a user")))
//      .andExpect(jsonPath("$.status", Is.is("CREATED"))).andExpect(content().json("{}"));
//  }
//}
