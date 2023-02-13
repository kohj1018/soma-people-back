package kr.somapeople.somapeopleback.web.users;

import kr.somapeople.somapeopleback.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UsersApiController {

    private final UsersService usersService;
}
