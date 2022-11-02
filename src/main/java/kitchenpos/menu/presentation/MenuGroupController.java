package kitchenpos.menu.presentation;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.presentation.dto.request.MenuGroupRequest;
import kitchenpos.menu.presentation.dto.response.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupController {
    private final MenuGroupService menuGroupService;

    public MenuGroupController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody MenuGroupRequest menuGroupRequest) {

        final MenuGroup created = menuGroupService.create(menuGroupRequest);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        final MenuGroupResponse menuGroupResponse = MenuGroupResponse.from(created);

        return ResponseEntity.created(uri)
                .body(menuGroupResponse);
    }

    @GetMapping("/api/menu-groups")
    @ResponseStatus(OK)
    public List<MenuGroupResponse> list() {

        final List<MenuGroup> menuGroups = menuGroupService.list();

        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(toList());
    }
}