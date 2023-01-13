import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class One {

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
