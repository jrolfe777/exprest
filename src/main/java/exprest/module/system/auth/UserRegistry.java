package exprest.module.system.auth;

public interface UserRegistry {
   User findUser(String key);
}
