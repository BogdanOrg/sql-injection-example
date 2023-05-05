import java.sql.PreparedStatement;
import java.sql.ResultSet;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flag;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Example extends AssignmentEndpoint {
  
 private final LessonDataSource dataSource;  
  
  public Example(LessonDataSource dataSource) {
    this.dataSource = dataSource;
  }
  
  @PostMapping("/challenge/5")
  @ResponseBody
  public AttackResult login(
      @RequestParam String username_login, @RequestParam String password_login) throws Exception {
    if (!StringUtils.hasText(username_login) || !StringUtils.hasText(password_login)) {
      return failed(this).feedback("required4").build();
    }
    if (!"Larry".equals(username_login)) {
      return failed(this).feedback("user.not.larry").feedbackArgs(username_login).build();
    }
    try (var connection = dataSource.getConnection()) {
      PreparedStatement statement =
          connection.prepareStatement(
              "select password from challenge_users where userid = '"
                  + username_login
                  + "' and password = '"
                  + password_login
                  + "'");
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        return success(this).feedback("challenge.solved").feedbackArgs(Flag.FLAGS.get(5)).build();
      } else {
        return failed(this).feedback("challenge.close").build();
      }
    }
  }
}
