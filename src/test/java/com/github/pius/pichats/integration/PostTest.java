package com.github.pius.pichats.integration;

import com.github.pius.pichats.model.Post;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.pius.pichats.integration.JsonString.jsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PostTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserDetails userDetails;

  @Test
  public void makeAPost() throws Exception {
    Post post = new Post();
    post.setId(3L);
    post.setPost("new post.jpg");
    post.setCaption("nice picture");

    String token = userDetails.getLoggedInToken("user@mail.com");

    this.mockMvc
      .perform(post("/post").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(jsonString(post)))
      .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message", Is.is("A post has been made successfully by a user")))
      .andExpect(jsonPath("$.status", Is.is("CREATED"))).andExpect(content().json("{}"));
  }
}
